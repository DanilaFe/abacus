package org.nwapw.abacus.context

import org.nwapw.abacus.exception.ContextException
import kotlin.reflect.KProperty

/**
 * A delegate to search a hierarchy made up of [EvaluationContext].
 *
 * ChainSearchDelegate is a variable delegate written specifically for use in [EvaluationContext], because
 * of its hierarchical structure. Variables not found in the current context are searched
 * for in its parent, which continues recursively until the context being examined has no parent.
 * This class assists that logic, which is commonly re-used with different variable types, by calling
 * [valueGetter] on the current context, then its parent, etc.
 *
 * @param V the type of the property to search recursively.
 * @property valueGetter the getter lambda to access the value from the context.
 */
class ChainSearchDelegate<out V>(private val valueGetter: EvaluationContext.() -> V?) {

    operator fun getValue(selfRef: Any, property: KProperty<*>): V {
        var currentRef = selfRef as EvaluationContext
        var returnedValue = currentRef.valueGetter()
        while (returnedValue == null) {
            currentRef = currentRef.parent ?: break
            returnedValue = currentRef.valueGetter()
        }
        return returnedValue ?: throw ContextException()
    }

}