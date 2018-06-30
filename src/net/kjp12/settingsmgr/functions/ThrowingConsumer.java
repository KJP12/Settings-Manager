package net.kjp12.settingsmgr.functions;//Created on 6/30/18.

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T t) throws Throwable;
}
