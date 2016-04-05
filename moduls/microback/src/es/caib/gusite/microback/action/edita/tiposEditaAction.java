package es.caib.gusite.microback.action.edita;

import java.util.ArrayList;
import java.util.Arrays;
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

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.ajax.AjaxCheckUriAction;
import es.caib.gusite.microback.ajax.AjaxCheckUriAction.UriType;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.microback.utils.VOUtils;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.TipoDelegate;

/**
 * Action que edita los tipos (listados) de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/tipoEdita" <BR> 
 *  name="tiponotForm" <BR> 
 *  input="/tiposAcc.do"   <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleTipo.jsp"
 *  
 *  @author Indra
 */
public class tiposEditaAction extends BaseAction {

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
	
	protected static Log log = LogFactory.getLog(tiposEditaAction.class);
	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {

    	TipoDelegate bdTipo = DelegateUtil.getTipoDelegate();
        Tipo tipo = null;
        TraDynaActionForm f = (TraDynaActionForm) form;

        if (request.getParameter("accion") != null) {

            if (request.getParameter("modifica") != null
                    || request.getParameter("anyade") != null
                    || (request.getParameter("accion").equals(getResources(request).getMessage("operacion.guardar")))) {

                if (f.get("id") == null) {
                    // Es Alta
                    tipo = new Tipo();
                } else {
                    // Es modificacion
                    tipo = bdTipo.obtenerTipo((Long) f.get("id"));
                    //************COMPROBACION DE IDES*************
                    if (tipo.getIdmicrosite().longValue() != ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().longValue()) {
                        addMessageError(request, "peticion.error");
                        return mapping.findForward("info");
                    }
                    //*********************************************
                }

                tipo.setIdmicrosite(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId());
                String tipoelemento = (String) f.get("tipoelemento");
                tipo.setTipoelemento(tipoelemento);

                if (!tipoelemento.equals(Tipo.TIPO_CONEXIO_EXTERNA)) {
                    tipo.setTampagina(Integer.parseInt("" + f.get("tampagina")));
                    tipo.setTipopagina((String) f.get("tipopagina"));
                    tipo.setBuscador((String) f.get("buscador"));
                    tipo.setOrden((String) f.get("orden"));
                    tipo.setClasificacion((String) f.get("clasificacion"));

                } else {
                    tipo.setTipopagina("0");
                    tipo.setBuscador("N");
                    tipo.setOrden("0");
                    tipo.setXjndi((String) f.get("xjndi"));
                    tipo.setXurl((String) f.get("xurl"));
                    tipo.setXusr((String) f.get("xusr"));
                    tipo.setXpwd((String) f.get("xpwd"));
                    tipo.setXid((String) f.get("xid"));
                    tipo.setClasificacion((String) f.get("clasificacion_ext"));
                }

                if (tipoelemento.equals(Tipo.TIPO_FOTO)) {
                    tipo.setFotosporfila((Integer)f.get("fotosporfila"));
                }

                // Si es una modificación solo se cambian los textos
                if (f.get("id") == null) {
                    VOUtils.populate(tipo, f);

                } else {
                    List<TraduccionTipo> llista = (List<TraduccionTipo>) f.get("traducciones");
                    List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();
                    for (int i = 0; i<llista.size(); i++) {
                    	
                        if (tipo.getTraducciones().containsKey(((Idioma) langs.get(i)).getLang())) {
                            tipo.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setNombre(llista.get(i).getNombre());
                            if (llista.get(i).getUri() == null || llista.get(i).getUri().isEmpty()) {
                            	final AjaxCheckUriAction ajax = new AjaxCheckUriAction();
                            	Long codigoTipo = null;
                            	if (tipo.getTraducciones().get(((Idioma) langs.get(i)).getLang()).getId() != null) {
                            		codigoTipo = tipo.getTraducciones().get(((Idioma) langs.get(i)).getLang()).getId().getCodigoTipo();
                            	}
        						final String nuevaUri = ajax.check(Cadenas.string2uri(llista.get(i).getNombre()), 
        															UriType.TPI_URI, 
        															((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().toString(), 
        															((Idioma) langs.get(i)).getLang() , 
        															codigoTipo, 
        															0);
        						llista.get(i).setUri(nuevaUri);
        						tipo.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setUri(llista.get(i).getUri());
                            } else {
                            	tipo.getTraducciones().get(((Idioma) langs.get(i)).getLang()).setUri(llista.get(i).getUri());
                            }
                        } else {
                            TraduccionTipo traduccio = new TraduccionTipo();
                            traduccio.setNombre(llista.get(i).getNombre());
                            if (!llista.get(i).getNombre().isEmpty() && (llista.get(i).getUri() == null || llista.get(i).getUri().isEmpty())) {
                            	final AjaxCheckUriAction ajax = new AjaxCheckUriAction();
        						final String nuevaUri = ajax.check(llista.get(i).getNombre(), 
        															UriType.TPI_URI, 
        															((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().toString(), 
        															((Idioma) langs.get(i)).getLang() , 
        															null, 
        															0);
        						llista.get(i).setUri(nuevaUri);
        						traduccio.setUri(llista.get(i).getUri());
                            } else {
                            	traduccio.setUri(llista.get(i).getUri());
                            }
                           

        					if (traduccio.getId() == null) {
        						TraduccionTipoPK tradId = new TraduccionTipoPK();
        						traduccio.setId(tradId);
        					}
                            traduccio.getId().setCodigoTipo(tipo.getId());
                            traduccio.getId().setCodigoIdioma(((Idioma) langs.get(i)).getLang());
                            tipo.getTraducciones().put(((Idioma) langs.get(i)).getLang(), traduccio);
                        }
                    }
                    
                    ///TODO
                    f.set("traducciones", llista);
                }

                List<String> eliminar = new ArrayList<String>();
                for (String lang : tipo.getTraducciones().keySet()) {
                	TraduccionTipo trad = tipo.getTraducciones().get(lang);
                    if (trad.getNombre().equals("") && trad.getUri().equals("")) {
                        eliminar.add(lang);
                    } else if (trad.getUri().equals("")) {
                        final AjaxCheckUriAction ajax = new AjaxCheckUriAction();
                        Long codigoTipo = null;
                        if (trad.getId() != null) {
                        	codigoTipo = trad.getId().getCodigoTipo();
                        }
						final String nuevaUri = ajax.check(Cadenas.string2uri(trad.getNombre()),  UriType.TPI_URI,  ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().toString(),  lang, codigoTipo, 0);
						trad.setUri(Cadenas.string2uri(nuevaUri));
                    }
                }
                for (String key : eliminar) {
                    tipo.getTraducciones().remove(key);
                }
                
                
                bdTipo.grabarTipo(tipo);
                
                


                if (f.get("id") == null) {
                    addMessageWithDate(request, "mensa.nuevotipo");
                    f.set("id", tipo.getId());
                } else {
                    addMessageWithDate(request, "mensa.modiftipo");
                }

            } else if (request.getParameter("accion").equals(getResources(request).getMessage("operacion.traducir"))) {
                // Traducimos el Contenido
                traducir(request, f);
            }
           
            // combo para que clasifiquen los listados
            request.setAttribute("listaClasificacion", bdTipo.comboClasificacion(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId()));
            request.setAttribute("idmicrosite", ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId() );
            Base.micrositeRefresh(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId(), request);
            Base.menuRefresh(request);
            //VOUtils.populate(tipo, f);
            return mapping.findForward("detalle");
        }

        //********************************************************
        //********************** EDITAMOS ************************
        //********************************************************
        if (request.getParameter("id") != null) {
            Long id = new Long("" + request.getParameter("id"));

            // Combo para que clasifiquen los listados
            request.setAttribute("listaClasificacion", bdTipo.comboClasificacion(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId()));
            request.setAttribute("idmicrosite", ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId() );
            
            if (bdTipo.checkSite(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId(), id)) {
                addMessageError(request, "info.seguridad");
                return mapping.findForward("info");
            }

            tipo = bdTipo.obtenerTipo(id);
            //************COMPROBACION DE IDES*************
            if (tipo.getIdmicrosite().longValue() != ((Microsite) request.getSession().getAttribute("MVS_microsite")).getId().longValue()) {
                addMessageError(request, "peticion.error");
                return mapping.findForward("info");
            }
            //*********************************************

            f.set("tipoelemento", tipo.getTipoelemento());
            if (!tipo.getTipoelemento().equals(Tipo.TIPO_CONEXIO_EXTERNA)) {
                f.set("tampagina", new Integer(tipo.getTampagina()));
                f.set("tipopagina", tipo.getTipopagina());
                f.set("buscador", tipo.getBuscador());
                f.set("orden", tipo.getOrden());
                f.set("clasificacion", tipo.getClasificacion());

            } else {
                f.set("xjndi", tipo.getXjndi());
                f.set("xurl", tipo.getXurl());
                f.set("xusr", tipo.getXusr());
                f.set("xpwd", tipo.getXpwd());
                f.set("xid", tipo.getXid());
                f.set("clasificacion_ext", tipo.getClasificacion());
            }

            if (tipo.getTipoelemento().equals(Tipo.TIPO_FOTO)) {
                f.set("fotosporfila", tipo.getFotosporfila());
            }

            TraDynaActionForm fdet = (TraDynaActionForm) form;
            VOUtils.describe(fdet, tipo);  // bean --> form

            Base.micrositeRefresh(((Microsite) request.getSession().getAttribute("MVS_microsite")).getId(), request);
            Base.menuRefresh(request);
            
            return mapping.findForward("detalle");
        }

        addMessageError(request, "peticion.error");
        return mapping.findForward("info");
    }
    
    /**
     * Método que traduce un formulario de Tipo (listado)
     * @param request     petición de usuario
     * @param tipoForm    formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void traducir (HttpServletRequest request, TraDynaActionForm tipoForm) throws Exception  {	

    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getServletContext().getAttribute("traductor");
    		String idiomaOrigen = "ca";

            TraduccionTipo tipoOrigen = (TraduccionTipo) tipoForm.get("traducciones", 0);
            Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");

            Iterator<?> itTradFichas = ((ArrayList<?>) tipoForm.get("traducciones")).iterator();                
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            List<String> idiomasMicro = Arrays.asList(micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
            
            while (itLang.hasNext()){

            	String idiomaDesti = itLang.next();
            	TraduccionTipo tipoDesti = (TraduccionTipo) itTradFichas.next();
	
			   	if (tipoDesti == null) {
			   		micrositeBean.setTraduccion(idiomaDesti, new TraduccionTipo());
			   		tipoDesti = (TraduccionTipo) micrositeBean.getTraduccion(idiomaDesti);
			   	}
            	
            	
            	//Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
            	if (idiomasMicro.contains(idiomaDesti)) {

	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(tipoOrigen, tipoDesti)) {
	            			request.setAttribute("mensajes", "traduccioCorrecte");
	            		}
	            		else {
	            			request.setAttribute("mensajes", "traduccioIncorrecte");
	            			break;
	            		}
	            	}
            	}
            }
            
			if (request.getAttribute("mensajes").equals("traduccioCorrecte")) addMessage(request, "mensa.traduccion.confirmacion");
			 else addMessageError(request, "mensa.traduccion.error");
           
			log.info("Traducción tipo - Id: " + (Long) tipoForm.get("id"));
    }
    
    
}
