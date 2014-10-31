/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.util.function.Function;

import org.junit.Test;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;


/**
 * @author root42 Inc.
 * @version $Id$
 */
public class SListTest {

    /**
     * Test of {@link SList#group(Function)}.
     */
    @Test
    public void group() {

        assertThat(
            prog1(
                () -> list(prog1(() -> new Bean1(), x -> {
                    x.key = "key1";
                    x.bean2 = prog1(() -> new Bean2(), y -> y.val = 1);
                }), prog1(() -> new Bean1(), x -> {
                    x.key = "key2";
                    x.bean2 = prog1(() -> new Bean2(), y -> y.val = 2);
                }), prog1(() -> new Bean1(), x -> {
                    x.key = "key3";
                    x.bean2 = prog1(() -> new Bean2(), y -> y.val = 4);
                }), prog1(() -> new Bean1(), x -> {
                    x.key = "key1";
                    x.bean2 = prog1(() -> new Bean2(), y -> y.val = 8);
                }), prog1(() -> new Bean1(), x -> {
                    x.key = "key2";
                    x.bean2 = prog1(() -> new Bean2(), y -> y.val = 16);
                })) //
                .group(x -> x.key), //
                x -> {
                    assertEquals(x.map(y -> y.map(z -> z.bean2.val)),
                        map("key1", list(1, 8), "key2", list(2, 16), "key3", list(4)));
                }) //
                .vals() //
                .map(x -> x.map(y -> y.bean2.val) //
                    .reduce(0, (y, z) -> y + z)) //
        ) //
        .isEqualTo(list(9, 18, 4));
    }

    static class Bean1 {

        String key;

        Bean2 bean2;
    }

    static class Bean2 {
        int val;
    }
}
