package org.nwapw.abacus.number;

public class BinaryNumber implements NumberInterface{

    /**
     * The number zero.
     */
    public static final BinaryNumber ZERO = new BinaryNumber(0);
    /**
     * The number one.
     */
    public static final BinaryNumber ONE = new BinaryNumber(1);
    /**
     * The value of this number.
     */
    private double value;

    /**
     * Creates a new BinaryNumber with the given string.
     *
     * @param value the value, which will be parsed as a double.
     */
    public BinaryNumber(String value) {
        toStandard(value);
    }
    /**
     * 
     */
    private void toStandard(String value) {
        String before;
        String after = "";
        if(value.indexOf(".")==-1){
            before=value;
        }else{
            before = value.substring(0,value.indexOf("."));
            after = value.substring(value.indexOf(".")+1);
        }
        double sum = 0;
        for(int it=0;before.length()>0;it++){
            if(before.charAt(before.length()-1)=='1'){
                sum+=Math.pow(2,it);
            }
            if(before.length()>1) {
                before = before.substring(0, before.length()-1);
            }else{
                before = "";
            }
        }
        for(int it=-1;after.length()>0;it--) {
            if (after.charAt(0) == '1') {
                sum += Math.pow(2, it);
            }
            if (after.length() > 1) {
                after = after.substring(1);
            } else {
                after = "";
            }
        }

        this.value = sum;
    }
    
    /**
     * Creates a new BinaryNumber with the given value.
     *
     * @param value the value to use.
     */
    public BinaryNumber(double value) {
        toStandard(""+value);
    }

    @Override
    public int getMaxPrecision() {
        return 18;
    }

    @Override
    public NumberInterface multiply(NumberInterface multiplier) {
        return new BinaryNumber(value * ((BinaryNumber) multiplier).value);
    }

    @Override
    public NumberInterface divide(NumberInterface divisor) {
        return new BinaryNumber(value / ((BinaryNumber) divisor).value);
    }

    @Override
    public NumberInterface add(NumberInterface summand) {
        return new BinaryNumber(value + ((BinaryNumber) summand).value);
    }

    @Override
    public NumberInterface subtract(NumberInterface subtrahend) {
        return new BinaryNumber(value - ((BinaryNumber) subtrahend).value);
    }

    @Override
    public NumberInterface negate() {
        return new BinaryNumber(-value);
    }

    @Override
    public NumberInterface intPow(int exponent) {
        if (exponent == 0) {
            return BinaryNumber.ONE;
        }
        boolean takeReciprocal = exponent < 0;
        exponent = Math.abs(exponent);
        NumberInterface power = this;
        for (int currentExponent = 1; currentExponent < exponent; currentExponent++) {
            power = power.multiply(this);
        }
        if (takeReciprocal) {
            power = BinaryNumber.ONE.divide(power);
        }
        return power;
    }

    @Override
    public int compareTo(NumberInterface number) {
        BinaryNumber num = (BinaryNumber) number;
        return Double.compare(value, num.value);
    }

    @Override
    public int signum() {
        return this.compareTo(ZERO);
    }

    @Override
    public NumberInterface promoteTo(Class<? extends NumberInterface> toClass) {
        if (toClass == this.getClass()) return this;
        else if (toClass == PreciseNumber.class) {
            return new PreciseNumber(Double.toString(value));
        }
        return null;
    }

    public String toString() {
        double sum = 0;
        double tempValue = Math.floor(value);
        double fraction = value-tempValue;
        for (int it=0;tempValue > 0;it++) {

            if (tempValue % 2 == 1) {
                sum+=Math.pow(10,it);
                tempValue-=1;
            }
            tempValue=tempValue/2;
        }
        for (int it=0;fraction > 0;it--) {

            if (fraction % 2 >= 1) {
                sum+=Math.pow(10,it);
                fraction-=1;
            }
            fraction=fraction*2;
        }
        double shiftBy = Math.pow(10, 10);
        return Double.toString(Math.round(sum * shiftBy) / shiftBy);
    }
}
