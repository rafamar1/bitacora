package server;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import datos.dao.UsuarioJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/*@WebServlet(
		name= "compruebaEmailRemote",
		urlPatterns={"/compruebaEmailRemote"})*/
public class compruebaEmailRemote extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String mail = "";
        boolean acceso = false;
        try {
            mail = request.getParameter("nickname");
            acceso = compruebaUsuario(mail);
            out.print(acceso);
            
        } catch (Exception ex) {
            out.println(ex);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); 
    }

    
    public boolean compruebaUsuario(String correo) {        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bitacoraPU");
        UsuarioJpaController controlUser = new UsuarioJpaController(emf);
        return controlUser.findUsuario(correo) == null;

    }

}
