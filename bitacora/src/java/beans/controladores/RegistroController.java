/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.RegistroBean;
import datos.dao.UsuarioJpaController;
import datos.entidades.Usuario;
import javax.inject.*;
import org.jasypt.util.password.*;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
//import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author rfmarquez
 */
//@Named(value = "registroController")
@ManagedBean

@RequestScoped
public class RegistroController implements Serializable {

    @ManagedProperty(value = "#{registroBean}")
    private RegistroBean registroBean;

    //@PersistenceUnit(unitName = "bitacoraPU")
    private final EntityManagerFactory emf;

    public RegistroController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
    }

    public RegistroBean getRegistroBean() {
        return registroBean;
    }

    public void setRegistroBean(RegistroBean registroBean) {
        this.registroBean = registroBean;
    }

    public String altaUsuario() {
        Usuario usuario = setUsuario();
        
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String passwordEncrypt = passwordEncryptor.encryptPassword(registroBean.getPassword());
        usuario.setPassword(passwordEncrypt);
        
        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);

        try {
            usuarioController.create(usuario);
        } catch (Exception ex) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
            return "error.xhtml";
        }

        return "index.xhtml";
    }

    
    private Usuario setUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(registroBean.getNombreCompleto());
        usuario.setNombreUsuario(registroBean.getNombreUsuario());
        usuario.setEmail(registroBean.getEmail());
        usuario.setPublico(1); //Siempre se inicializa un usuario como público
        return usuario;
    }
}
