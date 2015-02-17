package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Clase Estadística. Bean que define una Estadística. Modela la tabla de BBDD
 * GUS_STATS
 * 
 * @author Indra
 */

@Entity
@Table(name = "GUS_STATS")
public class Estadistica implements ValueObject {

	private static final long serialVersionUID = 7634727383680515426L;

	@Id
	@SequenceGenerator(name = "GUS_ESTADISTICA_ID_GENERATOR", sequenceName = "GUS_SEQSTA", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_ESTADISTICA_ID_GENERATOR")
	@Column(name = "STA_CODI")
	private Long id;

	@Column(name = "STA_ITEM")
	private Long item;

	@Column(name = "STA_MES")
	private int mes;

	@Column(name = "STA_REF")
	private String referencia;

	@Column(name = "STA_NACCES")
	private int accesos;

	@Column(name = "STA_MICCOD")
	private Long idmicrosite;

	@Column(name = "STA_PUB")
	private int publico;

	/**
	 * Define: Constructor de la clase.
	 */
	public Estadistica() {
	}

	/**
	 * Define: Constructor de la clase
	 * 
	 * @param idmicrosite
	 *            Identificador del Microsite
	 * @param item
	 *            Item
	 * @param referencia
	 *            Referencia
	 * @param mes
	 *            mes del acceso.
	 * @param publico
	 *            Tipo de acceso
	 */
	public Estadistica(Long idmicrosite, Long item, String referencia, int mes,
			int publico) {
		this.setIdmicrosite(idmicrosite);
		this.item = item;
		this.referencia = referencia;
		this.mes = mes;
		this.accesos = 0;
		this.publico = publico;
	}

	public int getAccesos() {
		return this.accesos;
	}

	public void setAccesos(int accesos) {
		this.accesos = accesos;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItem() {
		return this.item;
	}

	public void setItem(Long item) {
		this.item = item;
	}

	public int getMes() {
		return this.mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Long getIdmicrosite() {
		return this.idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public int getPublico() {
		return this.publico;
	}

	public void setPublico(int publico) {
		this.publico = publico;
	}

}