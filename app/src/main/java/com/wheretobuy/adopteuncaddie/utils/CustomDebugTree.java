package com.wheretobuy.adopteuncaddie.utils;

import timber.log.Timber;

public class CustomDebugTree extends Timber.DebugTree {
    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return String.format("[%s:%s | %s]",
                super.createStackElementTag(element),
                element.getLineNumber(),
                element.getMethodName()
                );
    }
}
