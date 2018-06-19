/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.respaldo;

import beans.controladores.*;
import beans.modelos.PublicarEntradaBean;
import beans.respaldo.SessionUtilsBean;
import datos.dao.CiudadJpaController;
import datos.dao.DiaJpaController;
import datos.dao.EntradaJpaController;
import datos.dao.UsuarioJpaController;
import datos.dao.exceptions.NonexistentEntityException;
import datos.entidades.Ciudad;
import datos.entidades.Dia;
import datos.entidades.Entrada;
import datos.entidades.Usuario;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author rfmarquez
 */
@ManagedBean
@ViewScoped
public class BusquedaUsuario implements Serializable {

    @ManagedProperty(value = "#{sessionUtilsBean}")
    private SessionUtilsBean sessionUtilsBean;
    private final EntityManagerFactory emf;
    private List<Usuario> listaUsuarios;
    
    public BusquedaUsuario() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        listaUsuarios = new ArrayList<>();
    }

    public SessionUtilsBean getSessionUtilsBean() {
        return sessionUtilsBean;
    }

    public void setSessionUtilsBean(SessionUtilsBean sessionUtilsBean) {
        this.sessionUtilsBean = sessionUtilsBean;
    }
    
    public List<Usuario> autocompletadoCiudades(String query) {
        
        if (query.length() > 1) {
            UsuarioJpaController userController = new UsuarioJpaController(emf);
            return userController.dameListaUsuariosLikeNombre(query);
        }
        return this.listaUsuarios;
    }

}
