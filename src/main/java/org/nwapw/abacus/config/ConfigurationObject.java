package org.nwapw.abacus.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;

/**
 * A configuration object, which essentially
 * manages saving, loading, and getting values
 * from the configuration. While Configuration is
 * the data model, this is the interface with it.
 */
public class ConfigurationObject {

    /**
     * The writer used to store the configuration.
     */
    private static final TomlWriter TOML_WRITER = new TomlWriter();
    /**
     * The configuration instance being modeled.
     */
    private Configuration configuration;

    /**
     * Creates a new configuration object with the given config.
     *
     * @param config the config to use.
     */
    public ConfigurationObject(Configuration config) {
        setup(config);
    }

    /**
     * Create a configuration object by attempting to
     * load a config from the given path, using the
     * default configuration otherwise.
     *
     * @param path the path to attempt to load.
     */
    public ConfigurationObject(File path) {
        Configuration config;
        if (!path.exists()) {
            config = getDefaultConfig();
        } else {
            Toml parse = new Toml();
            parse.read(path);
            config = parse.to(Configuration.class);
        }
        setup(config);
    }

    /**
     * Creates a new configuration object with the
     * default configuration.
     */
    public ConfigurationObject() {
        setup(getDefaultConfig());
    }

    /**
     * Sets up the ConfigurationObject.
     * different constructors do different things,
     * but they all lead here.
     *
     * @param configuration the configuration to set up with.
     */
    private void setup(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates a default configuration.
     *
     * @return the newly created default configuration.
     */
    private Configuration getDefaultConfig() {
        configuration = new Configuration();
        configuration.numberType = "naive";
        return configuration;
    }

    /**
     * Returns the implementation the user has requested to
     * represent their numbers.
     *
     * @return the implementation name.
     */
    public String getNumberImplementation() {
        return configuration.numberType;
    }

    /**
     * Saves the ConfigurationObject to the given file.
     *
     * @param toFile the file to save ot.
     * @return true if the save succeed, false if otherwise.
     */
    public boolean save(File toFile) {
        if (toFile.getParentFile() != null) toFile.getParentFile().mkdirs();
        try {
            TOML_WRITER.write(configuration, toFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
