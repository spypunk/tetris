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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.TetrisInstance;
import spypunk.tetris.model.TetrisInstance.State;

@Singleton
public class TetrisServiceImpl implements TetrisService {

    private static final int MAX_START_X = 7;

    private static final int ROWS_PER_LEVEL = 10;

    private final Map<Integer, Integer> scorePerRows = ImmutableMap.of(1, 40, 2, 100, 3, 300, 4, 1200);

    private final Map<Integer, Integer> levelSpeeds = Maps.newHashMap();

    private final Random random = new Random();

    @Inject
    private ShapeFactory shapeFactory;

    public TetrisServiceImpl() {
        initializeLevelSpeeds();
    }

    @Override
    public void newGame(Tetris tetris) {
        final Map<Point, Optional<Block>> blocks = Maps.newHashMap();

        IntStream.range(0, WIDTH).forEach(x -> IntStream.range(0, HEIGHT)
                .forEach(y -> blocks.put(new Point(x, y), Optional.empty())));

        final Map<ShapeType, Integer> statistics = Lists.newArrayList(ShapeType.values()).stream()
                .collect(Collectors.toMap(shapeType -> shapeType, shapeType -> 0));

        final int speed = getSpeed(0);

        final TetrisInstance tetrisInstance = TetrisInstance.Builder.instance().setBlocks(blocks)
                .setNextShape(shapeFactory.createRandomShape())
                .setStatistics(statistics).setState(State.RUNNING)
                .setSpeed(speed).build();

        tetris.setTetrisInstance(tetrisInstance);
    }

    @Override
    public void update(Tetris tetris) {
        final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

        if (!isTetrisInstanceRunning(tetrisInstance)) {
            return;
        }

        tetrisInstance.setCurrentGravityFrame(tetrisInstance.getCurrentGravityFrame() + 1);

        if (handleNextShape(tetrisInstance) && handleMovement(tetrisInstance)) {
            handleGravity(tetrisInstance);
        }
    }

    @Override
    public void pause(Tetris tetris) {
        final TetrisInstance tetrisInstance = tetris.getTetrisInstance();
        tetrisInstance.setState(tetrisInstance.getState().onPause());
    }

    @Override
    public void updateMovement(Tetris tetris, Movement movement) {
        final TetrisInstance tetrisInstance = tetris.getTetrisInstance();

        if (isTetrisInstanceRunning(tetrisInstance) && tetrisInstance.getCurrentShape() != null) {
            tetrisInstance.setMovement(Optional.of(movement));
        }
    }

    private void initializeLevelSpeeds() {
        levelSpeeds.put(0, 48);
        levelSpeeds.put(1, 43);
        levelSpeeds.put(2, 38);
        levelSpeeds.put(3, 33);
        levelSpeeds.put(4, 28);
        levelSpeeds.put(5, 23);
        levelSpeeds.put(6, 18);
        levelSpeeds.put(7, 13);
        levelSpeeds.put(8, 8);
        levelSpeeds.put(9, 6);

        IntStream.range(10, 13).forEach(level -> levelSpeeds.put(level, 5));
        IntStream.range(13, 16).forEach(level -> levelSpeeds.put(level, 4));
        IntStream.range(16, 19).forEach(level -> levelSpeeds.put(level, 3));
        IntStream.range(19, 29).forEach(level -> levelSpeeds.put(level, 2));
    }

    private boolean handleNextShape(TetrisInstance tetrisInstance) {
        if (tetrisInstance.getCurrentShape() != null) {
            return true;
        }

        if (isTimeToHandleGravity(tetrisInstance)) {
            clearCompleteRows(tetrisInstance);
            getNextShape(tetrisInstance);
            resetCurrentGravityFrame(tetrisInstance);
            checkShapeIsLocked(tetrisInstance);
        }

        return false;
    }

    private boolean handleMovement(TetrisInstance tetrisInstance) {
        final Optional<Movement> optionalMovement = tetrisInstance.getMovement();

        if (!optionalMovement.isPresent()) {
            return true;
        }

        final Movement movement = optionalMovement.get();

        tetrisInstance.setMovement(Optional.empty());

        if (Movement.DOWN.equals(movement) || canShapeMove(tetrisInstance, movement)) {
            return moveShape(tetrisInstance, movement);
        }

        return true;
    }

    private void handleGravity(TetrisInstance tetrisInstance) {
        if (!isTimeToHandleGravity(tetrisInstance)) {
            return;
        }

        moveShape(tetrisInstance, Movement.DOWN);

        resetCurrentGravityFrame(tetrisInstance);
    }

    private boolean checkShapeIsLocked(TetrisInstance tetrisInstance) {
        if (canShapeMove(tetrisInstance, Movement.DOWN)) {
            return false;
        }

        if (isGameOver(tetrisInstance)) {
            tetrisInstance.setState(State.GAME_OVER);
        } else {
            tetrisInstance.setCurrentShape(null);
            resetCurrentGravityFrame(tetrisInstance);
        }

        return true;
    }

    private void getNextShape(TetrisInstance tetrisInstance) {
        final Shape currentShape = tetrisInstance.getNextShape();

        tetrisInstance.setCurrentShape(currentShape);

        randomizeShapeStartX(currentShape);

        currentShape.getBlocks()
                .forEach(block -> tetrisInstance.getBlocks().put(block.getLocation(), Optional.of(block)));

        tetrisInstance.setNextShape(shapeFactory.createRandomShape());

        updateStatistics(tetrisInstance);
    }

    private void randomizeShapeStartX(Shape shape) {
        final int dx = random.nextInt(MAX_START_X);

        shape.getBoundingBox().translate(dx, 0);

        shape.getBlocks()
                .forEach(block -> block.getLocation().translate(dx, 0));
    }

