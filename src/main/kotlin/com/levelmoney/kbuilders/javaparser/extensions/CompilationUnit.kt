package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
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
