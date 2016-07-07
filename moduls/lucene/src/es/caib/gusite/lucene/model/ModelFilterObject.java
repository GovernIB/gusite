package es.caib.gusite.lucene.model;

import java.util.Hashtable;


public class ModelFilterObject {

    private String uo_id;
    private Long familia_id;
    private Long microsite_id;
    private Long foro_id;
    private String materia_id;
    private String seccion_id;
    private String restringido;
    private String buscador;
    private Hashtable traduccion;


    public ModelFilterObject() {
        traduccion = new Hashtable();
    }

    public Long getFamilia_id() {
        return familia_id;
    }

    public void setFamilia_id(Long familia_id) {
        this.familia_id = familia_id;
    }

    public String getMateria_id() {
        return materia_id;
    }

    public void setMateria_id(String materia_id) {
        this.materia_id = materia_id;
    }

    public Long getMicrosite_id() {
        return microsite_id;
    }

    public void setMicrosite_id(Long microsite_id) {
        this.microsite_id = microsite_id;
    }

    public String getUo_id() {
        return uo_id;
    }

    public void setUo_id(String uo_id) {
        this.uo_id = uo_id;
    }

    public String getSeccion_id() {
        return seccion_id;
    }

    public void setSeccion_id(String seccion_id) {
        this.seccion_id = seccion_id;
    }

    public void addTraduccion(String idioma, TraModelFilterObject trafilter) {
        traduccion.put(idioma, trafilter);
    }

    public TraModelFilterObject getTraduccion(String idioma) {
        return (TraModelFilterObject) traduccion.get(idioma);
    }

    public String getBuscador() {
        return buscador;
    }

    public void setBuscador(String buscador) {
        this.buscador = buscador;
    }

    public String getRestringido() {
        return restringido;
    }

    public void setRestringido(String restringido) {
        this.restringido = restringido;
    }

    public Long getForo_id() {
        return foro_id;
    }

    public void setForo_id(Long foro_id) {
        this.foro_id = foro_id;
    }

}