    private void updateStatistics(TetrisInstance tetrisInstance) {
        final ShapeType shapeType = tetrisInstance.getCurrentShape().getShapeType();
        final Map<ShapeType, Integer> statistics = tetrisInstance.getStatistics();
        final Integer count = statistics.get(shapeType);

        statistics.put(shapeType, count + 1);
    }

    private boolean isGameOver(TetrisInstance tetrisInstance) {
        return tetrisInstance.getBlocks().values().stream()
                .anyMatch(block -> block.isPresent() && block.get().getLocation().y == 0);
    }

    private boolean isTimeToHandleGravity(TetrisInstance tetrisInstance) {
        return tetrisInstance.getCurrentGravityFrame() > tetrisInstance.getSpeed();
    }

    private void clearCompleteRows(TetrisInstance tetrisInstance) {
        final List<Integer> completeRows = IntStream.range(0, HEIGHT)
                .filter(row -> isRowComplete(tetrisInstance, row)).boxed().collect(Collectors.toList());

        if (completeRows.isEmpty()) {
            return;
        }

        final int completedRows = completeRows.size();

        completeRows.forEach(row -> clearCompleteRow(tetrisInstance, row));

        tetrisInstance.setCompletedRows(tetrisInstance.getCompletedRows() + completedRows);

        updateScore(tetrisInstance, completedRows);
        updateLevel(tetrisInstance);
    }

    private void updateLevel(TetrisInstance tetrisInstance) {
        final int completedRows = tetrisInstance.getCompletedRows();
        final int nextLevel = tetrisInstance.getLevel() + 1;

        if (completedRows >= ROWS_PER_LEVEL * nextLevel) {
            tetrisInstance.setLevel(nextLevel);

            final int speed = getSpeed(nextLevel);

            tetrisInstance.setSpeed(speed);
        }
    }

    private void updateScore(TetrisInstance tetrisInstance, int completedRows) {
        final Integer rowsScore = scorePerRows.get(completedRows);
        final int score = tetrisInstance.getScore();

        tetrisInstance.setScore(score + rowsScore * (tetrisInstance.getLevel() + 1));
    }

    private void clearCompleteRow(TetrisInstance tetrisInstance, Integer row) {
        final Map<Point, Optional<Block>> blocks = tetrisInstance.getBlocks();
        final List<Block> blocksToMoveDown = blocks.values().stream()
                .filter(block -> block.isPresent() && block.get().getLocation().y < row).map(Optional::get)
                .collect(Collectors.toList());

        IntStream.range(0, WIDTH)
                .forEach(column -> clearBlockAt(tetrisInstance, new Point(column, row)));

        blocksToMoveDown.forEach(block -> clearBlockAt(tetrisInstance, block.getLocation()));
        blocksToMoveDown.forEach(block -> moveBlockDown(tetrisInstance, block));
    }

    private void clearBlockAt(TetrisInstance tetrisInstance, Point location) {
        tetrisInstance.getBlocks().put(location, Optional.empty());
    }

    private boolean isRowComplete(TetrisInstance tetrisInstance, int row) {
        final Map<Point, Optional<Block>> blocks = tetrisInstance.getBlocks();

        return IntStream.range(0, WIDTH).mapToObj(column -> blocks.get(new Point(column, row)))
                .allMatch(Optional::isPresent);
    }

    private boolean moveShape(TetrisInstance tetrisInstance, Movement movement) {
        final Shape currentShape = tetrisInstance.getCurrentShape();
        final Shape newShape = movement.apply(currentShape);

        currentShape.getBlocks()
                .forEach(block -> tetrisInstance.getBlocks().put(block.getLocation(), Optional.empty()));

        newShape.getBlocks().forEach(block -> tetrisInstance.getBlocks().put(block.getLocation(), Optional.of(block)));

        tetrisInstance.setCurrentShape(newShape);

        if (checkShapeIsLocked(tetrisInstance)) {
            return false;
        }

        return true;
    }

    private void moveBlockDown(TetrisInstance tetrisInstance, Block block) {
        final Point location = block.getLocation();
        final Point newLocation = Movement.DOWN.apply(location);

        block.setLocation(newLocation);

        tetrisInstance.getBlocks().put(block.getLocation(), Optional.of(block));
    }

    private boolean canShapeMove(TetrisInstance tetrisInstance, Movement movement) {
        final Shape currentShape = tetrisInstance.getCurrentShape();
        final Shape newShape = movement.apply(currentShape);

        return newShape.getBlocks().stream().allMatch(block -> canBlockMove(tetrisInstance, block));
    }

    private boolean canBlockMove(TetrisInstance tetrisInstance, Block block) {
        final Point location = block.getLocation();

        if (location.x < 0 || location.x == WIDTH || location.y < 0 || location.y == HEIGHT) {
            return false;
        }

        final Optional<Block> nextLocationBlock = tetrisInstance.getBlocks().get(location);

        if (nextLocationBlock.isPresent()
                && !nextLocationBlock.get().getShape().equals(tetrisInstance.getCurrentShape())) {
            return false;
        }

        return true;
    }

    private boolean isTetrisInstanceRunning(TetrisInstance tetrisInstance) {
        return isTetrisInstanceState(tetrisInstance, State.RUNNING);
    }

    private boolean isTetrisInstanceState(TetrisInstance tetrisInstance, State state) {
        return state.equals(tetrisInstance.getState());
    }

    private void resetCurrentGravityFrame(TetrisInstance tetrisInstance) {
        tetrisInstance.setCurrentGravityFrame(0);
    }

    private int getSpeed(int level) {
        return level < 29 ? levelSpeeds.get(level) : 1;
    }
}
