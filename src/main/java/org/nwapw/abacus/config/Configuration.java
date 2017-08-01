package org.nwapw.abacus.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;

/**
 * The configuration object that stores
 * options that the user can change.
 */
public class Configuration {

    /**
     * The TOML writer used to write this configuration to a file.
     */
    private static final TomlWriter TOML_WRITER = new TomlWriter();
    /**
     * The TOML reader used to load this config from a file.
     */
    private static final Toml TOML_READER = new Toml();

    /**
     * The implementation of the number that should be used.
     */
    private String numberImplementation = "naive";

    /**
     * Creates a new configuration with the given values.
     * @param numberImplementation the number implementation, like "naive" or "precise"
     */
    public Configuration(String numberImplementation){
        this.numberImplementation = numberImplementation;
    }

    /**
     * Loads a configuration from a given file, keeping non-specified fields default.
     * @param fromFile the file to load from.
     */
    public Configuration(File fromFile){
        if(!fromFile.exists()) return;
        copyFrom(TOML_READER.read(fromFile).to(Configuration.class));
    }

    /**
     * Copies the values from the given configuration into this one.
     * @param otherConfiguration the configuration to copy from.
     */
    public void copyFrom(Configuration otherConfiguration){
        this.numberImplementation = otherConfiguration.numberImplementation;
    }

    /**
     * Saves this configuration to the given file, creating
     * any directories that do not exist.
     * @param file the file to save to.
     */
    public void saveTo(File file){
        if(file.getParentFile() != null) file.getParentFile().mkdirs();
        try {
            TOML_WRITER.write(this, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the number implementation from this configuration.
     * @return the number implementation.
     */
    public String getNumberImplementation() {
        return numberImplementation;
    }
}
