/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.awt.Point;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

public class TetrisInstance {

    private Map<Point, Block> blocks = Maps.newHashMap();

    private Shape currentShape;

    private Shape nextShape;

    private Optional<Movement> movement = Optional.empty();

    private Map<ShapeType, Integer> statistics;

    private int level;

    private int score;

    private int completedRows;

    private int speed;

    private int currentGravityFrame;

    private int currentMovementScore;

    private boolean currentShapeLocked;

    private boolean hardDropEnabled;

    public static final class Builder {

        private final TetrisInstance tetrisInstance = new TetrisInstance();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setNextShape(final Shape nextShape) {
            tetrisInstance.setNextShape(nextShape);
            return this;
        }

        public Builder setStatistics(final Map<ShapeType, Integer> statistics) {
            tetrisInstance.setStatistics(statistics);
            return this;
        }

        public Builder setSpeed(final int speed) {
            tetrisInstance.setSpeed(speed);
            return this;
        }

        public TetrisInstance build() {
            return tetrisInstance;
        }

    }

    public Map<Point, Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(final Map<Point, Block> blocks) {
        this.blocks = blocks;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(final Shape currentShape) {
        this.currentShape = currentShape;
    }

    public Shape getNextShape() {
        return nextShape;
    }

    public void setNextShape(final Shape nextShape) {
        this.nextShape = nextShape;
    }

    public Optional<Movement> getMovement() {
        return movement;
    }

    public void setMovement(final Optional<Movement> movement) {
        this.movement = movement;
    }

    public Map<ShapeType, Integer> getStatistics() {
        return statistics;
    }

    public void setStatistics(final Map<ShapeType, Integer> statistics) {
        this.statistics = statistics;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public int getCompletedRows() {
        return completedRows;
    }

    public void setCompletedRows(final int completedRows) {
        this.completedRows = completedRows;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(final int speed) {
        this.speed = speed;
    }

    public int getCurrentGravityFrame() {
        return currentGravityFrame;
    }

    public void setCurrentGravityFrame(final int currentGravityFrame) {
        this.currentGravityFrame = currentGravityFrame;
    }

    public int getCurrentMovementScore() {
        return currentMovementScore;
    }

    public void setCurrentMovementScore(final int currentMovementScore) {
        this.currentMovementScore = currentMovementScore;
    }

    public boolean isCurrentShapeLocked() {
        return currentShapeLocked;
    }

    public void setCurrentShapeLocked(final boolean currentShapeLocked) {
        this.currentShapeLocked = currentShapeLocked;
    }

    public boolean isHardDropEnabled() {
        return hardDropEnabled;
    }

    public void setHardDropEnabled(final boolean hardDropEnabled) {
        this.hardDropEnabled = hardDropEnabled;
    }
}
