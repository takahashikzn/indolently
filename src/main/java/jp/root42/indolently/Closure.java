/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

/**
 * Represents an arbitrary operation that doesn't accept any argument.
 *
 * @author takahashikzn
 */
@FunctionalInterface
public interface Closure {

    /**
     * Perform this operation.
     */
    void perform();
}
