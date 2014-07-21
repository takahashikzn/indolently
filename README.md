<!--
  @author takahashikzn
-->

What this is
=================

A Java syntax sugar library for indolent person.

Intend to provide object literal syntax like list/set/map.


Requirement
=================

Java8. That is all.


Dependencies
=================

There is no dependencies.


Installation
=================

Copy single file "Indolently.java" to your source directory.


How to use
=================

```java
import static jp.root42.indolently.Indolently.*;


final Map<String, Integer> shortMapDecl = map(); // equivalent to "new HashMap<>()"
final List<Integer> shortListDecl = list();      // equivalent to "new ArrayList<>()"
final Set<String> shortSetDecl = set();          // equivalent to "new HashSet<>()"


final Map<String, Object> simple = map(
    "int", 1,
    "string", "abc",
    "level1", map(
        "level2", map(
            "level3", list(
                map("level4", 42)
            )
        )
    )
);


// a boring instruction for building 'simple' instance equivalence
final Map<String, Object> boring = new HashMap<String, Object>() {
    {
        final Map<String, Object> level1 = new HashMap<>();
        final Map<String, Object> level2 = new HashMap<>();
        final List<Map<String, Object>> level3 = new ArrayList<>();
        final Map<String, Object> level4 = new HashMap<>();

        this.put("int", 1);
        this.put("string", "abc");
        this.put("level1", level1);
        level1.put("level2", level2);
        level2.put("level3", level3);
        level3.add(level4);
        level4.put("level4", 42);
    }
};


// internal iterators
list(1, 2, 3)
    .map((i) -> i * i)
    .reduce((i, k) -> i + k)
    .each(System.out::println);
```

See JUnit testcase for more details.


Translation
=================

Currently most documents would written in Japanese.
Your contribution is welcome.
