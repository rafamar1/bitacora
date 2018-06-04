/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.dao.exceptions.IllegalOrphanException;
import datos.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import datos.entidades.Dia;
import datos.entidades.Opinion;
import java.util.ArrayList;
import java.util.List;
import datos.entidades.Ciudad;
import datos.entidades.Entrada;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Rafa
 */
public class EntradaJpaController implements Serializable {

    public EntradaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrada entrada) {
        if (entrada.getOpinionList() == null) {
            entrada.setOpinionList(new ArrayList<Opinion>());
        }
        if (entrada.getCiudadList() == null) {
            entrada.setCiudadList(new ArrayList<Ciudad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dia idDia = entrada.getIdDia();
            if (idDia != null) {
                idDia = em.getReference(idDia.getClass(), idDia.getId());
                entrada.setIdDia(idDia);
            }
            List<Opinion> attachedOpinionList = new ArrayList<Opinion>();
            for (Opinion opinionListOpinionToAttach : entrada.getOpinionList()) {
                opinionListOpinionToAttach = em.getReference(opinionListOpinionToAttach.getClass(), opinionListOpinionToAttach.getId());
                attachedOpinionList.add(opinionListOpinionToAttach);
            }
            entrada.setOpinionList(attachedOpinionList);
            List<Ciudad> attachedCiudadList = new ArrayList<Ciudad>();
            for (Ciudad ciudadListCiudadToAttach : entrada.getCiudadList()) {
                ciudadListCiudadToAttach = em.getReference(ciudadListCiudadToAttach.getClass(), ciudadListCiudadToAttach.getId());
                attachedCiudadList.add(ciudadListCiudadToAttach);
            }
            entrada.setCiudadList(attachedCiudadList);
            em.persist(entrada);
            if (idDia != null) {
                idDia.getEntradaList().add(entrada);
                idDia = em.merge(idDia);
            }
            for (Opinion opinionListOpinion : entrada.getOpinionList()) {
                Entrada oldIdEntradaOfOpinionListOpinion = opinionListOpinion.getIdEntrada();
                opinionListOpinion.setIdEntrada(entrada);
                opinionListOpinion = em.merge(opinionListOpinion);
                if (oldIdEntradaOfOpinionListOpinion != null) {
                    oldIdEntradaOfOpinionListOpinion.getOpinionList().remove(opinionListOpinion);
                    oldIdEntradaOfOpinionListOpinion = em.merge(oldIdEntradaOfOpinionListOpinion);
                }
            }
            for (Ciudad ciudadListCiudad : entrada.getCiudadList()) {
                ciudadListCiudad.getEntradaList().add(entrada);
                ciudadListCiudad = em.merge(ciudadListCiudad);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrada entrada) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrada persistentEntrada = em.find(Entrada.class, entrada.getId());
            Dia idDiaOld = persistentEntrada.getIdDia();
            Dia idDiaNew = entrada.getIdDia();
            List<Opinion> opinionListOld = persistentEntrada.getOpinionList();
            List<Opinion> opinionListNew = entrada.getOpinionList();
            List<Ciudad> ciudadListOld = persistentEntrada.getCiudadList();
            List<Ciudad> ciudadListNew = entrada.getCiudadList();
            List<String> illegalOrphanMessages = null;
            for (Opinion opinionListOldOpinion : opinionListOld) {
                if (!opinionListNew.contains(opinionListOldOpinion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Opinion " + opinionListOldOpinion + " since its idEntrada field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDiaNew != null) {
                idDiaNew = em.getReference(idDiaNew.getClass(), idDiaNew.getId());
                entrada.setIdDia(idDiaNew);
            }
            List<Opinion> attachedOpinionListNew = new ArrayList<Opinion>();
            for (Opinion opinionListNewOpinionToAttach : opinionListNew) {
                opinionListNewOpinionToAttach = em.getReference(opinionListNewOpinionToAttach.getClass(), opinionListNewOpinionToAttach.getId());
                attachedOpinionListNew.add(opinionListNewOpinionToAttach);
            }
            opinionListNew = attachedOpinionListNew;
            entrada.setOpinionList(opinionListNew);
            List<Ciudad> attachedCiudadListNew = new ArrayList<Ciudad>();
            for (Ciudad ciudadListNewCiudadToAttach : ciudadListNew) {
                ciudadListNewCiudadToAttach = em.getReference(ciudadListNewCiudadToAttach.getClass(), ciudadListNewCiudadToAttach.getId());
                attachedCiudadListNew.add(ciudadListNewCiudadToAttach);
            }
            ciudadListNew = attachedCiudadListNew;
            entrada.setCiudadList(ciudadListNew);
            entrada = em.merge(entrada);
            if (idDiaOld != null && !idDiaOld.equals(idDiaNew)) {
                idDiaOld.getEntradaList().remove(entrada);
                idDiaOld = em.merge(idDiaOld);
            }
            if (idDiaNew != null && !idDiaNew.equals(idDiaOld)) {
                idDiaNew.getEntradaList().add(entrada);
                idDiaNew = em.merge(idDiaNew);
            }
            for (Opinion opinionListNewOpinion : opinionListNew) {
                if (!opinionListOld.contains(opinionListNewOpinion)) {
                    Entrada oldIdEntradaOfOpinionListNewOpinion = opinionListNewOpinion.getIdEntrada();
                    opinionListNewOpinion.setIdEntrada(entrada);
                    opinionListNewOpinion = em.merge(opinionListNewOpinion);
                    if (oldIdEntradaOfOpinionListNewOpinion != null && !oldIdEntradaOfOpinionListNewOpinion.equals(entrada)) {
                        oldIdEntradaOfOpinionListNewOpinion.getOpinionList().remove(opinionListNewOpinion);
                        oldIdEntradaOfOpinionListNewOpinion = em.merge(oldIdEntradaOfOpinionListNewOpinion);
                    }
                }
            }
            for (Ciudad ciudadListOldCiudad : ciudadListOld) {
                if (!ciudadListNew.contains(ciudadListOldCiudad)) {
                    ciudadListOldCiudad.getEntradaList().remove(entrada);
                    ciudadListOldCiudad = em.merge(ciudadListOldCiudad);
                }
            }
            for (Ciudad ciudadListNewCiudad : ciudadListNew) {
                if (!ciudadListOld.contains(ciudadListNewCiudad)) {
                    ciudadListNewCiudad.getEntradaList().add(entrada);
                    ciudadListNewCiudad = em.merge(ciudadListNewCiudad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entrada.getId();
                if (findEntrada(id) == null) {
                    throw new NonexistentEntityException("The entrada with id " + id + " no longer exists.");
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
            Entrada entrada;
            try {
                entrada = em.getReference(Entrada.class, id);
                entrada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrada with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Opinion> opinionListOrphanCheck = entrada.getOpinionList();
            for (Opinion opinionListOrphanCheckOpinion : opinionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entrada (" + entrada + ") cannot be destroyed since the Opinion " + opinionListOrphanCheckOpinion + " in its opinionList field has a non-nullable idEntrada field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Dia idDia = entrada.getIdDia();
            if (idDia != null) {
                idDia.getEntradaList().remove(entrada);
                idDia = em.merge(idDia);
            }
            List<Ciudad> ciudadList = entrada.getCiudadList();
            for (Ciudad ciudadListCiudad : ciudadList) {
                ciudadListCiudad.getEntradaList().remove(entrada);
                ciudadListCiudad = em.merge(ciudadListCiudad);
            }
            em.remove(entrada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrada> findEntradaEntities() {
        return findEntradaEntities(true, -1, -1);
    }

    public List<Entrada> findEntradaEntities(int maxResults, int firstResult) {
        return findEntradaEntities(false, maxResults, firstResult);
    }

    private List<Entrada> findEntradaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrada.class));
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

    public Entrada findEntrada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrada.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntradaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrada> rt = cq.from(Entrada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
