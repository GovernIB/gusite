package es.caib.gusite.microback.actionform.busca;

/**
 * Formulario para las funcionalidades de búsqueda y ordenación en auditorias
 *
 * Created by tcerda on 16/12/2014.
 */
public class BuscaOrdenaAuditoriasActionForm extends BuscaOrdenaActionForm {

    private static final long serialVersionUID = 1810359670780473901L;

    public BuscaOrdenaAuditoriasActionForm() {}

    private String entity;
    private String idEntity;
    private String user;
    private String date;
    private String micro;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(String idEntity) {
        this.idEntity = idEntity;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMicro() {
        return micro;
    }

    public void setMicro(String micro) {
        this.micro = micro;
    }
}
