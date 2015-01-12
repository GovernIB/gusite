package org.ibit.rol.sac.microfront.util.microtag;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.bean.Tridato;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micropersistence.delegate.BannerDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;


/**
 * Clase MicrositeParser. Parseador de tags de microsite.
 * Esta clase contiene métodos que parsean los tags especiales de los microsites.
 * Los tags pueden ser de cualquier version.
 * 
 * @author Indra
 *
 */
public class MicrositeParser {

	protected static Log log = LogFactory.getLog(MicrositeParser.class);
	
	private String _version="N"; // S=retringido, N=version 1(azules), 2=version 4(blancos)
	
	private StringBuffer htmlOld = new StringBuffer();
	private StringBuffer htmlParsed = new StringBuffer();
	private Hashtable<String, String> hshTags = new Hashtable<String, String>();
	private Hashtable<String, Tridato> hshTagsStatus = new Hashtable<String, Tridato>();
	private Long idsite;
	private String idioma;
	private int numeronoticias;
	
	/**
	 * Constructor de la clase Inicia variables.
	 * @param version
	 * @param html
	 * @param idsite
	 * @param idioma
	 * @param numeronoticias
	 */
	public MicrositeParser(String version, StringBuffer html, Long idsite, String idioma, int numeronoticias) {
		this._version=version;
		this.htmlOld = html;
		this.idsite=idsite;
		this.idioma=idioma;
		this.numeronoticias=numeronoticias;
	}
	
	/**
	 * Constructor de la clase. Inicia variables
	 * @param version
	 * @param html
	 * @param idsite
	 * @param idioma
	 * @param numeronoticias
	 */
	public MicrositeParser(String version, String html, Long idsite, String idioma, int numeronoticias) {
		this(version, new StringBuffer((html==null)?"":html), idsite, idioma, numeronoticias);
	}	
	
	/**
	 * Constructor de la clase. Inicia variables
	 * @param version
	 * @param html
	 * @param idsite
	 * @param idioma
	 */
	public MicrositeParser(String version, String html, Long idsite, String idioma) {
		this(version, new StringBuffer((html==null)?"":html), idsite, idioma, 3);
	}	
	
