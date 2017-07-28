/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.guice.TetrisModule.TetrisProvider;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;
import spypunk.tetris.model.TetrisEvent;
import spypunk.tetris.model.TetrisInstance;

@Singleton
public class TetrisServiceImpl implements TetrisService {

    private static final int ROWS_PER_LEVEL = 10;

    private final ShapeFactory shapeFactory;

    private final Map<Integer, Integer> scorePerRows = ImmutableMap.of(1, 40, 2, 100, 3, 300, 4, 1200);

    private final Map<Integer, Integer> levelSpeeds = createLevelSpeeds();

    private final Tetris tetris;

    private TetrisInstance tetrisInstance;

    @Inject
    public TetrisServiceImpl(final ShapeFactory shapeFactory, @TetrisProvider final Tetris tetris) {
        this.shapeFactory = shapeFactory;
        this.tetris = tetris;
    }

    @Override
    public void start() {
        final Map<ShapeType, Integer> statistics = Lists.newArrayList(ShapeType.values()).stream()
                .collect(Collectors.toMap(shapeType -> shapeType, shapeType -> 0));

        final int speed = getLevelSpeed(0);

        tetrisInstance = TetrisInstance.Builder.instance()
                .setStatistics(statistics).setSpeed(speed).build();

        tetrisInstance.setNextShape(shapeFactory.createRandomShape());

        getNextShape();

        tetris.setTetrisInstance(tetrisInstance);

        tetris.setState(State.RUNNING);
    }

    @Override
    public void update() {
        if (!isTetrisRunning()) {
            return;
        }

        tetrisInstance.setCurrentGravityFrame(tetrisInstance.getCurrentGravityFrame() + 1);

        if (handleNextShape()) {
            if (tetrisInstance.isHardDropEnabled()) {
                handleHardDrop();
            } else if (handleMovement()) {
                handleGravity();
            }
        }
    }

    @Override
    public void pause() {
        tetris.setState(tetris.getState().onPause());
    }

    @Override
    public void triggerMovement(final Movement movement) {
        if (isTetrisRunning() && !tetrisInstance.isCurrentShapeLocked()
                && !tetrisInstance.isHardDropEnabled()) {
            tetrisInstance.setMovement(Optional.of(movement));
        }
    }

    @Override
    public void triggerHardDrop() {
        if (isTetrisRunning() && !tetrisInstance.isCurrentShapeLocked()
                && !tetrisInstance.isHardDropEnabled()) {
            tetrisInstance.setHardDropEnabled(true);
        }
    }

    @Override
    public void mute() {
        tetris.setMuted(!tetris.isMuted());
    }

    private void handleHardDrop() {
        moveShapeDown();
        updateScoreWithCompletedMovement();
    }

    private boolean handleNextShape() {
        if (!tetrisInstance.isCurrentShapeLocked()) {
            return true;
        }

        if (isTimeToHandleGravity()) {
            clearCompleteRows();
            getNextShape();
            checkShapeIsLocked();

            if (!tetrisInstance.isCurrentShapeLocked()) {
                resetCurrentGravityFrame();
            }
        }

        return false;
    }

    private boolean handleMovement() {
        final Optional<Movement> optionalMovement = tetrisInstance.getMovement();

        if (!optionalMovement.isPresent()) {
            return true;
        }

        final Movement movement = optionalMovement.get();

        tetrisInstance.setMovement(Optional.empty());

        final boolean isDownMovement = Movement.DOWN.equals(movement);

        if (isDownMovement || canShapeMove(movement)) {
            moveShape(movement);

            if (isDownMovement) {
                updateScoreWithCompletedMovement();
            }

            return !tetrisInstance.isCurrentShapeLocked();
        }

        return true;
    }

    private void handleGravity() {
        if (!isTimeToHandleGravity()) {
            return;
        }

        moveShapeDown();

        resetCurrentGravityFrame();
    }

    private void checkShapeIsLocked() {
        if (canShapeMove(Movement.DOWN)) {
            return;
        }

        tetrisInstance.getCurrentShape().getBlocks()
                .forEach(block -> tetrisInstance.getBlocks().put(block.getLocation(), block));

        if (isGameOver()) {
            tetris.setState(State.GAME_OVER);
            tetris.getTetrisEvents().add(TetrisEvent.GAME_OVER);
        } else {
            resetCurrentGravityFrame();
            tetris.getTetrisEvents().add(TetrisEvent.SHAPE_LOCKED);
            tetrisInstance.setCurrentShapeLocked(true);
        }

        tetrisInstance.setHardDropEnabled(false);
    }

    private void getNextShape() {
        final Shape currentShape = tetrisInstance.getNextShape();

        tetrisInstance.setCurrentShape(currentShape);
        tetrisInstance.setCurrentShapeLocked(false);
        tetrisInstance.setNextShape(shapeFactory.createRandomShape());

        updateStatistics();
    }

    private void updateStatistics() {
        final ShapeType shapeType = tetrisInstance.getCurrentShape().getShapeType();
        final Map<ShapeType, Integer> statistics = tetrisInstance.getStatistics();
        final Integer count = statistics.get(shapeType);

        statistics.put(shapeType, count + 1);
    }

