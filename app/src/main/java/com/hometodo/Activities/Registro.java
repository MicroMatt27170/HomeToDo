package com.hometodo.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hometodo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registro extends AppCompatActivity  {

    Button perfil_btnCargar, perfil_btnHacer;
    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PICTURE = 200;
    private ImageView perfil_img;
    private static final String IMAGE_DIRECTORY = "/DCIM";

    private TextInputLayout txtNombre,txtApellidos,txtAlias,txtCarrera,txtSemestre;
    private static final int MY_PERMISSONS = 1 ;
    private String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        txtNombre=findViewById(R.id.txtNombre);
        txtApellidos=findViewById(R.id.txtApellidos);
        txtAlias=findViewById(R.id.txtAlias);
        txtCarrera=findViewById(R.id.txtCarrera);
        txtSemestre=findViewById(R.id.txtSemestre);


        perfil_btnCargar  =  findViewById(R.id.perfil_btnCargar);
        perfil_btnHacer=findViewById(R.id.perfil_btnHacer);

        perfil_img = findViewById(R.id.perfil_img);

        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            //Si alguno de los permisos no esta concedido lo solicita
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSONS);
        }


        SharedPreferences shared = this.getApplicationContext().getSharedPreferences("userInfo", MODE_PRIVATE);

        try {
            Uri selectedImage = Uri.parse(shared.getString("imgPerfil","android.resource://"+getApplicationContext().getPackageName()+"/drawable/"+R.drawable.menu_img_usuario));
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getApplicationContext().getContentResolver().query(
                    selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            perfil_img.setVisibility(View.VISIBLE);
            // Bitmap thumbnail =
            // (BitmapFactory.decodeFile(picturePath));
            Bitmap thumbnail = decodeSampledBitmapFromResource(
                    picturePath, 500, 500);

            // rotated
            Bitmap thumbnail_r = imageOreintationValidator(
                    thumbnail, picturePath);
            perfil_img.setBackground(null);
            perfil_img.setImageBitmap(thumbnail_r);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        perfil_btnCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        perfil_btnHacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PICTURE); }
        });


    }




    public void registrarUsuario(View view){
        String nombre=txtNombre.getEditText().getText().toString();
        String apellidos=txtApellidos.getEditText().getText().toString();
        String alias=txtAlias.getEditText().getText().toString();
        String carrera=txtCarrera.getEditText().getText().toString();
        String semestre=txtSemestre.getEditText().getText().toString();

        if(nombre.replaceAll(" ","").isEmpty()) {
            txtNombre.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar su nombre (s)", Toast.LENGTH_LONG).show();
            return;
        } else {
            txtNombre.setError(null);
        }
        if(nombre.length()>30){
            txtNombre.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener más de 30 caracteres", Toast.LENGTH_LONG).show();
            return;
        }
        if(containsSpecialCharacter(nombre)){
            txtNombre.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener caracteres especiales", Toast.LENGTH_LONG).show();
            return;
        }
        if (conatainsNumber(nombre)) {
            txtNombre.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener números", Toast.LENGTH_LONG).show();
            return;
        }

        if(apellidos.replaceAll(" ","").isEmpty()) {
            txtApellidos.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar sus apellidos", Toast.LENGTH_LONG).show();
            return;
        } else {
            txtApellidos.setError(null);
        }
        if(apellidos.length()>30){
            txtApellidos.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener más de 30 caracteres", Toast.LENGTH_LONG).show();
            return;
        }
        if(containsSpecialCharacter(apellidos)){
            txtApellidos.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener caracteres especiales", Toast.LENGTH_LONG).show();
            return;
        }
        if (conatainsNumber(apellidos)) {
            txtApellidos.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener números", Toast.LENGTH_LONG).show();
            return;
        }

        if(alias.replaceAll(" ","").isEmpty()) {
            txtAlias.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar su alias", Toast.LENGTH_LONG).show();
            return;
        } else {
            txtAlias.setError(null);
        }
        if(alias.length()>15){
            txtAlias.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener más de 15 caracteres", Toast.LENGTH_LONG).show();
            return;
        }

        if(carrera.replaceAll(" ","").isEmpty()) {
            txtCarrera.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar su carrera", Toast.LENGTH_LONG).show();
            return;
        } else {
            txtCarrera.setError(null);
        }
        if(containsSpecialCharacter(carrera)){
            txtCarrera.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener caracteres especiales", Toast.LENGTH_LONG).show();
            return;
        }
        if (conatainsNumber(carrera)) {
            txtCarrera.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener números", Toast.LENGTH_LONG).show();
            return;
        }
        if(carrera.length()>60){
            txtCarrera.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener más de 60 caracteres", Toast.LENGTH_LONG).show();
            return;
        }

        if(semestre.replaceAll(" ","").isEmpty()) {
            txtSemestre.setError(" ");
            Toast.makeText(getApplicationContext(), "Favor de capturar su carrera", Toast.LENGTH_LONG).show();
            return;
        }
        if(containsSpecialCharacter(semestre)){
            txtSemestre.setError(" ");
            Toast.makeText(getApplicationContext(), "Este campo no puede contener caracteres especiales", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences shared = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("nombre",  nombre);
        editor.putString("apellido",  apellidos);
        editor.putString("alias",  alias);
        editor.putString("carrera",  carrera);
        editor.putString("semestre",  semestre);

        editor.commit();

        Intent intent = new Intent(Registro.this, MainActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        try{
            if (requestCode == PICK_IMAGE) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        try {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = getApplicationContext().getContentResolver().query(
                                    selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();
                            perfil_img.setVisibility(View.VISIBLE);
                            // Bitmap thumbnail =
                            // (BitmapFactory.decodeFile(picturePath));
                            Bitmap thumbnail = decodeSampledBitmapFromResource(
                                    picturePath, 500, 500);

                            // rotated
                            Bitmap thumbnail_r = imageOreintationValidator(
                                    thumbnail, picturePath);
                            perfil_img.setBackground(null);
                            perfil_img.setImageBitmap(thumbnail_r);

                            SharedPreferences shared = this.getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("imgPerfil", selectedImage.toString());
                            editor.commit();

                            Toast.makeText(getApplicationContext(), "Imagen cambiada con éxito" + data.getExtras().get("data"), Toast.LENGTH_SHORT).show();

                            //Intent i = new Intent(getActivity(), MainActivity.class);
                            //startActivity(i);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(requestCode == TAKE_PICTURE)
                if(resultCode == Activity.RESULT_OK){
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    perfil_img.setImageBitmap(thumbnail);
                    saveImage(thumbnail);

                    SharedPreferences shared = this.getSharedPreferences("userInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("imgPerfil", data.getData().toString());
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "Imagen cambiada con éxito", Toast.LENGTH_SHORT).show();

                }
        } catch(Exception e){}

    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    // for roted image......
    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathToFile,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathToFile, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        Log.e("inSampleSize", "inSampleSize______________in storage"
                + options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathToFile, options);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////


    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


}
