package validaciones;


import beans.controladores.RegistroController;
import datos.dao.UsuarioJpaController;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@FacesValidator("uniqueNicknameValidator")
public class UniqueNicknameValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return; // Let required="true" handle, if any.
        }

        String email = (String) value;

        if (!compruebaUsuario(email)) {
            throw new ValidatorException(new FacesMessage(
                FacesMessage.SEVERITY_ERROR, "Ese nickname ya esta registrado", null));
        }
    }
    
     public boolean compruebaUsuario(String nickname){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bitacoraPU");
        UsuarioJpaController controlUser = new UsuarioJpaController(emf);
        return controlUser.findUsuario(nickname) == null;
    }

}