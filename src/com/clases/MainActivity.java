package com.clases;

import java.util.List;

import com.clases.FragmentListado.ArticuloListener;
import com.clases.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Configuration;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends Activity implements ArticuloListener, OnQueryTextListener, FragmentListado.ListadoCallbacks
{

	private SearchView oSearchView;
	FragmentListado oListado;
	FragmentDetalle oDetalle;
	ProgressDialog prDiag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		oListado = (FragmentListado) getFragmentManager().findFragmentByTag("fr_listado");
		
		if (oListado != null)
		{
			getFragmentManager().beginTransaction().replace(R.id.contenedor, oListado,"fr_listado").commit();
		}
		
		oDetalle = (FragmentDetalle) getFragmentManager().findFragmentByTag("fr_detalle");
		
		if (oDetalle != null)
		{
			getFragmentManager().beginTransaction().replace(R.id.contenedor, oDetalle, "fr_detalle").addToBackStack(null).commit();
		}
		
		prDiag= new ProgressDialog(this);
	}
	
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) 
	 {
	     switch (item.getItemId()) 
	     {
	         case android.R.id.home:
	        	 if (getFragmentManager().getBackStackEntryCount() == 0) 
	    	     {
	        		 //oListado.limpiarListView();
	    	     } 
	        	 else 
	    	     {
	    	         getFragmentManager().popBackStack();
	    	     }
	          return true;
	     }
	     return super.onOptionsItemSelected(item);
	 }
	
	 
	private void muestraFragmentListado(String busqueda)
	{	
		oListado = new FragmentListado();
		oListado.setUrl(busqueda);
		oListado.setArguments(getIntent().getExtras());
		oListado.setArticuloListener(this);

		getFragmentManager().beginTransaction().replace(R.id.contenedor, oListado).commit();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		oSearchView = (SearchView) menu.findItem(R.id.action_buscar).getActionView();
		oSearchView.setOnQueryTextListener(this);
		return true;
	}

	@Override
	public void onArticuloSeleccionado(Articulo oArticulo)
	{       
		oDetalle = (FragmentDetalle) getFragmentManager().findFragmentByTag("fr_detalle");
		if (oDetalle != null)
		{
			getFragmentManager().beginTransaction().replace(R.id.contenedor, oDetalle,"fr_detalle").commit();
		}
		else
		{
			oDetalle = new FragmentDetalle();

			getFragmentManager().beginTransaction().replace(R.id.contenedor, oDetalle, "fr_detalle").addToBackStack(null).commit();
		}
		
		
		getFragmentManager().executePendingTransactions();

		oDetalle.mostrarDetalle(oArticulo);

	}

	@Override
	public boolean onQueryTextChange(String arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String busqueda)
	{	
		muestraFragmentListado(busqueda);

		return false;
	}
	
	 @Override
	 public void onBackPressed() 
	 {
	     if (getFragmentManager().getBackStackEntryCount() == 0) 
	     {
	         super.onBackPressed();
	     } else 
	     {
	         getFragmentManager().popBackStack();
	     }
	 }


	@Override
	public void onPreExecute()
	{
		prDiag.setTitle("Buscando artículos");
		prDiag.setMessage("Cargando...");
		//prDiag.setProgress(0);
		prDiag.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		prDiag.show();
		
	}


	@Override
	public void onProgressUpdate(int percent)
	{
		prDiag.setProgress(percent);		
	}


	@Override
	public void onCancelled()
	{
		prDiag.hide();
	}


	@Override
	public void onPostExecute()
	{
		prDiag.hide();		
	}


		
	

}
