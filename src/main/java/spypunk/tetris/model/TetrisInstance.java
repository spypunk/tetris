/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
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

    public enum State {
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
        GAME_OVER {
            @Override
            public State onPause() {
                return this;
            }
        };

        public abstract State onPause();
    }

    public static class Builder {

        private final TetrisInstance tetrisInstance = new TetrisInstance();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setNextShape(Shape nextShape) {
            tetrisInstance.setNextShape(nextShape);
            return this;
        }

        public Builder setStatistics(Map<ShapeType, Integer> statistics) {
            tetrisInstance.setStatistics(statistics);
            return this;
        }

        public Builder setState(State state) {
            tetrisInstance.setState(state);
            return this;
        }

        public Builder setSpeed(int speed) {
            tetrisInstance.setSpeed(speed);
            return this;
        }

        public TetrisInstance build() {
            return tetrisInstance;
        }

    }

    private Map<Point, Block> blocks = Maps.newHashMap();

    private Shape currentShape;

    private Shape nextShape;

    private Optional<Movement> movement = Optional.empty();

    private Map<ShapeType, Integer> statistics;

    private int level;

    private int score;

    private int completedRows;

    private int speed;

    private State state;

    private int currentGravityFrame = 0;

    public Map<Point, Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(Map<Point, Block> blocks) {
        this.blocks = blocks;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public Shape getNextShape() {
        return nextShape;
    }

    public void setNextShape(Shape nextShape) {
        this.nextShape = nextShape;
    }

    public Optional<Movement> getMovement() {
        return movement;
    }

    public void setMovement(Optional<Movement> movement) {
        this.movement = movement;
    }

    public Map<ShapeType, Integer> getStatistics() {
        return statistics;
    }

    public void setStatistics(Map<ShapeType, Integer> statistics) {
        this.statistics = statistics;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCompletedRows() {
        return completedRows;
    }

    public void setCompletedRows(int completedRows) {
        this.completedRows = completedRows;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getCurrentGravityFrame() {
        return currentGravityFrame;
    }

    public void setCurrentGravityFrame(int currentGravityFrame) {
        this.currentGravityFrame = currentGravityFrame;
    }
}
