package org.nwapw.abacus.window;

import org.nwapw.abacus.tree.TreeNode;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class HistoryTableModel extends AbstractTableModel {

    public static final String[] COLUMN_NAMES = {
            "Input",
            "Parsed Input",
            "Output"
    };

    public static final Class[] CLASS_TYPES = {
            String.class,
            TreeNode.class,
            String.class
    };

    public static class HistoryEntry {
        public String input;
        public TreeNode parsedInput;
        public String output;

        public HistoryEntry(String input, TreeNode parsedInput, String output){
            this.input = input;
            this.parsedInput = parsedInput;
            this.output = output;
        }

        Object nthValue(int n){
            if(n == 0) return input;
            if(n == 1) return parsedInput;
            if(n == 2) return output;
            return null;
        }
    }

    ArrayList<HistoryEntry> entries;

    public HistoryTableModel() {
        entries = new ArrayList<>();
    }

    public void addEntry(HistoryEntry entry){
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

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        return;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
