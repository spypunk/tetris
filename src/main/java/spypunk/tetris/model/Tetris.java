package spypunk.tetris.model;

import java.awt.Point;
import java.util.Map;
import java.util.Optional;

public class Tetris {

    public static class Builder {

        private final Tetris tetris = new Tetris();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setBlocks(Map<Point, Optional<Block>> blocks) {
            tetris.setBlocks(blocks);
            return this;
        }

        public Builder setNextShape(Shape nextShape) {
            tetris.setNextShape(nextShape);
            return this;
        }

        public Builder setStatistics(Map<ShapeType, Integer> statistics) {
            tetris.setStatistics(statistics);
            return this;
        }

        public Builder setSpeed(int speed) {
            tetris.setSpeed(speed);
            return this;
        }

        public Tetris build() {
            return tetris;
        }

    }

    private Map<Point, Optional<Block>> blocks;

    private Shape currentShape;

    private Shape nextShape;

    private boolean gameOver;

    private long lastMoveTime;

    private long lastLockedTime;

    private Optional<Movement> movement;

    private Map<ShapeType, Integer> statistics;

    private int level;

    private int score;

    private int completedRows;

    private int speed;

    public Map<Point, Optional<Block>> getBlocks() {
        return blocks;
    }

    public void setBlocks(Map<Point, Optional<Block>> blocks) {
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

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
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

    public long getLastLockedTime() {
        return lastLockedTime;
    }

    public void setLastLockedTime(long lastLockedTime) {
        this.lastLockedTime = lastLockedTime;
    }
}
