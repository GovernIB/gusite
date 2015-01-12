package es.caib.gusite.microback.actionform.busca;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

/**
 *	Formulario para las funcionalidades de búsqueda y ordenación
 *
 *@author Indra
 *
 */
public class BuscaOrdenaActionForm extends ActionForm
{

	private static final long serialVersionUID = 5241389774413621169L;
	public BuscaOrdenaActionForm()
    {
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest)
    {
    }

    public void setFiltro(String filtro)
    {
        this.filtro = filtro;
    }

    public String getFiltro()
    {
        return filtro;
    }

    public void setOrdenacion(String ordenacion)
    {
        this.ordenacion = ordenacion;
    }

    public String getOrdenacion()
    {
        return ordenacion;
    }

    private String filtro;
    private String ordenacion;
}