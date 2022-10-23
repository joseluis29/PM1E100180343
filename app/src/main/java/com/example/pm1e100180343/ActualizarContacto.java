package com.example.pm1e100180343;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm1e100180343.Configuracion.Operaciones;
import com.example.pm1e100180343.Configuracion.SQLiteConexion;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActualizarContacto extends AppCompatActivity {

    SQLiteConexion conexion = new SQLiteConexion(this, Operaciones.NameDatabase,null,1);

    Button btnatras,btnEditar;
    EditText codigo, nombrecompleto, telefono, nota;
    Spinner codigoPais;
    ArrayList<String> lista_paises;
    ArrayList<Pais> lista;

    int codigoPaisSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);
        btnatras = (Button) findViewById(R.id.acbtnatras);
        codigo = (EditText)findViewById(R.id.txtActCodigo);
        nombrecompleto = (EditText)findViewById(R.id.txtActNombreCompleto);
        telefono = (EditText)findViewById(R.id.txtActTelefono);
        nota = (EditText)findViewById(R.id.txtActNota);
        codigoPais = (Spinner)findViewById(R.id.cmbActSeleccionarPais);
        btnEditar = (Button) findViewById(R.id.acbtnEdit);


        codigo.setText(getIntent().getStringExtra("codigo"));
        nombrecompleto.setText(getIntent().getStringExtra("nombreContacto"));
        telefono.setText(getIntent().getStringExtra("numeroContacto"));
        nota.setText(getIntent().getStringExtra("notaContacto"));

        ObtenerListaPaises();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item,lista_paises);
        codigoPais.setAdapter(adp);

        codigoPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String cadena = adapterView.getSelectedItem().toString();

                //Quitar los caracteres del combobox para obtener solo el codigo del pais
                codigoPaisSeleccionado = Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ListadoContacto.class);
                startActivity(intent);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarContacto();
            }
        });
    }

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }


    private void ObtenerListaPaises() {
        Pais pais = null;
        lista = new ArrayList<Pais>();
        SQLiteDatabase db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Operaciones.tblPaises,null);

        while (cursor.moveToNext())
        {
            pais = new Pais();

            pais.setCodigo(cursor.getString(0));
            pais.setNombrePais(cursor.getString(1));

            lista.add(pais);
        }

        cursor.close();

        fillCombo();

    }

    private void fillCombo() {
        lista_paises = new ArrayList<String>();

        for (int i=0; i<lista.size();i++)
        {
            lista_paises.add(lista.get(i).getNombrePais()+" ( "+lista.get(i).getCodigo()+" )");
        }
    }

    private void EditarContacto() {

        SQLiteDatabase db = conexion.getWritableDatabase();

        String ObjCodigo = codigo.getText().toString();

        ContentValues valores = new ContentValues();

        valores.put(Operaciones.nombreCompleto, nombrecompleto.getText().toString());
        valores.put(Operaciones.telefono, telefono.getText().toString());
        valores.put(Operaciones.nota, nota.getText().toString());
        valores.put(Operaciones.pais, codigoPaisSeleccionado);

        try {
            db.update(Operaciones.tablacontactos,valores, Operaciones.id +" = "+ ObjCodigo, null);
            db.close();
            Toast.makeText(getApplicationContext(),"Se actualizo correctamente", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(this, ListadoContacto.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"No se actualizo", Toast.LENGTH_SHORT).show();
        }

    }
}