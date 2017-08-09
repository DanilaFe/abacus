package org.nwapw.abacus.plugin;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.function.OperatorType;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.number.Variable;

import java.lang.reflect.Method;
import java.util.HashMap;

public class VariablePlugin extends Plugin {
    private HashMap<String,NumberInterface> variableMap;
    public final Operator OP_EQUALS = new Operator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, -1, new Function() {
        //private HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>> storedList = new HashMap<Class<? extends NumberInterface>, ArrayList<NumberInterface>>();
        @Override
        protected boolean matchesParams(NumberInterface[] params) {
            return params.length == 2;
        }

        @Override
        protected NumberInterface applyInternal(NumberInterface[] params) {
            //System.out.println((char)Double.parseDouble(params[1].toString()));
            //System.out.println(params[0].toString());
            if (params[0] instanceof Variable){
                variableMap.put(((Variable) params[0]).getVariable(), params[1]);
            }
            return params[1];
        }
    });
    public NumberInterface getValue(String variable){
        return variableMap.get(variable);
    }
    public VariablePlugin(PluginManager manager) {
        super(manager);
        //variables = new ArrayList<>();
        variableMap=new HashMap<>();
    }


    @Override
    public void onEnable(){
        //variables = new ArrayList<>();
        variableMap=new HashMap<>();
        registerOperator("=",OP_EQUALS);
    }
    @Override
    public void onDisable(){

    }
};
