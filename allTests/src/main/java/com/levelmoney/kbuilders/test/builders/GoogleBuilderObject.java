package com.levelmoney.kbuilders.test.builders;

/**
 * Created by Aaron Sarazan on 4/12/15
 * Copyright(c) 2015 Level, Inc.
 */
public final class GoogleBuilderObject {

    public final int value;

    public GoogleBuilderObject(Builder builder) {
        this.value = builder.value;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(GoogleBuilderObject existing) {
        return new Builder(existing);
    }

    public static class Builder {
        public int value;

        private Builder() {}
        private Builder(GoogleBuilderObject existing) {
            value = existing.value;
        }

        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public GoogleBuilderObject build() {
            return new GoogleBuilderObject(this);
        }
    }

}
