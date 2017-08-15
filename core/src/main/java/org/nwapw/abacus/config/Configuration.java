package org.nwapw.abacus.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The configuration object that stores
 * options that the user can change.
 */
public class Configuration {

    /**
     * The defaults TOML string.
     */
    private static final String DEFAULT_CONFIG =
            "numberImplementation = \"naive\"\n" +
                    "disabledPlugins = []";
    /**
     * The defaults TOML object, parsed from the string.
     */
    private static final Toml DEFAULT_TOML = new Toml().read(DEFAULT_CONFIG);
    /**
     * The TOML writer used to write this configuration to a file.
     */
    private static final TomlWriter TOML_WRITER = new TomlWriter();

    /**
     * The computation delay for which the thread can run without interruption.
     */
    private double computationDelay = 0;
    /**
     * The implementation of the number that should be used.
     */
    private String numberImplementation = "<default>";
    /**
     * The list of disabled plugins in this Configuration.
     */
    private Set<String> disabledPlugins = new HashSet<>();

    /**
     * Creates a new configuration form the given configuration.
     *
     * @param copyFrom the configuration to copy.
     */
    public Configuration(Configuration copyFrom) {
        copyFrom(copyFrom);
    }

    /**
     * Creates a new configuration with the given values.
     *
     * @param computationDelay     the delay before the computation gets killed.
     * @param numberImplementation the number implementation, like "naive" or "precise"
     * @param disabledPlugins      the list of disabled plugins.
     */
    public Configuration(double computationDelay, String numberImplementation, String[] disabledPlugins) {
        this.computationDelay = computationDelay;
        this.numberImplementation = numberImplementation;
        this.disabledPlugins.addAll(Arrays.asList(disabledPlugins));
    }

    /**
     * Loads a configuration from a given file, keeping non-specified fields default.
     *
     * @param fromFile the file to load from.
     */
    public Configuration(File fromFile) {
        if (!fromFile.exists()) return;
        copyFrom(new Toml(DEFAULT_TOML).read(fromFile).to(Configuration.class));
    }

    /**
     * Copies the values from the given configuration into this one.
     *
     * @param otherConfiguration the configuration to copy from.
     */
    public void copyFrom(Configuration otherConfiguration) {
        this.computationDelay = otherConfiguration.computationDelay;
        this.numberImplementation = otherConfiguration.numberImplementation;
        this.disabledPlugins.addAll(otherConfiguration.disabledPlugins);
    }

    /**
     * Saves this configuration to the given file, creating
     * any directories that do not exist.
     *
     * @param file the file to save to.
     */
    public void saveTo(File file) {
        if (file.getParentFile() != null) file.getParentFile().mkdirs();
        try {
            TOML_WRITER.write(this, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the value of this configuration as a string.
     *
     * @return the string that represents this configuration.
     */
    public String asTomlString() {
        return TOML_WRITER.write(this);
    }

    /**
     * Gets the number implementation from this configuration.
     *
     * @return the number implementation.
     */
    public String getNumberImplementation() {
        return numberImplementation;
    }

    /**
     * Sets the number implementation for the configuration
     *
     * @param numberImplementation the number implementation.
     */
    public void setNumberImplementation(String numberImplementation) {
        this.numberImplementation = numberImplementation;
    }

    /**
     * Gets the list of disabled plugins.
     *
     * @return the list of disabled plugins.
     */
    public Set<String> getDisabledPlugins() {
        return disabledPlugins;
    }


    /**
     * Gets the computation delay specified in the configuration.
     *
     * @return the computaton delay.
     */
    public double getComputationDelay() {
        return computationDelay;
    }

    /**
     * Sets the computation delay.
     *
     * @param computationDelay the new computation delay.
     */
    public void setComputationDelay(double computationDelay) {
        this.computationDelay = computationDelay;
    }

}
