/*
 * Copyright (c) 2022 Docurain Inc. All rights reserved.
 */
package jp.root42.indolently.ref;

/**
 * @author takahashikzn
 */
public interface tuple3<T1, T2, T3>
    extends tuple2<T1, T2> {

    T3 _3();
}
