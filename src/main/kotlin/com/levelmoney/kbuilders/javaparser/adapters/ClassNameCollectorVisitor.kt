package com.levelmoney.kbuilders.javaparser.adapters

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.EnumDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

/**
 * Created by Aaron Sarazan on 3/26/15
 * Copyright(c) 2015 Level, Inc.
 */
public class ClassNameCollectorVisitor(val pakage: String) : VoidVisitorAdapter<MutableList<String>>() {

    override fun visit(n: ClassOrInterfaceDeclaration, arg: MutableList<String>) {
        visitHelper(n, arg)
    }

    override fun visit(n: EnumDeclaration, arg: MutableList<String>) {
        visitHelper(n, arg)
    }

    private fun visitHelper(n: TypeDeclaration, arg: MutableList<String>) {
        val cat = pakage + "." + n.getName()
        arg.add(cat)
        n.getMembers().forEach {
            when {
                it is ClassOrInterfaceDeclaration -> {
                    ClassNameCollectorVisitor(cat).visit(it, arg)
                }
                it is EnumDeclaration -> {
                    ClassNameCollectorVisitor(cat).visit(it, arg)
                }
            }
        }
    }

}