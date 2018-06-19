/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validaciones;

import beans.respaldo.Session;
import java.io.IOException;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebFilter("/bitacora/*")
public class AuthorizationFilter implements Filter {

    private static final String AJAX_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<partial-response><redirect url=\"%s\"></redirect></partial-response>";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String raizURL = request.getContextPath() + "/";
        String loginURL = request.getContextPath() + "/login.xhtml";
        String indexURL = request.getContextPath() + "/index.xhtml";
        String registroURL = request.getContextPath() + "/registro.xhtml";

        boolean loggedIn = (session != null) && (session.getAttribute("usuarioLogeado") != null);
        boolean loginRequest = request.getRequestURI().equals(loginURL);
        boolean indexRequest = request.getRequestURI().equals(indexURL);
        boolean registroRequest = request.getRequestURI().equals(registroURL);
        boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");
        boolean ajaxRequest = "partial/ajax".equals(request.getHeader("Faces-Request"));

        if (loggedIn || loginRequest || resourceRequest || indexRequest || registroRequest) {
            if (!resourceRequest) {
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
            }
            chain.doFilter(request, response);
        } else if (ajaxRequest) {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().printf(AJAX_REDIRECT_XML, loginURL);
        } else if(request.getRequestURI().equals(raizURL)){
            response.sendRedirect(indexURL);
        }  else {
            response.sendRedirect(loginURL);
        }

    }

    @Override
    public void init(FilterConfig filter) {
    }

    @Override
    public void destroy() {
    }

    // You need to override init() and destroy() as well, but they can be kept empty.
}
