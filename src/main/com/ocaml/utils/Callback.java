package com.ocaml.utils;

/**
 * A callback is a function that will be called
 * when an asynchronous code has finished executing.
 *
 * @param <T> any type, see #call
 */
@FunctionalInterface
public interface Callback<T> {

    void call(T arg);
}
