package es.unavarra.tlm.dscr_24_06;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import java.util.ArrayList;
import java.util.Objects;
import es.unavarra.tlm.dscr_24_06.Juego.Ships;
import es.unavarra.tlm.dscr_24_06.MenuJuego.Rechazar;
import es.unavarra.tlm.dscr_24_06.Perfil.RespuestaPerfilRival;

// En la clase Tablero
public class Tablero extends AppCompatActivity {
    private String puedes,gameid,token;
    private final int gridSize = 10;
    private final int[][] grid = new int[gridSize][gridSize];
    private ArrayList<Ships> barcos = new ArrayList<>();
    private boolean disparo2 = false;
    ArrayList<Gunfire> disparosRealizados =new ArrayList<>();
    ArrayList<Gunfire> disparosRecibidos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tablero);

        String gameIdString = getIntent().getStringExtra("EnemyID");
        token = getIntent().getStringExtra("token");
        gameid = getIntent().getStringExtra("gameid");
        puedes = getIntent().getStringExtra("disparo");


        Button Superfil = findViewById(R.id.btn_Rivalperfil);
        Button Disparo = findViewById(R.id.bnt_disparo);
        Button Rendirse =findViewById(R.id.btnRendirse);


        // Primer GridLayout
        GridLayout gridLayout = findViewById(R.id.tutablero);
        inicializarTablero1(gridLayout);

        // Segundo GridLayout
        GridLayout gridLayout2 = findViewById(R.id.tablerorival);
        inicializarTablero2(gridLayout2);

        // Muestra el perfil rival
        Superfil.setOnClickListener(view -> {
            RespuestaPerfilRival ResRiv = new RespuestaPerfilRival();
            ResRiv.RespuestaRival(Tablero.this, gameIdString, token);
        });

        //Boton para rendirse
        Rendirse.setOnClickListener(view -> {
            new AlertDialog.Builder(Tablero.this)
                    .setTitle("Seguro que quieres rendirte?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        //Aceptar la invitacion
                        Rechazar Re = new Rechazar();
                        Re.rechazarJuego(Tablero.this, gameid, token);
                        finish(); // Finaliza la actividad actual
                    })
                    .setNegativeButton("Rechazar", (dialog, which) -> {
                        // Rechazar el juego si el usuario confirma
                    })
                    .show();
        });

        //Boton que habilita el disapro
        Disparo.setOnClickListener(view -> {
            if (puedes.equals("Y")) {
                Toast.makeText(Tablero.this, "Has habilitado el disapro cuando toques el tablero rival ", Toast.LENGTH_SHORT).show();
                disparo2 = true;
            }
            else {
                Toast.makeText(Tablero.this, "Tienes que esperar al rival.", Toast.LENGTH_SHORT).show();
            }
        });

        //Para Mostrar Tus Barcos
        InfoMisBarcos infoBarcos = new InfoMisBarcos();
        infoBarcos.MisBarcos(this, gameid, token, new InfoMisBarcos.GameInfoCallback() {
            @Override
            public void onGameInfoReceived(ArrayList<Ships> ships, ArrayList<Gunfire> done, ArrayList<Gunfire> received) {
                // Recibir los barcos y los disparos
                barcos = ships;
                disparosRealizados = done;
                disparosRecibidos = received;

                // Mostrar los barcos en el tablero
                mostrarBarcosEnTablero();

                // Mostrar los disparos recibidos
                if (!disparosRecibidos.isEmpty()){
                    mostrarDisparosR();
                }else{
                    Toast.makeText(Tablero.this, "yfewudjk", Toast.LENGTH_SHORT).show();
                }


                //Mostart los disparos disparados
                if (!disparosRealizados.isEmpty()){
                    mostrarDisparosD();
                }

            }
        });
    }
    // Método para inicializar el tablero1
    private void inicializarTablero1(GridLayout gridLayout) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button button = new Button(this);
                button.setLayoutParams(new GridLayout.LayoutParams());
                button.setText("");
                button.setBackgroundColor(Color.BLUE);

                // Fijar el tamaño de los botones
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 75;
                params.height = 75;
                button.setLayoutParams(params);

                // Guardar las coordenadas en el Tag del botón
                button.setTag(new int[]{row, col});

                button.setOnClickListener(v -> {
                    // Recuperar las coordenadas del tag del botón

                    Toast.makeText(Tablero.this, "Este tablero no hace nada.", Toast.LENGTH_SHORT).show();

                });

                // Añadir el botón al GridLayout
                gridLayout.addView(button);
            }
        }
    }

    // Método para inicializar el tablero1
    private void inicializarTablero2(GridLayout gridLayout) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button button = new Button(this);
                button.setLayoutParams(new GridLayout.LayoutParams());
                button.setText("");
                button.setBackgroundColor(Color.BLUE);

                // Fijar el tamaño de los botones
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 75;
                params.height = 75;
                button.setLayoutParams(params);

                // Guardar las coordenadas en el Tag del botón
                button.setTag(new int[]{row, col});


                //Controla las cordenadas de los disparos:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(disparo2){
                            // Recuperar las coordenadas del tag del botón
                            int[] tag = (int[]) v.getTag();
                            String row = getPositionFromRow(tag[0]);
                            int col = tag[1]+1;
                            MandarDisparo dis = new MandarDisparo();
                            dis.MandarDis(Tablero.this, gameid, token,row, col);
                            new Handler().postDelayed(() -> {
                                Button myButton = findViewById(R.id.bnt_disparo);
                                myButton.setVisibility(View.INVISIBLE);
                                disparo2 =false;
                                InfoMisBarcos infoBarcos = new InfoMisBarcos();
                                infoBarcos.MisBarcos(Tablero.this, gameid, token, new InfoMisBarcos.GameInfoCallback() {
                                    @Override
                                    public void onGameInfoReceived(ArrayList<Ships> ships, ArrayList<Gunfire> done, ArrayList<Gunfire> received) {
                                        // Recibir los barcos y los disparos
                                        barcos = ships;
                                        disparosRealizados = done;
                                        disparosRecibidos = received;

                                        // Mostrar los barcos en el tablero
                                        mostrarBarcosEnTablero();

                                        // Mostrar los disparos recibidos
                                        if (!disparosRecibidos.isEmpty()){
                                            mostrarDisparosR();
                                        }else{
                                            Toast.makeText(Tablero.this, "yfewudjk", Toast.LENGTH_SHORT).show();
                                        }


                                        //Mostart los disparos disparados
                                        if (!disparosRealizados.isEmpty()){
                                            mostrarDisparosD();
                                        }

                                    }
                                });
                            }, 500); // Espera 500ms antes de actualizar
                        }else{
                            Toast.makeText(Tablero.this, "No puedes disparar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                // Añadir el botón al GridLayout
                gridLayout.addView(button);
            }
        }
    }

    // Método para mostrar los barcos en el tablero
    private void mostrarBarcosEnTablero() {
        for (Ships ship : barcos) {
            Log.d("Tablero", "Número de barcos111 " + ship);
            for (Ships.Position position : ship.getPositions()) {
                // Obtener la fila y la columna del barco
                int row = getRowFromPosition(position.getRow());
                int col = position.getColumn() - 1;

                grid[row][col] = 1;
                Button button = (Button) ((GridLayout) findViewById(R.id.tutablero)).getChildAt(row * gridSize + (col));
                button.setBackgroundColor(Color.BLACK);
                // Colocar el barco en el tablero (puedes personalizar el color)
            }
        }

    }

    private void mostrarDisparosR() {

        for (Gunfire disp : disparosRecibidos) {
            if(Objects.equals(disp.getResult(), "hit")){
                //Toast.makeText(this, ""+disp.getResult(), Toast.LENGTH_SHORT).show();
                int row = getRowFromPosition(disp.getRow());
                int col = disp.getColumn() - 1;
                grid[row][col] = 1;
                Button button = (Button) ((GridLayout) findViewById(R.id.tutablero)).getChildAt(row * gridSize + (col));
                button.setBackgroundColor(Color.RED);
            } else if (Objects.equals(disp.getResult(), "sunk")){
                //Toast.makeText(this, ""+disp.getResult(), Toast.LENGTH_SHORT).show();
                int row = getRowFromPosition(disp.getRow());
                int col = disp.getColumn() - 1;
                grid[row][col] = 1;
                Button button = (Button) ((GridLayout) findViewById(R.id.tutablero)).getChildAt(row * gridSize + (col));
                button.setBackgroundColor(Color.GREEN);
            }else{
                //Toast.makeText(this, ""+disp.getResult(), Toast.LENGTH_SHORT).show();
                int row = getRowFromPosition(disp.getRow());
                int col = disp.getColumn() - 1;
                grid[row][col] = 1;
                Button button = (Button) ((GridLayout) findViewById(R.id.tutablero)).getChildAt(row * gridSize + (col));
                button.setBackgroundColor(Color.YELLOW);
            }

        }
    }

    private void mostrarDisparosD() {

        for (Gunfire disp : disparosRealizados) {
            if(Objects.equals(disp.getResult(), "hit")){
                Toast.makeText(this, ""+disp.getResult(), Toast.LENGTH_SHORT).show();
                int row = getRowFromPosition(disp.getRow());
                int col = disp.getColumn() - 1;
                grid[row][col] = 1;
                Button button = (Button) ((GridLayout) findViewById(R.id.tablerorival)).getChildAt(row * gridSize + (col));
                button.setBackgroundColor(Color.RED);
            }
            else if (Objects.equals(disp.getResult(), "sunk")){
            //Toast.makeText(this, ""+disp.getResult(), Toast.LENGTH_SHORT).show();
                int row = getRowFromPosition(disp.getRow());
                int col = disp.getColumn() - 1;
                grid[row][col] = 1;
                Button button = (Button) ((GridLayout) findViewById(R.id.tablerorival)).getChildAt(row * gridSize + (col));
                button.setBackgroundColor(Color.GREEN);
            }else{
                Toast.makeText(this, ""+disp.getResult(), Toast.LENGTH_SHORT).show();
                int row = getRowFromPosition(disp.getRow());
                int col = disp.getColumn() - 1;
                grid[row][col] = 1;
                Button button = (Button) ((GridLayout) findViewById(R.id.tablerorival)).getChildAt(row * gridSize + (col));
                button.setBackgroundColor(Color.YELLOW);
            }

        }
    }

    // Método para mapear las posiciones del barco a la posición en la UI
    private int getRowFromPosition(String row) {
        switch (row) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            case "E":
                return 4;
            case "F":
                return 5;
            case "G":
                return 6;
            case "H":
                return 7;
            case "I":
                return 8;
            case "J":
                return 9;
            default:
                return -1;
        }
    }

    private String getPositionFromRow(int row) {
        switch (row) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "G";
            case 7:
                return "H";
            case 8:
                return "I";
            case 9:
                return "J";
            default:
                return null;  // o una cadena vacía, dependiendo de lo que necesites
        }
    }
}

