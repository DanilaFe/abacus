package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;

import java.util.HashMap;

/**
 * A plugin class that can be externally implemented and loaded via the
 * plugin manager. Plugins provide functionality to the calculator
 * with the "hasFunction" and "getFunction" functions,
 * and can use "registerFunction" and "functionFor" for
 * loading internally.
 */
public abstract class Plugin {

    /**
     * A hash map of functions mapped to their string names.
     */
    private HashMap<String, Function> functions;
    /**
     * The plugin manager in which to search for functions
     * not inside this package,
     */
    private PluginManager manager;

    private Plugin(){ }

    /**
     * Creates a new plugin with the given PluginManager.
     * @param manager the manager controlling this plugin.
     */
    public Plugin(PluginManager manager) {
        this.manager = manager;
        functions = new HashMap<>();
    }

    /**
     * Determines whether the current plugin provides the given function name.
     * @param functionName the name of the function provided.
     * @return true of the function exists, false if it doesn't.
     */
    public final boolean hasFunction(String functionName) {
        return functions.containsKey(functionName);
    }

    /**
     * Gets a function under the given function name.
     * @param functionName the name of the function to get
     * @return the function, or null if this plugin doesn't provide it.
     */
    public final Function getFunction(String functionName) {
        return functions.get(functionName);
    }

    /**
     * To be used in load(). Registers a function abstract class with the
     * plugin internally, which makes it accessible to the plugin manager.
     * @param name the name to register by.
     * @param toRegister the function implementation.
     * @return true if the function was registered successfully, false if not.
     */
    protected final boolean registerFunction(String name, Function toRegister) {
        if(functionFor(name) == null){
            functions.put(name, toRegister);
            return true;
        }
        return false;
    }

    /**
     * Searches the PluginManager for the given function name.
     * This can be used by the plugins internally in order to call functions
     * they do not provide.
     * @param name then name for which to search
     * @return the resulting function, or null if none was found for that name.
     */
    protected final Function functionFor(String name) {
        return manager.functionFor(name);
    }

    /**
     * Abstract method to be overridden by plugin implementation, in which the plugins
     * are supposed to register the functions they provide and do any other
     * necessary setup.
     */
    public abstract void load();

}
