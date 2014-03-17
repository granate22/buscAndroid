package com.clases;

public class Articulo
{
	String ID_Articulo="";
	String Nombre="";
	String Foto="";
	Double Precio=0.00;
	
	
	public Articulo()
	{
		super();
	}

	public Articulo(String iD_Articulo, String nombre, String foto,
			Double precio)
	{
		super();
		ID_Articulo = iD_Articulo;
		Nombre = nombre;
		Foto = foto;
		Precio = precio;
	}

	public String getID_Articulo()
	{
		return ID_Articulo;
	}

	public void setID_Articulo(String iD_Articulo)
	{
		ID_Articulo = iD_Articulo;
	}

	public String getNombre()
	{
		return Nombre;
	}

	public void setNombre(String nombre)
	{
		Nombre = nombre;
	}

	public String getFoto()
	{
		return Foto;
	}

	public void setFoto(String foto)
	{
		Foto = foto;
	}

	public Double getPrecio()
	{
		return Precio;
	}

	public void setPrecio(Double precio)
	{
		Precio = precio;
	}
	
	
	
}
