package es.unavarra.tlm.dscr_24_06;

import androidx.annotation.NonNull;

public class Gunfire {
    private final String result;
    private final String row;
    private final int column;

    public Gunfire(String result, String row, int column) {
        this.result = result;
        this.row = row;
        this.column = column;
    }

    // Getters y setters
    public String getResult() {
        return result;
    }

    public String getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @NonNull
    @Override
    public String toString() {
        return "Gunfire{" +
                "result='" + result + '\'' +
                ", row='" + row + '\'' +
                ", column=" + column +
                '}';
    }
}
