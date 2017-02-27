package es.caib.gusite.solrutiles.solr.model;

public class IndexEncontrado {
	 private String id;
	    private String titulo;
	    private String descripcion;
	    private String site;
	    private String url;
	    private float score;
	    private String tiposervicio;
	    private String familia;
	    private String seccion;
	    private String materia;
	    private String uo;
	    private String conforo;
	    private String tituloserviciomain;
	    private boolean disponible;


	    public IndexEncontrado(String id, String tiposervicio, String site, String familia, String seccion, String materia, String uo, String conforo, String titulo, String descripcion,
	        String tituloserviciomain, String url, float score,boolean disponible) {
	        this.id = id;
	        this.titulo = titulo;
	        this.descripcion = descripcion;
	        this.site = site;
	        this.url = url;
	        this.score = score;
	        this.tiposervicio = tiposervicio;
	        this.familia = familia;
	        this.seccion = seccion;
	        this.materia = materia;
	        this.uo = uo;
	        this.conforo = conforo;
	        this.tituloserviciomain = tituloserviciomain;
	        this.setDisponible(disponible);
	    }

	    public IndexEncontrado(String id, String titulo, String descripcion, String site, String url, float score, boolean disponible) {
	        this(id, null, site, null, null, null, null, null, titulo, descripcion, null, url, score,disponible);
	    }

	    public String getId() {
	        return id;
	    }

	    public void setId(String id) {
	        this.id = id;
	    }

	    public float getScore() {
	        return score;
	    }

	    public void setScore(float score) {
	        this.score = score;
	    }

	    public String getSite() {
	        return site;
	    }

	    public void setSite(String site) {
	        this.site = site;
	    }

	    public String getTitulo() {
	        return titulo;
	    }

	    public void setTitulo(String titulo) {
	        this.titulo = titulo;
	    }

	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	    public String getDescripcion() {
	        return descripcion;
	    }

	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
	    }

	    public String getFamilia() {
	        return familia;
	    }

	    public void setFamilia(String familia) {
	        this.familia = familia;
	    }

	    public String getMateria() {
	        return materia;
	    }

	    public void setMateria(String materia) {
	        this.materia = materia;
	    }

	    public String getSeccion() {
	        return seccion;
	    }

	    public void setSeccion(String seccion) {
	        this.seccion = seccion;
	    }

	    public String getTiposervicio() {
	        return tiposervicio;
	    }

	    public void setTiposervicio(String tiposervicio) {
	        this.tiposervicio = tiposervicio;
	    }

	    public String getTituloserviciomain() {
	        return tituloserviciomain;
	    }

	    public void setTituloserviciomain(String tituloserviciomain) {
	        this.tituloserviciomain = tituloserviciomain;
	    }

	    public String getUo() {
	        return uo;
	    }

	    public void setUo(String uo) {
	        this.uo = uo;
	    }

	    public String getConforo() {
	        return conforo;
	    }

	    public void setConforo(String conforo) {
	        this.conforo = conforo;
	    }
	    
	    public boolean isDisponible() {
			return disponible;
		}

		public void setDisponible(boolean disponible) {
			this.disponible = disponible;
		}

		public String getStringValores(){
	    	
	    	try {
				return "\n-id          :"+ getString(id) +", " 
	    		+"\n-titulo      :"+ getString(titulo)+ ", "
				+"\n-descripcion :"+ getString(descripcion)+", "
				+"\n-site        :"+ getString(site) +", "
				+"\n-url         :"+ getString(url)+", "    
				+"\n-tiposervicio:"+ getString(tiposervicio)+", "
				+"\n-familia     :"+ getString(familia)+", "
				+"\n-seccion     :"+ getString(seccion)+", "
				+"\n-materia     :"+ getString(materia)+", "
				+"\n-uo          :"+ getString(uo)+", "
				+"\n-tituloserviciomain:"+ getString(tituloserviciomain)+", "
				+"\n-score       :"+ getFloat(score)+", "
				+"\n-conforo     :"+ getString(conforo)+". ";
			} catch (Exception e) {
				return "Error al recuperar los datos del IndexEncontrado:" + e.getMessage();
			}
	    	
	    }
	    
	    private String getString(String value){
	    	String res = "null";
	    	if(value!=null){
	    		res=value;
	    	}
	    	return res;
	    }
	    
	    private String getFloat(float value){
	    	String res = "null";
		    try {
				res = String.valueOf(value);
			} catch (Exception e) {
				res = "??";
			}
	    	return res;
	    }
}
