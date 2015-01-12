package org.ibit.rol.sac.micromodel;

/**
 * Clase MProcedimiento. Bean que define un procedimiento. 
 * Modela la tabla de BBDD GUS_MICPRO.
 * @author Indra
 */
public class MProcedimiento implements ValueObject{

	private static final long serialVersionUID = -3544004361619423272L;
	private Long id;
    private Long idmicrosite;
    private String procedimientos;
      
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdmicrosite() {
		return idmicrosite;
	}
	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}
	public String getProcedimientos() {
		return procedimientos;
	}
	public void setProcedimientos(String procedimientos) {
		this.procedimientos = procedimientos;
	}
	
}
