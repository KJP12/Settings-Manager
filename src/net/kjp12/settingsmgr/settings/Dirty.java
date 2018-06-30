package net.kjp12.settingsmgr.settings;//Created on 6/30/18.

import java.io.Serializable;

public interface Dirty extends Serializable {
    void markDirty();

    boolean isDirty();
}
