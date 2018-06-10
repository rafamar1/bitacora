/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Rafa
 */
@Entity
@Table(name = "opinion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Opinion.findAll", query = "SELECT o FROM Opinion o")
    , @NamedQuery(name = "Opinion.findById", query = "SELECT o FROM Opinion o WHERE o.id = :id")
    , @NamedQuery(name = "Opinion.findByPuntuacion", query = "SELECT o FROM Opinion o WHERE o.puntuacion = :puntuacion")
    , @NamedQuery(name = "Opinion.findByComentario", query = "SELECT o FROM Opinion o WHERE o.comentario = :comentario")})
public class Opinion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "puntuacion")
    private int puntuacion;
    @Basic(optional = false)
    @Column(name = "comentario")
    private String comentario;
    @JoinColumn(name = "id_entrada", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Entrada idEntrada;
    @JoinColumn(name = "id_usuario", referencedColumnName = "nombre_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Opinion() {
    }

    public Opinion(Integer id) {
        this.id = id;
    }

    public Opinion(Integer id, int puntuacion, String comentario) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Entrada getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(Entrada idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Opinion)) {
            return false;
        }
        Opinion other = (Opinion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.entidades.Opinion[ id=" + id + " ]";
    }
    
}