    private boolean isGameOver() {
        return tetrisInstance.getBlocks().values().stream()
                .anyMatch(block -> block.getLocation().y == 0);
    }

    private boolean isTimeToHandleGravity() {
        return tetrisInstance.getCurrentGravityFrame() > tetrisInstance.getSpeed();
    }

    private void clearCompleteRows() {
        final List<Integer> completeRows = IntStream.range(0, HEIGHT)
                .filter(row -> isRowComplete(row)).boxed().collect(Collectors.toList());

        final int completedRows = completeRows.size();

        if (completedRows == 0) {
            return;
        }

        completeRows.forEach(row -> clearCompleteRow(row));

        tetrisInstance.setCompletedRows(tetrisInstance.getCompletedRows() + completedRows);

        updateScoreWithCompletedRows(completedRows);
        updateLevel();

        tetris.getTetrisEvents().add(TetrisEvent.ROWS_COMPLETED);
    }

    private void updateLevel() {
        final int completedRows = tetrisInstance.getCompletedRows();
        final int nextLevel = tetrisInstance.getLevel() + 1;

        if (completedRows >= ROWS_PER_LEVEL * nextLevel) {
            tetrisInstance.setLevel(nextLevel);

            final int speed = getLevelSpeed(nextLevel);

            tetrisInstance.setSpeed(speed);
        }
    }

    private void updateScoreWithCompletedRows(final int completedRows) {
        final Integer rowsScore = scorePerRows.get(completedRows);
        final int score = tetrisInstance.getScore();

        tetrisInstance.setScore(score + rowsScore * (tetrisInstance.getLevel() + 1));
    }

    private void updateScoreWithCompletedMovement() {
        tetrisInstance.setScore(tetrisInstance.getScore() + 1);
    }

    private void clearCompleteRow(final Integer row) {
        final Map<Point, Block> blocks = tetrisInstance.getBlocks();

        final List<Block> blocksToMoveDown = blocks.values().stream()
                .filter(block -> block.getLocation().y < row)
                .collect(Collectors.toList());

        IntStream.range(0, WIDTH)
                .forEach(column -> clearBlockAt(new Point(column, row)));

        blocksToMoveDown.forEach(block -> clearBlockAt(block.getLocation()));
        blocksToMoveDown.forEach(block -> moveBlockDown(block));
    }

    private void clearBlockAt(final Point location) {
        tetrisInstance.getBlocks().remove(location);
    }

    private boolean isRowComplete(final int row) {
        return IntStream.range(0, WIDTH)
                .allMatch(column -> tetrisInstance.getBlocks().containsKey(new Point(column, row)));
    }

    private void moveShape(final Movement movement) {
        final Shape currentShape = tetrisInstance.getCurrentShape();
        final Shape newShape = movement.apply(currentShape);

        tetrisInstance.setCurrentShape(newShape);

        checkShapeIsLocked();
    }

    private void moveShapeDown() {
        moveShape(Movement.DOWN);
    }

    private void moveBlockDown(final Block block) {
        final Point location = block.getLocation();
        final Point newLocation = Movement.DOWN.apply(location);

        block.setLocation(newLocation);

        tetrisInstance.getBlocks().put(block.getLocation(), block);
    }

    private boolean canShapeMove(final Movement movement) {
        final Shape currentShape = tetrisInstance.getCurrentShape();
        final Shape newShape = movement.apply(currentShape);

        return newShape.getBlocks().stream().allMatch(block -> canBlockMove(block));
    }

    private boolean canBlockMove(final Block block) {
        final Point location = block.getLocation();

        if (location.x < 0 || location.x == WIDTH || location.y < 0 || location.y == HEIGHT) {
            return false;
        }

        final Block nextLocationBlock = tetrisInstance.getBlocks().get(location);

        return nextLocationBlock == null;
    }

    private boolean isTetrisRunning() {
        return tetris.getState().equals(State.RUNNING);
    }

    private void resetCurrentGravityFrame() {
        tetrisInstance.setCurrentGravityFrame(0);
    }

    private static Map<Integer, Integer> createLevelSpeeds() {
        final int initialSpeed = 48;

        final Map<Integer, Integer> levelSpeeds = Maps.newHashMap();

        levelSpeeds.put(0, initialSpeed);
        levelSpeeds.put(9, 6);

        IntStream.range(1, 9).forEach(level -> levelSpeeds.put(level, initialSpeed - 5 * level));
        IntStream.range(10, 13).forEach(level -> levelSpeeds.put(level, 5));
        IntStream.range(13, 16).forEach(level -> levelSpeeds.put(level, 4));
        IntStream.range(16, 19).forEach(level -> levelSpeeds.put(level, 3));
        IntStream.range(19, 29).forEach(level -> levelSpeeds.put(level, 2));

        return levelSpeeds;
    }

    private int getLevelSpeed(final int level) {
        return level < 29 ? levelSpeeds.get(level) : 1;
    }
}
