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

package com.levelmoney.kbuilders.javaparser.adapters

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.EnumDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

public class ClassNameCollectorVisitor(
        val pakage: String,
        val seen: MutableSet<String> = hashSetOf() // Hail mary to deal with name collisions.
) : VoidVisitorAdapter<MutableList<String>>() {
    override fun visit(n: ClassOrInterfaceDeclaration, arg: MutableList<String>) {
        visitHelper(n, arg)
    }

    override fun visit(n: EnumDeclaration, arg: MutableList<String>) {
        visitHelper(n, arg)
    }

    private fun visitHelper(n: TypeDeclaration, arg: MutableList<String>) {
        val name = n.getName()
        if (seen.contains(name)) return
        seen.add(name)
        val cat = pakage + "." + name
        arg.add(cat)
        n.getMembers().forEach {
            when {
                it is ClassOrInterfaceDeclaration -> {
                    ClassNameCollectorVisitor(cat, seen).visit(it, arg)
                }
                it is EnumDeclaration -> {
                    ClassNameCollectorVisitor(cat, seen).visit(it, arg)
                }
            }
        }
    }

}