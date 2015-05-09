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

package levelmoney.kbuilders.javaparser.extensions

import com.github.javaparser.ast.body.ConstructorDeclaration

public fun ConstructorDeclaration.hasParameters(count: Int? = null): Boolean {
    val params = getParameters()
    if (params == null) return count == 0
    return count == null || params.size() == count
}
