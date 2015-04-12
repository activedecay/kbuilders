package com.levelmoney.kbuilders.test.builders;

/**
 * Created by Aaron Sarazan on 4/12/15
 * Copyright(c) 2015 Level, Inc.
 */
public final class ParentObject {

    public final BasicObject child;

    public ParentObject(Builder builder) {
        this.child = builder.child;
    }

    public static class Builder {

        public BasicObject child;

        public Builder() {}

        public Builder child(BasicObject child) {
            this.child = child;
            return this;
        }

        public ParentObject build() {
            return new ParentObject(this);
        }
    }
}
