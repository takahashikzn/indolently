<!--
  @author takahashikzn
-->

What this is
=================

A Java syntax sugar library for indolent person.

Intend to provide semi-literal syntax like list/set/map.


This is also my study of English/GitHub/Java8-Lambda.


Requirement
=================

Java8. That is all.


Dependencies
=================

There is no dependencies.


Installation
=================

Copy the directory "jp/root42/indolently" to your source directory.


After a time, the build file(s) for ant/maven would be available.


How to use
=================

```java
import static jp.root42.indolently.Expressions.*;
import static jp.root42.indolently.Functions.*;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterations.*;


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
).freeze(); // recursively freeze


// a boring instruction for building 'simple' instance equivalence
final Map<String, Object> boring = Collections.unmodifiableMap(new HashMap<String, Object>() {
    {
        final Map<String, Object> level1 = new HashMap<>();
        final Map<String, Object> level2 = new HashMap<>();
        final List<Map<String, Object>> level3 = new ArrayList<>();
        final Map<String, Object> level4 = new HashMap<>();

        this.put("int", 1);
        this.put("string", "abc");
        this.put("level1", level1);
        level1.put("level2", Collections.unmodifiableMap(level2));
        level2.put("level3", Collections.unmodifiableList(level3));
        level3.add(Collections.unmodifiableMap(level4));
        level4.put("level4", 42);
    }
});


// operation chaining without the "unfriendly" java.util.Stream
range(1, 10)
    .list()
    .slice(-5, 0) // negative index is acceptable - take last five elements
    .map((i) -> i * i)
    .each(System.out::println)
    .reduce(0, (i, k) -> i + k)
    .ifPresent(System.out::println); // print 330


// Totally wasteful manner of computing sum of integer range
int sumOfRange(final int from, final int to) {

    return list(iterator(
        ref(from),

        env -> match(
                when(
                    (final IntRef x) -> from < to,
                    x -> x.val <= to),
                when(
                    x -> to < from,
                    x -> to <= x.val)
                ).defaults(
                    x -> x.val == from
            ).apply(env),

        env -> match(
                when(
                    (final IntRef x) -> from < to, 
                    x -> prog1(
                            x::get,
                            () -> x.val += 1))
                ).defaults(
                    x -> prog1(
                            x::get, 
                            () -> x.val -= 1)
            ).apply(env)

    )).reduce((l, r) -> l + r).get();
}

// equivalent to range(-2, 5).list().reduce((l, r) -> l + r).get() => 12
Systme.out.println(sumOfRange(-2, 5));


// memoized fibonacci function
final Function<Integer, Integer> fib = function(
    (Function<Integer, Integer> self, Integer x) ->
        (x <= 1) ? x : self.apply(x - 1) + self.apply(x - 2)
    , new FunctionSpec().memoize(true));

// print list of first 42nd fibonacci numbers
System.out.println(
    range(0, 42)
    .mapred(
        list(),
        (rem, val) -> rem.push(fib.apply(val))
    )
    .get()
);
```

See JUnit testcase for more details.


Project status
=================

Currently production use is strongly not recommended
because the codes are almost not tested and changing very frequently.


Translation
=================

Currently most documents would written in broken English
bacause of this project's purpose itself.

Your contribution is welcome.
