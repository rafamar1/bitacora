/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.respaldo.Session;
import beans.respaldo.SessionUtilsBean;
import datos.dao.UsuarioJpaController;
import datos.dao.exceptions.NonexistentEntityException;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author rfmarquez
 */

@ManagedBean
@ViewScoped
public class PerfilController implements Serializable {
    
    @ManagedProperty(value = "#{sessionUtilsBean}")
    private SessionUtilsBean sessionUtilsBean;
    private final EntityManagerFactory emf;
    private Usuario usuarioSeleccionado;
    
    public PerfilController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        usuarioSeleccionado = dameUsuarioSeleccionadoPorId();
    }    

    public SessionUtilsBean getSessionUtilsBean() {
        return sessionUtilsBean;
    }

    public void setSessionUtilsBean(SessionUtilsBean sessionUtilsBean) {
        this.sessionUtilsBean = sessionUtilsBean;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }
    
    public List<Viaje> damelistaViajesUsuario(){
        UsuarioJpaController controlUser= new UsuarioJpaController(emf);
        //Usuario usuarioSeleccionado = controlUser.findUsuario((String)Session.getInstance().getAttribute("idUsuarioSeleccionado"));
        Usuario usuarioSeleccionado = (Usuario)Session.getInstance().getAttribute("usuario");
        
        return usuarioSeleccionado.getViajeList();
        
    }
    
    private Usuario dameUsuarioSeleccionadoPorId() {
        UsuarioJpaController controlUser = new UsuarioJpaController(emf);
        if(controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado()) != null){
            return controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
        } else {
           return controlUser.findUsuario(((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario());
        }
    }
    
    public String followUnfollow(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario userToFollow = controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
            Usuario userToUnfollow = controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
            Usuario userLogeado = sessionUtilsBean.getUsuario();
           
            //kendrick.getListaUsuarioSeguido().add(usuarioQueSigue);
            userToFollow.getListaUsuarioTeSigue().add(userLogeado);
            
            //controlUser.edit(usuarioSeguido);
            controlUser.edit(userToFollow);
            
            /*PARA DEJAR DE SEGUIR*/
            List<Usuario> listUserToUnfollow = userToUnfollow.getListaUsuarioTeSigue();
            if(listUserToUnfollow.contains(userLogeado)){
                listUserToUnfollow.remove(userLogeado);
            }
            return "followOK";
            
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }
    
    public String followLitro(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");
           
            //kendrick.getListaUsuarioSeguido().add(rafamar);
            rafamar.getListaUsuarioTeSigue().add(kendrick);
            
            //controlUser.edit(kendrick);
            controlUser.edit(rafamar);
            
            return "followOK";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }

    public String followKendrick(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");
           
            //rafamar.getListaUsuarioSeguido().add(kendrick);
            kendrick.getListaUsuarioTeSigue().add(rafamar);
            
            //controlUser.edit(rafamar);
            controlUser.edit(kendrick);
            
            return "followOK";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }
    
    public String unfollowLitro(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");
            
            /*List<Usuario> listaKendrick = kendrick.getListaUsuarioSeguido();
            if(listaKendrick.contains(rafamar)){
                listaKendrick.remove(rafamar);
            }*/
            
            List<Usuario> listaLitro = rafamar.getListaUsuarioTeSigue();
            if(listaLitro.contains(kendrick)){
                listaLitro.remove(kendrick);
            }
                            
            controlUser.edit(rafamar);
            
            return "followOK";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }
    
    public String unfollowKendrick(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");
           
            /*List<Usuario> listaLitro = rafamar.getListaUsuarioSeguido();
            if(listaLitro.contains(kendrick)){
                listaLitro.remove(kendrick);
            }*/
            
            List<Usuario> listaKendrick = kendrick.getListaUsuarioTeSigue();
            if(listaKendrick.contains(rafamar)){
                listaKendrick.remove(rafamar);
            }
            
            controlUser.edit(kendrick);
            
            
            
            return "followOK";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }
    

}
