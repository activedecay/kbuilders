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

package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.body.*
import com.github.javaparser.ast.type.ReferenceType
import com.levelmoney.kbuilders.Config
import levelmoney.kbuilders.javaparser.extensions.hasParameters

public fun ClassOrInterfaceDeclaration.getInternalClasses(): List<ClassOrInterfaceDeclaration> {
    return getMembers().filterIsInstance<ClassOrInterfaceDeclaration>()
}

public fun ClassOrInterfaceDeclaration.getParentClass(): ClassOrInterfaceDeclaration? {
    return getParentNode() as ClassOrInterfaceDeclaration
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

public fun ClassOrInterfaceDeclaration.getDefaultCtor(): ConstructorDeclaration? {
    return getConstructors().firstOrNull {
        it.hasParameters(0) && it.getModifiers().and(ModifierSet.PUBLIC) != 0
    }
}

public fun ClassOrInterfaceDeclaration.getCopyCtor(): ConstructorDeclaration? {
    return getConstructors().firstOrNull {
        it.hasParameters(1)
                && it.getParameters()[0].getType().toString().equals(getParentClass()!!.getName())
                && it.getModifiers().and(ModifierSet.PUBLIC) != 0
    }
}

private fun ClassOrInterfaceDeclaration.getParentStaticFactory(): MethodDeclaration? {
    return getParentClass()!!.getMethods().firstOrNull {
        it.hasParameters(0)
                && it.getName().equals("newBuilder")
    }
}

private fun ClassOrInterfaceDeclaration.getParentCopyStaticFactory(): MethodDeclaration? {
    return getParentClass()!!.getMethods().firstOrNull {
        it.hasParameters(1)
                && it.getParameters()[0].getType().toString().equals(getParentClass()!!.getName())
                && it.getName().equals("newBuilder")
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

public fun ClassOrInterfaceDeclaration.getConstructors(): List<ConstructorDeclaration> {
    return getMembers().filterIsInstance<ConstructorDeclaration>()
}

public fun ClassOrInterfaceDeclaration.assertIsBuilder() {
    if (!isBuilder()) throw IllegalStateException()
}

/**
 * Run on the Builder class. If it has a private constructor it will search
 * the parent for a newBuilder() method.
 *
 * Thanks Google...
 */
private fun ClassOrInterfaceDeclaration.builderGetCtor(): String {
    return getDefaultCtor()?.getName()?:getParentStaticFactory()!!.getName()
}
private fun ClassOrInterfaceDeclaration.builderGetCopyCtor(): String? {
    return getCopyCtor()?.getName()?:getParentCopyStaticFactory()?.getName()
}

public fun ClassOrInterfaceDeclaration.getCreator(config: Config): String {
    assertIsBuilder()
    val parent = getParentNode() as ClassOrInterfaceDeclaration
    val type = parent.getName()
    val inline = if (config.inline) " inline " else " "
    val newBuilder = builderGetCtor()
    return """public${inline}fun build$type(fn: $type.Builder.() -> Unit): $type {
    val builder = $type.$newBuilder()
    builder.fn()
    return builder.build()
}"""
}

public fun ClassOrInterfaceDeclaration.getRebuild(config: Config): String? {
    assertIsBuilder()
    val parent = getParentNode() as ClassOrInterfaceDeclaration
    val type = parent.getName()
    val inline = if (config.inline) " inline " else " "
    val newBuilder = builderGetCopyCtor()
    if (newBuilder == null) return null
    return """public${inline}fun $type.rebuild(fn: $type.Builder.() -> Unit): $type {
    val builder = $type.$newBuilder(this)
    builder.fn()
    return builder.build()
}"""
}

public fun ClassOrInterfaceDeclaration.getMethodStrings(config: Config): List<String> {
    val retval = arrayListOf(getCreator(config))
    val rebuild = getRebuild(config)
    if (rebuild != null) retval.add(rebuild)
    // This would enable k-combinator syntax for every property in the builder. We can re-enable if people want it.
//    retval.addAll(getBuilderMethods().flatMap { it.toKotlin(config) })
    return retval
}