package es.caib.gusite.microback.action.edita;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.struts.upload.FormFile;

import es.caib.gusite.microback.action.BaseAction;
import es.caib.gusite.microback.actionform.formulario.menuForm;
import es.caib.gusite.microback.base.Base;
import es.caib.gusite.microintegracion.traductor.TraductorMicrosites;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Menu;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionMenu;
import es.caib.gusite.micromodel.TraduccionMenuPK;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.IdiomaDelegate;
import es.caib.gusite.micropersistence.delegate.MenuDelegate;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;

/**
 * Action que edita los menus de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/menuEdita" <BR> 
 *  name="menuForm" <BR> 
 *  input="/menuAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleMenu.jsp" <BR>
 *  forward name="info" path="/infoContenido.jsp"
 *  
 *  @author Indra
 */
public class menuEditaAction extends BaseAction 
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
	
	protected static Log log = LogFactory.getLog(menuEditaAction.class);

	
    public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception {

    	MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();

    	String accion=""+request.getParameter("accion");
    	menuForm menuForm = (menuForm) form;
    	Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	ArrayList<?> listaObjetosMenu = menuDelegate.ObtenerObjetosMenu(micrositeBean.getId());


    	/** Crear Menú **/
    	if(accion.equals(getResources(request).getMessage("menu.accion.crear"))) {
    		
    		Menu menu = setFormtoBean(menuForm, micrositeBean.getId());
   		   	menuDelegate.grabarMenu(menu);
   		   	listaObjetosMenu = menuDelegate.ObtenerObjetosMenu(micrositeBean.getId());
   		   	addMessageWithDate(request, "menu.info.nuevo.menu");
       		request.getSession().setAttribute("cuenta_" + micrositeBean.getId(),
       				new Integer(Integer.parseInt("" + request.getSession().getAttribute("cuenta_" + micrositeBean.getId()))+1));
    	} 
    	
    	/** Borrar menú **/
    	else if (accion.startsWith(getResources(request).getMessage("menu.accion.borrar"))) {
    		
			Long idMenu = new Long(accion.substring(6));

   			if (menuDelegate.checkSite(micrositeBean.getId(), idMenu)) {
   				addMessageError(request, "info.seguridad");
   				return mapping.findForward("info");
   			}

   			//Antes de borrar, reordenamos
    		listaObjetosMenu = setFormtoArrayAndSave(menuForm, micrositeBean);

   			Menu menu = menuDelegate.obtenerMenu(idMenu);
   			menuDelegate.borrarMenu(idMenu);
   			menuDelegate.Reordena(menu.getOrden(), 'b', menu.getMicrosite().getId());

   			listaObjetosMenu = menuDelegate.ObtenerObjetosMenu(micrositeBean.getId());
   	   	  	addMessageWithDate(request, "menu.info.modifica.menu");
   	   	   	request.getSession().setAttribute("cuenta_"+micrositeBean.getId(),new Integer(listaObjetosMenu.size()));
	
    	}     	
    	
    	/** Guardar **/
    	else if(accion.equals(getResources(request).getMessage("operacion.guardar"))) {

    		//Recogemos los datos del formulario y actualizamos Menú en BBDD
    		//Retorna una lista con los objetos actualizados
    		listaObjetosMenu = setFormtoArrayAndSave(menuForm, micrositeBean);
   		   	addMessageWithDate(request, "menu.info.modifica.menu");
   		   	request.getSession().setAttribute("cuenta_"+micrositeBean.getId(),new Integer(listaObjetosMenu.size()));
   		
    	}  
    	
    	/** Traducir árbol de menús **/
    	else if (accion.equals(getResources(request).getMessage("operacion.traducir"))) {
    		
    		traducir (request, menuForm);
    		return mapping.findForward("detalle");
    	}

    	/** Mostrar árbol de menús **/
     	else request.getSession().setAttribute("cuenta_"+ micrositeBean.getId(),new Integer(listaObjetosMenu.size()));

        	setArraytoForm(listaObjetosMenu, menuForm);
        	Base.menuRefresh(request);

    		return mapping.findForward("detalle");
    }
    
    
    /**
     * Método que vuelca los datos de la Array de objetos de menú al formulario
     * @author Indra
     * @param listaObjetosMenu	ArrayList de objetos de menú
     * @param menuForm			formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void setArraytoForm (ArrayList<?> listaObjetosMenu, menuForm menuForm) throws Exception  {	
    	
		Long[] ids=new Long[listaObjetosMenu.size()];
		String[] visibles=new String[listaObjetosMenu.size()];
		String[] modos=new String[listaObjetosMenu.size()];
		Integer[] ordenes=new Integer[listaObjetosMenu.size()];
		Long[] idPadres=new Long[listaObjetosMenu.size()];
		String[] tipos=new String[listaObjetosMenu.size()];
		Integer numeroObjectes=new Integer(listaObjetosMenu.size());
		
		Long imagenMenuids[]=new Long[listaObjetosMenu.size()]; 
		String imagenMenunoms[]=new String[listaObjetosMenu.size()];
		FormFile imagenMenuArchivos[]=new FormFile[listaObjetosMenu.size()];
		
		
		List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
		int nlangs=langs.size();
		String[] trad = new String[listaObjetosMenu.size()*nlangs];  // traducciones de los menús, los contenidos vacios
		String padreconte="c1";
		
		for (int i=0;i<listaObjetosMenu.size();i++) { 
			Object obj=listaObjetosMenu.get(i);
			
			if (obj instanceof Menu) {
				Menu menu1=(Menu)obj;
				tipos[i]="m";
				ids[i]=menu1.getId();
				visibles[i]=menu1.getVisible();
				modos[i]=menu1.getModo();
				ordenes[i]=new Integer(menu1.getOrden());
				idPadres[i]=menu1.getPadre();
            	// obtengo los iconos
				if (menu1.getImagenmenu() != null) {
					imagenMenuids[i]=menu1.getImagenmenu().getId();
           		 	imagenMenunoms[i]=menu1.getImagenmenu().getNombre();
                }

    			for (int j = 0; j < nlangs; j++) {
    				TraduccionMenu traduccion = (TraduccionMenu) menu1.getTraduccion("" + langs.get(j));
    				if (traduccion != null && traduccion.getNombre() != null) {
                        trad[(nlangs*i) + j] = "" + traduccion.getNombre();
                    } else {
                        trad[(nlangs*i) + j] = "";
                    }
    			}
				
			}

			if (obj instanceof Contenido) {
				Contenido con=(Contenido)obj;
				ids[i]=con.getId();
				visibles[i]=con.getVisible();
				ordenes[i]=new Integer(con.getOrden());
				idPadres[i]=con.getMenu().getId();

				// Obtenemos el tipo de padre del contenido, por la tabulacion en la jsp
				for (int j=0;j<listaObjetosMenu.size();j++) { 
	    			Object obj1=listaObjetosMenu.get(j);
	    			if (obj1 instanceof Menu) {
	    				Menu menu2=(Menu)obj1;
	    				if (menu2.getId().longValue()==con.getMenu().getId().longValue()) {
	    					if (menu2.getPadre().intValue()==0) padreconte="c1";
	    					else					 			padreconte="c2";
	    					break;
	    				}
	    			}
				}
				tipos[i]=padreconte;
				
    			for (int j = 0; j < nlangs; j++) {
    				TraduccionContenido traduccion = (TraduccionContenido)con.getTraduccion(""+langs.get(j));
    				if (traduccion!= null)	 	trad[(nlangs*i)+j]= ""+traduccion.getTitulo();
    				else 						trad[(nlangs*i)+j]= "";
    			}
				
			}

        	 
		}
		
		menuForm.set("ids",ids);
		menuForm.set("visibles",visibles);
		menuForm.set("modos",modos);
		menuForm.set("ordenes",ordenes);
		menuForm.set("idPadres",idPadres);
		menuForm.set("tipos",tipos);
		menuForm.set("traducciones",trad);
		menuForm.set("imagenesid",imagenMenuids);
		menuForm.set("imagenesnom",imagenMenunoms);
		menuForm.set("imagenes",imagenMenuArchivos);
		menuForm.set("numeroobjectes",numeroObjectes);
    	
    }
    
    
    /**
     * Método que vuelca los datos del formulario, actualiza y los guarda en una Array
     * @author Indra
     * @param menuForm			formulario dinámico enviado por usuario
     * @param micrositeBean		bean de microsite
     * @return ArrayList 
     * @throws Exception
     */
    private ArrayList<?> setFormtoArrayAndSave (menuForm menuForm, Microsite micrositeBean) throws Exception  {	
    		
    	MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
    	
    	Long ids[] = (Long[]) menuForm.get("ids");
		String[] visibles = (String[]) menuForm.get("visibles");
		String[] modos = (String[]) menuForm.get("modos");
		Integer[] ordenes = (Integer[]) menuForm.get("ordenes");
		Long[] idPadres = (Long[]) menuForm.get("idPadres");
		String[] tipos = (String[]) menuForm.get("tipos");
		FormFile imagenes[] = (FormFile[]) menuForm.get("imagenes");
		String imagenesnom[] = (String[]) menuForm.get("imagenesnom");
        boolean[] imagenesbor = (boolean[]) menuForm.get("imagenesbor");
        String[] traducciones = (String[]) menuForm.get("traducciones");
		
		menuDelegate.actualizarMenus(micrositeBean.getId(), ids, visibles, modos, ordenes, idPadres, tipos, imagenes, imagenesnom, imagenesbor, traducciones);
		ArrayList<?> listaObjetosMenu = menuDelegate.ObtenerObjetosMenu(micrositeBean.getId());
		
		return listaObjetosMenu;
    }  

    /**
     * Método que vuelca los datos del formulario al Bean de Menu
     * @author Indra
     * @param menuForm			formulario dinámico enviado por usuario
     * @param idMicrosite		Id del Microsite al que pertenece el Menú
     * @return Menu
     * @throws Exception
     */
    private Menu setFormtoBean(menuForm menuForm, Long idMicrosite) throws Exception {

        IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
    	MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
    	MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();

    	Microsite micro = micrositeDelegate.obtenerMicrosite(idMicrosite);

    	Menu menu = new Menu();

    	menu.setMicrosite(micro);
		menu.setPadre(new Long("" + menuForm.get("padreCM")));
		menu.setVisible("" + menuForm.get("visibleCM"));
		menu.setModo("" + menuForm.get("modoCM"));
		// icono
        FormFile imagen = (FormFile) menuForm.get("imagenCM");
        if (archivoValido(imagen)) {
            menu.setImagenmenu(populateArchivo(menu.getImagenmenu(), imagen, menu.getIdmicrosite(), null));
        }

        List<?> lang = idiomaDelegate.listarIdiomas();
        String[] nombreCM = (String[]) menuForm.get("nombreCM");

        Map<String, TraduccionMenu> traducciones = new HashMap<String,TraduccionMenu>(lang.size());
        for (int i = 0; i < lang.size(); i++) {
            Idioma idi = (Idioma) lang.get(i);
            TraduccionMenu traduccion = new TraduccionMenu();
            if (nombreCM[i] != null) {
                traduccion.setNombre(nombreCM[i]);
                traduccion.setId(new TraduccionMenuPK());
                traduccion.getId().setCodigoIdioma(idi.getCodigoEstandar());
//                traduccion.getId().setCodigoMenu(null);
            }
            traducciones.put(idi.getLang(), traduccion);
        }
        menu.setTraduccionMap(traducciones);

        // Establecemos automaticamente el orden, siguiente al del padre o 0 si no tiene padre
        if (menu.getPadre().intValue() == 0) {
            menu.setOrden(0);
            menuDelegate.Reordena(-1, 'a', menu.getMicrosite().getId());
        } else {
            Menu menupadre = menuDelegate.obtenerMenu(menu.getPadre());
            menu.setOrden(menupadre.getOrden() + 1);
            menuDelegate.Reordena(menupadre.getOrden(), 'a', menu.getMicrosite().getId());
        }

        // Borramos los campos de Creación de menú
        menuForm.resetCreateForm();

        return menu;
    }

    /**
     * Método que traduce un formulario de Menu
     * @author Indra
     * @param request	petición de usuario
     * @param menuForm		formulario dinámico enviado por usuario
     * @throws Exception
     */
    private void traducir (HttpServletRequest request, menuForm menuForm) throws Exception  {	

    	MenuDelegate menuDelegate = DelegateUtil.getMenuDelegate();
    	TraductorMicrosites traductor = (TraductorMicrosites) request.getSession().getServletContext().getAttribute("traductor");
    	String idiomaOrigen = "ca";

     	Long idMicrosite = ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId();
     	Microsite micrositeBean = (Microsite)request.getSession().getAttribute("MVS_microsite");
    	ArrayList<?> listaobjetosMenu = menuDelegate.ObtenerObjetosMenu(idMicrosite);

    	//Lista de Idiomas
		List<String> listLang = traductor.getListLang();

		//Tabla temporal que guarda las traducciones del formulario
		String[] traducciones = (String[]) menuForm.get("traducciones");  // traducciones de los menús
    	int indiceT = 0;
			
    		for (int indiceOM=0;indiceOM<listaobjetosMenu.size();indiceOM++) { 
    			
    			Object objetoMenu = listaobjetosMenu.get(indiceOM);
	
    			//Si el objeto del Menú es un menú traducimos el nombre
    			if (objetoMenu instanceof Menu) {
    				
    				Menu menu = (Menu)objetoMenu;
        			
        	    	TraduccionMenu menuOrigen = (TraduccionMenu)menu.getTraduccion("ca");
        			Iterator<String> itLang = traductor.getListLang().iterator(); 
        	        List<String> idiomasMicro = Arrays.asList(micrositeBean.getIdiomas(micrositeBean.getIdiomas()));
       				            
        			while (itLang.hasNext()){
        				                
	        			   	String idiomaDesti = itLang.next();
	        			   	TraduccionMenu menuDesti = null;
	        			   	
	        			   	if (menu.getTraduccion(idiomaDesti) != null)
	        			   		menuDesti = (TraduccionMenu)menu.getTraduccion(idiomaDesti);
	        			   	else {
	        			   		menu.setTraduccion(idiomaDesti, new TraduccionMenu());
	        			   		menuDesti = (TraduccionMenu)menu.getTraduccion(idiomaDesti);
	        			   	}
	        				
	        			  //Comprobamos que el idioma Destino esté configurado en el Microsite si no está no se traduce
	        			   	if (idiomasMicro.contains(idiomaDesti)) {
		        			   	
	        			   		if (!idiomaOrigen.equals(idiomaDesti)) {
		        			   		traductor.setDirTraduccio(idiomaOrigen, idiomaDesti);
		        				            		
		       				   		if (traductor.traducir(menuOrigen, menuDesti)) {
		       				   				traducciones[indiceT]= ""+menuDesti.getNombre();
		       				   				indiceT = indiceT + 1;
		       				   				request.setAttribute("mensajes", "traduccioCorrecte");
		       				   		} else {
		        			   			request.setAttribute("mensajes", "traduccioIncorrecte");
		        			   			break;
		        			      		}
		        				} else {
		        					//traducciones[indiceT]= ""+menuOrigen.getNombre();
		        					indiceT = indiceT + 1;
		        				}
	        			   } else indiceT = indiceT + 1;
        			  }
        		} 
    			else {
        			//En caso de ser Contenido se escriben los valores de título que ya existen
//        			Contenido contenido = (Contenido)objetoMenu;
//        			
//	    			for (int indiceC = 0; indiceC < listLang.size(); indiceC++) {
//	    				TraduccionContenido traduccion = (TraduccionContenido)contenido.getTraduccion(""+listLang.get(indiceC));
//	    				if (traduccion!= null) traducciones[indiceT]= ""+traduccion.getTitulo(); else traducciones[indiceT]= "";
//	    				indiceT = indiceT + 1;
//	    			}
	    			
        			//Aumentamos el índice 
	    			indiceT = indiceT + listLang.size();
        		}

			}    			
        			if (request.getAttribute("mensajes").equals("traduccioCorrecte")) addMessage(request, "mensa.traduccion.confirmacion");
        			 else addMessageError(request, "mensa.traduccion.error");
        				           
        			//Guardamos la modificación de la tabla traducciones en el formulario
        			menuForm.set("traducciones",traducciones);
                 	//Long ids[]=(Long[])menuForm.get("ids");
                 	//request.setAttribute("objetosMenu", ids.length);
        			
        			log.info("Traducción Árbol de Menús de Microsite - Id: " + idMicrosite);

    }
    
    

}
