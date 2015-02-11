package org.ibit.rol.sac.microfront.noticia.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.noticia.actionforms.BuscaOrdenaNoticiasActionForm;
import org.ibit.rol.sac.microfront.util.Cadenas;
import org.ibit.rol.sac.microfront.util.Fechas;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.Noticia;
import org.ibit.rol.sac.micromodel.Tipo;
import org.ibit.rol.sac.micromodel.TraduccionTipo;
import org.ibit.rol.sac.micropersistence.delegate.BuscarElementosParameter;
import org.ibit.rol.sac.micropersistence.delegate.DelegateException;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate;
import org.ibit.rol.sac.micropersistence.delegate.NoticiaServiceItf;
import org.ibit.rol.sac.micropersistence.delegate.TipoDelegate;

/**
 * Clase Bdlistanoticias. Manejador de la petici�n de listado de noticias.
 * 
 * Recoge los datos para mostrarlos en el front. Introduce las noticias en una lista.
 * 
 * @author Indra
 *
 */

public class Bdlistanoticias extends Bdbase  {

	private static final String SESSION_ANYOS = "anyos";

	private static final int ONE_YEAR = 1;

	public static final String TOTS_ANYS = "Tots";

	protected static Log log = LogFactory.getLog(Bdlistanoticias.class);
	
	private String url_sinpagina="";
	private String desctiponoticia="";
	private Long idtipo=new Long(0);
	private List<?> listanoticias;
	private BuscaOrdenaNoticiasActionForm formulario = new BuscaOrdenaNoticiasActionForm();
	private HttpServletRequest req;
	private Hashtable<?, ?> parametros = new Hashtable<Object, Object>();
	private boolean error = false;
	private Tipo claseelemento = null;
	private List<String> listaanyos = new ArrayList<String>();
	private String anyo = null;
	private boolean busqueda = false;
	private String sqlOrder="";
	private String htmlExterno="";
	
	public void finalize() {
		this.dispose();
	}
	
	/**
	 * Metodo p�blico que borra las variables
	 */
	public void dispose() {
		url_sinpagina = null;
		desctiponoticia = null;
		idtipo = null;
		listanoticias = null;
		formulario = null;
		req = null;
		parametros = null;
		claseelemento = null;
		listaanyos = null;
		anyo = null;
		htmlExterno = null;
		sqlOrder = null;
		super.dispose();
	}
	
	public Bdlistanoticias() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor de la clase, carga el listado de noticias
	 * @param request
	 * @throws Exception
	 */
	public Bdlistanoticias(HttpServletRequest request, ActionForm form) throws Exception {
		super(request);
		formulario = (BuscaOrdenaNoticiasActionForm) form;
		req = request;
		if ((microsite!=null) && (existeServicio(Microfront.RNOTICIA))) {
			obtenerclaseelemento();
			preparaOrden();
			if (claseelemento.getTipoelemento().equals(Tipo.TIPO_CONEXIO_EXTERNA)) {
				recogerExterno();
			} else {
				if (claseelemento.getTipopagina().equals(Microfront.ELEM_PAG_NORMAL)) {
					crearlistado();
				} else {
					String txttipo = "" + req.getParameter(Microfront.PTIPO);
					NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
					obtenerlistaanyos(txttipo, noticiadel);
					crearlistadoAnual();
				}
			}
			preparaseulet();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la p�gina solicitada.");
			error=true;
		}
	}

	/**
	 * M�todo privado para obtener la clase (tipo) de elemento (noticia)
	 */
	private void obtenerclaseelemento() {
		try {
			String txttipo = "" + req.getParameter(Microfront.PTIPO);
			TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
			Tipo tipo = tipodel.obtenerTipo( new Long(Long.parseLong(txttipo)));
			claseelemento = tipo;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en el listado de elementos.");
			error=true;
			listanoticias = null;
			parametros = new Hashtable<Object, Object>();
		}			
	}

	/**
	 * M�todo privado para preparar el orden en el cual se mostrara el listado.
	 */
	private void preparaOrden() {
		
		if (claseelemento.getOrden().equals("0")) 
			sqlOrder = " order by noti.orden, noti.id ";
		if (claseelemento.getOrden().equals("1")) 
			sqlOrder = " order by noti.fpublicacion, noti.id ";
		if (claseelemento.getOrden().equals("2")) 
			sqlOrder = " order by noti.fpublicacion desc, noti.id";
		if (claseelemento.getOrden().equals("3")) 
			sqlOrder = " order by trad.titulo, noti.id ";
 
	}
	
