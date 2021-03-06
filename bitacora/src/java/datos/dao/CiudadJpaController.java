/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.dao.exceptions.IllegalOrphanException;
import datos.dao.exceptions.NonexistentEntityException;
import datos.entidades.Ciudad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import datos.entidades.Pais;
import datos.entidades.Entrada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

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
        if (ciudad.getEntradaList() == null) {
            ciudad.setEntradaList(new ArrayList<Entrada>());
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
            List<Entrada> attachedEntradaList = new ArrayList<Entrada>();
            for (Entrada entradaListEntradaToAttach : ciudad.getEntradaList()) {
                entradaListEntradaToAttach = em.getReference(entradaListEntradaToAttach.getClass(), entradaListEntradaToAttach.getId());
                attachedEntradaList.add(entradaListEntradaToAttach);
            }
            ciudad.setEntradaList(attachedEntradaList);
            em.persist(ciudad);
            if (idPais != null) {
                idPais.getCiudadList().add(ciudad);
                idPais = em.merge(idPais);
            }
            for (Entrada entradaListEntrada : ciudad.getEntradaList()) {
                Ciudad oldIdCiudadOfEntradaListEntrada = entradaListEntrada.getIdCiudad();
                entradaListEntrada.setIdCiudad(ciudad);
                entradaListEntrada = em.merge(entradaListEntrada);
                if (oldIdCiudadOfEntradaListEntrada != null) {
                    oldIdCiudadOfEntradaListEntrada.getEntradaList().remove(entradaListEntrada);
                    oldIdCiudadOfEntradaListEntrada = em.merge(oldIdCiudadOfEntradaListEntrada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getId());
            Pais idPaisOld = persistentCiudad.getIdPais();
            Pais idPaisNew = ciudad.getIdPais();
            List<Entrada> entradaListOld = persistentCiudad.getEntradaList();
            List<Entrada> entradaListNew = ciudad.getEntradaList();
            List<String> illegalOrphanMessages = null;
            for (Entrada entradaListOldEntrada : entradaListOld) {
                if (!entradaListNew.contains(entradaListOldEntrada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrada " + entradaListOldEntrada + " since its idCiudad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPaisNew != null) {
                idPaisNew = em.getReference(idPaisNew.getClass(), idPaisNew.getId());
                ciudad.setIdPais(idPaisNew);
            }
            List<Entrada> attachedEntradaListNew = new ArrayList<Entrada>();
            for (Entrada entradaListNewEntradaToAttach : entradaListNew) {
                entradaListNewEntradaToAttach = em.getReference(entradaListNewEntradaToAttach.getClass(), entradaListNewEntradaToAttach.getId());
                attachedEntradaListNew.add(entradaListNewEntradaToAttach);
            }
            entradaListNew = attachedEntradaListNew;
            ciudad.setEntradaList(entradaListNew);
            ciudad = em.merge(ciudad);
            if (idPaisOld != null && !idPaisOld.equals(idPaisNew)) {
                idPaisOld.getCiudadList().remove(ciudad);
                idPaisOld = em.merge(idPaisOld);
            }
            if (idPaisNew != null && !idPaisNew.equals(idPaisOld)) {
                idPaisNew.getCiudadList().add(ciudad);
                idPaisNew = em.merge(idPaisNew);
            }
            for (Entrada entradaListNewEntrada : entradaListNew) {
                if (!entradaListOld.contains(entradaListNewEntrada)) {
                    Ciudad oldIdCiudadOfEntradaListNewEntrada = entradaListNewEntrada.getIdCiudad();
                    entradaListNewEntrada.setIdCiudad(ciudad);
                    entradaListNewEntrada = em.merge(entradaListNewEntrada);
                    if (oldIdCiudadOfEntradaListNewEntrada != null && !oldIdCiudadOfEntradaListNewEntrada.equals(ciudad)) {
                        oldIdCiudadOfEntradaListNewEntrada.getEntradaList().remove(entradaListNewEntrada);
                        oldIdCiudadOfEntradaListNewEntrada = em.merge(oldIdCiudadOfEntradaListNewEntrada);
                    }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Entrada> entradaListOrphanCheck = ciudad.getEntradaList();
            for (Entrada entradaListOrphanCheckEntrada : entradaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudad (" + ciudad + ") cannot be destroyed since the Entrada " + entradaListOrphanCheckEntrada + " in its entradaList field has a non-nullable idCiudad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pais idPais = ciudad.getIdPais();
            if (idPais != null) {
                idPais.getCiudadList().remove(ciudad);
                idPais = em.merge(idPais);
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
    
    public List<Ciudad> dameListaCiudadesDadoIdPais(int idPais) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<List> query = em.createNamedQuery("Ciudad.findByIdPais", List.class);
        List lista = query.setParameter("idPais", idPais).getResultList();
        return lista;
}

    public List<Ciudad> dameListaCiudadesLikeNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<List> query = em.createNamedQuery("Ciudad.findLikeNombre", List.class);
        List lista = query.setParameter("nombre", nombre + "%").getResultList();
        return lista;
    }
}
