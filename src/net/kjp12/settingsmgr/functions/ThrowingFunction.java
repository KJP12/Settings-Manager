package net.kjp12.settingsmgr.functions;//Created on 6/30/18.

@FunctionalInterface
public interface ThrowingFunction<I, O> {
    O accept(I i) throws Throwable;
}
