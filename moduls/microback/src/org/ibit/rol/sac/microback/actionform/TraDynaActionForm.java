package org.ibit.rol.sac.microback.actionform;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.RequestUtils;
import org.ibit.rol.sac.microback.config.TraFormBeanConfig;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 *	Formulario que añade funcionalidad multilenguaje 
 *
 *@author Indra
 *
 */
public class TraDynaActionForm extends DynaActionForm {
	
	private static final long serialVersionUID = -1163286418802855304L;

	protected static Log log = LogFactory.getLog(TraDynaActionForm.class);

    protected String traClassName = null;

    protected ResourceBundle propertiesMessages = ResourceBundle.getBundle("sac-microback-messages");

    /* (non-Javadoc)
     * @see org.apache.struts.action.DynaActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    	
        super.reset(mapping, request);
        
        //No inicializamos formulario en aquellas peticiones que lleguen desde pleaseWait
        if (request.getParameter("espera") == null) {
        	
	        initialize(mapping);
	
	        if (traClassName == null) {
	            traClassName = getTraduccionClassName(request, mapping);
	        }
	
	        List traducciones = (List) this.get("traducciones");
	        try {
	            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
	            int numLangs = delegate.listarLenguajes().size();
	            for (int i = 0; i < numLangs; i++) {
	                traducciones.add(RequestUtils.applicationInstance(traClassName));
	            }
	        } catch (Throwable t) {
	            log.error("Error creando traducciones", t);
	        }
        
        }
    }
    
    /**
     * Método que resetea los campos de formulario
     * @param mapping	mapeo
     * @param request   petición de usuario
     */
    public void resetForm(ActionMapping mapping, HttpServletRequest request) {
        
        super.reset(mapping, request);

	        initialize(mapping);
	
	        if (traClassName == null) {
	            traClassName = getTraduccionClassName(request, mapping);
	        }
	
	        List traducciones = (List) this.get("traducciones");
	        try {
	            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
	            int numLangs = delegate.listarLenguajes().size();
	            for (int i = 0; i < numLangs; i++) {
	                traducciones.add(RequestUtils.applicationInstance(traClassName));
	            }
	        } catch (Throwable t) {
	            log.error("Error creando traducciones", t);
	        }
 
    }    

    /**
     * Método que devuelve el nombre de la clase multilenguaje
     * @param request	petición de usuario
     * @param mapping	mapeo
     * @return String	nombre de la clase multilenguaje
     */
    private String getTraduccionClassName(HttpServletRequest request, ActionMapping mapping) {
        ModuleConfig config = RequestUtils.getModuleConfig(request, getServlet().getServletContext());
        TraFormBeanConfig beanConfig = (TraFormBeanConfig) config.findFormBeanConfig(mapping.getName());
        String className = beanConfig.getTraduccionClassName();
        return className;
    }
    
    // FUNCIONES DE CHEQUEO DE CAMPOS
    
    /**
     * Método que revisa que el String pasado tiene formato de correo electrónico
     * @param email	correo electrónico
     * @return boolean	Devuelve true si el formato es correcto
     */
    public static boolean esMail(String email) 
    {
        if (email==null) email="";
        if (email.equals("null") || (email.length()==0)) email="";

        if (email.equals("")) return true;
        
        return email.matches(".+@.+\\.[a-z]+");
    }

    /**
     * Método que revisa que el String pasado es un número entero
     * @param numero número	
     * @return boolean Devuelve true si el string es un número entero
     */
    public static boolean esEntero(String numero) 
    {
        if (numero==null) numero="0";
        if (numero.equals("null") || (numero.length()==0)) numero="0";

        return numero.matches("[0-9]{1,40}");
    }
    
    /**
     * Método que revisa que el String pasado es una fecha de formato válido
     * @param in String fecha
     * @return boolean Devuelve true si el string tiene formato de fecha válida
     */
    public static boolean FechaValida(String in) {
    	
    if (in.length()!=10 && in.length()!=16)	return false;
    try{
    	DateFormat formato=null;
        if (in.length()==10) in=in+" 00:00";
    	if (!in.matches("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}")) return false;
        formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        formato.setLenient(false);
        formato.parse(in);

    }
    catch(ParseException e){
        return false;
    }

        return true;
    }
 
    /**
     * Método que revisa que el String pasado es un número decimal
     * @param in	String número decimal
     * @param entero integer que representa la parte entera del número
     * @param decimal integer que representa la parte decimal del número
     * @return boolean Devuelve true si el string es un número decimal
     */
    public static boolean Decimal(String in, int entero, int decimal) {

        String regexp="";

        if (in==null) in="0";
        if (in.equals("null") || (in.length()==0)) in="0";

        if (in.substring(0,1).equals(",")) in="0"+in;
        if (in.lastIndexOf(',')!=-1) regexp="[0-9]{1,"+entero+"}[,][0-9]{1,2}";
        else regexp="[0-9]{1,"+entero+"}";
       
        return in.matches(regexp);
    }

    /**
     * Método que transforma un String en formato double
     * @param in	String a transformar
     * @return String	String transformado
     */
    public static String Double2String(String in) {

        if (in==null) return "";
        if (in.equals("null") || (in.length()==0)) return "";
        in=in.replace('.',',');
        if (in.substring(0,1).equals(","))
            in="0"+in;
        return in;
    }    

    
}
