/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.service;

import static spypunk.tetris.constants.TetrisConstants.HEIGHT;
import static spypunk.tetris.constants.TetrisConstants.WIDTH;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.ImmutableMap;

import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;

@Singleton
public class TetrisServiceImpl implements TetrisService {

    private static final int MAX_START_X = 7;

    private static final int ROWS_PER_LEVEL = 10;

    private static final Map<Integer, Integer> SCORE_PER_ROWS = ImmutableMap.of(1, 40, 2, 100, 3, 300, 4, 1200);

    private final Random random = new Random();

    @Inject
    private ShapeFactory shapeFactory;

    @Override
    public void update(Tetris tetris) {
        if (!isTetrisRunning(tetris) || !handleNextShape(tetris) || !handleMovement(tetris)
                || !isTimeToHandleGravity(tetris)) {
            return;
        }

        handleGravity(tetris);
    }

    @Override
    public void pause(Tetris tetris) {
        if (isTetrisRunning(tetris)) {
            tetris.setState(State.PAUSED);
        } else if (isTetrisPaused(tetris)) {
            tetris.setState(State.RUNNING);
        }
    }

    @Override
    public void updateMovement(Tetris tetris, Optional<Movement> movement) {
        if (isTetrisRunning(tetris)) {
            tetris.setMovement(movement);
        }
    }

    private boolean handleNextShape(Tetris tetris) {
        if (tetris.getCurrentShape() != null) {
            return true;
        }

        if (!isTimeForNextShape(tetris)) {
            return false;
        }

        clearCompleteRows(tetris);

        getNextShape(tetris);

        return !checkShapeIsLocked(tetris);
    }

    private boolean handleMovement(Tetris tetris) {
        final Optional<Movement> movement = tetris.getMovement();

        return !movement.isPresent() || handleMovement(tetris, movement.get());
    }

    private boolean handleMovement(Tetris tetris, Movement movement) {
        if (Movement.DOWN.equals(movement) || canShapeMove(tetris, movement)) {
            return moveShape(tetris, movement);
        }

        return true;
    }

    private void handleGravity(Tetris tetris) {
        moveShape(tetris, Movement.DOWN);

        tetris.setLastMoveTime(now());
    }

    private boolean checkShapeIsLocked(Tetris tetris) {
        if (canShapeMove(tetris, Movement.DOWN)) {
            return false;
        }

        if (isGameOver(tetris)) {
            tetris.setState(State.GAME_OVER);
        } else {
            tetris.setCurrentShape(null);
            tetris.setLastLockedTime(now());
        }

        return true;
    }

    private void getNextShape(Tetris tetris) {
        final Shape currentShape = tetris.getNextShape();

        tetris.setCurrentShape(currentShape);

        randomizeShapeStartX(currentShape);

        currentShape.getBlocks()
                .forEach(block -> tetris.getBlocks().put(block.getLocation(), Optional.of(block)));

        tetris.setNextShape(shapeFactory.createRandomShape());
        tetris.setLastMoveTime(now());

        updateStatistics(tetris);
    }

    private void randomizeShapeStartX(Shape shape) {
        final int dx = random.nextInt(MAX_START_X);

        shape.getBoundingBox().translate(dx, 0);

        shape.getBlocks()
                .forEach(block -> block.getLocation().translate(dx, 0));
    }

    private void updateStatistics(Tetris tetris) {
        final ShapeType shapeType = tetris.getCurrentShape().getShapeType();
        final Map<ShapeType, Integer> statistics = tetris.getStatistics();
        final Integer count = statistics.get(shapeType);

        statistics.put(shapeType, count + 1);
    }

    private boolean isGameOver(Tetris tetris) {
        return tetris.getBlocks().values().stream()
                .anyMatch(block -> block.isPresent() && block.get().getLocation().y == 2);
    }

    private boolean isTimeToHandleGravity(Tetris tetris) {
        return isTimeTo(tetris, tetris.getLastMoveTime());
    }

    private boolean isTimeForNextShape(Tetris tetris) {
        return isTimeTo(tetris, tetris.getLastLockedTime());
    }

    private boolean isTimeTo(Tetris tetris, long lastTime) {
        return now() - lastTime > tetris.getSpeed();
    }

