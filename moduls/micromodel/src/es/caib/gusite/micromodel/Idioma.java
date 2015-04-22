package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Clase Idioma. Bean que define un idioma. Modela la tabla de BBDD GUS_IDIOMA
 * 
 * @author Indra
 */
@Entity
@Table(name = "GUS_IDIOMA")
public class Idioma implements ValueObject {

	private static final long serialVersionUID = -3185829276338530825L;

	/**
	 * Obtenemos la propiedad de sistema a través de un método ya que, si la
	 * almacenamos en una constante (final y estática) se quedará con el primer
	 * valor que tenga esa propiedad de sistema y, si éste cambia, no se
	 * actualizará.
	 * 
	 * @return String con el código de idioma por defecto de la aplicación.
	 */
	public static String getIdiomaPorDefecto() {

		String idiomaDefault = System
				.getProperty("es.caib.gusite.idioma.default");

		if (idiomaDefault == null || idiomaDefault.length() == 0) {
			idiomaDefault = "ca";
			// throw new
			// RuntimeException("No se estableció la propiedad de sistema es.caib.gusite.idioma.default");
		}

		return idiomaDefault;

	}

	@Id
	@Column(name = "IDI_CODI")
	private String lang;

	@Column(name = "IDI_CODEST")
	private String codigoEstandar;

	@Column(name = "IDI_ORDEN")
	private Integer orden;

	@Column(name = "IDI_NOMBRE")
	private String nombre;

	@Column(name = "IDI_TRADUCTOR")
	private String langTraductor;

	public Idioma() {
	}

