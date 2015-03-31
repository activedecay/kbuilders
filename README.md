# kbuilders

One of the most frustrating aspects of working with [Protocol Buffers](https://github.com/google/protobuf) is the unwieldy construction syntax, especially when building unit tests.

This [Kotlin](kotlinlang.org) tool applies the [Type-Safe Builder](http://kotlinlang.org/docs/reference/type-safe-builders.html) pattern to any source code implementing the [Builder Pattern](http://en.wikipedia.org/wiki/Builder_pattern), so that you can easily construct new objects with a nicer syntax.

So that 
```
Person.Builder()
  .firstName("Aaron")
  .lastName("Sarazan")
  .address(Address.Builder()
    .number(847)
    .street("Sansome")
    .addressType(AddressType.BUSINESS)
    .build()
  ).build()
``` 
becomes
```kotlin
buildPerson {
  firstName { "Aaron" } // For basic types, you can use block syntax...
  lastName("Sarazan") // ...or parameter syntax!
  address { buildAddress {
    number(847)
    street("Sansome")
    addressType(AddressType.BUSINESS)
  } }
}
```

###Build
To build this project, execute `./gradlew jar`. This will produce `build/libs/kbuilders.jar`.

###Usage

This project is still in very early development, so the usage is pretty spartan:

```bash
java -jar kbuilders.jar --protoRoot=<root of java proto files> --kotlinRoot=<root of destination kotlin files> [--inline] [--methodPrefix=<prefix>]
```

This will produce a `.kt` file for each `.java` file in the tree that contains builders.

###Known Issues
* Only tested with [Wire](https://github.com/square/wire). Should theoretically work with any builder implementation.
