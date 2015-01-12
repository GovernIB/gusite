package org.ibit.rol.sac.micromodel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Clase Contacto. Bean que define un Contacto. 
 * Modela la tabla de BBDD GUS_FRMCON
 * @author Indra
 */
public class Contacto implements ValueObject {


	private static final long serialVersionUID = 465778084775544846L;
	
	public static final String RTYPE_TITULO="1";
	public static final String RTYPE_TEXTO="2";
	public static final String RTYPE_TEXTAREA="3";
	public static final String RTYPE_SELECTOR="4";
	public static final String RTYPE_SELECTORMULTIPLE="5";
	public static final String RTYPE_TEXTODESCRIPTIVO="6";
	
	
	private Long id;
	private String email;
	private String visible;
	private String anexarch;
	private Long idmicrosite;
	private Set lineasdatocontacto = new HashSet();


	public Contacto() {}

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Set getLineasdatocontacto() {
		return lineasdatocontacto;
	}

	public void setLineasdatocontacto(Set lineasdatocontacto) {
		this.lineasdatocontacto = lineasdatocontacto;
	}

	// Metodo para poder leer la coleccion del XML
	public void addLineasdatocontacto (Lineadatocontacto lineas) {
		lineasdatocontacto.add(lineas);
	}
	
	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getAnexarch() {
		return anexarch;
	}

	public void setAnexarch(String anexarch) {
		this.anexarch = anexarch;
	}

	public String getTitulocontacto(String idi) {
		String retorno="";
		try {
			Iterator<?> iter = lineasdatocontacto.iterator();
		    Lineadatocontacto lineadatocontacto;
	        while (iter.hasNext()) {
	        	 lineadatocontacto = (Lineadatocontacto)iter.next();
	        	 if (lineadatocontacto.getTipo().equals(RTYPE_TITULO)) {
	        		 retorno=((TraduccionLineadatocontacto)lineadatocontacto.getTraduccion(idi)).getTexto();
	        		 break;
	        	 }
	         }
		} catch (Exception e) {
			retorno="[sense titol]";
		}
        return retorno;
	}

}