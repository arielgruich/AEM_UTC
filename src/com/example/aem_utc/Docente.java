package com.example.aem_utc;

import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Docente extends Activity {
	// declaracion de variables
TextView txt_id_docente;
EditText txt_nombre_docente,txt_email_docente;
ListView lista_docente;

//bas de datos
private SQLiteDatabase BBDD;
private static final String nombreBBDD="BB_TAREAS";
private static final String tablaDocente="create table if not exists tbl_docentes(_id integer primary key autoincrement, nombre_docente text, email_docente text)";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_docente);
		txt_id_docente=(TextView)findViewById(R.id.txt_id_docente);
		txt_nombre_docente=(EditText)findViewById(R.id.txt_nombre_docente);
		txt_email_docente=(EditText)findViewById(R.id.txt_email_docente);
		lista_docente=(ListView)findViewById(R.id.lista_docente);
		crear();
		
	}
	private void crear() {
		try {
			BBDD=openOrCreateDatabase(nombreBBDD, MODE_WORLD_WRITEABLE,null);
			BBDD.execSQL(tablaDocente);
		} catch (Exception e) {
				Log.i("ERROR","LA BASE DE DATOS NO FE CREADA CON EXITO"+e);
		}
		
	}
	//limpiar cajas de textos
	public void limpiar_cajas_texto(){
		txt_id_docente.setText("");
		txt_nombre_docente.setText("");
		txt_email_docente.setText("");
	}
	///ingresar docente en BBDD
	public void guardar_docente(View a){
		String nom=txt_nombre_docente.getText().toString();
		String ema=txt_email_docente.getText().toString();
		if(nom.equals("")&&ema.equals("")){			
			Toast.makeText(getApplicationContext(),"Ingrese Nombre del"+"\n"+"del Docente Porfavor",Toast.LENGTH_SHORT).show();
			//txt_id_docente.setBackgroundColor(Color.RED);
		}else
		if(almacenar_docente_BBDD(nom,ema)){
			Toast.makeText(getApplicationContext(),"Nuevo Docente Ingresado Exitosamente",Toast.LENGTH_SHORT).show();
			buscar_docente_BBDD();
			limpiar_cajas_texto();
		}else{
			Toast.makeText(getApplicationContext(),"Error al Intentar Registrar Docente",Toast.LENGTH_SHORT).show();
			buscar_docente_BBDD();
		}
	}
	public boolean almacenar_docente_BBDD( String nombre, String email){
		ContentValues valores_docente=new ContentValues();
		valores_docente.put("nombre_docente",nombre);
		valores_docente.put("email_docente",email);
		return (BBDD.insert("tbl_docentes", null, valores_docente)>0);
		
	}
	//consultar docente en BBDD y almacenar en una list
	final ArrayList<String>fil=new ArrayList<String>();
	public void buscar_docente_BBDD(){
		fil.clear();
		Cursor cursor=BBDD.rawQuery("select *from tbl_docentes;",null);
		if(cursor!=null){
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1)+"\n"+"        "+cursor.getString(2));
			} while (cursor.moveToNext());
		}
		final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,fil);
		lista_docente.setAdapter(adapter);
		//click en una lista y almacenar en las cajas de texto
		lista_docente.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select * from tbl_docentes;",null);
				cu.moveToPosition(position);
				txt_id_docente.setText(String.valueOf(cu.getLong(0)));
				txt_nombre_docente.setText(String.valueOf(cu.getString(1)));
				txt_email_docente.setText(String.valueOf(cu.getString(2)));
			}
		});
		}
	//buscar docente en vista
	public void buscar_docente(View a){
	buscar_docente_BBDD();
	Toast.makeText(getApplicationContext(), "Lista completa de docentes", Toast.LENGTH_SHORT).show();
		}
	
	// ediar las datos de docente	
	public void editar_docente(View a){
		int idd=Integer.parseInt(txt_id_docente.getText().toString());
		String nomd=txt_nombre_docente.getText().toString();
		String emad=txt_email_docente.getText().toString();
		if(nomd.equals("")&&emad.equals("")){
			Toast.makeText(getApplicationContext(), "No ha selecionado Docente", Toast.LENGTH_SHORT).show();
			buscar_docente_BBDD();
			limpiar_cajas_texto();
		}else
		if(actualizar_docente_BBDD(idd, nomd, emad)){
			Toast.makeText(getApplicationContext(), "Docente Actualizado correctamente", Toast.LENGTH_SHORT).show();
			buscar_docente_BBDD();
			limpiar_cajas_texto();
		}else{
			Toast.makeText(getApplicationContext(), "error al editar docente", Toast.LENGTH_SHORT).show();
			buscar_docente_BBDD();
		}
	}
	public boolean actualizar_docente_BBDD(int id_docente, String nombre_docente,String email_docente){
		ContentValues valores_docente=new ContentValues();
		valores_docente.put("_id",id_docente);
		valores_docente.put("nombre_docente",nombre_docente);
		valores_docente.put("email_docente",email_docente);
		return(BBDD.update("tbl_docentes", valores_docente,"_id="+id_docente,null)>0);
	}
	//eliminar datos de docente
	public void eliminar_docente(View a){
		int idd=Integer.parseInt(txt_id_docente.getText().toString());
		if(borrarr_docente_BBDD(idd)){
			Toast.makeText(getApplicationContext(), "Docente eliminado correctamente", Toast.LENGTH_SHORT).show();
			limpiar_cajas_texto();
			buscar_docente_BBDD();
		}else{
			Toast.makeText(getApplicationContext(), "error al eliminar docente", Toast.LENGTH_SHORT).show();
			
		}
	}
	public boolean borrarr_docente_BBDD(int id){
		return(BBDD.delete("tbl_docentes","_id="+id,null)>0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.docente, menu);
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
