/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.respaldo;

import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

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
    
    private Integer idCiudadSeleccionada;
    
    
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
    
    public String cierraSesionYRedirigeLogin(){
        setUsuarioLogeado(false);
        Session.getInstance().currentExternalContext().invalidateSession();
        return "logout";
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
}