	/**
	 * M�todo privado para  obtener una lista con los a�os.
	 */
	public void obtenerlistaanyos(String txttipo, NoticiaServiceItf noticiaService) {
		
		try {
			//preparar el tipo de noticias.
			
			idtipo=new Long(Long.parseLong(txttipo));
	    	noticiaService.init();
	    	String wherenoticias = "where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + super.idsite + " and noti.tipo=" + txttipo + " and noti.fpublicacion is not null ";
	    	noticiaService.setWhere(wherenoticias);
	    	noticiaService.setOrderby2("order by noti.fpublicacion desc");
	        noticiaService.setTampagina(Microfront.MAX_INTEGER);//todooooooo
	        noticiaService.setPagina(1);

	        
	        
	        listaanyos = cogerAnyosDeSession();
	        
	        if(null == listaanyos) {
	        	listaanyos = noticiaService.listarAnyos();
	        	ponerAnyosEnSession();
	        }
	        	
	        
	        
	        /*
	        //se coje la fechaPublicacion de cada noticia y se crea una lista de años
	        listanoticias = noticiaService.listarNoticiasThin(idioma);
	        
	        Iterator<?> iter = listanoticias.iterator();
	        String anyoant="";
	        while (iter.hasNext()) {
	        	Noticia noti = (Noticia)iter.next();
	        	String anyo = "" + Fechas.obtenerAnyo(noti.getFpublicacion());
	        	if (!anyo.equals(anyoant) && (anyo.length()==4)) { 
	        		listaanyos.add(anyo);
	        	}
	        	anyoant=anyo;
	        }
	        */
	        
			if (idsite.longValue()==0) error=true;
	        
			//si hay mas de un anyo, se añade "tots" al principio
			if(ONE_YEAR < listaanyos.size()) {
				List<String> tmpList = listaanyos;
				listaanyos = new ArrayList<String>();
				listaanyos.add(TOTS_ANYS);
				listaanyos.addAll(tmpList);
			}
			
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al obtener el listado de a�os para la paginacion.");
			error = true;
			listanoticias = null;
			parametros = new Hashtable<Object, Object>();
		}
		
	}


	protected List<String> cogerAnyosDeSession() {
		Map<String, List<String>> map = consultarAtributoAnyosDeSession();
		String key = generarKeyAnyosEnSession();
		return map.get(key);

	}

	protected void ponerAnyosEnSession() {
		Map<String, List<String>> map = consultarAtributoAnyosDeSession();
		String key = generarKeyAnyosEnSession();
		map.put(key,listaanyos);
		req.getSession().setAttribute(SESSION_ANYOS, map);
	}

	private Map<String, List<String>> consultarAtributoAnyosDeSession() {
		Map<String, List<String>> map = (Map<String, List<String>>) req.getSession().getAttribute(SESSION_ANYOS);
		if(null==map) {
			map = new HashMap<String,List<String>>();
		}
		return map;
	}

	
	
	private String generarKeyAnyosEnSession() {
		String tipo = "" + req.getParameter(Microfront.PTIPO);
		String siteId = microsite.getId().toString();
		return siteId+"_"+tipo;
	}
	
	/**
	 * M�todo privado para crear el listado de noticias para un a�o en concreto.
	 */
	private void crearlistadoAnual() {
		try {
				//preparar el tipo de noticias.
				String txttipo = "" + req.getParameter(Microfront.PTIPO);
				String txtsearch = "";
				if ((formulario.getTipoelemento()!=null) && (formulario.getTipoelemento().equals(txttipo))) {
					txtsearch = "" + formulario.getFiltro();
				} else {	
					txtsearch="";
					formulario.setFiltro("");
				}

				anyo = "" + req.getParameter(Microfront.PANYO);
				idtipo=new Long(Long.parseLong(txttipo));
		    	NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
				
		    	int pagina = req.getParameter(Microfront.PPAGINA)!=null? 
				        		(Integer.parseInt(req.getParameter(Microfront.PPAGINA)))
				        	 :
				        		1; 

		    	
				if (isEmpty(txtsearch)){
					buscarNoticiasPorAnyo(txttipo, pagina, noticiadel);
					
				} else {
					buscarNoticiasPorCampos(txttipo, txtsearch, noticiadel);
				}
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en el listado de noticias.");
			error=true;
			listanoticias = null;
			parametros = new Hashtable<Object, Object>();
		}

	}


	public void buscarNoticiasPorCampos(String txttipo, String txtsearch, NoticiaServiceItf noticiasService) throws Exception, DelegateException {
		//listado procedente de la busqueda de lucene
		busqueda = true;
		
		recogetiponoticia();
		
		BuscarElementosParameter paramsBuscador = rellenarParametrosBuscador(txttipo, txtsearch); 
		
		listanoticias = noticiasService.buscarElementos(paramsBuscador);
		
		
		//busqueda por lucene. Pendiente de activar
		//listanoticias=noticiadel.buscarElementosLuc(microsite.getId().toString(),idioma,txttipo, txtsearch, true);
		parametros = noticiasService.getParametros();
	}

