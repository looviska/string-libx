package strlib;

public enum SplitMode {
    KEEP_EMPTY(true),
    SKIP_EMPTY(false);

    private final boolean keeepsEmpty;

    SplitMode(boolean keeepsEmpty) {
        this.keeepsEmpty = keeepsEmpty;
    }

    public boolean keepsEmpty() {
        return keeepsEmpty;
    }
}
