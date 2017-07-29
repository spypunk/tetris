/*
 * Copyright © 2016-2017 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.net.URI;
import java.util.List;

import com.google.common.collect.Lists;

public class Tetris {

    public enum State {
        STOPPED,
        RUNNING {
            @Override
            public State onPause() {
                return PAUSED;
            }
        },
        PAUSED {
            @Override
            public State onPause() {
                return RUNNING;
            }
        },
        GAME_OVER;

        public State onPause() {
            return this;
        }
    }

    private final String name;

    private final String version;

    private final URI projectURI;

    private final List<TetrisEvent> tetrisEvents = Lists.newArrayList();

    private TetrisInstance tetrisInstance;

    private State state = State.STOPPED;

    private boolean muted;

    public Tetris(final String name, final String version, final URI projectURI) {
        this.name = name;
        this.version = version;
        this.projectURI = projectURI;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public URI getProjectURI() {
        return projectURI;
    }

    public TetrisInstance getTetrisInstance() {
        return tetrisInstance;
    }

    public void setTetrisInstance(final TetrisInstance tetrisInstance) {
        this.tetrisInstance = tetrisInstance;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public List<TetrisEvent> getTetrisEvents() {
        return tetrisEvents;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(final boolean muted) {
        this.muted = muted;
    }
}
