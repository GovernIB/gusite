package es.caib.gusite.microback.actionform.formulario;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/**
 * Formulario para el proceso de importación de tema
 * 
 * @author at4.net
 * 
 */
public class ImportarTemaForm extends ActionForm {

	private static final long serialVersionUID = 2033793824633732367L;
	private FormFile archi;
	private Long idtema;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
	}

	public FormFile getArchi() {
		return archi;
	}

	public void setArchi(FormFile archi) {
		this.archi = archi;
	}

	public void setIdtema(Long idtema) {
		this.idtema = idtema;
	}

	public Long getIdtema() {
		return idtema;
	}

}
