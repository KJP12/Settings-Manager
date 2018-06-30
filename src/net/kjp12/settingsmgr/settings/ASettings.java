package net.kjp12.settingsmgr.settings;//Created on 6/30/18.

import com.google.gson.annotations.Expose;

public class ASettings implements Dirty {
    @Expose(deserialize = false, serialize = false)
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
