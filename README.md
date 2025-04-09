# JLens

![build](https://github.com/kfabi/jlens/actions/workflows/build.yml/badge.svg)
![GitHub Release](https://img.shields.io/github/v/release/kfabi/jlens)

## What is JLens
JLens introduces lenses for java records. Lenses allow the user to 'modify' deeply nested record structures. Consider the following situation:

````java
import java.util.List;

public record MyRecord(String someString, int someInt, MyNestedRecord nested) {
}

public record MyNestedRecord(boolean someBoolean, List<String> someStrings) {
}

MyRecord instance = new MyRecord(
    "Hello, World!", 1, new MyNestedRecord(true, List.of("my element")));
````

and you have an ``instance`` of ``MyRecord`` where you just want to have a new ``changedInstance`` where the value of ``myRecord.nested().someBoolean()`` is set to false instead of true. 
You now have to do the following:

````java
MyRecord changedInstance = new MyRecord(
    instance.someString(), instance.someInt(), 
    new MyNestedRecord(false, instance.nested().someStrings()));
````

You can imagine that this would become quite tedious quite quickly. 
Especially if the structure was nested even deeper or the records have more components.

But JLens comes to the rescue! With JLens you can annotate your records with the ``@Lenses`` annotation and the JLens-annotation-processor will generate lenses for the annotated records.

With lenses the tedious task of *setting* ``myRecord.nested().someBoolean()`` to false looks like this:

````java
MyRecord changedWithLens = MyRecordLenses.nested()
    .compose(MyNestedRecordLenses.someBoolean())
    .set(instance, false);
````

Looks quite a bit nicer, doesn't it? And if you squint your eyes a bit, you can almost see ``instance.nested().someBoolean().set(false)``.

In this example you can see that Lenses *compose* nicely and that you just need to compose one more Lens for every level of nesting.

And if you need to combine multiple state changes, there is a builder with a fluent interface:

`````java
MyRecord changedWithBuilder = 
    CopyBuilder.of(instance)
        .set(MyRecordLenses.nested().compose(MyNestedRecordLenses.someBoolean()), false)
        .modify(MyRecordLenses.someInt(), x -> x + 1)
        .build();
`````

## Installing

### Gradle:
First you need to create a [personal GitHub access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#creating-a-personal-access-token-classic) with the ``read:packages`` scope.

Now you need to add the following properties to the ``gradle.properties`` either at your [gradle user home](https://docs.gradle.org/current/userguide/directory_layout.html#dir:gradle_user_home) (recommended) or in the root of your project:

````properties
gpr.user=<your_github_username>
gpr.key=<your_personal_GitHub_access_token>
````

In your projects ``build.gradle.kts`` add the following repository:

````kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/kfabi/jlens")
        credentials {
            username = project.findProperty("gpr.user") as String?
            password = project.findProperty("gpr.key") as String?
        }
    }
}
````

And declare the following dependencies:

````kotlin
dependencies {
    implementation("de.kfabi:jlens:1.0.2")
    annotationProcessor("de.kfabi:jlens-annotation-processor:1.0.2")
}
````

For more information about installing packages from GitHub Package Registry visit the [official docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package).

## Supported Features

JLens can generate lenses for records with:
* Regular types like ``String``
* Primitive types
* Generic types e.g. ```record MyRecord<T>(T t) {}```

Naming Scheme:

For top-level records the name will be {Record}Lenses. e.g.: ``record MyRecord() {} -> MyRecordLenses``

For records that are inner classes the name will be {OuterClass}{Record}Lenses. e.g.: ``class Outer { record Innner() {} } -> OuterInnerLenses``

The lenses will be generated in the same package as the record.

## Credits

This project is inspired by [arrow-kt](https://arrow-kt.io/learn/immutable-data/). A phenomenal library for typed fp in Kotlin.