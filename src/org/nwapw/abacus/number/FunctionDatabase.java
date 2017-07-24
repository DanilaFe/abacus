package org.nwapw.abacus.number;

import java.util.HashMap;

public class FunctionDatabase {

    private HashMap<String, Function> functions;

    private void registerDefault(){

    }

    public FunctionDatabase(){
        functions = new HashMap<>();
    }

    public Function getFunction(String name){
        return functions.get(name);
    }

}
