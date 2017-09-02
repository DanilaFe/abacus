package org.nwapw.abacus.number

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.plugin.NumberImplementation
import java.util.function.Function

class PromotionManager(val abacus: Abacus) {

    val computePaths = mutableMapOf<Pair<NumberImplementation, NumberImplementation>, PromotionPath?>()

    fun computePathBetween(from: NumberImplementation, to: NumberImplementation): PromotionPath? {
        val fromName = abacus.pluginManager.interfaceImplementationNameFor(from.implementation)
        val toName = abacus.pluginManager.interfaceImplementationNameFor(to.implementation)

        if(fromName == toName) return listOf(Function { it })

        if(from.promotionPaths.containsKey(toName))
            return listOf(from.promotionPaths[toName] ?: return null)
        return null
    }

    fun getPathBetween(from: NumberImplementation, to: NumberImplementation): PromotionPath? {
        return computePaths.computeIfAbsent(from to to, {
             computePathBetween(it.first, it.second)
        })
    }

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

}