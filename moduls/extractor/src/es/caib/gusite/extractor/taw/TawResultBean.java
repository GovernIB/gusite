package es.caib.gusite.extractor.taw;

import org.fundacionctic.taw.treetable.model.URLNode;

public class TawResultBean {

	public static final boolean PARSED_OK=true;
	public static final boolean PARSED_ERROR=false;
	
	protected int errores;
	protected int warnings;
	
	protected int p1auto;
	protected int p1manual;
	protected int p2auto;
	protected int p2manual;
	protected int p3auto;
	protected int p3manual;
	protected boolean parseado;
	protected String mensaje;
	
	protected URLNode urlNode;
	
	public TawResultBean() {
		p1auto=0;
		p1manual=0;
		p2auto=0;
		p2manual=0;
		p3auto=0;
		p3manual=0;
		errores=0;
		warnings=0;
		mensaje="";
	}
	

	public int getErrores() {
		return errores;
	}

	public void setErrores(int errores) {
		this.errores = errores;
	}

	public int getWarnings() {
		return warnings;
	}

	public void setWarnings(int warnings) {
		this.warnings = warnings;
	}

	public int getP1auto() {
		return p1auto;
	}

	public void setP1auto(int p1auto) {
		this.p1auto = p1auto;
	}

	public int getP1manual() {
		return p1manual;
	}

	public void setP1manual(int p1manual) {
		this.p1manual = p1manual;
	}

	public int getP2auto() {
		return p2auto;
	}

	public void setP2auto(int p2auto) {
		this.p2auto = p2auto;
	}

	public int getP2manual() {
		return p2manual;
	}

	public void setP2manual(int p2manual) {
		this.p2manual = p2manual;
	}

	public int getP3auto() {
		return p3auto;
	}

	public void setP3auto(int p3auto) {
		this.p3auto = p3auto;
	}

	public int getP3manual() {
		return p3manual;
	}

	public void setP3manual(int p3manual) {
		this.p3manual = p3manual;
	}

	public boolean isParseado() {
		return parseado;
	}

	public void setParseado(boolean parseado) {
		this.parseado = parseado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public URLNode getUrlNode() {
		return urlNode;
	}

	public void setUrlNode(URLNode urlNode) {
		this.urlNode = urlNode;
	}
}
