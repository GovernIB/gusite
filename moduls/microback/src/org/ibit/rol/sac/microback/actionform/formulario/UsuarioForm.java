package org.ibit.rol.sac.microback.actionform.formulario;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Formulario de usuario
 * 
 * @author Indra
 */
public class UsuarioForm extends ActionForm {

	private static final long serialVersionUID = 8645819480796090771L;

	protected static Log log = LogFactory.getLog(UsuarioForm.class);

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
	}

	
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	if(httpServletRequest.getParameter("modifica")!=null || httpServletRequest.getParameter("anyade")!=null) {
	    	if ( !((getPassword()).equals(getRepitepwd())) ) 
	            errors.add("repitepwd", new ActionError("error.usuari.password.confirm"));
	    	
	        if ( ((getUsername()).equals("null")) || (getUsername().length()==0) )
	        	errors.add("username", new ActionError("error.usuari.login.nonulo"));
	
	        if (getPerfil().equals("0"))
	        	errors.add("perfil", new ActionError("error.usuari.perfil.nonulo"));
	        if (errors.size()!=0) httpServletRequest.setAttribute("errores", "yes");
    	}
    	return errors;

    }	
	
	Long id;
	String username;
	String password;
	String repitepwd;
	String nombre;
	String observaciones;
	String perfil;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRepitepwd() {
		return repitepwd;
	}


	public void setRepitepwd(String repitepwd) {
		this.repitepwd = repitepwd;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getPerfil() {
		return perfil;
	}


	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
    
}
