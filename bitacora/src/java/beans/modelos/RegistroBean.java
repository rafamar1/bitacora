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
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.*;


/**
 *
 * @author Rafa
 */
//@Named
@ManagedBean
@RequestScoped
public class RegistroBean implements Serializable{

//   @ManagedProperty(value = "#{usuario}")
    private Usuario usuario;
    private String name;
    //private Conversation conversation;
    
    public RegistroBean() {
        usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String loginOk(){
        return "registrOK";
    }
    
}
