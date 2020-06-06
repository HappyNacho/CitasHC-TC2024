package com.happynacho.citashc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterCitas extends RecyclerView.Adapter<AdapterCitas.ViewHolderCitas> {
    Context context;
    ArrayList<Cita> citas;

    public AdapterCitas(Context context,ArrayList<Cita> citas){
        this.context=context;
        this.citas=citas;
    }
    @NonNull
    @Override
    public ViewHolderCitas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_userlayout,parent,false);

        return new ViewHolderCitas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCitas holder, int position) {
        holder.asignarCitas(citas.get(position));
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    public class ViewHolderCitas extends RecyclerView.ViewHolder {
        TextView id,date,description;
        public ViewHolderCitas(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.textViewPID);
            date=itemView.findViewById(R.id.textViewFecha);
            description=itemView.findViewById(R.id.textViewDescrip);
        }

        public void asignarCitas(Cita cita) {
            id.setText(cita.getId());
            date.setText(cita.getDate());
            description.setText(cita.getDescription());
        }
    }
}
