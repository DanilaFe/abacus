package org.nwapw.abacus.fx

import javafx.beans.property.SimpleStringProperty

/**
 * A model representing an input / output in the calculator.
 *
 * The HistoryModel class stores a record of a single user-provided input,
 * its parsed form as it was interpreted by the calculator, and the output
 * that was provided by the calculator. These are represented as properties
 * to allow easy access by JavaFX cells.
 *
 * @param input the user input
 * @param parsed the parsed version of the input.
 * @param output the output string.
 */
class HistoryModel(input: String, parsed: String, output: String) {

    /**
     * The property that holds the input.
     */
    val inputProperty = SimpleStringProperty(input)
    /**
     * The property that holds the parsed input.
     */
    val parsedProperty = SimpleStringProperty(parsed)
    /**
     * The property that holds the output.
     */
    val outputProperty = SimpleStringProperty(output)

}