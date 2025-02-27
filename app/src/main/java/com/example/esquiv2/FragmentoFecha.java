package com.example.esquiv2;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class FragmentoFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Fecha fecha;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fecha=(Fecha)getActivity();
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) { //Crea la pantalla que se muestra
        Calendar calendar = Calendar.getInstance();
        // Para establecer la fecha del picker
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Los meses van de 0 (enero) a 11 (diciembre)
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog capturaFecha = new DatePickerDialog(getActivity(),this::onDateSet,year,month,day); // indicar el establecedor de fechas, con la fecha indicada
        return capturaFecha;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) { // evento que estaclece la fecha
        Toast.makeText(getActivity(), i+ "/"+(i1+1)+"/"+i2, Toast.LENGTH_SHORT).show();
        fecha.pasarFecha(i,i1,i2);
    }

    public interface Fecha{ // Interfaz para pasar los datos
        public void pasarFecha(int año,int mes, int dia); // Metodo para pasar los datos
    }
}