package es.caib.gusite.extractor.tidy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.w3c.tidy.Tidy;


public class TidyHtmlParser {

	
	
	private ByteArrayInputStream bais; //Stream de entrada
	private ByteArrayOutputStream baos; //Stream de salida limpio
	private StringWriter stringWriterConsola = new StringWriter(); //Consola de salida
	
    private Element element; 
    private StringBuffer titulo = null;
    private StringBuffer body = null;
    private TidyResultBean resultado = null; //Resultado final 
    
    public TidyHtmlParser() {}


    public TidyResultBean parse(String in) throws Exception  {
        this.bais = new ByteArrayInputStream(in.getBytes());
        parseSimple();
        this.bais = new ByteArrayInputStream(in.getBytes());
        parseDOM();
        return resultado;
    }

    public TidyResultBean parseSimple(String in) throws Exception  {
        this.bais = new ByteArrayInputStream(in.getBytes());
        parseSimple();
        return resultado;
    }

    
    public TidyResultBean parseDOM(String in) throws Exception  {
        this.bais = new ByteArrayInputStream(in.getBytes());
        parseDOM();
        return resultado;
    }

    private void parseSimple() throws Exception  {
        try {
            Tidy tidy = new Tidy();
            tidy.setUpperCaseTags(true);
            tidy.setXHTML(true);
    		tidy.setDocType("omit");
    		tidy.setMakeClean(true);
    		tidy.setQuiet(false);
    		tidy.setIndentContent(true);
    		tidy.setSmartIndent(true);
    		tidy.setIndentAttributes(true);
    		tidy.setWord2000(true);
    		tidy.setShowWarnings(true);
    		tidy.setAltText("");

    		tidy.setErrout(new PrintWriter(stringWriterConsola ,true));
            
    		baos = new ByteArrayOutputStream();
    		tidy.parse(bais,baos);
            
    		this.resultado = new TidyResultBean();
            this.resultado.setErrores(tidy.getParseErrors());
            this.resultado.setWarnings(tidy.getParseWarnings());
            this.resultado.setMensajesall(new StringBuffer(stringWriterConsola.toString()));
            this.resultado.setMensajes(limpiaMensajesall(this.resultado.getMensajesall()));
            this.resultado.setOutxhtml(new StringBuffer(baos.toString()));
            this.resultado.setOuthead(getContentTag(this.resultado.getOutxhtml(), "head", 0));
            this.resultado.setOutbody(getContentTag(this.resultado.getOutxhtml(), "body", 0));
            this.resultado.setParseado(TidyResultBean.PARSED_OK);
            stringbuffer2List();
            
        } catch (Exception ex) {
        	this.resultado.setMensajes(new StringBuffer(ex.getMessage()));
        	this.resultado.setParseado(TidyResultBean.PARSED_ERROR);
            throw new Exception(ex);
        }
    }
    
    
    private void parseDOM() throws Exception  {
        try {
        	Tidy tidy = new Tidy();
            tidy.setUpperCaseTags(true);
            tidy.setMakeClean(true);
            tidy.setShowWarnings(false);
            tidy.setXmlOut(false);
            tidy.setWord2000(true);
            tidy.setFixBackslash(true);
            tidy.setXHTML(true);
            tidy.setWrapSection(true);
            tidy.setWrapScriptlets(true);
            tidy.setWrapPhp(true);
            tidy.setQuiet(true);

    	
            PrintWriter errorWriterCDOM = new PrintWriter(new StringWriter() ,false);
            tidy.setErrout(errorWriterCDOM);
            org.w3c.dom.Document root = tidy.parseDOM(bais, null);
            element = root.getDocumentElement();
            
            this.titulo = this.getTitulo();
            this.body = this.getBody();

            this.resultado.setOutarboldom(element);
            this.resultado.setOuttextoplano(this.getBody());
            this.resultado.setParseado(TidyResultBean.PARSED_OK);
            
        } catch (Exception ex) {
        	this.resultado.setMensajes(new StringBuffer(ex.getMessage()));
        	this.resultado.setParseado(TidyResultBean.PARSED_ERROR);
            throw new Exception(ex);
        }
    }


