package levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.body.ConstructorDeclaration

/**
 * Created by Aaron Sarazan on 4/30/15
 * Copyright(c) 2015 Level, Inc.
 */

public fun ConstructorDeclaration.hasParameters(count: Int? = null): Boolean {
    val params = getParameters()
    if (params == null) return count == 0
    return count == null || params.size() == count
}
