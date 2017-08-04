package org.nwapw.abacus.fx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Class that represents an entry in the plugin check box list.
 * The changes from this property are written to the config on application.
 */
public class ToggleablePlugin {

    /**
     * The property that determines whether the plugin will be enabled.
     */
    private final BooleanProperty enabled;
    /**
     * The name of the class this entry toggles.
     */
    private final String className;

    /**
     * Creates a new toggleable plugin with the given properties.
     * @param enabled the enabled / disabled state at the beginning.
     * @param className the name of the class this plugin toggles.
     */
    public ToggleablePlugin(boolean enabled, String className){
        this.enabled = new SimpleBooleanProperty();
        this.enabled.setValue(enabled);
        this.className = className;
    }

    /**
     * Gets the enabled property of this plugin.
     * @return the enabled property.
     */
    public BooleanProperty enabledProperty() {
        return enabled;
    }

    /**
     * Checks if this plugin entry should be enabled.
     * @return whether this plugin will be enabled.
     */
    public boolean isEnabled() {
        return enabled.get();
    }

    /**
     * Gets the class name this plugin toggles.
     * @return the class name that should be disabled.
     */
    public String getClassName() {
        return className;
    }

}
