package es.caib.gusite.micromodel;

import javax.persistence.*;
import java.util.*;

/**
 * Clase Auditoria. Bean que define una Auditoria.
 * Modela la tabla de BBDD GUS_MENU
 * @author Brujula
 */
@Entity
@Table(name="GUS_AUDITORIA")
public class Auditoria {

    public static int CREAR = 0;
    public static int MODIFICAR = 1;
    public static int ELIMINAR = 2;

    @Id
    @SequenceGenerator(name="GUS_AUDT_ID_GENERATOR", sequenceName="GUS_SEQMEN", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GUS_AUDT_ID_GENERATOR")
    @Column(name="AUD_CODI")
    private Long id;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="AUD_MICCOD")
    private Microsite microsite;

    @Column(name="AUD_USUARI")
    private String usuario;

    @Column(name="AUD_FECHA")
    private Date fecha;

    @Column(name="AUD_OPERACION")
    private int operacion;

    @Column(name="AUD_ENTIDAD")
    private String entidad;

    @Column(name="AUD_INFORMACION")
    private byte[] informacion;

    @Column(name="AUD_ID_ENTIDAD")
    private String idEntidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Microsite getMicrosite() {
        return microsite;
    }

    public void setMicrosite(Microsite microsite) {
        this.microsite = microsite;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getOperacion() {
        return operacion;
    }

    public void setOperacion(int operacion) {
        this.operacion = operacion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public byte[] getInformacion() {
        return informacion;
    }

    public void setInformacion(byte[] informacion) {
        this.informacion = informacion;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }
}
