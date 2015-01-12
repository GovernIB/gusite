package org.ibit.rol.sac.micromodel;

/**
 * Clase RespuestaDato. Bean que define el dato de una respuesta. 
 * Hay respuestas en las cuales el usuario puede introducir información, esta información es el dato.
 * Modela la tabla de BBDD GUS_ENCVOT.
 * @author Indra
 */
public class RespuestaDato implements ValueObject {


		private static final long serialVersionUID = 2552194475264149738L;
		private Long id;
		private Long idencuesta;	
		private Long idpregunta;
		private Long idrespueta;
		private String dato;
		private Long idusuari;
		
		public String getDato() {
			return dato;
		}
		public void setDato(String dato) {
			this.dato = dato;
		}
		public Long getIdencuesta() {
			return idencuesta;
		}
		public void setIdencuesta(Long idencuesta) {
			this.idencuesta = idencuesta;
		}
		public Long getIdpregunta() {
			return idpregunta;
		}
		public void setIdpregunta(Long idpregunta) {
			this.idpregunta = idpregunta;
		}
		public Long getIdrespueta() {
			return idrespueta;
		}
		public void setIdrespueta(Long idrespueta) {
			this.idrespueta = idrespueta;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public void setIdusuari(Long idusuari) {
			this.idusuari = idusuari;
		}
		public Long getIdusuari() {
			return idusuari;
		}
		
}
