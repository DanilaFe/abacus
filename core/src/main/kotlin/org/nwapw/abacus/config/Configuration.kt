package org.nwapw.abacus.config

/**
 * A class that holds information that tells Abacus how to behave.
 *
 * Configuration stores information about how Abacus should behave, for
 * instance, what number implementation it should use and what
 * plugins should be ignored during loading.
 *
 * @property numberImplementation the number implementation Abacus should use for loading.
 * @param disabledPlugins the plugins that should be disabled and not loaded by the plugin manager.
 */
open class Configuration(var numberImplementation: String = "<default>", disabledPlugins: Array<String> = emptyArray()) {

    /**
     * The set of disabled plugins that should be ignored by the plugin manager.
     */
    val disabledPlugins = disabledPlugins.toMutableSet()

}