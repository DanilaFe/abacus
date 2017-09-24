package org.nwapw.abacus.number.promotion

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.exception.PromotionException
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.plugin.PluginListener
import org.nwapw.abacus.plugin.PluginManager

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

        if(fromName == toName) return listOf(object : PromotionFunction {
            override fun promote(number: NumberInterface): NumberInterface {
                return number
            }
        })

        if(from.promotionPaths.containsKey(toName))
            return listOf(from.promotionPaths[toName] ?: return null)
        return null
    }

    /**
     * Promote all the numbers in the list to the same number implementation, to ensure
     * they can be used with each other. Finds the highest priority implementation
     * in the list, and promotes all other numbers to it.
     *
     * @param numbers the numbers to promote.
     * @return the resulting promotion result.
     */
    fun promote(vararg numbers: NumberInterface): PromotionResult {
        val pluginManager = abacus.pluginManager
        val implementations = numbers.map { pluginManager.interfaceImplementationFor(it.javaClass) }
        val highestPriority = implementations.sortedBy { it.priority }.last()
        return PromotionResult(items = numbers.map {
            if (it.javaClass == highestPriority.implementation) it
            else computePaths[pluginManager.interfaceImplementationFor(it.javaClass) to highestPriority]
                    ?.promote(it) ?: throw PromotionException()
        }.toTypedArray(), promotedTo = highestPriority)
    }

    override fun onLoad(manager: PluginManager) {
        val implementations = manager.allNumberImplementations.map { manager.numberImplementationFor(it) }
        for(first in implementations) {
            for(second in implementations) {
                val promoteFrom = if(second.priority > first.priority) first else second
                val promoteTo = if(second.priority > first.priority) second else first
                val path = computePathBetween(promoteFrom, promoteTo)
                computePaths[promoteFrom to promoteTo] = path
            }
        }
    }

    override fun onUnload(manager: PluginManager) {
        computePaths.clear()
    }


}