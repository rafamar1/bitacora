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
import datos.entidades.Usuario;
import datos.entidades.Dia;
import java.util.ArrayList;
import java.util.List;
import datos.entidades.Foto;
import datos.entidades.Viaje;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Rafa
 */
public class ViajeJpaController implements Serializable {

    public ViajeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viaje viaje) {
        if (viaje.getDiaList() == null) {
            viaje.setDiaList(new ArrayList<Dia>());
        }
        if (viaje.getFotoList() == null) {
            viaje.setFotoList(new ArrayList<Foto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = viaje.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getNombreUsuario());
                viaje.setUsuario(usuario);
            }
            List<Dia> attachedDiaList = new ArrayList<Dia>();
            for (Dia diaListDiaToAttach : viaje.getDiaList()) {
                diaListDiaToAttach = em.getReference(diaListDiaToAttach.getClass(), diaListDiaToAttach.getId());
                attachedDiaList.add(diaListDiaToAttach);
            }
            viaje.setDiaList(attachedDiaList);
            List<Foto> attachedFotoList = new ArrayList<Foto>();
            for (Foto fotoListFotoToAttach : viaje.getFotoList()) {
                fotoListFotoToAttach = em.getReference(fotoListFotoToAttach.getClass(), fotoListFotoToAttach.getId());
                attachedFotoList.add(fotoListFotoToAttach);
            }
            viaje.setFotoList(attachedFotoList);
            em.persist(viaje);
            if (usuario != null) {
                usuario.getViajeList().add(viaje);
                usuario = em.merge(usuario);
            }
            for (Dia diaListDia : viaje.getDiaList()) {
                Viaje oldIdViajeOfDiaListDia = diaListDia.getIdViaje();
                diaListDia.setIdViaje(viaje);
                diaListDia = em.merge(diaListDia);
                if (oldIdViajeOfDiaListDia != null) {
                    oldIdViajeOfDiaListDia.getDiaList().remove(diaListDia);
                    oldIdViajeOfDiaListDia = em.merge(oldIdViajeOfDiaListDia);
                }
            }
            for (Foto fotoListFoto : viaje.getFotoList()) {
                Viaje oldIdViajeOfFotoListFoto = fotoListFoto.getIdViaje();
                fotoListFoto.setIdViaje(viaje);
                fotoListFoto = em.merge(fotoListFoto);
                if (oldIdViajeOfFotoListFoto != null) {
                    oldIdViajeOfFotoListFoto.getFotoList().remove(fotoListFoto);
                    oldIdViajeOfFotoListFoto = em.merge(oldIdViajeOfFotoListFoto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viaje viaje) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viaje persistentViaje = em.find(Viaje.class, viaje.getId());
            Usuario usuarioOld = persistentViaje.getUsuario();
            Usuario usuarioNew = viaje.getUsuario();
            List<Dia> diaListOld = persistentViaje.getDiaList();
            List<Dia> diaListNew = viaje.getDiaList();
            List<Foto> fotoListOld = persistentViaje.getFotoList();
            List<Foto> fotoListNew = viaje.getFotoList();
            List<String> illegalOrphanMessages = null;
            for (Dia diaListOldDia : diaListOld) {
                if (!diaListNew.contains(diaListOldDia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dia " + diaListOldDia + " since its idViaje field is not nullable.");
                }
            }
            for (Foto fotoListOldFoto : fotoListOld) {
                if (!fotoListNew.contains(fotoListOldFoto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Foto " + fotoListOldFoto + " since its idViaje field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getNombreUsuario());
                viaje.setUsuario(usuarioNew);
            }
            List<Dia> attachedDiaListNew = new ArrayList<Dia>();
            for (Dia diaListNewDiaToAttach : diaListNew) {
                diaListNewDiaToAttach = em.getReference(diaListNewDiaToAttach.getClass(), diaListNewDiaToAttach.getId());
                attachedDiaListNew.add(diaListNewDiaToAttach);
            }
            diaListNew = attachedDiaListNew;
            viaje.setDiaList(diaListNew);
            List<Foto> attachedFotoListNew = new ArrayList<Foto>();
            for (Foto fotoListNewFotoToAttach : fotoListNew) {
                fotoListNewFotoToAttach = em.getReference(fotoListNewFotoToAttach.getClass(), fotoListNewFotoToAttach.getId());
                attachedFotoListNew.add(fotoListNewFotoToAttach);
            }
            fotoListNew = attachedFotoListNew;
            viaje.setFotoList(fotoListNew);
            viaje = em.merge(viaje);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getViajeList().remove(viaje);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getViajeList().add(viaje);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Dia diaListNewDia : diaListNew) {
                if (!diaListOld.contains(diaListNewDia)) {
                    Viaje oldIdViajeOfDiaListNewDia = diaListNewDia.getIdViaje();
                    diaListNewDia.setIdViaje(viaje);
                    diaListNewDia = em.merge(diaListNewDia);
                    if (oldIdViajeOfDiaListNewDia != null && !oldIdViajeOfDiaListNewDia.equals(viaje)) {
                        oldIdViajeOfDiaListNewDia.getDiaList().remove(diaListNewDia);
                        oldIdViajeOfDiaListNewDia = em.merge(oldIdViajeOfDiaListNewDia);
                    }
                }
            }
            for (Foto fotoListNewFoto : fotoListNew) {
                if (!fotoListOld.contains(fotoListNewFoto)) {
                    Viaje oldIdViajeOfFotoListNewFoto = fotoListNewFoto.getIdViaje();
                    fotoListNewFoto.setIdViaje(viaje);
                    fotoListNewFoto = em.merge(fotoListNewFoto);
                    if (oldIdViajeOfFotoListNewFoto != null && !oldIdViajeOfFotoListNewFoto.equals(viaje)) {
                        oldIdViajeOfFotoListNewFoto.getFotoList().remove(fotoListNewFoto);
                        oldIdViajeOfFotoListNewFoto = em.merge(oldIdViajeOfFotoListNewFoto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = viaje.getId();
                if (findViaje(id) == null) {
                    throw new NonexistentEntityException("The viaje with id " + id + " no longer exists.");
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
            Viaje viaje;
            try {
                viaje = em.getReference(Viaje.class, id);
                viaje.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viaje with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Dia> diaListOrphanCheck = viaje.getDiaList();
            for (Dia diaListOrphanCheckDia : diaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Viaje (" + viaje + ") cannot be destroyed since the Dia " + diaListOrphanCheckDia + " in its diaList field has a non-nullable idViaje field.");
            }
            List<Foto> fotoListOrphanCheck = viaje.getFotoList();
            for (Foto fotoListOrphanCheckFoto : fotoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Viaje (" + viaje + ") cannot be destroyed since the Foto " + fotoListOrphanCheckFoto + " in its fotoList field has a non-nullable idViaje field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuario = viaje.getUsuario();
            if (usuario != null) {
                usuario.getViajeList().remove(viaje);
                usuario = em.merge(usuario);
            }
            em.remove(viaje);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Viaje> findViajeEntities() {
        return findViajeEntities(true, -1, -1);
    }

    public List<Viaje> findViajeEntities(int maxResults, int firstResult) {
        return findViajeEntities(false, maxResults, firstResult);
    }

    private List<Viaje> findViajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Viaje.class));
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

    public Viaje findViaje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Viaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getViajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Viaje> rt = cq.from(Viaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Viaje> dameListaViajesOrderFecha() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<List> query = em.createNamedQuery("Viaje.findAllOrderByFechaModificacion", List.class);
        List listaResultados = query.getResultList();
        return listaResultados;
    }
    
}
