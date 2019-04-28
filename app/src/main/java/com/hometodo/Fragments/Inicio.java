package com.hometodo.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hometodo.Adapters.InicioAdapter;
import com.hometodo.Adapters.TareasAdapter;
import com.hometodo.Models.Course;
import com.hometodo.Models.Homework;
import com.hometodo.Models.InicioModel;
import com.hometodo.Models.TareaModel;
import com.hometodo.R;

import java.util.ArrayList;
import java.util.List;


public class Inicio extends Fragment {

    // UI references and main declarations
    private List<InicioModel> inicioList = new ArrayList<>(); //it's a list of objects based in LogModel attributes
    private RecyclerView rv;
    private InicioAdapter mAdapter; //this is the adapter that will manage de recycle view



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
        View retView = inflater.inflate(R.layout.inicio, container, false);

        setHasOptionsMenu(false);
        mAdapter = new InicioAdapter(inicioList, getContext()); //the adapter will be charged by the model list
        rv = retView.findViewById(R.id.rv);


        return retView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Inicio");

        //this declares the recycle view
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);


        try {
            List <Homework>myList = new Homework.QueryBuilder(getActivity()).all();
            List <Course> materias=new Course.QueryBuilder(getActivity()).all();

            //fill the recycleview with info, going through the object model
            int index=0;
            //fill the recycleview with info, going through the object model
            for(int i=0; i<myList.size(); i++){
                InicioModel lists=new InicioModel();
                lists.setNombre(myList.get(i).getTitle());
                lists.setDescripcion(myList.get(i).getDescription());//
                lists.setFecha(myList.get(i).getDeliveryDate());

                for (int j=0; j<materias.size();j++){
                    if(materias.get(j).getId()==myList.get(i).getCourseId()){
                        lists.setMateria(materias.get(j).getCourseName());
                        index=j;
                    }
                }
                lists.setColor(materias.get(index).getColor());
                inicioList.add(lists);
            }
        }catch (Exception e){

        }



    }


    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment) {
        android.support.v4.app.FragmentManager fragmentManager = this.getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contenedor, destFragment);
        fragmentTransaction.commit();
    }

}
