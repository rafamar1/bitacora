/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.modelos;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author Rafa
 */

@ManagedBean
@RequestScoped
public class PublicarEntradaBean implements Serializable{


    private String titulo;
    private String descripcion;
    private UploadedFile imagen;
    private String ciudad;
    private String tipo;
    private Integer precio;
    private Integer puntuacion;
    
    
    public PublicarEntradaBean() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }
    
    
}
