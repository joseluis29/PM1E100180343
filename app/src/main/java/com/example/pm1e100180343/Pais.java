package com.example.pm1e100180343;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm1e100180343.Configuracion.Operaciones;
import com.example.pm1e100180343.Configuracion.SQLiteConexion;


public class Pais extends AppCompatActivity {

    EditText aptxtCodigo,aptxtPais;
    Button apbtnGuardar, aptnAtras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);

        aptxtCodigo = (EditText) findViewById(R.id.aptxtCodigo);
        aptxtPais = (EditText) findViewById(R.id.aptxtPais);
        apbtnGuardar = (Button) findViewById(R.id.apbtnGuardar);
        aptnAtras = (Button) findViewById(R.id.apbtnAtras);

        apbtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertarPais();
            }
        });

        aptnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void InsertarPais() {
        SQLiteConexion conexion = new SQLiteConexion(this, Operaciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Operaciones.codigo,aptxtCodigo.getText().toString());
        valores.put(Operaciones.p_pais,aptxtPais.getText().toString());

        Long resultado = db.insert(Operaciones.tblPaises,Operaciones.codigo,valores);

        Toast.makeText(getApplicationContext(),"Registro Exitoso!!!"+resultado.toString(),Toast.LENGTH_LONG).show();
        db.close();

        limpiarPantalla();

    }

    private void limpiarPantalla() {
        aptxtPais.setText("");
        aptxtCodigo.setText("");
    }
    private String codigo;
    private String nombrePais;

    public Pais() {
    }

    public Pais(String codigo, String nombrePais) {
        this.codigo = codigo;
        this.nombrePais = nombrePais;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

}