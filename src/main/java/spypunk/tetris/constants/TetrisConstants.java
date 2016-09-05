/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.constants;

import java.awt.Point;

public final class TetrisConstants {

    public static final int WIDTH = 10;

    public static final int HEIGHT = 20;

    public static final Point LOCATION_0_0 = new Point(0, 0);

    public static final Point LOCATION_0_1 = new Point(0, 1);

    public static final Point LOCATION_0_2 = new Point(0, 2);

    public static final Point LOCATION_1_M1 = new Point(1, -1);

    public static final Point LOCATION_1_0 = new Point(1, 0);

    public static final Point LOCATION_1_1 = new Point(1, 1);

    public static final Point LOCATION_1_2 = new Point(1, 2);

    public static final Point LOCATION_1_3 = new Point(1, 3);

    public static final Point LOCATION_2_M1 = new Point(2, -1);

    public static final Point LOCATION_2_0 = new Point(2, 0);

    public static final Point LOCATION_2_1 = new Point(2, 1);

    public static final Point LOCATION_2_2 = new Point(2, 2);

    public static final Point LOCATION_2_3 = new Point(2, 3);

    public static final Point LOCATION_3_0 = new Point(3, 0);

    public static final Point LOCATION_3_1 = new Point(3, 1);

    public static final Point LOCATION_3_2 = new Point(3, 2);

    private TetrisConstants() {
        throw new IllegalAccessError();
    }
}
