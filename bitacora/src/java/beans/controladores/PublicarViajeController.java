/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.PublicarViajeBean;
import beans.respaldo.Session;
import datos.dao.CiudadJpaController;
import datos.dao.PaisJpaController;
import datos.dao.ViajeJpaController;
import datos.entidades.Ciudad;
import datos.entidades.Pais;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author rfmarquez
 */
@ManagedBean
@RequestScoped
public class PublicarViajeController implements Serializable {

    @ManagedProperty(value = "#{publicarViajeBean}")
    private PublicarViajeBean publicarViajeBean;
    /*@ManagedProperty(value = "#{detalleViajeController}")
    private DetalleViajeController detalleViajeController;*/
    private final EntityManagerFactory emf;
    private List<Ciudad> listaCiudades;
    private String cadena;

    public PublicarViajeController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        listaCiudades = new ArrayList<>();
    }

    public PublicarViajeBean getPublicarViajeBean() {
        return publicarViajeBean;
    }

    public void setPublicarViajeBean(PublicarViajeBean publicarViajeBean) {
        this.publicarViajeBean = publicarViajeBean;
    }

    /*public DetalleViajeController getDetalleViajeController() {
        return detalleViajeController;
    }

    public void setDetalleViajeController(DetalleViajeController detalleViajeController) {
        this.detalleViajeController = detalleViajeController;
    }*/
    
    public List<Ciudad> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudad> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public List<Pais> dameListaPaises() {
        PaisJpaController paisController = new PaisJpaController(emf);
        /*Pais pais = paisController.findPaisEntities().get(5);
        int idPais = paisController.dameIDdadoNombrePais(pais.getNombre());*/
        return paisController.findPaisEntities();
    }

    public void cambioPais(ValueChangeEvent event) {
        String cadenaPais = event.getNewValue().toString();
        if (cadenaPais != null && cadenaPais.length() > 3) {
            PaisJpaController paisController = new PaisJpaController(emf);
            CiudadJpaController ciudadController = new CiudadJpaController(emf);
            int idPais = paisController.dameIDdadoNombrePais(cadenaPais);
            List<Ciudad> listaCiudades = ciudadController.dameListaCiudadesDadoIdPais(idPais);
            this.listaCiudades = listaCiudades;
        }
    }

    public String publicar() {
        Viaje newViaje = new Viaje();
        try {
            subirFoto();
        } catch (IOException ex) {
            Logger.getLogger(PublicarViajeController.class.getName()).log(Level.SEVERE, null, ex);
            return "error"; //TODO revisar control de excepciones
        }
        newViaje.setTitulo(publicarViajeBean.getTitulo());
        newViaje.setDescripcion(publicarViajeBean.getDescripcion());
        //TODO COGER USUARIO DE LA SESION
        newViaje.setUsuario(((Usuario)Session.getInstance().getAttribute("usuario")));
        //newViaje.setUsuario(new Usuario("kendrick"));
        newViaje.setImgMiniatura(publicarViajeBean.getImagen().getFileName());
        //TODO gestionar la imagen tanto las individuales como las de la galería
        ViajeJpaController viajeController = new ViajeJpaController(emf);
        viajeController.create(newViaje);
        
        int idViajeInsert = viajeController.getViajeCount();
        Session.getInstance().setAttribute("idViajeSeleccionado", idViajeInsert);
        
        //detalleViajeController.setViaje(newViaje);
        
        return "ok";
    }

    public void manejaEvento() {
        if (cadena.length() >= 3) {
            CiudadJpaController ciudadController = new CiudadJpaController(emf);
            this.listaCiudades = ciudadController.dameListaCiudadesLikeNombre(cadena);
        }
    }

    public void subirFoto() throws IOException {
        UploadedFile uploadedPhoto = publicarViajeBean.getImagen();
        //String filePath = "c:/bitacora";
        byte[] bytes = null;

        if (null != uploadedPhoto) {
            //String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String ruta = "C:/bitacora/usuarios/"; // main location for uploads
            String nombreUsuario = "frankamente"; //TODO coger de la sesion
            String filename = FilenameUtils.getName(uploadedPhoto.getFileName());
            File theFile = new File(ruta + "/" + nombreUsuario);
            theFile.mkdirs(); //Creación de carpetas

            bytes = uploadedPhoto.getContents();
//          BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(ruta + filename)));
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(ruta + "/" + nombreUsuario+"/"+filename)); // cannot find path when adding username atm
            
            stream.write(bytes);
            stream.close();
        }

    }

}
