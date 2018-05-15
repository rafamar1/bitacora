/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.modelos;

import datos.entidades.Usuario;
import java.io.Serializable;
//import javax.annotation.ManagedBean;
//import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.*;


/**
 *
 * @author Rafa
 */
//@Named(value = "registroBean")
@ManagedBean
@RequestScoped
public class RegistroBean implements Serializable{

//   @ManagedProperty(value = "#{usuario}")
    //private Usuario usuario;
    private String nombreUsuario;
    private String nombreCompleto;
    private String email;
    private String password;
    //private Conversation conversation;
    
    public RegistroBean() {
        //usuario = new Usuario();
    }

    /*public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String loginOk(){
        return "registrOK";
    }*/

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