	private BuscarElementosParameter rellenarParametrosBuscador(
												String txttipo,
												String txtsearch) throws Exception {
		
		txtsearch = txtsearch.toUpperCase();

		Map<String, String> paramMap = new HashMap<String, String>();
		Map<String, String> traduccionMap = new HashMap<String, String>();
		
		String whereAnyo="";
		
		// Par�metros generales
		if(isAnyoSeleccionado()) {
			whereAnyo = construirPartialWhereAnyo(anyo);
		}
		else {
			paramMap.put("fpublicacion", txtsearch);
		}
	
		
		// Par�metros traducibles
		traduccionMap.put("titulo", txtsearch);
		
		traduccionMap.put("subtitulo", txtsearch);
		traduccionMap.put("fuente", txtsearch);
		traduccionMap.put("laurl", txtsearch);
		traduccionMap.put("urlnom", txtsearch);
		traduccionMap.put("texto", txtsearch);
		
		BuscarElementosParameter paramsBuscador = new BuscarElementosParameter();
		paramsBuscador.parametros = paramMap;
		paramsBuscador.traduccion = traduccionMap;
		paramsBuscador.idmicrosite = microsite.getId().toString();
		paramsBuscador.idioma = idioma;
		paramsBuscador.idtipo = txttipo;
		paramsBuscador.where = whereAnyo; 
		return paramsBuscador;
	}

	private boolean isAnyoSeleccionado() {
		return !isEmpty(anyo) && !anyo.equals(TOTS_ANYS);
	}

	
	public void buscarNoticiasPorAnyo(String txttipo, int pagina, NoticiaServiceItf noticiasService)
			throws DelegateException, Exception {
		if (anyo.equals("null")) {
			if (listaanyos.size()>0)
				anyo=(String)listaanyos.get(0); //coger el primero
			else {
				anyo="" + Fechas.obtenerAnyo(new Date()); //obtener el del a�o en curso
			}
		}
		noticiasService.init();
		String wherenoticias="where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + super.idsite + " and noti.tipo=" + txttipo;
		
		if(!TOTS_ANYS.equals(anyo)) {
			wherenoticias+=" and "+construirPartialWhereAnyo(anyo);
		}
		
		noticiasService.setWhere(wherenoticias);
		noticiasService.setOrderby2(sqlOrder);
		
		noticiasService.setTampagina(claseelemento.getTampagina());

		
		if (!isEmpty(formulario.getOrdenacion())) {
			noticiasService.setOrderby(formulario.getOrdenacion());
		}
		
		// No hay paginacion dentro de la paginacion anual
		noticiasService.setPagina(pagina);
		
		recogetiponoticia();
		listanoticias = noticiasService.listarNoticiasThin(idioma);
		parametros = (Hashtable<?, ?>) noticiasService.getParametros();
		
		//Si hay alg�n registro limpiamos el filtro
		if (listanoticias.size()==0) formulario.setFiltro("");
		
		if (idsite.longValue()==0) error=true;
	}

	public String construirPartialWhereAnyo(String anyo) {
		String where="" ;
		where+="     (to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + anyo + "-12-31')";
		where+=" and (to_char(noti.fpublicacion,'yyyy-MM-dd')>='" + anyo + "-01-01')";
		return where;
	}

	private boolean isEmpty(String txtsearch) {
		return  null==txtsearch || 
				txtsearch.equals("null") || 
				txtsearch.length()==0;
	}
	
