package com.flexcore.builder.query.types;

public class Tuple<A, B> {
    private final A key;
    private final B value;

    public Tuple(A key, B value) {
        this.key = key;
        this.value = value;
    }

    public A getKey() {
        return key;
    }

    public B getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}

