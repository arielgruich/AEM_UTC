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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Materia extends Activity {
TextView txt_id_materia,txt_docente_materia,txt_m;
EditText txt_nombre_materia,txt_alias_materia;
Spinner lista_docente_materia;
ListView lista_materia;
//BBDD
private SQLiteDatabase BBDD;
private static final String nombreBBDD="BB_TAREAS";
private static final String tablaMateria="create table if not exists tbl_materias(_id integer primary key autoincrement, nombre_materia text, alias_materia text,docente_materia text)";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_materia);
		txt_id_materia=(TextView)findViewById(R.id.txt_id_materia);
		txt_nombre_materia=(EditText)findViewById(R.id.txt_nombre_materia);
		txt_alias_materia=(EditText)findViewById(R.id.txt_alias_materia);
		txt_docente_materia=(TextView)findViewById(R.id.txt_abre_materia);		
		lista_docente_materia=(Spinner)findViewById(R.id.lista_docente_materia);
		lista_materia=(ListView)findViewById(R.id.lista_materia);
		crear();
		final ArrayList<String>fil2=new ArrayList<String>();
		fil2.add("Selecione Docente");
		final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,fil2);
		lista_docente_materia.setAdapter(adapter);
	}
	

////boton actualizar en vista para cargar Docente
	public void actualizar_materia_vista(View v){
		final ArrayList<String>fil=new ArrayList<String>();
		Cursor cursor=BBDD.rawQuery("select *from tbl_docentes;",null);
		if(cursor!=null){
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		
		final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,fil);
			lista_docente_materia.setAdapter(adapter);		
		lista_docente_materia.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() { 
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
        Cursor cu=BBDD.rawQuery("select nombre_docente from tbl_docentes;",null);
		cu.moveToPosition(i);
		txt_docente_materia.setHint(String.valueOf(cu.getString(0)));
           
             }
              // If no option selected
  public void onNothingSelected(AdapterView<?> arg0) {
   // TODO Auto-generated method stub
        
  } 

      });
	}
	private void crear() {
		//iniciar BBDD
		try {
			BBDD=openOrCreateDatabase(nombreBBDD, MODE_WORLD_WRITEABLE,null);
			BBDD.execSQL(tablaMateria);
		} catch (Exception e) {
				Log.i("ERROR","LA BASE DE DATOS NO FE CREADA CON EXITO"+e);
		}
		
	}
	

	//ingresar materia en BBDD
	public void ingresar_materia(View v){
		String nomm=txt_nombre_materia.getText().toString();
		String aliasm=txt_alias_materia.getText().toString();
		String docem=txt_docente_materia.getText().toString();		
		if(nomm.equals("")&&aliasm.equals("")&&docem.equals("")){
			Toast.makeText(getApplicationContext(),"Debe ingresar datos", Toast.LENGTH_SHORT).show();
		}
		else
		if(almacenar_materia_BBDD(nomm, aliasm, docem)){
			Toast.makeText(getApplicationContext(),"Registro de Materia exitosa",Toast.LENGTH_SHORT).show();
			limpiar_cajasdetextto_materia();
			buscar_materia_BBDD();
			
		}
		else{
			Toast.makeText(getApplicationContext(),"Error al Intentar Registrar Materia",Toast.LENGTH_SHORT).show();
		}
		
	}
	public boolean almacenar_materia_BBDD(String nombre_materia, String alias_materia, String docente_materia){
		ContentValues valores_materia=new ContentValues();
	 valores_materia.put("nombre_materia",nombre_materia);
	 valores_materia.put("alias_materia",alias_materia);
	 valores_materia.put("docente_materia",docente_materia);
	 return (BBDD.insert("tbl_materias",null,valores_materia)>0);	 
	}
	
	//consultar materias y almacenar en la lista
	final ArrayList<String>fil3=new ArrayList<String>();
	public void buscar_materia_BBDD(){
		fil3.clear();
		Cursor cursor=BBDD.rawQuery("select *from tbl_materias;",null);
		if(cursor!=null){
			cursor.moveToFirst();
			do {
				fil3.add(cursor.getString(1)+"\n"+"        "+cursor.getString(2)+"\n"+cursor.getString(3));
			} while (cursor.moveToNext());
		}
		final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,fil3);
		lista_materia.setAdapter(adapter);
		//click en una lista y almacenar en las cajas de texto
		lista_materia.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
				Cursor cu=BBDD.rawQuery("select * from tbl_materias;",null);
				cu.moveToPosition(position);
				txt_id_materia.setText(String.valueOf(cu.getLong(0)));
				txt_nombre_materia.setText(String.valueOf(cu.getString(1)));
				txt_alias_materia.setText(String.valueOf(cu.getString(2)));
				txt_docente_materia.setHint(String.valueOf(cu.getString(3)));
				
			}
		});
		}
	
	public void buscar_materia(View a){
		Toast.makeText(getApplicationContext(), "Lista completa de Materias", Toast.LENGTH_SHORT).show();
			buscar_materia_BBDD();
		}
	//edittar materia
public void editar_materia(View a){
		int id=Integer.parseInt(txt_id_materia.getText().toString());
		String mat=txt_nombre_materia.getText().toString();
		String ali=txt_alias_materia.getText().toString();
		String docm=txt_docente_materia.getHint().toString();
		if (actualizar_materia_BBDD(id, mat, ali, docm)){
			Toast.makeText(getApplicationContext(), "Datos de materia"+"Editado correctamente", Toast.LENGTH_SHORT).show();
			buscar_materia_BBDD();
			limpiar_cajasdetextto_materia();
		}else{
			Toast.makeText(getApplicationContext(), "Error al editar materia", Toast.LENGTH_SHORT).show();
		}
	}
	public boolean actualizar_materia_BBDD(int id,String n_materia,String a_materia, String d_materia){
		ContentValues valore_materia= new ContentValues();
		valore_materia.put("_id",id);
		valore_materia.put("nombre_materia",n_materia);
		valore_materia.put("alias_materia",a_materia);
		valore_materia.put("docente_materia",d_materia);
		return (BBDD.update("tbl_materias",valore_materia,"_id="+id,null)>0);
		
	}
	//eliminar materia de BBDD
	public void eliminar_materia(View a){
		int ide=Integer.parseInt(txt_id_materia.getText().toString());
		if (borrar_materia_BBDD(ide)) {
			Toast.makeText(getApplicationContext(), "Materia eliminado correctamente", Toast.LENGTH_SHORT).show();
			buscar_materia_BBDD();
			limpiar_cajasdetextto_materia();
		}
	}
	public boolean borrar_materia_BBDD(int id){
	return(BBDD.delete("tbl_materias", "_id="+id, null)>0);
	
	}
	public void limpiar_cajasdetextto_materia(){
		txt_id_materia.setText("");
		txt_nombre_materia.setText("");
		txt_alias_materia.setText("");
		txt_docente_materia.setHint("");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.materia, menu);
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
