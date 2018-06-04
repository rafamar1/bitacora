/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.dao.exceptions.NonexistentEntityException;
import datos.entidades.Foto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import datos.entidades.Viaje;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rafa
 */
public class FotoJpaController implements Serializable {

    public FotoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Foto foto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viaje idViaje = foto.getIdViaje();
            if (idViaje != null) {
                idViaje = em.getReference(idViaje.getClass(), idViaje.getId());
                foto.setIdViaje(idViaje);
            }
            em.persist(foto);
            if (idViaje != null) {
                idViaje.getFotoList().add(foto);
                idViaje = em.merge(idViaje);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Foto foto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Foto persistentFoto = em.find(Foto.class, foto.getId());
            Viaje idViajeOld = persistentFoto.getIdViaje();
            Viaje idViajeNew = foto.getIdViaje();
            if (idViajeNew != null) {
                idViajeNew = em.getReference(idViajeNew.getClass(), idViajeNew.getId());
                foto.setIdViaje(idViajeNew);
            }
            foto = em.merge(foto);
            if (idViajeOld != null && !idViajeOld.equals(idViajeNew)) {
                idViajeOld.getFotoList().remove(foto);
                idViajeOld = em.merge(idViajeOld);
            }
            if (idViajeNew != null && !idViajeNew.equals(idViajeOld)) {
                idViajeNew.getFotoList().add(foto);
                idViajeNew = em.merge(idViajeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = foto.getId();
                if (findFoto(id) == null) {
                    throw new NonexistentEntityException("The foto with id " + id + " no longer exists.");
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
            Foto foto;
            try {
                foto = em.getReference(Foto.class, id);
                foto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The foto with id " + id + " no longer exists.", enfe);
            }
            Viaje idViaje = foto.getIdViaje();
            if (idViaje != null) {
                idViaje.getFotoList().remove(foto);
                idViaje = em.merge(idViaje);
            }
            em.remove(foto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Foto> findFotoEntities() {
        return findFotoEntities(true, -1, -1);
    }

    public List<Foto> findFotoEntities(int maxResults, int firstResult) {
        return findFotoEntities(false, maxResults, firstResult);
    }

    private List<Foto> findFotoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Foto.class));
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

    public Foto findFoto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Foto.class, id);
        } finally {
            em.close();
        }
    }

    public int getFotoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Foto> rt = cq.from(Foto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
