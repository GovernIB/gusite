package es.caib.gusite.micromodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
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
	
	/** Tipo. IDX_TODO = INDEXAR TODO, IDX_TSI = INDEXAR TODO SIN INDEXAR, IDX_UA = INDEXAR POR UNID. ADMINISTRATIVA, IDX_PDT = INDEXAR PENDIENTE, IDX_MIC = INDEXAR POR MICROSITE. **/
	@Column(name = "JOB_TIPO")
	private String tipo;
	
	/** Descripcion del job. **/
	@Column(name = "JOB_DESCRI")
	private String descripcion;
	
	/** Finalizado, se necesita en el indexar todo sin indexar.**/
	@Column(name = "JOB_FINALI")
	private int finalizado;
	
	/** Resumen. **/
	@Column(name = "JOB_RESUME")
	private String resumen;
	
	/** Campo transient para transformarlo en el descripcion. **/
	//@Column(name = "JOB_DESCRI",updatable=false, insertable=false)
	@Transient
	private String info;
	
	/** Id del elemento (microsite, ua). **/
	@Column(name = "JOB_IDELEM")
	private Long idElem;
	
	
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
	
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param clob the descripcion to set
	 */
	public void setDescripcion(String clob) {
		this.descripcion = clob;
	}
	
	/**
	 * @return the info
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public String getInfo() throws SQLException, IOException {
		/*
		String read;
		StringBuffer buffer = new StringBuffer();
		if (descripcion != null){
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(descripcion.getAsciiStream()));
			read = null;
			
			while((read = reader.readLine()) != null ){
				buffer.append(read);
			}
			
		}
		
		
		
		return buffer.toString();*/
		return descripcion;
	}
	/**
	 * @return the idElem
	 */
	public Long getIdElem() {
		return idElem;
	}
	/**
	 * @param idElem the idElem to set
	 */
	public void setIdElem(Long idElem) {
		this.idElem = idElem;
	}
	/**
	 * @return the finalizado
	 */
	public int getFinalizado() {
		return finalizado;
	}
	/**
	 * @param finalizado the finalizado to set
	 */
	public void setFinalizado(int finalizado) {
		this.finalizado = finalizado;
	}
	/**
	 * @return the resumen
	 */
	public String getResumen() {
		return resumen;
	}
	/**
	 * @param resumen the resumen to set
	 */
	public void setResumen(String resumen) {
		if (resumen == null) {
			this.resumen = "";
		} else { 
			//Se pone el limite para evitar un posible error ORA al ir con un LOB
			if (resumen.length() > 1000) {
				this.resumen = resumen.substring(0, 999);
			} else {
				this.resumen = resumen;
			}
		}
	}
	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	
	@Override
	public String toString() {
		StringBuffer texto = new StringBuffer();
		texto.append("SolrPendienteJob id:");
		texto.append(id);
		texto.append(" fechaIni:");
		texto.append(fechaIni);
		texto.append(" fechaFin:");
		texto.append(fechaFin);
		texto.append(" tipo:");
		texto.append(tipo);
		//No se añaden mas propiedades porque podría provocar problemas de memoria y no son necesarios.
		return texto.toString();
	}	
	
}
