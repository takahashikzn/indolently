/*
 * Copyright (C) 2015 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.root42.indolently.SMap.SEntry;

import static jp.root42.indolently.Indolently.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit test of {@link SMap}.
 *
 * @author root42 Inc.
 * @version $Id$
 */
public class SMapTest {

    /**
     * {@link SMap#iterator()}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void iterator() {

        final Map<String, String> data = mock(Map.class);
        final Set<Entry<String, String>> entrySet = mock(Set.class);
        final Iterator<Entry<String, String>> entryIter = mock(Iterator.class);

        when(data.entrySet()).thenReturn(entrySet);
        when(entrySet.iterator()).thenReturn(entryIter);
        when(entryIter.hasNext()).thenReturn(true, false);
        when(entryIter.next()).thenReturn(mock(Entry.class));

        final SIter<SEntry<String, String>> testee = $(data).iterator();

        verify(entryIter, times(0)).hasNext();

        assertThat(testee.next()).isNotNull();
        assertThat(testee.hasNext()).isFalse();

        verify(entryIter, times(2)).hasNext();
    }

    /**
     * {@link SMap#entries()}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void entries() {

        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("3", "3");
        map.put("2", "2");
        map.put("1", "1");
        map.put("0", "0");

        assertThat(new SMapImpl<>(map).entries().list().map(x -> x.key)).isEqualTo(list("3", "2", "1", "0"));
    }
}
