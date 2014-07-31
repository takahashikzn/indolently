<!--
  @author takahashikzn
-->

# What this is

A Java syntax sugar library for indolent person.

Intend to provide semi-literal syntax like list/set/map.


This is also my study of English/GitHub/Java8-Lambda.


# Requirement

Java8. That is all.


# Dependencies

There is no runtime dependency.

To build, <a href="http://ant.apache.org/">Apache Ant</a> and <a href="http://ant.apache.org/ivy/">Apache Ivy</a> are required.


# Installation

Just type <code>ant [RET]</code> at project root.

A moment later, you will find a jar file at ./target/indolently.jar


# How to use

### import required functions using static import.

```java
import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Functional.*;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;
```


### List/Set/Map construction

```java
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
```


### Operation Chain

```java
// operation chaining without the "unfriendly" java.util.Stream
range(1, 10)
    .list()
    .slice(-5, 0) // negative index is acceptable - take last five elements
    .map((i) -> i * i)
    .each(System.out::println)
    .reduce(0, (i, k) -> i + k)
    .ifPresent(System.out::println); // print 330
```


### Match expression

```java
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

// equivalent to range(-2, 5).reduce((l, r) -> l + r).get() => 12
Systme.out.println(sumOfRange(-2, 5));
```


### Function expression beyond java8's lambda syntax

```java
// memoized fibonacci function
final Function<Integer, Integer> fib = function(

    // function declaration/initialization section: The compiler requires this to do type inference
    (Function<Integer, Integer> self) -> { System.out.println("initialized!"); },

    // function body section
    (self, x) -> (x <= 1) ? x : self.apply(x - 1) + self.apply(x - 2),

).memoize();

// print list of first 42nd fibonacci numbers
System.out.println(
    range(0, 42)
    .mapred(
        list(),
        (rem, val) -> rem.push(fib.apply(val))
    )
    .get()
);


// memoized Tarai function: http://en.wikipedia.org/wiki/Tak_%28function%29
int tarai20 = function(
    (Function<Trio<Integer, Integer, Integer>, Integer> self) -> {},
    (self, v) -> {

    final int x = v.fst;
    final int y = v.snd;
    final int z = v.trd;

    if (y < x) {
        return self.apply(
            tuple(
                self.apply(tuple(x - 1, y, z)),
                self.apply(tuple(y - 1, z, x)),
                self.apply(tuple(z - 1, x, y))));
    } else {
        return y;
    }
}).memoize().apply(tuple(20, 6, 0));
```

See JUnit testcase for more details.


# Notice

Currently production use is strongly not recommended
because the codes are almost not tested and incompatibly changed very frequently.


# Translation

Currently most documents would written in broken English
bacause of this project's purpose itself.

Your contribution is welcome.
