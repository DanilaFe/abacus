package org.nwapw.abacus.plugin;

import org.nwapw.abacus.Abacus;
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
     * The map of functions registered by the plugins.
     */
    private Map<String, Function> registeredFunctions;
    /**
     * The map of operators registered by the plugins
     */
    private Map<String, Operator> registeredOperators;
    /**
     * The map of number implementations registered by the plugins.
     */
    private Map<String, NumberImplementation> registeredNumberImplementations;
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
     * The list of plugin listeners attached to this instance.
     */
    private Set<PluginListener> listeners;
    /**
     * The abacus instance used to access other
     * components of the application.
     */
    private Abacus abacus;

    /**
     * Creates a new plugin manager.
     *
     * @param abacus the abacus instance.
     */
    public PluginManager(Abacus abacus) {
        this.abacus = abacus;
        loadedPluginClasses = new HashSet<>();
        plugins = new HashSet<>();
        registeredFunctions = new HashMap<>();
        registeredOperators = new HashMap<>();
        registeredNumberImplementations = new HashMap<>();
        cachedInterfaceImplementations = new HashMap<>();
        cachedPi = new HashMap<>();
        listeners = new HashSet<>();
    }

    /**
     * Registers a function under the given name.
     * @param name the name of the function.
     * @param function the function to register.
     */
    public void registerFunction(String name, Function function){
        registeredFunctions.put(name, function);
    }

    /**
     * Registers an operator under the given name.
     * @param name the name of the operator.
     * @param operator the operator to register.
     */
    public void registerOperator(String name, Operator operator){
        registeredOperators.put(name, operator);
    }

    /**
     * Registers a number implementation under the given name.
     * @param name the name of the number implementation.
     * @param implementation the number implementation to register.
     */
    public void registerNumberImplementation(String name, NumberImplementation implementation){
        registeredNumberImplementations.put(name, implementation);
    }

    /**
     * Gets the function registered under the given name.
     * @param name the name of the function.
     * @return the function, or null if it was not found.
     */
    public Function functionFor(String name){
        return registeredFunctions.get(name);
    }

    /**
     * Gets the operator registered under the given name.
     * @param name the name of the operator.
     * @return the operator, or null if it was not found.
     */
    public Operator operatorFor(String name){
        return registeredOperators.get(name);
    }

    /**
     * Gets the number implementation registered under the given name.
     * @param name the name of the number implementation.
     * @return the number implementation, or null if it was not found.
     */
    public NumberImplementation numberImplementationFor(String name){
        return registeredNumberImplementations.get(name);
    }

    /**
     * Gets the number implementation for the given implementation class.
     *
     * @param name the class for which to find the implementation.
     * @return the implementation.
     */
    public NumberImplementation interfaceImplementationFor(Class<? extends NumberInterface> name) {
        if (cachedInterfaceImplementations.containsKey(name)) return cachedInterfaceImplementations.get(name);
        NumberImplementation toReturn = null;
        for(String key : registeredNumberImplementations.keySet()){
            NumberImplementation implementation = registeredNumberImplementations.get(key);
            if(implementation.getImplementation() == name) {
                toReturn = implementation;
                break;
            }
        }
        cachedInterfaceImplementations.put(name, toReturn);
        return toReturn;
    }

    /**
     * Gets the mathematical constant pi for the given implementation class.
     *
     * @param forClass the class for which to find pi.
     * @return pi
     */
    public NumberInterface piFor(Class<? extends NumberInterface> forClass) {
        if (cachedPi.containsKey(forClass)) return cachedPi.get(forClass);
        NumberImplementation implementation = interfaceImplementationFor(forClass);
        NumberInterface generatedPi = null;
        if (implementation != null) {
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
        Set<String> disabledPlugins = abacus.getConfiguration().getDisabledPlugins();
        for (Plugin plugin : plugins) {
            if (disabledPlugins.contains(plugin.getClass().getName())) continue;
            plugin.enable();
        }
        listeners.forEach(e -> e.onLoad(this));
    }

    /**
     * Unloads all the plugins in the PluginManager.
     */
    public void unload() {
        listeners.forEach(e -> e.onUnload(this));
        Set<String> disabledPlugins = abacus.getConfiguration().getDisabledPlugins();
        for (Plugin plugin : plugins) {
            if (disabledPlugins.contains(plugin.getClass().getName())) continue;
            plugin.disable();
        }
        registeredFunctions.clear();
        registeredOperators.clear();
        registeredNumberImplementations.clear();
        cachedInterfaceImplementations.clear();
        cachedPi.clear();
        listeners.forEach(e -> e.onUnload(this));
    }

    /**
     * Reloads all the plugins in the PluginManager.
     */
    public void reload() {
        unload();
        load();
    }

    /**
     * Gets all the functions loaded by the Plugin Manager.
     *
     * @return the set of all functions that were loaded.
     */
    public Set<String> getAllFunctions() {
        return registeredFunctions.keySet();
    }

    /**
     * Gets all the operators loaded by the Plugin Manager.
     *
     * @return the set of all operators that were loaded.
     */
    public Set<String> getAllOperators() {
        return registeredOperators.keySet();
    }

    /**
     * Gets all the number implementations loaded by the Plugin Manager.
     *
     * @return the set of all implementations that were loaded.
     */
    public Set<String> getAllNumberImplementations() {
        return registeredNumberImplementations.keySet();
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

    /**
     * Gets a list of all the plugin class files that have been
     * added to the plugin manager.
     *
     * @return the list of all the added plugin classes.
     */
    public Set<Class<?>> getLoadedPluginClasses() {
        return loadedPluginClasses;
    }
}
