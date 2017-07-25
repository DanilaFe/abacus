package org.nwapw.abacus.number;

import java.util.HashMap;

public abstract class externalFunction {

    private HashMap<String, Function> functions;

    public externalFunction(){
        functions=new HashMap<>();
    }

    public boolean hasFunction(Function x){
        return functions.containsKey(x);
    }
    public Function getFunction(String x){
        return functions.get(x);
    }
    public boolean registerFunction(String x, Function y){
        if(!functions.containsKey(x))
            return functions.put(x,y)==null;
        return false;
    }
    public Function functionFor(String x){
        return null;
    }
    public abstract void load();
}
