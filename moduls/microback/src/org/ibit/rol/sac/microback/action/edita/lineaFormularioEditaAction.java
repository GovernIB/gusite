package org.ibit.rol.sac.microback.action.edita;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.lineaFormularioForm;
import org.ibit.rol.sac.microback.utils.Cadenas;
import org.ibit.rol.sac.micromodel.Lineadatocontacto;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionLineadatocontacto;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.ContactoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;

import es.indra.gusite.microintegracion.traductor.TraductorMicrosites;

/**
 * Action que edita las lineas de formulario de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/lineaformularioEdita" <BR> 
 *  name="lineaFormularioForm" <BR> 
 *  input="/lineaFormuAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleLineaFormulario.jsp"
 *  
 *  @author Indra
 */
public class lineaFormularioEditaAction extends BaseAction 
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
	
	protected static Log log = LogFactory.getLog(lineaFormularioEditaAction.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	ContactoDelegate contactoDelegate = DelegateUtil.getContactoDelegate();	
    	lineaFormularioForm lineaFormularioForm = (lineaFormularioForm) form;
    		
			/********************** CREAR ************************/	
    		if	((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.crear"))) {
				
				Long idContacto= (Long)lineaFormularioForm.get("idcontacto");
				if(idContacto == null){
					idContacto = new Long((""+ request.getParameter("idcontacto")));
				}
				lineaFormularioForm.resetForm(mapping, request);
				lineaFormularioForm.set("idcontacto",idContacto );
		    	request.setAttribute("lineaFormularioForm", lineaFormularioForm);
		    	return mapping.findForward("detalle");
		    	
		    /********************** EDITAR ************************/	
			}else if	((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.editar"))) {
				
				Long idContacto = new Long((""+ request.getParameter("idcontacto")));
				setBeantoForm (lineaFormularioForm,idContacto);
				return mapping.findForward("detalle");
	    	/********************** GUARDAR *************************/
			}else if ((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.guardar"))) {
	        	
	    		Lineadatocontacto lineaDatoContacto = setFormtoBean(lineaFormularioForm);
	
	        	contactoDelegate.creamodificaLinea(lineaDatoContacto, (Long)lineaFormularioForm.get("idcontacto") );
	        	        	
		       	if(lineaFormularioForm.get("id") == null){ 
		       		lineaFormularioForm.set("id",lineaDatoContacto.getId());
		       		addMessageWithDate(request, "formu.linea.mensa.nuevaagenda");
		       	} else addMessageWithDate(request, "formu.linea.mensa.modifagenda");
		       	
		       	return mapping.findForward("detalle");
	    		
	    	 /********************** TRADUCIR ************************/	
	    	}else if	((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.traducir"))) {
	 	   
	    		traducir (request, lineaFormularioForm);
	    		request.setAttribute("lineaFormularioForm", lineaFormularioForm);
	    		return mapping.findForward("detalle");

	       	 /********************** BORRAR ************************/	
	    	}else if	((""+request.getParameter("accion")).equals(getResources(request).getMessage("operacion.borrar"))) {
	
	    		Long idLinea= (Long)lineaFormularioForm.get("id");
	    		Long idContacto= (Long)lineaFormularioForm.get("idcontacto");
	    		
	    		ContactoDelegate bdFormu = DelegateUtil.getContactoDelegate();
	    		bdFormu.eliminarLinea(idLinea , idContacto);
	    		
	    		lineaFormularioForm.resetForm(mapping, request);
	    		lineaFormularioForm.set("idcontacto",idContacto );
	        	request.setAttribute("lineaFormularioForm", lineaFormularioForm);
	    		
	    		addMessageAlert(request, "mensa.listaformulinborradas");
	    		return mapping.findForward("detalle");
	    	}
    	
        addMessageError(request, "peticion.error");
        return mapping.findForward("info");
    }
    
    
    /**
     * Método que vuelca el formulario de una linea de Contacto en el Bean LineaDatoContacto
     * @author Indra
     * @param lineaFormularioForm 		formulario dinámico enviado por usuario
     * @return LineDatoContacto			Bean de tipo LineaDatoContacto
     * @throws Exception
     */
    private Lineadatocontacto setFormtoBean (lineaFormularioForm lineaFormularioForm) throws Exception  {	
    	
    	ContactoDelegate contactoDelegate = DelegateUtil.getContactoDelegate();
    	Lineadatocontacto lineaDatoContacto = new Lineadatocontacto();;
		
		if (lineaFormularioForm.get("id") == null)   
			lineaDatoContacto.setIdcontacto( (Long)lineaFormularioForm.get("idcontacto"));
			else lineaDatoContacto = contactoDelegate.obtenerLinea((Long)lineaFormularioForm.get("id"));
	
		if (lineaFormularioForm.get("lineas")!=null)
			lineaDatoContacto.setLineas(Integer.parseInt(""+lineaFormularioForm.get("lineas")));
		if (lineaFormularioForm.get("visible")!=null)
			lineaDatoContacto.setVisible( ""+lineaFormularioForm.get("visible"));
		if (lineaFormularioForm.get("obligatorio")!=null)
			lineaDatoContacto.setObligatorio( Integer.parseInt(""+lineaFormularioForm.get("obligatorio")));
		if (lineaFormularioForm.get("orden")!=null)
			lineaDatoContacto.setOrden( Integer.parseInt(""+lineaFormularioForm.get("orden")));
		if (lineaFormularioForm.get("tamano")!=null)
			lineaDatoContacto.setTamano( Integer.parseInt(""+lineaFormularioForm.get("tamano")));
		lineaDatoContacto.setTipo( ""+lineaFormularioForm.get("tipo"));

    	List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
    	String[] etiquetas=(String[])lineaFormularioForm.get("etiq");
    	String[] textos=(String[])lineaFormularioForm.get("textos");
    	
    	//Creamos una tabla nueva para tratar lineas de formulario de tipo selector
    	//Se contactenan los string para guardar en campo texto de BBDD
    	String[] etiquetasTipo4o5 = new String[etiquetas.length];
    	
    	
    	if (lineaDatoContacto.getTipo().equals("4") || lineaDatoContacto.getTipo().equals("5")) { 
    		
    		String fila;
    		int limite=0;
    		if (lineaDatoContacto.getTipo().equals("4")) limite=Integer.parseInt(""+lineaFormularioForm.get("numSelectors"));
   			if (lineaDatoContacto.getTipo().equals("5")) {
   				limite=Integer.parseInt(""+lineaFormularioForm.get("numSelectors2"));
   				lineaDatoContacto.setLineas(Integer.parseInt(""+lineaFormularioForm.get("numSelectors2")));
   			}

   			for (int j=0; j<langs.size(); j++) {
   				fila=""+etiquetas[j]+"#"; // El primero es la etiqueta
        		for (int i=0; i<limite;i++)	
       				fila+=textos[(i*langs.size())+j]+"#";
        		if (fila.length()>0)
        			etiquetasTipo4o5[j]=fila.substring(0,fila.length()-1);
   			}
	
    	}
    	
        Map<String, TraduccionLineadatocontacto> mapaTraducciones = new HashMap<String, TraduccionLineadatocontacto>(langs.size());
        TraduccionLineadatocontacto traduccionLineaDatoContacto;
        
        for (int i = 0; i < langs.size(); i++) {
            String lang = (String) langs.get(i);
            traduccionLineaDatoContacto=new TraduccionLineadatocontacto();
            
            if (etiquetasTipo4o5[i] != null) traduccionLineaDatoContacto.setTexto(etiquetasTipo4o5[i]);
            else traduccionLineaDatoContacto.setTexto(etiquetas[i]);

            mapaTraducciones.put(lang, traduccionLineaDatoContacto);
        }
        lineaDatoContacto.setTraduccionMap(mapaTraducciones);  
 	
        return lineaDatoContacto;
    }
    
    
    /**
     * Método que vuelca el formulario de una linea de Contacto en el Bean LineaDatoContacto
     * @author Indra
     * @param lineaFormularioForm 		formulario dinámico enviado por usuario
     * @return LineDatoContacto			Bean de tipo LineaDatoContacto
     * @throws Exception
     */
    private void setBeantoForm (lineaFormularioForm lineaFormularioForm, Long idLineaContacto) throws Exception  {	
    	
    	ContactoDelegate contactoDelegate = DelegateUtil.getContactoDelegate();
    	Lineadatocontacto lineaDatoContactoBean = contactoDelegate.obtenerLinea(idLineaContacto);
    	
		lineaFormularioForm.set("id", lineaDatoContactoBean.getId());
		lineaFormularioForm.set("idcontacto", lineaDatoContactoBean.getIdcontacto());
		lineaFormularioForm.set("visible", lineaDatoContactoBean.getVisible());
		lineaFormularioForm.set("tamano", new Integer(lineaDatoContactoBean.getTamano()));
		lineaFormularioForm.set("lineas",  new Integer(lineaDatoContactoBean.getLineas()));
		lineaFormularioForm.set("obligatorio",  new Integer(lineaDatoContactoBean.getObligatorio()));
		lineaFormularioForm.set("orden",  new Integer(lineaDatoContactoBean.getOrden()));
		lineaFormularioForm.set("tipo", lineaDatoContactoBean.getTipo());
		
		IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
		List<?> idis=delegate.listarLenguajes();
        int numLangs = idis.size();
        String[] etis = new String[numLangs];
        String[] textos= new String[numLangs*Lineadatocontacto.NUMERO_MAXIMO_SELECTOR];
        int max_lin=-1000;
        String trad_texto="";
		Integer resultVectorlenght= new Integer(0);
        for (int i = 0; i < numLangs; i++) {
        	if ( lineaDatoContactoBean.getTraduccion( ""+idis.get(i) )!=null ) {
        		trad_texto=((TraduccionLineadatocontacto)lineaDatoContactoBean.getTraduccion( ""+idis.get(i) )).getTexto();
        		String[] vector= trad_texto.split("#");
                
        		if (lineaDatoContactoBean.getTipo().equals("4") || lineaDatoContactoBean.getTipo().equals("5")){
        			for (int m=0; m<vector.length; m++) {
        				if (m==0) 	etis[i]=vector[0]; // El primero es la etiqueta
        				if (m>0)    textos[(numLangs*(m-1))+i]=vector[m];
        			}
            	    int cont = Cadenas.countSubstr(trad_texto, "#");
        			if (cont > 0 ){
        				if ((cont) > resultVectorlenght.intValue()){
        					resultVectorlenght = new Integer(cont);
        				}
        				lineaFormularioForm.set("numOptions",resultVectorlenght.toString());
        			}
        		}else{ 
       				etis[i]=trad_texto;
        		}
        		if (vector.length>max_lin) max_lin=vector.length;
        	}
        }
		if (lineaDatoContactoBean.getTipo().equals("4")) lineaFormularioForm.set("numSelectors", new Integer(max_lin-1) );
		if (lineaDatoContactoBean.getTipo().equals("5")) lineaFormularioForm.set("numSelectors2", new Integer(max_lin-1) );
		lineaFormularioForm.set("etiq", etis);
		lineaFormularioForm.set("textos", textos);
    }    
    
    /**
     * Método que traduce un formulario de una linea de formulario
     * @author Indra
     * @param request			petición de usuario
     * @param contenidoForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void traducir (HttpServletRequest request, lineaFormularioForm lineaFormularioForm) throws Exception  {	

    		TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getAttribute("traductor");
            Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");
    		String idiomaOrigen = "ca";
    		String [] textoSelector = null;
      
    		TraduccionLineadatocontacto lineadatocontactoOrigen = new TraduccionLineadatocontacto();

    		boolean boolSelector = false;
    		int limiteSelector=0;
        	if (lineaFormularioForm.get("tipo").equals("4") || lineaFormularioForm.get("tipo").equals("5")) { 
        	  	textoSelector = (String[])lineaFormularioForm.get("textos");
        		boolSelector = true;	
        		if (lineaFormularioForm.get("tipo").equals("4")) limiteSelector=Integer.parseInt(""+lineaFormularioForm.get("numSelectors"));
       			if (lineaFormularioForm.get("tipo").equals("5")) limiteSelector=Integer.parseInt(""+lineaFormularioForm.get("numSelectors2"));
       				
        	}
    	    		
            Iterator<String> itLang = traductor.getListLang().iterator(); 
            int numLangs = 0;
            while (itLang.hasNext()){
            	itLang.next();
            	numLangs = numLangs + 1;
            }
            
    		String[] etiquetas=(String[])lineaFormularioForm.get("etiq");
    		lineadatocontactoOrigen.setTexto(etiquetas[0]);
            String[] etiq = new String[traductor.getListLang().size()];
            int indexIdioma =0 ;
            etiq[0] = etiquetas[0];
           
           itLang = traductor.getListLang().iterator(); 
            while (itLang.hasNext()){
            	
            	String idiomaDesti = itLang.next();
            	TraduccionLineadatocontacto lineadatocontactoDesti = new TraduccionLineadatocontacto();
            	lineadatocontactoDesti.setTexto(etiquetas[indexIdioma]);
			   	//Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
            	if (micrositeBean.getIdiomas().contains(idiomaDesti)) {

	            	if (!idiomaOrigen.equals(idiomaDesti)) {
	            		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
	            		
	            		if (traductor.traducir(lineadatocontactoOrigen, lineadatocontactoDesti,boolSelector,limiteSelector,numLangs, indexIdioma, textoSelector)) {
	            			etiq[indexIdioma]=lineadatocontactoDesti.getTexto();
	            			
	            			request.setAttribute("mensajes", "traduccioCorrecte");
	            		}
	            		else {
	            			request.setAttribute("mensajes", "traduccioIncorrecte");
	            			break;
	            		}
	            	}
            	}
            	indexIdioma= indexIdioma + 1;
            }
            
            lineaFormularioForm.set("etiq",etiq);
            
            if(boolSelector == true)
            	lineaFormularioForm.set("textos",textoSelector);

			if (request.getAttribute("mensajes").equals("traduccioCorrecte"))
				addMessage(request, "mensa.traduccion.confirmacion");
			else 
				addMessageError(request, "mensa.traduccion.error");

			log.info("Traducción Linea Formulario - Id: " + (Long) lineaFormularioForm.get("id"));
    }    
    
    
    
}
