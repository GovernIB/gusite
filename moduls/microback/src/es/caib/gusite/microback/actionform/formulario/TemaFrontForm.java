package es.caib.gusite.microback.actionform.formulario;

import es.caib.gusite.microback.actionform.TraDynaActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by brújula on 19/02/2015.
 */
public class TemaFrontForm extends TraDynaActionForm {

    private static final long serialVersionUID = -5673184444653688291L;

    protected static Log log = LogFactory.getLog(TemaFrontForm.class);

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }
}
