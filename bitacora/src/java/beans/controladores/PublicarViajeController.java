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
import datos.dao.exceptions.NonexistentEntityException;
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

        Viaje newViaje = setearValoresNuevoViaje();
        //TODO gestionar la imagen tanto las individuales como las de la galería

        try {
            cambiarNombreImagenPorIdViaje(newViaje);
            subirFotoViaje();

        } catch (IOException ex) {
            Logger.getLogger(PublicarViajeController.class.getName()).log(Level.SEVERE, null, ex);
            return "error"; //TODO revisar control de excepciones
        } catch (Exception ex) {
            Logger.getLogger(PublicarViajeController.class.getName()).log(Level.SEVERE, null, ex);
            return "error";
        }

        return "ok";
    }

    private Viaje setearValoresNuevoViaje() {
        Viaje newViaje = new Viaje();
        newViaje.setTitulo(publicarViajeBean.getTitulo());
        newViaje.setDescripcion(publicarViajeBean.getDescripcion());
        if (((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario() != null) {
            newViaje.setUsuario(((Usuario) Session.getInstance().getAttribute("usuario")));
        }
        newViaje.setImgMiniatura(publicarViajeBean.getImagen().getFileName());
        return newViaje;
    }

    private void cambiarNombreImagenPorIdViaje(Viaje newViaje) throws NonexistentEntityException, Exception {
        ViajeJpaController viajeController = new ViajeJpaController(emf);
        viajeController.create(newViaje);
        Integer idViajeInsert = newViaje.getId();
        newViaje.setImgMiniatura(String.valueOf(idViajeInsert) + ".jpg");
        viajeController.edit(newViaje);

        DiaJpaController diaController = new DiaJpaController(emf);
        Dia primerDia = setearPrimerDia(viajeController, newViaje);
        diaController.create(primerDia);
        Integer idPrimerDia = primerDia.getId();

        Session.getInstance().setAttribute("idDiaSeleccionado", idPrimerDia);
        Session.getInstance().setAttribute("idViajeSeleccionado", idViajeInsert);

    }

    private Dia setearPrimerDia(ViajeJpaController viajeController, Viaje newViaje) {
        Dia primerDia = new Dia();
        primerDia.setIdViaje(viajeController.findViaje(newViaje.getId()));
        primerDia.setFecha(Calendar.getInstance().getTime());
        return primerDia;
    }

    public void subirFotoViaje() throws IOException {
        UploadedFile uploadedPhoto = publicarViajeBean.getImagen();
        byte[] bytes = null;

        if (null != uploadedPhoto) {
            //String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String rutaRaiz = "C:/bitacora/usuarios/"; // main location for uploads
            if (((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario() != null
                    && Session.getInstance().getAttribute("idViajeSeleccionado") != null) {
                String nombreUsuario = ((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario();
                String idViajeSeleccionado = String.valueOf(Session.getInstance().getAttribute("idViajeSeleccionado"));
                String rutaFichero = rutaRaiz + "/" + nombreUsuario + "/" + idViajeSeleccionado;
                File theFile = new File(rutaFichero);
                theFile.mkdirs(); //Creación de carpeta

                bytes = uploadedPhoto.getContents();

                FileOutputStream nuevoFichero = new FileOutputStream(rutaFichero + "/" + idViajeSeleccionado + ".jpg");
                BufferedOutputStream stream = new BufferedOutputStream(nuevoFichero);

                stream.write(bytes);
                stream.close();

            }

        }

    }

}
