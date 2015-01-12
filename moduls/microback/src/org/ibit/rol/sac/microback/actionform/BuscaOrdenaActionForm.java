package org.ibit.rol.sac.microback.actionform;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;


/**
 *	Formulario para las funcionalidades de b�squeda y ordenaci�n
 *
 *@author Indra
 *
 */
public class BuscaOrdenaActionForm extends ActionForm
{

	private static final long serialVersionUID = 3746946055550629073L;
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