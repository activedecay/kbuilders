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
 * java -jar kbuilder.jar --protoRoot=<dir> --kotlinRoot=<dir>
 */
fun main(args : Array<String>) {
    if (args.size() < 2) {
        println("Usage: java -jar kbuilder.jar --protoRoot=<dir> --kotlinRoot=<dir>")
        return
    }
    val parser = OptionParser()
    parser.accepts("protoRoot").withRequiredArg()
    parser.accepts("kotlinRoot").withRequiredArg()
    parser.accepts("methodPrefix").withRequiredArg()
    parser.accepts("inline")
    val options = parser.parse(*args)

    val protoRoot = options.valueOf("protoRoot").toString()
    val kotlinRoot = options.valueOf("kotlinRoot").toString()

    val config = Config(
            inline      = options.has("inline"),
            methodPrefix= options.valueOf("methodPrefix")?.toString()?:""
    )
    val dir = File(protoRoot)
    dir.recurse {
        if (it.isFile()) {
            val pkgAndText = generatePackageAndText(it, config)
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
public fun generatePackageAndText(file: File, config: Config): Pair<String, String>? {
    val cu = JavaParser.parse(file)
    val pakage = cu.getPackage().getName().toString()
    val imports = cu.getRequiredImports()
    val builders = cu.getBuilders()
    if (builders.isEmpty()) return null
    val methods = cu.getBuilders().flatMap { it.getMethodStrings(config) }
    return Pair(pakage,
"""package $pakage

${imports.map{ "import " + it }.join("\n")}

${methods.join("\n")}
""")
}
