package com.hometodo.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hometodo.Models.Course;
import com.hometodo.Models.Exam;
import com.hometodo.Models.Homework;
import com.hometodo.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgregarExamen extends AppCompatActivity {

    Spinner spn_materia;
    TextInputLayout txtNombre,txt_descripcion,txt_fecha,txt_hora;
    Button btn_agregarExamen;
    Calendar calendar, calendar1;

    //estas son las variables globales que se guardarán en bdd
    String titulo="", descripcion="",fecha="",hora="";
    long materia=0;

    List<Course> myList = new ArrayList<>();
    List materias=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_examen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtNombre = findViewById(R.id.txtNombre);
        txt_descripcion = findViewById(R.id.txt_descripcion);
        txt_fecha = findViewById(R.id.txt_fecha);
        txt_hora = findViewById(R.id.txt_hora);
        btn_agregarExamen = findViewById(R.id.btn_agregarExamen);
        
        spn_materia = findViewById(R.id.spn_materia);

        consultarMaterias();

        calendarioHoraEntrega();
        calendarioFechaEntrega();

        ArrayList exam = new Exam.QueryBuilder(getApplicationContext()).all();

        btn_agregarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                    Exam exam=new Exam(materia,descripcion,titulo,fecha,false);

                    if(exam.save(getApplicationContext())){
                        Toast.makeText(getApplicationContext(), "Guardado con éxito", Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(getApplicationContext(), "RIP", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private void consultarMaterias() {
        try{

            myList=new Course.QueryBuilder(getApplicationContext()).all();

            for (int i=0; i<myList.size();i++){
                materias.add(myList.get(i).getCourseName());
            }

            ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_item, materias);
            spn_materia.setAdapter(adapter);

            if(materias.size()<=0) {
                Toast.makeText(getApplicationContext(), "Lo siento primero debe agregar una materia", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }catch (Exception ex){
            Log.d("Materias", ex.toString());

        }

    }

    private boolean validarCampos() {
        boolean validator=false;
        titulo=txtNombre.getEditText().getText().toString();
        descripcion=txt_descripcion.getEditText().getText().toString();
        fecha=txt_fecha.getEditText().getText().toString();
        hora=txt_hora.getEditText().getText().toString();


        //validar materia
        if(titulo.replaceAll(" ","").isEmpty()) {
            txtNombre.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar el nombre de la materia", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txtNombre.setError(null);
        }
        if(titulo.length()>128) {
            txtNombre.setError(" ");
            Toast.makeText(getApplicationContext(), "Lo siento, la materia no puede contener más de 128 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txtNombre.setError(null);
        }

        //validar descripcion
        if(descripcion.replaceAll(" ","").isEmpty()) {
            txt_descripcion.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar el nombre de la materia", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_descripcion.setError(null);
        }
        if(descripcion.length()>256) {
            txt_descripcion.setError(" ");
            Toast.makeText(getApplicationContext(), "Lo siento, la materia no puede contener más de 128 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_descripcion.setError(null);
        }

        //validar hora inicio y fin
        if(hora.replaceAll(" ","").isEmpty()) {
            txt_hora.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar la hora", Toast.LENGTH_LONG).show();
            return false;
        } else {
            txt_hora.setError(null);
        }
        if(fecha.replaceAll(" ","").isEmpty()) {
            txt_fecha.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar la fecha", Toast.LENGTH_LONG).show();
            return false;
        }else {
            txt_fecha.setError(null);
        }

        Date fechaEntrega = null;
        try {
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

            try {
                fechaEntrega=dateFormat.parse(txt_fecha.getEditText().getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date fechaActual=new Date();

            if (fechaEntrega.before(fechaActual)){
                Toast.makeText(getApplicationContext(), "Lo siento el día de entrega no puede ser menor al día actual", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        materia=myList.get(spn_materia.getSelectedItemPosition()).getId();
        return true;
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


    }

    private void calendarioHoraEntrega() {
        calendar = Calendar.getInstance();
        
        txt_hora.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AgregarExamen.this);
                View dialogLayout = getLayoutInflater().inflate(R.layout.time_picker, null);
                final TimePicker picker=dialogLayout.findViewById(R.id.datePicker1);

                picker.setIs24HourView(true);
                dialogBuilder.setView(dialogLayout);


                dialogBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int hour, minute;
                        if (Build.VERSION.SDK_INT >= 23 ){
                            hour = picker.getHour();
                            minute = picker.getMinute();
                        }
                        else{
                            hour = picker.getCurrentHour();
                            minute = picker.getCurrentMinute();
                        }
                        txt_hora.getEditText().setText(hour +":"+ minute);
                        hora=hour +":"+ minute;
                    }
                });
                dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = dialogBuilder.create();
                alert.setTitle("Seleccionar Hora");
                alert.show();

            }
        });

        txt_hora.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        txt_hora.getEditText().setInputType(InputType.TYPE_NULL);

        txt_hora.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_hora.setError(null);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void calendarioFechaEntrega() {
        calendar1 = Calendar.getInstance();
        txt_fecha.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        AgregarExamen.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                calendar.set(year, monthOfYear, dayOfMonth);
                                txt_fecha.getEditText().setText(df.format(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
                //DatePickerDialog.newInstance(getActivity(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });

        txt_fecha.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        txt_fecha.getEditText().setInputType(InputType.TYPE_NULL);

        txt_fecha.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_fecha.setError(null);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}
