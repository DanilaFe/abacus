package org.nwapw.abacus.context

import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.tree.Reducer
import org.nwapw.abacus.tree.TreeNode

/**
 * A reduction context that is mutable.
 * @param parent the parent of this context.
 * @param numberImplementation the number implementation used in this context.
 * @param reducer the reducer used in this context
 */
class MutableEvaluationContext(parent: EvaluationContext? = null,
                               numberImplementation: NumberImplementation? = null,
                               reducer: Reducer<NumberInterface>? = null) :
        EvaluationContext(parent, numberImplementation, reducer) {

    override var numberImplementation: NumberImplementation? = super.numberImplementation
    override var reducer: Reducer<NumberInterface>? = super.reducer

    /**
     * Writes data stored in the [other] context over data stored in this one.
     * @param other the context from which to copy data.
     */
    fun apply(other: EvaluationContext) {
        if(other.numberImplementation != null) numberImplementation = other.numberImplementation
        if(other.reducer != null) reducer = other.reducer
        for(name in other.variables) {
            setVariable(name, other.getVariable(name) ?: continue)
        }
        for(name in other.definitions) {
            setDefinition(name, other.getDefinition(name) ?: continue)
        }
    }

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