package org.nwapw.abacus.number

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.plugin.PluginListener
import org.nwapw.abacus.plugin.PluginManager
import java.util.function.Function

/**
 * A class that handles promotions based on priority and the
 * transition paths each implementation provides.
 *
 * @property abacus the Abacus instance to use to access other components.
 */
class PromotionManager(val abacus: Abacus) : PluginListener {

    /**
     * The already computed paths
     */
    val computePaths = mutableMapOf<Pair<NumberImplementation, NumberImplementation>, PromotionPath?>()

    /**
     * Computes a path between a starting and an ending implementation.
     *
     * @param from the implementation to start from.
     * @param to the implementation to get to.
     * @return the resulting promotion path, or null if it is not found
     */
    fun computePathBetween(from: NumberImplementation, to: NumberImplementation): PromotionPath? {
        val fromName = abacus.pluginManager.interfaceImplementationNameFor(from.implementation)
        val toName = abacus.pluginManager.interfaceImplementationNameFor(to.implementation)

        if(fromName == toName) return listOf(Function { it })

        if(from.promotionPaths.containsKey(toName))
            return listOf(from.promotionPaths[toName] ?: return null)
        return null
    }

    /**
     * If a path between the given implementations has already been computed, uses
     * the already calculated path. Otherwise, calls [computePathBetween] to compute a new
     * path.
     *
     * @param from the implementation to start from.
     * @param to the implementation to get to.
     * @return the resulting promotion path, or null if it is not found
     */
    fun getPathBetween(from: NumberImplementation, to: NumberImplementation): PromotionPath? {
        return computePaths.computeIfAbsent(from to to, {
             computePathBetween(it.first, it.second)
        })
    }

    /**
     * Promote all the numbers in the list to the same number implementation, to ensure
     * they can be used with each other. Finds the highest priority implementation
     * in the list, and promotes all other numbers to it.
     *
     * @param numbers the numbers to promote.
     * @return the resulting promotion result.
     */
    fun promote(vararg numbers: NumberInterface): PromotionResult? {
        val pluginManager = abacus.pluginManager
        val implementations = numbers.map { pluginManager.interfaceImplementationFor(it.javaClass) }
        val highestPriority = implementations.sortedBy { it.priority }.last()
        return PromotionResult(items = numbers.map {
            if(it.javaClass == highestPriority.implementation) it
            else getPathBetween(pluginManager.interfaceImplementationFor(it.javaClass), highestPriority)
                    ?.promote(it) ?: return null
        }.toTypedArray(), promotedTo = highestPriority)
    }

    override fun onLoad(manager: PluginManager?) {

    }

    override fun onUnload(manager: PluginManager?) {
        computePaths.clear()
    }


}