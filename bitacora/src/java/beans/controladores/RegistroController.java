/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.RegistroBean;
import beans.respaldo.EnviarMail;
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

@ManagedBean
@RequestScoped
public class RegistroController implements Serializable {

    @ManagedProperty(value = "#{registroBean}")
    private RegistroBean registroBean;
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
            return "error";
        }
        
        EnviarMail sendEmail = new EnviarMail();
        sendEmail.setUsuario(usuario.getNombreUsuario());
        sendEmail.setBody(usuario.getNombreCompleto()+", bienvenido a bitacora.\n+"
                + "Ahora puede compartir experiencias con miles de usuarios.\n"
                + "Sus datos son:\n"
                + "usuario: "+usuario.getNombreUsuario()
                + "\nmail: " + usuario.getEmail());
        return "registroOk";
    }

    
    private Usuario setUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(registroBean.getNombreCompleto());
        usuario.setNombreUsuario(registroBean.getNombreUsuario());
        usuario.setEmail(registroBean.getEmail());
        usuario.setImgPerfil("sin-imagen.jpg"); 
        usuario.setImgPortada("sin-imagen-portada.jpg"); 
        usuario.setPublico(1); //Siempre se inicializa un usuario como público
        return usuario;
    }
}
