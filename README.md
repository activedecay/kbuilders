Here at Level Money, we really like [Kotlin](https://www.kotlinlang.org). With its small footprint, low overhead, and _spicy musk_, Kotlin is one of the most compelling JVM languages to come out in years; Type inference, lambdas, extension methods-- it's all in there.

# Builders Galore
Another library we use a lot is [Protocol Buffers](https://developers.google.com/protocol-buffers). I'll spare you the details, but suffice it to say we write a _lot_ of code that looks like this:

```Java
Person bilbo = new Person.Builder()
	.firstName("Bilbo")
	.lastName("Baggins")
	.carrying(Arrays.asList(ring, pipe))
	.weapon(new Weapon.Builder()
		.name("Sting")
		.size(Size.SMALL)
		.detects(Race.GOBLINS)
		.build()
	.build();

// And if you want to copy and alter an object:
Person frodo = new Person.Builder(bilbo)
	.firstName("Frodo")
	.build();
```

Wouldn't it be great if we could automatically use Kotlin's [typesafe builders](http://kotlinlang.org/docs/reference/type-safe-builders.html) instead?

# KBuilders

[KBuilders](https://github.com/Levelmoney/kbuilders) is a code generator that will automatically produce Kotlin builder extensions for all your protocol buffer objects.

So now if you want to send your hobbits over the wire, it will look more like this:

```Kotlin
val hobbit = buildPerson {
	firstName = "Bilbo"
	lastName = "Baggins"
	carrying = listOf(ring, pipe)
	weapon = buildWeapon {
		name = "Sting"
		size = Size.SMALL
		detects = Race.GOBLINS
	}
}

// FRODOCOL BUFFERS
val frodo = buildPerson(hobbit) {
	firstName = "Frodo" 
}
```

# Getting Started
To use KBuilders, just grab the [latest JAR](https://github.com/Levelmoney/kbuilders/releases/download/0.9/kbuilders.jar)
```bash
java -jar kbuilders.jar --javaRoot=<root of java proto files> --kotlinRoot=<root of destination kotlin files> [--inline] [--methodPrefix=<prefix>]
```

`KBuilders` supports both Google and Wire protocol buffers, as well as most generic Builder implementations. Pull requests welcome!
