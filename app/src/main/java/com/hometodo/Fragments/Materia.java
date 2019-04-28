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

import com.hometodo.Activities.AgregarMateria;
import com.hometodo.Adapters.MateriasAdapter;
import com.hometodo.Models.Course;
import com.hometodo.Models.MateriaModel;
import com.hometodo.R;

import java.util.ArrayList;
import java.util.List;


public class Materia extends Fragment {

    // UI references and main declarations
    private List<MateriaModel> materiaList = new ArrayList<>(); //it's a list of objects based in LogModel attributes
    private RecyclerView rv;
    private MateriasAdapter mAdapter; //this is the adapter that will manage de recycle view


    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
        View retView = inflater.inflate(R.layout.materia, container, false);
        fab = (FloatingActionButton) retView.findViewById(R.id.fab);

        mAdapter = new MateriasAdapter(materiaList, getContext()); //the adapter will be charged by the model list
        rv = retView.findViewById(R.id.rv);
        return retView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Materias");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(getContext(), AgregarMateria.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //this declares the recycle view
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);

        try{
            List <Course>myList = new ArrayList<>();

            myList=new Course.QueryBuilder(getActivity()).all();

            //fill the recycleview with info, going through the object model
            for(int i=0; i<myList.size(); i++){
                MateriaModel lists=new MateriaModel();

                lists.setNombre(myList.get(i).getCourseName());
                if (myList.get(i).getDays()==1){
                    lists.setDias("Lunes y Miércoles");//
                }else if (myList.get(i).getDays()==2){
                    lists.setDias("Martes y Jueves");//
                }else{
                    lists.setDias("Viernes");//
                }
                lists.setHora(myList.get(i).getBeginTime());
                lists.setProf(myList.get(i).getProfessor());
                lists.setSalon(myList.get(i).getRoom());
                lists.setColor(myList.get(i).getColor());
                materiaList.add(lists);
            }
        }catch (Exception e){

        }
    }




}
