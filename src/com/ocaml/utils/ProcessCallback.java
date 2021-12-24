package com.ocaml.utils;

@FunctionalInterface
public interface ProcessCallback<T> {
    void call(T o);
}
