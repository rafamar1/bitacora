/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.controladores;

import beans.modelos.LoginBean;
import datos.dao.UsuarioJpaController;
import datos.entidades.Usuario;
import org.jasypt.util.password.*;
import java.io.Serializable;
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
public class LoginController implements Serializable {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    //@PersistenceUnit(unitName = "bitacoraPU")
    private final EntityManagerFactory emf;
    private boolean badLogin;

    public LoginController() {
        emf = Persistence.createEntityManagerFactory("bitacoraPU");
        badLogin = false;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public boolean isBadLogin() {
        return badLogin;
    }

    public void setBadLogin(boolean badLogin) {
        this.badLogin = badLogin;
    }
    
    public String loginUsuario() {
        String nickname = loginBean.getNombreUsuario();
        String inputPassword = loginBean.getPassword();

        UsuarioJpaController usuarioController = new UsuarioJpaController(emf);

        try {
            Usuario usuario = usuarioController.findUsuario(nickname);
            if (usuario != null) {
                BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
                
                if (passwordEncryptor.checkPassword(inputPassword, usuario.getPassword())) {
                    return "loginOk";
                } else {
                    this.badLogin = true;
                    return "error";
                }
                
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            this.badLogin = true;
            return "login.xhtml";
        }

        this.badLogin = true;
        return "login.xhtml";
    }

}
