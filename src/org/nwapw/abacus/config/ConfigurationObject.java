package org.nwapw.abacus.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.nwapw.abacus.number.NaiveNumber;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * A configuration object, which essentially
 * manages saving, loading, and getting values
 * from the configuration. While Configuration is
 * the data model, this is the interface with it.
 */
public class ConfigurationObject {

    /**
     * The default implementation to use for instantiating numbers.
     */
    private static final Class<? extends NaiveNumber> DEFAULT_IMPLEMENTATION = NaiveNumber.class;
    /**
     * The writer used to store the configuration.
     */
    private static final TomlWriter TOML_WRITER = new TomlWriter();
    /**
     * The configuration instance being modeled.
     */
    private Configuration configuration;
    /**
     * A map of number names to their implementations, which
     * will be provided by plugins.
     */
    private Map<String, Class<? extends NaiveNumber>> numberImplementations;

    /**
     * Sets up the ConfigurationObject.
     * different constructors do different things,
     * but they all lead here.
     * @param configuration the configuration to set up with.
     */
    private void setup(Configuration configuration){
        this.configuration = configuration;
        numberImplementations = new HashMap<>();
    }

    /**
     * Creates a default configuration.
     * @return the newly created default configuration.
     */
    private Configuration getDefaultConfig(){
        configuration = new Configuration();
        configuration.decimalPrecision = -1;
        configuration.numberType = "naive";
        return configuration;
    }

    /**
     * Register a number implementation.
     * @param name the name of the number implementation to register the class as.
     * @param newClass the class that will be used to instantiate the new number.
     *                 It is required that this class provides a Number(String) constructor.
     */
    public void registerImplementation(String name, Class<? extends NaiveNumber> newClass){
        numberImplementations.put(name, newClass);
    }

    /**
     * Creates a new number with the configured type, passing
     * it the given string.
     * @param string the string from which the number should be parsed.
     * @return the resulting number, or null if an error occurred.
     */
    public NaiveNumber numberFromString(String string)  {
        Class<? extends NaiveNumber> toLoad =
                numberImplementations.getOrDefault(configuration.numberType, DEFAULT_IMPLEMENTATION);
        try {
            return toLoad.getConstructor(String.class).newInstance(string);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the configured, user-requested precision.
     * @return the precision.
     */
    public int getPrecision(){
        return configuration.decimalPrecision;
    }

    /**
     * Saves the ConfigurationObject to the given file.
     * @param toFile the file to save ot.
     * @return true if the save succeed, false if otherwise.
     */
    public boolean save(File toFile){
        if(toFile.getParentFile() != null) toFile.getParentFile().mkdirs();
        try {
            TOML_WRITER.write(configuration, toFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a new configuration object with the given config.
     * @param config the config to use.
     */
    public ConfigurationObject(Configuration config){
        setup(config);
    }

    /**
     * Create a configuration object by attempting to
     * load a config from the given path, using the
     * default configuration otherwise.
     * @param path the path to attempt to load.
     */
    public ConfigurationObject(File path){
        Configuration config;
        if(!path.exists()) {
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
    public ConfigurationObject(){
        setup(getDefaultConfig());
    }

}
