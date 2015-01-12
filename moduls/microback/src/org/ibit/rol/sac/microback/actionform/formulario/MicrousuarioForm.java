package org.ibit.rol.sac.microback.actionform.formulario;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.actionform.TraDynaActionForm;

/**
 *	Formulario para los microsites de usuario
 *
 *@author Indra
 *
 */
public class MicrousuarioForm extends TraDynaActionForm {


	private static final long serialVersionUID = 820787294237561685L;
	protected static Log log = LogFactory.getLog(microForm.class);

    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#initialize(org.apache.struts.action.ActionMapping)
     */
    public void initialize(ActionMapping actionMapping) {
        super.initialize(actionMapping);
        inicio();
    }

    /* (non-Javadoc)
     * @see org.ibit.rol.sac.microback.actionform.TraDynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
        super.reset(mapping, request);
        inicio();
    }

    private void inicio() {
    }
	
}
