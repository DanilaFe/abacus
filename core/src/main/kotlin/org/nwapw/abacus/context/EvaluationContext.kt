package org.nwapw.abacus.context

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.tree.Reducer
import org.nwapw.abacus.tree.nodes.TreeNode

/**
 * A context for the reduction of a [org.nwapw.abacus.tree.TreeNode] into a number.
 *
 * The reduction context is used to carry important state information captured at the beginning
 * of the reduction of an expression, such as the variables and the implementation in use.
 *
 * @property parent the parent of this context.
 * @property numberImplementation the implementation for numbers of this context.
 * @property abacus the abacus instance used by this reducer.
 */
abstract class EvaluationContext(val parent: EvaluationContext? = null,
                                 open val numberImplementation: NumberImplementation? = null,
                                 open val abacus: Abacus? = null): Reducer<NumberInterface> {

    /**
     * The map of variables in this context.
     */
    protected val variableMap = mutableMapOf<String, NumberInterface>()
    /**
     * The map of definitions in this context.
     */
    protected val definitionMap = mutableMapOf<String, TreeNode>()

    /**
     * The set of all variable names defined in this context.
     */
    val variables: Set<String>
        get() = variableMap.keys

    /**
     * The set of all definition names defined in this context.
     */
    val definitions: Set<String>
        get() = definitionMap.keys

    /**
     * The implementation inherited from this context's parent.
     */
    val inheritedNumberImplementation: NumberImplementation
            by ChainSearchDelegate { numberImplementation }

    /**
     * The Abacus instance inherited from this context's parent.
     */
    val inheritedAbacus: Abacus
            by ChainSearchDelegate { abacus }

    /**
     * The set of all variables in this context and its parents.
     */
    val inheritedVariables: Set<String> by ChainAccumulateDelegate { variables }

    /**
     * The set of all definition in this context and its parents.
     */
    val inheritedDefinitions: Set<String> by ChainAccumulateDelegate { definitions }

    /**
     * Create a new child instance of this context that is mutable.
     * @return the new child instance.
     */
    fun mutableSubInstance(): MutableEvaluationContext = MutableEvaluationContext(this)

    /**
     * Gets a variable stored in this context.
     */
    fun getVariable(name: String): NumberInterface? {
        return variableMap[name] ?: parent?.getVariable(name)
    }
    /**
     * Gets the definition stored in this context.
     */
    fun getDefinition(name: String): TreeNode? {
        return definitionMap[name] ?: parent?.getDefinition(name)
    }

}
