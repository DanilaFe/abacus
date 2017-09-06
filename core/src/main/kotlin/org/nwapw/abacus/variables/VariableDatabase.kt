package org.nwapw.abacus.variables

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.PluginListener
import org.nwapw.abacus.plugin.PluginManager
import org.nwapw.abacus.tree.TreeNode

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
    val variables = mutableMapOf<String, NumberInterface>()
    /**
     * The definitions that are stored in the database.
     */
    val definitions = mutableMapOf<String, TreeNode>()

    override fun onLoad(manager: PluginManager?) {

    }

    override fun onUnload(manager: PluginManager?) {
        variables.clear()
        definitions.clear()
    }

}