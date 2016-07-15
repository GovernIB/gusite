package es.caib.gusite.micromodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;


/**
 * Representación de un SolrPendiente Job. Modela la tabla de BBDD GUS_SOLJOB.
 * 
 * @author Indra
 */

@Entity
@Table(name = "GUS_SOLJOB")
public class SolrPendienteJob implements ValueObject {
	
	/** Serial version UID. **/
	private static final long serialVersionUID = 1L;
	
			
	/** Id. **/
	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_SOLJOB_GENERATOR", sequenceName = "GUS_SEQSOLJ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_SOLJOB_GENERATOR")
	@Column(name = "JOB_ID")
	private Long id;
	
	/** Fecha de inicio. **/
	@Column(name = "JOB_FECINI")
	private Date fechaIni;
	
	/** Fecha de fin. **/
	@Column(name = "JOB_FECFIN")
	private Date fechaFin;
	
	/** Tipo. IDX_TODO = INDEXAR TODO, IDX_UA = INDEXAR POR UNID. ADMINISTRATIVA, IDX_PDT = INDEXAR PENDIENTE, IDX_MIC = INDEXAR POR MICROSITE. **/
	@Column(name = "JOB_TIPO")
	private String tipo;
	
	
	/**
	 * @return the id
	 */
	public final Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public final void setId(final Long id) {
		this.id = id;
	}
	
	
	
	/**
	 * @return the fechaIni
	 */
	public final Date getFechaIni() {
		return fechaIni;
	}
	/**
	 * @param fechaIni the fechaIni to set
	 */
	public final void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	
	/**
	 * @return the fechaFin
	 */
	public final Date getFechaFin() {
		return fechaFin;
	}
	/**
	 * @param fechaFin the fechaFin to set
	 */
	public final void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		StringBuffer texto = new StringBuffer();
		texto.append("SolrPendienteJob id:");
		texto.append(fechaIni);
		texto.append(" fechaIni:");
		texto.append(fechaFin);
		texto.append(" fechaFin:");
		texto.append(tipo);
		texto.append(" tipo:");
		return texto.toString();
	}
	
}
