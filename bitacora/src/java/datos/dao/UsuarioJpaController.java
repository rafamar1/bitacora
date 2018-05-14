/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.dao.exceptions.NonexistentEntityException;
import datos.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import datos.entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rafa
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getUsuarioList() == null) {
            usuario.setUsuarioList(new ArrayList<Usuario>());
        }
        if (usuario.getUsuarioList1() == null) {
            usuario.setUsuarioList1(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : usuario.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getNombreUsuario());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            usuario.setUsuarioList(attachedUsuarioList);
            List<Usuario> attachedUsuarioList1 = new ArrayList<Usuario>();
            for (Usuario usuarioList1UsuarioToAttach : usuario.getUsuarioList1()) {
                usuarioList1UsuarioToAttach = em.getReference(usuarioList1UsuarioToAttach.getClass(), usuarioList1UsuarioToAttach.getNombreUsuario());
                attachedUsuarioList1.add(usuarioList1UsuarioToAttach);
            }
            usuario.setUsuarioList1(attachedUsuarioList1);
            em.persist(usuario);
            for (Usuario usuarioListUsuario : usuario.getUsuarioList()) {
                usuarioListUsuario.getUsuarioList().add(usuario);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            for (Usuario usuarioList1Usuario : usuario.getUsuarioList1()) {
                usuarioList1Usuario.getUsuarioList().add(usuario);
                usuarioList1Usuario = em.merge(usuarioList1Usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getNombreUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getNombreUsuario());
            List<Usuario> usuarioListOld = persistentUsuario.getUsuarioList();
            List<Usuario> usuarioListNew = usuario.getUsuarioList();
            List<Usuario> usuarioList1Old = persistentUsuario.getUsuarioList1();
            List<Usuario> usuarioList1New = usuario.getUsuarioList1();
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getNombreUsuario());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            usuario.setUsuarioList(usuarioListNew);
            List<Usuario> attachedUsuarioList1New = new ArrayList<Usuario>();
            for (Usuario usuarioList1NewUsuarioToAttach : usuarioList1New) {
                usuarioList1NewUsuarioToAttach = em.getReference(usuarioList1NewUsuarioToAttach.getClass(), usuarioList1NewUsuarioToAttach.getNombreUsuario());
                attachedUsuarioList1New.add(usuarioList1NewUsuarioToAttach);
            }
            usuarioList1New = attachedUsuarioList1New;
            usuario.setUsuarioList1(usuarioList1New);
            usuario = em.merge(usuario);
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    usuarioListOldUsuario.getUsuarioList().remove(usuario);
                    usuarioListOldUsuario = em.merge(usuarioListOldUsuario);
                }
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    usuarioListNewUsuario.getUsuarioList().add(usuario);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                }
            }
            for (Usuario usuarioList1OldUsuario : usuarioList1Old) {
                if (!usuarioList1New.contains(usuarioList1OldUsuario)) {
                    usuarioList1OldUsuario.getUsuarioList().remove(usuario);
                    usuarioList1OldUsuario = em.merge(usuarioList1OldUsuario);
                }
            }
            for (Usuario usuarioList1NewUsuario : usuarioList1New) {
                if (!usuarioList1Old.contains(usuarioList1NewUsuario)) {
                    usuarioList1NewUsuario.getUsuarioList().add(usuario);
                    usuarioList1NewUsuario = em.merge(usuarioList1NewUsuario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getNombreUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getNombreUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Usuario> usuarioList = usuario.getUsuarioList();
            for (Usuario usuarioListUsuario : usuarioList) {
                usuarioListUsuario.getUsuarioList().remove(usuario);
                usuarioListUsuario = em.merge(usuarioListUsuario);
            }
            List<Usuario> usuarioList1 = usuario.getUsuarioList1();
            for (Usuario usuarioList1Usuario : usuarioList1) {
                usuarioList1Usuario.getUsuarioList().remove(usuario);
                usuarioList1Usuario = em.merge(usuarioList1Usuario);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
