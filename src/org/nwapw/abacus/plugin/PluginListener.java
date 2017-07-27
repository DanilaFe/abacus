package org.nwapw.abacus.plugin;

/**
 * A listener that responds to changes in the PluginManager.
 */
public interface PluginListener {

    /**
     * Called when the PluginManager loads plugins.
     * @param manager the manager that fired the event.
     */
    public void onLoad(PluginManager manager);

    /**
     * Called when the PluginManager unloads all its plugins.
     * @param manager the manager that fired the event.
     */
    public void onUnload(PluginManager manager);

}
