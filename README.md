# kbuilders

One of the most frustrating aspects of working with [Protocol Buffers](https://github.com/google/protobuf) is the unwieldy construction syntax, especially when building unit tests.

This [Kotlin](kotlinlang.org) tool applies the [Type-Safe Builder](http://kotlinlang.org/docs/reference/type-safe-builders.html) pattern to your protobuf builders, so that you can easily construct new objects with a nicer syntax.

So that `Person.Builder().firstName("Aaron").lastName("Sarazan").build()` becomes
```
person {
  firstName { "Aaron" } // For basic types, you can use block syntax...
  lastName("Sarazan") // ...or parameter syntax!
}
```

###Usage

This project is still in very early development, so the usage is pretty spartan:

```
./kbuilders <package_name> <java file list>
```
