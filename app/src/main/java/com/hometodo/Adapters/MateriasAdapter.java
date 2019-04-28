package com.hometodo.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hometodo.Models.MateriaModel;
import com.hometodo.R;

import java.util.List;


public class MateriasAdapter extends RecyclerView.Adapter<MateriasAdapter.MyViewHolder> {
    private List<MateriaModel> materiaList;
    private Context context;
    private static int lastClickedPosition=0;


    /**
     * Constructior
     * @param materiaList This is the list of information
     * @param context  the activity context
     */
    public MateriasAdapter(List<MateriaModel> materiaList, Context context) {
        this.materiaList = materiaList;
        this.context = context;
    }

    /*
     * The class that gets the UI reference from the layout
     * it gets a global information that will be used during
     * the adapter. it extend to the recycleView in which will
     * appear the attributes
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_nombre,txt_dias,txt_hora,txt_profesor,txt_salon;
        public LinearLayout linear_materias;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(View itemView) {
            super(itemView);

            txt_nombre=(TextView) itemView.findViewById(R.id.txt_nombre);
            txt_dias=(TextView) itemView.findViewById(R.id.txt_dias);
            txt_hora=(TextView) itemView.findViewById(R.id.txt_hora);
            txt_profesor=(TextView) itemView.findViewById(R.id.txt_profesor);
            txt_salon=(TextView) itemView.findViewById(R.id.txt_salon);
            linear_materias=itemView.findViewById(R.id.linear_materias);

        }
    }

    /*
     * The method is constructed with the MyViewHolder class and
     * it defines the xml will be used and get the UI attributes
     * */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_materias, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * This method uses the global UI references to set the
     * information from the model in the specific attribute
     * @param holder this gets the UI reference and the inflated layout
     * @param position  This gets the position from the recycleview based in the model
     * @return nothing
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try{
            MateriaModel materia = materiaList.get(position);
            holder.txt_nombre.setText(materia.getNombre());
            holder.txt_dias.setText(materia.getDias());
            holder.txt_hora.setText(materia.getHora());
            holder.txt_profesor.setText(materia.getProf());
            holder.txt_salon.setText(materia.getSalon());

            GradientDrawable drawable = (GradientDrawable)holder.linear_materias.getBackground();
            drawable.setStroke(8, ColorStateList.valueOf(Integer.parseInt(materia.getColor()))); // set stroke width and stroke color

        }catch (Exception ex){
            Toast.makeText(holder.itemView.getContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            Log.d("MATERIA ADAPTER ", "response error " + ex.toString());

        }


    }


    /**
     * This method is used the get the recycleview length based
     * in the model list
     * @return the list length
     */
    @Override
    public int getItemCount() {
        return materiaList.size();
    }


}