package com.example.aem_utc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Listado_materias extends Activity {
 ListView listado_tareas;
 TextView  txt_fecha_tarea,txt_nombre_materia,txt_id_tarea;
	EditText txt_titulo_tarea,txt_descripcion_tarea;
 private SQLiteDatabase BBDD;
 private static final String nombreBBDD="BB_TAREAS";
	private static final String tablaTarea="create table if not exists tbl_tareas(_id integer primary key autoincrement, titulo_tarea text, descripcion_tarea text,fecha_tarea text,docente_tarea text,recordatorio text)";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado_tareas);		
		
			 crear();
	}
	private void crear() {
		try {
			BBDD=openOrCreateDatabase(nombreBBDD, MODE_WORLD_WRITEABLE,null);
			BBDD.execSQL(tablaTarea);
			
			
		} catch (Exception e) {
			Log.i("ERROR","LA BASE DE DATOS NO FUE CREADO CON EXITO"+e);
		}
	
}
	final ArrayList<String>fil=new ArrayList<String>();
	public void regresar_menu_m(View v){
		//Intent n=new Intent(this, Principal.class);
		//startActivity(n);
		fil.clear();
		Cursor cursor=BBDD.rawQuery("select *from tbl_tareas;",null);
		if(cursor!=null){
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1)+"\n"+"        materia: "+cursor.getString(4)+"\n"+"           fecha: "+cursor.getString(3));
			} while (cursor.moveToNext());
		}
		final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,fil);
		listado_tareas.setAdapter(adapter);
		//click en una lista y almacenar en las cajas de texto
		listado_tareas.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select * from tbl_tareas;",null);
				cu.moveToPosition(position);
				
				txt_id_tarea.setText(String.valueOf(cu.getLong(0)));
				txt_titulo_tarea.setText(String.valueOf(cu.getString(1)));
				txt_descripcion_tarea.setText(String.valueOf(cu.getString(2)));
				txt_fecha_tarea.setText(String.valueOf(cu.getString(3)));
				txt_nombre_materia.setText(String.valueOf(cu.getString(4)));
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listado_materias, menu);
		return true;
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
