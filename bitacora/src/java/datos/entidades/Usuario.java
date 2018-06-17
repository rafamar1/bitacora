/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datos.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rafa
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByNombreUsuario", query = "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    , @NamedQuery(name = "Usuario.findByPassword", query = "SELECT u FROM Usuario u WHERE u.password = :password")
    , @NamedQuery(name = "Usuario.findByNombreCompleto", query = "SELECT u FROM Usuario u WHERE u.nombreCompleto = :nombreCompleto")
    , @NamedQuery(name = "Usuario.findByEmail", query = "SELECT u FROM Usuario u WHERE u.email = :email")
    , @NamedQuery(name = "Usuario.findByPublico", query = "SELECT u FROM Usuario u WHERE u.publico = :publico")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombre_usuario")
    private String nombreUsuario;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Column(name = "publico")
    private Integer publico;    
    @Column(name = "img_perfil")
    private String imgPerfil;
    @Column(name = "img_portada")
    private String imgPortada;
    @JoinTable(name = "usuario_sigue",
            joinColumns = {@JoinColumn(name = "usuario", referencedColumnName = "nombre_usuario")},
            inverseJoinColumns = {@JoinColumn(name = "usuario_seguido", referencedColumnName = "nombre_usuario")})
    @ManyToMany
    private List<Usuario> listaUsuarioSeguido;
    @ManyToMany(mappedBy = "listaUsuarioSeguido")
    private List<Usuario> listaUsuarioTeSigue;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Viaje> viajeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<Opinion> opinionList;

    public Usuario() {
    }

    public Usuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Usuario(String nombreUsuario, String password, String nombreCompleto, String email) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPublico() {
        return publico;
    }

    public void setPublico(Integer publico) {
        this.publico = publico;
    }

    public String getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public String getImgPortada() {
        return imgPortada;
    }

    public void setImgPortada(String imgPortada) {
        this.imgPortada = imgPortada;
    }

    @XmlTransient
    public List<Usuario> getListaUsuarioSeguido() {
        return listaUsuarioSeguido;
    }

    public void setListaUsuarioSeguido(List<Usuario> listaUsuarioSeguido) {
        this.listaUsuarioSeguido = listaUsuarioSeguido;
    }

    @XmlTransient
    public List<Usuario> getListaUsuarioTeSigue() {
        return listaUsuarioTeSigue;
    }

    public void setListaUsuarioTeSigue(List<Usuario> listaUsuarioTeSigue) {
        this.listaUsuarioTeSigue = listaUsuarioTeSigue;
    }

    @XmlTransient
    public List<Viaje> getViajeList() {
        return viajeList;
    }

    public void setViajeList(List<Viaje> viajeList) {
        this.viajeList = viajeList;
    }

    @XmlTransient
    public List<Opinion> getOpinionList() {
        return opinionList;
    }

    public void setOpinionList(List<Opinion> opinionList) {
        this.opinionList = opinionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreUsuario != null ? nombreUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.nombreUsuario == null && other.nombreUsuario != null) || (this.nombreUsuario != null && !this.nombreUsuario.equals(other.nombreUsuario)));
    }  

    @Override
    public String toString() {
        return "datos.entidades.Usuario[ nombreUsuario=" + nombreUsuario + " ]";
    }
    
}
