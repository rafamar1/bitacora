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
import datos.entidades.Dia;
import datos.entidades.Entrada;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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

    public String dameRutaImgPerfil(Usuario usuario) {
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(usuario.getNombreUsuario());
        sb.append("/");
        sb.append(usuario.getImgPerfil());
        return sb.toString();
    }

    public String dameRutaImgViaje(Viaje viaje) {
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(viaje.getUsuario().getNombreUsuario());
        sb.append("/");
        sb.append(viaje.getId());
        sb.append("/");
        sb.append(viaje.getImgMiniatura());
        return sb.toString();
    }

    public String dameRutaImgPortada(Usuario usuario) {
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(usuario.getNombreUsuario());
        sb.append("/");
        sb.append(usuario.getImgPortada());
        return sb.toString();
    }

    public List<Viaje> damelistaViajesUsuario() {
        //Usuario usuarioSeleccionado = (Usuario) Session.getInstance().getAttribute("usuario");
        Usuario userSeleccionado = dameUsuarioSeleccionadoPorId();
        return userSeleccionado.getViajeList();

    }
    
    public String dameCiudadesVisitadas(Viaje viaje) {

        List<Dia> listaDias = viaje.getDiaList();
        HashSet<String> hashSetNombresCiudades = new HashSet<>();
        for (Dia dia : listaDias) {
            for (Entrada entrada : dia.getEntradaList()) {
                hashSetNombresCiudades.add(entrada.getIdCiudad().getNombre());
            }
        }
        ArrayList<String> listaNombresCiudades = new ArrayList(hashSetNombresCiudades);
        int numMaximoCiudades = 3;
        StringBuilder sb = new StringBuilder();
        if (listaNombresCiudades.size() > 0) {
            sb.append(listaNombresCiudades.get(0));
            for (int i = 1; i < listaNombresCiudades.size(); i++) {
                if (listaNombresCiudades.get(i) != null && i < numMaximoCiudades) {
                    sb.append(" | ");
                    sb.append(listaNombresCiudades.get(i));
                }
            }
        }
        return sb.toString();
    }

    public Usuario dameUsuarioSeleccionadoPorId() {
        UsuarioJpaController controlUser = new UsuarioJpaController(emf);
        if (controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado()) != null) {
            return controlUser.findUsuario(sessionUtilsBean.getIdUsuarioSeleccionado());
        } else {
            return controlUser.findUsuario(((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario());
        }
    }

    public boolean usuarioSeleccionadoNoEsUsuarioLogeado() {
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
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        "Has comenzado a seguir a "+userToFollowUnfollow.getNombreUsuario()));

            } else {
                /*PARA DEJAR DE SEGUIR*/

                if (listUserToFollowUnfollow.contains(userLogeado)) {
                    listUserToFollowUnfollow.remove(userLogeado);
                }
                controlUser.edit(userToFollowUnfollow);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        "Has dejado de seguir a "+userToFollowUnfollow.getNombreUsuario()));
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
