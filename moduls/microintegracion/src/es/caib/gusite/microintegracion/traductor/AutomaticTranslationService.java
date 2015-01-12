package  es.caib.gusite.microintegracion.traductor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	 * @throws Exception
	 */
	protected String translate(String input) throws Exception {

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

		} catch (TraductorException traEx) {
		    throw traEx;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e); 
		}
	}

	/**
	 * Método que guarda el parámetro TRANSLATION_DIRECTION del traductor Lucy
	 * 
	 * @param translationDirection	dirección de traducción
	 * @return Param				devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setTranslationDirection(String translationDirection) throws Exception {

		String transDirName = "TRANSLATION_DIRECTION";
		String eMessage = "Error al carregar el la direcció de traducció";
		return setParamValue(transDirName, translationDirection, eMessage);
	}	

	/**
	 * Método que guarda el parámetro SUBJECT_AREAS del traductor Lucy
	 * 
	 * @param subjectArea	area de traducción
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setSubjectArea(String subjectArea) throws Exception {

	    String subjectAreaName = "SUBJECT_AREAS";
		String eMessage = "Error al carregar l' àrea de traducció";
		return setParamValue(subjectAreaName, subjectArea, eMessage);
	}

	/**
     * Método que guarda el parámetro DIALEG_SETTING del traductor Lucy
     * 
     * @param subjectArea   area de traducción
     * @return Param        devuelve el parámetro con el texto y el valor
     * @throws Exception
     */
    private Param setDialegSetting(String dialegSetting) throws Exception {

        String dialegSettingName = "DIALEG_SETTING";
        String eMessage = "Error al carregar el dialeg de la traducció";
        return setParamValue(dialegSettingName, dialegSetting, eMessage);
    }

    /**
     * Método generico para introducir parametros al traductor Lucy
     * 
     * @param subjectArea   area de traducción
     * @return Param        devuelve el parámetro con el texto y el valor
     * @throws Exception
     */
    private Param setParamValue(String name, String value, String error) throws Exception {

        try {
            Param param = (Param) getObject(Param.class);
            param.setName(name);
            param.setValue(value);
            return param;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception(error);
        }
    }

	/**
	 * Método que guarda el parámetro INPUT del traductor Lucy
	 * 
	 * @param input		input de traducción (texto a traducir)
	 * @return Param	devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setInput(String input) throws Exception {

		String inputName = "INPUT";
		String eMessage = "Error al carregar l' entrada a traduir";
		return setParamTxtValue(inputName, input, eMessage);
	}

	/**
	 * Método que guarda el parámetro COLOR_MARKUPS del traductor Lucy
	 * 
	 * @param colorMarkups	Marcadores de color (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setColorMarkups(String colorMarkups) throws Exception {

	    String inputName = "COLOR_MARKUPS";
		String eMessage = "Error al carregar el parámetre COLOR_M";
		return setParamTxtValue(inputName, colorMarkups, eMessage);
	}	

	/**
	 * Método que guarda el parámetro MARK_UNKNOWNS del traductor Lucy
	 * 
	 * @param markUnknowns	Marcar las palabras desconocidas (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setMarkUnknowns(String markUnknowns) throws Exception {

		String inputName = "MARK_UNKNOWNS";
		String eMessage = "Error al carregar el parámetre M_UNKN";
		return setParamTxtValue(inputName, markUnknowns, eMessage);
	}
	
	/**
	 * Método que guarda el parámetro MARK_CONSTANTS del traductor Lucy
	 * 
	 * @param markConstants	Marcar las constantes (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setMarkConstants(String markConstants) throws Exception {

		String inputName = "MARK_CONSTANTS";
		String eMessage = "Error al carregar el parámetre M_CONS";
		return setParamTxtValue(inputName, markConstants, eMessage);
	}
	
	/**
	 * Método que guarda el parámetro MARK_COMPOUNDS del traductor Lucy
	 * 
	 * @param markCompounds	Marcar las palabras desconocidas (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setMarkCompounds(String markCompounds) throws Exception {

		String inputName = "MARK_COMPOUNDS";
		String eMessage = "Error al carregar el parámetre M_COMP";
		return setParamTxtValue(inputName, markCompounds, eMessage);
	}

	/**
	 * Método que guarda el parámetro MARK_ALTERNATIVES del traductor Lucy
	 * 
	 * @param markAlternatives	Marcar las palabras con más de una acepción (Si/no)
	 * @return Param		devuelve el parámetro con el texto y el valor
	 * @throws Exception
	 */
	private Param setMarkAlternatives(String markAlternatives) throws Exception {

		String inputName = "MARK_ALTERNATIVES";
		String eMessage = "Error al carregar el parámetre M_ALTE";
		return setParamTxtValue(inputName, markAlternatives, eMessage);
	}

	/**
     * Método generico para introducir parametros al traductor Lucy
     * 
     * @param markAlternatives  Marcar las palabras con más de una acepción (Si/no)
     * @return Param        devuelve el parámetro con el texto y el valor
     * @throws Exception
     */
    private Param setParamTxtValue(String name, String txtValue, String error) throws Exception {

        try {
            Param param = (Param) getObject(Param.class);
            param.setName(name);
            param.setTxtValue(txtValue);
            return param;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception(error);
        }
    }

	/**
	 * Método que devuelve Stream con los datos traducidos del traductor Lucy
	 * 
	 * @param params		Listado de parámetros de traductor
	 * @return InputStream	devuelve los datos traducidos en un Stream
	 * @throws Exception
	 */
	private InputStream getOutput(ParamListType params) throws Exception {

		String outputName = "OUTPUT";
		String eMessage = "Error al recuperar el text traduit";
		InputStream is = null;

		try {
		    for (Param param : params.getParam()) {
		        if(param.getName().equals(outputName)) {
		            is= param.getBinValue().getInputStream();
		            break;
		        }
		    }
		    return is;

		} catch (Exception e) {
		    log.error(e.getMessage());
		    throw new Exception(eMessage);
		}
	}

	/**
	 * Método que crea una nueva instancia org.apache.axis2.databinding.ADBBean
	 * 
	 * @param type
	 * @return	ADBBean
	 * @throws java.lang.Exception
	 */
    private org.apache.axis2.databinding.ADBBean getObject(java.lang.Class type) throws java.lang.Exception{
        return (org.apache.axis2.databinding.ADBBean) type.newInstance();
     }

    /**
     * Método que convierte el Stream de datos traducidos en un String
     * @param is	Stream de datos traducidos por el traductor Lucy
     * @return	String	texto traducido de tipo String
     * @throws Exception 
     */
    private String convertStreamToString(InputStream is) throws Exception {

        if (is == null) {
            log.error("Idioma no disponible en el traductor");
            throw new TraductorException("Idioma no disponible en el traductor");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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
