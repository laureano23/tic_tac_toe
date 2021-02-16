package com.laureano.ta_te_ti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class JugadorListAdapter extends ArrayAdapter<Jugador> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;

    public JugadorListAdapter(Context context, int resource, ArrayList<Jugador> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // obtengo la informacion sobre el Jugador
        String nombre = getItem(position).getNombre();
        String dni = getItem(position).getDni();
        int victorias = getItem(position).getVictorias();
        String key = getItem(position).getKey();

        // Creamos el objeto Jugador
        Jugador jug = new Jugador(dni, nombre, victorias,key);

        //creamos la vista
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvNombre = (TextView) convertView.findViewById(R.id.textview_nombre_adapter);
        TextView tvDni = (TextView) convertView.findViewById(R.id.textview_dni_adapter);
        TextView tvVictorias = (TextView) convertView.findViewById(R.id.textview_victorias_adapter);

        tvNombre.setText("NOMBRE: " + nombre);
        tvDni.setText("DNI: " + dni);
        tvVictorias.setText(Integer.toString(victorias));

        return convertView;
    }
}
