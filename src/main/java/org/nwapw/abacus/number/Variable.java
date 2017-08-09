package org.nwapw.abacus.number;

public class Variable extends NumberInterface{
    public NumberInterface value;
    public String variable;

    public Variable(NumberInterface value,String variable){
        this.value = value;
        this.variable = variable;
    }
    public String getVariable(){
        return variable;
    }

    @Override
    public NumberInterface number(){
        return value.number();
    }

    @Override
    public Class<? extends NumberInterface> getClassVal(){
        return value.getClassVal();
    }

    @Override
    public int getMaxPrecision() {
        return value.getMaxPrecision();
    }

    @Override
    protected NumberInterface multiplyInternal(NumberInterface multiplier) {
        value = value.promoteToInternal(multiplier.number().getClass());
        return value.multiplyInternal(multiplier.number());
    }

    @Override
    protected NumberInterface divideInternal(NumberInterface divisor) {
        value = value.promoteToInternal(divisor.number().getClass());
        return value.divideInternal(divisor.number());
    }

    @Override
    protected NumberInterface addInternal(NumberInterface summand) {
        value = value.promoteToInternal(summand.number().getClass());
        return value.addInternal(summand.number());
    }

    @Override
    protected NumberInterface subtractInternal(NumberInterface subtrahend) {
        value = value.promoteToInternal(subtrahend.number().getClass());
        return value.subtractInternal(subtrahend.number());
    }

    @Override
    protected NumberInterface negateInternal() {
        return value.negateInternal();
    }

    @Override
    protected NumberInterface intPowInternal(int exponent) {
        return value.intPowInternal(exponent);
    }

    @Override
    public int compareTo(NumberInterface number) {
        value = value.promoteToInternal(number.number().getClass());
        return value.compareTo(number.number());
    }

    @Override
    public int signum() {
        return value.signum();
    }

    @Override
    protected NumberInterface ceilingInternal() {
        return value.ceilingInternal();
    }

    @Override
    protected NumberInterface floorInternal() {
        return value.floorInternal();
    }

    @Override
    protected NumberInterface fractionalPartInternal() {
        return value.fractionalPartInternal();
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    protected NumberInterface promoteToInternal(Class<? extends NumberInterface> toClass) {
        return value.promoteToInternal(toClass);
    }
    public String toString(){
        return value.toString();
    }
}