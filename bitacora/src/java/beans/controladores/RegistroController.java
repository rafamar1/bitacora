/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;


import beans.modelos.RegistroBean;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author rfmarquez
 */
@Named(value = "registroController")
@ConversationScoped
public class RegistroController implements Serializable {

    @ManagedProperty(value = "#{registroBean}")
    private RegistroBean registroBean;
    /**
     * Creates a new instance of RegistroController
     */
    public RegistroController() {
    }

    public RegistroBean getRegistroBean() {
        return registroBean;
    }

    public void setRegistroBean(RegistroBean registroBean) {
        this.registroBean = registroBean;
    }
    
    public String altaUsuario(){
        return "index.xhtml";
    }
}
