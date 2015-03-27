# kbuilders

One of the most frustrating aspects of working with [Protocol Buffers](https://github.com/google/protobuf) is the unwieldy construction syntax, especially when building unit tests.

This [Kotlin](kotlinlang.org) tool applies the [Type-Safe Builder](http://kotlinlang.org/docs/reference/type-safe-builders.html) pattern to your protobuf builders, so that you can easily construct new objects with a nicer syntax.

So that `Person.Builder().firstName("Aaron").lastName("Sarazan").build()` becomes
```kotlin
person {
  firstName { "Aaron" } // For basic types, you can use block syntax...
  lastName("Sarazan") // ...or parameter syntax!
}
```

###Build
To build this project, execute `./gradlew jar`. This will produce `build/libs/kbuilder.jar`.

###Usage

This project is still in very early development, so the usage is pretty spartan:

```bash
java -jar kbuilder.jar <java file>
```

This will output monolithic Kotlin code containing the necessary extension methods for all builders in the file. You should probably redirect it into a file somewhere.

###Known Issues
* Doesn't currently play nice with enum types, as it doesn't know to import them. Fix incoming.
* IDEA's indexer really hates the monolithic approach. Would be nice to have it smartly create a hierarchical directory of extension files.
* Only tested with [Wire](https://github.com/square/wire). Should theoretically work with any builder implementation.
