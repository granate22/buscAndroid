package com.clases;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.clases.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentListado extends Fragment
{
	private ListView lstListado;
	private ArticuloListener listener;
	private String url;
	public AdaptadorArticulos adapter;
	private Articulo[] datos = new Articulo[0];
	private int ArtsMostrados = 30;
	private ListadoCallbacks objCallbacks;
	private String ultimaURL="";

	public static interface ListadoCallbacks
	{
		void onPreExecute();

		void onProgressUpdate(int percent);

		void onCancelled();

		void onPostExecute();
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		objCallbacks = (ListadoCallbacks) activity;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		objCallbacks = null;
	}

	public interface ArticuloListener
	{
		void onArticuloSeleccionado(Articulo oArticulo);
	}

	public void setUrl(String busqueda)
	{
		busqueda = busqueda.replace(" ", "%20");
		this.url = "https://api.mercadolibre.com/sites/MLA/search?q="
				+ busqueda + "&limit=" + ArtsMostrados;
	}

	public void setArticuloListener(ArticuloListener listener)
	{
		this.listener = listener;
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
			if (!menu.findItem(R.id.action_buscar).isVisible())
				menu.findItem(R.id.action_buscar).setVisible(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		if (!this.url.equals("") && !this.url.equals(this.ultimaURL))
		{
			this.ultimaURL=this.url;
			(new Datos()).execute(this.url);
		}

		return inflater.inflate(R.layout.fragment_listado, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state)
	{
		super.onActivityCreated(state);

		lstListado = (ListView) getView().findViewById(R.id.LstListado);

		nuevoAdaptador();

		lstListado.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id)
			{
				if (listener != null)
				{
					listener.onArticuloSeleccionado((Articulo) lstListado
							.getAdapter().getItem(pos));
				}
			}
		});

	}

	public void nuevoAdaptador()
	{
		adapter = new AdaptadorArticulos(this);
		lstListado.setAdapter(adapter);
	}

	class AdaptadorArticulos extends ArrayAdapter<Articulo>
	{

		Activity context;

		AdaptadorArticulos(Fragment context)
		{
			super(context.getActivity(), R.layout.listitem_articulo, datos);
			this.context = context.getActivity();
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_articulo, null);

			TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
			lblTitulo.setText(datos[position].getNombre());

			TextView lblSubTitulo = (TextView) item
					.findViewById(R.id.LblSubTitulo);
			lblSubTitulo.setText("Precio: $"
					+ datos[position].getPrecio().toString());

			return (item);
		}

	}

	public class Datos extends AsyncTask<String, Integer, Articulo[]>
	{

		Articulo[] articulos = new Articulo[0];

		public Datos()
		{

		}

		@Override
		protected void onPostExecute(Articulo[] result)
		{
			super.onPostExecute(result);
			datos = result;
			nuevoAdaptador();
			if (objCallbacks != null)
			{
				objCallbacks.onPostExecute();
			}
		}

		@Override
		protected Articulo[] doInBackground(String... params)
		{

			String url = params[0];
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet del = new HttpGet(url);

			del.setHeader("content-type", "application/jsonp");
			try
			{
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONObject oJSON = new JSONObject(respStr);
				JSONArray respJSON = oJSON.getJSONArray("results");
				articulos = new Articulo[respJSON.length()];
				Articulo oArti;

				for (int i = 0; i < respJSON.length(); i++)
				{
					JSONObject obj = respJSON.getJSONObject(i);
					oArti = new Articulo();
					oArti.setID_Articulo(obj.getString("id"));
					oArti.setNombre(obj.getString("title"));
					if (!obj.get("price").equals(null))
						oArti.setPrecio(obj.getDouble("price"));

					if (!obj.get("thumbnail").equals(null))
						oArti.setFoto(obj.getString("thumbnail"));

					articulos[i] = oArti;

					publishProgress(i);
				}

			} catch (Exception ex)
			{
				ex.printStackTrace();
			}

			return articulos;
		}

		@Override
		protected void onPreExecute()
		{
			if (objCallbacks != null)
			{
				objCallbacks.onPreExecute();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... percent)
		{
			if (objCallbacks != null)
			{
				//int progreso = (int) percent[0].intValue() * 100	/ ArtsMostrados;
				objCallbacks.onProgressUpdate(percent[0].intValue());
			}
		}

		@Override
		protected void onCancelled()
		{
			if (objCallbacks != null)
			{
				objCallbacks.onCancelled();
			}
		}

	}
}
