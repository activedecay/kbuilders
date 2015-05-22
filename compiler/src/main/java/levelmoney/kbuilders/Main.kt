/*
 * Copyright 2015 Level Money, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.levelmoney.kbuilders

import com.github.javaparser.JavaParser
import com.levelmoney.kbuilders.javaparser.extensions.getBuilders
import com.levelmoney.kbuilders.javaparser.extensions.getMethodStrings
import com.levelmoney.kbuilders.javaparser.extensions.getRequiredImports
import joptsimple.OptionParser
import joptsimple.OptionSet
import java.io.File

/**
 * java -jar kbuilder.jar --javaRoot=<dir> --kotlinRoot=<dir>
 */
fun main(args : Array<String>) {
    if (args.size() < 2) {
        println("Usage: java -jar kbuilder.jar --javaRoot=<dir> --kotlinRoot=<dir>")
        return
    }
    val parser = OptionParser()
    parser.accepts("javaRoot").withRequiredArg()
    parser.accepts("kotlinRoot").withRequiredArg()
    val options = parser.parse(*args)

    val javaRoot = options.valueOf("javaRoot").toString()
    val kotlinRoot = options.valueOf("kotlinRoot").toString()
    val config = Config(
            inline = true
    )

    val dir = File(javaRoot)
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
