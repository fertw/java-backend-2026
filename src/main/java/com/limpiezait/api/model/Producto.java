package com.limpiezait.api.model;

public class Producto {

	private Long id;
	private String nombre;
	private Double precio;
	private String descripcion;
	private String urlFoto;

	public Producto() {
	}

	public Producto(Long id, String nombre, Double precio, String descripcion, String urlFoto) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.descripcion = descripcion;
		this.urlFoto = urlFoto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

}
