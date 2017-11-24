package org.nwapw.abacus.context

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.tree.nodes.TreeNode

/**
 * An evaluation context with limited mutability.
 *
 * An evaluation context that is mutable but in a limited way, that is, not allowing the modifications
 * of variables whose changes might cause issues outside of the function. An example of this would be
 * the modification of the [numberImplementation], which would cause code paths such as the parsing
 * of NumberNodes to produce a different type of number than if the function did not run, whcih is unacceptable.
 *
 * @param parent the parent of this context.
 * @param numberImplementation the number implementation used in this context.
 * @param abacus the abacus instance used.
 */
abstract class PluginEvaluationContext(parent: EvaluationContext? = null,
                                       numberImplementation: NumberImplementation? = null,
                                       abacus: Abacus? = null) :
        EvaluationContext(parent, numberImplementation, abacus) {

    /**
     * Sets a variable to a certain [value].
     * @param name the name of the variable.
     * @param value the value of the variable.
     */
    fun setVariable(name: String, value: NumberInterface) {
        variableMap[name] = value
    }

    /**
     * Set a definition to a certain [value].
     * @param name the name of the definition.
     * @param value the value of the definition.
     */
    fun setDefinition(name: String, value: TreeNode) {
        definitionMap[name] = value
    }

    /**
     * Clears the variables defined in this context.
     */
    fun clearVariables(){
        variableMap.clear()
    }

    /**
     * Clears the definitions defined in this context.
     */
    fun clearDefinitions(){
        definitionMap.clear()
    }


}