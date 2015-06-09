package  es.caib.gusite.microintegracion.traductor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationServiceStub;
import es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationServiceStub.Param;
import es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationServiceStub.ParamListType;
import es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationServiceStub.Task;
import es.caib.gusite.microintegracion.ws.traductor.AutomaticTranslationServiceStub.TaskE;

/**
 * Clase que parametriza la petición de traducción y la envía al web-service Lucy
 * @author Indra
 *
 */
public class AutomaticTranslationService {

	protected static Log log = LogFactory.getLog(AutomaticTranslationService.class);

	protected static final String SERVICE_NAME = "TRANSLATE-TEXT",
								  ACTIVE="1", 
								  INACTIVE="0";

	protected static String _translationServerUrl = "http://scatwnt1.caib.es:8080/jaxws-AutomaticTranslationService/AutoTranslate",	//endpoint per defecte, es pot especificar en "es.caib.gusite.integracion.traductor.servidor"
							_areaGV = "(GV)",
							_dialegSetting = "BAL",
							_translationDirection = "CATALAN-SPANISH",
							_colorMarkups = INACTIVE,
							_markUnknowns = INACTIVE,
							_markConstants = INACTIVE,
							_markCompounds = INACTIVE,
							_markAlternatives = INACTIVE;

	/**
	 * Método que guarda los parámetros de la petición de traducción y el texto a traducir, 
	 * envía la petición y devuelve el texto traducido
	 * 
	 * @param input	texto a traducir
	 * @return	String	texto traducido
	 * @throws TraductorException 
	 */
	protected String translate(String input) throws TraductorException {

		try {			
			AutomaticTranslationServiceStub stub = new AutomaticTranslationServiceStub(_translationServerUrl);

			TaskE task13 = (TaskE)getObject(TaskE.class);

			Task task = (Task)getObject(Task.class);
			task.setService(SERVICE_NAME);
			task.setVerbose(true);

			ParamListType params = (ParamListType)getObject(ParamListType.class);

			params.addParam(setTranslationDirection(_translationDirection));
			params.addParam(setSubjectArea(_areaGV));
			params.addParam(setDialegSetting(_dialegSetting));
			params.addParam(setColorMarkups(_colorMarkups));
			params.addParam(setMarkUnknowns(_markUnknowns));
			params.addParam(setMarkConstants(_markConstants));
			params.addParam(setMarkCompounds(_markCompounds));
			params.addParam(setMarkAlternatives(_markAlternatives));
			params.addParam(setInput(input));

			task.setInputParams(params);
			task13.setTask(task);

			task13 = stub.translate(task13);
			params = task13.getTask().getOutputParams();

			return manageColors(convertStreamToString(getOutput(params)));

		} catch (AxisFault e) {
			log.error(e);
			throw new TraductorException("AxisFault:" + e.getMessage(), e);
		} catch (RemoteException e) {
			log.error(e);
			throw new TraductorException("RemoteException:" + e.getMessage(), e);
		} catch (IOException e) {
			log.error(e);
			throw new TraductorException("IOException:" + e.getMessage(), e);
		}
	}

	/**
	 * Método que guarda el parámetro TRANSLATION_DIRECTION del traductor Lucy
	 * 
	 * @param translationDirection	dirección de traducción
	 * @return Param				devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
	 */
	private Param setTranslationDirection(String translationDirection) throws TraductorException {

		String transDirName = "TRANSLATION_DIRECTION";
		String eMessage = "Error al carregar el la direcció de traducció";
		return setParamValue(transDirName, translationDirection, eMessage);
	}	

	/**
	 * Método que guarda el parámetro SUBJECT_AREAS del traductor Lucy
	 * 
	 * @param subjectArea	area de traducción
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
	 */
	private Param setSubjectArea(String subjectArea) throws TraductorException  {

	    String subjectAreaName = "SUBJECT_AREAS";
		String eMessage = "Error al carregar l' àrea de traducció";
		return setParamValue(subjectAreaName, subjectArea, eMessage);
	}

	/**
     * Método que guarda el parámetro DIALEG_SETTING del traductor Lucy
     * 
     * @param subjectArea   area de traducción
     * @return Param        devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
     */
    private Param setDialegSetting(String dialegSetting) throws TraductorException {

        String dialegSettingName = "DIALEG_SETTING";
        String eMessage = "Error al carregar el dialeg de la traducció";
        return setParamValue(dialegSettingName, dialegSetting, eMessage);
    }

    /**
     * Método generico para introducir parametros al traductor Lucy
     * 
     * @param subjectArea   area de traducción
     * @return Param        devuelve el parámetro con el texto y el valor
     * @throws TraductorException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    private Param setParamValue(String name, String value, String error) throws TraductorException {

        Param param = (Param) getObject(Param.class);
        param.setName(name);
        param.setValue(value);
        return param;

    }

	/**
	 * Método que guarda el parámetro INPUT del traductor Lucy
	 * 
	 * @param input		input de traducción (texto a traducir)
	 * @return Param	devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
	 */
	private Param setInput(String input) throws TraductorException {

		String inputName = "INPUT";
		String eMessage = "Error al carregar l' entrada a traduir";
		return setParamTxtValue(inputName, input, eMessage);
	}

