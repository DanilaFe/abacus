package org.nwapw.abacus.fx

import javafx.beans.property.SimpleBooleanProperty

/**
 * A model representing a plugin that can be disabled or enabled.
 *
 * ToggleablePlugin is a model that is used to present to the user the option
 * of disabling / enabling plugins. The class name in this plugin is stored if
 * its "enabledPropery" is false, essentially blacklisting the plugin.
 *
 * @param className the name of the class that this model concerns.
 * @param enabled whether or not the model should start enabled.
 */
class ToggleablePlugin (val className: String, enabled: Boolean) {

    /**
     * The property used to interact with JavaFX components.
     */
    val enabledProperty = SimpleBooleanProperty(enabled)

    /**
     * Checks whether this plugin is currently enabled or not.
     *
     * @return true if it is enabled, false otherwise.
     */
    fun isEnabled(): Boolean {
        return enabledProperty.value
    }

}