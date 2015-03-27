# kbuilders

One of the most frustrating aspects of working with [Protocol Buffers](https://github.com/google/protobuf) is the unwieldy construction syntax, especially when building unit tests.

This [Kotlin](kotlinlang.org) tool applies the [Type-Safe Builder](http://kotlinlang.org/docs/reference/type-safe-builders.html) pattern to your protobuf builders (or any Builders!), so that you can easily construct new objects with a nicer syntax.

So that `Person.Builder().firstName("Aaron").lastName("Sarazan").build()` becomes
```kotlin
person {
  firstName { "Aaron" } // For basic types, you can use block syntax...
  lastName("Sarazan") // ...or parameter syntax!
}
```

###Build
To build this project, execute `./gradlew jar`. This will produce `build/libs/kbuilders.jar`.

###Usage

This project is still in very early development, so the usage is pretty spartan:

```bash
java -jar kbuilders.jar --protoRoot=<root of java proto files> --kotlinRoot=<root of destination kotlin files>
```

This will produce a `.kt` file for each `.java` file in the tree that contains builders.

###Known Issues
* Still has some trouble with internal classes.
* Only tested with [Wire](https://github.com/square/wire). Should theoretically work with any builder implementation.
