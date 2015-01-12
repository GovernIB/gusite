package org.ibit.rol.sac.microfront.contacto.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.util.microtag.MParserHTML;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.base.bean.Pardato;
import org.ibit.rol.sac.micromodel.Contacto;
import org.ibit.rol.sac.micromodel.Lineadatocontacto;
import org.ibit.rol.sac.micromodel.TraduccionLineadatocontacto;
import org.ibit.rol.sac.micropersistence.delegate.ContactoDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;


/**
 * Manejador de la petici�n de formulario de contacto.
 * 
 * Mete en una lista el bean pardato.
 * En ese bean mete el texto y el tag html que se emplear�n en el JSP.
 * <BR>
 * Adem�s meter� en una variable de sesi�n una hashtable con la misma lista.
 * De esta forma cuando se envie el formulario, nos iremos a esta hash para
 * obtener el mapeo de los parametros con su texto correspondiente.
 * La variable en sesi�n es MVS_listparamform. 
 * 
 * @author Indra
 */

public class Bdcontacto extends Bdbase  {

	protected static Log log = LogFactory.getLog(Bdcontacto.class);
	
	//este contiene los tags html
	private ArrayList<Pardato> listalineas = new ArrayList<Pardato>(); 
	private Hashtable<String, Pardato> listalineashash = new Hashtable<String, Pardato>();
	private Contacto contacto = new Contacto();
	private String titulocontacto="";
	private HttpServletRequest req;
	private boolean error = false;
	
	/**
	 * Constructor de la clase, carga el contacto a partir de la request
	 * @param request
	 * @throws Exception
	 */
	public Bdcontacto(HttpServletRequest request) throws Exception {
		super(request);
		req = request;
		if ((microsite!=null) && (existeServicio(Microfront.RCONTACTO))) {
			recogercontacto();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la pagina solicitada.");
			error=true;
		}
	}
	
	/**
	 * M�tdo privado para recoger el contacto
	 * @throws Exception 
	 */
	private void recogercontacto() throws Exception{
		ContactoDelegate contactoldel = DelegateUtil.getContactoDelegate();
		String idcontacto ="" + getParameter(Microfront.PCONT);
		try {
			Long idcont = new Long(Long.parseLong(idcontacto));
			contacto = contactoldel.obtenerContacto(idcont);

			//comprobacion de microsite
			if (!(contacto.getIdmicrosite().longValue()==idsite.longValue())) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El formulario de contacto solicitado no pertenece al site");
					error=true;
			}
			//comprobacion de visibilidad
			if (!contacto.getVisible().equals("S")) {
					beanerror.setAviso("Aviso");
					beanerror.setMensaje("El formulario de contacto solicitado no pertenece al site");
					error=true;
			}	
				
			titulocontacto=contacto.getTitulocontacto(idioma);
			prepararlistado();
		} catch (Exception e) {
			log.error(e.getMessage());
            beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido al recuperar la pagina solicitada.");
			error=true;
		}
		
	}
	
	/**
	 * Prepara un arraylist que contiene el bean pardato.
	 * Prepara una hashtable que contiene el bean pardato.
	 * En el bean se mete:
	 * en el key: el texto o titulo
	 * en el value: el tag html del elemento del formulario
	 * 
	 * La clave en el hash ser� el id de lineadatocontacto.
	 *
	 */
	private void prepararlistado() {
    	Iterator<?> iter = contacto.getLineasdatocontacto().iterator();
	    Lineadatocontacto ld;
        while (iter.hasNext()) {
        	 ld = (Lineadatocontacto)iter.next();
        	 Pardato pardato = new Pardato();
        	 MParserHTML parserhtml = new MParserHTML(microsite.getRestringido());
        	 if ((ld.getTipo().equals(Contacto.RTYPE_TEXTAREA)) || (ld.getTipo().equals(Contacto.RTYPE_TEXTO))) {
	        	 pardato.setKey(((TraduccionLineadatocontacto)ld.getTraduccion(idioma)).getTexto()) ;
	        	 if (ld.getLineas()==0) 
	        		 pardato.setValue(parserhtml.getTagText(
			        				 	ld.getId().toString(),
			        				 	ld.getTamano(),
			        			 		ld.getObligatorio()).toString());
	        	 else
	        		 pardato.setValue(parserhtml.getTagTextarea(
			        				 	ld.getId().toString(),
			        				 	50,
			        				 	ld.getLineas(),
			        			 		ld.getObligatorio()).toString());
        	 }
        	 if  ((ld.getTipo().equals(Contacto.RTYPE_SELECTORMULTIPLE)) ||  (ld.getTipo().equals(Contacto.RTYPE_SELECTOR))) {
	        	 pardato.setKey(getNombreinselect(((TraduccionLineadatocontacto)ld.getTraduccion(idioma)).getTexto()));
	        	 pardato.setValue(parserhtml.getTagSelect(
	        			 		ld.getId().toString(),
	        			 		getListaopciones(((TraduccionLineadatocontacto)ld.getTraduccion(idioma)).getTexto()),
	        			 		ld.getTipo(),
	        			 		ld.getLineas(),
	        			 		ld.getObligatorio()).toString());
        	 }
        	 if (!ld.getTipo().equals(Contacto.RTYPE_TITULO)) {
	        	 listalineas.add(pardato);
	        	 listalineashash.put(ld.getId().toString(),pardato);
        	 }
        }
        if (listalineas.size()>0) req.getSession().setAttribute("MVS_listparamform", listalineashash);
	}

    /**
     * Devuelve un arraylist que contiene strings.
     * En la lista se introducen todos menos el primero.
     * Se utiliza como separador del string Microfront.separatorwords
     * @param cadena
     * @return
     */
    private ArrayList<String> getListaopciones(String cadena) {
    	ArrayList<String> lista=new ArrayList<String>();
	    if (cadena.length()>0) {
	        String txseparador = "" + Microfront.separatorwordsform;
	        String[] listastringcadenas = cadena.split(txseparador);
	        for (int i=1;i<listastringcadenas.length;i++)
	          if (listastringcadenas[i].length()>0) lista.add(listastringcadenas[i]);
	    }
	    return lista;
    }
    
    /**
     * devuelve la primera cadena de texto de un string utilizando como
     * separador Microfront.separatorwords
     * @param cadena
     * @return
     */
    private String getNombreinselect(String cadena) {
    	String retorno="";
    	if (cadena.length()>0) {
	        String txseparador = "" + Microfront.separatorwordsform;
	        String[] listastringcadenas = cadena.split(txseparador);
	        retorno=listastringcadenas[0];
	    }
    	return retorno;
    }
	
	public String getTitulocontacto() {
		return titulocontacto;
	}

	public boolean isError() {
		return error;
	}


	public void setError(boolean error) {
		this.error = error;
	}	

	public Contacto getContacto() {
		return contacto;
	}

	public ArrayList<Pardato> getListalineas() {
		return listalineas;
	}
	
	/**
	 * Implementacion del m�todo abstracto.
	 * Se le indica que estamos en el servicio de contacto.
	 */
	public String setServicio() {
		return Microfront.RCONTACTO;
	}
	
}
