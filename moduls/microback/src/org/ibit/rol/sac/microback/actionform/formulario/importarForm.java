package org.ibit.rol.sac.microback.actionform.formulario;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile; 

/**
 *	Formulario para el proceso de importación de microsite
 *
 *@author Indra
 *
 */
public class importarForm extends ActionForm {

	private static final long serialVersionUID = 2033793824633732367L;
	private FormFile archi;
	private String nuevonombre;
	private String indexar;
	private String tarea;
	
	    
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
	    	indexar = "N"; //no indexar
	    	tarea="C"; //crear microsite
	    }

		public FormFile getArchi() {
			return archi;
		}

		public void setArchi(FormFile archi) {
			this.archi = archi;
		}
	
		public String getNuevonombre() {
			return nuevonombre;
		}

		public void setNuevonombre(String nuevonombre) {
			this.nuevonombre = nuevonombre;
		}

		public String getIndexar() {
			return indexar;
		}

		public void setIndexar(String indexar) {
			this.indexar = indexar;
		}

		public String getTarea() {
			return tarea;
		}

		public void setTarea(String tarea) {
			this.tarea = tarea;
		}


}
