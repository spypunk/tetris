package spypunk.tetris.factory;

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
import spypunk.tetris.model.Block;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.repository.ShapeTypeRepository;

@Singleton
public class TetrisFactoryImpl implements TetrisFactory {

    @Inject
    private ShapeFactory shapeFactory;

    @Inject
    private ShapeTypeRepository shapeTypeRepository;

    @Override
    public Tetris createTetris() {
        Map<Point, Optional<Block>> blocks = Maps.newHashMap();

        IntStream.range(0, TetrisConstants.WIDTH).forEach(x -> IntStream.range(0, TetrisConstants.HEIGHT)
                .forEach(y -> blocks.put(new Point(x, y), Optional.empty())));

        List<ShapeType> shapeTypes = shapeTypeRepository.findAll();

        Map<ShapeType, Integer> shapesStatistics = shapeTypes.stream()
                .collect(Collectors.toMap(shapeType -> shapeType, shapeType -> 0));

        return Tetris.Builder.instance().setBlocks(blocks).setNextShape(shapeFactory.createRandomShape())
                .setShapesStatistics(shapesStatistics).setSpeed(1000).build();
    }
}
