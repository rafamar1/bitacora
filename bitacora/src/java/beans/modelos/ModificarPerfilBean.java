/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.modelos;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Rafa
 */
@ManagedBean
@RequestScoped
public class ModificarPerfilBean implements Serializable {

    private String nombreCompleto;
    private String email;
    private String password;
    private String passwordActual;
    private UploadedFile imagenPerfil;
    private UploadedFile imagenPortada;

    public ModificarPerfilBean() {
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordActual() {
        return passwordActual;
    }

    public void setPasswordActual(String passwordActual) {
        this.passwordActual = passwordActual;
    }

    public UploadedFile getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(UploadedFile imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public UploadedFile getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(UploadedFile imagenPortada) {
        this.imagenPortada = imagenPortada;
    }
    
}
