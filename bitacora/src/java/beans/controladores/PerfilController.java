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
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
    //TODO habria se puede eliminar el usuarioSeleccionado? o setearlo en el initialize
    @ManagedProperty(value = "#{sessionUtilsBean}")
    private SessionUtilsBean sessionUtilsBean;
    private final EntityManagerFactory emf;
    private Usuario usuarioSeleccionado;
    private boolean followUnfollow;

    @PostConstruct
    public void initialize() {
        UsuarioJpaController controlUser = new UsuarioJpaController(emf);
        Usuario userToFollowUnfollow = controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
        this.usuarioSeleccionado = controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
        Usuario userLogeado = sessionUtilsBean.getUsuario();
        this.followUnfollow = esSeguidor(userToFollowUnfollow, userLogeado);
    }

    public PerfilController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
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

    public boolean isFollowUnfollow() {
        return followUnfollow;
    }

    public void setFollowUnfollow(boolean followUnfollow) {
        this.followUnfollow = followUnfollow;
    }

    public List<Viaje> damelistaViajesUsuario() {
        //Usuario usuarioSeleccionado = (Usuario) Session.getInstance().getAttribute("usuario");
        Usuario userSeleccionado = dameUsuarioSeleccionadoPorId();
        return userSeleccionado.getViajeList();

    }

    public Usuario dameUsuarioSeleccionadoPorId() {
        UsuarioJpaController controlUser = new UsuarioJpaController(emf);
        if (controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado()) != null) {
            return controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
        } else {
            return controlUser.findUsuario(((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario());
        }
    }
    
    public boolean usuarioSeleccionadoNoEsUsuarioLogeado(){
        return !sessionUtilsBean.getUsuario().getNombreUsuario().equals(sessionUtilsBean.getIdUsuarioSeleccionado());
    }

    public void toFollowUnfollow() {
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario userToFollowUnfollow = controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
            Usuario userLogeado = sessionUtilsBean.getUsuario();

            boolean isFollower = esSeguidor(userToFollowUnfollow, userLogeado);
            List<Usuario> listUserToFollowUnfollow = userToFollowUnfollow.getListaUsuarioTeSigue();
            if (!isFollower) {
                /*/TODO REVISAR ESE CONTAINS
                if (!listUserToFollowUnfollow.contains(userLogeado)) {
                    listUserToFollowUnfollow.add(userLogeado)
                }*/
                userToFollowUnfollow.getListaUsuarioTeSigue().add(userLogeado);
                controlUser.edit(userToFollowUnfollow);

            } else {
                /*PARA DEJAR DE SEGUIR*/
                
                if (listUserToFollowUnfollow.contains(userLogeado)) {
                    listUserToFollowUnfollow.remove(userLogeado);
                }
                controlUser.edit(userToFollowUnfollow);
            }

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean esSeguidor(Usuario userToFollowUnfollow, Usuario userLogeado) {
        return userToFollowUnfollow.getListaUsuarioTeSigue().contains(userLogeado);
    }
    
    public String followKendrick() {
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

    public String unfollowKendrick() {
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");

            /*List<Usuario> listaLitro = rafamar.getListaUsuarioSeguido();
            if(listaLitro.contains(kendrick)){
                listaLitro.remove(kendrick);
            }*/
            List<Usuario> listaKendrick = kendrick.getListaUsuarioTeSigue();
            if (listaKendrick.contains(rafamar)) {
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
