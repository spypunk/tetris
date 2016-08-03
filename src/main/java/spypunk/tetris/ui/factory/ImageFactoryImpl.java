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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import spypunk.tetris.exception.TetrisException;
import spypunk.tetris.model.ShapeType;

@Singleton
public class ImageFactoryImpl implements ImageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageFactoryImpl.class);

    private static final String BLOCKS_FOLDER = "/img/blocks/";

    private final Map<ShapeType, Image> blockImages;

    @Inject
    public ImageFactoryImpl() {
        blockImages = Lists.newArrayList(ShapeType.values()).stream().collect(Collectors.toMap(Function.identity(),
            shapeType -> createImage(String.format("%s%s.png", BLOCKS_FOLDER, shapeType.name()))));
    }

    @Override
    public Image createBlockImage(ShapeType shapeType) {
        return blockImages.get(shapeType);
    }

    private Image createImage(String resourceName) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
            return ImageIO.read(inputStream);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TetrisException(e);
        }
    }
}
