/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import datos.entidades.Entrada;
import datos.entidades.Opinion;
import datos.entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rafa
 */
public class OpinionJpaController implements Serializable {

    public OpinionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Opinion opinion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrada idEntrada = opinion.getIdEntrada();
            if (idEntrada != null) {
                idEntrada = em.getReference(idEntrada.getClass(), idEntrada.getId());
                opinion.setIdEntrada(idEntrada);
            }
            Usuario idUsuario = opinion.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getNombreUsuario());
                opinion.setIdUsuario(idUsuario);
            }
            em.persist(opinion);
            if (idEntrada != null) {
                idEntrada.getOpinionList().add(opinion);
                idEntrada = em.merge(idEntrada);
            }
            if (idUsuario != null) {
                idUsuario.getOpinionList().add(opinion);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Opinion opinion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Opinion persistentOpinion = em.find(Opinion.class, opinion.getId());
            Entrada idEntradaOld = persistentOpinion.getIdEntrada();
            Entrada idEntradaNew = opinion.getIdEntrada();
            Usuario idUsuarioOld = persistentOpinion.getIdUsuario();
            Usuario idUsuarioNew = opinion.getIdUsuario();
            if (idEntradaNew != null) {
                idEntradaNew = em.getReference(idEntradaNew.getClass(), idEntradaNew.getId());
                opinion.setIdEntrada(idEntradaNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getNombreUsuario());
                opinion.setIdUsuario(idUsuarioNew);
            }
            opinion = em.merge(opinion);
            if (idEntradaOld != null && !idEntradaOld.equals(idEntradaNew)) {
                idEntradaOld.getOpinionList().remove(opinion);
                idEntradaOld = em.merge(idEntradaOld);
            }
            if (idEntradaNew != null && !idEntradaNew.equals(idEntradaOld)) {
                idEntradaNew.getOpinionList().add(opinion);
                idEntradaNew = em.merge(idEntradaNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getOpinionList().remove(opinion);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getOpinionList().add(opinion);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = opinion.getId();
                if (findOpinion(id) == null) {
                    throw new NonexistentEntityException("The opinion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Opinion opinion;
            try {
                opinion = em.getReference(Opinion.class, id);
                opinion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The opinion with id " + id + " no longer exists.", enfe);
            }
            Entrada idEntrada = opinion.getIdEntrada();
            if (idEntrada != null) {
                idEntrada.getOpinionList().remove(opinion);
                idEntrada = em.merge(idEntrada);
            }
            Usuario idUsuario = opinion.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getOpinionList().remove(opinion);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(opinion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Opinion> findOpinionEntities() {
        return findOpinionEntities(true, -1, -1);
    }

    public List<Opinion> findOpinionEntities(int maxResults, int firstResult) {
        return findOpinionEntities(false, maxResults, firstResult);
    }

    private List<Opinion> findOpinionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Opinion.class));
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

    public Opinion findOpinion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Opinion.class, id);
        } finally {
            em.close();
        }
    }

    public int getOpinionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Opinion> rt = cq.from(Opinion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
