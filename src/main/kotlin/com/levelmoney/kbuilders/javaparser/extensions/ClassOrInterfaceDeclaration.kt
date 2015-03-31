package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.type.ReferenceType
import com.levelmoney.kbuilders.Config

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

public fun ClassOrInterfaceDeclaration.getInternalClasses(): List<ClassOrInterfaceDeclaration> {
    return getMembers().filterIsInstance<ClassOrInterfaceDeclaration>()
}

// This could return false positives but should be adequate for v1.0
public fun ClassOrInterfaceDeclaration.getBuilderClass(): ClassOrInterfaceDeclaration? {
    return getInternalClasses().firstOrNull { it.isBuilder() }
}

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
    return getMembers().filterIsInstance<MethodDeclaration>()
}

public fun ClassOrInterfaceDeclaration.assertIsBuilder() {
    if (!isBuilder()) throw IllegalStateException()
}

public fun ClassOrInterfaceDeclaration.getCreator(config: Config): String {
    assertIsBuilder()
    val type = getTypeForThisBuilder().toString()
    val inline = if (config.inline) " inline " else " "
    return """public${inline}fun build$type(fn: $type.Builder.() -> Unit): $type {
    val builder = $type.Builder()
    builder.fn()
    return builder.build()
}"""
}

public fun ClassOrInterfaceDeclaration.getRebuild(config: Config): String {
    assertIsBuilder()
    val type = getTypeForThisBuilder().toString()
    val inline = if (config.inline) " inline " else " "
    return """public${inline}fun $type.rebuild(fn: $type.Builder.() -> Unit): $type {
    val builder = $type.Builder(this)
    builder.fn()
    return builder.build()
}"""
}

public fun ClassOrInterfaceDeclaration.getMethodStrings(config: Config): List<String> {
    val retval = arrayListOf(getCreator(config),getRebuild(config))
    retval.addAll(getBuilderMethods().flatMap { it.toKotlin(config) })
    return retval
}