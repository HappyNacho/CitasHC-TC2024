package com.happynacho.citashc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CitasFragment extends Fragment {
    AdapterCitas adapter;
    RecyclerView recycler;
    ArrayList<Cita> citas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_citas, container, false);
        citas = new ArrayList<>();

        recycler = vista.findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        llenarlista();
        adapter = new AdapterCitas(getContext(),citas);
        recycler.setAdapter(adapter);

        return vista;

    }
    public void llenarlista(){
        citas = ((MenuSelection)getActivity()).getDB().getAllCitas();
    }
}
