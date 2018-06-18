/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.PublicarEntradaBean;
import beans.respaldo.SessionUtilsBean;
import datos.dao.CiudadJpaController;
import datos.dao.DiaJpaController;
import datos.dao.EntradaJpaController;
import datos.dao.exceptions.NonexistentEntityException;
import datos.entidades.Ciudad;
import datos.entidades.Dia;
import datos.entidades.Entrada;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author rfmarquez
 */
@ManagedBean
@ViewScoped
public class PublicarEntradaController implements Serializable {

    @ManagedProperty(value = "#{publicarEntradaBean}")
    private PublicarEntradaBean publicarEntradaBean;
    @ManagedProperty(value = "#{sessionUtilsBean}")
    private SessionUtilsBean sessionUtilsBean;
    private final EntityManagerFactory emf;
    private List<Ciudad> listaCiudades;
    private List<String> listaCategorias;
    private boolean primeraEntrada = true;
    private Integer idEntrada;

    public PublicarEntradaController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        listaCiudades = new ArrayList<>();
        listaCategorias = cargaListaCategorias();
    }

    public PublicarEntradaBean getPublicarEntradaBean() {
        return publicarEntradaBean;
    }

    public void setPublicarEntradaBean(PublicarEntradaBean publicarEntradaBean) {
        this.publicarEntradaBean = publicarEntradaBean;
    }

    public SessionUtilsBean getSessionUtilsBean() {
        return sessionUtilsBean;
    }

    public void setSessionUtilsBean(SessionUtilsBean sessionUtilsBean) {
        this.sessionUtilsBean = sessionUtilsBean;
    }
    
    

    public List<Ciudad> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudad> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public boolean isPrimeraEntrada() {
        return primeraEntrada;
    }

    public void setPrimeraEntrada(boolean primeraEntrada) {
        this.primeraEntrada = primeraEntrada;
    }

    public List<String> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<String> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public Integer getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(Integer idEntrada) {
        this.idEntrada = idEntrada;
    }
    
    public List<Ciudad> autocompletadoCiudades(String query) {

        if (query.length() > 2) {
            CiudadJpaController ciudadController = new CiudadJpaController(emf);
            return ciudadController.dameListaCiudadesLikeNombre(query);
        }
        return this.listaCiudades;
    }
    
    public String publicarEntrada() {        
        //TODO revisar el seteo de la fecha de modificaion!
        Entrada newEntrada = setearValoresEntrada();
        
        try {
            cambiarNombreImagenPorIdEntrada(newEntrada);
            subirFotoEntrada();
//            subirFoto();
        } catch (IOException ex) {
            Logger.getLogger(PublicarEntradaController.class.getName()).log(Level.SEVERE, null, ex);
            return "error"; //TODO revisar control de excepciones
        } catch (Exception ex) {
            Logger.getLogger(PublicarEntradaController.class.getName()).log(Level.SEVERE, null, ex);
            return "error"; 
        }                
        
        return "ok";
    }

    private Entrada setearValoresEntrada() {
        DiaJpaController diaController = new DiaJpaController(emf);
        CiudadJpaController controllerCiudad = new CiudadJpaController(emf);
        Dia primerDia = diaController.findDia(sessionUtilsBean.getIdDiaSeleccionado());
        
        Entrada newEntrada = new Entrada();
        newEntrada.setTitulo(publicarEntradaBean.getTitulo());
        newEntrada.setDescripcion(publicarEntradaBean.getDescripcion());
        newEntrada.setIdDia(primerDia);
        
        newEntrada.setIdCiudad(controllerCiudad.findCiudad(publicarEntradaBean.getIdCiudad()));
        newEntrada.setImgMiniatura(publicarEntradaBean.getImagen().getFileName());
        newEntrada.setPuntuacion(publicarEntradaBean.getPuntuacion());
        newEntrada.setTipo(publicarEntradaBean.getTipo());
        
        if(publicarEntradaBean.getPrecio() != null){
            newEntrada.setPrecio(publicarEntradaBean.getPrecio());
        }  
        
        return newEntrada;
    }
    
     private void cambiarNombreImagenPorIdEntrada(Entrada newEntrada) throws NonexistentEntityException, Exception {
        EntradaJpaController entradaControl = new EntradaJpaController(emf);
        entradaControl.create(newEntrada);
        Integer idEntradaInsert = newEntrada.getId();
        newEntrada.setImgMiniatura(String.valueOf(idEntradaInsert) + ".jpg");
        entradaControl.edit(newEntrada);
        this.idEntrada = idEntradaInsert;
    }
    
    public void subirFotoEntrada() throws IOException {
        UploadedFile uploadedPhoto = publicarEntradaBean.getImagen();
        byte[] bytes = null;

        if (null != uploadedPhoto) {
            String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String rutaUsuarios = rutaFaces.concat("resources\\images\\usuarios");
            //String rutaRaiz = "C:/bitacora/usuarios";
            if (sessionUtilsBean.getUsuario().getNombreUsuario() != null
                    && sessionUtilsBean.getIdViajeSeleccionado() != null
                    && sessionUtilsBean.getIdDiaSeleccionado() != null
                    && this.idEntrada != null) {
                String nombreUsuario = sessionUtilsBean.getUsuario().getNombreUsuario();
                String idViajeSeleccionado = String.valueOf(sessionUtilsBean.getIdViajeSeleccionado());
                String idDiaSeleccionado = String.valueOf(sessionUtilsBean.getIdDiaSeleccionado());
                String rutaFichero = rutaUsuarios + "/" + nombreUsuario + "/" + idViajeSeleccionado + "/" + idDiaSeleccionado;
                File theFile = new File(rutaFichero);
                theFile.mkdirs(); //Creación de carpeta

                bytes = uploadedPhoto.getContents();

                FileOutputStream nuevoFichero = new FileOutputStream(rutaFichero + "/" + this.idEntrada + ".jpg");
                BufferedOutputStream stream = new BufferedOutputStream(nuevoFichero);

                stream.write(bytes);
                stream.close();
            }

        }

    }
    
    /*public void subirFoto() throws IOException {
        UploadedFile uploadedPhoto = publicarEntradaBean.getImagen();
        byte[] bytes = null;
        
        if (null != uploadedPhoto) {
            //String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String rutaRaiz = "C:/bitacora/usuarios"; 
            if (((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario() != null
                    && Session.getInstance().getAttribute("idViajeSeleccionado") != null
                    && Session.getInstance().getAttribute("idDiaSeleccionado") != null
                    && this.idEntrada != null) {
                String nombreUsuario = ((Usuario) Session.getInstance().getAttribute("usuario")).getNombreUsuario();
                String idViajeSeleccionado = String.valueOf(Session.getInstance().getAttribute("idViajeSeleccionado"));
                String idDiaSeleccionado = String.valueOf(Session.getInstance().getAttribute("idDiaSeleccionado"));
                String rutaFichero = rutaRaiz + "/" + nombreUsuario + "/" + idViajeSeleccionado + "/" + idDiaSeleccionado;
                File theFile = new File(rutaFichero);
                theFile.mkdirs();

                bytes = uploadedPhoto.getContents();

                FileOutputStream nuevoFichero = new FileOutputStream(rutaFichero + "/" + this.idEntrada + ".jpg");
                BufferedOutputStream stream = new BufferedOutputStream(nuevoFichero);

                stream.write(bytes);
                stream.close();
            }
        }

    }*/

    private List<String> cargaListaCategorias() {
        List<String> tagList = new ArrayList<>();
        tagList.add("Restaurante");
        tagList.add("Espectáculo");
        tagList.add("Ocio");
        tagList.add("Alojamiento");

        return tagList;
    }

}
