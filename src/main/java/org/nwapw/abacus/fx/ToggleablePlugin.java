package org.nwapw.abacus.fx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ToggleablePlugin {

    private final BooleanProperty enabled;
    private final String className;

    public ToggleablePlugin(boolean enabled, String className){
        this.enabled = new SimpleBooleanProperty();
        this.enabled.setValue(enabled);
        this.className = className;
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public String getClassName() {
        return className;
    }

}
