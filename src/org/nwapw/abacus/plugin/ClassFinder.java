package org.nwapw.abacus.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Class that loads plugin classes from their jars.
 */
public class ClassFinder {

    /**
     * Loads all the plugin classes from the given plugin folder.
     * @param filePath the path for the plugin folder.
     * @return the list of all loaded classes.
     * @throws IOException thrown if an error occurred scanning the plugin folder.
     * @throws ClassNotFoundException thrown if the class listed in the file doesn't get loaded.
     */
    public static ArrayList<Class<?>> loadJars(String filePath) throws IOException, ClassNotFoundException {
        return loadJars(new File(filePath));
    }

    /**
     * Loads all the plugin classes from the given plugin folder.
     * @param pluginFolderPath the folder in which to look for plugins.
     * @return the list of all loaded classes.
     * @throws IOException thrown if an error occurred scanning the plugin folder.
     * @throws ClassNotFoundException thrown if the class listed in the file doesn't get loaded.
     */
    public static ArrayList<Class<?>> loadJars(File pluginFolderPath) throws IOException, ClassNotFoundException {
        ArrayList<Class<?>> toReturn = new ArrayList<>();
        if(!pluginFolderPath.exists()) return toReturn;
        ArrayList<File> files = Files.walk(pluginFolderPath.toPath())
                .map(Path::toFile)
                .filter(f -> f.getName().endsWith(".jar"))
                .collect(Collectors.toCollection(ArrayList::new));
        for (File file : files){
            toReturn.addAll(loadJar(file));
        }
        return toReturn;
    }

    /**
     * Loads the classes from a single path, given by the file.
     * @param jarLocation the location of the jar to load.
     * @return the list of loaded classes loaded from the jar.
     * @throws IOException thrown if there was an error reading the file
     * @throws ClassNotFoundException thrown if the class could not be loaded.
     */
    public static ArrayList<Class<?>> loadJar(File jarLocation) throws IOException, ClassNotFoundException {
        ArrayList<Class<?>> loadedClasses = new ArrayList<>();
        String path = jarLocation.getPath();
        URL[] urls = new URL[]{new URL("jar:file:" + path + "!/")};

        URLClassLoader classLoader = URLClassLoader.newInstance(urls);
        JarFile jarFolder = new JarFile(jarLocation);
        Enumeration jarEntityList = jarFolder.entries();

        while (jarEntityList.hasMoreElements()) {
            JarEntry jarEntity = (JarEntry) jarEntityList.nextElement();
            if (jarEntity.getName().endsWith(".class")) {
                loadedClasses.add(classLoader.loadClass(jarEntity.getName().replace('/', '.').substring(0, jarEntity.getName().length() - 6)));
            }
        }
        return loadedClasses;
    }

}
