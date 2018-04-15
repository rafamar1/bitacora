/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import DTO.Dia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Viaje;
import DTO.Entrada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rafa
 */
public class DiaJpaController implements Serializable {

    public DiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dia dia) {
        if (dia.getEntradaList() == null) {
            dia.setEntradaList(new ArrayList<Entrada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viaje idViaje = dia.getIdViaje();
            if (idViaje != null) {
                idViaje = em.getReference(idViaje.getClass(), idViaje.getId());
                dia.setIdViaje(idViaje);
            }
            List<Entrada> attachedEntradaList = new ArrayList<Entrada>();
            for (Entrada entradaListEntradaToAttach : dia.getEntradaList()) {
                entradaListEntradaToAttach = em.getReference(entradaListEntradaToAttach.getClass(), entradaListEntradaToAttach.getId());
                attachedEntradaList.add(entradaListEntradaToAttach);
            }
            dia.setEntradaList(attachedEntradaList);
            em.persist(dia);
            if (idViaje != null) {
                idViaje.getDiaList().add(dia);
                idViaje = em.merge(idViaje);
            }
            for (Entrada entradaListEntrada : dia.getEntradaList()) {
                Dia oldIdDiaOfEntradaListEntrada = entradaListEntrada.getIdDia();
                entradaListEntrada.setIdDia(dia);
                entradaListEntrada = em.merge(entradaListEntrada);
                if (oldIdDiaOfEntradaListEntrada != null) {
                    oldIdDiaOfEntradaListEntrada.getEntradaList().remove(entradaListEntrada);
                    oldIdDiaOfEntradaListEntrada = em.merge(oldIdDiaOfEntradaListEntrada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dia dia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dia persistentDia = em.find(Dia.class, dia.getId());
            Viaje idViajeOld = persistentDia.getIdViaje();
            Viaje idViajeNew = dia.getIdViaje();
            List<Entrada> entradaListOld = persistentDia.getEntradaList();
            List<Entrada> entradaListNew = dia.getEntradaList();
            List<String> illegalOrphanMessages = null;
            for (Entrada entradaListOldEntrada : entradaListOld) {
                if (!entradaListNew.contains(entradaListOldEntrada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Entrada " + entradaListOldEntrada + " since its idDia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idViajeNew != null) {
                idViajeNew = em.getReference(idViajeNew.getClass(), idViajeNew.getId());
                dia.setIdViaje(idViajeNew);
            }
            List<Entrada> attachedEntradaListNew = new ArrayList<Entrada>();
            for (Entrada entradaListNewEntradaToAttach : entradaListNew) {
                entradaListNewEntradaToAttach = em.getReference(entradaListNewEntradaToAttach.getClass(), entradaListNewEntradaToAttach.getId());
                attachedEntradaListNew.add(entradaListNewEntradaToAttach);
            }
            entradaListNew = attachedEntradaListNew;
            dia.setEntradaList(entradaListNew);
            dia = em.merge(dia);
            if (idViajeOld != null && !idViajeOld.equals(idViajeNew)) {
                idViajeOld.getDiaList().remove(dia);
                idViajeOld = em.merge(idViajeOld);
            }
            if (idViajeNew != null && !idViajeNew.equals(idViajeOld)) {
                idViajeNew.getDiaList().add(dia);
                idViajeNew = em.merge(idViajeNew);
            }
            for (Entrada entradaListNewEntrada : entradaListNew) {
                if (!entradaListOld.contains(entradaListNewEntrada)) {
                    Dia oldIdDiaOfEntradaListNewEntrada = entradaListNewEntrada.getIdDia();
                    entradaListNewEntrada.setIdDia(dia);
                    entradaListNewEntrada = em.merge(entradaListNewEntrada);
                    if (oldIdDiaOfEntradaListNewEntrada != null && !oldIdDiaOfEntradaListNewEntrada.equals(dia)) {
                        oldIdDiaOfEntradaListNewEntrada.getEntradaList().remove(entradaListNewEntrada);
                        oldIdDiaOfEntradaListNewEntrada = em.merge(oldIdDiaOfEntradaListNewEntrada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dia.getId();
                if (findDia(id) == null) {
                    throw new NonexistentEntityException("The dia with id " + id + " no longer exists.");
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
            Dia dia;
            try {
                dia = em.getReference(Dia.class, id);
                dia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Entrada> entradaListOrphanCheck = dia.getEntradaList();
            for (Entrada entradaListOrphanCheckEntrada : entradaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dia (" + dia + ") cannot be destroyed since the Entrada " + entradaListOrphanCheckEntrada + " in its entradaList field has a non-nullable idDia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Viaje idViaje = dia.getIdViaje();
            if (idViaje != null) {
                idViaje.getDiaList().remove(dia);
                idViaje = em.merge(idViaje);
            }
            em.remove(dia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dia> findDiaEntities() {
        return findDiaEntities(true, -1, -1);
    }

    public List<Dia> findDiaEntities(int maxResults, int firstResult) {
        return findDiaEntities(false, maxResults, firstResult);
    }

    private List<Dia> findDiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dia.class));
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

    public Dia findDia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dia.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dia> rt = cq.from(Dia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
