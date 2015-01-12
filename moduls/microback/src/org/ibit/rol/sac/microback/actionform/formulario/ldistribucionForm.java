package org.ibit.rol.sac.microback.actionform.formulario;

import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 *	Formulario para los contenidos
 *
 *@author Indra
 *
 */
public class ldistribucionForm extends ActionForm {

	protected ResourceBundle propertiesMessages = ResourceBundle.getBundle("sac-microback-messages");
	private static final long serialVersionUID = -3575980351954309067L;
    protected static Log log = LogFactory.getLog(ldistribucionForm.class);

    public void reset(ActionMapping mapping, HttpServletRequest request){
        super.reset(mapping, request);
    }

    public void resetForm() {
    	id = null;
       	nombre = "";
       	descripcion = "";
       	email = "";
       	destinatarios = Collections.EMPTY_SET;       	
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

    	ActionErrors errors = new ActionErrors();
    	
    	if(httpServletRequest.getParameter("modifica")!=null || httpServletRequest.getParameter("anyade")!=null) {
	    	if ( getNombre()==null || getNombre().equals("")) 
	            errors.add("nombre", new ActionError("error.convocatoria.nombre"));	    	

	        if (errors.size()!=0) httpServletRequest.setAttribute("errores", "yes");
    	}
    	return errors;

    }
    
    private Long id;
   	private String nombre;
   	private String descripcion;
   	private String publico;
   	private String email;
   	private FormFile upLoad;
   	private Set destinatarios;
   	private String[] seleccionados;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String[] getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(String[] seleccionados) {
		this.seleccionados = seleccionados;
	}

	public Set getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(Set destinatarios) {
		this.destinatarios = destinatarios;
	}

	public FormFile getUpLoad() {
		return upLoad;
	}

	public void setUpLoad(FormFile upLoad) {
		this.upLoad = upLoad;
	}

	public String getPublico() {
		return publico;
	}

	public void setPublico(String publico) {
		this.publico = publico;
	}
   	
}
