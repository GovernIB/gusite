package es.caib.gusite.micromodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Clase Archivo. Bean que define una Archivo. Modela la tabla de BBDD GUS_DOCUS
 * 
 * @author Indra
 */
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "GUS_DOCUS")
@SuppressWarnings({"rawtypes", "unchecked"}) // Para los Map sin parametrizar.
public class ArchivoFull extends AuditableModel implements Indexable {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -3122017714028641802L;

	@XmlAttribute
	@Id
	@SequenceGenerator(name = "GUS_DOCUS_ID_GENERATOR", sequenceName = "GUS_SEQDOC", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUS_DOCUS_ID_GENERATOR")
	@Column(name = "DCM_CODI")
	private Long id;

	@Column(name = "DCM_DATOS")
	private byte[] datos;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getDatos() {
		return this.datos;
	}

	public void setDatos(byte[] datos) {
		this.datos = datos;
	}
}