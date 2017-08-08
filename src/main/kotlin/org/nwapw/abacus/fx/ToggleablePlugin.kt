package org.nwapw.abacus.fx

import javafx.beans.property.SimpleBooleanProperty

class ToggleablePlugin (val className: String, enabled: Boolean) {

    val enabledProperty = SimpleBooleanProperty(enabled)

    fun isEnabled(): Boolean {
        return enabledProperty.value
    }

}