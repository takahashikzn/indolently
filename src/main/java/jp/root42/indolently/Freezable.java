/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Express this instance knows freeze operation.
 *
 * @param <SELF> self type
 * @author takahashikzn
 */
public interface Freezable<SELF extends Freezable<SELF>> {

    /**
     * construct freezed new {@link Collections#unmodifiableList(List) List} / {@link Collections#unmodifiableMap(Map)
     * Map} / {@link Collections#unmodifiableSet(Set) Set} instance.
     * Circular structure is not supported yet.
     *
     * @return freezed new instance
     * @see Collections#unmodifiableList(List)
     * @see Collections#unmodifiableMap(Map)
     * @see Collections#unmodifiableSet(Set)
     */
    SELF freeze();
}
