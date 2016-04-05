package es.caib.gusite.microback.action.edita;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.caib.gusite.microback.utils.Cadenas;
import es.caib.gusite.micromodel.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.encuestaForm;
import es.caib.gusite.microback.ajax.AjaxCheckUriAction;
import es.caib.gusite.microback.ajax.AjaxCheckUriAction.UriType;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;

/**
 * Action que edita las encuestas de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/encuestaEdita" <BR> 
 *  name="encuestaForm" <BR> 
 *  input="/encuestassAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleEncuesta.jsp"
 *  
 *  @author Indra
 */
public class encuestasEditaAction extends BaseAction 
{
	
	protected static Log log = LogFactory.getLog(encuestasEditaAction.class);
	
    @SuppressWarnings({ "rawtypes", "unchecked" })	
	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
    	Encuesta enc=null;
    	encuestaForm f = (encuestaForm) form;

        //********************************************************
        //*********** EDICION / CREACION DE LA ENCUESTA **********
        //********************************************************

    	String pModifica="" + request.getParameter("modifica");
    	String pAnyade="" + request.getParameter("anyade");
    	
    	final Long idMicrosite = ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
        request.setAttribute("idmicrosite" , idMicrosite);
        
    	if ( (!pModifica.equals("null")) || (!pAnyade.equals("null")) ) { 

        	if (f.get("id") == null) {  
            	enc = new Encuesta(); // Es Alta
            } else {  // Es modificacion
            	enc = bdEncuesta.obtenerEncuesta((Long)f.get("id"));
            	//************COMPROBACION DE IDES*************
            	if (enc.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            	request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
            }
        
	       	enc.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
	       	enc.setFcaducidad(f.getFcaducidad());
	       	enc.setFpublicacion(f.getFpublicacion());
	       	enc.setVisible(""+f.get("visible"));
	       	enc.setIndivisible(""+f.get("indivisible"));
	       	enc.setMostrar(""+f.get("mostrar"));
	       	enc.setIdentificacion(""+f.get("identificacion"));
	       	if (f.get("paginacion")!=null)
	       		enc.setPaginacion(new Integer(""+f.get("paginacion")));
	       	else
	       		enc.setPaginacion(null);
	
	       	if (f.get("id") == null) {  
	       		VOUtils.populate(enc, f);  // form --> bean
	       	} else {
	    		List<TraduccionEncuesta> llista = (List<TraduccionEncuesta>) ((DynaActionForm) form).get("traducciones");
	    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
	    		
	    		for (int i=0; i<llista.size(); i++)
	    		{
	    			if (enc.getTraducciones().containsKey(((Idioma)langs.get(i)).getLang()))
	    			{
	    				enc.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTitulo(llista.get(i).getTitulo());
	    				enc.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setUri(llista.get(i).getUri());

	    			} else {
	    				TraduccionEncuesta traduccio = new TraduccionEncuesta();
	    				TraduccionEncuestaPK idt = new TraduccionEncuestaPK();
	    				idt.setCodigoEncuesta(enc.getId());
	    				idt.setCodigoIdioma( ((Idioma)langs.get(i)).getLang());
	    				traduccio.setId(idt);
	    				traduccio.setTitulo(llista.get(i).getTitulo());
	    				traduccio.setUri(llista.get(i).getUri());
	    				
	    				enc.getTraducciones().put(((Idioma)langs.get(i)).getLang(), traduccio);
	    			}
	    		}
	       	}

			List<String> eliminar = new ArrayList<String>();
			for (String lang : enc.getTraducciones().keySet()) {
				TraduccionEncuesta trad = enc.getTraducciones().get(lang);
				if (trad.getTitulo().equals("") && trad.getUri().equals("")) {
					eliminar.add(lang);
				} else if (trad.getUri().equals("")) {
					final AjaxCheckUriAction ajax = new AjaxCheckUriAction();
					Long codigoEncuesta = null;
					if (trad.getId() != null) {
						codigoEncuesta = trad.getId().getCodigoEncuesta();
					}
					final String nuevaUri = ajax.check(Cadenas.string2uri(trad.getTitulo()),  UriType.EID_URI,  ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().toString(),  lang, codigoEncuesta, 0);
					trad.setUri(Cadenas.string2uri(nuevaUri));
				}
			}
			for (String key : eliminar) {
				enc.getTraducciones().remove(key);
			}
	       	bdEncuesta.grabarEncuesta(enc);
	
	       	if(request.getParameter("anyade")!=null) 
	       		addMessage(request, "mensa.nuevaencuesta");
	       	if(request.getParameter("modifica")!=null)	
	       		addMessage(request, "mensa.modifencuesta");	
	       	
	   		addMessage(request, "mensa.editarencuesta", "" + enc.getId().longValue());
	       	addMessage(request, "mensa.listaencuestas");
	       	
	       	return mapping.findForward("info");
       }

        //********************************************************
        //*********** BORRAMOS PREGUNTAS DE LA ENCUESTA **********
        //********************************************************
    	if(request.getParameter("borrar")!=null) {

    		bdEncuesta.eliminarPreguntas((String[])f.get("seleccionados") , (Long)f.get("id"));

       		addMessage(request, "mensa.listapregborradas");
       		addMessage(request, "mensa.editarencuesta", "" + f.get("id"));
           	addMessage(request, "mensa.listaencuestas");
           	
           	
           	return mapping.findForward("info");
    		
        }    	
    	
        //********************************************************
        //********************** EDITAMOS ************************
        //********************************************************
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));
          	
                if (bdEncuesta.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
                	addMessage(request, "info.seguridad");
                	return mapping.findForward("info");
                }

                enc = bdEncuesta.obtenerEncuesta(id);
            	request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
                
                //************COMPROBACION DE IDES*************
            	if (enc.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            	encuestaForm fdet=(encuestaForm) form;
               
                fdet.setFcaducidad(enc.getFcaducidad());
                fdet.setFpublicacion(enc.getFpublicacion());
                fdet.set("visible",enc.getVisible());
                
                fdet.set("indivisible",enc.getIndivisible());
                fdet.set("paginacion", enc.getPaginacion());
                fdet.set("mostrar", enc.getMostrar());
                fdet.set("identificacion", (enc.getIdentificacion()==null)?"N":enc.getIdentificacion());
               	Iterator it = enc.getPreguntas().iterator();	
            	ArrayList pregs= new ArrayList();
            	while (it.hasNext()) {
            		pregs.add((Pregunta)it.next());
            	}
                fdet.set("preguntas",pregs);
                
                VOUtils.describe(fdet, enc);  // bean --> form
                
            return mapping.findForward("detalle");

        }    	

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}