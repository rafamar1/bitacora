/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import datos.dao.EntradaJpaController;
import datos.dao.ViajeJpaController;
import datos.entidades.Entrada;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
public class ExplorarController implements Serializable {
    
    private final EntityManagerFactory emf;
    private List<Viaje>  listaViajesAsia;
    private List<Viaje>  listaViajesEuropa;
    
    public ExplorarController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");        
        this.listaViajesAsia = dameListaViajesAsia();
        this.listaViajesEuropa = dameListaViajesEuropa();
    } 
           
    public List<Viaje> dameListaViajes(ArrayList<Integer> idPaises){
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
    
    private List<Viaje> dameListaViajesAsia() {
        Integer[] arrayIdAsia = new Integer[] { 1, 2 };
        return dameListaViajes(new ArrayList<>(Arrays.asList(arrayIdAsia)));
    }
    
    private List<Viaje> dameListaViajesEuropa() {
        Integer[] arrayIdEuropa = new Integer[] { 3, 4 };
        return dameListaViajes(new ArrayList<>(Arrays.asList(arrayIdEuropa)));
    }
    
    public String dameRutaViaje(Viaje viaje){
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(viaje.getUsuario().getNombreUsuario());
        sb.append("/");
        sb.append(viaje.getId());
        sb.append("/");
        sb.append(viaje.getImgMiniatura());
        return sb.toString();
    } 

    public List<Viaje> getListaViajesAsia() {
        return listaViajesAsia;
    }

    public void setListaViajesAsia(List<Viaje> listaViajesAsia) {
        this.listaViajesAsia = listaViajesAsia;
    }

    public List<Viaje> getListaViajesEuropa() {
        return listaViajesEuropa;
    }

    public void setListaViajesEuropa(List<Viaje> listaViajesEuropa) {
        this.listaViajesEuropa = listaViajesEuropa;
    }
    
}
