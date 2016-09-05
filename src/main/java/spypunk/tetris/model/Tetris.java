/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.model;

import java.net.URI;

public class Tetris {

    public static final class Builder {

        private final Tetris tetris = new Tetris();

        private Builder() {
        }

        public static Builder instance() {
            return new Builder();
        }

        public Builder setName(final String name) {
            tetris.setName(name);
            return this;
        }

        public Builder setVersion(final String version) {
            tetris.setVersion(version);
            return this;
        }

        public Builder setProjectURI(final URI projectURI) {
            tetris.setProjectURI(projectURI);
            return this;
        }

        public Tetris build() {
            return tetris;
        }

    }

    private String name;

    private String version;

    private URI projectURI;

    private TetrisInstance tetrisInstance;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public URI getProjectURI() {
        return projectURI;
    }

    public void setProjectURI(final URI projectURI) {
        this.projectURI = projectURI;
    }

    public TetrisInstance getTetrisInstance() {
        return tetrisInstance;
    }

    public void setTetrisInstance(final TetrisInstance tetrisInstance) {
        this.tetrisInstance = tetrisInstance;
    }
}
