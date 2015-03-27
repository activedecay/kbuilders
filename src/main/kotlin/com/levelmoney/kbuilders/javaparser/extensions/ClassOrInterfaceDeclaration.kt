package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.type.ReferenceType
import com.levelmoney.kbuilders.Config

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

public fun ClassOrInterfaceDeclaration.getBuilderMethods(): List<MethodDeclaration> {
    return getMethods().filter {
        it.getType().toString().equals(getName()) && it.getParameters()?.size()?:0 > 0
    }
}

public fun ClassOrInterfaceDeclaration.getBuildMethod(): MethodDeclaration? {
    return getMethods().firstOrNull { it.isBuildMethod () }
}

public fun ClassOrInterfaceDeclaration.isBuilder(): Boolean {
    return getBuildMethod() != null
}

public fun ClassOrInterfaceDeclaration.getTypeForThisBuilder(): ReferenceType {
    return getBuildMethod()!!.getType() as ReferenceType
}

public fun ClassOrInterfaceDeclaration.getMethods(): List<MethodDeclaration> {
    val retval = arrayListOf<MethodDeclaration>()
    getMembers().forEach { if (it is MethodDeclaration) retval.add(it) }
    return retval
}

public fun ClassOrInterfaceDeclaration.assertIsBuilder() {
    if (!isBuilder()) throw IllegalStateException()
}

public fun ClassOrInterfaceDeclaration.getCreator(): String {
    assertIsBuilder()
    val type = getTypeForThisBuilder().toString()
    return """public fun build$type(fn: $type.Builder.() -> Unit): $type {
    val builder = $type.Builder()
    builder.fn()
    return builder.build()
}"""
}

public fun ClassOrInterfaceDeclaration.getMethodStrings(config: Config): List<String> {
    val retval = arrayListOf(getCreator())
    retval.addAll(getBuilderMethods().map { it.toKotlin(config) })
    return retval
}