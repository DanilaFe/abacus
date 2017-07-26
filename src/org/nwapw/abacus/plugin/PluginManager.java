package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that controls instances of plugins, allowing for them
 * to interact with each other and the calculator.
 */
public class PluginManager {

    /**
     * A list of loaded plugins.
     */
    private ArrayList<Plugin> plugins;
    /**
     * List of functions that have been cached,
     * that is, found in a plugin and returned.
     */
    private HashMap<String, Function> cachedFunctions;

    /**
     * Creates a new plugin manager.
     */
    public PluginManager(){
        plugins = new ArrayList<>();
        cachedFunctions = new HashMap<>();
    }

    /**
     * Gets a function under the given name.
     * @param name the name of the function
     * @return the function under the given name.
     */
    public Function functionFor(String name){
        if(cachedFunctions.containsKey(name)) {
            return cachedFunctions.get(name);
        }

        Function loadedFunction = null;
        for(Plugin plugin : plugins){
            if(plugin.hasFunction(name)){
                loadedFunction = plugin.getFunction(name);
                break;
            }
        }
        cachedFunctions.put(name, loadedFunction);
        return loadedFunction;
    }

    /**
     * Adds an instance of Plugin that already has been instantiated.
     * @param plugin the plugin to add.
     */
    public void addInstantiated(Plugin plugin){
        plugin.load();
        cachedFunctions.clear();
        plugins.add(plugin);
    }

    /**
     * Instantiates a class of plugin, and adds it to this
     * plugin manager.
     * @param newClass the new class to instantiate.
     */
    public void addClass(Class<?> newClass){
        if(!Plugin.class.isAssignableFrom(newClass)) return;
        try {
            addInstantiated((Plugin) newClass.getConstructor(PluginManager.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
