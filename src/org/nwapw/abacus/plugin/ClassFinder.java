package org.nwapw.abacus.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {
    ArrayList<Class> classes;
    URL[] urls;
    public ClassFinder(){
        classes = new ArrayList();
    }
    public ArrayList<String> addJar(String path) throws IOException, ClassNotFoundException {
        //urls = new URL[]{new URL("jar:file:" + path + "!/")};
        return addJar(new File(path));
    }
    public ArrayList<String> addJar(File jarLocation) throws IOException, ClassNotFoundException {
        String path = jarLocation.getPath();
        urls = new URL[]{new URL("jar:file:" + path + "!/")};
        URLClassLoader classLoader = URLClassLoader.newInstance(urls);
        JarFile jarFolder = new JarFile(jarLocation);
        Enumeration jarList = jarFolder.entries();
        ArrayList<String> classNames = new ArrayList();
        while(jarList.hasMoreElements()){
            JarEntry tempJar = (JarEntry)jarList.nextElement();
            if(tempJar.getName().endsWith(".class")){
                //System.out.println(tempJar.getName());
                classNames.add(tempJar.getName());
                classes.add(classLoader.loadClass(tempJar.getName().replace('/','.').substring(0,tempJar.getName().length()-6)));
            }
        }
        return classNames;
    }
    public ArrayList<Class> getClasses(){
        return classes;
    }
    public Class getClass(int number){
        return classes.get(number);
    }
    public void delClasses(){
        classes=new ArrayList();
    }
    public int classCount(){
        return classes.size();
    }
}
