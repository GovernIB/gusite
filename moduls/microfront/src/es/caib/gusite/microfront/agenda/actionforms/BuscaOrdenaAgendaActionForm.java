package es.caib.gusite.microfront.agenda.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Clase BuscaOrdenaAgendaActionForm.  Utilizado para buscar y ordenar Agendas. 
 * <P>
 * Formulario definido para la acción ListarAgendaAction.
 * @author Indra
 */
public class BuscaOrdenaAgendaActionForm extends ActionForm  {


	private static final long serialVersionUID = 4767405990764347256L;
	
	private String filtro;
	private String ordenacion;
	 
	public BuscaOrdenaAgendaActionForm() {}
	 
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