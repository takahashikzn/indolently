/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

/**
 * Express this instance can be identical.
 *
 * @param <SELF> self type
 * @author takahashikzn
 */
public interface Identical<SELF extends Identical<SELF>> {

    /**
     * return this instance.
     *
     * @return {@code this} instance.
     */
    default SELF identity() {
        @SuppressWarnings("unchecked")
        final SELF self = (SELF) this;
        return self;
    }
}
