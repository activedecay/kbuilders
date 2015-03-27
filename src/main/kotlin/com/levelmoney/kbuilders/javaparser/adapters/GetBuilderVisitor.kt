package com.levelmoney.kbuilders.javaparser.adapters

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import com.levelmoney.kbuilders.javaparser.extensions.isBuilder

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

class GetBuilderVisitor: VoidVisitorAdapter<MutableList<ClassOrInterfaceDeclaration>>() {

    override fun visit(n: ClassOrInterfaceDeclaration,
                       arg: MutableList<ClassOrInterfaceDeclaration>)
    {
        n.getMembers().forEach {
            if (it is ClassOrInterfaceDeclaration) {
                if (it.isBuilder()) {
                    arg.add(it)
                } else {
                    visit(it, arg)
                }
            }
        }
    }

}