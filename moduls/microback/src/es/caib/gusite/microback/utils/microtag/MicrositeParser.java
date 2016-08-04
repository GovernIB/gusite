package es.caib.gusite.microback.utils.microtag;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microback.Microback;


/**
 * Parseador de tags de microsite.
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
	private Hashtable hshTags = new Hashtable();
	private Hashtable<String, Tridato> hshTagsStatus = new Hashtable<String, Tridato>();
	private Long idsite;
	private String idioma;
	private int numeronoticias;
	
	public MicrositeParser(String version, StringBuffer html, Long idsite, String idioma, int numeronoticias) {
		this._version=version;
		this.htmlOld = html;
		this.idsite=idsite;
		this.idioma=idioma;
		this.numeronoticias=numeronoticias;
	}
	
	public MicrositeParser(String version, String html, Long idsite, String idioma, int numeronoticias) {
		this(version, new StringBuffer((html==null)?"":html), idsite, idioma, numeronoticias);
	}	
	
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
				
				
				/* ****************************************** */
				/* ********     tags version 1.0     ******** */
				/* ****************************************** */
				
			
				int pos=0;
				
				// buscar tag de noticias. v1.0
				pos = stbuf.indexOf(Microback.TAG_NOTICIAS);
				while (pos > -1) {
			    	  	StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf(">",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+1);
						hshTagsStatus.put(tag,new Tridato(Microback.TAG_NOTICIAS,"V1.0",null));
						pos = stbuf.indexOf(Microback.TAG_NOTICIAS, pos + tag.length());
			    }
				
				// buscar tag de agenda. v1.0
				pos = stbuf.indexOf(Microback.TAG_AGENDA);
				while (pos > -1) {
			    	  	StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf(">",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+1);
						hshTagsStatus.put(tag,new Tridato(Microback.TAG_AGENDA,"V1.0",null));
						pos = stbuf.indexOf(Microback.TAG_AGENDA, pos + tag.length());
			    }				
				
				/* ****************************************** */
				/* ********     tags version 1.3     ******** */
				/* ****************************************** */				

				// buscar tag de noticias. v1.3
				pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String tiponoticia = averiguaTipo(tag);
						if (!tiponoticia.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA,"V1.3",tiponoticia));
						pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA, pos + tag.length());
			    }				

				// buscar tag de agenda. v1.3
				pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RAGENDA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						hshTagsStatus.put(tag,new Tridato(Microback.TAG_GENERICO_DUMMY + Microback.RAGENDA,"V1.3",null));
						pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RAGENDA, pos + tag.length());
			    }				
			
				// buscar tag de banner. v1.3
				pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RBANNER);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						hshTagsStatus.put(tag,new Tridato(Microback.TAG_GENERICO_DUMMY + Microback.RBANNER,"V1.3",null));
						pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RBANNER, pos + tag.length());
			    }
				
				
				/* ****************************************** */
				/* ********     tags version 1.4     ******** */
				/* ****************************************** */
				/* Los tags de agenda y banner no han sufrido variacion desde la version anterior.*/

				// buscar tag de noticias. v1.4
				pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String idcomponente = averiguaComponente(tag);
						if (!idcomponente.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA,"V1.4",idcomponente));
						pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA, pos + tag.length());
			    }				
	
			 			
				
