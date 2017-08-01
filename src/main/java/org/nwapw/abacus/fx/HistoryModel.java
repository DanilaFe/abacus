package org.nwapw.abacus.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The data model used for storing history entries.
 */
public class HistoryModel {

    /**
     * The property used for displaying the column
     * for the user input.
     */
    private final StringProperty input;
    /**
     * The property used for displaying the column
     * that contains the parsed input.
     */
    private final StringProperty parsed;
    /**
     * The property used for displaying the column
     * that contains the program output.
     */
    private final StringProperty output;

    /**
     * Creates a new history model with the given variables.
     * @param input the user input
     * @param parsed the parsed input
     * @param output the program output.
     */
    public HistoryModel(String input, String parsed, String output){
        this.input = new SimpleStringProperty();
        this.parsed = new SimpleStringProperty();
        this.output = new SimpleStringProperty();
        this.input.setValue(input);
        this.parsed.setValue(parsed);
        this.output.setValue(output);
    }

    /**
     * Gets the input property.
     * @return the input property.
     */
    public StringProperty inputProperty() {
        return input;
    }
    /**
     * Gets the input.
     * @return the input.
     */
    public String getInput() {
        return input.get();
    }

    /**
     * Gets the parsed input property.
     * @return the parsed input property.
     */
    public StringProperty parsedProperty() {
        return parsed;
    }
    /**
     * Gets the parsed input.
     * @return the parsed input.
     */
    public String getParsed() {
        return parsed.get();
    }

    /**
     * Gets the output property.
     * @return the output property.
     */
    public StringProperty outputProperty() {
        return output;
    }
    /**
     * Gets the program output.
     * @return the output.
     */
    public String getOutput() {
        return output.get();
    }

}
