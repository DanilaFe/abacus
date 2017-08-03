package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.number.NumberInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private Map<String, Function> functions;
    /**
     * A hash map of operators mapped to their string names.
     */
    private Map<String, Operator> operators;
    /**
     * A hash map of operators mapped to their string names.
     */
    private Map<String, Class<? extends NumberInterface>> numbers;
    /**
     * A hash map of constant providers for each number type.
     */
    private Map<Class<?>, java.util.function.Function<String, NumberInterface>> constantProviders;
    /**
     * The plugin manager in which to search for functions
     * not inside this package,
     */
    private PluginManager manager;
    /**
     * Whether this plugin has been loaded.
     */
    private boolean enabled;

    private Plugin() {
    }

    /**
     * Creates a new plugin with the given PluginManager.
     *
     * @param manager the manager controlling this plugin.
     */
    public Plugin(PluginManager manager) {
        this.manager = manager;
        functions = new HashMap<>();
        operators = new HashMap<>();
        numbers = new HashMap<>();
        constantProviders = new HashMap<>();
        enabled = false;
    }

    /**
     * Gets the list of functions provided by this plugin.
     *
     * @return the list of registered functions.
     */
    public final Set<String> providedFunctions() {
        return functions.keySet();
    }

    /**
     * Gets the list of functions provided by this plugin.
     *
     * @return the list of registered functions.
     */
    public final Set<String> providedOperators() {
        return operators.keySet();
    }

    /**
     * Gets the list of all numbers provided by this plugin.
     *
     * @return the list of registered numbers.
     */
    public final Set<String> providedNumbers() {
        return numbers.keySet();
    }

    /**
     * Gets the list of all constant providers provided by this plugin.
     * @return the list of constant providers.
     */
    public final Set<Class<?>> providedConstantProviders() {
        return constantProviders.keySet();
    }

    /**
     * Gets a function under the given function name.
     *
     * @param functionName the name of the function to get
     * @return the function, or null if this plugin doesn't provide it.
     */
    public final Function getFunction(String functionName) {
        return functions.get(functionName);
    }

    /**
     * Gets an operator under the given operator name.
     *
     * @param operatorName the name of the operator to get.
     * @return the operator, or null if this plugin doesn't provide it.
     */
    public final Operator getOperator(String operatorName) {
        return operators.get(operatorName);
    }

    /**
     * Gets the class under the given name.
     *
     * @param numberName the name of the class.
     * @return the class, or null if the plugin doesn't provide it.
     */
    public final Class<? extends NumberInterface> getNumber(String numberName) {
        return numbers.get(numberName);
    }

    /**
     * Gets the constant provider for the given class.
     *
     * @param pluginClass the class for which to provide constants.
     * @return the provider, or null, if the plugin doesn't provide it.
     */
    public final java.util.function.Function<String, NumberInterface> getConstantProvider(Class<?> pluginClass){
        return constantProviders.get(pluginClass);
    }

    /**
     * Enables the function, loading the necessary instances
     * of functions.
     */
    public final void enable() {
        if (enabled) return;
        onEnable();
        enabled = true;
    }

    /**
     * Disables the plugin, clearing loaded data store by default
     * and calling its disable() method.
     */
    public final void disable() {
        if (!enabled) return;
        onDisable();
        functions.clear();
        operators.clear();
        numbers.clear();
        constantProviders.clear();
        enabled = false;
    }

    /**
     * To be used in load(). Registers a function abstract class with the
     * plugin internally, which makes it accessible to the plugin manager.
     *
     * @param name       the name to register by.
     * @param toRegister the function implementation.
     */
    protected final void registerFunction(String name, Function toRegister) {
        functions.put(name, toRegister);
    }

    /**
     * To be used in load(). Registers an operator abstract class
     * with the plugin internally, which makes it accessible to
     * the plugin manager.
     *
     * @param name     the name of the operator.
     * @param operator the operator to register.
     */
    protected final void registerOperator(String name, Operator operator) {
        operators.put(name, operator);
    }

    /**
     * To be used in load(). Registers a number class
     * with the plugin internally, which makes it possible
     * for the user to select it as an "implementation" for the
     * number that they would like to use.
     *
     * @param name       the name to register it under.
     * @param toRegister the class to register.
     */
    protected final void registerNumber(String name, Class<? extends NumberInterface> toRegister) {
        numbers.put(name, toRegister);
    }

    /**
     * To be used in load(). Registers a constant provider
     * with the plugin internally, which makes it possible
     * for the calculations to look up constants for each different
     * number type.
     * @param providerFor the class the provider works with.
     * @param constantProvider the provider to register.
     */
    protected final void registerConstantProvider(Class<?> providerFor,
                                                   java.util.function.Function<String, NumberInterface> constantProvider) {
        constantProviders.put(providerFor, constantProvider);
    }

    /**
     * Searches the PluginManager for the given function name.
     * This can be used by the plugins internally in order to call functions
     * they do not provide.
     *
     * @param name the name for which to search
     * @return the resulting function, or null if none was found for that name.
     */
    protected final Function functionFor(String name) {
        return manager.functionFor(name);
    }

    /**
     * Searches the PluginManager for the given operator name.
     * This can be used by the plugins internally in order to call
     * operations they do not provide.
     *
     * @param name the name for which to search
     * @return the resulting operator, or null if none was found for that name.
     */
    protected final Operator operatorFor(String name) {
        return manager.operatorFor(name);
    }

    /**
     * Searches the PluginManager for the given number implementation.
     * This can be used by the plugins internally in order to
     * find classes by name that they do not provide.
     *
     * @param name the name for which to search
     * @return the resulting number class.
     */
    protected final Class<? extends NumberInterface> numberFor(String name) {
        return manager.numberFor(name);
    }

    /**
     * Searches the PluginManager for the given constant provider.
     * This can be used by the plugins internally in order
     * to find constant providers for number provider they do not provide.
     *
     * @param forClass the class for which to get a generator for.
     * @return the resulting generator
     */
    protected final java.util.function.Function<String, NumberInterface> constantProviderFor(Class<?> forClass){
        return manager.constantProviderFor(forClass);
    }

    /**
     * Abstract method to be overridden by plugin implementation, in which the plugins
     * are supposed to register the functions they provide and do any other
     * necessary setup.
     */
    public abstract void onEnable();

    /**
     * Abstract method overridden by the plugin implementation, in which the plugins
     * are supposed to dispose of loaded functions, operators, and macros.
     */
    public abstract void onDisable();

}
