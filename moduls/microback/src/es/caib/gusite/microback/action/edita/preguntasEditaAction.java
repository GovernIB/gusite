package es.caib.gusite.microback.action.edita;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.preguntaForm;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionPreguntaPK;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.EncuestaDelegate;

/**
 * Action que edita las preguntas de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/preguntaEdita" <BR> 
 *  name="preguntaForm" <BR> 
 *  input="/preguntasAcc.do"   <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detallePregunta.jsp"
 *  
 *  @author Indra
 */
public class preguntasEditaAction extends BaseAction {
	
	protected static Log log = LogFactory.getLog(preguntasEditaAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	EncuestaDelegate bdEncuesta = DelegateUtil.getEncuestaDelegate();
    	Pregunta pre = null;
    	preguntaForm f = (preguntaForm) form;

        //********************************************************
        //*********** EDICION / CREACION DE LA PREGUNTA **********
        //********************************************************
		String pModifica = "" + request.getParameter("modifica");
		String pAnyade = "" + request.getParameter("anyade");
		
		Microsite site = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	
    	if ( (!pModifica.equals("null")) || (!pAnyade.equals("null")) ) {
    		
    		if (f.get("id") == null) {
    			
            	pre = new Pregunta(); // Es Alta
            	pre.setNrespuestas(new Integer(0));
				pre.setIdencuesta(new Long("" + f.get("idencuesta")));

            } else {  // Es modificacion
            	
            	pre = bdEncuesta.obtenerPregunta((Long)f.get("id"));
            	pre.setNrespuestas(new Integer(  ((""+f.get("nrespuestas")).equals("null"))?"0":""+f.get("nrespuestas")  ));
            	
            	//************COMPROBACION DE IDES*************
            	// Obtengo el microsite y el titulo
            	Encuesta enc = bdEncuesta.obtenerEncuesta(pre.getIdencuesta());
            	request.setAttribute("titencuesta", ((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
            	request.setAttribute("titpregunta", ((TraduccionPregunta)pre.getTraduccion()).getTitulo());
            	            	
            	if (enc.getIdmicrosite().longValue() != site.getId().longValue()) {
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            	
            }
        	
			pre.setVisible("" + f.get("visible"));
			pre.setMultiresp("" + f.get("multiresp"));
			pre.setVisiblecmp("" + f.get("visiblecmp"));
			pre.setObligatorio("" + f.get("obligatorio"));
           	
           	if (f.get("maxContestadas")!= null && !"".equals(f.get("maxContestadas")))
           		pre.setMaxContestadas((Integer)f.get("maxContestadas"));
           	else
           		pre.setMaxContestadas(0);
           		
			if ("S".equals(f.get("multiresp"))) {
				if (f.get("minContestadas") != null && !"".equals(f.get("minContestadas")))
					pre.setMinContestadas((Integer) f.get("minContestadas"));
				else
					pre.setMinContestadas(0);
			} else {
				if (f.get("obligatoria") != null
						&& !"".equals(f.get("obligatoria"))
						&& (new Integer(1)).equals(f.get("obligatoria")))
					pre.setMinContestadas(new Integer(1));
				else
					pre.setMinContestadas(new Integer(0));
			}
           	
			if (f.get("orden") != null)
				pre.setOrden(new Integer("" + f.get("orden")));
			else
				pre.setOrden(null);

			FormFile imagen = (FormFile) f.get("imagen");
			if (archivoValido(imagen))
				pre.setImagen(populateArchivo(pre.getImagen(), imagen, null, null));
			else if (((Boolean) f.get("imagenbor")).booleanValue())
				pre.setImagen(null);
           
			if (pre.getImagen() != null)
				if (("" + f.get("imagennom")).length() > 0)
					pre.getImagen().setNombre("" + f.get("imagennom"));

           if (f.get("id") == null) {
        	   
        	   VOUtils.populate(pre, f);  // form --> bean
        	   
           } else {
        	   
	    		@SuppressWarnings("unchecked")
				List<TraduccionPregunta> llista = (List<TraduccionPregunta>) f.get("traducciones");
	    		List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
	    		
				for (int i = 0; i < llista.size(); i++) {
	    			
	    			if (pre.getTraducciones().containsKey(((Idioma)langs.get(i)).getLang())) {
	    				
	    				pre.getTraducciones().get(((Idioma)langs.get(i)).getLang()).setTitulo(llista.get(i).getTitulo());

	    			} else {
	    				
	    				TraduccionPregunta traduccio = new TraduccionPregunta();
	    				TraduccionPreguntaPK idtp = new TraduccionPreguntaPK(); 
	    				idtp.setCodigoPregunta(pre.getId());
	    				idtp.setCodigoIdioma( ((Idioma)langs.get(i)).getLang());
	    				traduccio.setId(idtp);
	    				traduccio.setTitulo(llista.get(i).getTitulo());
	    				pre.getTraducciones().put(((Idioma)langs.get(i)).getLang(), traduccio);
	    			}
	    			
	    		}
	    		
           }

           bdEncuesta.grabarPregunta(pre);
          	
          	if (request.getParameter("anyade") != null) {
          		addMessage(request, "mensa.nuevapregunta");
           		addMessage(request, "mensa.crearnuevapregunta", "" + pre.getIdencuesta().longValue());
          	}
          	
           	if (request.getParameter("modifica") != null)	
           		addMessage(request, "mensa.modifpregunta");	
           	
       		addMessage(request, "mensa.editarpregunta", "" + pre.getId().longValue());
       		addMessage(request, "mensa.editarencuesta", "" + pre.getIdencuesta().longValue());
      		
           	return mapping.findForward("info");
               
       }

        //********************************************************
        //*********** ALTA PREGUNTAS DE LA ENCUESTA **************
        //********************************************************
		if (request.getParameter("idenc") != null) {

    		Encuesta enc = bdEncuesta.obtenerEncuesta(new Long(""+request.getParameter("idenc")));
        	request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());

    		preguntaForm fdet=(preguntaForm) form;
            fdet.set("idencuesta", new Long(""+request.getParameter("idenc")));
    		
       		return mapping.findForward("detalle");
       		
        }    	
    	
        //********************************************************
        //********** BORRAMOS RESPUESTAS DE LA PREGUNTA **********
        //********************************************************
		if (request.getParameter("borrar") != null) {

    		bdEncuesta.eliminarRespuestas((String[])f.get("seleccionados"), (Long)f.get("id"));
            
    		addMessage(request, "mensa.listaresborradas");
       		addMessage(request, "mensa.editarpregunta", "" + f.get("id"));
           	addMessage(request, "mensa.listaencuestas");
           	
           	return mapping.findForward("info");
           	
        }    	
    	
        //********************************************************
        //************ EDITAMOS LA PREGUNTA **********************
        //********************************************************
		if (request.getParameter("id") != null) {

			Long id = new Long("" + request.getParameter("id"));

            pre = bdEncuesta.obtenerPregunta(id);
        	
            if (bdEncuesta.checkSite(site.getId(), pre.getIdencuesta())) {
            	addMessage(request, "info.seguridad");
            	return mapping.findForward("info");
            }

            //************COMPROBACION DE IDES*************
            // Obtengo el microsite y el titulo
        	Encuesta enc = bdEncuesta.obtenerEncuesta(pre.getIdencuesta());
        	request.setAttribute("titencuesta",((TraduccionEncuesta)enc.getTraduccion()).getTitulo());
        	request.setAttribute("titpregunta",((TraduccionPregunta)pre.getTraduccion()).getTitulo());
        	
        	if (enc.getIdmicrosite().longValue() != site.getId().longValue()) {
        		addMessage(request, "peticion.error");
                return mapping.findForward("info");
        	}
        	//*********************************************
        	
			preguntaForm fdet = (preguntaForm) form;
            
            fdet.set("orden", pre.getOrden());
            fdet.set("nrespuestas", pre.getNrespuestas());
            fdet.set("multiresp", pre.getMultiresp());
            fdet.set("visiblecmp", pre.getVisiblecmp());
            fdet.set("obligatorio", pre.getObligatorio());
            fdet.set("maxContestadas", pre.getMaxContestadas());
            fdet.set("minContestadas", pre.getMinContestadas());
            
			if (pre.getMinContestadas() != null && pre.getMinContestadas() > 0) {
				fdet.set("obligatoria", new Integer(1));
			} else {
				fdet.set("obligatoria", new Integer(0));
			}
			
            fdet.set("visible", pre.getVisible());
            fdet.set("idencuesta", pre.getIdencuesta());
            
           	Iterator<?> it = pre.getRespuestas().iterator();	
        	ArrayList<Respuesta> resp = new ArrayList<Respuesta>();
        	while (it.hasNext()) {
        		resp.add((Respuesta)it.next());
        	}
            fdet.set("respuestas",resp);
            
            VOUtils.describe(fdet, pre);  // bean --> form

            if (pre.getImagen() != null) {
            	fdet.set("imagennom", pre.getImagen().getNombre());
            	fdet.set("imagenid", pre.getImagen().getId());
            }

            return mapping.findForward("detalle");

        }    	
    	
        addMessage(request, "peticion.error");
        
        return mapping.findForward("info");
        
    }
    
}
