package com.snake.utils;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Handles all game sounds
 */
public class SoundManager {
    
    private Clip eatClip;
    private Clip gameOverClip;
    private Clip clickClip;
    private Clip backgroundClip;

    // Volumen settings (0.0 to 1.0)
    private float musicVolume = 0.8f;
    private float sfxVolume = 0.8f;
    private boolean muted = false;

    // Singleton instance (only one SoundManager exists)
    private static SoundManager instance;

    /**
     * Private constructor - use getInstance() instead
     */
    private SoundManager() {
        loadSounds();
    }

    /**
     * Gets the single instance of SoundManager
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads all sounds files
     */
    private void loadSounds() {
        eatClip = loadClip("/sounds/eat_sound.wav");
        gameOverClip = loadClip("/sounds/game_over_sound.wav");
        clickClip = loadClip("/sounds/click_sound.wav");
        backgroundClip = loadClip("/sounds/background_sound.wav");

    }

    /**
     * Loads a single sound file
     */
    private Clip loadClip(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.out.println("Sound file not found: " + path);
                return null;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;

        } catch (Exception e) {
            System.out.println("Error loading sound: " + path);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets volume on a clip (0.0 to 1.0)
     */
    private void setClipVolume(Clip clip, float volume) {
        if (clip == null) return;

        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Convert 0.0-1.0 to decibels
            // 0.0 = -80dB (silent), 1.0 = 0db (full volume)
            float dB;
            if (volume <= 0.0f) {
                dB = -80.0f;
            } else {
                dB = 20.0f * (float) Math.log(volume);
                // Clamp to valid range
                dB = Math.max(dB, -80.0f);
                dB = Math.min(dB, 6.0f);
            }

            gainControl.setValue(dB);
        } catch (Exception e) {
            System.out.println("Could not set volume: " + e.getMessage());
        }
    }

    /**
     * Plays a sound effect clip
     */
    private void playClip(Clip clip) {
        if (clip != null && !muted) {
            setClipVolume(clip, sfxVolume);
            clip.stop();    // Stop if already playing
            clip.setFramePosition(0);   // Rewind to start
            clip.start();   // Play the sound
        }
    }

    // Public methods to play sounds
    public void playEat() {
        playClip(eatClip);
    }

    public void playGameOver() {
        playClip(gameOverClip);
    }

    public void playClick() {
        playClip(clickClip);
    }

    /**
     * Plays background music on loop
     */
    public void playBackgroundLoop() {
        if (backgroundClip != null && !muted) {
            setClipVolume(backgroundClip, musicVolume);
            backgroundClip.stop();
            backgroundClip.setFramePosition(0);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loops forever
        }
    }

    /**
     * Stops background music
     */
    public void stopBackground() {
        if (backgroundClip != null) {
            backgroundClip.stop();
        }
    }

    // Volume Control methods

    /**
     * Sets music volume (0.0 to 1.0)
     */
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));

        // Update background music if playing
        if (backgroundClip != null && backgroundClip.isRunning()) {
            setClipVolume(backgroundClip, musicVolume);
        }
    }

    /**
     * Sets sounds effects volume (0.0 to 1.0)
     */
    public void setSfxVolume(float volume) {
        this.sfxVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }

    /**
     * Toggles mute on/off
     */
    public void toggleMute() {
        muted = !muted;

        if (muted) {
            stopBackground();
        }
    }

    /**
     * Sets mute state
     */
    public void setMuted(boolean muted) {
        this.muted = muted;

        if (muted) {
            stopBackground();
        }
    }

    // GETTERS

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    public boolean isMuted() {
        return muted;
    }
}