package com.levelmoney.kbuilders.tests

import com.levelmoney.kbuilders.test.builders.*
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by Aaron Sarazan on 4/12/15
 * Copyright(c) 2015 Level, Inc.
 */

public class BasicTests {

    Test fun testBasic() {
        val basic = buildBasicObject {
            value = 5
        }
        assertEquals(basic.value, 5)
    }

    Test fun testRebuild() {
        val first = buildRebuildObject {
            value = 5
        }
        val second = first.rebuild {
            value = 6
        }
        assertEquals(first.value, 5)
        assertEquals(second.value, 6)
    }

    Test fun testParent() {
        val parent = buildParentObject {
            child = buildBasicObject { value = 5 }
        }
        assertEquals(parent.child.value, 5)
    }

    Test fun testNested() {
        val parent = buildNestedParentObject {
            child = buildChildObject { value = 5 }
        }
        assertEquals(parent.child.value, 5)
    }
}
