package com.example.aem_utc;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Tarea extends Activity  implements OnClickListener, android.view.View.OnClickListener{
	private ImageButton btn_fecha_tarea;
	 private Calendar cal;
	 private int day;
	 private int month;
	 private int year;
	 //variabls de activity
	TextView  txt_fecha_tarea,txt_nombre_materia,txt_id_tarea;
	EditText txt_titulo_tarea,txt_descripcion_tarea;
	Spinner spiner_materia;
	ListView lista_tarea_l;
	CheckBox recordatorio;
	// BBDD
	private SQLiteDatabase BBDD;
	private static final String nombreBBDD="BB_TAREAS";
	private static final String tablaTarea="create table if not exists tbl_tareas(_id integer primary key autoincrement, titulo_tarea text, descripcion_tarea text,fecha_tarea text,docente_tarea text,recordatorio text)";

	// alerta
	private static final int ALERTA_ID=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tarea);
		btn_fecha_tarea = (ImageButton) findViewById(R.id.bt_calendar);
		  cal = Calendar.getInstance();
		  day = cal.get(Calendar.DAY_OF_MONTH);
		  month = cal.get(Calendar.MONTH);
		  year = cal.get(Calendar.YEAR);
		  txt_id_tarea=(TextView)findViewById(R.id.txt_id_tarea);
		 txt_titulo_tarea=(EditText)findViewById(R.id.txt_titulo_tarea);
		 txt_descripcion_tarea=(EditText)findViewById(R.id.txt_descripcion_tarea);
		 txt_fecha_tarea = (TextView) findViewById(R.id.txt_fecha_tarea);
		 txt_nombre_materia=(TextView)findViewById(R.id.txt_materia_tareas);
		 recordatorio=(CheckBox)findViewById(R.id.recordatorio);
		 spiner_materia=(Spinner)findViewById(R.id.lista_docente_tarea);
		 lista_tarea_l=(ListView)findViewById(R.id.lista_tarea);
		 btn_fecha_tarea.setOnClickListener(this);
		 final ArrayList<String>fil2=new ArrayList<String>();
			fil2.add("Selecione Materia");
			final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,fil2);
			spiner_materia.setAdapter(adapter);
		 crear();
		
	}
