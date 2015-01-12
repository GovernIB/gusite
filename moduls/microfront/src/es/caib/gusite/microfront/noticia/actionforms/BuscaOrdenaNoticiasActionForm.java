package es.caib.gusite.microfront.noticia.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Clase BuscaOrdenaNoticiasActionForm.  Utilizado para buscar y ordenar Noticias. 
 * <P>
 * Formulario definido para la acción ListarNoticiasAction.
 * @author Indra
 */
public class BuscaOrdenaNoticiasActionForm extends ActionForm  {

	private static final long serialVersionUID = -167316134229888157L;
	private String filtro;
	private String ordenacion;
	private String tipoelemento;
	 
	public BuscaOrdenaNoticiasActionForm() {}
	 
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
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

	public String getTipoelemento() {
		return tipoelemento;
	}

	public void setTipoelemento(String tipoelemento) {
		this.tipoelemento = tipoelemento;
	}	 
	
}
