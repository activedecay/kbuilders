package com.levelmoney.kbuilders

import com.github.javaparser.JavaParser
import com.levelmoney.kbuilders.javaparser.extensions.getBuilders
import com.levelmoney.kbuilders.javaparser.extensions.getMethodStrings
import com.levelmoney.kbuilders.javaparser.extensions.getRequiredImports
import joptsimple.OptionParser
import joptsimple.OptionSet
import java.io.File

/**
 * Created by Aaron Sarazan on 3/22/15
 * Copyright(c) 2015 Level, Inc.
 */

/**
 * java -jar kbuilder.jar --proto_root=<dir> --kotlin_root=<dir>
 */
fun main(args : Array<String>) {
    if (args.size() < 2) {
        println("Usage: java -jar kbuilder.jar --proto_root=<dir> --kotlin_root=<dir>")
        return
    }
    val parser = OptionParser()
    parser.accepts("proto_root").withRequiredArg()
    parser.accepts("kotlin_root").withRequiredArg()
    val options = parser.parse(*args)

    val protoRoot = options.valueOf("proto_root").toString()
    val kotlinRoot = options.valueOf("kotlin_root").toString()

    val dir = File(protoRoot)
    dir.recurse {
        if (it.isFile()) {
            val pkgAndText = generatePackageAndText(it)
            if (pkgAndText != null) {
                val name = it.getName().replace("."+it.extension, ".kt")
                val (pkg, text) = pkgAndText
                val dest = File(kotlinRoot, pkg.split("\\.").join("/") + "/" + name)
                dest.getParentFile().mkdirs()
                dest.writeText(text)
            }
        }
    }
}


/**
 * Generates a full kotlin file from the provided java files.
 */
public fun generatePackageAndText(file: File): Pair<String, String>? {
    val cu = JavaParser.parse(file)
    val pakage = cu.getPackage().getName().toString()
    val imports = cu.getRequiredImports()
    val builders = cu.getBuilders()
    if (builders.isEmpty()) return null
    val methods = cu.getBuilders().flatMap { it.getMethodStrings() }
    return Pair(pakage,
"""package $pakage

${imports.map{ "import " + it }.join("\n")}

${methods.join("\n")}
""")
}
