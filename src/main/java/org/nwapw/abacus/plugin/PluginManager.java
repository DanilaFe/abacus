package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.number.NumberInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A class that controls instances of plugins, allowing for them
 * to interact with each other and the calculator.
 */
public class PluginManager {

    /**
     * List of classes loaded by this manager.
     */
    private Set<Class<?>> loadedPluginClasses;
    /**
     * A list of loaded plugins.
     */
    private Set<Plugin> plugins;
    /**
     * List of functions that have been cached,
     * that is, found in a plugin and returned.
     */
    private Map<String, Function> cachedFunctions;
    /**
     * List of operators that have been cached,
     * that is, found in a plugin and returned.
     */
    private Map<String, Operator> cachedOperators;
    /**
     * The list of number implementations that have
     * been cached, that is, found in a plugin and returned.
     */
    private Map<String, NumberImplementation> cachedNumberImplementations;
    /**
     * The list of number implementations that have been
     * found by their implementation class.
     */
    private Map<Class<? extends NumberInterface>, NumberImplementation> cachedInterfaceImplementations;
    /**
     * The pi values for each implementation class that have already been computer.
     */
    private Map<Class<? extends NumberInterface>, NumberInterface> cachedPi;
    /**
     * List of all functions loaded by the plugins.
     */
    private Set<String> allFunctions;
    /**
     * List of all operators loaded by the plugins.
     */
    private Set<String> allOperators;
    /**
     * List of all the number implementations loaded by the plugins.
     */
    private Set<String> allNumberImplementations;
    /**
     * The list of plugin listeners attached to this instance.
     */
    private Set<PluginListener> listeners;

    /**
     * Creates a new plugin manager.
     */
    public PluginManager() {
        loadedPluginClasses = new HashSet<>();
        plugins = new HashSet<>();
        cachedFunctions = new HashMap<>();
        cachedOperators = new HashMap<>();
        cachedNumberImplementations = new HashMap<>();
        cachedInterfaceImplementations = new HashMap<>();
        cachedPi = new HashMap<>();
        allFunctions = new HashSet<>();
        allOperators = new HashSet<>();
        allNumberImplementations = new HashSet<>();
        listeners = new HashSet<>();
    }

    /**
     * Searches the plugin list for a certain value, retrieving the Plugin's
     * list of items of the type using the setFunction and getting the value
     * of it is available via getFunction. If the value is contained
     * in the cache, it returns the cached value instead.
     *
     * @param plugins     the plugin list to search.
     * @param cache       the cache to use
     * @param setFunction the function to retrieve a set of available T's from the plugin
     * @param getFunction the function to get the T value under the given name
     * @param name        the name to search for
     * @param <T>         the type of element being search
     * @param <K>         the type of key that the cache is indexed by.
     * @return the retrieved element, or null if it was not found.
     */
    private static <T, K> T searchCached(Collection<Plugin> plugins, Map<K, T> cache,
                                      java.util.function.Function<Plugin, Set<K>> setFunction,
                                      java.util.function.BiFunction<Plugin, K, T> getFunction,
                                      K name) {
        if (cache.containsKey(name)) return cache.get(name);

        T loadedValue = null;
        for (Plugin plugin : plugins) {
            if (setFunction.apply(plugin).contains(name)) {
                loadedValue = getFunction.apply(plugin, name);
                break;
            }
        }

        cache.put(name, loadedValue);
        return loadedValue;
    }

    /**
     * Gets a function under the given name.
     *
     * @param name the name of the function
     * @return the function under the given name.
     */
    public Function functionFor(String name) {
        return searchCached(plugins, cachedFunctions, Plugin::providedFunctions, Plugin::getFunction, name);
    }

    /**
     * Gets an operator under the given name.
     *
     * @param name the name of the operator.
     * @return the operator under the given name.
     */
    public Operator operatorFor(String name) {
        return searchCached(plugins, cachedOperators, Plugin::providedOperators, Plugin::getOperator, name);
    }

