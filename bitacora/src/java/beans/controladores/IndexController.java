/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import datos.dao.ViajeJpaController;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.faces.bean.ManagedBean;
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

    private final EntityManagerFactory emf;
    private boolean logeado = false;

    public IndexController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
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
