# JLens

![build](https://github.com/kfabi/jlens/actions/workflows/build.yml/badge.svg)

## What is JLens
JLens introduces Lenses for java records. Lenses allow the user to 'modify' deeply nested record structures. Consider the following situation:

````java
import java.util.List;

public record MyRecord(String someString, int someInt, MyNestedRecord nested) {
}

public record MyNestedRecord(boolean someBoolean, List<String> someStrings) {
}

MyRecord instance = new MyRecord("Hello, World!", 1, new MyNestedRecord(true, List.of("my element")));
````

and you have an ``instance`` of ``MyRecord`` where u just want to have a new ``changedInstance`` where the value of ``myRecord.nested().someBoolean()`` is set to false instead of true. 
You now have to do the following:

````java
MyRecord changedInstance = new MyRecord(instance.someString(), instance.someInt(), new MyNestedRecord(false, instance.nested().someStrings()));
````

You can imagine that this would become quite tedious quite quickly. 
Especially if the structure was nested even deeper or the records have more components.

But JLens comes to the rescue! With JLens you can annotate your records with the ``@Lenses`` annotation and the JLens-annotation-processor will generate lenses for the annotated records.

With lenses the tedious task of *setting* ``myRecord.nested().someBoolean()`` to false looks like this:

````java
MyRecord changedWithLens = MyRecordLenses.nested().compose(MyNestedRecordLenses.someBoolean()).set(instance, false);
````

Looks quite a bit nicer, doesn't it? And if you squint your eyes a bit, you can almost see ``instance.nested().someBoolean().set(false)``.

In this example u can see that Lenses *compose* nicely and that you just need to compose one more Lens for every level of nesting.

## How to use

### Gradle:
add the lens module as an ``implementation`` dependency and teh annotation-processor as an ``annotationProcessor`` dependency.
Now just annotate your records with ``@Lenses`` and compile your project and the lenses should be generated.

## Supported Features

JLens can generate lenses for records with:
* Regular types like ``String``
* Primitive types
* Generic types e.g. ```record MyRecord<T>(T t) {}```

Naming Scheme:

For top-level records the name will be <Record>Lenses. e.g.: ``record MyRecord() {} -> MyRecordLenses``

For records that are inner classes the name will be <OuterClass><Record>Lenses. eg.: ``class Outer { record Innner() {} } -> OuterInnerLenses``

The lenses will be generated in the same package as the record.