public void Actualizar_tarea_vista(View v){
 final ArrayList<String>fil=new ArrayList<String>();
		 
		 Cursor cursor=BBDD.rawQuery("select *from tbl_materias;",null);
		 if (cursor!=null) {
			cursor.moveToFirst();
			do {
				fil.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		 final ArrayAdapter<String>adapter =new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,fil);
		 spiner_materia.setAdapter(adapter);
		 
		spiner_materia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Cursor cu=BBDD.rawQuery("select alias_materia from tbl_materias;",null);
				cu.moveToPosition(position);
				txt_nombre_materia.setText(String.valueOf(cu.getString(0)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			});
	}
	

		private void crear() {
			try {
				BBDD=openOrCreateDatabase(nombreBBDD, MODE_WORLD_WRITEABLE,null);
				BBDD.execSQL(tablaTarea);
				
				
			} catch (Exception e) {
				Log.i("ERROR","LA BASE DE DATOS NO FUE CREADO CON EXITO"+e);
			}
		
	}
		///ingresar tareas en BBDD
		public void ingresar_tarea(View v){
			String tit=txt_titulo_tarea.getText().toString();
			String desc=txt_descripcion_tarea.getText().toString();
			String fect=txt_fecha_tarea.getText().toString();
			String matd=txt_nombre_materia.getText().toString();
			String rec=recordatorio.getText().toString();
			if(ingresar_tarea_BBDD(tit, desc, fect, matd, rec)){
				Toast.makeText(getApplicationContext(), "Tarea ingresada correctamente ", Toast.LENGTH_SHORT).show();
					
			}else{
				Toast.makeText(getApplicationContext(), "Error al ingresar materia", Toast.LENGTH_SHORT).show();
				
			}
			
			}
		public boolean ingresar_tarea_BBDD(String titulo_tarea,String descripcion_tarea, String fecha_tarea, String docente_tarea,String recorad){
			ContentValues valores_tarea=new ContentValues();
			valores_tarea.put("titulo_tarea", titulo_tarea);
			valores_tarea.put("descripcion_tarea",descripcion_tarea);
			valores_tarea.put("fecha_tarea", fecha_tarea);
			valores_tarea.put("docente_tarea",docente_tarea);
			return (BBDD.insert("tbl_tareas",null, valores_tarea)>0);
		}
		
		///buscar tarea
		final ArrayList<String>fil=new ArrayList<String>();
		public void buscar_tarea_BBDD(){
			fil.clear();
			Cursor cursor=BBDD.rawQuery("select *from tbl_tareas;",null);
			if(cursor!=null){
				cursor.moveToFirst();
				do {
					fil.add(cursor.getString(1)+"\n"+"        materia: "+cursor.getString(4)+"\n"+"           fecha: "+cursor.getString(3));
				} while (cursor.moveToNext());
			}
			final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,fil);
			lista_tarea_l.setAdapter(adapter);
			//click en una lista y almacenar en las cajas de texto
			lista_tarea_l.setOnItemClickListener(new OnItemClickListener() {
		
				@Override
				public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
					// TODO Auto-generated method stub
					Cursor cu=BBDD.rawQuery("select * from tbl_tareas;",null);
					cu.moveToPosition(position);
					txt_id_tarea.setText(String.valueOf(cu.getLong(+0)));
					txt_titulo_tarea.setText(String.valueOf(cu.getString(1)));
					txt_descripcion_tarea.setText(String.valueOf(cu.getString(2)));
					txt_fecha_tarea.setText(String.valueOf(cu.getString(3)));
					txt_nombre_materia.setText(String.valueOf(cu.getString(4)));
					
					
				}
			});
			}
		public void buscar_tarea(View v){
		buscar_tarea_BBDD();
			
		}
		///editar tarea
		public void editar_tarea(View v){
		int idt=Integer.parseInt(txt_id_tarea.getText().toString());
		String titt=txt_titulo_tarea.getText().toString();
		String dest=txt_descripcion_tarea.getText().toString();
		String fect=txt_fecha_tarea.getText().toString();
		String doct=txt_nombre_materia.getText().toString();
		String rect=recordatorio.getText().toString();
		if(editar_tareaBBDD(idt, titt, dest, fect, doct, rect)){
			Toast.makeText(getApplicationContext(), "Se edito correctamente La tarea", Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(getApplicationContext(), "Error al intentar editar tarea adios", Toast.LENGTH_SHORT).show();
		}
		
		}
		public boolean editar_tareaBBDD(int id,String tit,String  des,String  fece,String  docem,String  rec){
			ContentValues valore_tarea= new ContentValues();
			valore_tarea.put("_id",id);
			valore_tarea.put("titulo_tarea",tit);
			valore_tarea.put("descripcion_tarea",des);
			valore_tarea.put("fecha_tarea",fece);
			valore_tarea.put("docente_tarea",docem);
			valore_tarea.put("recordatorio",rec);		
			return (BBDD.update("tbl_tareas",valore_tarea,"_id="+id,null)>0);
		}
		
		public void eliminar_tarea(View v){
			int idt=Integer.parseInt(txt_id_tarea.getText().toString());
			if (eliminar_tareaBBDD(idt)){
				Toast.makeText(getApplicationContext(), "tarea eliminada correctamente ", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "cerror al intentar eliminar tarea ", Toast.LENGTH_SHORT).show();
			}
			
		}
		public boolean eliminar_tareaBBDD(int id){
			return(BBDD.delete("tbl_tareas", "_id="+id, null)>0);
		}
		//notificar tarea creada
		
		public void notificar(View v){
			NotificationCompat.Builder mBuilder=	new NotificationCompat.Builder(this)
			.setSmallIcon(android.R.drawable.stat_sys_warning)
			.setLargeIcon(((BitmapDrawable)getResources()
					.getDrawable(R.drawable.ic_launcher)).getBitmap())
					.setContentTitle(txt_titulo_tarea.getText().toString())
					.setContentText(txt_descripcion_tarea.getText().toString())
					.setContentInfo("Fecha de entrega"+txt_fecha_tarea.getText().toString())
					.setTicker("Recordatorio ...!!!!!");
					Intent notificacionINtent=new Intent(this,Tarea.class);
					PendingIntent conINtent=
							PendingIntent.getActivity(Tarea.this,0,notificacionINtent,0);
					mBuilder.setContentIntent(conINtent);
				NotificationManager mnm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
				mnm.notify(ALERTA_ID,mBuilder.build());
		}



		 @Override
		 @Deprecated
		 protected Dialog onCreateDialog(int id) {
		  return new DatePickerDialog(this, datePickerListener, year, month, day);
		 }

		 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		  public void onDateSet(DatePicker view, int selectedYear,
		    int selectedMonth, int selectedDay) {
			  //enviar fecha a texviex
		   txt_fecha_tarea.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
		  }
		 };
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tarea, menu);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 showDialog(0);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
}
