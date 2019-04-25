package com.github.kolyall.rxjavautils;


import java.util.logging.Logger;

public class ThreadUtils {

    private static final Logger logger = Logger.getLogger(ThreadUtils.class.getName());
    /**
     * check that the executed command is not in Main Thread, otherwise will print it in logcat
     * */
    public static void printThread(String tag) {
        if (Thread.currentThread().getName().equals("main")) {
            printThreadName(tag);
        }
    }

    public static void printThreadName(String tag) {
        logger.fine( tag + " is called from " + Thread.currentThread().getName() + " Thread");
    }
}
