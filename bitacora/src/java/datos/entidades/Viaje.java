/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rafa
 */
@Entity
@Table(name = "viaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viaje.findAll", query = "SELECT v FROM Viaje v")
    , @NamedQuery(name = "Viaje.findAllOrderByFechaModificacion", query = "SELECT v FROM Viaje v order by v.fechaModificacion desc")
    , @NamedQuery(name = "Viaje.findById", query = "SELECT v FROM Viaje v WHERE v.id = :id")
    , @NamedQuery(name = "Viaje.findByTitulo", query = "SELECT v FROM Viaje v WHERE v.titulo = :titulo")
    , @NamedQuery(name = "Viaje.findByDescripcion", query = "SELECT v FROM Viaje v WHERE v.descripcion = :descripcion")
    , @NamedQuery(name = "Viaje.findByImgMiniatura", query = "SELECT v FROM Viaje v WHERE v.imgMiniatura = :imgMiniatura")
    , @NamedQuery(name = "Viaje.findByFechaCreacion", query = "SELECT v FROM Viaje v WHERE v.fechaCreacion = :fechaCreacion")
    , @NamedQuery(name = "Viaje.findByFechaModificacion", query = "SELECT v FROM Viaje v WHERE v.fechaModificacion = :fechaModificacion")})
public class Viaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "img_miniatura")
    private String imgMiniatura;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @JoinColumn(name = "usuario", referencedColumnName = "nombre_usuario")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idViaje")
    private List<Dia> diaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idViaje")
    private List<Foto> fotoList;    

    public Viaje() {
    }

    public Viaje(Integer id) {
        this.id = id;
    }

    public Viaje(Integer id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImgMiniatura() {
        return imgMiniatura;
    }

    public void setImgMiniatura(String imgMiniatura) {
        this.imgMiniatura = imgMiniatura;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
    
    @XmlTransient
    public List<Dia> getDiaList() {
        return diaList;
    }

    public void setDiaList(List<Dia> diaList) {
        this.diaList = diaList;
    }

    @XmlTransient
    public List<Foto> getFotoList() {
        return fotoList;
    }

    public void setFotoList(List<Foto> fotoList) {
        this.fotoList = fotoList;
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
        if (!(object instanceof Viaje)) {
            return false;
        }
        Viaje other = (Viaje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "datos.entidades.Viaje[ id=" + id + " ]";
    }
    
}
