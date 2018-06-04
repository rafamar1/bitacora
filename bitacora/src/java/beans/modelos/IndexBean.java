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

@ManagedBean
@RequestScoped
public class IndexBean implements Serializable {

    private String busqueda;
    
    public IndexBean() {
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

}