	/**
	 * Método que analiza el código html.
	 * Meterá en hshTagsStatus un listado con todos los tags a parsear encontrados 
	 * en el html.
	 * @param idioma
	 */
	public void doAnalisy(String idioma) {
		if (htmlOld!=null) {
			
				StringBuffer stbuf= new StringBuffer();
				stbuf = htmlOld;
				
		/* ********     tags version 1.0     ******** */							
				int pos=0;

				// buscar tag de noticias. v1.0
				pos = stbuf.indexOf(Microfront.TAG_NOTICIAS);
				while (pos > -1) {
			    	  	StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf(">",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+1);
						hshTagsStatus.put(tag,new Tridato(Microfront.TAG_NOTICIAS,"V1.0",null));
						pos = stbuf.indexOf(Microfront.TAG_NOTICIAS, pos + tag.length());
			    }
				
				// buscar tag de agenda. v1.0
				pos = stbuf.indexOf(Microfront.TAG_AGENDA);
				while (pos > -1) {
			    	  	StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf(">",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+1);
						hshTagsStatus.put(tag,new Tridato(Microfront.TAG_AGENDA,"V1.0",null));
						pos = stbuf.indexOf(Microfront.TAG_AGENDA, pos + tag.length());
			    }				
				
				// buscar tag de banner. v1.0
				pos = stbuf.indexOf(Microfront.TAG_BANNER);
				while (pos > -1) {
			    	  	StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf(">",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+1);
						hshTagsStatus.put(tag,new Tridato(Microfront.TAG_BANNER,"V1.0",null));
						pos = stbuf.indexOf(Microfront.TAG_BANNER, pos + tag.length());
			    }	
				

		/* ********     tags version 1.3     ******** */			

				// buscar tag de noticias. v1.3
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String tiponoticia = averiguaTipo(tag);
						if (!tiponoticia.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA,"V1.3",tiponoticia));
						pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA, pos + tag.length());
			    }				

				// buscar tag de agenda. v1.3
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						hshTagsStatus.put(tag,new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA,"V1.3",null));
						pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA, pos + tag.length());
			    }				
			
				// buscar tag de banner. v1.3
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						hshTagsStatus.put(tag,new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER,"V1.3",null));
						pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER, pos + tag.length());
			    }
				
				
		/* ********     tags version 1.4     ******** */
		/* Los tags de agenda y banner no han sufrido variacion desde la version anterior.*/

				// buscar tag de noticias. v1.4
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String idcomponente = averiguaComponente(tag);
						if (!idcomponente.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA,"V1.4",idcomponente));
						pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA, pos + tag.length());
			    }				
	
	
				//				 buscar tag de qssi
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String idcomponente = averiguaComponente(tag);
						if (!idcomponente.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI,"V1.4",idcomponente));
						pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI, pos + tag.length());
			    }				
				
				
		/* ********     tags version 2.0     ******** */
		/* Los tags de agenda y banner no han sufrido variacion desde la version anterior.*/

				// buscar tag de encuestas. v2.0
				pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String idcomponente = averiguaComponente(tag);
						if (!idcomponente.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA,"V2.0",idcomponente));
						pos = stbuf.indexOf(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA, pos + tag.length());
			    }					
				
		}
	}

	/**
	 * Método que parsea el código html.
	 * Reemplaza los tags de los microsites por los "trozos" de html correspondientes.
	 * Además meterá en hshTagsStatus un listado con todos los tags a parsear encontrados y su estado.
	 * Además meterá en hshTags un listado con todos los tags a parsear encontrados y su correspondiete "trozo" de html.
	 * 
	 */
	public void doParser(String idioma) {
		try {
			doAnalisy(idioma);
			doCalculaTags();
			 
			if (htmlOld!=null) {
				  htmlParsed = htmlOld;
				  Enumeration<String> enumera = hshTags.keys();
				  while (enumera.hasMoreElements()) {
						      String oldstringtmp = (String)enumera.nextElement();
						      String newstringtmp = (String)hshTags.get(oldstringtmp);
						      log.debug( "Parseado " + ((Tridato)hshTagsStatus.get(oldstringtmp)).getKey() + " " + ((Tridato)hshTagsStatus.get(oldstringtmp)).getValue1() );
						      
						      int pos = htmlParsed.indexOf(oldstringtmp);
						      while (pos > -1) {
						    	  	htmlParsed.replace(pos, pos + oldstringtmp.length(), newstringtmp);
									pos = htmlParsed.toString().indexOf(oldstringtmp, pos + newstringtmp.length());
						      }
				  }
			}   
			  			
			
			
		} catch (Exception e) {
			log.error("Se ha producido un error parseando html. " + e);
		}
		
	}
	
	/**
	 * Método que parsea el código html.<br/>
	 * Reemplaza los tags de los microsites por comentarios
	 * Además meterá en hshTagsStatus un listado con todos los tags a parsear encontrados y su estado.
	 * Además meterá en hshTags un listado con todos los tags a parsear encontrados y su correspondiete "trozo" de html.
	 * 
	 */
	public void doParser2Comentario(String idioma) {
		try {
			doAnalisy(idioma);
			doCalculaTagsComentario();
			
			  
			if (htmlOld!=null) {
				  htmlParsed = htmlOld;
				  Enumeration<String> enumera = hshTags.keys();
				  while (enumera.hasMoreElements()) {
						      String oldstringtmp = (String)enumera.nextElement();
						      String newstringtmp = (String)hshTags.get(oldstringtmp);
						      log.debug( "Parseado " + ((Tridato)hshTagsStatus.get(oldstringtmp)).getKey() + " " + ((Tridato)hshTagsStatus.get(oldstringtmp)).getValue1() );
						      
						      int pos = htmlParsed.indexOf(oldstringtmp);
						      while (pos > -1) {
						    	  	htmlParsed.replace(pos, pos + oldstringtmp.length(), newstringtmp);
									pos = htmlParsed.toString().indexOf(oldstringtmp, pos + newstringtmp.length());
						      }
				  }
			}   
			  			
		} catch (Exception e) {
			log.error("Se ha producido un error parseando html. " + e);
		}
		
	}
	
	/**
	 * Reemplaza el tag por un comentario.
	 */
	private void doCalculaTagsComentario() {
		
		Enumeration<String> enumera = hshTagsStatus.keys();
    	while (enumera.hasMoreElements()) {
    		String key = (String)enumera.nextElement();
    		Tridato tridato= (Tridato)hshTagsStatus.get(key);

    		if (tridato.getKey().equals(Microfront.TAG_NOTICIAS)) {
    			hshTags.put(key, "<!-- " + Microfront.TAG_NOTICIAS + idsite + idioma + numeronoticias + " -->");
    		}
    		if (tridato.getKey().equals(Microfront.TAG_AGENDA)) {
    			hshTags.put(key, "<!-- " + Microfront.TAG_AGENDA + idsite + idioma + numeronoticias + " -->");
    		}
    		if (tridato.getKey().equals(Microfront.TAG_BANNER)) {
    			hshTags.put(key, "<!-- " + Microfront.TAG_BANNER + idsite + idioma + 1 + " -->");
    		}    		
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
    			hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA + idsite + idioma + numeronoticias + " -->");
    		} 
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
    			hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI + idsite + tridato.getValue2() + idioma +  " -->");
    		}    	
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER)) {
    			hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER + idsite + idioma + 1 + " -->");
    		}      		
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
    			if (tridato.getValue1().equals("V1.3")) {
    				hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA + idsite + tridato.getValue2() +  idioma + numeronoticias + " -->");
    			}
    			if (tridato.getValue1().equals("V1.4")) {
    				hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA + idsite + tridato.getValue2() + idioma + " -->");
    			}    			
    		}
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
    			if (tridato.getValue1().equals("V2.0")) {
    				hshTags.put(key, "<!-- " + Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA + idsite + tridato.getValue2() + idioma + " -->");
    			}
    		}
    		
    	}
	
	}
	
	/**
	 * Método privado para calcular Tags
	 */
	private void doCalculaTags() {
		
		MParserElemento parseelemento = new MParserElemento(_version);
		MParserAgenda parseagenda = new MParserAgenda(_version);
		MParserBanner parsebanner = new MParserBanner(_version);
		MParserComponente parsecomponente = new MParserComponente(_version);
		MParserEncuesta parseencuesta = new MParserEncuesta(_version);
		MParserQssi parserqssi = new MParserQssi(_version);
		Enumeration<String> enumera = hshTagsStatus.keys();
    	while (enumera.hasMoreElements()) {
    		String key = (String)enumera.nextElement();
    		Tridato tridato= (Tridato)hshTagsStatus.get(key);

    		
    		if (tridato.getKey().equals(Microfront.TAG_NOTICIAS)) {
    			hshTags.put(key, parseelemento.getHtmlNoticias(idsite,idioma,numeronoticias).toString());
    		}
    		if (tridato.getKey().equals(Microfront.TAG_AGENDA)) {
    			hshTags.put(key, parseagenda.getHtmlAgendaCalendario(idsite,idioma,numeronoticias).toString());
    		}
    		if (tridato.getKey().equals(Microfront.TAG_BANNER)) {
    			hshTags.put(key, parsebanner.getHtmlBanner(idsite,idioma,obtenerbanner(1)).toString());
    		}    		
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RAGENDA)) {
    			hshTags.put(key, parseagenda.getHtmlAgendaCalendario(idsite,idioma,numeronoticias).toString());
    		} 
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RQSSI)) {
    			hshTags.put(key, parserqssi.getHtmlQssi(idsite,tridato.getValue2(),idioma).toString());
    		}    	
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RBANNER)) {
    			hshTags.put(key, parsebanner.getHtmlBanner(idsite,idioma,obtenerbanner(1)).toString());
    		}      		
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RNOTICIA)) {
    			if (tridato.getValue1().equals("V1.3")) {
    				hshTags.put(key, parseelemento.getHtmlElementos(idsite,tridato.getValue2(),idioma,numeronoticias).toString());
    			}
    			if (tridato.getValue1().equals("V1.4")) {
    				hshTags.put(key, parsecomponente.getHtmlElementosComponente(idsite,tridato.getValue2(),idioma).toString());
    			}    			
    		}
    		if (tridato.getKey().equals(Microfront.TAG_GENERICO_DUMMY + Microfront.RENCUESTA)) {
    			if (tridato.getValue1().equals("V2.0")) {
    				hshTags.put(key, parseencuesta.getHtmlEncuesta(null, idsite,tridato.getValue2(),idioma).toString());
    			}
    		}
    		
    	}
	
	}


	/* ********************   auxiliares    ******************* */
	
	private String averiguaTipo(String cadena) {
		MParserHTML parsehtml = new MParserHTML(_version);
		return parsehtml.getAtributoTag(cadena, "propertyid");
	}

	private String averiguaComponente(String cadena) {
		MParserHTML parsehtml = new MParserHTML(_version);
		return parsehtml.getAtributoTag(cadena, "componenteid");
	}
	
	private String averiguaComponente2(String cadena, String idioma) {
		MParserHTML parsehtml = new MParserHTML(_version);
		return parsehtml.getAtributoTag2(cadena, "componenteID",idioma);
	}
	
	private Banner obtenerbanner(int numerobanners) {
		Banner retornobanner = new Banner();
		
			//obtener el listado y sacar los que nos interesen
			try {
				Hashtable<Object, Banner> hashbanners = new Hashtable<Object, Banner>();
				
				BannerDelegate bannerdel = DelegateUtil.getBannerDelegate();
				bannerdel.init();bannerdel.setTampagina(Integer.MAX_VALUE);
				String where="where index(trad)='" + idioma + "' and banner.visible='S' and banner.idmicrosite=" + idsite;
				where+=" and (banner.fpublicacion is null OR banner.fpublicacion<=SYSDATE) and (banner.fcaducidad is null OR banner.fcaducidad>=SYSDATE)";
				bannerdel.setWhere(where);
				List<?> listabannerslocal;
				listabannerslocal=bannerdel.listarBanners();
				
				if (listabannerslocal.size()<numerobanners)
					numerobanners=listabannerslocal.size();
				
				while (hashbanners.size()<numerobanners) {
					MParserHTML parsehtml = new MParserHTML(_version);
					int numerobanner = parsehtml.getNumeroaleatorio(listabannerslocal.size());
					Iterator<?> iter = listabannerslocal.iterator();
					for (int i=1; i<numerobanner; i++) iter.next();
					retornobanner = new Banner();
					retornobanner = (Banner)iter.next();
					hashbanners.put("" + retornobanner.getId(),retornobanner);
				}
		    	
			} catch(Exception e) {
				log.error("[obtenerbanner]: " + e.getMessage());
			}

		return retornobanner;
	}	
	
	
	public Hashtable<String, String> getHshTags() {
		return hshTags;
	}
	public void setHshTags(Hashtable<String, String> hshTags) {
		this.hshTags = hshTags;
	}
	public StringBuffer getHtmlOld() {
		return htmlOld;
	}
	public void setHtmlOld(StringBuffer htmlOld) {
		this.htmlOld = htmlOld;
	}
	public StringBuffer getHtmlParsed() {
		return htmlParsed;
	}
	public void setHtmlParsed(StringBuffer htmlParsed) {
		this.htmlParsed = htmlParsed;
	}
	
}
