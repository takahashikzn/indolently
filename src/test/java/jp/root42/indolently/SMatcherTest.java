/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import org.junit.Test;

import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Regexive.*;
import static org.assertj.core.api.Assertions.*;


/**
 * @author root42 Inc.
 * @version $Id$
 */
public class SMatcherTest {

    /**
     * Test of {@link SMatcher#iterator()}.
     */
    @Test
    public void testRegex() {

        assertThat(list(regex("\\w+", "foo.bar.baz"))) //
            .isEqualTo(list("foo", "bar", "baz"));

        assertThat(list(regex("\\d+", "abc"))) //
            .isEqualTo(list());
    }
}
