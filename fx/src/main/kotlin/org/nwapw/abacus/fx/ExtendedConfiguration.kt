package org.nwapw.abacus.fx

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import org.nwapw.abacus.config.Configuration
import java.io.File

class ExtendedConfiguration(var computationDelay: Double = 0.0,
                     implementation: String = "<default>",
                     disabledPlugins: Array<String> = emptyArray())
    : Configuration(implementation, disabledPlugins) {

    companion object {
        val DEFAULT_TOML_STRING = """
            computationDelay=0.0
            implementation="naive"
            disabledPlugins=[]
            """
        val DEFAULT_TOML_READER = Toml().read(DEFAULT_TOML_STRING)
        val DEFAULT_TOML_WRITER = TomlWriter()
    }

    constructor(tomlFile: File) : this() {
        copyFrom(Toml(DEFAULT_TOML_READER).read(tomlFile).to(ExtendedConfiguration::class.java))
    }

    fun copyFrom(config: ExtendedConfiguration) {
        computationDelay = config.computationDelay
        numberImplementation = config.numberImplementation
        disabledPlugins.clear()
        disabledPlugins.addAll(config.disabledPlugins)
    }

    fun saveTo(file: File) {
        DEFAULT_TOML_WRITER.write(this, file)
    }

}