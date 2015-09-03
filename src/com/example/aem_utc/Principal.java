package com.example.aem_utc;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Principal extends TabActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		TabHost tabhost=(TabHost)findViewById(android.R.id.tabhost);
		//menu tarea
		TabSpec tab_tarea=tabhost.newTabSpec("menu tarea");
		tab_tarea.setIndicator("Tarea");
		 tab_tarea.setContent(new Intent(this,Tarea.class));
		 tabhost.addTab(tab_tarea);
		 //materia

			TabSpec tab_materia=tabhost.newTabSpec("menu materia");
			tab_materia.setIndicator("Materia");
			
			 tab_materia.setContent(new Intent(this,Materia.class));
			 tabhost.addTab(tab_materia);
			 //menu docente

				TabSpec tab_docente=tabhost.newTabSpec("menu docente");
				tab_docente.setIndicator("Docente");
				 tab_docente.setContent(new Intent(this,Docente.class));
				 tabhost.addTab(tab_docente);
				 //menu horario

					TabSpec tab_horario=tabhost.newTabSpec("menu horario");
					tab_horario.setIndicator("Horario");
					 tab_horario.setContent(new Intent(this,Horario.class));
					 tabhost.addTab(tab_horario);

			
		 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
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
