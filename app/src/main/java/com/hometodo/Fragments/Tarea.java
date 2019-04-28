package com.hometodo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hometodo.Activities.AgregarTarea;
import com.hometodo.Adapters.MateriasAdapter;
import com.hometodo.Adapters.TareasAdapter;
import com.hometodo.Models.Course;
import com.hometodo.Models.Exam;
import com.hometodo.Models.Homework;
import com.hometodo.Models.MateriaModel;
import com.hometodo.Models.TareaModel;
import com.hometodo.R;

import java.util.ArrayList;
import java.util.List;


public class Tarea extends Fragment {

    // UI references and main declarations
    private List<TareaModel> tareaList = new ArrayList<>(); //it's a list of objects based in LogModel attributes
    private RecyclerView rv;
    private TareasAdapter mAdapter; //this is the adapter that will manage de recycle view


    FloatingActionButton fab;

    public Tarea() {

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Tareas");

        //this declares the recycle view
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);

        try {
            List <Homework>myList = new Homework.QueryBuilder(getActivity()).all();
            List <Course> materias=new Course.QueryBuilder(getActivity()).all();

            int index=0;
            //fill the recycleview with info, going through the object model
            for(int i=0; i<myList.size(); i++){
                TareaModel lists=new TareaModel();
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
                tareaList.add(lists);
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
        View retView = inflater.inflate(R.layout.tarea, container, false);
        fab = (FloatingActionButton) retView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AgregarTarea.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        mAdapter = new TareasAdapter(tareaList, getContext()); //the adapter will be charged by the model list
        rv = retView.findViewById(R.id.rv);
        return retView;
    }


}
