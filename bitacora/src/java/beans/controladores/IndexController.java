/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.IndexBean;
import datos.dao.EntradaJpaController;
import datos.dao.ViajeJpaController;
import datos.entidades.Entrada;
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
    private List<Entrada> listaEntradasRandom;

    public IndexController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        this.listaEntradasRandom = dameListaEntradas(6);
    }

    public IndexBean getIndexBean() {
        return indexBean;
    }

    public void setIndexBean(IndexBean indexBean) {
        this.indexBean = indexBean;
    }

    public List<Entrada> getListaEntradasRandom() {
        return listaEntradasRandom;
    }

    public void setListaEntradasRandom(List<Entrada> listaEntradasRandom) {
        this.listaEntradasRandom = listaEntradasRandom;
    }

    public List<Viaje> dameListaViajes() {
        HashSet<Viaje> hashSetViajes = new HashSet<>();

        ViajeJpaController controlViaje = new ViajeJpaController(emf);
        int numViajes = controlViaje.getViajeCount();
        int viajesAMostrar = 2;

        while (hashSetViajes.size() < viajesAMostrar) {
            int idViajeRandom = (new Random()).nextInt(numViajes) + 1;
            hashSetViajes.add(controlViaje.findViaje(idViajeRandom));
        }

        return new ArrayList(hashSetViajes);
    }

    private List<Entrada> dameListaEntradas(int entradasAMostrar) {
        HashSet<Entrada> hashSetEntradas = new HashSet<>();

        EntradaJpaController controlEntrada = new EntradaJpaController(emf);
        int numEntradas = controlEntrada.getEntradaCount();

        while (hashSetEntradas.size() < entradasAMostrar) {
            int idEntradaRandom = (new Random()).nextInt(numEntradas) + 1;
            if (controlEntrada.findEntrada(idEntradaRandom) != null) {
                hashSetEntradas.add(controlEntrada.findEntrada(idEntradaRandom));
            }
        }

        return new ArrayList(hashSetEntradas);
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

}