    private void clearCompleteRows(Tetris tetris) {
        final List<Integer> completeRows = IntStream.range(2, HEIGHT)
                .filter(row -> isRowComplete(tetris, row)).boxed().collect(Collectors.toList());

        if (completeRows.isEmpty()) {
            return;
        }

        final int completedRows = completeRows.size();

        completeRows.forEach(row -> clearCompleteRow(tetris, row));

        tetris.setCompletedRows(tetris.getCompletedRows() + completedRows);

        updateScore(tetris, completedRows);
        updateLevel(tetris);
    }

    private void updateLevel(Tetris tetris) {
        final int completedRows = tetris.getCompletedRows();
        final int nextLevel = tetris.getLevel() + 1;

        if (completedRows >= ROWS_PER_LEVEL * nextLevel) {
            final int speed = tetris.getSpeed();

            tetris.setLevel(nextLevel);
            tetris.setSpeed(speed - speed / 6);
        }
    }

    private void updateScore(Tetris tetris, int completedRows) {
        final Integer rowsScore = SCORE_PER_ROWS.get(completedRows);
        final int score = tetris.getScore();

        tetris.setScore(score + rowsScore * (tetris.getLevel() + 1));
    }

    private void clearCompleteRow(Tetris tetris, Integer row) {
        final Map<Point, Optional<Block>> blocks = tetris.getBlocks();
        final List<Block> blocksToMoveDown = blocks.values().stream()
                .filter(block -> block.isPresent() && block.get().getLocation().y < row).map(Optional::get)
                .collect(Collectors.toList());

        IntStream.range(0, WIDTH)
                .forEach(column -> clearBlockAt(tetris, new Point(column, row)));

        blocksToMoveDown.forEach(block -> clearBlockAt(tetris, block.getLocation()));
        blocksToMoveDown.forEach(block -> moveBlockDown(tetris, block));
    }

    private void clearBlockAt(Tetris tetris, Point location) {
        tetris.getBlocks().put(location, Optional.empty());
    }

    private boolean isRowComplete(Tetris tetris, int row) {
        final Map<Point, Optional<Block>> blocks = tetris.getBlocks();

        return IntStream.range(0, WIDTH).mapToObj(column -> blocks.get(new Point(column, row)))
                .allMatch(Optional::isPresent);
    }

    private boolean moveShape(Tetris tetris, Movement movement) {
        final Shape currentShape = tetris.getCurrentShape();
        final Shape newShape = movement.apply(currentShape);

        currentShape.getBlocks().forEach(block -> tetris.getBlocks().put(block.getLocation(), Optional.empty()));

        newShape.getBlocks().forEach(block -> tetris.getBlocks().put(block.getLocation(), Optional.of(block)));

        tetris.setCurrentShape(newShape);

        if (checkShapeIsLocked(tetris)) {
            return false;
        }

        return true;
    }

    private void moveBlockDown(Tetris tetris, Block block) {
        final Point location = block.getLocation();
        final Point newLocation = Movement.DOWN.apply(location);

        block.setLocation(newLocation);

        tetris.getBlocks().put(block.getLocation(), Optional.of(block));
    }

    private boolean canShapeMove(Tetris tetris, Movement movement) {
        final Shape currentShape = tetris.getCurrentShape();
        final Shape newShape = movement.apply(currentShape);

        return newShape.getBlocks().stream().allMatch(block -> canBlockMove(tetris, block));
    }

    private boolean canBlockMove(Tetris tetris, Block block) {
        final Point location = block.getLocation();

        if (location.x < 0 || location.x == WIDTH || location.y == HEIGHT) {
            return false;
        }

        final Optional<Block> nextLocationBlock = tetris.getBlocks().get(location);

        if (nextLocationBlock.isPresent() && !nextLocationBlock.get().getShape().equals(tetris.getCurrentShape())) {
            return false;
        }

        return true;
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private boolean isTetrisRunning(Tetris tetris) {
        return isTetrisState(tetris, State.RUNNING);
    }

    private boolean isTetrisPaused(Tetris tetris) {
        return isTetrisState(tetris, State.PAUSED);
    }

    private boolean isTetrisState(Tetris tetris, State state) {
        return state.equals(tetris.getState());
    }
}
