package com.levelmoney.kbuilders.test.builders;

/**
 * Created by Aaron Sarazan on 4/12/15
 * Copyright(c) 2015 Level, Inc.
 */
public final class RebuildObject {

    public final int value;

    public RebuildObject(Builder builder) {
        this.value = builder.value;
    }

    public static class Builder {
        public int value;

        public Builder() {}

        public Builder(RebuildObject other) {
            this.value = value;
        }

        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public RebuildObject build() {
            return new RebuildObject(this);
        }
    }

}
