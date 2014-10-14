/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently.trait;

import java.util.function.BiFunction;

import jp.root42.indolently.Indolently;

import org.junit.Test;

import static jp.root42.indolently.Indolently.*;
import static org.assertj.core.api.Assertions.*;


/**
 * @author root42 Inc.
 * @version $Id$
 */
public class ReducibleTest {

    /**
     * Test of {@link Reducible#reduce(BiFunction)}.
     */
    @Test
    public void reduce() {

        assertThat(Indolently.<String> list().reduce((x, y) -> x + y).isPresent()).isFalse();
        assertThat(Indolently.<String> list().reduce("0", (x, y) -> x + y)).isEqualTo("0");

        assertThat(list("1").reduce((x, y) -> x + y).get()).isEqualTo("1");
        assertThat(list("1", "2").reduce((x, y) -> x + y).get()).isEqualTo("12");
        assertThat(list("1", "2").reduce("0", (x, y) -> x + y)).isEqualTo("012");
    }
}
