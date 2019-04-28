package com.hometodo.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hometodo.BDD.CourseContract;
import com.hometodo.Fragments.Inicio;
import com.hometodo.Fragments.Materia;
import com.hometodo.Models.Course;
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

import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;


public class AgregarMateria extends AppCompatActivity {

    //declaración de variables
    Spinner spnDias;
    Button btn_seleccionarColor,btn_agregarMateria;
    Calendar calendar, calendar1;
    TextInputLayout txt_horaInicio,txt_horaFin, txt_profesor,txt_nombreMateria,txt_salon;

    //estas son las variables globales que se guardarán en bdd
    String nombreMateria="", horaInicio="",horaFin="",nombreProfesor="",color="",salon="";
    int dias=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_materia);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txt_nombreMateria = findViewById(R.id.txt_nombreMateria);
        txt_profesor = findViewById(R.id.txt_profesor);

        spnDias = findViewById(R.id.spnDias);
        txt_horaInicio = findViewById(R.id.txt_horaInicio);
        txt_horaFin = findViewById(R.id.txt_horaFin);

        txt_salon = findViewById(R.id.txt_salon);

        btn_seleccionarColor = findViewById(R.id.btn_seleccionarColor);
        btn_agregarMateria = findViewById(R.id.btn_agregarMateria);

        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.dias, R.layout.spinner_item);
        spnDias.setAdapter(adapter1);

        //estos métodos tienen un onchangeListener para los campos de cambio de hora
        //y palsmar el valor en el edittext
        calendarioHoraInicio();
        calendarioHoraFin();

        btn_agregarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                    Course curso=new Course(nombreProfesor, "1", nombreMateria, horaInicio, color, dias, salon);

                    if(curso.save(getApplicationContext())){
                        Toast.makeText(getApplicationContext(), "Guardado con éxito", Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(getApplicationContext(), "RIP", Toast.LENGTH_LONG).show();

                    /*Aquí va el método para guardar en bdd y
                    para comprar que no existan los campos en la bdd*/

                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    private boolean validarCampos() {
        boolean validator=false;
        nombreMateria=txt_nombreMateria.getEditText().getText().toString();
        horaInicio=txt_horaInicio.getEditText().getText().toString();
        horaFin=txt_horaFin.getEditText().getText().toString();
        nombreProfesor=txt_profesor.getEditText().getText().toString();
        salon=txt_salon.getEditText().getText().toString();


        //validar materia
        if(nombreMateria.replaceAll(" ","").isEmpty()) {
            txt_nombreMateria.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar el nombre de la materia", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_nombreMateria.setError(null);
        }
        if(nombreMateria.length()>128) {
            txt_nombreMateria.setError(" ");
            Toast.makeText(getApplicationContext(), "Lo siento, la materia no puede contener más de 128 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_nombreMateria.setError(null);
        }

        //validar profesor
        if(nombreProfesor.replaceAll(" ","").isEmpty()) {
            txt_profesor.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar el nombre del profesor", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_profesor.setError(null);
        }
        if(nombreProfesor.length()>128) {
            txt_profesor.setError(" ");
            Toast.makeText(getApplicationContext(), "Lo siento, el nombre del profesor no puede contener más de 128 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_profesor.setError(null);
        }
        if(containsSpecialCharacter(nombreProfesor)){
            txt_profesor.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener caracteres especiales", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_profesor.setError(null);
        }
        if (conatainsNumber(nombreProfesor)) {
            txt_profesor.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener números", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_profesor.setError(null);
        }

        //validar hora inicio y fin
        if(horaInicio.replaceAll(" ","").isEmpty()) {
            txt_horaInicio.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar la hora de inicio", Toast.LENGTH_LONG).show();
            return false;
        } else {
            txt_horaInicio.setError(null);
        }
        /*if(horaFin.replaceAll(" ","").isEmpty()) {
            txt_horaFin.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar la hora de fin", Toast.LENGTH_LONG).show();
            return false;
        }else {
            txt_horaFin.setError(null);
        }

        try {
            DateFormat dateFormat = new SimpleDateFormat ("hh:mm");
            Date comparar1, comparar2;
            comparar1 = dateFormat.parse(horaInicio);
            comparar2 = dateFormat.parse(horaFin);

            if (comparar1.after(comparar2)){
                Toast.makeText(getApplicationContext(), "Lo siento la hora de inicio no puede ser menor a la final", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        //validar profesor
        if(salon.replaceAll(" ","").isEmpty()) {
            txt_salon.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar el salón", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_salon.setError(null);
        }
        if(salon.length()>15) {
            txt_salon.setError(" ");
            Toast.makeText(getApplicationContext(), "Lo siento, el salón no puede contener más de 15 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }else{
            txt_salon.setError(null);
        }
        //validar color
        if(color.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Favor de seleccionar un color", Toast.LENGTH_LONG).show();
            return false;
        }

        //validar color
        if(spnDias.getSelectedItemPosition()==0) {
            Toast.makeText(getApplicationContext(), "Favor de seleccionar los días", Toast.LENGTH_LONG).show();
            return false;
        }

        dias= spnDias.getSelectedItemPosition();//se guarda el valor en días

        List<Course> myList = new ArrayList<>();
        List listHoras=new ArrayList<>();
        List listDias=new ArrayList<>();

        myList=new Course.QueryBuilder(getApplicationContext()).where(CourseContract.CourseEntry.COLUMN_BEGIN_TIME, horaInicio).all();

        for (int i=0; i<myList.size();i++){
            listHoras.add(myList.get(i).getBeginTime());
            listDias.add(myList.get(i).getDays());

        }
        for (int i=0; i<myList.size();i++){
            if(listHoras.get(i).equals(horaInicio)&&listDias.get(i).equals(dias)){
                Toast.makeText(getApplicationContext(), "No pueden existir 2 cursos que empiecen a la misma hora", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;

    }

    private boolean conatainsNumber(String string) {
        String pattern1 = "^(?=.*[0-9])";
        Pattern reg1 = Pattern.compile(pattern1);
        Matcher match1 = reg1.matcher(string);
        return match1.find();
    }

    private boolean containsSpecialCharacter(String string) {
        boolean locCaracterEspecial=false;

        String pattern1 = "^(?=.*[(){}<>@#$+=*&¿?])";
        Pattern reg1 = Pattern.compile(pattern1);
        Matcher match1 = reg1.matcher(string);
        locCaracterEspecial=match1.find();

        if(!locCaracterEspecial) {
            pattern1 = "^(?=.*[¡;:])";
            reg1 = Pattern.compile(pattern1);
            match1 = reg1.matcher(string);
            locCaracterEspecial = match1.find();
        }

        if(!locCaracterEspecial) {
            pattern1 = "^(?=.*['!.])";
            reg1 = Pattern.compile(pattern1);
            match1 = reg1.matcher(string);
            locCaracterEspecial = match1.find();
        }

        if(!locCaracterEspecial) {
            pattern1 = "^(?=.*[,])";
            reg1 = Pattern.compile(pattern1);
            match1 = reg1.matcher(string);
            locCaracterEspecial = match1.find();
        }

        if(!locCaracterEspecial) {
            pattern1 = "^(?=.*[-])";
            reg1 = Pattern.compile(pattern1);
            match1 = reg1.matcher(string);
            locCaracterEspecial = match1.find();
        }

        if(!locCaracterEspecial) {
            pattern1 = "^(?=.*[_])";
            reg1 = Pattern.compile(pattern1);
            match1 = reg1.matcher(string);
            locCaracterEspecial = match1.find();
        }
        return locCaracterEspecial;
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


    }
    private void calendarioHoraInicio() {
        calendar = Calendar.getInstance();
        txt_horaInicio.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AgregarMateria.this);
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
                        txt_horaInicio.getEditText().setText(hour +":"+ minute);
                        horaInicio=hour +":"+ minute;
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

        txt_horaInicio.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        txt_horaInicio.getEditText().setInputType(InputType.TYPE_NULL);

        txt_horaInicio.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_horaInicio.setError(null);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void calendarioHoraFin() {
        calendar1 = Calendar.getInstance();
        txt_horaFin.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AgregarMateria.this);
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
                        txt_horaFin.getEditText().setText(hour +":"+ minute);
                        horaFin=hour +":"+ minute;

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

        txt_horaFin.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        txt_horaFin.getEditText().setInputType(InputType.TYPE_NULL);

        txt_horaFin.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_horaFin.setError(null);

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void seleccionarColor(View view) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogLayout = getLayoutInflater().inflate(R.layout.color_piker_accent, null);
        final LineColorPicker colorPicker = dialogLayout.findViewById(R.id.color_picker_accent);
        final TextView dialogTitle = dialogLayout.findViewById(R.id.cp_accent_title);
        dialogTitle.setText("Selecciona un color");
        colorPicker.setColors(getAccentColors(getApplicationContext()));
        colorPicker.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int c) {
                dialogTitle.setBackgroundColor(c);

            }
        });
        dialogBuilder.setView(dialogLayout);
        dialogBuilder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                btn_seleccionarColor.setBackgroundTintList(ColorStateList.valueOf(colorPicker.getColor()));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(colorPicker.getColor()));
                color= String.valueOf(colorPicker.getColor());
            }
        });
        dialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialogBuilder.show();
    }

    public static int[] getAccentColors(Context context){
        return new int[]{
                ContextCompat.getColor(context, R.color.accent_red),
                ContextCompat.getColor(context, R.color.accent_pink),
                ContextCompat.getColor(context, R.color.accent_purple),
                ContextCompat.getColor(context, R.color.accent_deep_purple),
                ContextCompat.getColor(context, R.color.accent_indago),
                ContextCompat.getColor(context, R.color.accent_blue),
                ContextCompat.getColor(context, R.color.accent_cyan),
                ContextCompat.getColor(context, R.color.accent_teal),
                ContextCompat.getColor(context, R.color.accent_green),
                ContextCompat.getColor(context, R.color.accent_yellow),
                ContextCompat.getColor(context, R.color.accent_amber),
                ContextCompat.getColor(context, R.color.accent_orange),
                ContextCompat.getColor(context, R.color.accent_brown),
                ContextCompat.getColor(context, R.color.accent_black)
        };
    }


}