    public StringBuffer getTitulo() {
        if (this.titulo != null) {
            return this.titulo;
        }

        if (element == null) {
            return null;
        }

        StringBuffer title = null;

        NodeList nl = element.getElementsByTagName("title");

        if (nl.getLength() > 0) {
            Element titleElement = ((Element) nl.item(0));
            Text text = (Text) titleElement.getFirstChild();

            if (text != null) {
                title = new StringBuffer(text.getData());
            }
        }

        return title;
    }

    public StringBuffer getBody() {
        if (this.body != null) {
            return this.body;
        }

        if (element == null) {
            return null;
        }

        StringBuffer body = new StringBuffer("");
        NodeList nl = element.getElementsByTagName("body");

        if (nl.getLength() > 0) {
            body = getBodyText(nl.item(0));
        }

        return body;
    }


    private StringBuffer getBodyText(Node node) {
        NodeList nl = node.getChildNodes();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < nl.getLength(); i++) {
            Node child = nl.item(i);

            switch (child.getNodeType()) {
            case Node.ELEMENT_NODE:

                buffer.append(getBodyText(child));
                buffer.append(" \n");
            
            	/*
            	if (!child.getNodeName().toLowerCase().equals("script")) {
                    buffer.append(getBodyText(child));
                    buffer.append(" \n");
                }
                */

                break;

            case Node.TEXT_NODE:
                buffer.append(((Text) child).getData());
                
                break;
            }
        }

        return buffer;
    }
	
    /**
     * Devuelve el <em>innerHMTL</em> de dentro de una etiqueta HTML. <br/>
     * Se empieza a buscar el tag a partir de una determinada posición. <br/>
     * @param html, String con código html
     * @param tag, String con el nombre de la etiqueta
     * @param pos, int, posición a partir de la que se busca
     * @return StringBuffer
     */
    private StringBuffer getContentTag(StringBuffer html, String tag, int pos) {
    	StringBuffer stbuf2find = new StringBuffer(html.toString().toLowerCase());
    	String tag2search_init = "<" + tag + ">";
    	String tag2search_end = "</" + tag + ">";
    	if ( (html!=null) && (html.length()>(tag2search_init.length()+tag2search_end.length())) ) {
	    	int pos_ini_tag=stbuf2find.indexOf(tag2search_init, pos)+tag2search_init.length();
	    	int pos_fin_tag=stbuf2find.indexOf(tag2search_end,pos_ini_tag);
	    	return (new StringBuffer(html.substring(pos_ini_tag,pos_fin_tag)));
    	} else {
    		return null;
    	}
    }
    
    private StringBuffer limpiaMensajesall(StringBuffer mensajesall) {

    	String tag2search_init = "\"InputStream\"";
    	String tag2search_end = "\n\n";
    	if ((mensajesall != null) && (mensajesall.length() > (tag2search_init.length() + tag2search_end.length()))) {
	    	int pos_ini_tag = mensajesall.indexOf(tag2search_init) + tag2search_init.length();
	    	int pos_fin_tag = mensajesall.indexOf(tag2search_end, pos_ini_tag);
            if (pos_fin_tag == -1) {
                pos_fin_tag = mensajesall.length();
            }
	    	return (new StringBuffer(mensajesall.substring(pos_ini_tag, pos_fin_tag)));
    	} else {
    		return null;
    	}
    }
	
    private void stringbuffer2List() {
    	//recorrer todas las líneas del string de mensajes e ir metiéndolas en la lista
    	if ((resultado.getMensajes()!=null) && (resultado.getMensajes().length()>0)) {
    		ArrayList listawarnings = new ArrayList();
    		ArrayList listaerrors = new ArrayList();
	    	StringTokenizer st=new StringTokenizer(resultado.getMensajes().toString(),"\n");
			int n=st.countTokens();
			String[] str= new String[n];
			for (int i=0;i<n;i++) {
				str[i]=st.nextToken();
				if (str[i].indexOf("Warning:")!=-1) listawarnings.add(str[i]);
				else if (str[i].indexOf("Error:")!=-1) listaerrors.add(str[i]);
			}
			resultado.setListawarnings(listawarnings);
			resultado.setListaerrores(listaerrors);
    	}
    }
    
    
}