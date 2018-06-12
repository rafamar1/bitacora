/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.IndexBean;
import datos.dao.UsuarioJpaController;
import datos.dao.ViajeJpaController;
import datos.dao.exceptions.NonexistentEntityException;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            /*TODO revisar para usuarios publicos solamente
            if(controlViaje.findViaje(idViajeRandom).getUsuario().getPublico()?1:false){
                
            }*/
            hashSetViajes.add(controlViaje.findViaje(idViajeRandom));
        }        
        
        return new ArrayList(hashSetViajes);
    }
    
    public String followLitro(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");
           
            //kendrick.getListaUsuarioSeguido().add(rafamar);
            rafamar.getListaUsuarioTeSigue().add(kendrick);
            
            //controlUser.edit(kendrick);
            controlUser.edit(rafamar);
            
            return "followOK";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }
    
    public String followKendrick(){
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
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }
    
    public String unfollowLitro(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");
            
            /*List<Usuario> listaKendrick = kendrick.getListaUsuarioSeguido();
            if(listaKendrick.contains(rafamar)){
                listaKendrick.remove(rafamar);
            }*/
            
            List<Usuario> listaLitro = rafamar.getListaUsuarioTeSigue();
            if(listaLitro.contains(kendrick)){
                listaLitro.remove(kendrick);
            }
                            
            controlUser.edit(rafamar);
            
            return "followOK";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }
    
    public String unfollowKendrick(){
        try {
            UsuarioJpaController controlUser = new UsuarioJpaController(emf);
            Usuario rafamar = controlUser.findUsuario("rafamar");
            Usuario kendrick = controlUser.findUsuario("kendrick");
           
            /*List<Usuario> listaLitro = rafamar.getListaUsuarioSeguido();
            if(listaLitro.contains(kendrick)){
                listaLitro.remove(kendrick);
            }*/
            
            List<Usuario> listaKendrick = kendrick.getListaUsuarioTeSigue();
            if(listaKendrick.contains(rafamar)){
                listaKendrick.remove(rafamar);
            }
            
            controlUser.edit(kendrick);
            
            
            
            return "followOK";
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        } catch (Exception ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        
    }


}
