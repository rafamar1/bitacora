/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.respaldo;

import datos.dao.UsuarioJpaController;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author rfmarquez
 */
@ManagedBean(name = "sessionUtilsBean")
@SessionScoped
public class SessionUtilsBean implements Serializable {

    private boolean usuarioLogeado;
    private Usuario usuario;
    private Integer idViajeSeleccionado;
    private Integer idDiaSeleccionado;
    private Integer idEntradaSeleccionada;
    private String idUsuarioSeleccionado;
    private String usuarioBusqueda;
    private Integer idCiudadSeleccionada;

    public String getUsuarioBusqueda() {
        return usuarioBusqueda;
    }

    public void setUsuarioBusqueda(String usuarioBusqueda) {
        this.usuarioBusqueda = usuarioBusqueda;
    }

    public SessionUtilsBean() {
    }

    public boolean isUsuarioLogeado() {
        return usuarioLogeado;
    }

    public void setUsuarioLogeado(boolean usuarioLogeado) {
        this.usuarioLogeado = usuarioLogeado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getIdViajeSeleccionado() {
        return idViajeSeleccionado;
    }

    public void setIdViajeSeleccionado(Integer idViajeSeleccionado) {
        this.idViajeSeleccionado = idViajeSeleccionado;
    }

    public Integer getIdDiaSeleccionado() {
        return idDiaSeleccionado;
    }

    public void setIdDiaSeleccionado(Integer idDiaSeleccionado) {
        this.idDiaSeleccionado = idDiaSeleccionado;
    }

    public Integer getIdEntradaSeleccionada() {
        return idEntradaSeleccionada;
    }

    public void setIdEntradaSeleccionada(Integer idEntradaSeleccionada) {
        this.idEntradaSeleccionada = idEntradaSeleccionada;
    }

    public String getIdUsuarioSeleccionado() {
        return idUsuarioSeleccionado;
    }

    public void setIdUsuarioSeleccionado(String idUsuarioSeleccionado) {
        this.idUsuarioSeleccionado = idUsuarioSeleccionado;
    }

    public Integer getIdCiudadSeleccionada() {
        return idCiudadSeleccionada;
    }

    public void setIdCiudadSeleccionada(Integer idCiudadSeleccionada) {
        this.idCiudadSeleccionada = idCiudadSeleccionada;
    }

    public String cierraSesionYRedirigeLogin() {
        setUsuarioLogeado(false);
        Session.getInstance().currentExternalContext().invalidateSession();
        return "logout";
    }

    public String dameRutaImgPerfil(Usuario usuario) {
        if (usuario.getImgPerfil().equals("sin-imagen.jpg")) {
            return "resources/images/comun/sin-imagen.jpg";
        } else {
            StringBuilder sb = new StringBuilder("resources/images/usuarios/");
            sb.append(usuario.getNombreUsuario());
            sb.append("/");
            sb.append(usuario.getImgPerfil());
            return sb.toString();
        }
    }

    public String dameRutaImgPortada(Usuario usuario) {
        if (usuario.getImgPortada().equals("sin-imagen-portada.jpg")) {
            return "resources/images/comun/sin-imagen-portada.jpg";
        } else {
            StringBuilder sb = new StringBuilder("resources/images/usuarios/");
            sb.append(usuario.getNombreUsuario());
            sb.append("/");
            sb.append(usuario.getImgPortada());
            return sb.toString();
        }
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

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "logout";
    }

    public List<Usuario> buscarUsuario(String query) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bitacoraPU");

        if (query.length() > 1) {
            UsuarioJpaController userController = new UsuarioJpaController(emf);
            return userController.dameListaUsuariosLikeNombre(query);
        }
        return new ArrayList<>();
    }
}
