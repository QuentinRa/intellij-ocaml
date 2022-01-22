package com.ocaml.utils;

/**
 * Compute a method having
 * @param <R> a return type of the type R
 * @param <A> arguments of the type A
 */
@FunctionalInterface
public interface ComputeMethod<R, A> {

    R call(A provider);

}
