package org.ibit.rol.sac.microback.action.buscaordena;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.base.Base;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.UsuarioPropietarioMicrosite;
import org.ibit.rol.sac.micromodel.Usuario;
import org.ibit.rol.sac.micropersistence.delegate.AccesibilidadDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate;
import org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate;
import org.ibit.rol.sac.model.Idioma;
import org.ibit.rol.sac.model.TraduccionUA;
import org.ibit.rol.sac.model.UnidadAdministrativa;
import org.ibit.rol.sac.persistence.delegate.UnidadAdministrativaDelegate;

/**
 * Action que prepara el listado de microsites de Usuario <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/microUsuarios" <BR> 
 *  name="BuscaOrdenaMicroUsuarioActionForm" <BR> 
 *  forward name="listarUsuarios" path="/listaUsuariosMicrosite.jsp"<BR>
 *  forward name="listarMicrositesdelusu" path="/listaMicrositesdelusu.jsp"
 *  
 *  @author Indra
 */
public class BuscaOrdenaMicroUsuariosAction extends BaseAction {

	protected static Log log = LogFactory.getLog(org.ibit.rol.sac.microback.action.buscaordena.BuscaOrdenaMicroUsuariosAction.class);
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 
		 
	    	/***************************************************************/
	    	/***************   RECOGER USUARIO Y ROLES   *******************/
	    	/***************************************************************/    	
	    	Base.usuarioRefresh(request);
	    	
	    	
	    	/***************************************************************/
	    	/***************     NUEVO   USUARIO         *******************/
	    	/***************************************************************/ 
	        String accion= ""+request.getParameter("accion");
	        if ( accion.equals("nuevouser")) {
	        	Long idsite=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
	        	Long iduser = new Long(""+request.getParameter("iduser"));
	        	UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(idsite,iduser);
	        	MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
	        	bdMicro.grabarUsuarioPropietarioMicrosite(upm);
	        	
	        	addMessage(request, "micro.usuario.mensa.listausuarios");
	            addMessage(request, "micro.usuario.mensa.newuser.ok");
	            
	            return mapping.findForward("info");
	        }
	        if ( accion.equals("nuevomicro")) {
	        	Long idsite= new Long(""+request.getParameter("idmicro"));
	        	Long iduser = new Long(""+request.getParameter("iduser"));
	        	UsuarioPropietarioMicrosite upm = new UsuarioPropietarioMicrosite(idsite,iduser);
	        	MicrositeDelegate bdMicro = DelegateUtil.getMicrositeDelegate();
	        	bdMicro.grabarUsuarioPropietarioMicrosite(upm);
	        	
	        	String destino = ("<a href=\"microUsuarios.do?accion=lista&id="+iduser+"\">Llista de microsites de l'usuari</a>");
	        	request.setAttribute("destino",destino);
	        	//addMessage(request, destino);
	            addMessage(request, "micro.usuario.mensa.newmicro.ok");    
	              
	            return mapping.findForward("microusu");
	        }
	        
	         
	        if ( accion.equals("lista")) 
	        {	
	        	/***************************************************************/
		    	/***************     LISTADO  MICROSITES POR USUARIO       *****/
		    	/***************************************************************/
		        
	        	MicrositeDelegate microde = DelegateUtil.getMicrositeDelegate();
	        	List listamicros =microde.listarMicrodeluser(""+request.getParameter("id"));
	        	List listaresultante = microde.listarMicrositesThin();
	        	Iterator iter = listamicros.iterator();
	        	while (iter.hasNext()) {
	        		Microsite mic = (Microsite) iter.next();
	        		UnidadAdministrativaDelegate uniad=org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate();
	            	UnidadAdministrativa uo = uniad.obtenerUnidadAdministrativa(new Long(mic.getUnidadAdministrativa()));
	            	mic.setNombreUA(((TraduccionUA)uo.getTraduccion(Idioma.DEFAULT)).getNombre());
	            	
	    	     	AccesibilidadDelegate acces = DelegateUtil.getAccesibilidadDelegate();
	    	     	int nivelAcces = acces.existeAccesibilidadMicrosite(mic.getId());
	    	     	mic.setNivelAccesibilidad(nivelAcces);	            	
	            	
	        	} 
	        	iter = listaresultante.iterator();
	        	while (iter.hasNext()) {
	        		Microsite mic = (Microsite) iter.next();
	        		UnidadAdministrativaDelegate uniad=org.ibit.rol.sac.persistence.delegate.DelegateUtil.getUADelegate();
	            	UnidadAdministrativa uo = uniad.obtenerUnidadAdministrativa(new Long(mic.getUnidadAdministrativa()));
	            	mic.setNombreUA(((TraduccionUA)uo.getTraduccion(Idioma.DEFAULT)).getNombre());
	            		            	
	    	     	AccesibilidadDelegate acces = DelegateUtil.getAccesibilidadDelegate();
	    	     	int nivelAcces = acces.existeAccesibilidadMicrosite(mic.getId());
	    	     	mic.setNivelAccesibilidad(nivelAcces);	            	
	        	} 
	        	request.setAttribute("listatodos", listaresultante);
	        	request.setAttribute("listado", listamicros);
	        	request.setAttribute("elusu", (""+request.getParameter("id")));
	        	
		     	UsuarioDelegate usudel=DelegateUtil.getUsuarioDelegate();
		     	Usuario usu = usudel.obtenerUsuario(Long.valueOf(request.getParameter("id")));
		     		        	
	        	request.setAttribute("nomusu", (""+usu.getNombre()));
	        	
	        	return mapping.findForward("listarMicrositesdelusu");
	        }
	        else{  	
		    	/***************************************************************/
		    	/***************     LISTADO  USUARIOS       *******************/
		    	/***************************************************************/    	
		     	MicrositeDelegate micro = DelegateUtil.getMicrositeDelegate();
		     	List listausuarios =micro.listarUsernamesbyMicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
		     	
		     	UsuarioDelegate uDel = DelegateUtil.getUsuarioDelegate();
	        	List listaresultante = uDel.listarUsuariosPerfil("sacoper");
	        	List lista = uDel.listarUsuariosPerfil("sacsuper");
	        	listaresultante.addAll(lista);
	        	lista = uDel.listarUsuariosPerfil("sacadmin");
	        	listaresultante.addAll(lista);
	            	
	    		Comparator comp = new ProcedimientoComparator();
	    	  	Collections.sort(listaresultante, comp);
	    	  	
	            request.setAttribute("listatodos", listaresultante);
		     	request.setAttribute("listado", listausuarios);
			 
		     	return mapping.findForward("listarUsuarios");
	  }
	 }
	 
	  private static class ProcedimientoComparator implements Comparator<Object> {
		    public int compare(Object element1, Object element2) {
		    	
		    	String nom1 = ( ((Usuario)element1).getNombre()!=null )?((Usuario)element1).getNombre():"";
		    	String nom2 = ( ((Usuario)element2).getNombre()!=null )?((Usuario)element2).getNombre():"";
	    	
		    	return nom1.toLowerCase().compareTo(nom2.toLowerCase());
		    }
	  }	 		
}
