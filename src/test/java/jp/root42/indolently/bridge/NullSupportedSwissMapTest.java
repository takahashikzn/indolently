/*
 * Copyright (c) 2026 Docurain Inc. All rights reserved.
 */
package jp.root42.indolently.bridge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class NullSupportedSwissMapTest {

    // --- get / put ---

    @Test
    public void get_returnsNullForNullKeyInInitialState() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);

        assertThat(map).doesNotContainKey(null);
        assertThat(map.get(null)).isNull();
    }

    @Test
    public void put_returnsNullOnFirstNullKeyInsertionAndValueIsRetrievable() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);

        final String oldVal = map.put(null, "val1");

        assertThat(oldVal).isNull();
        assertThat(map).containsEntry(null, "val1");
    }

    @Test
    public void put_returnsOldValueOnNullKeyOverwriteAndValueIsUpdated() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val1");

        final String oldVal = map.put(null, "val2");

        assertThat(oldVal).isEqualTo("val1");
        assertThat(map).containsEntry(null, "val2");
    }

    // --- containsKey / remove ---

    @Test
    public void containsKey_correctlyDetectsNullKeyPresence() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);

        assertThat(map).doesNotContainKey(null);

        map.put(null, "val");
        assertThat(map).containsKey(null);
    }

    @Test
    public void remove_returnsOldValueAndNullKeyIsNoLongerPresent() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val");

        final String removed = map.remove(null);

        assertThat(removed).isEqualTo("val");
        assertThat(map).doesNotContainKey(null);
        assertThat(map).isEmpty();
    }

    // --- size / isEmpty / clear ---

    @Test
    public void size_includesNullKeyInCount() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put("key1", "val1");
        map.put(null, "val2");

        assertThat(map).hasSize(2);
    }

    @Test
    public void isEmpty_returnsFalseWhenNullKeyExists() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val");

        assertThat(map).isNotEmpty();
    }

    @Test
    public void clear_removesNullKeyAsWell() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val");
        map.put("key1", "val1");

        map.clear();

        assertThat(map).doesNotContainKey(null).doesNotContainKey("key1").isEmpty();
    }

    // --- Collection views (read-only) ---

    @Test
    public void keySet_containsNullKeyAndRejectsModification() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val");

        final Set<String> keys = map.keySet();

        assertThat(keys.contains(null)).isTrue();
        assertThatThrownBy(() -> keys.remove(null)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void entrySet_containsNullKeyEntryAndRejectsModification() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val");

        final Set<Map.Entry<String, String>> entries = map.entrySet();

        assertThat(entries).hasSize(1);
        assertThat(entries.iterator().next().getKey()).isNull();

        assertThatThrownBy(() -> entries.remove(entries.iterator().next())).isInstanceOf(UnsupportedOperationException.class);
    }

    // --- equals / hashCode ---

    @Test
    public void equals_matchesStandardHashMapWithSameContent() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val1");
        map.put("key2", "val2");

        final Map<String, String> standardMap = new HashMap<>();
        standardMap.put(null, "val1");
        standardMap.put("key2", "val2");

        assertThat(map).isEqualTo(standardMap);
        assertThat(standardMap).isEqualTo(map);
    }

    @Test
    public void equals_returnsFalseWhenNullKeyValuesDiffer() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val1");

        final Map<String, String> standardMap = new HashMap<>();
        standardMap.put(null, "different_val");

        assertThat(map).isNotEqualTo(standardMap);
    }

    @Test
    public void equals_returnsFalseWhenNullKeyPresenceDiffers() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val1");

        final Map<String, String> standardMap = new HashMap<>();
        standardMap.put("key1", "val1");

        assertThat(map).isNotEqualTo(standardMap);
    }

    @Test
    public void hashCode_matchesStandardHashMapWithSameContent() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "val1");
        map.put("key2", "val2");

        final Map<String, String> standardMap = new HashMap<>();
        standardMap.put(null, "val1");
        standardMap.put("key2", "val2");

        assertThat(map.hashCode()).isEqualTo(standardMap.hashCode());
    }

    // --- Java 8+ default methods ---

    @Test
    public void getOrDefault_returnsDefaultWhenNullKeyAbsent() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);

        assertThat(map.getOrDefault(null, "default_val")).isEqualTo("default_val");

        map.put(null, "actual_val");
        assertThat(map.getOrDefault(null, "default_val")).isEqualTo("actual_val");
    }

    @Test
    public void putIfAbsent_insertsOnlyWhenNullKeyIsAbsent() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);

        final String old1 = map.putIfAbsent(null, "first_val");
        assertThat(old1).isNull();
        assertThat(map).containsEntry(null, "first_val");

        final String old2 = map.putIfAbsent(null, "second_val");
        assertThat(old2).isEqualTo("first_val");
        assertThat(map).containsEntry(null, "first_val");
    }

    @Test
    public void remove_withValue_removesOnlyWhenBothKeyAndValueMatch() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "target_val");

        final boolean removedWrong = map.remove(null, "wrong_val");
        assertThat(removedWrong).isFalse();
        assertThat(map).containsEntry(null, "target_val");

        final boolean removedCorrect = map.remove(null, "target_val");
        assertThat(removedCorrect).isTrue();
        assertThat(map).doesNotContainKey(null);
    }

    @Test
    public void replace_withOldValue_replacesOnlyWhenOldValueMatches() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);
        map.put(null, "old_val");

        final boolean replacedWrong = map.replace(null, "wrong_val", "new_val");
        assertThat(replacedWrong).isFalse();
        assertThat(map).containsEntry(null, "old_val");

        final boolean replacedCorrect = map.replace(null, "old_val", "new_val");
        assertThat(replacedCorrect).isTrue();
        assertThat(map).containsEntry(null, "new_val");
    }

    @Test
    public void replace_replacesOnlyWhenNullKeyAlreadyExists() {
        final Map<String, String> map = new NullSupportedSwissMap<>(16);

        final String replacedEmpty = map.replace(null, "new_val");
        assertThat(replacedEmpty).isNull();
        assertThat(map).doesNotContainKey(null);

        map.put(null, "old_val");
        final String replacedExisting = map.replace(null, "new_val");
        assertThat(replacedExisting).isEqualTo("old_val");
        assertThat(map).containsEntry(null, "new_val");
    }
}
