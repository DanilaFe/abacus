package org.nwapw.abacus.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class PluginManager {

    private ArrayList<Plugin> plugins;
    private HashMap<String, Plugin> pluginsForFunctions;

    public PluginManager(){
        plugins = new ArrayList<>();
        pluginsForFunctions = new HashMap<>();
    }

    public Plugin pluginForFunction(String name){
        if(pluginsForFunctions.containsKey(name)) {
            return pluginsForFunctions.get(name);
        }

        Plugin foundPlugin = null;
        for(Plugin plugin : plugins){
            if(plugin.hasFunction(name)) {
                foundPlugin = plugin;
                break;
            }
        }
        pluginsForFunctions.put(name, foundPlugin);

        return foundPlugin;
    }

    public void addInstantiated(Plugin plugin){
        plugin.load();
        pluginsForFunctions.clear();
        plugins.add(plugin);
    }

    public void addClass(Class<?> newClass){
        if(!Plugin.class.isAssignableFrom(newClass)) return;
        try {
            addInstantiated((Plugin) newClass.getConstructor(PluginManager.class).newInstance(this));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
