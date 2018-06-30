package net.kjp12.settingsmgr.settings;//Created on 6/30/18.

public class ASettings implements Dirty {
    private transient boolean dirty;

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}
