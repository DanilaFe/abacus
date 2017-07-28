package org.nwapw.abacus.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * A plugin loader, used to scan a directory for
 * plugins and load them into classes that can then be
 * used by the plugin manager.
 */
public class PluginLoader {

    /**
     * An internal class that represents a Jar file that
     * has been founded, but not loaded.
     */
    private static final class PluginEntry {
        String mainClass;
        File jarPath;
    }

    /**
     * The path which to search for plugins.
     */
    private File path;
    /**
     * The array of loaded plugin main classes.
     */
    private ArrayList<Class<?>> foundMainClasses;

    /**
     * Creates a new plugin loader at the given path.
     * @param path the path which to search for plugins.
     */
    public PluginLoader(File path) {
        this.path = path;
    }

    /**
     * Loads all the plugin classes that have been found.
     * @return the list of loaded classes.
     * @throws IOException thrown when loading classes from URL fails.
     * @throws ClassNotFoundException thrown when the class specified in plugin.properties is missing.
     */
    private ArrayList<Class<?>> loadPluginClasses() throws IOException, ClassNotFoundException {
        ArrayList<Class<?>> foundMainClasses = new ArrayList<>();
        for(PluginEntry entry : findPlugins()){
            if(entry.mainClass == null) continue;
            ClassLoader loader = URLClassLoader.newInstance(
                    new URL[]{ entry.jarPath.toURI().toURL() },
                    getClass().getClassLoader());
            Class<?> loadedClass = loader.loadClass(entry.mainClass);
            if(!Plugin.class.isAssignableFrom(loadedClass)) continue;
            foundMainClasses.add(loadedClass);
        }
        return foundMainClasses;
    }

    /**
     * Find all jars that have a plugin.properties file in the plugin folder.
     * @return the list of all plugin entries, with their main class names and the jars files.
     * @throws IOException thrown if reading the jar file fails
     */
    private ArrayList<PluginEntry> findPlugins() throws IOException {
        ArrayList<PluginEntry> pluginEntries = new ArrayList<>();
        File[] childFiles = path.listFiles();

        if(childFiles == null) return pluginEntries;
        for(File file : childFiles){
            if(!file.isFile() || !file.getName().endsWith(".jar")) continue;
            JarFile jarFile = new JarFile(file);
            if(jarFile.getEntry("plugin.properties") == null) continue;
            Properties properties = new Properties();
            properties.load(jarFile.getInputStream(jarFile.getEntry("plugin.properties")));

            PluginEntry entry = new PluginEntry();
            entry.mainClass = properties.getProperty("mainClass");
            entry.jarPath = file;
            pluginEntries.add(entry);
        }
        return pluginEntries;
    }

    /**
     * Loads all valid plugins and keeps track of them.
     * @throws IOException thrown if loading from jar files fails.
     * @throws ClassNotFoundException thrown if class specified in plugin.properties doesn't exist.
     */
    public void loadValidPlugins() throws IOException, ClassNotFoundException {
        foundMainClasses = loadPluginClasses();
    }

    /**
     * Gets the list of all the plugins that have last been loaded.
     * @return the list of loaded class files.
     */
    public ArrayList<Class<?>> getFoundMainClasses() {
        return foundMainClasses;
    }

}
