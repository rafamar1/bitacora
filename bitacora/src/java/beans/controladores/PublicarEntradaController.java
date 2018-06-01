/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.PublicarEntradaBean;
import beans.respaldo.Session;
import datos.dao.CiudadJpaController;
import datos.dao.ViajeJpaController;
import datos.entidades.Ciudad;
import datos.entidades.Dia;
import datos.entidades.Entrada;
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
public class PublicarEntradaController implements Serializable {

    @ManagedProperty(value = "#{publicarEntradaBean}")
    private PublicarEntradaBean publicarEntradaBean;
    private final EntityManagerFactory emf;
    private List<Ciudad> listaCiudades;
    private List<String> listaEtiquetas;
    private String cadena;
    private boolean primeraEntrada = true;

    public PublicarEntradaController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        listaCiudades = new ArrayList<>();
        listaEtiquetas = cargaListaEtiquetas();
    }    
    
    public PublicarEntradaBean getPublicarEntradaBean() {
        return publicarEntradaBean;
    }

    public void setPublicarEntradaBean(PublicarEntradaBean publicarEntradaBean) {
        this.publicarEntradaBean = publicarEntradaBean;
    }

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

    public boolean isPrimeraEntrada() {
        return primeraEntrada;
    }

    public void setPrimeraEntrada(boolean primeraEntrada) {
        this.primeraEntrada = primeraEntrada;
    }

    public List<String> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(List<String> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }
    
    public String publicar() {        
        ViajeJpaController viajeController = new ViajeJpaController(emf);
        Viaje viajeSeleccionado = viajeController.findViaje((int)Session.getInstance().getAttribute("idViajeSeleccionado"));
        try {
            subirFoto();
        } catch (IOException ex) {
            Logger.getLogger(PublicarEntradaController.class.getName()).log(Level.SEVERE, null, ex);
            return "error"; //TODO revisar control de excepciones
        }
        Entrada newEntrada = new Entrada();
        newEntrada.setTitulo(publicarEntradaBean.getTitulo());
        newEntrada.setDescripcion(publicarEntradaBean.getDescripcion());
        newEntrada.setIdDia((Dia)Session.getInstance().getAttribute("diaSeleccionado"));
        newEntrada.setImgMiniatura(publicarEntradaBean.getImagen().getFileName());
        //TODO gestionar la imagen tanto las individuales como las de la galería
        
        if(publicarEntradaBean.getPrecio() != null){
            newEntrada.setPrecio(publicarEntradaBean.getPrecio());
        }        
        
        int idViajeInsert = viajeController.getViajeCount();
        Session.getInstance().setAttribute("idViajeSeleccionado", idViajeInsert);
        
        return "ok";
    }

    public void manejaEvento() {
        if (cadena.length() >= 3) {
            CiudadJpaController ciudadController = new CiudadJpaController(emf);
            this.listaCiudades = ciudadController.dameListaCiudadesLikeNombre(cadena);
        }
    }

    public void subirFoto() throws IOException {
        UploadedFile uploadedPhoto = publicarEntradaBean.getImagen();
        //String filePath = "c:/bitacora";
        byte[] bytes = null;

        if (null != uploadedPhoto) {
            //String rutaFaces = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            String ruta = "C:/bitacora/usuarios/"; 
            //TODO extraer de la sesión o extraer del propio Viaje??
            String nombreUsuario = ((Usuario)Session.getInstance().getAttribute("usuarioLogeado")).getNombreUsuario();
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

    private List<String> cargaListaEtiquetas() {
        List<String> listaEtiquetas = new ArrayList<String>();
        listaEtiquetas.add("Alojamiento");
        listaEtiquetas.add("Restaurante");
        listaEtiquetas.add("Actividad");
        
        return listaEtiquetas;
    }

}
