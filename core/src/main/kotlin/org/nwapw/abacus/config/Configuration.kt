package org.nwapw.abacus.config

open class Configuration(var numberImplementation: String = "<default>", disabledPlugins: Array<String> = emptyArray()) {

    val disabledPlugins = disabledPlugins.toMutableSet()

}