package org.nwapw.abacus.fx

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import org.nwapw.abacus.config.Configuration
import java.io.File

/**
 * Additional settings for user interface.
 *
 * ExtendedConfiguration is used to add other settings
 * that aren't built into Abacus core, but are necessary
 * for the fx module.
 *
 * @property computationDelay the delay before which the computation stops.
 * @param implementation the number implementation, same as [Configuration.numberImplementation]
 * @param disabledPlugins the list of plugins that should be disabled, same as [Configuration.disabledPlugins]
 */
class ExtendedConfiguration(var computationDelay: Double = 0.0,
                            definitionFiles: Array<String> = emptyArray(),
                            implementation: String = "<default>",
                            disabledPlugins: Array<String> = emptyArray())
    : Configuration(implementation, disabledPlugins) {

    companion object {
        /**
         * The default TOML.
         */
        val DEFAULT_TOML_STRING = """
            computationDelay=0.0
            definitionFiles=[]
            implementation="naive"
            disabledPlugins=[]
            """
        /**
         * A reader with the default TOML data.
         */
        val DEFAULT_TOML_READER = Toml().read(DEFAULT_TOML_STRING)
        /**
         * A writer used to writing the configuration to disk.
         */
        val DEFAULT_TOML_WRITER = TomlWriter()
    }

    /**
     * The set of files that definitions should be loaded from.
     */
    val definitionFiles: MutableSet<String> = mutableSetOf(*definitionFiles)

    /**
     * Constructs a new configuration from a file on disk.
     * @param tomlFile the file from disk to load.
     */
    constructor(tomlFile: File) : this() {
        val toml = Toml(DEFAULT_TOML_READER)
        if(tomlFile.exists()) toml.read(tomlFile)
        copyFrom(toml.to(ExtendedConfiguration::class.java))
    }

    /**
     * Copies data from another configuration into this one.
     * @param config the configuration to copy from.
     */
    fun copyFrom(config: ExtendedConfiguration) {
        computationDelay = config.computationDelay
        numberImplementation = config.numberImplementation
        disabledPlugins.clear()
        disabledPlugins.addAll(config.disabledPlugins)
        definitionFiles.clear()
        definitionFiles.addAll(config.definitionFiles)
    }

    /**
     * Saves this configuration to a file.
     * @param file the file to save to.
     */
    fun saveTo(file: File) {
        DEFAULT_TOML_WRITER.write(this, file)
    }

}