package es.unavarra.tlm.dscr_24_06.Juego;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.unavarra.tlm.dscr_24_06.Login.SessionManager;
import es.unavarra.tlm.dscr_24_06.MenuJuego.GameActivity;
import es.unavarra.tlm.dscr_24_06.R;


public class MainActivityBarcos extends AppCompatActivity {

    // Dimensiones del tablero
    private final int gridSize = 10;
    private final int[][] grid = new int[gridSize][gridSize]; // 0 = vacío, 1 = ocupado por barco

    // Cantidad de cuadros a cambiar (tamaño del barco) y orientación
    private int tamanioBarco = 1; // Por defecto, un barco de 1 cuadro
    private boolean esHorizontal = true; // Por defecto, horizontal
    private int quebarco= 0;
    private boolean tablerocompleto=false;
    // Contadores de barcos colocados
    private int barco5 = 0;
    private int barco4 = 0;
    private int barco3 = 0;
    private int barco2 = 0;
    private int barco1 = 0;

    private final ArrayList<Ships> barcos = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_barcos);

        // Referencia al GridLayout del XML
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Botones para seleccionar el tamaño del barco
        Button button5 = findViewById(R.id.carrier5celdas);
        Button button4 = findViewById(R.id.Battleship4celdas);
        Button button3s = findViewById(R.id.Submarine3celdas);
        Button button3c = findViewById(R.id.crusier3celdas);
        Button button2 = findViewById(R.id.Destroyer2celdas);
        Button buttonenviar = findViewById(R.id.enviar);


        Button buttonRotate = findViewById(R.id.buttonRotate);
        Button butonquitar = findViewById(R.id.quitarbarco);

        // Manejo de eventos para cambiar el tamaño del barco
        button2.setOnClickListener(v -> {
            tamanioBarco = 2;
            quebarco=1;
            Toast.makeText(MainActivityBarcos.this, "Seleccionado: Barco de 2 cuadros", Toast.LENGTH_SHORT).show();
        });

        button3c.setOnClickListener(v -> {
            tamanioBarco = 3;
            quebarco=2;
            Toast.makeText(MainActivityBarcos.this, "Seleccionado: Barco de 3 cuadros", Toast.LENGTH_SHORT).show();
        });

        button3s.setOnClickListener(v -> {
            tamanioBarco = 3;
            quebarco=3;
            Toast.makeText(MainActivityBarcos.this, "Seleccionado: Barco de 3 cuadros", Toast.LENGTH_SHORT).show();
        });
        button4.setOnClickListener(v -> {
            tamanioBarco = 4;
            quebarco=4;
            Toast.makeText(MainActivityBarcos.this, "Seleccionado: Barco de 4 cuadros", Toast.LENGTH_SHORT).show();
        });
        button5.setOnClickListener(v -> {
            tamanioBarco = 5;
            quebarco=5;
            Toast.makeText(MainActivityBarcos.this, "Seleccionado: Barco de 5 cuadros", Toast.LENGTH_SHORT).show();
        });
        butonquitar.setOnClickListener(v -> {
            quitarBarco(); // Llamar al método para quitar el barco
        });
        buttonenviar.setOnClickListener(v -> {
            enviar(); // Llamar al método para quitar el barco
        });

        // Botón para girar orientación
        buttonRotate.setOnClickListener(v -> {
            esHorizontal = !esHorizontal;
            String orientacion = esHorizontal ? "horizontal" : "vertical";
            Toast.makeText(MainActivityBarcos.this, "Orientación: " + orientacion, Toast.LENGTH_SHORT).show();
        });

        // Crear el tablero de 10x10
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button button = new Button(this);
                button.setLayoutParams(new GridLayout.LayoutParams());
                button.setText("");
                button.setBackgroundColor(Color.BLUE);

                // Fijar el tamaño de los botones
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 100;
                params.height = 100;
                button.setLayoutParams(params);

                // Guardar las coordenadas en el Tag del botón como un arreglo de dos elementos
                button.setTag(new int[]{row, col});



                button.setOnClickListener(v -> {
                    if(quebarco==0){
                        Toast.makeText(MainActivityBarcos.this, "por favor pulsa un barco", Toast.LENGTH_SHORT).show();
                    }else {
                        // Recuperar las coordenadas del tag del botón
                        int[] tag = (int[]) v.getTag();
                        int row1 = tag[0];
                        int col1 = tag[1];
                        colocarBarco(row1, col1); // Llamar al método para colocar el barco
                    }
                });


                // Añadir el botón al GridLayout
                gridLayout.addView(button);
            }
        }
    }

    // Método para verificar y colocar el barco en el tablero
    private void colocarBarco(int row, int col) {
        // Verificar si se puede colocar un barco del tamaño actual
        if (esHorizontal && col + tamanioBarco <= gridSize || !esHorizontal && row + tamanioBarco <= gridSize) {
            boolean puedeColocar = true;
            int inicioFila = Math.max(0, row - 1);
            int finFila = Math.min(gridSize - 1, row + (esHorizontal ? 1 : tamanioBarco));
            int inicioColumna = Math.max(0, col - 1);
            int finColumna = Math.min(gridSize - 1, col + (esHorizontal ? tamanioBarco : 1));

            for (int i = inicioFila; i <= finFila; i++) {
                for (int j = inicioColumna; j <= finColumna; j++) {
                    if (grid[i][j] == 1) {
                        Toast.makeText(MainActivityBarcos.this, "Estan pegados", Toast.LENGTH_SHORT).show();
                        puedeColocar=false; // Si alguna celda vecina está ocupada, retorna falso
                        break;
                    }
                }
            }

            // Verificar si alguna celda ya está ocupada
            for (int i = 0; i < tamanioBarco; i++) {
                if (esHorizontal) {
                    if (grid[row][col + i] == 1) {
                        puedeColocar = false;
                        Toast.makeText(MainActivityBarcos.this, "Error: El barco no cabe en esta posición", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    if (grid[row + i][col] == 1) {
                        puedeColocar = false;
                        Toast.makeText(MainActivityBarcos.this, "Error: El barco no cabe en esta posición", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
            Button button5 = findViewById(R.id.carrier5celdas);
            Button button4 = findViewById(R.id.Battleship4celdas);
            Button button3s = findViewById(R.id.Submarine3celdas);
            Button button3c = findViewById(R.id.crusier3celdas);
            Button button2 = findViewById(R.id.Destroyer2celdas);


            // tope maximo de barcos colocables
            if(quebarco==5){
                if(barco5>0){
                    puedeColocar=false;
                }
            }
            else if(quebarco==4){
                if(barco4>0){
                    puedeColocar=false;
                }
            }
            else if(quebarco==3){
                if(barco3>0){
                    puedeColocar=false;
                }
            }
            else if(quebarco==2){
                if(barco2>1){
                    puedeColocar=false;
                }
            }
            else if(quebarco==1){
                if(barco1>1){
                    puedeColocar=false;
                }
            }
            Ships ship = new Ships();
            ArrayList<Ships.Position> posicionesDelBarcoreal = new ArrayList<>();
            // Si todas las celdas están libres, colocar el barco
            if (puedeColocar) {
                for (int i = 0; i < tamanioBarco; i++) {
                    if (esHorizontal) {
                        grid[row][col + i] = 1;
                        Button button = (Button) ((GridLayout) findViewById(R.id.gridLayout)).getChildAt(row * gridSize + (col + i));
                        button.setBackgroundColor(Color.BLACK);
                        // Convertir la fila a letra (A, B, C...)
                        char letraFila = (char) ('A' + row);
                        int columna = col + i + 1;

                        // Crear la posición y agregarla a la lista
                        posicionesDelBarcoreal.add(new Ships.Position(String.valueOf(letraFila),columna));
                    } else {
                        grid[row + i][col] = 1;
                        Button button = (Button) ((GridLayout) findViewById(R.id.gridLayout)).getChildAt((row + i) * gridSize + col);
                        button.setBackgroundColor(Color.BLACK);
                        char letraFila = (char) ('A' + (row + i));
                        int columna = col + 1;

                        // Crear la posición y agregarla a la lista
                        posicionesDelBarcoreal.add(new Ships.Position(String.valueOf(letraFila),columna));
                    }
                }
                Ships.Position posicion = posicionesDelBarcoreal.get(0);
                Toast.makeText(MainActivityBarcos.this, posicion.getColumn()+posicion.getRow(), Toast.LENGTH_SHORT).show();

                // Incrementar el contador según el tamaño del barco colocado
                if (quebarco == 5) {
                    barco5++;
                    ship.setShip_type("carrier");
                    ship.setPositions(posicionesDelBarcoreal);
                    barcos.add(ship);
                    Toast.makeText(MainActivityBarcos.this, barcos.size()+"", Toast.LENGTH_SHORT).show();

                    if (barco5>0){
                        button5.setVisibility(View.GONE);
                        quebarco=0;
                    }
                }
                else if (quebarco == 4) {
                    barco4++;
                    ship.setShip_type("battleship");
                    ship.setPositions(posicionesDelBarcoreal);
                    barcos.add(ship);

                    if (barco4>0){
                        button4.setVisibility(View.GONE);
                        quebarco=0;
                    }
                }
                else if (quebarco == 3) {
                    barco3++;
                    ship.setShip_type("cruiser");
                    ship.setPositions(posicionesDelBarcoreal);
                    barcos.add(ship);
                    if (barco3>0){
                        button3s.setVisibility(View.GONE);
                        quebarco=0;
                    }
                }
                else if (quebarco == 2) {
                    barco2++;
                    ship.setShip_type("submarine");
                    ship.setPositions(posicionesDelBarcoreal);
                    barcos.add(ship);
                    if (barco2>1){
                        button3c.setVisibility(View.GONE);
                        quebarco=0;
                    }
                }
                else if (quebarco == 1) {
                    barco1++;
                    ship.setShip_type("destroyer");
                    ship.setPositions(posicionesDelBarcoreal);
                    barcos.add(ship);
                    if (barco1>1){
                        button2.setVisibility(View.GONE);
                        quebarco=0;
                    }
                }
                if (barco5==1){
                    if (barco4==1){
                        if (barco3==1){
                            if (barco2==2){
                                if (barco1==2){
                                    tablerocompleto=true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Si el barco no cabe en la orientación seleccionada, mostrar error
            Toast.makeText(MainActivityBarcos.this, "Error: El barco no cabe en esta posición", Toast.LENGTH_SHORT).show();
        }
    }
    // Método para quitar un barco
    private void quitarBarco() {
        // Verificar si la celda seleccionada tiene parte de un barco
        barcos.clear();
        Toast.makeText(MainActivityBarcos.this, barcos.size()+"", Toast.LENGTH_SHORT).show();

        // Cambiar las celdas ocupadas por el barco a vacío (0)
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == 1) { // Si es parte de un barco
                    Button button = (Button) ((GridLayout) findViewById(R.id.gridLayout))
                            .getChildAt(i * gridSize + j); // Obtener el botón correspondiente
                    button.setBackgroundColor(Color.BLUE); // Restaurar color
                    grid[i][j] = 0; // Marcar como vacío en el array
                }
            }
        }

        // Actualizar los contadores y restaurar visibilidad de los botones de selección

        findViewById(R.id.carrier5celdas).setVisibility(View.VISIBLE);
        barco5=0;
        findViewById(R.id.Battleship4celdas).setVisibility(View.VISIBLE);
        barco4=0;
        findViewById(R.id.Submarine3celdas).setVisibility(View.VISIBLE);
        barco3=0;
        findViewById(R.id.crusier3celdas).setVisibility(View.VISIBLE);
        barco2=0;
        findViewById(R.id.Destroyer2celdas).setVisibility(View.VISIBLE);
        barco1=0;
        Toast.makeText(MainActivityBarcos.this, "Barcos quitados", Toast.LENGTH_SHORT).show();

    }
    private void enviar(){
        if (tablerocompleto){
            for (int i = 0; i < barcos.size(); i++) {
                final int index = i;  // Se necesita para acceder correctamente en la ejecución del Handler
                new Handler().postDelayed(() -> {
                    Ships barcoEnvio = barcos.get(index);
                    ObjectMapper objectMapper = new ObjectMapper();
                    // Convertir la lista de barcos a JSON
                    String json;
                    try {
                        json = objectMapper.writeValueAsString(barcoEnvio);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    // Mostrar el JSON resultante
                    Log.d("JSONOutput", json);
                    MandarBarco ss = new MandarBarco();
                    SessionManager sessionManager = new SessionManager(MainActivityBarcos.this);  // Asegúrate de usar el contexto adecuado
                    String sesion = sessionManager.getSession();
                    String token = sessionManager.extractTokenFromSession(sesion); // Extraer el token

                    String gameIdString = getIntent().getStringExtra("GAME_ID");

                    //Toast.makeText(MainActivityBarcos.this, gameIdString, Toast.LENGTH_SHORT).show();

                    // Ahora sí mandamos el barco con el método correcto
                    ss.Mandar(MainActivityBarcos.this, gameIdString, token, barcoEnvio);
                }, i * 500L);  // Retraso de 1 segundo entre cada solicitud
            }
            //nuevo
            new Handler().postDelayed(() -> {
                Intent in = new Intent(MainActivityBarcos.this,GameActivity.class);
                startActivity(in);
                Toast.makeText(this, "Barcos Enviados de forma exitosa", Toast.LENGTH_SHORT).show();

                finish();
            }, 4000); // Espera 500ms antes de actualizar

           



        }else{
            Toast.makeText(MainActivityBarcos.this, "Por Favor, commpleta el tablero", Toast.LENGTH_SHORT).show();
        }
    }


}