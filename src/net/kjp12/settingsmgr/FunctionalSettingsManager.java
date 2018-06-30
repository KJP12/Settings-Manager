package net.kjp12.settingsmgr;//Created on 6/30/18.

import net.kjp12.settingsmgr.settings.Dirty;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class FunctionalSettingsManager<I, O extends Dirty> extends AbstractSettingsManager<I, O> {
    private final BiConsumer<I, O> SET_SETTINGS;
    private final Function<I, O> GET_SETTINGS;

    public FunctionalSettingsManager(@Nonnull Class<O> clazz, BiConsumer<I, O> setSettings, Function<I, O> getSettings) {
        super(clazz);
        SET_SETTINGS = setSettings;
        GET_SETTINGS = getSettings;
    }

    @Override
    protected O getSettings0(I i) {
        return GET_SETTINGS.apply(i);
    }

    @Override
    public void setSettings(I i, O o) {
        SET_SETTINGS.accept(i, o);
    }
}
