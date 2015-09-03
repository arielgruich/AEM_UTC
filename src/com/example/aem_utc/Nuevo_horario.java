package com.example.aem_utc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Nuevo_horario extends Activity {
	private SQLiteDatabase BBDD;
	private static final String nombreBBDD="BB_TAREAS";
	private static final String tablaHorario="create table if not exists tbl_horarios(_id integer primary key autoincrement,  hora text, lunes text,martes text,miercoles text,jueves text,viernes text)";
	Spinner spiner_lunes,spiner_martes,spiner_miercoles,spiner_jueves,spiner_viernes;
	TextView  txt_lunes,txt_martes,txt_miercoles,txt_jueves,txt_viernes,txt_id;
	EditText txt_hora;
	ListView lista_horario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nuevo_horario);
		spiner_lunes=(Spinner)findViewById(R.id.spiner_lunes);
		spiner_martes=(Spinner)findViewById(R.id.spiner_martes);
		spiner_miercoles=(Spinner)findViewById(R.id.spiner_miercoles);
		spiner_jueves=(Spinner)findViewById(R.id.spiner_jueves);
		spiner_viernes=(Spinner)findViewById(R.id.spiner_viernes);
		lista_horario=(ListView)findViewById(R.id.lista_horario);
		txt_lunes=(TextView)findViewById(R.id.lunes);
		txt_martes=(TextView)findViewById(R.id.martes);
		txt_miercoles=(TextView)findViewById(R.id.miercoles);
		txt_jueves=(TextView)findViewById(R.id.jueves);
		txt_viernes=(TextView)findViewById(R.id.viernes);
		txt_hora=(EditText)findViewById(R.id.hora);
		txt_id=(TextView)findViewById(R.id.id);
		
	
		
		 final ArrayList<String>fil2=new ArrayList<String>();
			fil2.add("Selecione Materias");
			final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,fil2);
			spiner_lunes.setAdapter(adapter);
			spiner_martes.setAdapter(adapter);
			spiner_miercoles.setAdapter(adapter);
			spiner_jueves.setAdapter(adapter);
			spiner_viernes.setAdapter(adapter);
			
		 crear();
	}
	//almacenar datos
			public void ingresar_horario(View v){
				String hor=txt_hora.getText().toString();				
				String lun=txt_lunes.getText().toString();
				String mar=txt_martes.getText().toString();
				String mier=txt_miercoles.getText().toString();
				String jue=txt_jueves.getText().toString();
				String vie=txt_viernes.getText().toString();
				if(hor.equals("")){
					Toast.makeText(getApplicationContext(), "Debe Ingresar Datos", Toast.LENGTH_SHORT).show();
				}else
				
				if(ingresar_horario_BBDD(hor,lun,mar,mier,jue,vie)){
					Toast.makeText(getApplicationContext(), "Horario ingresada correctamente ", Toast.LENGTH_SHORT).show();
					impiar_cajas();
					buscar_horario_BBDD();
				}else{
					Toast.makeText(getApplicationContext(), "Error al ingresar Horario", Toast.LENGTH_SHORT).show();
					
				}
				
				}
			public boolean ingresar_horario_BBDD(String h,String l, String m, String mi,String j,String v){
				ContentValues valores_horario=new ContentValues();
				valores_horario.put("hora", h);
				valores_horario.put("lunes",l);
				valores_horario.put("martes", m);
				valores_horario.put("miercoles",mi);
				valores_horario.put("jueves",j);
				valores_horario.put("viernes",v);
				
				return (BBDD.insert("tbl_horarios",null,valores_horario)>0);
			}
