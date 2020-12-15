/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.awt.Point;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import spypunk.tetris.model.Shape.Block;

public class Tetris {

    public enum State {
        STOPPED,
        RUNNING {
            @Override
            public State onPause() {
                return PAUSED;
            }
        },
        PAUSED {
            @Override
            public State onPause() {
                return RUNNING;
            }
        },
        GAME_OVER;

        public State onPause() {
            return this;
        }
    }

    private final String name;

    private final String version;

    private final URI projectURI;

    private TetrisInstance tetrisInstance;

    private boolean muted;

    public Tetris(final String name, final String version, final URI projectURI) {
        this.name = name;
        this.version = version;
        this.projectURI = projectURI;

        tetrisInstance = new TetrisInstance();
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public URI getProjectURI() {
        return projectURI;
    }

    public void setTetrisInstance(final TetrisInstance tetrisInstance) {
        this.tetrisInstance = tetrisInstance;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(final boolean muted) {
        this.muted = muted;
    }

    public Map<Point, Block> getBlocks() {
        return tetrisInstance.getBlocks();
    }

    public Shape getCurrentShape() {
        return tetrisInstance.getCurrentShape();
    }

    public void setCurrentShape(final Shape currentShape) {
        tetrisInstance.setCurrentShape(currentShape);
    }

    public Shape getNextShape() {
        return tetrisInstance.getNextShape();
    }

    public void setNextShape(final Shape nextShape) {
        tetrisInstance.setNextShape(nextShape);
    }

    public Optional<Movement> getMovement() {
        return tetrisInstance.getMovement();
    }

    public void setMovement(final Optional<Movement> movement) {
        tetrisInstance.setMovement(movement);
    }

    public Map<ShapeType, Integer> getStatistics() {
        return tetrisInstance.getStatistics();
    }

    public int getLevel() {
        return tetrisInstance.getLevel();
    }

    public void setLevel(final int level) {
        tetrisInstance.setLevel(level);
    }

    public int getScore() {
        return tetrisInstance.getScore();
    }

    public void setScore(final int score) {
        tetrisInstance.setScore(score);
    }

    public int getHighScore() { return tetrisInstance.getHighScore(); }

    public void setHighScore(final int highScore) { tetrisInstance.setHighScore(highScore); }


    public int getCompletedRows() {
        return tetrisInstance.getCompletedRows();
    }

    public void setCompletedRows(final int completedRows) {
        tetrisInstance.setCompletedRows(completedRows);
    }

    public int getSpeed() {
        return tetrisInstance.getSpeed();
    }

    public void setSpeed(final int speed) {
        tetrisInstance.setSpeed(speed);
    }

    public int getCurrentGravityFrame() {
        return tetrisInstance.getCurrentGravityFrame();
    }

    public void setCurrentGravityFrame(final int currentGravityFrame) {
        tetrisInstance.setCurrentGravityFrame(currentGravityFrame);
    }

    public int getCurrentMovementScore() {
        return tetrisInstance.getCurrentMovementScore();
    }

    public void setCurrentMovementScore(final int currentMovementScore) {
        tetrisInstance.setCurrentMovementScore(currentMovementScore);
    }

    public boolean isCurrentShapeLocked() {
        return tetrisInstance.isCurrentShapeLocked();
    }

    public void setCurrentShapeLocked(final boolean currentShapeLocked) {
        tetrisInstance.setCurrentShapeLocked(currentShapeLocked);
    }

    public boolean isHardDropEnabled() {
        return tetrisInstance.isHardDropEnabled();
    }

    public void setHardDropEnabled(final boolean hardDropEnabled) {
        tetrisInstance.setHardDropEnabled(hardDropEnabled);
    }

    public State getState() {
        return tetrisInstance.getState();
    }

    public void setState(final State state) {
        tetrisInstance.setState(state);
    }

    public List<TetrisEvent> getTetrisEvents() {
        return tetrisInstance.getTetrisEvents();
    }
}
