/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.dao.exceptions.NonexistentEntityException;
import datos.entidades.Ciudad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import datos.entidades.Pais;
import datos.entidades.Dia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rafa
 */
public class CiudadJpaController implements Serializable {

    public CiudadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudad ciudad) {
        if (ciudad.getDiaList() == null) {
            ciudad.setDiaList(new ArrayList<Dia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais idPais = ciudad.getIdPais();
            if (idPais != null) {
                idPais = em.getReference(idPais.getClass(), idPais.getId());
                ciudad.setIdPais(idPais);
            }
            List<Dia> attachedDiaList = new ArrayList<Dia>();
            for (Dia diaListDiaToAttach : ciudad.getDiaList()) {
                diaListDiaToAttach = em.getReference(diaListDiaToAttach.getClass(), diaListDiaToAttach.getId());
                attachedDiaList.add(diaListDiaToAttach);
            }
            ciudad.setDiaList(attachedDiaList);
            em.persist(ciudad);
            if (idPais != null) {
                idPais.getCiudadList().add(ciudad);
                idPais = em.merge(idPais);
            }
            for (Dia diaListDia : ciudad.getDiaList()) {
                diaListDia.getCiudadList().add(ciudad);
                diaListDia = em.merge(diaListDia);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getId());
            Pais idPaisOld = persistentCiudad.getIdPais();
            Pais idPaisNew = ciudad.getIdPais();
            List<Dia> diaListOld = persistentCiudad.getDiaList();
            List<Dia> diaListNew = ciudad.getDiaList();
            if (idPaisNew != null) {
                idPaisNew = em.getReference(idPaisNew.getClass(), idPaisNew.getId());
                ciudad.setIdPais(idPaisNew);
            }
            List<Dia> attachedDiaListNew = new ArrayList<Dia>();
            for (Dia diaListNewDiaToAttach : diaListNew) {
                diaListNewDiaToAttach = em.getReference(diaListNewDiaToAttach.getClass(), diaListNewDiaToAttach.getId());
                attachedDiaListNew.add(diaListNewDiaToAttach);
            }
            diaListNew = attachedDiaListNew;
            ciudad.setDiaList(diaListNew);
            ciudad = em.merge(ciudad);
            if (idPaisOld != null && !idPaisOld.equals(idPaisNew)) {
                idPaisOld.getCiudadList().remove(ciudad);
                idPaisOld = em.merge(idPaisOld);
            }
            if (idPaisNew != null && !idPaisNew.equals(idPaisOld)) {
                idPaisNew.getCiudadList().add(ciudad);
                idPaisNew = em.merge(idPaisNew);
            }
            for (Dia diaListOldDia : diaListOld) {
                if (!diaListNew.contains(diaListOldDia)) {
                    diaListOldDia.getCiudadList().remove(ciudad);
                    diaListOldDia = em.merge(diaListOldDia);
                }
            }
            for (Dia diaListNewDia : diaListNew) {
                if (!diaListOld.contains(diaListNewDia)) {
                    diaListNewDia.getCiudadList().add(ciudad);
                    diaListNewDia = em.merge(diaListNewDia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ciudad.getId();
                if (findCiudad(id) == null) {
                    throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.");
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
            Ciudad ciudad;
            try {
                ciudad = em.getReference(Ciudad.class, id);
                ciudad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.", enfe);
            }
            Pais idPais = ciudad.getIdPais();
            if (idPais != null) {
                idPais.getCiudadList().remove(ciudad);
                idPais = em.merge(idPais);
            }
            List<Dia> diaList = ciudad.getDiaList();
            for (Dia diaListDia : diaList) {
                diaListDia.getCiudadList().remove(ciudad);
                diaListDia = em.merge(diaListDia);
            }
            em.remove(ciudad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudad> findCiudadEntities() {
        return findCiudadEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadEntities(int maxResults, int firstResult) {
        return findCiudadEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
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

    public Ciudad findCiudad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudad> rt = cq.from(Ciudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
