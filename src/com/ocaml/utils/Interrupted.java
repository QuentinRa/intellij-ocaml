package com.ocaml.utils;

public class Interrupted {
    public static void sleep(int timeToWait) {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }
}
