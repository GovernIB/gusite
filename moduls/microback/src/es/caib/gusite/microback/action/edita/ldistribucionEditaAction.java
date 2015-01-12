package es.caib.gusite.microback.action.edita;


import java.io.FileInputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.ldistribucionForm;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.micromodel.Correo;
import es.caib.gusite.micromodel.ListaDistribucion;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.LDistribucionDelegate;

/**
 * Action que edita los convocatorias de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/distribucionEdita" <BR> 
 *  name="convocatoriaForm" <BR> 
 *  input="/convocatoriasAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleDistribucion.jsp" <BR>
 *  forward name="info" path="/infoDistribucion.jsp"
 *  
 *  @author Salvador Antich
 */
public class ldistribucionEditaAction extends BaseAction 
{

	protected static Log log = LogFactory.getLog(ldistribucionEditaAction.class);


	public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {
		ldistribucionForm lDistribForm = (ldistribucionForm)form;
		//********************************************************
        //*********** EDICION / CREACION DE LA LISTA DE DISTRIBUCION **********
        //********************************************************

    	if 	((String)request.getParameter("accion") != null) {
    		
    		if (request.getParameter("accion").equals("crearDistrib")) {
    			request.setAttribute("destinatarios", new ArrayList());
    			lDistribForm.resetForm();       		
    			return mapping.findForward("detalle");
    			
    		}else if (request.getParameter("accion").equals("guardarDistrib")) {
    			ListaDistribucion distrib = null;
				try{
					distrib = guardar(form, request);		        
				}catch(Exception e){
					addMessage(request, "peticion.error");
		            return mapping.findForward("info");
				}
				if(lDistribForm.getId() != null)
					addMessage(request, "mensa.modifldistribucion");
				else
					addMessage(request, "mensa.nuevaldistribucion");
	       		addMessage(request, "mensa.editarldistribucion", "" + distrib.getId());
	           	addMessage(request, "mensa.listaldistribucion");
		       	return mapping.findForward("info");
		       	
			}else if(request.getParameter("accion").equals("borrarDistrib")) {
				
				LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
				distribDelegate.borrarListaDistribucion(lDistribForm.getId());
				
	       		addMessage(request, "mensa.listaldistribucionborradas", "" + lDistribForm.getId());
	           	addMessage(request, "mensa.listaldistribucion");	           	
	           	return mapping.findForward("info");
	           	
	        }else if (request.getParameter("accion").equals("guardarCorreo")) {
	        	LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
	        	distribDelegate.anadeCorreo(lDistribForm.getId(), lDistribForm.getEmail());
	        	lDistribForm.setEmail("");
	        	addMessage(request, "mensa.anadecorreo");
	       		addMessage(request, "mensa.editarldistribucion", "" + lDistribForm.getId());
	           	addMessage(request, "mensa.listaldistribucion");
	        	return mapping.findForward("info");

	        }else if (request.getParameter("accion").equals("borrar")) {
	        	LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
	        	if(lDistribForm.getSeleccionados()!=null){
		        	for (int i = 0; i < lDistribForm.getSeleccionados().length; i++){
		        		if (!distribDelegate.borrarCorreo(lDistribForm.getId(), lDistribForm.getSeleccionados()[i])){	        			
				       		//addMessage(request, "mensa.editarldistribucion", "" + lDistribForm.getSeleccionados()[i]);
		        		}
		        	}
	        	}
	        	lDistribForm.setEmail("");
	        	addMessage(request, "mensa.quitacorreo");
	       		addMessage(request, "mensa.editarldistribucion", "" + lDistribForm.getId());
	           	addMessage(request, "mensa.listaldistribucion");
	        	return mapping.findForward("info");
	        	
	        }else if (request.getParameter("accion").equals("importar")) {
	        	LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();	        	
	        	String[] fileImport = new String(lDistribForm.getUpLoad().getFileData()).trim().split("\n");
	        	ArrayList sinImportar = new ArrayList();
	        	for(String s:fileImport){ //"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
	        		s = s.replace("\r", "").replace("\t","").replace("\n", "");
	        		String[] col = s.split(";");
	        		col[0] = col[0].replace(" ", "");
	        		if (col[0].matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")){
	        			distribDelegate.anadeCorreo(lDistribForm.getId(), col[0]);
	        			if (col.length == 3) distribDelegate.actualizaCorreo(new Correo (col[0],col[1],col[2],false));
	        			else if (col.length == 1) distribDelegate.actualizaCorreo(new Correo (col[0],"","",false));
	        		}else{
	        			sinImportar.add(col[0]);
	        		}
	        	}
	        	log.info("Correos sin importar: " + sinImportar);
	        	addMessage(request, "mensa.anadecorreo");
	        	if (!sinImportar.isEmpty()) addMessage(request, "mensa.sinImportar", "" + sinImportar);
	       		addMessage(request, "mensa.editarldistribucion", "" + lDistribForm.getId());
	           	addMessage(request, "mensa.listaldistribucion");
	        	return mapping.findForward("info");
	        	
	        }else if (request.getParameter("accion").equals("exportar")) {
	        	LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
	        	StringBuffer sbfOut = distribDelegate.exportarListaDistribucion(new Long(request.getParameter("id")));
	    	    if (sbfOut != null) {
	                response.reset();
	                response.setContentType("plain/text");
	                response.setHeader("Content-Disposition", "attachment; filename=\"" + "lista_distribucion_" + lDistribForm.getNombre() + ".csv" + "\"");
	                response.setContentLength(sbfOut.length());
	                response.getWriter().write(sbfOut.toString());
	            }
	            return null;
	        	
	        }
    	}else if (request.getParameter("id")!=null) { 
    		try{
        		recuperar(form, request);	                
        	}catch(Exception e){
        		addMessage(request, "peticion.error");
                return mapping.findForward("info");
        	}
            return mapping.findForward("detalle");
        }
		request.setAttribute("lDistribucionForm", lDistribForm);
		//Refresco de parámetro MVS de menú
		Base.menuRefresh(request);

		return mapping.findForward("detalle");    
	}
	
