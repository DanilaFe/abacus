package org.nwapw.abacus.window;

import org.nwapw.abacus.tree.TreeNode;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A table model to store data about the history of inputs
 * in the calculator.
 */
public class HistoryTableModel extends AbstractTableModel {

    /**
     * Static array used to get the column names.
     */
    public static final String[] COLUMN_NAMES = {
            "Input",
            "Parsed Input",
            "Output"
    };

    /**
     * Static array used to get the class of each column.
     */
    public static final Class[] CLASS_TYPES = {
            String.class,
            TreeNode.class,
            String.class
    };
    /**
     * The list of entries.
     */
    List<HistoryEntry> entries;

    /**
     * Creates a new empty history table model
     */
    public HistoryTableModel() {
        entries = new ArrayList<>();
    }

    /**
     * Adds an entry to the model.
     *
     * @param entry the entry to add.
     */
    public void addEntry(HistoryEntry entry) {
        entries.add(entry);
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CLASS_TYPES[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return entries.get(rowIndex).nthValue(columnIndex);
    }

    /**
     * Class used specifically to hold data about
     * the previous entries into the calculator.
     */
    public static class HistoryEntry {
        public String input;
        public TreeNode parsedInput;
        public String output;

        public HistoryEntry(String input, TreeNode parsedInput, String output) {
            this.input = input;
            this.parsedInput = parsedInput;
            this.output = output;
        }

        Object nthValue(int n) {
            if (n == 0) return input;
            if (n == 1) return parsedInput;
            if (n == 2) return output;
            return null;
        }
    }

}
