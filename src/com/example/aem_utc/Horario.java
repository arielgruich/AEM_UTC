package com.example.aem_utc;

import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Horario extends Activity {
	private SQLiteDatabase BBDD;
	private static final String nombreBBDD="BB_TAREAS";
	//private static final String tablaTarea="create table if not exists tbl_tareas(_id integer primary key autoincrement, titulo_tarea text, descripcion_tarea text,fecha_tarea text,docente_tarea text,recordatorio text)";
	private static final String tablaHorario="create table if not exists tbl_horarios(_id integer primary key autoincrement,  hora text, lunes text,martes text,miercoles text,jueves text,viernes text)";
	ListView listado_hor;
	TextView textoe;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_horario);
		listado_hor=(ListView)findViewById(R.id.listado_hor);
		textoe=(TextView)findViewById(R.id.texto);
		 crear();
	}
	public void ver_horario(View v){
		 
		 final ArrayList<String>element=new ArrayList<String>();		 
		element.clear();
		listado_hor.setTextFilterEnabled(true);
			Cursor cursor=BBDD.rawQuery("select *from tbl_horarios;",null);
			if(cursor!=null){
				cursor.moveToFirst();
				do {
				element.add("Hora: "+cursor.getString(1)+"\n"+" L: "+cursor.getString(2)+" M: "+cursor.getString(3)+" Mie: "+cursor.getString(4)+" J: "+cursor.getString(5)+" V: "+cursor.getString(6)+"\n");
					
				} while (cursor.moveToNext());
			}
			final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_selectable_list_item,element);
		
			listado_hor.setAdapter((adapter));
	
			   
	}


	private void crear() {
		// TODO Auto-generated method stub
		try {
			BBDD=openOrCreateDatabase(nombreBBDD, MODE_WORLD_WRITEABLE,null);
			BBDD.execSQL(tablaHorario);
			
			
		} catch (Exception e) {
			Log.i("ERROR","LA BASE DE DATOS NO FUE CREADO CON EXITO"+e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.horarrio, menu);
		return true;
	}
public void nuevo_horario(View v){
	Intent n_h=new Intent(this,Nuevo_horario.class);
	startActivity(n_h);
}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
