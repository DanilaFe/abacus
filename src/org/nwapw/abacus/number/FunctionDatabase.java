package org.nwapw.abacus.number;

import java.util.HashMap;

public class FunctionDatabase {

    private HashMap<String, Function> functions;

    private void registerDefault(){
        functions.put("+", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length >= 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                NumberInterface sum = params[0];
                for(int i = 1; i < params.length; i++){
                    sum = sum.add(params[i]);
                }
                return sum;
            }
        });

        functions.put("-", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 2;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return params[0].subtract(params[1]);
            }
        });

        functions.put("*", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length >= 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                NumberInterface product = params[1];
                for(int i = 1; i < params.length; i++){
                    product = product.multiply(params[i]);
                }
                return product;
            }
        });

        functions.put("/", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 2;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                return params[0].divide(params[1]);
            }
        });

        functions.put("!", new Function() {
            @Override
            protected boolean matchesParams(NumberInterface[] params) {
                return params.length == 1;
            }

            @Override
            protected NumberInterface applyInternal(NumberInterface[] params) {
                NumberInterface factorial = params[0];
                NumberInterface multiplier = params[0];
                //It is necessary to later prevent calls of factorial on anything but non-negative integers.
                while((multiplier = multiplier.subtract(NaiveNumber.ONE.promoteTo(multiplier.getClass()))).signum() == 1){
                    factorial = factorial.multiply(multiplier);
                }
                return factorial;
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
