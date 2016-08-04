package es.caib.gusite.extractor.tidy;

import java.util.ArrayList;

import org.w3c.dom.Element;

public class TidyResultBean {

	public static final boolean PARSED_OK=true;
	public static final boolean PARSED_ERROR=false;
	
	protected int errores;
	protected int warnings;
	protected StringBuffer mensajesall;
	protected StringBuffer mensajes;
	protected StringBuffer outxhtml;
	protected StringBuffer outtextoplano;
	protected StringBuffer outbody;
	protected StringBuffer outhead;
	protected Element outarboldom;
	protected boolean parseado;
	protected ArrayList listawarnings;
	protected ArrayList listaerrores;
	

	/**
	 * Devuelve el número de errores que se han producido parseando.<br/>
	 * @return int
	 */
	public int getErrores() {
		return errores;
	}
	public void setErrores(int errores) {
		this.errores = errores;
	}
	
	/**
	 * Devuelve el número de warnings que se han producido parseando.<br/>
	 * @return int
	 */
	public int getWarnings() {
		return warnings;
	}
	public void setWarnings(int warnings) {
		this.warnings = warnings;
	}
	
	/**
	 * Devuelve una cadena con los mensajes resultado del parseado.<br/>
	 * Sólo se incluyen mensajes de error o warnings.<br/>
	 * @return StringBuffer
	 */
	public StringBuffer getMensajes() {
		return mensajes;
	}
	public void setMensajes(StringBuffer mensajes) {
		this.mensajes = mensajes;
	}
	
	/**
	 * Devuelve un xhtml como resultado del parseado.<br/> 
	 * @return StringBuffer
	 */
	public StringBuffer getOutxhtml() {
		return outxhtml;
	}
	public void setOutxhtml(StringBuffer outxhtml) {
		this.outxhtml = outxhtml;
	}
	
	/**
	 * Devuelve sólo el texto del html. En otras palabras: quita todos los tags html y retorna el texto.<br/>
	 * @return StringBuffer
	 */
	public StringBuffer getOuttextoplano() {
		return outtextoplano;
	}
	public void setOuttextoplano(StringBuffer outtextoplano) {
		this.outtextoplano = outtextoplano;
	}
	
	/**
	 * Devuelve la parte <em>body</em> del xhtml resultado del parseado.<br/>
	 * No incluye los propios tags de <em>body</em>.<br/>
	 * @return StringBuffer
	 */
	public StringBuffer getOutbody() {
		return outbody;
	}
	public void setOutbody(StringBuffer outbody) {
		this.outbody = outbody;
	}
	
	/**
	 * Devuelve la parte <em>head</em> del xhtml resultado del parseado.<br/>
	 * No incluye los propios tags de <em>head</em>.<br/>
	 * @return StringBuffer
	 */
	public StringBuffer getOuthead() {
		return outhead;
	}
	public void setOuthead(StringBuffer outhead) {
		this.outhead = outhead;
	}
	
	/**
	 * Devuelve el arbol DOM completo del xhtml resultado del parseado.<br/>
	 * @return Element
	 */
	public Element getOutarboldom() {
		return outarboldom;
	}
	public void setOutarboldom(Element outarboldom) {
		this.outarboldom = outarboldom;
	}
	
	/**
	 * Indica si el parseado ha sido correcto o no. Independientemente de errores o warnings.<br/>
	 * @return boolean
	 */
	public boolean isParseado() {
		return parseado;
	}
	public void setParseado(boolean parseado) {
		this.parseado = parseado;
	}
	
	/**
	 * Devuelve una cadena con <strong>todos los mensajes</strong> resultado del parseado.<br/>
	 * Se incluye la versión del tidy, incluyen mensajes de error o warnings y se incluyen los textos de ayuda a problemas.<br/>
	 * @return StringBuffer
	 */
	public StringBuffer getMensajesall() {
		return mensajesall;
	}
	public void setMensajesall(StringBuffer mensajesall) {
		this.mensajesall = mensajesall;
	}

	/**
	 * Devuelve un listado con los <em>warnings</em> encontrados.<br/>
	 * @return ArrayList
	 */
	public ArrayList getListawarnings() {
		return listawarnings;
	}
	public void setListawarnings(ArrayList listawarnings) {
		this.listawarnings = listawarnings;
	}
	
	/**
	 * Devuelve un listado con los <em>errores</em> encontrados.<br/>
	 * @return ArrayList
	 */
	public ArrayList getListaerrores() {
		return listaerrores;
	}
	public void setListaerrores(ArrayList listaerrores) {
		this.listaerrores = listaerrores;
	}
	
}
