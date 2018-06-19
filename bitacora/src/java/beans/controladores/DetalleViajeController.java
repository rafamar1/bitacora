/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.DetalleViajeBean;
import beans.respaldo.Session;
import beans.respaldo.SessionUtilsBean;
import datos.dao.DiaJpaController;
import datos.dao.UsuarioJpaController;
import datos.dao.ViajeJpaController;
import datos.entidades.Dia;
import datos.entidades.Entrada;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
//import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author rfmarquez
 */
@ManagedBean
@ViewScoped
public class DetalleViajeController implements Serializable {

    @ManagedProperty(value = "#{sessionUtilsBean}")
    private SessionUtilsBean sessionUtilsBean;
    @ManagedProperty(value = "#{detalleViajeBean}")
    private DetalleViajeBean detalleViajeBean;
    private final EntityManagerFactory emf;
    private Viaje viaje;

    @PostConstruct
    public void initialize() {
        ViajeJpaController controlViaje = new ViajeJpaController(emf);
        this.viaje = controlViaje.findViaje(sessionUtilsBean.getIdViajeSeleccionado());
    }
    
    public DetalleViajeController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
    }

    public DetalleViajeBean getDetalleViajeBean() {
        return detalleViajeBean;
    }

    public void setDetalleViajeBean(DetalleViajeBean detalleViajeBean) {
        this.detalleViajeBean = detalleViajeBean;
    }

    public SessionUtilsBean getSessionUtilsBean() {
        return sessionUtilsBean;
    }

    public void setSessionUtilsBean(SessionUtilsBean sessionUtilsBean) {
        this.sessionUtilsBean = sessionUtilsBean;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }
    
    public String dameRutaImgPerfilUsuario(Usuario usuario) {
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(usuario.getNombreUsuario());
        sb.append("/");
        sb.append(usuario.getImgPerfil());
        return sb.toString();
    }
    
    public String dameRutaImgPortadaViaje(Viaje viaje){
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(viaje.getUsuario().getNombreUsuario());
        sb.append("/");
        sb.append(viaje.getId());
        sb.append("/");
        sb.append(viaje.getImgMiniatura());
        return sb.toString();
    }

    public String dameRutaEntrada(Entrada entrada){
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(entrada.getIdDia().getIdViaje().getUsuario().getNombreUsuario());
        sb.append("/");
        sb.append(entrada.getIdDia().getIdViaje().getId());
        sb.append("/");
        sb.append(entrada.getIdDia().getId());
        sb.append("/");
        sb.append(entrada.getImgMiniatura());
        return sb.toString();
    }
    
    public boolean viajeEsDeUsuarioLogeado(){
        return this.viaje.getUsuario().equals(sessionUtilsBean.getUsuario());
    }
    
    public String publicarNuevaEntrada(){
        int listaDiaSize = this.viaje.getDiaList().size();
        if(!this.viaje.getDiaList().isEmpty()){
            Integer ultimoDia = this.viaje.getDiaList().get(listaDiaSize - 1).getId();
            sessionUtilsBean.setIdDiaSeleccionado(ultimoDia);
            return "publicarEntrada";
        }
        return "errorIdDia";
    }
    
    public String addNuevoDia(){
        ViajeJpaController viajeController = new ViajeJpaController(emf);
        DiaJpaController diaController = new DiaJpaController(emf);
        Dia nuevoDia = new Dia();
        nuevoDia.setIdViaje(viajeController.findViaje(this.viaje.getId()));
        diaController.create(nuevoDia);
        sessionUtilsBean.setIdDiaSeleccionado(nuevoDia.getId());
        return "publicarEntrada";
    }
}
