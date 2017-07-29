package org.nwapw.abacus.config;

/**
 * Serializable class that will be used to load TOML
 * configurations.
 */
public class Configuration {

    /**
     * The precision to which the calculator should operator.
     */
    public int decimalPrecision;
    /**
     * The type of number this calculator should use.
     */
    public String numberType;

}
