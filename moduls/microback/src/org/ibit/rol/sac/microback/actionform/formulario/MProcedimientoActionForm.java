package org.ibit.rol.sac.microback.actionform.formulario;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *	Formulario para los procedimientos
 *
 *@author Indra
 *
 */
public class MProcedimientoActionForm extends ActionForm {

	private static final long serialVersionUID = -4347269256648520484L;
	private Long id;
	private Long idmicrosite;
	private String[] procedimientos;
	private boolean todonada; // Check de selección/deselección
	private String accion; // acción ejecutada

	    
	    /* (non-Javadoc)
	     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	     */
	    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
	        return null;
	    }
	    
	    
	    /* (non-Javadoc)
	     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	     */
	    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {

	    // Limpiamos el array de objetos checkbox seleccionados
	    setTodonada(false);
	    id=null;
	    idmicrosite=null;
	    procedimientos=null;
	    
	   }



	    public void setTodonada(boolean todonada)
	    {
	        this.todonada = todonada;
	    }


	    public boolean isTodonada()
	    {
	        return todonada;
	    }


	    public void setAccion(String accion)
	    {
	        this.accion = accion;
	    }


	    public String getAccion()
	    {
	        return accion;
	    }
	
	
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
		
		public String[] getProcedimientos() {
			return procedimientos;
		}
		
		public void setProcedimientos(String[] procedimientos) {
			this.procedimientos = procedimientos;
		}
	
}
