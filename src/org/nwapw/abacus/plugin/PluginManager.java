package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.function.Operator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
     * List of operators tha have been cached,
     * that is, found in a plugin and returned.
     */
    private HashMap<String, Operator> cachedOperators;

    /**
     * Creates a new plugin manager.
     */
    public PluginManager(){
        plugins = new ArrayList<>();
        cachedFunctions = new HashMap<>();
    }

    private static <T> T searchCached(Collection<Plugin> plugins, Map<String, T> cache,
                                      java.util.function.Function<Plugin, Set<String>> setFunction,
                                      java.util.function.BiFunction<Plugin, String, T> getFunction,
                                      String name){
        if(cache.containsKey(name)) return cache.get(name);

        T loadedValue = null;
        for(Plugin plugin : plugins){
            if(setFunction.apply(plugin).contains(name)){
                loadedValue = getFunction.apply(plugin, name);
                break;
            }
        }

        cache.put(name, loadedValue);
        return loadedValue;
    }
    /**
     * Gets a function under the given name.
     * @param name the name of the function
     * @return the function under the given name.
     */
    public Function functionFor(String name){
        return searchCached(plugins, cachedFunctions, Plugin::providedFunctions, Plugin::getFunction, name);
    }

    /**
     * Gets an operator under the given name.
     * @param name the name of the operator.
     * @return the operator under the given name.
     */
    public Operator operatorFor(String name){
        return searchCached(plugins, cachedOperators, Plugin::providedOperators, Plugin::getOperator, name);
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
