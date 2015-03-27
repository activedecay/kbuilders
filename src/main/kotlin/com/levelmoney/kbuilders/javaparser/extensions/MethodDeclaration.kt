package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

public fun MethodDeclaration.isBuildMethod():Boolean {
    return getName().equals("build") && !getType().toString().equals("void")
}

public fun MethodDeclaration.toKotlin(): String {
    assertIsBuilder()
    val builderClass = getClassOrInterface()
    val enclosing = builderClass.getTypeForThisBuilder()
    val builderName = builderClass.getName()
    val type = getParameters().first().getType().toString()
    val name = getName ()
    return "public fun $enclosing.$builderName.$name(fn: () -> $type): $enclosing.$builderName = $name(fn())"
}

public fun MethodDeclaration.getClassOrInterface(): ClassOrInterfaceDeclaration {
    return getParentNode() as ClassOrInterfaceDeclaration
}

//public fun MethodDeclaration.getImports(): List<ClassOrInterfaceDeclaration> {
//    val retval = arrayListOf<ClassOrInterfaceDeclaration>()
//    retval.add(getParentNode() as ClassOrInterfaceDeclaration)
//    retval.addAll(getParameters().map { (it.getType() as ClassOrInterfaceType). })
//    return retval
//}

public fun MethodDeclaration.assertIsBuilder() {
    getClassOrInterface().assertIsBuilder()
}
