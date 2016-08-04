package es.caib.gusite.microfront.faq.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Clase BuscaOrdenaFaqActionForm.  Utilizado para buscar y ordenar Faq. 
 * <P>
 * Formulario definido para la acción ListarFaqsAction.
 * @author Indra
 */
public class BuscaOrdenaFaqActionForm extends ActionForm {


	private static final long serialVersionUID = 1861426949712400768L;
	
	private String filtro;
    private String ordenacion;
    
	public BuscaOrdenaFaqActionForm() {}
	
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest){
        return null;
    }

    public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest){
    }

    public void setFiltro(String filtro){
        this.filtro = filtro;
    }

    public String getFiltro(){
        return filtro;
    }

    public void setOrdenacion(String ordenacion){
        this.ordenacion = ordenacion;
    }

    public String getOrdenacion(){
        return ordenacion;
    }
	
}
