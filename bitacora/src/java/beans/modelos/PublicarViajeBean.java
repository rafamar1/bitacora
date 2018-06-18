/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.modelos;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author Rafa
 */

@ManagedBean
@RequestScoped
public class PublicarViajeBean implements Serializable{
    
    @Size(min=3)
    private String titulo;
    
    @Size(min=3, max=180)
    private String descripcion;
    
    @NotNull
    private UploadedFile imagen;
    
    
    public PublicarViajeBean() {
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

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
    }
    
}
