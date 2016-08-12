/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.ui.service;

import javax.inject.Singleton;

@Singleton
public class MusicServiceImpl implements MusicService {

    private enum State {
        PLAYING {
            @Override
            public State pause() {
                return PAUSED;
            }
        },
        PAUSED {
            @Override
            public State pause() {
                return PLAYING;
            }
        },
        STOPPED {
            @Override
            public State play() {
                return PLAYING;
            }
        };

        public State play() {
            return this;
        }

        public State pause() {
            return this;
        }

        public State stop() {
            return STOPPED;
        }
    }

    private State state = State.STOPPED;

    @Override
    public void playMusic() {
        state = state.play();
    }

    @Override
    public void pauseMusic() {
        state = state.pause();
    }

    @Override
    public void stopMusic() {
        state = state.stop();
    }
}
