package com.nevergarden.myna.display;

/**
 * Interface for every frame by frame animatable display object.
 */
public interface Animatable {
    /** Plays the animation. */
    void play();
    /** Stops the animation. */
    void stop();

    /**
     * Checks if animation is loop.
     */
    Boolean isLoop();

    /**
     * Set looping value.
     */
    void setLooping(Boolean isLoop);

    /**
     * Get frame count updated per second.
     */
    Integer getFrameRate();

    /**
     * Sets frame counts updated per second.
     */
    void setFrameRate(Integer frameRate);

    /**
     * Total animation frames.
     */
    Integer getFrameCount();
}
