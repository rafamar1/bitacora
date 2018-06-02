/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.PublicarViajeBean;
import beans.respaldo.Session;
import datos.dao.DiaJpaController;
import datos.dao.ViajeJpaController;
import datos.entidades.Dia;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author rfmarquez
 */

//TODO revisar si esta refactorizacion de anotaciones funciona correctamente
@ManagedBean
@RequestScoped
public class PublicarViajeController implements Serializable {

    @ManagedProperty(value = "#{publicarViajeBean}")
    private PublicarViajeBean publicarViajeBean;
    private final EntityManagerFactory emf;

    public PublicarViajeController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
    }

    public PublicarViajeBean getPublicarViajeBean() {
        return publicarViajeBean;
    }

    public void setPublicarViajeBean(PublicarViajeBean publicarViajeBean) {
        this.publicarViajeBean = publicarViajeBean;
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
        newViaje.setUsuario(((Usuario)Session.getInstance().getAttribute("usuario")));
        newViaje.setImgMiniatura(publicarViajeBean.getImagen().getFileName());
        //TODO gestionar la imagen tanto las individuales como las de la galería
        ViajeJpaController viajeController = new ViajeJpaController(emf);
        viajeController.create(newViaje);
        Integer idViajeInsert = newViaje.getId();
        Session.getInstance().setAttribute("idViajeSeleccionado", idViajeInsert);
        
        DiaJpaController diaController = new DiaJpaController(emf);
        Dia primerDia = new Dia();
        primerDia.setIdViaje(viajeController.findViaje(idViajeInsert));
        primerDia.setFecha(Calendar.getInstance().getTime());
        diaController.create(primerDia);
        Integer idPrimerDia = primerDia.getId();
        Session.getInstance().setAttribute("idDiaSeleccionado", idPrimerDia);
        
        
        return "ok";
    }

    public void subirFoto() throws IOException {
        UploadedFile uploadedPhoto = publicarViajeBean.getImagen();
        //String filePath = "c:/bitacora";
        byte[] bytes = null;

        if (null != uploadedPhoto) {
            //String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String ruta = "C:/bitacora/usuarios/"; // main location for uploads
            //TODO coger de la sesion
            String nombreUsuario = ((Usuario)Session.getInstance().getAttribute("usuario")).getNombreUsuario(); 
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
