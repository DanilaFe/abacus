package org.nwapw.abacus.number;

import java.util.HashMap;

public class FunctionDatabase {

    private HashMap<String, Function> functions;

    private void registerDefault(){

        functions.put("+", new Function() {
            @Override
            protected boolean matchesParams(Number[] params) {
                return params.length >= 1;
            }

            @Override
            protected Number applyInternal(Number[] params) {
                Number sum = params[0];
                for(int i = 1; i < params.length; i++){
                    sum = sum.add(params[i]);
                }
                return sum;
            }
        });

        functions.put("-", new Function() {
            @Override
            protected boolean matchesParams(Number[] params) {
                return params.length == 2;
            }

            @Override
            protected Number applyInternal(Number[] params) {
                return params[0].subtract(params[1]);
            }
        });

        functions.put("*", new Function() {
            @Override
            protected boolean matchesParams(Number[] params) {
                return params.length >= 1;
            }

            @Override
            protected Number applyInternal(Number[] params) {
                Number product = params[1];
                for(int i = 1; i < params.length; i++){
                    product = product.multiply(params[i]);
                }
                return product;
            }
        });

        functions.put("/", new Function() {
            @Override
            protected boolean matchesParams(Number[] params) {
                return params.length == 2;
            }

            @Override
            protected Number applyInternal(Number[] params) {
                return params[0].divide(params[1]);
            }
        });
    }

    public FunctionDatabase(){
        functions = new HashMap<>();
        registerDefault();
    }

    public Function getFunction(String name){
        return functions.get(name);
    }

}
