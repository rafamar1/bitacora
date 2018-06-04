/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.IndexBean;
import datos.dao.ViajeJpaController;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author rfmarquez
 */

@ManagedBean
@RequestScoped
public class IndexController implements Serializable {
    
    @ManagedProperty(value = "#{indexBean}")
    private IndexBean indexBean;
    private final EntityManagerFactory emf;
    
    public IndexController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
    }    

    public IndexBean getIndexBean() {
        return indexBean;
    }

    public void setIndexBean(IndexBean indexBean) {
        this.indexBean = indexBean;
    }
    
    public List<Viaje> dameListaViajes(){
        HashSet<Viaje> hashSetViajes = new HashSet<>();
        
        ViajeJpaController controlViaje = new ViajeJpaController(emf);
        int numViajes = controlViaje.getViajeCount();
        int viajesAMostrar = 2;
        
        while (hashSetViajes.size()< viajesAMostrar){
            int idViajeRandom = (new Random()).nextInt(numViajes) + 1;
            hashSetViajes.add(controlViaje.findViaje(idViajeRandom));
        }        
        
        return new ArrayList(hashSetViajes);
    }

}
