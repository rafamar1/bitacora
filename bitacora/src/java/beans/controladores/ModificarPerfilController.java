/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.ModificarPerfilBean;
import beans.modelos.RegistroBean;
import beans.respaldo.SessionUtilsBean;
import datos.dao.UsuarioJpaController;
import datos.entidades.Usuario;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jasypt.util.password.*;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
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
@RequestScoped
public class ModificarPerfilController implements Serializable {

    @ManagedProperty(value = "#{sessionUtilsBean}")
    private SessionUtilsBean sessionUtilsBean;
    @ManagedProperty(value = "#{modificarPerfilBean}")
    private ModificarPerfilBean modificarPerfilBean;
    private final EntityManagerFactory emf;

    public ModificarPerfilController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
    }

    public ModificarPerfilBean getModificarPerfilBean() {
        return modificarPerfilBean;
    }

    public void setModificarPerfilBean(ModificarPerfilBean modificarPerfilBean) {
        this.modificarPerfilBean = modificarPerfilBean;
    }

    public SessionUtilsBean getSessionUtilsBean() {
        return sessionUtilsBean;
    }

    public void setSessionUtilsBean(SessionUtilsBean sessionUtilsBean) {
        this.sessionUtilsBean = sessionUtilsBean;
    }

    public String modificarUsuario() {
        Usuario usuario = setUsuario();

        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);

        try {
            usuarioController.edit(usuario);
            
        } catch (Exception ex) {
            Logger.getLogger(ModificarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }

        return "modificaOK";
    }

    private Usuario setUsuario() {
        Usuario usuario = sessionUtilsBean.getUsuario();
        if (modificarPerfilBean.getNombreCompleto() != null && !modificarPerfilBean.getNombreCompleto().equals("")) {
            usuario.setNombreCompleto(modificarPerfilBean.getNombreCompleto());
        }
        if (modificarPerfilBean.getPassword() != null && !modificarPerfilBean.getPassword().equals("")) {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            String passwordEncrypt = passwordEncryptor.encryptPassword(modificarPerfilBean.getPassword());
            usuario.setPassword(passwordEncrypt);
        }
        if (modificarPerfilBean.getEmail() != null && !modificarPerfilBean.getEmail().equals("")) {
            usuario.setEmail(modificarPerfilBean.getEmail());
        }
        if (modificarPerfilBean.getImagenPerfil()!= null && !modificarPerfilBean.getImagenPerfil().getFileName().equals("")) {
            usuario.setImgPerfil(sessionUtilsBean.getUsuario().getNombreUsuario()+".jpg");
            try {
                subirNuevaImagenPerfil();
            } catch (IOException ex) {
                Logger.getLogger(ModificarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (modificarPerfilBean.getImagenPortada()!= null && !modificarPerfilBean.getImagenPortada().getFileName().equals("")) {
            usuario.setImgPortada(sessionUtilsBean.getUsuario().getNombreUsuario()+"_portada.jpg");
            try {
                subirNuevaImagenPortada();
            } catch (IOException ex) {
                Logger.getLogger(ModificarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        //TODO REVISAR usuario.setPublico(1);
        return usuario;
    }
    
    public void subirNuevaImagenPerfil() throws IOException {
        UploadedFile uploadedPhoto = modificarPerfilBean.getImagenPerfil();
        byte[] bytes = null;

        if (null != uploadedPhoto) {
            String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String rutaUsuarios = rutaFaces.concat("resources/images/usuarios");
            if (sessionUtilsBean.getUsuario().getNombreUsuario() != null) {
                String nombreUsuario = sessionUtilsBean.getUsuario().getNombreUsuario();
                String rutaFichero = rutaUsuarios + "/" + nombreUsuario;
                File theFile = new File(rutaFichero);
                theFile.mkdirs(); //Creación de carpeta

                bytes = uploadedPhoto.getContents();

                FileOutputStream nuevoFichero = new FileOutputStream(rutaFichero + "/" + nombreUsuario + ".jpg");
                BufferedOutputStream stream = new BufferedOutputStream(nuevoFichero);

                stream.write(bytes);
                stream.close();
            }

        }

    }
    
    public void subirNuevaImagenPortada() throws IOException {
        UploadedFile uploadedPhoto = modificarPerfilBean.getImagenPortada();
        byte[] bytes = null;

        if (null != uploadedPhoto) {
            String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String rutaUsuarios = rutaFaces.concat("resources/images/usuarios");
            if (sessionUtilsBean.getUsuario().getNombreUsuario() != null) {
                String nombreUsuario = sessionUtilsBean.getUsuario().getNombreUsuario();
                String rutaFichero = rutaUsuarios + "/" + nombreUsuario;
                File theFile = new File(rutaFichero);
                theFile.mkdirs(); //Creación de carpeta

                bytes = uploadedPhoto.getContents();

                FileOutputStream nuevoFichero = new FileOutputStream(rutaFichero + "/" + nombreUsuario + "_portada.jpg");
                BufferedOutputStream stream = new BufferedOutputStream(nuevoFichero);

                stream.write(bytes);
                stream.close();
            }

        }

    }
    
}
