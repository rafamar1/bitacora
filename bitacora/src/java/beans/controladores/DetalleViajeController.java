/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.DetalleViajeBean;
import beans.respaldo.Session;
import datos.dao.ViajeJpaController;
import datos.entidades.Viaje;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
//import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author rfmarquez
 */
@ManagedBean
@RequestScoped
public class DetalleViajeController implements Serializable {

    @ManagedProperty(value = "#{detalleViajeBean}")
    private DetalleViajeBean detalleViajeBean;
    private final EntityManagerFactory emf;
    private Viaje viaje;

    public DetalleViajeController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        this.viaje = cargarViaje();
    }

    public DetalleViajeBean getDetalleViajeBean() {
        return detalleViajeBean;
    }

    public void setDetalleViajeBean(DetalleViajeBean detalleViajeBean) {
        this.detalleViajeBean = detalleViajeBean;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }
    
    private Viaje cargarViaje(){
        ViajeJpaController controlViaje = new ViajeJpaController(emf);
        
        return controlViaje.findViaje((int)Session.getInstance().getAttribute("idViajeSeleccionado"));
    }
    
    
    
}
