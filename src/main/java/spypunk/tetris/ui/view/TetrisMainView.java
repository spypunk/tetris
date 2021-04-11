/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.view;

import javax.swing.*;

public interface TetrisMainView {

    void show();

    void hide();

    void update();

    void setMuted(boolean muted);

    JPanel getJPanel();
}
