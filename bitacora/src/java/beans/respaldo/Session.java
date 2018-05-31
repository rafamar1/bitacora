/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.respaldo;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Rafa
 */

public class Session {

    private static Session instance;
    private static boolean usuarioLogeado;

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    private Session() {
    }

    private ExternalContext currentExternalContext() {
        if (FacesContext.getCurrentInstance() == null) {
            throw new RuntimeException("FacesContext can’t be called outside of a HTTP request");
        } else {
            return FacesContext.getCurrentInstance().getExternalContext();
        }
    }

    public String logout() {
        //TODO revisar este setAttribute
        setAttribute("usuarioLogeado", false);
        this.currentExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
    public Object getAttribute(String name) {
        return currentExternalContext().getSessionMap().get(name);
    }

    public void setAttribute(String name, Object value) {
        currentExternalContext().getSessionMap().put(name, value);
    }

    public static boolean isUsuarioLogeado() {
        return usuarioLogeado;
    }

    public static void setUsuarioLogeado(boolean usuarioLogeado) {
        Session.usuarioLogeado = usuarioLogeado;
    }
    
}

