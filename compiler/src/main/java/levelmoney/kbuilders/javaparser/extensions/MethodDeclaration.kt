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

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.levelmoney.kbuilders.Config

public fun MethodDeclaration.hasParameters(count: Int? = null): Boolean {
    val params = getParameters()
    if (params == null) return count == 0
    return count == null || params.size() == count
}

public fun MethodDeclaration.isBuildMethod():Boolean {
    return getName().equals("build") && !getType().toString().equals("void")
}

public fun MethodDeclaration.toKotlin(config: Config): List<String> {
    val retval = arrayListOf(baseKotlin(config))
    // Once we can reliably link TypeParameter <-> ClassOrInterfaceDeclaration, we can add a shortcut method here.
    return retval
}

public fun MethodDeclaration.baseKotlin(config: Config): String {
    assertIsBuilder()
    val builderClass = getClassOrInterface()
    val enclosing = builderClass.getTypeForThisBuilder()
    val builderName = builderClass.getName()
    val type = kotlinifyType(getParameters().first().getType().toString())
    val name = getName()
    val inline = if (config.inline) " inline " else " "
    return "public${inline}fun $enclosing.$builderName.build$name(fn: () -> $type): $enclosing.$builderName = $name(fn())"
}

public fun MethodDeclaration.getClassOrInterface(): ClassOrInterfaceDeclaration {
    return getParentNode() as ClassOrInterfaceDeclaration
}

public fun kotlinifyType(type: String): String {
    return when (type) {
        "byte" -> "Byte"
        "short" -> "Short"
        "int", "Integer" -> "Int"
        "long" -> "Long"
        "float" -> "Float"
        "double" -> "Double"
        "boolean" -> "Boolean"
        "char" -> "Char"
        else -> kotlinifyOther(type)
    }
}

public fun kotlinifyOther(type: String): String {
    return when {
        type.endsWith("[]") -> kotlinifyType(type.replace("[]", "")) + "Array"
        else -> type
    }
}

public fun MethodDeclaration.assertIsBuilder() {
    getClassOrInterface().assertIsBuilder()
}