	/**
	 * M�todo privado para crear listado de noticias.
	 */
	private void crearlistado() {
		try {
				//preparar el tipo de noticias.
				String txttipo = "" + req.getParameter(Microfront.PTIPO);
				String txtsearch = "";
				if ((formulario.getTipoelemento()!=null) && (formulario.getTipoelemento().equals(txttipo))) {
					txtsearch = "" + formulario.getFiltro();
				} else {	
					txtsearch="";
					formulario.setFiltro("");
				}
				
				idtipo=new Long(Long.parseLong(txttipo));
		    	NoticiaDelegate noticiadel = DelegateUtil.getNoticiasDelegate();
		    	
				if (isEmpty(txtsearch)){
						// listado tradicional
						busqueda = false;
				    	noticiadel.init();
				    	java.sql.Date dt = new java.sql.Date((new Date()).getTime());
				    	String wherenoticias="where index(trad)='" + idioma + "' and noti.visible='S' and noti.idmicrosite=" + super.idsite + " and noti.tipo=" + txttipo;
				    	wherenoticias+=" and (noti.fpublicacion is null OR to_char(noti.fpublicacion,'yyyy-MM-dd')<='" + dt + "')";
				    	wherenoticias+=" and (noti.fcaducidad is null OR to_char(noti.fcaducidad,'yyyy-MM-dd')>='" + dt + "')";
				    	noticiadel.setWhere(wherenoticias);
				    	noticiadel.setOrderby2(sqlOrder);
				    	noticiadel.setTampagina(claseelemento.getTampagina());
				    
				        if (formulario.getOrdenacion()!= null && formulario.getOrdenacion().length()>0)
				        	noticiadel.setOrderby(formulario.getOrdenacion());
				
				        // Indicamos la p�gina a visualizar
				        if (req.getParameter(Microfront.PPAGINA)!=null)
				        	noticiadel.setPagina(Integer.parseInt(req.getParameter(Microfront.PPAGINA)));
				        else
				        	noticiadel.setPagina(1);
				        
				        recogetiponoticia();
				        listanoticias = noticiadel.listarNoticiasThin(idioma);
				        parametros = noticiadel.getParametros();
				        
				        //Si hay alg�n registro limpiamos el filtro
				        if (listanoticias.size()==0) formulario.setFiltro("");

						if (idsite.longValue()==0) error=true;
						
				} else {
					buscarNoticiasPorCampos(txttipo, txtsearch, noticiadel);
				}
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en el listado de noticias.");
			error=true;
			listanoticias = null;
			parametros = new Hashtable<Object, Object>();
		}

	}
	
	/**
	 * M�todo privado para recoger el tipo de noticia.
	 * @throws Exception
	 */
	protected void recogetiponoticia() throws Exception {
		TipoDelegate tipodel = DelegateUtil.getTipoDelegate();
		Tipo tipo = tipodel.obtenerTipo(idtipo);
		desctiponoticia = ((TraduccionTipo)tipo.getTraduccion(idioma)).getNombre();
	}

	/**
	 * M�todo privado para recoger Externo.
	 */
	private void recogerExterno() {
		try {
			TipoDelegate tD = DelegateUtil.getTipoDelegate();
			
			//preparamos el map a enviar
			Map<String, String> filtrado = new HashMap<String, String>();
			
			Enumeration<?> paramNames = req.getParameterNames();
		    while(paramNames.hasMoreElements()) {
		      String paramName = (String)paramNames.nextElement();
		      String paramValue = req.getParameter(paramName);
		      filtrado.put(paramName, paramValue);
		    }
			
			String resultadohtml = tD.obtenerPegoteHTMLExterno(claseelemento.getId(),filtrado);
			htmlExterno = resultadohtml;
		} catch (DelegateException ne) {
			beanerror = new ErrorMicrosite("Error", "[Se ha producido un error desconocido en el listado de noticias]  Stack==" +  Cadenas.statcktrace2String(ne.getStackTrace(), 6));
			error=true;
		}
	}
	
	/**
	 * M�todo privado que quita el parametro 'pagina' de la lista de parametros del servlet
	 *
	 */
	private void preparaseulet() {
		
		url_sinpagina = url;
		url_sinpagina=req.getContextPath() + req.getServletPath() + "?";
		
			Enumeration<?> paramNames = req.getParameterNames();
		    while(paramNames.hasMoreElements()) {
		      String paramName = (String)paramNames.nextElement();
		      String paramValue = req.getParameter(paramName);
		      if ((!paramName.equals(Microfront.PLANG)) 
		    		  && (!paramName.equals(Microfront.PSTAT))
		    		  && (!paramName.equals(Microfront.PPAGINA)))
		    	  url_sinpagina += paramName + "=" + paramValue + "&";
		    }
		
	}
	
	/**
	 * Implementacion del m�todo abstracto.
	 * Se le indica que estamos en el servicio de noticias.
	 */
	public String setServicio() {
		return Microfront.RNOTICIA;
	}	
	
	public List<?> getListanoticias() {
		return listanoticias;
	}

	public boolean isError() {
		return error;
	}

	public String getDesctiponoticia() {
		return desctiponoticia;
	}

	public Hashtable<?, ?> getParametros() {
		return parametros;
	}

	public String getUrl_sinpagina() {
		return url_sinpagina;
	}

	public Tipo getClaseelemento() {
		return claseelemento;
	}

	public void setClaseelemento(Tipo claseelemento) {
		this.claseelemento = claseelemento;
	}

	public List<String> getListaanyos() {
		return listaanyos;
	}

	public void setListaanyos(ArrayList<String> listaanyos) {
		this.listaanyos = listaanyos;
	}

	public void setAnyo(String anyo) {
		this.anyo = anyo;
	}
	
	public String getAnyo() {
		return anyo;
	}
	

	public boolean isBusqueda() {
		return busqueda;
	}

	public String getHtmlExterno() {
		return htmlExterno;
	}
	
}
