package org.ibit.rol.sac.microback.action.edita;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.microForm;
import org.ibit.rol.sac.microback.base.Base;
import org.ibit.rol.sac.micromodel.Archivo;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionMicrosite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;

/**
 * Action que edita la p�gina de inicio de un microsite <BR>
 * <P>
 * 	Definici�n Struts:<BR>
 *  action path="/paginaInicioEdita" <BR> 
 *  name="microForm" <BR> 
 *  input="/microAcc.do"   <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="home" path="/detallePaginaInicio.jsp"<BR>
 *  forward name="info" path="/info.jsp"<BR>
 *  forward name="inicio" path="/index.jsp"
 *  
 *  @author Indra
 */
public class paginaInicioEditaAction extends BaseAction 
{
    protected static Log log = LogFactory.getLog(microEditaAction.class);
	
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return ActionForward
     */
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	Microsite micrositeBean = null;
    	microForm microForm = (microForm) form;

    	/** Guardar **/	
		if ((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.guardar"))) {

    		micrositeBean = setFormtoBean(microForm);
            micrositeDelegate.grabarMicrosite(micrositeBean);
            addMessageWithDate(request, "micro.pinicio.info.modifica.microsite");
       		Base.micrositeRefresh(micrositeBean.getId(), request);
       		Base.menuRefresh(request);
           	return mapping.findForward("home");

		}
		/** Pagina Inicio **/	
    	else {
    		
    		if (request.getParameter("idsite")!=null) {

    	    	micrositeBean = micrositeDelegate.obtenerMicrosite(new Long(""+request.getParameter("idsite")));
      	    	setBeantoForm(micrositeBean, microForm);
               	request.setAttribute("tituloMicro", ((TraduccionMicrosite)micrositeBean.getTraduccion()).getTitulo() );
            	request.setAttribute("MVS_URL_migapan",Base.obtenerMigaContenidoFromURI(micrositeBean.getUrlhome()));
            	
            	return mapping.findForward("home");

    		}
    	}

    	 addMessageError(request, "peticion.error");
         return mapping.findForward("info");
         
         
    }

    /**
     * M�todo que vuelca los datos del formulario en el Bean de Microsite
     * @param microForm	 formulario din�mico enviado por usuario
     * @return Microsite devuelve bean de Microsite con los datos del formulario
     * @throws Exception
     */
    private Microsite setFormtoBean (microForm microForm) throws Exception  {
    	
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
    	Microsite micrositeBean = micrositeDelegate.obtenerMicrosite((Long)microForm.get("id"));
		
        micrositeBean.setPlantilla((String)microForm.get("plantilla"));
        micrositeBean.setUrlhome((String)microForm.get("urlhome"));
        micrositeBean.setServiciosSeleccionados((String[])microForm.get("planti5serv"));
        
        return micrositeBean;
	
    }
    
    /**
     * M�todo que vuelca los datos del Bean de Microsite al formulario del usuario
     * @param micrositeBean		Bean de tipo Microsite
     * @param microForm			formulario din�mico enviado por usuario
     * @throws Exception
     */
    private void setBeantoForm (Microsite micrositeBean, microForm microForm) throws Exception  {

    	microForm.set("id",micrositeBean.getId());
    	
    	microForm.set("plantilla", micrositeBean.getPlantilla());
    	microForm.set("urlhome",micrositeBean.getUrlhome());

    	if (micrositeBean.getImagenPrincipal()!=null)
    		microForm.set("imagenPrincipalid",micrositeBean.getImagenPrincipal().getId());

    	microForm.set("planti5serv", micrositeBean.getServiciosSeleccionados(micrositeBean.getServiciosSeleccionados()));
	
    } 

	//MCR v1.1
    /*
     * El tag de hoja de estilos por defecto es una copia del bueno, pero quitandole la imagen de fondo al tag 'body'.
     * El objetivo de esto es que se visualice correctamente en el tinymce mientras estamos editando. 
     */
    
	public String tagCSS(Archivo archivocss) {
		String retorno="";
		if (archivocss!=null)
			retorno="archivo.do?id=" + archivocss.getId().longValue();
		else
			retorno="/sacmicrofront/css/dummy_estilos01_blau.css";
		return retorno;
	}	
	
}
