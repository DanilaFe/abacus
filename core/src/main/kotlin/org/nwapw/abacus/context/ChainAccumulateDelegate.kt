package org.nwapw.abacus.context

import kotlin.reflect.KProperty

/**
 * A delegate to accumulate a collection of elements in a [EvaluationContext] hierarchy.
 *
 * ChainAccumulateDelegate is similar to the [ChainSearchDelegate], however, it operates only on collections.
 * Instead of returning the most recent collection, it merges them into a [Set].
 *
 * @param T the type of element in the collection.
 * @property valueGetter the getter used to access the collection from the context.
 */
class ChainAccumulateDelegate<out T>(private val valueGetter: EvaluationContext.() -> Collection<T>) {

    operator fun getValue(selfRef: Any, property: KProperty<*>): Set<T> {
        val set = mutableSetOf<T>()
        var currentRef: EvaluationContext = selfRef as? EvaluationContext ?: return set
        while(true) {
            set.addAll(currentRef.valueGetter())
            currentRef = currentRef.parent ?: break
        }
        return set
    }

}