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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hometodo.Activities.AgregarExamen;
import com.hometodo.Adapters.ExamenesAdapter;
import com.hometodo.Adapters.InicioAdapter;
import com.hometodo.Adapters.TareasAdapter;
import com.hometodo.Models.Course;
import com.hometodo.Models.Exam;
import com.hometodo.Models.ExamenModel;
import com.hometodo.Models.InicioModel;
import com.hometodo.R;

import java.util.ArrayList;
import java.util.List;

import static com.hometodo.BDD.HomeworkContract.HomeworkEntry.COLUMN_DATE_DELIVERY;


public class Examenes extends Fragment {

    // UI references and main declarations
    private List<ExamenModel> examenList = new ArrayList<>(); //it's a list of objects based in LogModel attributes
    private RecyclerView rv;
    private ExamenesAdapter mAdapter; //this is the adapter that will manage de recycle view


    FloatingActionButton fab;

    public Examenes() {

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Exámenes");

        //this declares the recycle view
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mAdapter);

        try {
            List <Exam>myList = new Exam.QueryBuilder(getContext()).all();
            List <Course> materias=new Course.QueryBuilder(getContext()).all();

            int index=0;
            //fill the recycleview with info, going through the object model
            for(int i=0; i<myList.size(); i++){
                ExamenModel lists=new ExamenModel();

                lists.setNombre(myList.get(i).getTitle());
                lists.setDescripcion(myList.get(i).getDescription());//
                lists.setFecha(myList.get(i).getDateDelivery());

                for (int j=0; j<materias.size();j++){
                    if(materias.get(j).getId()==myList.get(i).getCourseId()){
                        lists.setMateria(materias.get(j).getCourseName());
                        index=j;
                    }
                }
                lists.setColor(materias.get(index).getColor());
                examenList.add(lists);
            }
        }catch (Exception e){
            Log.e("Error", "Type Error " + e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Please note the third parameter should be false, otherwise a java.lang.IllegalStateException maybe thrown.
        View retView = inflater.inflate(R.layout.examenes, container, false);
        fab = (FloatingActionButton) retView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AgregarExamen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        mAdapter = new ExamenesAdapter(examenList, getContext()); //the adapter will be charged by the model list
        rv = retView.findViewById(R.id.rv);
        return retView;
    }



}
