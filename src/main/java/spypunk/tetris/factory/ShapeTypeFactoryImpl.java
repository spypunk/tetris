/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 * 
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.factory;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import spypunk.tetris.model.ShapeType;
import spypunk.tetris.model.ShapeType.Id;

@Singleton
public class ShapeTypeFactoryImpl implements ShapeTypeFactory {

    private static final Point LOCATION_0_0 = new Point(0, 0);
    private static final Point LOCATION_0_1 = new Point(0, 1);
    private static final Point LOCATION_0_2 = new Point(0, 2);
    private static final Point LOCATION_1_0 = new Point(1, 0);
    private static final Point LOCATION_1_1 = new Point(1, 1);
    private static final Point LOCATION_1_2 = new Point(1, 2);
    private static final Point LOCATION_1_3 = new Point(1, 3);
    private static final Point LOCATION_2_0 = new Point(2, 0);
    private static final Point LOCATION_2_1 = new Point(2, 1);
    private static final Point LOCATION_2_2 = new Point(2, 2);
    private static final Point LOCATION_2_3 = new Point(2, 3);
    private static final Point LOCATION_3_1 = new Point(3, 1);
    private static final Point LOCATION_3_2 = new Point(3, 2);

    private final List<ShapeType> shapeTypes = Lists.newArrayList();

    public ShapeTypeFactoryImpl() {
        shapeTypes.add(createOShapeType());
        shapeTypes.add(createIShapeType());
        shapeTypes.add(createJShapeType());
        shapeTypes.add(createLShapeType());
        shapeTypes.add(createSShapeType());
        shapeTypes.add(createTShapeType());
        shapeTypes.add(createZShapeType());
    }

    @Override
    public List<ShapeType> createAll() {
        return shapeTypes;
    }

    private static ShapeType createOShapeType() {
        return ShapeType.Builder.instance().setId(Id.O).setBoundingBox(new Rectangle(0, 0, 2, 2))
                .setRotations(
                    Arrays.asList(Sets.newHashSet(LOCATION_0_0, LOCATION_1_0, LOCATION_0_1, LOCATION_1_1)))
                .build();
    }

    private static ShapeType createIShapeType() {
        return ShapeType.Builder.instance().setId(Id.I).setBoundingBox(new Rectangle(0, 0, 4, 4))
                .setRotations(
                    Arrays.asList(Sets.newHashSet(LOCATION_0_1, LOCATION_1_1, LOCATION_2_1, LOCATION_3_1),
                        Sets.newHashSet(LOCATION_2_0, LOCATION_2_1, LOCATION_2_2, LOCATION_2_3),
                        Sets.newHashSet(LOCATION_0_2, LOCATION_1_2, LOCATION_2_2, LOCATION_3_2),
                        Sets.newHashSet(LOCATION_1_0, LOCATION_1_1, LOCATION_1_2, LOCATION_1_3)))
                .build();
    }

    private static ShapeType createJShapeType() {
        return ShapeType.Builder.instance().setId(Id.J).setBoundingBox(new Rectangle(0, 0, 3, 3))
                .setRotations(
                    Arrays.asList(Sets.newHashSet(LOCATION_0_1, LOCATION_1_1, LOCATION_2_1, LOCATION_2_0),
                        Sets.newHashSet(LOCATION_1_0, LOCATION_1_1, LOCATION_1_2, LOCATION_2_2),
                        Sets.newHashSet(LOCATION_0_1, LOCATION_1_1, LOCATION_2_1, LOCATION_0_2),
                        Sets.newHashSet(LOCATION_0_0, LOCATION_1_0, LOCATION_1_1, LOCATION_1_2)))
                .build();
    }

    private static ShapeType createLShapeType() {
        return ShapeType.Builder.instance().setId(Id.L).setBoundingBox(new Rectangle(0, 0, 3, 3))
                .setRotations(
                    Arrays.asList(Sets.newHashSet(LOCATION_0_1, LOCATION_1_1, LOCATION_2_1, LOCATION_2_0),
                        Sets.newHashSet(LOCATION_1_0, LOCATION_1_1, LOCATION_1_2, LOCATION_2_2),
                        Sets.newHashSet(LOCATION_0_1, LOCATION_1_1, LOCATION_2_1, LOCATION_0_2),
                        Sets.newHashSet(LOCATION_0_0, LOCATION_1_0, LOCATION_1_1, LOCATION_1_2)))
                .build();
    }

    private static ShapeType createSShapeType() {
        return ShapeType.Builder.instance().setId(Id.S).setBoundingBox(new Rectangle(0, 0, 3, 3))
                .setRotations(
                    Arrays.asList(Sets.newHashSet(LOCATION_1_0, LOCATION_2_0, LOCATION_0_1, LOCATION_1_1),
                        Sets.newHashSet(LOCATION_1_0, LOCATION_1_1, LOCATION_2_1, LOCATION_2_2),
                        Sets.newHashSet(LOCATION_1_1, LOCATION_2_1, LOCATION_0_2, LOCATION_1_2),
                        Sets.newHashSet(LOCATION_0_0, LOCATION_0_1, LOCATION_1_1, LOCATION_1_2)))
                .build();
    }

    private static ShapeType createTShapeType() {
        return ShapeType.Builder.instance().setId(Id.T).setBoundingBox(new Rectangle(0, 0, 3, 3))
                .setRotations(
                    Arrays.asList(Sets.newHashSet(LOCATION_1_0, LOCATION_0_1, LOCATION_1_1, LOCATION_2_1),
                        Sets.newHashSet(LOCATION_1_0, LOCATION_1_1, LOCATION_2_1, LOCATION_1_2),
                        Sets.newHashSet(LOCATION_0_1, LOCATION_1_1, LOCATION_2_1, LOCATION_1_2),
                        Sets.newHashSet(LOCATION_1_0, LOCATION_0_1, LOCATION_1_1, LOCATION_1_2)))
                .build();
    }

    private static ShapeType createZShapeType() {
        return ShapeType.Builder.instance().setId(Id.Z).setBoundingBox(new Rectangle(0, 0, 3, 3))
                .setRotations(
                    Arrays.asList(Sets.newHashSet(LOCATION_0_0, LOCATION_1_0, LOCATION_1_1, LOCATION_2_1),
                        Sets.newHashSet(LOCATION_2_0, LOCATION_1_1, LOCATION_2_1, LOCATION_1_2),
                        Sets.newHashSet(LOCATION_0_1, LOCATION_1_1, LOCATION_1_2, LOCATION_2_2),
                        Sets.newHashSet(LOCATION_1_0, LOCATION_0_1, LOCATION_1_1, LOCATION_0_2)))
                .build();
    }
}
