package org.nwapw.abacus.plugin;

import org.nwapw.abacus.number.Function;

import java.util.HashMap;

public abstract class Plugin {

    private HashMap<String, Function> functions;
    private PluginManager manager;

    private Plugin(){ }

    public Plugin(PluginManager manager) {
        this.manager = manager;
        functions = new HashMap<>();
    }

    public final boolean hasFunction(String functionName) {
        return functions.containsKey(functionName);
    }

    public final Function getFunction(String functionName) {
        return functions.get(functionName);
    }

    protected final boolean registerFunction(String name, Function toRegister) {
        if(functionFor(name) == null){
            functions.put(name, toRegister);
            return true;
        }
        return false;
    }

    protected final Function functionFor(String name) {
        Plugin ownerPlugin = manager.pluginForFunction(name);
        if(ownerPlugin == null) return null;
        return ownerPlugin.getFunction(name);
    }

    public abstract void load();

}
