package com.levelmoney.kbuilders.test

import com.levelmoney.wire.kbuilders.generate
import org.junit.Test
import java.io.File
import java.net.URL

/**
 * Created by Aaron Sarazan on 3/24/15
 * Copyright(c) 2015 Level, Inc.
 */

public class Tests {

    private fun getResource(path: String): URL? = javaClass.getClassLoader().getResource(path)

    Test fun testSomething() {
        val address = getResource("proto/Address.txt")!!
        val parsed = generate("com.levelmoney", listOf(File(address.getFile())))
        // nothing
    }

}