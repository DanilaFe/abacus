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

    public static ArrayList<Class<?>> loadJars(String filePath) throws IOException, ClassNotFoundException {
        return loadJars(new File(filePath));
    }

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

    public static ArrayList<Class<?>> loadJar(String path) throws IOException, ClassNotFoundException {
        return loadJar(new File(path));
    }

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
