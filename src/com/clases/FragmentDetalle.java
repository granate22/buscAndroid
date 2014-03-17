package com.clases;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.clases.R;
import com.clases.FragmentListado.ListadoCallbacks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentDetalle extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_detalle, container, false);
	}
	
	@Override
	  public void onCreate(Bundle savedInstanceState) 
	 {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);
	 }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		if (menu != null)
		{
			menu.findItem(R.id.action_buscar).setVisible(false);
		}
	}

	public void mostrarDetalle(Articulo oArticulo)
	{
		TextView txtNombre = (TextView) getView().findViewById(R.id.TxtNombre);
		txtNombre.setText(oArticulo.getNombre());

		TextView txtPrecio = (TextView) getView().findViewById(R.id.TxtPrecio);
		txtPrecio.setText("Precio: $" + oArticulo.getPrecio().toString());

		ImageView imgImagen = (ImageView) getView().findViewById(R.id.imagen);

		mostrarImagen(imgImagen, oArticulo.getFoto());
	}

	public void mostrarImagen(ImageView mImgView1, String url)
	{

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		Bitmap bm = loadBitmap(url, bmOptions);
		mImgView1.setImageBitmap(bm);
	}

	public static Bitmap loadBitmap(String URL, BitmapFactory.Options options)
	{
		Bitmap bitmap = null;
		InputStream in = null;
		try
		{
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e1)
		{
		}
		return bitmap;
	}

	private static InputStream OpenHttpConnection(String strURL)
			throws IOException
	{
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try
		{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex)
		{
		}
		return inputStream;
	}

	

}
