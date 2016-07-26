package spypunk.tetris.service;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.Maps;

import spypunk.tetris.constants.TetrisConstants;
import spypunk.tetris.factory.ShapeFactory;
import spypunk.tetris.model.Block;
import spypunk.tetris.model.Movement;
import spypunk.tetris.model.Shape;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;

@Singleton
public class TetrisServiceImpl implements TetrisService {

    @Inject
    private ShapeFactory shapeFactory;

    private final Map<Integer, Integer> scorePerCompletedRows = Maps.newHashMap();

    public TetrisServiceImpl() {
        scorePerCompletedRows.put(1, 40);
        scorePerCompletedRows.put(2, 100);
        scorePerCompletedRows.put(3, 300);
        scorePerCompletedRows.put(4, 1200);
    }

    @Override
    public void update(Tetris tetris) {
        if (tetris.isGameOver() || !handleNextShape(tetris) || !handleMovement(tetris) || !isTimeToMoveShape(tetris)) {
            return;
        }

        handleGravity(tetris);
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
        Optional<Movement> movement = tetris.getMovement();

        if (movement.isPresent() && !handleMovement(tetris, movement.get())) {
            return false;
        }

        return true;
    }

    private void handleGravity(Tetris tetris) {
        moveShape(tetris, Movement.DOWN);
        tetris.setLastMoveTime(System.currentTimeMillis());
    }

    private boolean handleMovement(Tetris tetris, Movement movement) {
        if (Movement.DOWN.equals(movement) || canShapeMove(tetris, movement)) {
            return moveShape(tetris, movement);
        }

        return true;
    }

    private boolean checkShapeIsLocked(Tetris tetris) {
        if (canShapeMove(tetris, Movement.DOWN)) {
            return false;
        }

        if (isGameOver(tetris)) {
            tetris.setGameOver(true);
        } else {
            tetris.setCurrentShape(null);
            tetris.setLastLockedTime(System.currentTimeMillis());
        }

        return true;
    }

    private void getNextShape(Tetris tetris) {
        tetris.setCurrentShape(tetris.getNextShape());

        tetris.setNextShape(shapeFactory.createRandomShape());

        tetris.getCurrentShape().getBlocks()
                .forEach(block -> tetris.getBlocks().put(block.getLocation(), Optional.of(block)));

        tetris.setLastMoveTime(System.currentTimeMillis());

        updateStatistics(tetris);
    }

    private void updateStatistics(Tetris tetris) {
        Map<ShapeType, Integer> statistics = tetris.getStatistics();

        ShapeType shapeType = tetris.getCurrentShape().getShapeType();

        Integer count = statistics.get(shapeType);

        statistics.put(shapeType, count + 1);
    }

    private boolean isGameOver(Tetris tetris) {
        return tetris.getBlocks().values().stream()
                .anyMatch(block -> block.isPresent() && block.get().getLocation().y == 2);
    }

    private boolean isTimeToMoveShape(Tetris tetris) {
        long currentTime = System.currentTimeMillis();
        long lastMoveTime = tetris.getLastMoveTime();

        return currentTime - lastMoveTime > tetris.getSpeed();
    }

    private boolean isTimeForNextShape(Tetris tetris) {
        long currentTime = System.currentTimeMillis();
        long lastLockedTime = tetris.getLastLockedTime();

        return currentTime - lastLockedTime > tetris.getSpeed();
    }

    private void clearCompleteRows(Tetris tetris) {
        List<Integer> completeRows = IntStream.range(2, TetrisConstants.HEIGHT)
                .filter(row -> isRowComplete(tetris, row)).boxed().collect(Collectors.toList());

        if (completeRows.isEmpty()) {
            return;
        }

        completeRows.forEach(row -> clearCompleteRow(tetris, row));

        int completedRows = completeRows.size();

        updateScore(tetris, completedRows);
        updateLevel(tetris, completedRows);
    }

    private void updateLevel(Tetris tetris, int completedRows) {
        int currentCompletedRows = tetris.getCompletedRows() + completedRows;

        if (currentCompletedRows >= TetrisConstants.ROWS_PER_LEVEL) {
            tetris.setCompletedRows(0);
            tetris.setLevel(tetris.getLevel() + 1);

            int speed = tetris.getSpeed();

            tetris.setSpeed(speed - speed / 6);
        } else {
            tetris.setCompletedRows(currentCompletedRows);
        }
    }

    private void updateScore(Tetris tetris, int completedRows) {
        Integer rowsScore = scorePerCompletedRows.get(completedRows);
        int score = tetris.getScore();
        tetris.setScore(score + rowsScore * (tetris.getLevel() + 1));
    }

    private void clearCompleteRow(Tetris tetris, Integer row) {
        Map<Point, Optional<Block>> blocks = tetris.getBlocks();

        IntStream.range(0, TetrisConstants.WIDTH)
                .forEach(column -> blocks.put(new Point(column, row), Optional.empty()));

        List<Block> blocksToMoveDown = blocks.values().stream()
                .filter(block -> block.isPresent() && block.get().getLocation().y < row).map(Optional::get)
                .collect(Collectors.toList());

        blocksToMoveDown.forEach(block -> blocks.put(block.getLocation(), Optional.empty()));
        blocksToMoveDown.forEach(block -> moveBlockDown(tetris, block));
    }

    private boolean isRowComplete(Tetris tetris, int row) {
        Map<Point, Optional<Block>> blocks = tetris.getBlocks();

        return IntStream.range(0, TetrisConstants.WIDTH).mapToObj(column -> blocks.get(new Point(column, row)))
                .allMatch(Optional::isPresent);
    }

    private boolean moveShape(Tetris tetris, Movement movement) {
        Shape currentShape = tetris.getCurrentShape();
        Shape newShape = movement.apply(currentShape);

        currentShape.getBlocks().forEach(block -> tetris.getBlocks().put(block.getLocation(), Optional.empty()));

        newShape.getBlocks().forEach(block -> tetris.getBlocks().put(block.getLocation(), Optional.of(block)));

        tetris.setCurrentShape(newShape);

        if (checkShapeIsLocked(tetris)) {
            return false;
        }

        return true;
    }

    private void moveBlockDown(Tetris tetris, Block block) {
        Point location = block.getLocation();
        Point newLocation = Movement.DOWN.apply(location);

        block.setLocation(newLocation);

        tetris.getBlocks().put(block.getLocation(), Optional.of(block));
    }

    private boolean canShapeMove(Tetris tetris, Movement movement) {
        Shape currentShape = tetris.getCurrentShape();
        Shape newShape = movement.apply(currentShape);

        return newShape.getBlocks().stream().allMatch(block -> canBlockMove(tetris, block));
    }

    private boolean canBlockMove(Tetris tetris, Block block) {
        Point location = block.getLocation();

        if (location.x < 0 || location.x == TetrisConstants.WIDTH || location.y == TetrisConstants.HEIGHT) {
            return false;
        }

        Optional<Block> nextLocationBlock = tetris.getBlocks().get(location);

        if (nextLocationBlock.isPresent() && !nextLocationBlock.get().getShape().equals(tetris.getCurrentShape())) {
            return false;
        }

        return true;
    }
}
