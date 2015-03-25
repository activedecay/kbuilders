package com.levelmoney.kbuilders.test

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.levelmoney.kbuilders.javaparser.extensions.getBuilders
import com.levelmoney.wire.kbuilders.generate
import org.junit.Test
import java.io.File
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

public class Tests {

    private fun getResourcePath(path: String): URL = javaClass.getClassLoader().getResource(path)!!
    private fun getResourceFile(path: String): File = File(getResourcePath(path).getFile())

    private fun getAddress(): CompilationUnit {
        return JavaParser.parse(getResourceFile("proto/Address.txt"))
    }

    Test fun testBuilderCount() {
        val test = generate("test", listOf(getResourceFile("proto/Address.txt")))

        val cu = getAddress()
        assertEquals(1, cu.getBuilders().size())
    }

}