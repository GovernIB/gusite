package es.caib.gusite.micromodel;

import java.util.Date;

/**
 * Created by tcerda on 13/01/2015.
 */
public class AuditoriaNombre {

    public static int CREAR = 0;
    public static int MODIFICAR = 1;
    public static int ELIMINAR = 2;

    private String usuario;

    private Date fecha;

    private String operacion;

    private String entidad;

    private String idEntidad;

    public AuditoriaNombre(Auditoria auditoria) {
        this.usuario = auditoria.getUsuario();
        this.fecha = auditoria.getFecha();
        this.operacion = auditoria.estado(auditoria.getOperacion());
        this.entidad = auditoria.getEntidad();
        this.idEntidad = auditoria.getIdEntidad();
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

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(String idEntidad) {
        this.idEntidad = idEntidad;
    }
}
