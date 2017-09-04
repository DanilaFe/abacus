package org.nwapw.abacus.plugin;

import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.function.*;
import org.nwapw.abacus.number.NumberInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private Map<String, NumberFunction> registeredFunctions;
    /**
     * The map of tree value functions regstered by the plugins.
     */
    private Map<String, TreeValueFunction> registeredTreeValueFunctions;
    /**
     * The map of operators registered by the plugins
     */
    private Map<String, NumberOperator> registeredOperators;
    /**
     * The map of tree value operators registered by the plugins.
     */
    private Map<String, TreeValueOperator> registeredTreeValueOperators;
    /**
     * The map of number implementations registered by the plugins.
     */
    private Map<String, NumberImplementation> registeredNumberImplementations;
    /**
     * The map of documentation for functions registered by the plugins.
     */
    private Set<Documentation> registeredDocumentation;
    /**
     * The list of number implementation names.
     */
    private Map<Class<? extends NumberInterface>, String> interfaceImplementationNames;
    /**
     * The list of number implementations.
     */
    private Map<Class<? extends NumberInterface>, NumberImplementation> interfaceImplementations;
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
        registeredTreeValueFunctions = new HashMap<>();
        registeredOperators = new HashMap<>();
        registeredTreeValueOperators = new HashMap<>();
        registeredNumberImplementations = new HashMap<>();
        registeredDocumentation = new HashSet<>();
        interfaceImplementations = new HashMap<>();
        interfaceImplementationNames = new HashMap<>();
        cachedPi = new HashMap<>();
        listeners = new HashSet<>();
    }

    /**
     * Registers a function under the given name.
     *
     * @param name     the name of the function.
     * @param function the function to register.
     */
    public void registerFunction(String name, NumberFunction function) {
        registeredFunctions.put(name, function);
    }

    /**
     * Registers a tree value function under the given name.
     *
     * @param name     the name of the function.
     * @param function the function to register.
     */
    public void registerTreeValueFunction(String name, TreeValueFunction function) {
        registeredTreeValueFunctions.put(name, function);
    }

    /**
     * Registers an operator under the given name.
     *
     * @param name     the name of the operator.
     * @param operator the operator to register.
     */
    public void registerOperator(String name, NumberOperator operator) {
        registeredOperators.put(name, operator);
    }

    /**
     * Registers a tree value operator under the given name.
     *
     * @param name     the name of the tree value operator.
     * @param operator the tree value operator to register.
     */
    public void registerTreeValueOperator(String name, TreeValueOperator operator) {
        registeredTreeValueOperators.put(name, operator);
    }

    /**
     * Registers a number implementation under the given name.
     *
     * @param name           the name of the number implementation.
     * @param implementation the number implementation to register.
     */
    public void registerNumberImplementation(String name, NumberImplementation implementation) {
        registeredNumberImplementations.put(name, implementation);
        interfaceImplementationNames.put(implementation.getImplementation(), name);
        interfaceImplementations.put(implementation.getImplementation(), implementation);
    }

    /**
     * Registers the given documentation with the plugin manager,
     * making it accessible to the plugin manager etc.
     *
     * @param documentation the documentation to register.
     */
    public void registerDocumentation(Documentation documentation) {
        registeredDocumentation.add(documentation);
    }

    /**
     * Gets the function registered under the given name.
     *
     * @param name the name of the function.
     * @return the function, or null if it was not found.
     */
    public NumberFunction functionFor(String name) {
        return registeredFunctions.get(name);
    }

    /**
     * Gets the tree value function registered under the given name.
     *
     * @param name the name of the function.
     * @return the function, or null if it was not found.
     */
    public TreeValueFunction treeValueFunctionFor(String name) {
        return registeredTreeValueFunctions.get(name);
    }

    /**
     * Gets the operator registered under the given name.
     *
     * @param name the name of the operator.
     * @return the operator, or null if it was not found.
     */
    public NumberOperator operatorFor(String name) {
        return registeredOperators.get(name);
    }

    /**
     * Gets the tree value operator registered under the given name.
     *
     * @param name the name of the tree value operator.
     * @return the operator, or null if it was not found.
     */
    public TreeValueOperator treeValueOperatorFor(String name) {
        return registeredTreeValueOperators.get(name);
    }

    /**
     * Gets the number implementation registered under the given name.
     *
     * @param name the name of the number implementation.
     * @return the number implementation, or null if it was not found.
     */
    public NumberImplementation numberImplementationFor(String name) {
        return registeredNumberImplementations.get(name);
    }

    /**
     * Gets the documentation for the given entity of the given type.
     *
     * @param name the name of the entity to search for.
     * @param type the type that this entity is, to filter out similarly named documentation.
     * @return the documentation object.
     */
    public Documentation documentationFor(String name, DocumentationType type) {
        Documentation toReturn = null;
        for (Documentation entry : registeredDocumentation) {
            if (entry.getCodeName().equals(name) && entry.getType() == type) {
                toReturn = entry;
                break;
            }
        }
        if (toReturn == null) {
            toReturn = new Documentation(name, "", "", "", type);
            registerDocumentation(toReturn);
        }
        return toReturn;
    }

    /**
     * Gets the number implementation for the given implementation class.
     *
     * @param name the class for which to find the implementation.
     * @return the implementation.
     */
    public NumberImplementation interfaceImplementationFor(Class<? extends NumberInterface> name) {
        return interfaceImplementations.get(name);
    }

    /**
     * Gets the number implementation name for the given implementation class.
     *
     * @param name the class for which to find the implementation name.
     * @return the implementation name.
     */
    public String interfaceImplementationNameFor(Class<? extends NumberInterface> name) {
        return interfaceImplementationNames.get(name);
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
     * Removes the plugin with the given class from the manager.
     *
     * @param toRemove the plugin to remove.
     */
    public void removeClass(Class<? extends Plugin> toRemove) {
        if (!loadedPluginClasses.contains(toRemove)) return;
        plugins.removeIf(plugin -> plugin.getClass() == toRemove);
        loadedPluginClasses.remove(toRemove);
    }

    /**
     * Removes all plugins from this plugin manager.
     */
    public void removeAll() {
        loadedPluginClasses.clear();
        plugins.clear();
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
        registeredTreeValueFunctions.clear();
        registeredOperators.clear();
        registeredTreeValueOperators.clear();
        registeredNumberImplementations.clear();
        registeredDocumentation.clear();
        interfaceImplementationNames.clear();
        interfaceImplementations.clear();
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
     * Gets all the tree vlaue functions loaded by the PluginManager.
     *
     * @return the set of all the tree value functions that were loaded.
     */
    public Set<String> getAllTreeValueFunctions() {
        return registeredTreeValueFunctions.keySet();
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
     * Gets all the tree value operators loaded by the PluginManager.
     *
     * @return the set of all tree value operators that were loaded.
     */
    public Set<String> getAllTreeValueOperators() {
        return registeredTreeValueOperators.keySet();
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
