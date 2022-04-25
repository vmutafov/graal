package com.oracle.truffle.polyglot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ThreadUtils {
    private ThreadUtils() {
    }

    static Thread currentPlatformThread() {
        if (!supportsVirtualThread()) {
            return ThreadUtils.currentPlatformThread();
        }

        return reflectiveGetCarrierThread();
    }

    private static Thread reflectiveGetCarrierThread() {
        try {
            Method currentCarrierThreadMethod = Thread.class.getDeclaredMethod("currentCarrierThread");
            currentCarrierThreadMethod.setAccessible(true);
            return (Thread) currentCarrierThreadMethod.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean supportsVirtualThread() {
        try {
            Thread.class.getDeclaredMethod("isVirtual");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
