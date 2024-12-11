package es.unavarra.tlm.dscr_24_06.Juego;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// Clase para representar un barco
@JsonPropertyOrder({ "ship_type", "positions" }) // Asegura que 'ship_type' sea primero
public class Ships {
    private String ship_type; // Tipo de barco (e.g., "carrier")
    private ArrayList<Position> positions; // Lista de posiciones ocupadas por el barco

    public Ships() {}

    // Constructor
    public Ships(String ship_type) {
        this.ship_type = ship_type;
        this.positions = new ArrayList<>();
    }

    // Getters y Setters
    public String getShip_type() {
        return ship_type;
    }

    public void setShip_type(String ship_type) {
        this.ship_type = ship_type;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }

    // Método para añadir una posición al barco
    public void addPosition(String row, int column) {
        this.positions.add(new Position(row, column));
    }

    // Método para mostrar información del barco en formato JSON con "ship_type" primero
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"ship_type\": \"").append(ship_type).append("\",\n");
        sb.append("  \"positions\": [\n");
        for (int i = 0; i < positions.size(); i++) {
            Position pos = positions.get(i);
            sb.append("    { \"row\": \"").append(pos.getRow()) // 'row' antes de 'column'
                    .append("\", \"column\": ").append(pos.getColumn()).append(" }");
            if (i < positions.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    // Clase interna para representar una posición
    @JsonPropertyOrder({ "row", "column" }) // Asegura que 'row' sea primero en 'Position'
    public static class Position {
        private String row; // Letra de la fila (e.g., "A")
        private int column; // Número de la columna (e.g., 1)

        // Constructor
        public Position(String row, int column) {
            this.row = row;
            this.column = column;
        }

        // Getters y Setters
        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        // Método para representar la posición como texto
        @Override
        public String toString() {
            return "{ \"row\": \"" + row + "\", \"column\": " + column + " }"; // 'row' primero
        }
    }
}
