package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.ast.type.ReferenceType

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

public fun ClassOrInterfaceDeclaration.builderMethods(): List<MethodDeclaration> {
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

public fun ClassOrInterfaceDeclaration.getBuilderType(): ReferenceType {
    return getBuildMethod()!!.getType() as ReferenceType
}

public fun ClassOrInterfaceDeclaration.getMethods(): List<MethodDeclaration> {
    val retval = arrayListOf<MethodDeclaration>()
    getMembers().forEach { if (it is MethodDeclaration) retval.add(it) }
    return retval
}