	public Idioma(String idioma) {
		this.setCodigoEstandar(idioma);
		this.setLang(idioma);
		this.setNombre(idioma);
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCodigoEstandar() {
		return this.codigoEstandar;
	}

	public void setCodigoEstandar(String codigoEstandar) {
		this.codigoEstandar = codigoEstandar;
	}

	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLangTraductor() {
		return this.langTraductor;
	}

	public void setLangTraductor(String langTraductor) {
		this.langTraductor = langTraductor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Idioma other = (Idioma) obj;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (other.lang == null) {
			return false;
		} else if (!lang.toLowerCase().equals(other.lang.toLowerCase()))
			return false;
		return true;
	}

	/*
	 * TODO amartin: bidireccionalidad que dejamos comentada porque no aparecía
	 * en el HBM y da problemas en la exportación
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="ATI_CODIDI") private Set<TraduccionActividadagenda>
	 * traduccionesActividadesAgenda;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="AGE_CODIDI") private Set<TraduccionAgenda>
	 * traduccionesAgendas;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="CPI_CODIDI") private Set<TraduccionComponente>
	 * traduccionesComponentes;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="CID_CODIDI") private Set<TraduccionContenido>
	 * traduccionesContenidos;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="EID_CODIDI") private Set<TraduccionEncuesta>
	 * traduccionesEncuestas;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="FID_CODIDI") private Set<TraduccionFaq>
	 * traduccionesFaqs;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="RID_CODIDI") private Set<TraduccionLineadatocontacto>
	 * traduccionesLineasdatocontacto;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="FQI_CODIDI") private Set<TraduccionFrqssi>
	 * traduccionesFrqssi;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="MID_CODIDI") private Set<TraduccionMicrosite>
	 * traduccionesMicrosites;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="MDI_CODIDI") private Set<TraduccionMenu>
	 * traduccionesMenus;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="NID_CODIDI") private Set<TraduccionNoticia>
	 * traduccionesNoticias;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="PID_CODIDI") private Set<TraduccionPregunta>
	 * traduccionesPreguntas;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="REI_CODIDI") private Set<TraduccionRespuesta>
	 * traduccionesRespuestas;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="TID_CODIDI") private Set<TraduccionTemafaq>
	 * traduccionesTemafaq;
	 * 
	 * @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="TPI_CODIDI") private Set<TraduccionTipo>
	 * traduccionesTipos;
	 * 
	 * public Set<TraduccionActividadagenda> getTraduccionesActividadesAgenda()
	 * { return traduccionesActividadesAgenda; }
	 * 
	 * public void setTraduccionesActividadesAgenda(
	 * Set<TraduccionActividadagenda> traduccionesActividadesAgenda) {
	 * this.traduccionesActividadesAgenda = traduccionesActividadesAgenda; }
	 * 
	 * public Set<TraduccionAgenda> getTraduccionesAgendas() { return
	 * traduccionesAgendas; }
	 * 
	 * public void setTraduccionesAgendas(Set<TraduccionAgenda>
	 * traduccionesAgendas) { this.traduccionesAgendas = traduccionesAgendas; }
	 * 
	 * public Set<TraduccionComponente> getTraduccionesComponentes() { return
	 * traduccionesComponentes; }
	 * 
	 * public void setTraduccionesComponentes( Set<TraduccionComponente>
	 * traduccionesComponentes) { this.traduccionesComponentes =
	 * traduccionesComponentes; }
	 * 
	 * public Set<TraduccionContenido> getTraduccionesContenidos() { return
	 * traduccionesContenidos; }
	 * 
	 * public void setTraduccionesContenidos( Set<TraduccionContenido>
	 * traduccionesContenidos) { this.traduccionesContenidos =
	 * traduccionesContenidos; }
	 * 
	 * public Set<TraduccionEncuesta> getTraduccionesEncuestas() { return
	 * traduccionesEncuestas; }
	 * 
	 * public void setTraduccionesEncuestas( Set<TraduccionEncuesta>
	 * traduccionesEncuestas) { this.traduccionesEncuestas =
	 * traduccionesEncuestas; }
	 * 
	 * public Set<TraduccionFaq> getTraduccionesFaqs() { return
	 * traduccionesFaqs; }
	 * 
	 * public void setTraduccionesFaqs(Set<TraduccionFaq> traduccionesFaqs) {
	 * this.traduccionesFaqs = traduccionesFaqs; }
	 * 
	 * public Set<TraduccionLineadatocontacto>
	 * getTraduccionesLineasdatocontacto() { return
	 * traduccionesLineasdatocontacto; }
	 * 
	 * public void setTraduccionesLineasdatocontacto(
	 * Set<TraduccionLineadatocontacto> traduccionesLineasdatocontacto) {
	 * this.traduccionesLineasdatocontacto = traduccionesLineasdatocontacto; }
	 * 
	 * public Set<TraduccionFrqssi> getTraduccionesFrqssi() { return
	 * traduccionesFrqssi; }
	 * 
	 * public void setTraduccionesFrqssi(Set<TraduccionFrqssi>
	 * traduccionesFrqssi) { this.traduccionesFrqssi = traduccionesFrqssi; }
	 * 
	 * public Set<TraduccionMicrosite> getTraduccionesMicrosites() { return
	 * traduccionesMicrosites; }
	 * 
	 * public void setTraduccionesMicrosites( Set<TraduccionMicrosite>
	 * traduccionesMicrosites) { this.traduccionesMicrosites =
	 * traduccionesMicrosites; }
	 * 
	 * public Set<TraduccionMenu> getTraduccionesMenus() { return
	 * traduccionesMenus; }
	 * 
	 * public void setTraduccionesMenus(Set<TraduccionMenu> traduccionesMenus) {
	 * this.traduccionesMenus = traduccionesMenus; }
	 * 
	 * public Set<TraduccionNoticia> getTraduccionesNoticias() { return
	 * traduccionesNoticias; }
	 * 
	 * public void setTraduccionesNoticias(Set<TraduccionNoticia>
	 * traduccionesNoticias) { this.traduccionesNoticias = traduccionesNoticias;
	 * }
	 * 
	 * public Set<TraduccionPregunta> getTraduccionesPreguntas() { return
	 * traduccionesPreguntas; }
	 * 
	 * public void setTraduccionesPreguntas( Set<TraduccionPregunta>
	 * traduccionesPreguntas) { this.traduccionesPreguntas =
	 * traduccionesPreguntas; }
	 * 
	 * public Set<TraduccionRespuesta> getTraduccionesRespuestas() { return
	 * traduccionesRespuestas; }
	 * 
	 * public void setTraduccionesRespuestas( Set<TraduccionRespuesta>
	 * traduccionesRespuestas) { this.traduccionesRespuestas =
	 * traduccionesRespuestas; }
	 * 
	 * public Set<TraduccionTemafaq> getTraduccionesTemafaq() { return
	 * traduccionesTemafaq; }
	 * 
	 * public void setTraduccionesTemafaq(Set<TraduccionTemafaq>
	 * traduccionesTemafaq) { this.traduccionesTemafaq = traduccionesTemafaq; }
	 * 
	 * public Set<TraduccionTipo> getTraduccionesTipos() { return
	 * traduccionesTipos; }
	 * 
	 * public void setTraduccionesTipos(Set<TraduccionTipo> traduccionesTipos) {
	 * this.traduccionesTipos = traduccionesTipos; }
	 */

}