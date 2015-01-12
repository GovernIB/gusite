package es.caib.gusite.lucene.model;

import java.io.Serializable;

public class ModelIndexObject implements Serializable {

    public static final float DEFAULT_BOOST = 1F;
    private String id;
    private String clasificacion;
    private String titulo;
    private String tituloserviciomain;
    private String uo;
    private String seccion;
    private Long familia;
    private String materia;
    private Long micro;
    private Long foro;
    private String publicacion;
    private String caducidad;
    private String restringido;
    private String conforo;
    private String text;
    private String textopcional;
    private String url;
    private String descripcion;
    private float boost;


    public ModelIndexObject() {
        text = "";
        textopcional = "";
        boost = 1.0F;
    }

    public float getBoost() {
        return boost;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Long getMicro() {
        return micro;
    }

    public void setMicro(Long micro) {
        this.micro = micro;
    }

    public String getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(String publicacion) {
        this.publicacion = publicacion;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFamilia() {
        return familia;
    }

    public void setFamilia(Long familia) {
        this.familia = familia;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String tiposervicio) {
        clasificacion = tiposervicio;
    }

    public String getTituloserviciomain() {
        return tituloserviciomain;
    }

    public void setTituloserviciomain(String tituloserciciomain) {
        tituloserviciomain = tituloserciciomain;
    }

    public String getTextopcional() {
        return textopcional;
    }

    public void setTextopcional(String textopcional) {
        this.textopcional = textopcional;
    }

    public void addTextLine(String line) {
        if (line != null && line.length() > 0)
            text = (new StringBuilder()).append(text).append(line).append("\n").toString();
    }

    public void addTextopcionalLine(String line) {
        if (line != null && line.length() > 0)
            textopcional = (new StringBuilder()).append(textopcional).append(line).append("\n").toString();
    }

    public void addDescripcionLine(String line) {
        if (line != null && line.length() > 0)
            descripcion = (new StringBuilder()).append(descripcion).append(line).append("\n").toString();
    }

    public String getRestringido() {
        return restringido;
    }

    public void setRestringido(String restringido) {
        this.restringido = restringido;
    }

    public String getConforo() {
        return conforo;
    }

    public void setConforo(String conforo) {
        this.conforo = conforo;
    }

    public Long getForo() {
        return foro;
    }

    public void setForo(Long foro) {
        this.foro = foro;
    }

}
