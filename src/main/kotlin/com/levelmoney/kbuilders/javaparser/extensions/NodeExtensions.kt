package com.levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node

/**
 * Created by Aaron Sarazan on 3/26/15
 * Copyright(c) 2015 Level, Inc.
 */

public fun Node.getPackageString(): String {
    return when {
        this is CompilationUnit -> {
            getPackage().getName().getName()
        }
        else -> getParentNode().getPackageString()
    }
}