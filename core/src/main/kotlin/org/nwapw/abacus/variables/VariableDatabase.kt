package org.nwapw.abacus.variables

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.PluginListener
import org.nwapw.abacus.plugin.PluginManager
import javax.swing.tree.TreeNode

/**
 * A database for variables and definition.
 *
 * The variable database is used to keep track of
 * variables and definitions throughout the calculator.
 *
 * @property abacus the Abacus instance.
 */
class VariableDatabase(val abacus: Abacus): PluginListener {

    /**
     * The variables that are stored in the database.
     */
    private val variables = mutableMapOf<String, NumberInterface>()
    /**
     * The definitions that are stored in the database.
     */
    private val definitions = mutableMapOf<String, TreeNode>()

    /**
     * Stores a variable in the database.
     *
     * @param name the name of the variable.
     * @param value the new value of the variable.
     */
    fun storeVariable(name: String, value: NumberInterface) {
        variables[name] = value
    }

    /**
     * Stores a definition in the database
     *
     * @param name the name of the definition.
     * @param value the new value of the definition.
     */
    fun storeDefinition(name: String, value: TreeNode) {
        definitions[name] = value
    }

    /**
     * Gets the value of the variable, or 0 if
     * it is not defined.
     *
     * @param name the name of the variable.
     * @return the value of the variable.
     */
    fun getVariableValue(name: String): NumberInterface {
        return variables[name] ?:
                abacus.numberImplementation.instanceForString("0")
    }

    /**
     * Gets the definition.
     *
     * @param name the name of the definition.
     * @return the value of the definition, or null if one doesn't exist.
     */
    fun getDefinition(name: String): TreeNode? {
        return definitions[name]
    }

    override fun onLoad(manager: PluginManager?) {

    }

    override fun onUnload(manager: PluginManager?) {
        variables.clear()
        definitions.clear()
    }

}