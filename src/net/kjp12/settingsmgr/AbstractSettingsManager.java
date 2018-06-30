package net.kjp12.settingsmgr;//Created on 6/29/18.

import com.google.common.base.Ticker;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.kjp12.settingsmgr.functions.ThrowingConsumer;
import net.kjp12.settingsmgr.functions.ThrowingFunction;
import net.kjp12.settingsmgr.settings.Dirty;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
public abstract class AbstractSettingsManager<I, O extends Dirty> {
    protected transient final Class<O> CLASS_O;
    protected transient final Cache<I, O> CACHE = CacheBuilder.newBuilder().concurrencyLevel(10).expireAfterAccess(1, TimeUnit.HOURS).ticker(Ticker.systemTicker()).build();
    protected transient final ConcurrentMap<I, O> CACHE_MAP = CACHE.asMap();
    protected transient final ConcurrentHashMap<I, ReentrantLock> LOCK = new ConcurrentHashMap<>();

    public AbstractSettingsManager(@Nonnull Class<O> oClass) {
        CLASS_O = oClass;
    }

    public <T> T getSetting(I i, ThrowingFunction<O, T> tf) {
        ReentrantLock rl = getLock(i);
        O ou = getSettings(i);
        rl.lock();
        try {
            return tf.accept(ou);
        } catch (Throwable t) {
            if (t instanceof Error) throw new Error("Unexpected error on " + i, t);
            if (t instanceof RuntimeException) throw (RuntimeException) t;
            throw new RuntimeException(t);
        } finally {
            rl.unlock();
            if (ou.isDirty()) setSettings(i, ou);
        }
    }

    public void getSettings(I i, ThrowingConsumer<O> o) {
        ReentrantLock rl = getLock(i);
        O ou = getSettings(i);
        rl.lock();
        try {
            o.accept(getSettings(i));
        } catch (Throwable t) {
            if (t instanceof Error) throw new Error("Unexpected error on " + i, t);
            if (t instanceof RuntimeException) throw (RuntimeException) t;
            throw new RuntimeException(t);
        } finally {
            rl.unlock();
            if (ou.isDirty()) setSettings(i, ou);
        }
    }

    /**
     * Can be exposed if wanted
     */
    protected O getSettings(I i) {
        return CACHE_MAP.computeIfAbsent(i, this::getSettings0);
    }

    protected abstract O getSettings0(I i);

    public abstract void setSettings(I i, O o);

    private ReentrantLock getLock(I i) {
        return LOCK.computeIfAbsent(i, i0 -> new ReentrantLock(true));
    }

    public final boolean isType(Class c) {
        return CLASS_O.isAssignableFrom(c);
        //return CLASS_O.equals(c);
    }

    public final boolean isInstance(Object o) {
        return CLASS_O.isInstance(o);
    }
}
