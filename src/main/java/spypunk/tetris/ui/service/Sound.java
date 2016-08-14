package spypunk.tetris.ui.service;

public enum Sound {

    SHAPE_LOCKED(Extension.WAV);

    private enum Extension {
        WAV
    }

    private final Extension extension;

    private Sound(Extension extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return name() + "." + extension.name().toLowerCase();
    }
}