////consultar datos
			final ArrayList<String>fil=new ArrayList<String>();
			public void buscar_horario_BBDD(){
				fil.clear();
				Cursor cursor=BBDD.rawQuery("select *from tbl_horarios;",null);
				if(cursor!=null){
					cursor.moveToFirst();
					do {
						fil.add("Hora: "+cursor.getString(1));
					} while (cursor.moveToNext());
				}
				final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,fil);
				lista_horario.setAdapter(adapter);
				//click en una lista y almacenar en las cajas de texto
				lista_horario.setOnItemClickListener(new OnItemClickListener() {
			
					@Override
					public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
						// TODO Auto-generated method stub
						Cursor cu=BBDD.rawQuery("select * from tbl_horarios;",null);
						cu.moveToPosition(position);
						txt_id.setVisibility(View.INVISIBLE);
						txt_id.setText(String.valueOf(cu.getLong(+0)));
						txt_hora.setText(String.valueOf(cu.getLong(+1)));
						txt_lunes.setText(String.valueOf(cu.getString(2)));
						txt_martes.setText(String.valueOf(cu.getString(3)));
						txt_miercoles.setText(String.valueOf(cu.getString(4)));
						txt_jueves.setText(String.valueOf(cu.getString(5)));
						txt_viernes.setText(String.valueOf(cu.getString(6)));
						
						
						
					}
				});
				}
			public void buscar_horario(View v){
				Toast.makeText(getApplicationContext(), "Listado de Horario", Toast.LENGTH_SHORT).show();
					buscar_horario_BBDD();
					//limpiar_cajas();
					//Intent lt=new Intent(this,Listado_tarea.class);
					//startActivity(lt);
				
			}
			public void editar_horario(View v){
				int idh=Integer.parseInt(txt_id.getText().toString());
				String hor=txt_hora.getText().toString();				
				String lun=txt_lunes.getText().toString();
				String mar=txt_martes.getText().toString();
				String mier=txt_miercoles.getText().toString();
				String jue=txt_jueves.getText().toString();
				String vie=txt_viernes.getText().toString();
				
				if(editar_HorarioBBDD(idh, hor, lun, mar, mier, jue,vie)){
					Toast.makeText(getApplicationContext(), "Se edito correctamente el Horario", Toast.LENGTH_SHORT).show();
					impiar_cajas();
					buscar_horario_BBDD();
				}
				else{
					Toast.makeText(getApplicationContext(), "Error al intentar editar Horario", Toast.LENGTH_SHORT).show();
				}
				
				}
				public boolean editar_HorarioBBDD(int id,String h,String  l,String  m,String  mi,String  j,String v){
					ContentValues valore_horario= new ContentValues();
					valore_horario.put("_id",id);
					valore_horario.put("hora",h);
					valore_horario.put("lunes",l);
					valore_horario.put("martes",m);
					valore_horario.put("miercoles",mi);
					valore_horario.put("jueves",j);
					valore_horario.put("viernes",v);
					
					return (BBDD.update("tbl_horarios",valore_horario,"_id="+id,null)>0);
				}
///vaciar cajas
			public void impiar_cajas(){
				txt_id.setText("");
				txt_hora.setText("");				
			txt_lunes.setText("");
				txt_martes.setText("");
				txt_miercoles.setText("");
				txt_jueves.setText("");
				txt_viernes.setText("");
			}
			
	public void lunes_btn(View v){
		final ArrayList<String>fil=new ArrayList<String>();
		 
		 Cursor cursor=BBDD.rawQuery("select *from tbl_materias;",null);
		 if (cursor!=null) {
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		 final ArrayAdapter<String>adapter =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,fil);
		 spiner_lunes.setAdapter(adapter);
		 
		spiner_lunes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select alias_materia from tbl_materias;",null);
				cu.moveToPosition(position);
				txt_lunes.setText(String.valueOf(cu.getString(0)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			});
	}
	//martes
	public void martes_btn(View v){
		final ArrayList<String>fil=new ArrayList<String>();
		 
		 Cursor cursor=BBDD.rawQuery("select *from tbl_materias;",null);
		 if (cursor!=null) {
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		 final ArrayAdapter<String>adapter =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,fil);
		 spiner_martes.setAdapter(adapter);
		 
		spiner_martes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select alias_materia from tbl_materias;",null);
				cu.moveToPosition(position);
				txt_martes.setText(String.valueOf(cu.getString(0)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			});
	}
	//miercoles
	public void miercoles_btn(View v){
		final ArrayList<String>fil=new ArrayList<String>();
		 
		 Cursor cursor=BBDD.rawQuery("select *from tbl_materias;",null);
		 if (cursor!=null) {
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		 final ArrayAdapter<String>adapter =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,fil);
		 spiner_miercoles.setAdapter(adapter);
		 
		spiner_miercoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select alias_materia from tbl_materias;",null);
				cu.moveToPosition(position);
				txt_miercoles.setText(String.valueOf(cu.getString(0)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			});
	}
	//jueves
	public void jueves_btn(View v){
		final ArrayList<String>fil=new ArrayList<String>();
		 
		 Cursor cursor=BBDD.rawQuery("select *from tbl_materias;",null);
		 if (cursor!=null) {
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		 final ArrayAdapter<String>adapter =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,fil);
		 spiner_jueves.setAdapter(adapter);
		 
		spiner_jueves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select alias_materia from tbl_materias;",null);
				cu.moveToPosition(position);
				txt_jueves.setText(String.valueOf(cu.getString(0)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			});
	}
	//viernes
	public void viernes_btn(View v){
		final ArrayList<String>fil=new ArrayList<String>();
		 
		 Cursor cursor=BBDD.rawQuery("select *from tbl_materias;",null);
		 if (cursor!=null) {
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		 final ArrayAdapter<String>adapter =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,fil);
		 spiner_viernes.setAdapter(adapter);
		 
		spiner_viernes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select alias_materia from tbl_materias;",null);
				cu.moveToPosition(position);
				txt_viernes.setText(String.valueOf(cu.getString(0)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			});
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
		getMenuInflater().inflate(R.menu.nuevo_hoarario, menu);
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
