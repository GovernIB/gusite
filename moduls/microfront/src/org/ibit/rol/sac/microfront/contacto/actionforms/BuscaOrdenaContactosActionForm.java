package org.ibit.rol.sac.microfront.contacto.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Clase BuscaOrdenaContactosActionForm.  Utilizado para buscar y ordenar Contactos. 
 * <P>
 * Formulario definido para la acción ListarNoticiasAction.
 * @author Indra
 */
public class BuscaOrdenaContactosActionForm extends ActionForm   {


	private static final long serialVersionUID = 320758427090372322L;
	
	private String filtro;
	 private String ordenacion;
	 
	 public BuscaOrdenaContactosActionForm() {}
	 	
	 public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest){
		 return null;
	 }
	
	 public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest){
	 }
	
	 public void setFiltro(String filtro){
		 this.filtro = filtro;
	 }
	
	 public String getFiltro(){
		 return filtro;
	 }
	
	 public void setOrdenacion(String ordenacion){
		 this.ordenacion = ordenacion;
	 }
	
	 public String getOrdenacion(){
		 return ordenacion;
	 }	 	
}
