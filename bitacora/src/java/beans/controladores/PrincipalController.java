package beans.controladores;

import beans.respaldo.SessionUtilsBean;
import datos.dao.UsuarioJpaController;

import datos.dao.ViajeJpaController;
import datos.entidades.Dia;
import datos.entidades.Entrada;
import datos.entidades.Usuario;
import datos.entidades.Viaje;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author rfmarquez
 */
@ManagedBean
@ViewScoped
public class PrincipalController implements Serializable {

    //TODO habria se puede eliminar el usuarioSeleccionado? o setearlo en el initialize
    @ManagedProperty(value = "#{sessionUtilsBean}")
    private SessionUtilsBean sessionUtilsBean;
    private final EntityManagerFactory emf;
    private List<Viaje> listaViajesQueSigo;

    public PrincipalController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
    }
    
    @PostConstruct
    public void initialize() {
        this.listaViajesQueSigo = null;
        this.listaViajesQueSigo = dameListaViajesOrdenados();
    }

    public SessionUtilsBean getSessionUtilsBean() {
        return sessionUtilsBean;
    }

    public void setSessionUtilsBean(SessionUtilsBean sessionUtilsBean) {
        this.sessionUtilsBean = sessionUtilsBean;
    }

    public List<Viaje> getListaViajesQueSigo() {
        return listaViajesQueSigo;
    }

    public void setListaViajesQueSigo(List<Viaje> listaViajesQueSigo) {
        this.listaViajesQueSigo = listaViajesQueSigo;
    }
    
    public String goToMiPerfil() {
        sessionUtilsBean.setIdUsuarioSeleccionado(sessionUtilsBean.getUsuario().getNombreUsuario());
        return "verPerfil";
    }

    public String dameRutaImgPerfil(Usuario usuario) {
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(usuario.getNombreUsuario());
        sb.append("/");
        sb.append(usuario.getImgPerfil());
        return sb.toString();
    }

    public String dameRutaImgViaje(Viaje viaje) {
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(viaje.getUsuario().getNombreUsuario());
        sb.append("/");
        sb.append(viaje.getId());
        sb.append("/");
        sb.append(viaje.getImgMiniatura());
        return sb.toString();
    }

    public String dameRutaImgPortada(Usuario usuario) {
        StringBuilder sb = new StringBuilder("resources/images/usuarios/");
        sb.append(usuario.getNombreUsuario());
        sb.append("/");
        sb.append(usuario.getImgPortada());
        return sb.toString();
    }

    public String dameCiudadesVisitadas(Viaje viaje) {

        List<Dia> listaDias = viaje.getDiaList();
        HashSet<String> hashSetNombresCiudades = new HashSet<>();
        for (Dia dia : listaDias) {
            for (Entrada entrada : dia.getEntradaList()) {
                hashSetNombresCiudades.add(entrada.getIdCiudad().getNombre());
            }
        }
        ArrayList<String> listaNombresCiudades = new ArrayList(hashSetNombresCiudades);
        int numMaximoCiudades = 3;
        StringBuilder sb = new StringBuilder();
        if (listaNombresCiudades.size() > 0) {
            sb.append(listaNombresCiudades.get(0));
            for (int i = 1; i < listaNombresCiudades.size(); i++) {
                if (listaNombresCiudades.get(i) != null && i < numMaximoCiudades) {
                    sb.append(" | ");
                    sb.append(listaNombresCiudades.get(i));
                }
            }
        }
        return sb.toString();
    }

    public List<Viaje> dameListaViajesOrdenados() {
        ViajeJpaController controlViaje = new ViajeJpaController(emf);
        List<Viaje> viajesOrdenadosList = controlViaje.dameListaViajesOrderFecha();
        List<Viaje> listaViajesDeUsuariosQueSigo = new ArrayList<>();
        for (Viaje viaje : viajesOrdenadosList) {
            if (esSeguidoPorUserLogged(viaje.getUsuario())) {
                listaViajesDeUsuariosQueSigo.add(viaje);
            }
        }

        return listaViajesDeUsuariosQueSigo;
    }

    private boolean esSeguidoPorUserLogged(Usuario user) {
        return user.getListaUsuarioTeSigue().contains(sessionUtilsBean.getUsuario()) || sessionUtilsBean.getUsuario().getListaUsuarioSeguido().contains(user);
    }
}
