package org.ibit.rol.sac.microback.action.util;

import java.util.*;

import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microback.Microback;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.utils.betwixt.Configurator;
import org.ibit.rol.sac.microback.utils.log.MicroLog;
import org.ibit.rol.sac.micromodel.*;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action que exporta un microsite <P>
 * 	Definición Struts:<BR>
 *  action path="exportar"<BR> 
 *	scope="request" <BR>
 *  unknown="false"
 *  
 *  @author - Indra
 */
public class exportarAction extends BaseAction {

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

	protected static Log log = LogFactory.getLog(exportarAction.class);
	private static String[] roles = new String[]{"sacsystem", "sacadmin"};
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
    	MicrositeCompleto micro=null;
		Hashtable<String, String> rolenames=null;
		
    	// recoger usuario.....
    	if (request.getSession().getAttribute("MVS_usuario")==null) {
        	UsuarioDelegate usudel=DelegateUtil.getUsuarioDelegate();
        	Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
        	request.getSession().setAttribute("MVS_usuario", usu);	
    	}
    	if (request.getSession().getAttribute("rolenames")==null) {
           	if (request.getRemoteUser() != null) {
        		request.getSession().setAttribute("username", request.getRemoteUser());
                rolenames = new Hashtable<String, String>();
                for (int i = 0; i < roles.length; i++) 
                    if (request.isUserInRole(roles[i])) rolenames.put(roles[i],roles[i]);
                request.getSession().setAttribute("rolenames", rolenames);
            }        
    	}
    	
		// Solo podrán exportar los roles sacsystem y sacadmin
		rolenames=(Hashtable)request.getSession().getAttribute("rolenames");

		if (!(rolenames.contains(roles[0]) || rolenames.contains(roles[1]))) {
			addMessage(request, "peticion.error");
            return mapping.findForward("info");
		}
		