	private ListaDistribucion guardar(ActionForm form, HttpServletRequest request) throws Exception{
		ldistribucionForm lDistribForm = (ldistribucionForm)form;
		LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
		Long idMicrosite = ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue();
		ListaDistribucion listaDistrib = null;
		
		if (lDistribForm.getId() == null || lDistribForm.getId() == 0) { // ALTA
			listaDistrib = new ListaDistribucion();
        } else { 							// MODIFICACION
        	listaDistrib = distribDelegate.obtenerListaDistribucion(lDistribForm.getId());
           	//COMPROBACION DEL id del microsite
           	if (!listaDistrib.getMicrosite().getId().equals(idMicrosite)){
           		throw new Exception();
           	}
        }
		Microsite microsite = DelegateUtil.getMicrositeDelegate().obtenerMicrosite(idMicrosite);
		listaDistrib.setMicrosite(microsite);
    	listaDistrib.setNombre(lDistribForm.getNombre());    	
    	listaDistrib.setDescripcion(lDistribForm.getDescripcion());
    	listaDistrib.setPublico((lDistribForm.getPublico() != null)? lDistribForm.getPublico().equals("S"):false);
    	listaDistrib.setDestinatarios(lDistribForm.getDestinatarios());
    	distribDelegate.grabarListaDistribucion(listaDistrib);
    	return listaDistrib;
	}
	
	private void recuperar(ActionForm form, HttpServletRequest request)throws Exception{
		ldistribucionForm lDistribForm = (ldistribucionForm)form;
		LDistribucionDelegate distribDelegate = DelegateUtil.getLlistaDistribucionDelegate();
		ListaDistribucion listaDistrib = distribDelegate.obtenerListaDistribucion(new Long (request.getParameter("id")));
    	//request.setAttribute("titconvocatoria",((TraduccionConvocatoria)enc.getTraduccion()).getTitulo());
        
        //************COMPROBACION DE IDES*************
    	if (listaDistrib.getMicrosite().getId()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
    	{
    		throw new Exception();
    	}
    	//*********************************************
    	lDistribForm.setId(listaDistrib.getId());
    	lDistribForm.setNombre(listaDistrib.getNombre());
    	lDistribForm.setPublico((listaDistrib.getPublico())?"S":"N");
    	lDistribForm.setDescripcion(listaDistrib.getDescripcion());
    	lDistribForm.setDestinatarios(listaDistrib.getDestinatarios());
    	request.setAttribute("destinatarios", listaDistrib.getDestinatarios());
    	request.setAttribute("nreg", listaDistrib.getDestinatarios().size());
	}
}

