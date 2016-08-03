/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.factory;

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

import com.google.common.collect.Maps;

import spypunk.tetris.model.Block;
import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.Tetris;
import spypunk.tetris.model.Tetris.State;

@Singleton
public class TetrisFactoryImpl implements TetrisFactory {

    @Inject
    private ShapeFactory shapeFactory;

    @Inject
    private ShapeTypeFactory shapeTypeFactory;

    @Override
    public Tetris createTetris() {
        final Map<Point, Optional<Block>> blocks = Maps.newHashMap();

        IntStream.range(0, WIDTH).forEach(x -> IntStream.range(0, HEIGHT)
                .forEach(y -> blocks.put(new Point(x, y), Optional.empty())));

        final List<ShapeType> shapeTypes = shapeTypeFactory.createAll();

        final Map<ShapeType, Integer> statistics = shapeTypes.stream()
                .collect(Collectors.toMap(shapeType -> shapeType, shapeType -> 0));

        return Tetris.Builder.instance().setBlocks(blocks).setNextShape(shapeFactory.createRandomShape())
                .setStatistics(statistics).setState(State.RUNNING).build();
    }
}
