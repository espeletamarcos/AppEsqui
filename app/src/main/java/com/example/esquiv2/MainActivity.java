package com.example.esquiv2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Adaptador adaptador;
    RecyclerView recyclerView;
    BDHelper bdHelper;
    SQLiteDatabase dbr, dbw;
    ActivityResultLauncher<Intent> lanzador;
    int posicionEdicion;
    ArrayList<Estacion> listaDatos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        registerForContextMenu(recyclerView);
        bdHelper = new BDHelper(this, "bdestacionesqui", null, 5);
        consultar();



        lanzador = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult resultado) { //es ek resultado del intent
                        if (resultado.getResultCode() == RESULT_OK) {
                            Intent i = resultado.getData();
                            if (i.getIntExtra("ID", 0) == -1) { // si se es crear
                                String nombre = i.getStringExtra("NOMBRE");
                                String cordilleras = i.getStringExtra("CORDILLERA");
                                String notas = i.getStringExtra("NOTAS");
                                int remontes = i.getIntExtra("REMONTES", 0);
                                float kilometros = i.getFloatExtra("KM", 0.0f);
                                long fecha = i.getLongExtra("FECHA", 0);
                                float valoracion = i.getFloatExtra("VALORACION", 0.0f);
                                int id = i.getIntExtra("ID", 0);
                                Toast.makeText(MainActivity.this, fecha+"", Toast.LENGTH_SHORT).show();

                                // Se está creando un nuevo estacion
                                //Estacion nuevaEstacion = new Estacion(id, nombre, cordilleras, remontes, kilometros, fecha, valoracion, notas); //no hace falta, consultamos database
                                // datos.add(nuevaEstacion); // Añade el nuevo alumno, luego hay que actualizarla, no hace falta, consultamos database

                                // Insertar en la base de datos
                                dbw = bdHelper.getWritableDatabase(); // para acceder a la base
                                dbw.execSQL("INSERT INTO esqui (nombre, cordillera, n_remontes, km_pistas, fecha_ult_visita, valoracion, notas) VALUES ('" + nombre + "', '" + cordilleras + "', '" + remontes + "', '" + kilometros + "', '" + fecha + "', '" + valoracion + "', '" + notas + "')");

                                consultar();
                                // Notificar al adaptador que los datos han cambiado y se debe actualizar el ListView

                                Toast.makeText(MainActivity.this, "Estacion agregado", Toast.LENGTH_SHORT).show();

                            } else {
                                String nombre = i.getStringExtra("NOMBRE");
                                String cordilleras = i.getStringExtra("CORDILLERA");
                                String notas = i.getStringExtra("NOTAS");
                                int remontes = i.getIntExtra("REMONTES", 0);
                                float kilometros = i.getFloatExtra("KM", 0.0f);
                                long fecha = i.getLongExtra("FECHA", 0);
                                float valoracion = i.getFloatExtra("VALORACION", 0.0f);
                                int id = i.getIntExtra("ID", 0);

                                // Se está editando un estacion
                                dbw = bdHelper.getWritableDatabase(); // para acceder a la base
                                dbw.execSQL("UPDATE esqui SET nombre='" + nombre + "', cordillera='" + cordilleras + "', n_remontes='" + remontes + "', km_pistas='" + kilometros + "', fecha_ult_visita='" + fecha + "', valoracion='" + valoracion + "', notas='" + notas + "' WHERE _id=" + id);
                                Toast.makeText(MainActivity.this, "Estacion modificado", Toast.LENGTH_SHORT).show();
                                // Notificar al adaptador que los datos han cambiado y se debe actualizar el ListView

                                consultar();
                            }

                        } else {
                            // no hacemos nada
                        }

                    }
                }
        );
    }


    //Creamos el menú principal (Alta)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    //Funcionalidad a los items del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itemAlta) {
            Intent i = new Intent(MainActivity.this, SecondaryActivity.class);
            i.putExtra("ID", -1);
            lanzador.launch(i);
        } else {
            System.exit(0);
        }
        return true;
    }

    public void consultar() {
        dbr = bdHelper.getReadableDatabase(); //Abrimos bd modo lectura

        Cursor c = dbr.rawQuery("SELECT * FROM esqui", null);

        listaDatos.clear();

        Estacion nuevaEstacion;

        if(c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex("_id"));
                String nombre = c.getString(c.getColumnIndex("nombre"));
                String cordillera = c.getString(c.getColumnIndex("cordillera"));
                int n_remontes = c.getInt(c.getColumnIndex("n_remontes"));
                float km_pistas = c.getFloat(c.getColumnIndex("km_pistas"));
                long fecha_ult_visita = c.getLong(c.getColumnIndex("fecha_ult_visita"));
                float valoracion = c.getFloat(c.getColumnIndex("valoracion"));
                String notas = c.getString(c.getColumnIndex("notas"));

                nuevaEstacion = new Estacion(id, nombre, cordillera, n_remontes, km_pistas, fecha_ult_visita, valoracion, notas);

                listaDatos.add(nuevaEstacion);
            } while (c.moveToNext());
        }

        c.close();

        adaptador = new Adaptador(this, listaDatos, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);
    }

    @Override
    public void onClick(View v) { //Click corto en el Recycler por ejemplo editar es aqui
        Estacion estEditar = listaDatos.get(recyclerView.getChildAdapterPosition(v));

        Intent i = new Intent(MainActivity.this, SecondaryActivity.class);
        i.putExtra("NOMBRE", estEditar.getNombre());
        i.putExtra("CORDILLERA", estEditar.getCordillera());
        i.putExtra("REMONTES", estEditar.getN_remontes());
        i.putExtra("KM", estEditar.getKm_pistas());
        i.putExtra("FECHA", estEditar.getFecha_ult_visita());
        i.putExtra("VALORACION", estEditar.getValoracion());
        i.putExtra("NOTAS", estEditar.getNotas());
        i.putExtra("ID", estEditar.getId());

        lanzador.launch(i); // Lanzamos el intent que acabos de crear
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case 122:

                int position = adaptador.getAdapterPosition();
                Estacion estacionBorrar = listaDatos.get(position);
                int id = estacionBorrar.getId();
                listaDatos.remove(position);
                adaptador.notifyDataSetChanged();

                String sql = "Delete FROM esqui WHERE _id=" + id;

                dbw = bdHelper.getWritableDatabase(); // para acceder a la base de datos
                dbw.execSQL(sql); // Ejecuta sentencia sql sin más.
                Toast.makeText(this, "Estacion eliminada en la bd", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}