//				 buscar tag de qssi
			
				pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RQSSI);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String idcomponente = averiguaComponente(tag);
						if (!idcomponente.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microback.TAG_GENERICO_DUMMY + Microback.RQSSI,"V1.4",idcomponente));
						pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RQSSI, pos + tag.length());
			    }				
				
				
				
		 
				
				
				/* ****************************************** */
				/* ********     tags version 2.0     ******** */
				/* ****************************************** */
				/* Los tags de agenda y banner no han sufrido variacion desde la version anterior.*/

				// buscar tag de encuestas. v2.0
				pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RENCUESTA);
				while (pos > -1) {
						StringBuffer stbuf_tmp = new StringBuffer(stbuf.substring(0,pos));
						int pos_ini_tag=stbuf_tmp.lastIndexOf("<");
						int pos_fin_tag=stbuf.indexOf("</div>",pos);
						String tag = stbuf.substring(pos_ini_tag, pos_fin_tag+6);
						String idcomponente = averiguaComponente(tag);
						if (!idcomponente.equals("-1"))
							hshTagsStatus.put(tag,new Tridato(Microback.TAG_GENERICO_DUMMY + Microback.RENCUESTA,"V2.0",idcomponente));
						pos = stbuf.indexOf(Microback.TAG_GENERICO_DUMMY + Microback.RENCUESTA, pos + tag.length());
			    }					
				
		}
	}

	
	/**
	 * Método que parsea el código html.<br/>
	 * Reemplaza los tags de los microsites por comentarios
	 * Además meterá en hshTagsStatus un listado con todos los tags a parsear encontrados y su estado.
	 * Además meterá en hshTags un listado con todos los tags a parsear encontrados y su correspondiete "trozo" de html.
	 * 
	 * @param idioma
	 */
	public void doParser2Comentario(String idioma) {
		try {
			doAnalisy(idioma);
			doCalculaTagsComentario();
			
			  
			if (htmlOld!=null) {
				  htmlParsed = htmlOld;
				  Enumeration enumera = hshTags.keys();
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
		

		Enumeration enumera = hshTagsStatus.keys();
    	while (enumera.hasMoreElements()) {
    		String key = (String)enumera.nextElement();
    		Tridato tridato= (Tridato)hshTagsStatus.get(key);

    		
    		if (tridato.getKey().equals(Microback.TAG_NOTICIAS)) {
    			hshTags.put(key, "<!-- " + Microback.TAG_NOTICIAS + idsite + idioma + numeronoticias + " -->");
    		}
    		if (tridato.getKey().equals(Microback.TAG_AGENDA)) {
    			hshTags.put(key, "<!-- " + Microback.TAG_AGENDA + idsite + idioma + numeronoticias + " -->");
    		}
    		if (tridato.getKey().equals(Microback.TAG_BANNER)) {
    			hshTags.put(key, "<!-- " + Microback.TAG_BANNER + idsite + idioma + 1 + " -->");
    		}    		
    		if (tridato.getKey().equals(Microback.TAG_GENERICO_DUMMY + Microback.RAGENDA)) {
    			hshTags.put(key, "<!-- " + Microback.TAG_GENERICO_DUMMY + Microback.RAGENDA + idsite + idioma + numeronoticias + " -->");
    		} 
    		if (tridato.getKey().equals(Microback.TAG_GENERICO_DUMMY + Microback.RQSSI)) {
    			hshTags.put(key, "<!-- " + Microback.TAG_GENERICO_DUMMY + Microback.RQSSI + idsite + tridato.getValue2() + idioma +  " -->");
    		}    	
    		if (tridato.getKey().equals(Microback.TAG_GENERICO_DUMMY + Microback.RBANNER)) {
    			hshTags.put(key, "<!-- " + Microback.TAG_GENERICO_DUMMY + Microback.RBANNER + idsite + idioma + 1 + " -->");
    		}      		
    		if (tridato.getKey().equals(Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA)) {
    			if (tridato.getValue1().equals("V1.3")) {
    				hshTags.put(key, "<!-- " + Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA + idsite + tridato.getValue2() +  idioma + numeronoticias + " -->");
    			}
    			if (tridato.getValue1().equals("V1.4")) {
    				hshTags.put(key, "<!-- " + Microback.TAG_GENERICO_DUMMY + Microback.RNOTICIA + idsite + tridato.getValue2() + idioma + " -->");
    			}    			
    		}
    		if (tridato.getKey().equals(Microback.TAG_GENERICO_DUMMY + Microback.RENCUESTA)) {
    			if (tridato.getValue1().equals("V2.0")) {
    				hshTags.put(key, "<!-- " + Microback.TAG_GENERICO_DUMMY + Microback.RENCUESTA + idsite + tridato.getValue2() + idioma + " -->");
    			}
    		}
    		
    	}
    	
		
	}

	/* *************************************************************************** */
	/* ********************          auxiliares                ******************* */
	/* *************************************************************************** */	
	
	
	private String averiguaTipo(String cadena) {
		MParserHTML parsehtml = new MParserHTML(_version);
		return parsehtml.getAtributoTag(cadena, "propertyid");
	}

	private String averiguaComponente(String cadena) {
		MParserHTML parsehtml = new MParserHTML(_version);
		return parsehtml.getAtributoTag(cadena, "componenteid");
	}
	private String averiguaComponente2(String cadena,String idioma) {
		MParserHTML parsehtml = new MParserHTML(_version);
		return parsehtml.getAtributoTag2(cadena, "componenteID",idioma);
	}
	
	
	
	/* *************************************************************************** */
	/* ********************     getters/setters                ******************* */
	/* *************************************************************************** */
	
	public Hashtable getHshTags() {
		return hshTags;
	}
	public void setHshTags(Hashtable hshTags) {
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
