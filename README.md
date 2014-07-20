<!--
  @author takahashikzn
-->

Indolently
=================

A Java syntax sugar library for indolent person.


Requirement
=================

Java8. That is all.


Dependencies
=================

There is no dependencies.


How to use
=================

'''java
import static jp.root42.indolently.Indolently.*;


final Map<String, Object> simple = map(
    "int", 1
        , "level1"
        , map(
            "level2"
            , map(
                "level3",
                list(
                    map("level4", 42)
                )
            )
        )
    );

final Map<Object, Object> boring = new HashMap<Object, Object>() {
    private static final long serialVersionUID = 1L;
    {
        final Map<String, Object> level1 = new HashMap<>();
        final Map<String, Object> level2 = new HashMap<>();
        final List<Map<String, Object>> level3 = new ArrayList<>();
        final Map<String, Object> level4 = new HashMap<>();

        this.put("int", 1);
        this.put("level1", level1);
        level1.put("level2", level2);
        level2.put("level3", level3);
        level3.add(level4);
        level4.put("level4", 42);
    }
};
'''

See JUnit testcase for more details.

<src/test/java/jp/root42/indolently/IndolentlyTest.java>


Translation wanted!
=================

Currently most documents would written in Japanese.
Your contribution is welcome.
