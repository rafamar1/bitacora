/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.dao;

import datos.dao.exceptions.IllegalOrphanException;
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
import datos.entidades.Viaje;
import datos.entidades.Opinion;
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
        if (usuario.getListaUsuarioSeguido() == null) {
            usuario.setListaUsuarioSeguido(new ArrayList<Usuario>());
        }
        if (usuario.getListaUsuarioTeSigue() == null) {
            usuario.setListaUsuarioTeSigue(new ArrayList<Usuario>());
        }
        if (usuario.getViajeList() == null) {
            usuario.setViajeList(new ArrayList<Viaje>());
        }
        if (usuario.getOpinionList() == null) {
            usuario.setOpinionList(new ArrayList<Opinion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuario> attachedListaUsuarioSeguido = new ArrayList<Usuario>();
            for (Usuario listaUsuarioSeguidoUsuarioToAttach : usuario.getListaUsuarioSeguido()) {
                listaUsuarioSeguidoUsuarioToAttach = em.getReference(listaUsuarioSeguidoUsuarioToAttach.getClass(), listaUsuarioSeguidoUsuarioToAttach.getNombreUsuario());
                attachedListaUsuarioSeguido.add(listaUsuarioSeguidoUsuarioToAttach);
            }
            usuario.setListaUsuarioSeguido(attachedListaUsuarioSeguido);
            List<Usuario> attachedListaUsuarioTeSigue = new ArrayList<Usuario>();
            for (Usuario listaUsuarioTeSigueUsuarioToAttach : usuario.getListaUsuarioTeSigue()) {
                listaUsuarioTeSigueUsuarioToAttach = em.getReference(listaUsuarioTeSigueUsuarioToAttach.getClass(), listaUsuarioTeSigueUsuarioToAttach.getNombreUsuario());
                attachedListaUsuarioTeSigue.add(listaUsuarioTeSigueUsuarioToAttach);
            }
            usuario.setListaUsuarioTeSigue(attachedListaUsuarioTeSigue);
            List<Viaje> attachedViajeList = new ArrayList<Viaje>();
            for (Viaje viajeListViajeToAttach : usuario.getViajeList()) {
                viajeListViajeToAttach = em.getReference(viajeListViajeToAttach.getClass(), viajeListViajeToAttach.getId());
                attachedViajeList.add(viajeListViajeToAttach);
            }
            usuario.setViajeList(attachedViajeList);
            List<Opinion> attachedOpinionList = new ArrayList<Opinion>();
            for (Opinion opinionListOpinionToAttach : usuario.getOpinionList()) {
                opinionListOpinionToAttach = em.getReference(opinionListOpinionToAttach.getClass(), opinionListOpinionToAttach.getId());
                attachedOpinionList.add(opinionListOpinionToAttach);
            }
            usuario.setOpinionList(attachedOpinionList);
            em.persist(usuario);
            for (Usuario listaUsuarioSeguidoUsuario : usuario.getListaUsuarioSeguido()) {
                listaUsuarioSeguidoUsuario.getListaUsuarioSeguido().add(usuario);
                listaUsuarioSeguidoUsuario = em.merge(listaUsuarioSeguidoUsuario);
            }
            for (Usuario listaUsuarioTeSigueUsuario : usuario.getListaUsuarioTeSigue()) {
                listaUsuarioTeSigueUsuario.getListaUsuarioSeguido().add(usuario);
                listaUsuarioTeSigueUsuario = em.merge(listaUsuarioTeSigueUsuario);
            }
            for (Viaje viajeListViaje : usuario.getViajeList()) {
                Usuario oldUsuarioOfViajeListViaje = viajeListViaje.getUsuario();
                viajeListViaje.setUsuario(usuario);
                viajeListViaje = em.merge(viajeListViaje);
                if (oldUsuarioOfViajeListViaje != null) {
                    oldUsuarioOfViajeListViaje.getViajeList().remove(viajeListViaje);
                    oldUsuarioOfViajeListViaje = em.merge(oldUsuarioOfViajeListViaje);
                }
            }
            for (Opinion opinionListOpinion : usuario.getOpinionList()) {
                Usuario oldIdUsuarioOfOpinionListOpinion = opinionListOpinion.getIdUsuario();
                opinionListOpinion.setIdUsuario(usuario);
                opinionListOpinion = em.merge(opinionListOpinion);
                if (oldIdUsuarioOfOpinionListOpinion != null) {
                    oldIdUsuarioOfOpinionListOpinion.getOpinionList().remove(opinionListOpinion);
                    oldIdUsuarioOfOpinionListOpinion = em.merge(oldIdUsuarioOfOpinionListOpinion);
                }
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

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getNombreUsuario());
            List<Usuario> listaUsuarioSeguidoOld = persistentUsuario.getListaUsuarioSeguido();
            List<Usuario> listaUsuarioSeguidoNew = usuario.getListaUsuarioSeguido();
            List<Usuario> listaUsuarioTeSigueOld = persistentUsuario.getListaUsuarioTeSigue();
            List<Usuario> listaUsuarioTeSigueNew = usuario.getListaUsuarioTeSigue();
            List<Viaje> viajeListOld = persistentUsuario.getViajeList();
            List<Viaje> viajeListNew = usuario.getViajeList();
            List<Opinion> opinionListOld = persistentUsuario.getOpinionList();
            List<Opinion> opinionListNew = usuario.getOpinionList();
            List<String> illegalOrphanMessages = null;
            for (Viaje viajeListOldViaje : viajeListOld) {
                if (!viajeListNew.contains(viajeListOldViaje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Viaje " + viajeListOldViaje + " since its usuario field is not nullable.");
                }
            }
            for (Opinion opinionListOldOpinion : opinionListOld) {
                if (!opinionListNew.contains(opinionListOldOpinion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Opinion " + opinionListOldOpinion + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuario> attachedListaUsuarioSeguidoNew = new ArrayList<Usuario>();
            for (Usuario listaUsuarioSeguidoNewUsuarioToAttach : listaUsuarioSeguidoNew) {
                listaUsuarioSeguidoNewUsuarioToAttach = em.getReference(listaUsuarioSeguidoNewUsuarioToAttach.getClass(), listaUsuarioSeguidoNewUsuarioToAttach.getNombreUsuario());
                attachedListaUsuarioSeguidoNew.add(listaUsuarioSeguidoNewUsuarioToAttach);
            }
            listaUsuarioSeguidoNew = attachedListaUsuarioSeguidoNew;
            usuario.setListaUsuarioSeguido(listaUsuarioSeguidoNew);
            List<Usuario> attachedListaUsuarioTeSigueNew = new ArrayList<Usuario>();
            for (Usuario listaUsuarioTeSigueNewUsuarioToAttach : listaUsuarioTeSigueNew) {
                listaUsuarioTeSigueNewUsuarioToAttach = em.getReference(listaUsuarioTeSigueNewUsuarioToAttach.getClass(), listaUsuarioTeSigueNewUsuarioToAttach.getNombreUsuario());
                attachedListaUsuarioTeSigueNew.add(listaUsuarioTeSigueNewUsuarioToAttach);
            }
            listaUsuarioTeSigueNew = attachedListaUsuarioTeSigueNew;
            usuario.setListaUsuarioTeSigue(listaUsuarioTeSigueNew);
            List<Viaje> attachedViajeListNew = new ArrayList<Viaje>();
            for (Viaje viajeListNewViajeToAttach : viajeListNew) {
                viajeListNewViajeToAttach = em.getReference(viajeListNewViajeToAttach.getClass(), viajeListNewViajeToAttach.getId());
                attachedViajeListNew.add(viajeListNewViajeToAttach);
            }
            viajeListNew = attachedViajeListNew;
            usuario.setViajeList(viajeListNew);
            List<Opinion> attachedOpinionListNew = new ArrayList<Opinion>();
            for (Opinion opinionListNewOpinionToAttach : opinionListNew) {
                opinionListNewOpinionToAttach = em.getReference(opinionListNewOpinionToAttach.getClass(), opinionListNewOpinionToAttach.getId());
                attachedOpinionListNew.add(opinionListNewOpinionToAttach);
            }
            opinionListNew = attachedOpinionListNew;
            usuario.setOpinionList(opinionListNew);
            usuario = em.merge(usuario);
            for (Usuario listaUsuarioSeguidoOldUsuario : listaUsuarioSeguidoOld) {
                if (!listaUsuarioSeguidoNew.contains(listaUsuarioSeguidoOldUsuario)) {
                    listaUsuarioSeguidoOldUsuario.getListaUsuarioSeguido().remove(usuario);
                    listaUsuarioSeguidoOldUsuario = em.merge(listaUsuarioSeguidoOldUsuario);
                }
            }
            for (Usuario listaUsuarioSeguidoNewUsuario : listaUsuarioSeguidoNew) {
                if (!listaUsuarioSeguidoOld.contains(listaUsuarioSeguidoNewUsuario)) {
                    listaUsuarioSeguidoNewUsuario.getListaUsuarioSeguido().add(usuario);
                    listaUsuarioSeguidoNewUsuario = em.merge(listaUsuarioSeguidoNewUsuario);
                }
            }
            for (Usuario listaUsuarioTeSigueOldUsuario : listaUsuarioTeSigueOld) {
                if (!listaUsuarioTeSigueNew.contains(listaUsuarioTeSigueOldUsuario)) {
                    listaUsuarioTeSigueOldUsuario.getListaUsuarioSeguido().remove(usuario);
                    listaUsuarioTeSigueOldUsuario = em.merge(listaUsuarioTeSigueOldUsuario);
                }
            }
            for (Usuario listaUsuarioTeSigueNewUsuario : listaUsuarioTeSigueNew) {
                if (!listaUsuarioTeSigueOld.contains(listaUsuarioTeSigueNewUsuario)) {
                    listaUsuarioTeSigueNewUsuario.getListaUsuarioSeguido().add(usuario);
                    listaUsuarioTeSigueNewUsuario = em.merge(listaUsuarioTeSigueNewUsuario);
                }
            }
            for (Viaje viajeListNewViaje : viajeListNew) {
                if (!viajeListOld.contains(viajeListNewViaje)) {
                    Usuario oldUsuarioOfViajeListNewViaje = viajeListNewViaje.getUsuario();
                    viajeListNewViaje.setUsuario(usuario);
                    viajeListNewViaje = em.merge(viajeListNewViaje);
                    if (oldUsuarioOfViajeListNewViaje != null && !oldUsuarioOfViajeListNewViaje.equals(usuario)) {
                        oldUsuarioOfViajeListNewViaje.getViajeList().remove(viajeListNewViaje);
                        oldUsuarioOfViajeListNewViaje = em.merge(oldUsuarioOfViajeListNewViaje);
                    }
                }
            }
            for (Opinion opinionListNewOpinion : opinionListNew) {
                if (!opinionListOld.contains(opinionListNewOpinion)) {
                    Usuario oldIdUsuarioOfOpinionListNewOpinion = opinionListNewOpinion.getIdUsuario();
                    opinionListNewOpinion.setIdUsuario(usuario);
                    opinionListNewOpinion = em.merge(opinionListNewOpinion);
                    if (oldIdUsuarioOfOpinionListNewOpinion != null && !oldIdUsuarioOfOpinionListNewOpinion.equals(usuario)) {
                        oldIdUsuarioOfOpinionListNewOpinion.getOpinionList().remove(opinionListNewOpinion);
                        oldIdUsuarioOfOpinionListNewOpinion = em.merge(oldIdUsuarioOfOpinionListNewOpinion);
                    }
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

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Viaje> viajeListOrphanCheck = usuario.getViajeList();
            for (Viaje viajeListOrphanCheckViaje : viajeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Viaje " + viajeListOrphanCheckViaje + " in its viajeList field has a non-nullable usuario field.");
            }
            List<Opinion> opinionListOrphanCheck = usuario.getOpinionList();
            for (Opinion opinionListOrphanCheckOpinion : opinionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Opinion " + opinionListOrphanCheckOpinion + " in its opinionList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuario> listaUsuarioSeguido = usuario.getListaUsuarioSeguido();
            for (Usuario listaUsuarioSeguidoUsuario : listaUsuarioSeguido) {
                listaUsuarioSeguidoUsuario.getListaUsuarioSeguido().remove(usuario);
                listaUsuarioSeguidoUsuario = em.merge(listaUsuarioSeguidoUsuario);
            }
            List<Usuario> listaUsuarioTeSigue = usuario.getListaUsuarioTeSigue();
            for (Usuario listaUsuarioTeSigueUsuario : listaUsuarioTeSigue) {
                listaUsuarioTeSigueUsuario.getListaUsuarioSeguido().remove(usuario);
                listaUsuarioTeSigueUsuario = em.merge(listaUsuarioTeSigueUsuario);
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
