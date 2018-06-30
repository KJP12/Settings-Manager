package net.kjp12.settingsmgr;//Created on 6/30/18.

import net.kjp12.settingsmgr.functions.ThrowingConsumer;
import net.kjp12.settingsmgr.functions.ThrowingFunction;
import net.kjp12.settingsmgr.settings.Dirty;

@SuppressWarnings("unchecked")
public class SettingsSystem {
    protected final AbstractSettingsManager<?, ?>[] ARR;

    public SettingsSystem(int init) {
        ARR = new AbstractSettingsManager<?, ?>[init];
    }

    public SettingsSystem(AbstractSettingsManager<?, ?>... managers) {
        ARR = managers;
    }

    public void newSettings(AbstractSettingsManager<?, ?> mgr) {
        for (int i = 0; i < ARR.length; i++)
            if (ARR[i] == null) {
                ARR[i] = mgr;
                return;
            }
        throw new ArrayIndexOutOfBoundsException("Cannot store " + mgr);
    }

    public <I, O extends Dirty> AbstractSettingsManager<I, O> getManager(Class<O> clazz) {
        for (AbstractSettingsManager<?, ?> mgr : ARR)
            if (mgr != null && mgr.isType(clazz)) return (AbstractSettingsManager<I, O>) mgr;
        return null;
    }

    public <T, I, O extends Dirty> T getSetting(I i, Class<O> oClass, ThrowingFunction<O, T> taco) {
        for (AbstractSettingsManager<?, ?> mgr : ARR)
            if (mgr != null && mgr.isType(oClass)) return ((AbstractSettingsManager<I, O>) mgr).getSetting(i, taco);
        return null;
    }

    public <I, O extends Dirty> void getSettings(I i, Class<O> oClass, ThrowingConsumer<O> taco) {
        for (AbstractSettingsManager<?, ?> mgr : ARR)
            if (mgr != null && mgr.isType(oClass)) {
                ((AbstractSettingsManager<I, O>) mgr).getSettings(i, taco);
                return;
            }
    }

    public <I, O extends Dirty> void setSettings(I i, O o) {
        for (AbstractSettingsManager<?, ?> mgr : ARR)
            if (mgr != null && mgr.isInstance(o)) {
                ((AbstractSettingsManager<I, O>) mgr).setSettings(i, o);
                return;
            }
    }
}