    /**
     * Gets the number implementation under the given name.
     * @param name the name of the implementation.
     * @return the implementation.
     */
    public NumberImplementation numberImplementationFor(String name){
        return searchCached(plugins, cachedNumberImplementations, Plugin::providedNumberImplementations,
                Plugin::getNumberImplementation, name);
    }

    /**
     * Gets the number implementation for the given implementation class.
     * @param name the class for which to find the implementation.
     * @return the implementation.
     */
    public NumberImplementation interfaceImplementationFor(Class<? extends NumberInterface> name){
        if(cachedInterfaceImplementations.containsKey(name)) return cachedInterfaceImplementations.get(name);
        NumberImplementation toReturn = null;
        outside:
        for(Plugin plugin : plugins){
            for(String implementationName : plugin.providedNumberImplementations()){
                NumberImplementation implementation = plugin.getNumberImplementation(implementationName);
                if(implementation.getImplementation().equals(name)) {
                    toReturn = implementation;
                    break outside;
                }
            }
        }
        cachedInterfaceImplementations.put(name, toReturn);
        return toReturn;
    }

    /**
     * Gets the mathematical constant pi for the given implementation class.
     * @param forClass the class for which to find pi.
     * @return pi
     */
    public NumberInterface piFor(Class<? extends NumberInterface> forClass){
        if(cachedPi.containsKey(forClass)) return cachedPi.get(forClass);
        NumberImplementation implementation = interfaceImplementationFor(forClass);
        NumberInterface generatedPi = null;
        if(implementation != null){
            generatedPi = implementation.instanceForPi();
        }
        cachedPi.put(forClass, generatedPi);
        return generatedPi;
    }

    /**
     * Adds an instance of Plugin that already has been instantiated.
     *
     * @param plugin the plugin to add.
     */
    public void addInstantiated(Plugin plugin) {
        if (loadedPluginClasses.contains(plugin.getClass())) return;
        plugins.add(plugin);
        loadedPluginClasses.add(plugin.getClass());
    }

    /**
     * Instantiates a class of plugin, and adds it to this
     * plugin manager.
     *
     * @param newClass the new class to instantiate.
     */
    public void addClass(Class<?> newClass) {
        if (!Plugin.class.isAssignableFrom(newClass) || newClass == Plugin.class) return;
        try {
            addInstantiated((Plugin) newClass.getConstructor(PluginManager.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all the plugins in the PluginManager.
     */
    public void load() {
        for (Plugin plugin : plugins) plugin.enable();
        for (Plugin plugin : plugins) {
            allFunctions.addAll(plugin.providedFunctions());
            allOperators.addAll(plugin.providedOperators());
            allNumberImplementations.addAll(plugin.providedNumberImplementations());
        }
        listeners.forEach(e -> e.onLoad(this));
    }

    /**
     * Unloads all the plugins in the PluginManager.
     */
    public void unload() {
        for (Plugin plugin : plugins) plugin.disable();
        allFunctions.clear();
        allOperators.clear();
        allNumberImplementations.clear();
        listeners.forEach(e -> e.onUnload(this));
    }

    /**
     * Reloads all the plugins in the PluginManager.
     */
    public void reload() {
        unload();
        reload();
    }

    /**
     * Gets all the functions loaded by the Plugin Manager.
     *
     * @return the set of all functions that were loaded.
     */
    public Set<String> getAllFunctions() {
        return allFunctions;
    }

    /**
     * Gets all the operators loaded by the Plugin Manager.
     *
     * @return the set of all operators that were loaded.
     */
    public Set<String> getAllOperators() {
        return allOperators;
    }

    /**
     * Gets all the number implementations loaded by the Plugin Manager.
     *
     * @return the set of all implementations that were loaded.
     */
    public Set<String> getAllNumberImplementations(){
        return allNumberImplementations;
    }

    /**
     * Adds a plugin change listener to this plugin manager.
     *
     * @param listener the listener to add.
     */
    public void addListener(PluginListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove the plugin change listener from this plugin manager.
     *
     * @param listener the listener to remove.
     */
    public void removeListener(PluginListener listener) {
        listeners.remove(listener);
    }

}
