package org.nwapw.abacus.plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

public class ClassFinder extends ClassLoader{

    ArrayList<Class> classes;

    public ClassFinder(){
        super(ClassFinder.class.getClassLoader());
        classes=new ArrayList();
    }
    public Class loadClass(String className) throws ClassNotFoundException{
        return findClass(className);
    }
    public ArrayList<String> loadClass(File jarLocation) throws ClassNotFoundException, IOException{
        return addJar(jarLocation);
    }
    public ArrayList<String> addJar(File jarLocation) throws IOException {
        JarFile jarFolder = new JarFile(jarLocation);
        Enumeration jarList = jarFolder.entries();
        HashMap classSize = new HashMap();
        HashMap classContent = new HashMap();
        ArrayList<String> classNames = new ArrayList();
        JarEntry tempJar;
        ZipInputStream zipStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(jarLocation)));
        while(jarList.hasMoreElements()){
            tempJar = (JarEntry)jarList.nextElement();
            zipStream.getNextEntry();
            if(!tempJar.isDirectory()) {
                if (tempJar.getName().substring(tempJar.getName().indexOf('.')).equals(".class") && (tempJar.getName().length() < 9 || !tempJar.getName().substring(0, 9).equals("META-INF/"))) {
                    int size = (int)tempJar.getSize();
                    classSize.put(tempJar.getName(),new Integer((int)tempJar.getSize()));
                    byte[] bytes = new byte[size];
                    zipStream.read(bytes,0,size);
                    classContent.put(tempJar.getName(),bytes);
                    classNames.add(tempJar.getName());
                }
            }
        }
        jarFolder.close();
        for(String name:classNames) {
            classes.add(super.defineClass(name, (byte[]) classContent.get(name), 0, (int) classSize.get(name)));
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
}