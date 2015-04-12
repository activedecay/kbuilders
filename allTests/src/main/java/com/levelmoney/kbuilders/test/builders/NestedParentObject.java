package com.levelmoney.kbuilders.test.builders;

/**
 * Created by Aaron Sarazan on 4/12/15
 * Copyright(c) 2015 Level, Inc.
 */
public final class NestedParentObject {

    public final ChildObject child;

    public NestedParentObject(Builder builder) {
        this.child = builder.child;
    }

    public static class Builder {

        public ChildObject child;

        public Builder() {}

        public Builder child(ChildObject child) {
            this.child = child;
            return this;
        }

        public NestedParentObject build() {
            return new NestedParentObject(this);
        }
    }

    public static final class ChildObject {

        public final int value;

        public ChildObject(Builder builder) {
            this.value = builder.value;
        }

        public static class Builder {
            public int value;

            public Builder() {}

            public Builder value(int value) {
                this.value = value;
                return this;
            }

            public ChildObject build() {
                return new ChildObject(this);
            }
        }
    }
}
