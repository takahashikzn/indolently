<!-- @author takahashikzn -->

[![TravisCI](http://travis-ci.org/takahashikzn/indolently.svg?branch=develop)](http://travis-ci.org/takahashikzn/indolently)

# What this is

A Java syntactic sugar library for indolent guys.

This library was started to provide semi-literal syntactic expression like list/set/map.
Now there are miscellaneous syntactic sugars like let/eval and more.

This is also my study of English/GitHub/Java8-Lambda.


# Requirement

Java17. That is all.


# Dependencies

### Runtime
* [typetools](http://github.com/jhalterman/typetools)
* [fastutil](http://fastutil.di.unimi.it/) (Optional)
* [Koloboke](http://github.com/OpenHFT/Koloboke) (Optional)
* [Eclipse Collection](http://www.eclipse.org/collections/) (Optional)
* [RE2](http://github.com/google/re2/) (Optional)
* [Automaton](http://www.brics.dk/automaton/) (Optional)

### Test
* [JUnit](http://junit.org/)
* [Hamcrest](http://hamcrest.org/JavaHamcrest/)
* [AssertJ](http://joel-costigliola.github.io/assertj/)
* [Mockito](http://mockito.org/)
* [JUnitParams](http://pragmatists.github.io/JUnitParams/)

### Build
* [Apache Ant](http://ant.apache.org/)
* [Apache Ivy](http://ant.apache.org/ivy/)


# Installation

Just type <code>ant [RET]</code> at project root.

A moment later, you will find a jar file in ./target directory.


# How to use

### Preparation for use

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
final Map<String, Object> boring = Collections.unmodifiableMap(
    new HashMap<String, Object>() {
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
    }
);
```


### Conditional statement without local variable declaration

```java
// This local variable declaration is required if doing like a below
final int i = new Random().nextInt();
if (i % 2 == 0) {
    System.out.println(i + " is even number");
} else {
    System.out.println(i + " is odd number");
}


// declaration not required
let(
    new Random().nextInt(),
    i -> i % 2 == 0,
    i -> System.out.println(i + " is even number"),
    i -> System.out.println(i + " is odd number")
);
```


### Operation Chain

```java
// operation chaining on list with enhanced stream methods
range(1, 10)
    .list()
    .slice(-5, 0) // negative index is acceptable - take last five elements
    .map((i) -> i * i)
    .each(System.out::println) // each is not terminal operation
    .reduce((i, k) -> i + k)
    .ifPresent(System.out::println); // print 330
```


### when-then ladder expression

```java
// Totally wasteful manner of computing sum of integer range
int sumOfRange(final int from, final int to) {

    return list(
        iterator(
            ref(from),

            ref ->
                when(from < to).
                    then(() -> ref.val <= to).
                when(to < from).
                    then(() -> to <= ref.val).
                none(() -> ref.val == from),

            ref ->
                when(from < to).
                    then(() -> ref.getThen(self -> self.val += step)).
                none(() -> prog1(ref::get, () -> ref.val -= step))
        )
    ).reduce((l, r) -> l + r);
}

// equivalent to range(-2, 5).reduce((l, r) -> l + r).get() => 12
System.out.println(sumOfRange(-2, 5));
```


### Function expression beyond java8's lambda syntax

```java
// Memoized version of The tarai function
// (see http://en.wikipedia.org/wiki/Tak_%28function%29)
int tarai20 = function(

    // Function declaration/initialization section.
    // Inline function expression requires extra type information
    // to do type inference.
    (Function3<Integer, Integer, Integer, Integer> self) ->
        System.out.println("initialized!"),

    // function body section
    (self, x, y, z) ->
        (y < x)
            ? self.apply(
                self.apply(x - 1, y, z),
                self.apply(y - 1, z, x),
                self.apply(z - 1, x, y))
            : y
).memoize().apply(20, 6, 0);


// fibonacci function
// NOTE: 'func' is an alias of 'function' but not overloaded one
final SFunc<Integer, Integer> fib = func(
    // In this case, extra type information not required
    self -> {},
    (self, x) -> (x <= 1) ? x : self.apply(x - 1) + self.apply(x - 2)
);

final Function<Integer, Integer> memoFib = this.fib.memoize();

// print list of first 42nd fibonacci numbers
range(0, 42)
    .reduce(
        list(),
        (rem, val) -> rem.push(memoFib.apply(val))
    )
    .each(System.out::println);
```

See JUnit testcase for more details.


# Notice

I'm using this library in my daily works but it is still in experimental stage.

Currently production use is not recommended
because the codes are largely not tested and incompatibly changed very frequently.


# Translation

Currently most documents would written in broken English
bacause of this project's purpose itself.

Your contribution is welcome.
