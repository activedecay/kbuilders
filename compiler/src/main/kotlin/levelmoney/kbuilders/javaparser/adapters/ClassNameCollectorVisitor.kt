package com.levelmoney.kbuilders.javaparser.adapters

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.EnumDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

/**
 * Created by Aaron Sarazan on 3/26/15
 * Copyright(c) 2015 Level, Inc.
 */
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