	/**
	 * Método que guarda el parámetro COLOR_MARKUPS del traductor Lucy
	 * 
	 * @param colorMarkups	Marcadores de color (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
	 */
	private Param setColorMarkups(String colorMarkups) throws TraductorException {

	    String inputName = "COLOR_MARKUPS";
		String eMessage = "Error al carregar el parámetre COLOR_M";
		return setParamTxtValue(inputName, colorMarkups, eMessage);
	}	

	/**
	 * Método que guarda el parámetro MARK_UNKNOWNS del traductor Lucy
	 * 
	 * @param markUnknowns	Marcar las palabras desconocidas (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
	 */
	private Param setMarkUnknowns(String markUnknowns) throws TraductorException {

		String inputName = "MARK_UNKNOWNS";
		String eMessage = "Error al carregar el parámetre M_UNKN";
		return setParamTxtValue(inputName, markUnknowns, eMessage);
	}
	
	/**
	 * Método que guarda el parámetro MARK_CONSTANTS del traductor Lucy
	 * 
	 * @param markConstants	Marcar las constantes (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
	 */
	private Param setMarkConstants(String markConstants) throws TraductorException {

		String inputName = "MARK_CONSTANTS";
		String eMessage = "Error al carregar el parámetre M_CONS";
		return setParamTxtValue(inputName, markConstants, eMessage);
	}
	
	/**
	 * Método que guarda el parámetro MARK_COMPOUNDS del traductor Lucy
	 * 
	 * @param markCompounds	Marcar las palabras desconocidas (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
	 */
	private Param setMarkCompounds(String markCompounds) throws TraductorException {

		String inputName = "MARK_COMPOUNDS";
		String eMessage = "Error al carregar el parámetre M_COMP";
		return setParamTxtValue(inputName, markCompounds, eMessage);
	}

	/**
	 * Método que guarda el parámetro MARK_ALTERNATIVES del traductor Lucy
	 * 
	 * @param markAlternatives	Marcar las palabras con más de una acepción (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 */
	private Param setMarkAlternatives(String markAlternatives) throws TraductorException {

		String inputName = "MARK_ALTERNATIVES";
		String eMessage = "Error al carregar el parámetre M_ALTE";
		return setParamTxtValue(inputName, markAlternatives, eMessage);
	}

	/**
     * Método generico para introducir parametros al traductor Lucy
     * 
     * @param markAlternatives  Marcar las palabras con más de una acepción (Si/no)
     * @return Param        devuelve el parámetro con el texto y el valor
	 * @throws TraductorException 
     */
    private Param setParamTxtValue(String name, String txtValue, String error) throws TraductorException {

        Param param = (Param) getObject(Param.class);
        param.setName(name);
        param.setTxtValue(txtValue);
        return param;

    }

	/**
	 * Método que devuelve Stream con los datos traducidos del traductor Lucy
	 * 
	 * @param params		Listado de parámetros de traductor
	 * @return InputStream	devuelve los datos traducidos en un Stream
	 * @throws IOException 
	 */
	private InputStream getOutput(ParamListType params) throws IOException {

		String outputName = "OUTPUT";
		String eMessage = "Error al recuperar el text traduit";
		InputStream is = null;

	    for (Param param : params.getParam()) {
	        if(param.getName().equals(outputName)) {
	            is= param.getBinValue().getInputStream();
	            break;
	        }
	    }
	    return is;

	}

	/**
	 * Método que crea una nueva instancia org.apache.axis2.databinding.ADBBean
	 * 
	 * @param type
	 * @return	ADBBean
	 * @throws TraductorException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws java.lang.Exception
	 */
    private org.apache.axis2.databinding.ADBBean getObject(java.lang.Class type) throws TraductorException {
        try {
			return (org.apache.axis2.databinding.ADBBean) type.newInstance();
		} catch (InstantiationException e) {
			log.error(e);
			throw new TraductorException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e);
			throw new TraductorException(e.getMessage(), e);
		}
     }

    /**
     * Método que convierte el Stream de datos traducidos en un String
     * @param is	Stream de datos traducidos por el traductor Lucy
     * @return	String	texto traducido de tipo String
     * @throws TraductorException 
     */
    private String convertStreamToString(InputStream is) throws TraductorException {

        if (is == null) {
            log.error("Idioma no disponible en el traductor");
            throw new TraductorException("Idioma no disponible en el traductor");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1));
    	StringBuilder sb = new StringBuilder();
    	String line = null;
    	try {
    		while ((line = reader.readLine()) != null) {
    			sb.append(line + "\n");
    		}

    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    		try {
    		    is.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}

    	return sb.toString().trim();
    }

    /**
     * Método que reemplaza los tags de color rojo por defecto de las palabras desconocidas 
     * del traductor en color azul
     * 
     * @param input	texto traducido por el traductor Lucy
     * @return	String texto con los tags de color azul en el caso de tener tags de color rojo
     */
    private String manageColors(String output) {

    	String red = "<FONT COLOR=#ff0000>";
    	String blue = "<FONT COLOR=#0000ff>";

    	return output.replace(red, blue);
    }

}
