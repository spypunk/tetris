/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.factory;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.factory.ShapeTypeFactory;
import spypunk.tetris.model.ShapeType;

@Singleton
public class BlockImageFactoryImpl implements BlockImageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockImageFactoryImpl.class);

    private static final String BLOCKS_FOLDER = "/img/blocks/";

    private final Map<ShapeType, Image> images = Maps.newHashMap();

    @Inject
    public BlockImageFactoryImpl(ImageFactory imageFactory, ShapeTypeFactory shapeTypeFactory) {
        List<ShapeType> shapeTypes = shapeTypeFactory.createAll();

        try {
            for (ShapeType shapeType : shapeTypes) {
                loadShapeTypeBlockImage(imageFactory, shapeType);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }

    @Override
    public Image createBlockImage(ShapeType shapeType) {
        return images.get(shapeType);
    }

    private void loadShapeTypeBlockImage(ImageFactory imageFactory, ShapeType shapeType) throws IOException {
        String resourceName = String.format("%s%s.png", BLOCKS_FOLDER, shapeType.getId());

        try (InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
            Image image = imageFactory.createImage(getClass().getResourceAsStream(resourceName));
            images.put(shapeType, image);
        }
    }
}
