package org.nwapw.abacus.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HistoryModel {

    private final StringProperty input;
    private final StringProperty parsed;
    private final StringProperty output;

    public HistoryModel(String input, String parsed, String output){
        this.input = new SimpleStringProperty();
        this.parsed = new SimpleStringProperty();
        this.output = new SimpleStringProperty();
        this.input.setValue(input);
        this.parsed.setValue(parsed);
        this.output.setValue(output);
    }

    public StringProperty inputProperty() {
        return input;
    }
    public String getInput() {
        return input.get();
    }

    public StringProperty parsedProperty() {
        return parsed;
    }
    public String getParsed() {
        return parsed.get();
    }

    public StringProperty outputProperty() {
        return output;
    }
    public String getOutput() {
        return output.get();
    }

}
