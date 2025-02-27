package com.example.esquiv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class SecondaryActivity extends AppCompatActivity implements FragmentoFecha.Fecha {

    EditText etId, etNombre, etCordillera, etNotas, etRemontes, etKm;
    RatingBar ratingBar;
    TextView tvFecha;

    int id = -1;
    String nombre, cordillera, notas;
    int remontes;
    float kilometros, valoracion;
    long fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        etId = findViewById(R.id.editTextID);
        etNombre = findViewById(R.id.editTextNombre);
        etCordillera = findViewById(R.id.editTextCordillera);
        etNotas = findViewById(R.id.editTextNotas);
        etRemontes = findViewById(R.id.editTextNumberRemontes);
        etKm = findViewById(R.id.editTextNumberDecimalKM);
        ratingBar = findViewById(R.id.ratingBar);
        tvFecha = findViewById(R.id.textViewFecha);

        Intent i = getIntent();
        if(i.getIntExtra("ID", 0) != -1) {
            etId.setText(String.valueOf(i.getIntExtra("ID", 0)));
            id = i.getIntExtra("ID", 0); // seteamos la idea a editar
            etNombre.setText(i.getStringExtra("NOMBRE"));
            etCordillera.setText(i.getStringExtra("CORDILLERA"));
            etNotas.setText(i.getStringExtra("NOTAS"));
            etRemontes.setText(String.valueOf(i.getIntExtra("REMONTES", 0)));
            etKm.setText(String.valueOf(i.getFloatExtra("KM", 0.0f)));
            ratingBar.setRating(i.getFloatExtra("VALORACION", 0.0f));
            long fechaMilis = getIntent().getLongExtra("FECHA", 0);

            if (fechaMilis != 0) {
                // Crea un objeto Date con los milisegundos
                Date fecha = new Date(fechaMilis);

                // Usa el Calendar para obtener el día, mes, año, etc.
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fecha);

                // Extrae los valores de día, mes, año, etc.
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH) + 1; // Los meses comienzan desde 0 (enero)
                int año = calendar.get(Calendar.YEAR);

                tvFecha.setText(dia + "/" + (mes + 1) + "/" + año);
            } else {
                tvFecha.setText("Fecha no válida");
            }
        }
    }

    public void clickAceptar(View v) {
        if (etNombre.getText().toString().isEmpty() ||
                etCordillera.getText().toString().isEmpty() ||
                etNotas.getText().toString().isEmpty() ||
                etRemontes.getText().toString().isEmpty() ||
                etKm.getText().toString().isEmpty()) {

            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            //return; // No sigue ejecutando el código
        } else {
            // Si todos los campos están llenos, continua con la lógica
            nombre = etNombre.getText().toString();
            cordillera = etCordillera.getText().toString();
            notas = etNotas.getText().toString();
            valoracion = ratingBar.getRating();
            String fechaString = tvFecha.getText().toString();
            kilometros = Float.parseFloat(etKm.getText().toString());
            remontes = Integer.parseInt(etRemontes.getText().toString());

            // Toast.makeText(this, fecha+"", Toast.LENGTH_SHORT).show(); debug


            Intent i = new Intent();
            i.putExtra("NOMBRE", nombre);
            i.putExtra("VALORACION", valoracion);
            i.putExtra("ID", id);
            i.putExtra("KM", kilometros);
            i.putExtra("REMONTES", remontes);
            i.putExtra("NOTAS", notas);
            i.putExtra("CORDILLERA", cordillera);

            // convertir fecha string a milisegundos
            // Usamos Calendar para parsear la fecha
            String[] fechaArray = fechaString.split("/");

            int dia = Integer.parseInt(fechaArray[0]);
            int mes = Integer.parseInt(fechaArray[1]) - 1; // por que van hasta el 11
            int anio = Integer.parseInt(fechaArray[2]);

            // crear un calendario para establecer la fecha
            Calendar calendar = Calendar.getInstance();
            calendar.set(anio, mes, dia);

            // Obtener los milisegundos
            long milisegundos = calendar.getTimeInMillis();
            i.putExtra("FECHA", milisegundos);

            new AlertDialog.Builder(this)
                    .setTitle("¿Estás seguro?")
                    .setMessage("¿Deseas continuar o cancelar?")
                    .setPositiveButton("Continuar", (dialog, which) -> {
                        // Acción a realizar si el usuario presiona "Continuar"
                        Toast.makeText(this, "Acción continuada", Toast.LENGTH_SHORT).show();

                        // Después de la confirmación, se procede con el código
                        setResult(RESULT_OK, i);
                        finish(); // Finaliza la actividad
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        // Acción a realizar si el usuario presiona "Cancelar"
                        Toast.makeText(this, "Acción cancelada", Toast.LENGTH_SHORT).show();
                    })
                    .show(); // Mostrar el diálogo
        }
    }
    @Override
    public void pasarFecha(int año, int mes, int dia) {
        tvFecha.setText(dia+"/"+(mes+1)+"/"+año);
    }
    public void clickElegirFecha(View view) {
        FragmentoFecha ff= new FragmentoFecha();
        ff.show(getSupportFragmentManager(),"fecha");
    }
}