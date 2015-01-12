package es.caib.gusite.microback.action.edita;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.respuestaForm;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;

/**
 * Action que edita las respuestas de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/respuestaEdita" <BR> 
 *  name="respuestaForm" <BR> 
 *  input="/respuestasAcc.do"   <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleRespuesta.jsp"
 *  
 *  @author Indra
 */
public class respuestasEditaAction extends BaseAction 
{
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */
	
	protected static Log log = LogFactory.getLog(respuestasEditaAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
    	Respuesta res=null;
    	respuestaForm f = (respuestaForm) form;
    	
        //********************************************************
        //********** EDICION / CREACION DE LA RESPUESTA **********
        //********************************************************
    	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null) {

        	if (f.get("id") == null) {  
            	res = new Respuesta(); // Es Alta
            	res.setNrespuestas(new Integer(0));
            	res.setIdpregunta(new Long(""+f.get("idpregunta")));
            } else {  // Es modificacion
            	res = bdEncuesta.obtenerRespuesta((Long)f.get("id"));
            	res.setNrespuestas(new Integer(  ((""+f.get("nrespuestas")).equals("null"))?"0":""+f.get("nrespuestas")  ));
            	//************COMPROBACION DE IDES*************
            	// Obtengo el microsite y el titulo
            	Pregunta pre = bdEncuesta.obtenerPregunta(res.getIdpregunta());
            	Encuesta enc = bdEncuesta.obtenerEncuesta(pre.getIdencuesta());
            	
            	request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
            	request.setAttribute("titpregunta",((TraduccionPregunta)pre.getTraduccion()).getTitulo());
            	request.setAttribute("titrespuesta",((TraduccionRespuesta)res.getTraduccion()).getTitulo());
            	
            	if (enc.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            }
        	
        	res.setTipo(""+f.get("tipo"));
        	
           	if (f.get("orden")!=null)
           		res.setOrden(new Integer(""+f.get("orden")));
           	else
           		res.setOrden(null);

           	if (f.get("id") == null) { 
           		VOUtils.populate(res, f);  // form --> bean
           	} else {
           		List<TraduccionRespuesta> llista = (List<TraduccionRespuesta>) f.get("traducciones");
	    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
	    		
	    		for (int i=0; i<llista.size(); i++)
	    		{
	    			if (res.getTraducciones().containsKey(((Idioma)langs.get(i)).getLang()))
	    			{
	    				res.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTitulo(llista.get(i).getTitulo());

	    			} else {
	    				TraduccionRespuesta traduccio = new TraduccionRespuesta();
//	    				traduccio.setRespuesta(res);
//	    				traduccio.setIdioma((Idioma)langs.get(i));
	    				traduccio.setTitulo(llista.get(i).getTitulo());
	    				
	    				res.getTraducciones().put(((Idioma)langs.get(i)).getLang(), traduccio);
	    			}
	    		}
           	}

           	bdEncuesta.grabarRespuesta(res);
          
	        if(request.getParameter("anyade")!=null) {
	        	addMessage(request, "mensa.nuevarespuesta");
	           	addMessage(request, "mensa.crearnuevarespuesta", "" + res.getIdpregunta().longValue());
	        }
          	if(request.getParameter("modifica")!=null)	
          		addMessage(request, "mensa.modifrespuesta");	
          	
      		addMessage(request, "mensa.editarrespuesta", "" + res.getId().longValue());
      		addMessage(request, "mensa.editarpregunta", "" + res.getIdpregunta().longValue());
      		
          	return mapping.findForward("info");
               
       }

        //********************************************************
        //*********** ALTA RESPUESTAS DE LA PREGUNTA *************
        //********************************************************
    	if(request.getParameter("idpreg")!=null) {

    		Pregunta pre = bdEncuesta.obtenerPregunta(new Long(""+request.getParameter("idpreg")));
    		Encuesta enc = bdEncuesta.obtenerEncuesta(pre.getIdencuesta());
    		    		
    		request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
        	request.setAttribute("titpregunta",((TraduccionPregunta)pre.getTraduccion()).getTitulo());
        	request.setAttribute("idencuesta", ""+enc.getId());
        	
    		respuestaForm fdet=(respuestaForm) form;
            fdet.set("idpregunta", new Long(""+request.getParameter("idpreg")));
    		
       		return mapping.findForward("detalle");
        }    	
    	
        //********************************************************
        //*********** EDITAMOS LA RESPUESTA **********************
        //********************************************************
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));

                res = bdEncuesta.obtenerRespuesta(id);
                Pregunta pre=bdEncuesta.obtenerPregunta(res.getIdpregunta());
            	Encuesta enc = bdEncuesta.obtenerEncuesta(pre.getIdencuesta());

            	request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
            	request.setAttribute("titpregunta",((TraduccionPregunta)pre.getTraduccion()).getTitulo());
            	request.setAttribute("titrespuesta",((TraduccionRespuesta)res.getTraduccion()).getTitulo());
            	request.setAttribute("idencuesta", ""+enc.getId());
            	
                if (bdEncuesta.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(), pre.getIdencuesta())) {
                	addMessage(request, "info.seguridad");
                	return mapping.findForward("info");
                }

                //************COMPROBACION DE IDES*************
            	
            	if (enc.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
                respuestaForm fdet=(respuestaForm) form;
                
                fdet.set("orden", res.getOrden());
                fdet.set("tipo", res.getTipo());
                fdet.set("nrespuestas", res.getNrespuestas());
                fdet.set("idpregunta", res.getIdpregunta());
                
                VOUtils.describe(fdet, res);  // bean --> form
              
            return mapping.findForward("detalle");

        }
    	
        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}