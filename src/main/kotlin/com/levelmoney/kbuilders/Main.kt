package com.levelmoney.kbuilders

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import com.levelmoney.kbuilders.javaparser.extensions.*
import java.io.File

/**
 * Created by Aaron Sarazan on 3/22/15
 * Copyright(c) 2015 Level, Inc.
 */

fun main(args : Array<String>) {
    val file = args[0]
    print(generateString(File(file)))
}


/**
 * Generates a full kotlin file from the provided java files.
 */
public fun generateString(file: File): String {
    val cu = JavaParser.parse(file)
    val pakage = cu.getPackage().getName().toString()
    val imports = cu.getRequiredImports()
    val methods = cu.getBuilders().flatMap { it.getMethodStrings() }
    return """package $pakage

${imports.map{ "import " + it }.join("\n")}

${methods.join("\n")}
"""
}