    	if (request.getParameter("idsite")!=null) {
			MicroLog.addLog("Inici Exportació Microsite: [" + request.getParameter("idsite") + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
			
    		Long idmicrosite= new Long(""+request.getParameter("idsite"));
    		micro = bdMicro.obtenerMicrositeCompleto(idmicrosite);
    		micro.setVersion(Microback.microsites_version);
    		micro.setServiciosSeleccionados(null);
    		
            response.setContentType("application/octet-stream");
            String contentDispositionHeader = "attachment; filename=\"microsite-" + ((TraduccionMicrosite)micro.getTraduccion()).getTitulo() + ".xml\"";
            response.setHeader( "Content-Disposition", contentDispositionHeader );
            BeanWriter beanWriter = new BeanWriter(response.getOutputStream(), "UTF-8" );
            beanWriter.writeXmlDeclaration("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            Configurator.configure(beanWriter);
            beanWriter.setEndTagForEmptyElement(true);
            micro= LimpiarIdiomasVacios(micro);
            beanWriter.write(micro);
            beanWriter.close();
            
            MicroLog.addLog("Fi Exportació Microsite: [" + request.getParameter("idsite") + "] , Usuari: [" + request.getSession().getAttribute("username") + "]");
            
    	}
		
        return null;
	
	}

	/* Debemos grabar solo en el XML los maps que no tengan valor nulo puesto 
	 * que betwixt no lo hace por nosotros
	 */ 
	private MicrositeCompleto LimpiarIdiomasVacios (MicrositeCompleto mic) {

		Set lista;
		Iterator<?> it;
		
		// Idiomas del microsite
		mic.setTraduccionMap(ActualizaMapa (new TraduccionMicrosite(), mic.getTraduccionMap()));

		// betwixt no deja suprimir una propiedad de una propiedad
		if (mic.getImagenCampanya()!=null) mic.getImagenCampanya().setId(null);
		if (mic.getImagenPrincipal()!=null) mic.getImagenPrincipal().setId(null);
		if (mic.getEstiloCSS()!=null) mic.getEstiloCSS().setId(null);
		
		// Actividades de la agenda
		lista= mic.getActividades();
		it=lista.iterator();
		while (it.hasNext())  {
			Actividadagenda act=(Actividadagenda)it.next();
			act.setTraduccionMap(ActualizaMapa (new TraduccionActividadagenda(), act.getTraduccionMap()));
		}

		// Agendas
		lista= mic.getAgendas();
		it=lista.iterator();
		while (it.hasNext())  {
			Agenda age=(Agenda)it.next();
			age.setTraduccionMap(ActualizaMapa (new TraduccionAgenda(), age.getTraduccionMap()));
		}
		
		// Banner
		lista= mic.getBanners();
		it=lista.iterator();
		while (it.hasNext())  {
			Banner ban=(Banner)it.next();
			ban.setTraduccionMap(ActualizaMapa (new TraduccionBanner(), ban.getTraduccionMap()));
		}
		
		// Formularios de contacto, las traducciones en las líneas (LineaDatoContacto)
		Set listaforms= mic.getFormularioscontacto();
		Iterator<?> it_forms=listaforms.iterator(); // lista de formularios
		while (it_forms.hasNext())  { // para cada formulario busco sus lineas
			Contacto formcontacto=(Contacto)it_forms.next();
			lista = formcontacto.getLineasdatocontacto(); // lista de lineas
			it=lista.iterator();
			while (it.hasNext())  {
				Lineadatocontacto lin=(Lineadatocontacto)it.next();
				lin.setTraduccionMap(ActualizaMapa (new TraduccionLineadatocontacto(), lin.getTraduccionMap()));
			}		
		}		
		
		// Menus y contenidos
		lista= mic.getMenus();
		it=lista.iterator();
		while (it.hasNext())  {
			Menu men=(Menu)it.next();
			if (men.getImagenmenu()!=null) men.getImagenmenu().setId(null);
			men.setTraduccionMap(ActualizaMapa (new TraduccionMenu(), men.getTraduccionMap()));
			
			Iterator<?> it1=men.getContenidos().iterator();
			while (it1.hasNext())  {
				Contenido con=(Contenido)it1.next();
				if (con.getImagenmenu()!=null) con.getImagenmenu().setId(null);
				con.setTraduccionMap(ActualizaMapa (new TraduccionContenido(), con.getTraduccionMap()));

			}	
			
		}	
		
		// Noticia
		lista= mic.getNoticias();
		it=lista.iterator();
		while (it.hasNext())  {
			Noticia not=(Noticia)it.next();
			if (not.getImagen()!=null) not.getImagen().setId(null);
			not.setTraduccionMap(ActualizaMapa (new TraduccionNoticia(), not.getTraduccionMap()));
		}
		
		// Faq
		lista= mic.getFaqs();
		it=lista.iterator();
		while (it.hasNext())  {
			Faq faq=(Faq)it.next();
			faq.setTraduccionMap(ActualizaMapa (new TraduccionFaq(), faq.getTraduccionMap()));
		}

		
		// Temafaq
		lista= mic.getTemas();
		it=lista.iterator();
		while (it.hasNext())  {
			Temafaq tema=(Temafaq)it.next();
			tema.setTraduccionMap(ActualizaMapa (new TraduccionTemafaq(), tema.getTraduccionMap()));
		}	

		// Tipo noticias
		lista= mic.getTiponotis();
		it=lista.iterator();
		while (it.hasNext())  {
			Tipo tp=(Tipo)it.next();
			tp.setTraduccionMap(ActualizaMapa (new TraduccionTipo(), tp.getTraduccionMap()));
		}	

		// Componentes
		lista= mic.getComponentes();
		it=lista.iterator();
		while (it.hasNext())  {
			Componente comp=(Componente)it.next();
			if (comp.getImagenbul()!=null) comp.getImagenbul().setId(null);
			comp.setTraduccionMap(ActualizaMapa (new TraduccionComponente(), comp.getTraduccionMap()));
		}	
		
		// Encuestas
		lista= mic.getEncuestas();
		it=lista.iterator();
		while (it.hasNext())  {
			Encuesta encu=(Encuesta)it.next();
			encu.setTraduccionMap(ActualizaMapa (new TraduccionEncuesta(), encu.getTraduccionMap()));
			
			Iterator<?> it1=encu.getPreguntas().iterator();
			while (it1.hasNext())  {
				Pregunta pre=(Pregunta)it1.next();
				if (pre.getImagen()!=null) pre.getImagen().setId(null);
				pre.setTraduccionMap(ActualizaMapa (new TraduccionPregunta(), pre.getTraduccionMap()));

				Iterator<?> it2=pre.getRespuestas().iterator();
				while (it2.hasNext())  {
					Respuesta res=(Respuesta)it2.next();
					res.setTraduccionMap(ActualizaMapa (new TraduccionRespuesta(), res.getTraduccionMap()));
				}	
			}	
			
		}	
		
		// Frqssi
		lista= mic.getFrqssis();
		it=lista.iterator();
		while (it.hasNext())  {
			Frqssi frq=(Frqssi)it.next();
			frq.setTraduccionMap(ActualizaMapa (new TraduccionFrqssi(), frq.getTraduccionMap()));
		}	
		
		return mic;
	}
	
	private Map<String, Object> ActualizaMapa (Object clazz, Map<?, ?> mapa1) {
		Map<String, Object> trads = new HashMap<String, Object>();
		String key="";

		Iterator<?> it = mapa1.keySet().iterator();
		while (it.hasNext())  {
			key = (String)it.next( );
			if (mapa1.get(key)!=null) 	trads.put(key, mapa1.get(key));
			else					     	trads.put(key, clazz);
			
			// Elimino los id de los documentos e imagenes en traducciones
			if (mapa1.get(key)!=null) {
				if (clazz instanceof TraduccionNoticia) {
					TraduccionNoticia obj = (TraduccionNoticia)mapa1.get(key);
					if (obj.getDocu()!=null) obj.getDocu().setId(null);
				}
				if (clazz instanceof TraduccionBanner) {
					TraduccionBanner obj = (TraduccionBanner)mapa1.get(key);
					if (obj.getImagen()!=null) obj.getImagen().setId(null);
				}
				if (clazz instanceof TraduccionAgenda) {
					TraduccionAgenda obj = (TraduccionAgenda)mapa1.get(key);
					if (obj.getDocumento()!=null) obj.getDocumento().setId(null);
					if (obj.getImagen()!=null) obj.getImagen().setId(null);
				}
			}
			
	   }
		
		return trads;
	}
}
