package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.levelmoney.kbuilders.javaparser.adapters.ClassNameCollectorVisitor
import com.levelmoney.kbuilders.javaparser.adapters.GetBuilderVisitor

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

public fun CompilationUnit.getBuilders(): List<ClassOrInterfaceDeclaration> {
    val retval = arrayListOf<ClassOrInterfaceDeclaration>()
    GetBuilderVisitor().visit(this, retval)
    return retval
}

public fun CompilationUnit.getRequiredImports(): List<String> {
    val retval = arrayListOf(getPackage().getName().toString() + ".*")
    ClassNameCollectorVisitor(getPackage().getName().toString()).visit(this, retval)
    retval.addAll(getImports().map { it.getName().toString() })
    return retval.filterImports()
}

private fun List<String>.filterImports(): List<String> {
    return filter {
        !(it.startsWith("java."))
    }
}
