package es.caib.gusite.microback.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.struts.action.DynaActionForm;

import es.caib.gusite.microback.actionform.TraDynaActionForm;
import es.caib.gusite.microback.plugins.HibernateDelegateConverter;
import es.caib.gusite.micromodel.Actividadagenda;
import es.caib.gusite.micromodel.Agenda;
import es.caib.gusite.micromodel.Componente;
import es.caib.gusite.micromodel.Contacto;
import es.caib.gusite.micromodel.Contenido;
import es.caib.gusite.micromodel.Encuesta;
import es.caib.gusite.micromodel.Faq;
import es.caib.gusite.micromodel.Frqssi;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Noticia;
import es.caib.gusite.micromodel.Pregunta;
import es.caib.gusite.micromodel.Respuesta;
import es.caib.gusite.micromodel.Temafaq;
import es.caib.gusite.micromodel.Tipo;
import es.caib.gusite.micromodel.Traduccion;
import es.caib.gusite.micromodel.TraduccionActividadagenda;
import es.caib.gusite.micromodel.TraduccionActividadagendaPK;
import es.caib.gusite.micromodel.TraduccionAgenda;
import es.caib.gusite.micromodel.TraduccionAgendaPK;
import es.caib.gusite.micromodel.TraduccionComponente;
import es.caib.gusite.micromodel.TraduccionComponentePK;
import es.caib.gusite.micromodel.TraduccionContenido;
import es.caib.gusite.micromodel.TraduccionContenidoPK;
import es.caib.gusite.micromodel.TraduccionEncuesta;
import es.caib.gusite.micromodel.TraduccionEncuestaPK;
import es.caib.gusite.micromodel.TraduccionFContacto;
import es.caib.gusite.micromodel.TraduccionFContactoPK;
import es.caib.gusite.micromodel.TraduccionFaq;
import es.caib.gusite.micromodel.TraduccionFaqPK;
import es.caib.gusite.micromodel.TraduccionFrqssi;
import es.caib.gusite.micromodel.TraduccionFrqssiPK;
import es.caib.gusite.micromodel.TraduccionMicrosite;
import es.caib.gusite.micromodel.TraduccionMicrositePK;
import es.caib.gusite.micromodel.TraduccionNoticia;
import es.caib.gusite.micromodel.TraduccionNoticiaPK;
import es.caib.gusite.micromodel.TraduccionPregunta;
import es.caib.gusite.micromodel.TraduccionPreguntaPK;
import es.caib.gusite.micromodel.TraduccionRespuesta;
import es.caib.gusite.micromodel.TraduccionRespuestaPK;
import es.caib.gusite.micromodel.TraduccionTemafaq;
import es.caib.gusite.micromodel.TraduccionTemafaqPK;
import es.caib.gusite.micromodel.TraduccionTipo;
import es.caib.gusite.micromodel.TraduccionTipoPK;
import es.caib.gusite.micromodel.Traducible2;
import es.caib.gusite.micromodel.ValueObject;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;

/**
 * Utilidades para pasar datos entre <code>ValueObject</code> i
 * <code>ActionForm</code>.
 * 
 * @author Indra
 */
public class VOUtils {

	public static void populate(ValueObject vo, DynaActionForm form) throws Exception {
        //BeanUtils.populate(vo, form.getMap());
    }

    public static void populate(Traducible2 vo, TraDynaActionForm form) throws Exception {

    	List<?> llista = (List<?>) form.get("traducciones");
    	List<?> langs = DelegateUtil.getIdiomaDelegate().listarIdiomas();

        Map traduccions = new HashMap(langs.size());
                
        for (int i = 0; i < langs.size(); i++) {        	
        	
            Idioma lang = (Idioma) langs.get(i);
            
            if (vo instanceof Actividadagenda) {
            	((TraduccionActividadagenda) llista.get(i)).setId(new TraduccionActividadagendaPK());
            	((TraduccionActividadagenda) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Actividadagenda)vo).getId() != null) {
                	((TraduccionActividadagenda) llista.get(i)).getId().setCodigoActividadAgenda(((Actividadagenda)vo).getId());
            	}
            }
            
            if (vo instanceof Agenda) {
            	((TraduccionAgenda) llista.get(i)).setId(new TraduccionAgendaPK());
            	((TraduccionAgenda) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Agenda)vo).getId() != null) {
            		((TraduccionAgenda) llista.get(i)).getId().setCodigoAgenda(((Agenda)vo).getId());
            	}
            }
            
            if (vo instanceof Componente) {
            	((TraduccionComponente) llista.get(i)).setId(new TraduccionComponentePK());
            	((TraduccionComponente) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	
            	if (((Componente)vo).getId() != null) {
            		TraduccionComponentePK tradPk = ((TraduccionComponente) llista.get(i)).getId();
            		tradPk.setCodigoComponente(((Componente)vo).getId());
                	((TraduccionComponente) llista.get(i)).setId(tradPk);
            	}
            }
            
            if (vo instanceof Contenido) {
            	((TraduccionContenido) llista.get(i)).setId(new TraduccionContenidoPK());
            	((TraduccionContenido) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Contenido)vo).getId() != null) {
                	((TraduccionContenido) llista.get(i)).getId().setCodigoContenido(((Contenido)vo).getId());
            	}
            }
            
            if (vo instanceof Encuesta) {
            	((TraduccionEncuesta) llista.get(i)).setId(new TraduccionEncuestaPK());
            	((TraduccionEncuesta) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Encuesta)vo).getId() != null) {
                	((TraduccionEncuesta) llista.get(i)).getId().setCodigoEncuesta(((Encuesta)vo).getId());
            	}
            }
            
            if (vo instanceof Faq) {
            	((TraduccionFaq) llista.get(i)).setId(new TraduccionFaqPK());
            	((TraduccionFaq) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	
            	if (((Faq)vo).getId() != null) {
                	((TraduccionFaq) llista.get(i)).getId().setCodigoFaq(((Faq)vo).getId());
            	}
            }
            
            if (vo instanceof Frqssi) {
            	((TraduccionFrqssi) llista.get(i)).setId(new TraduccionFrqssiPK());
            	((TraduccionFrqssi) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	
            	if (((Frqssi)vo).getId() != null) {
                	((TraduccionFrqssi) llista.get(i)).getId().setCodigoFrqssi(((Frqssi)vo).getId());
            	}
            }
            
            if (vo instanceof Noticia) {
            	((TraduccionNoticia) llista.get(i)).setId(new TraduccionNoticiaPK());
            	((TraduccionNoticia) llista.get(i)).getId().setCodigoIdioma(lang.getLang());            	
            	if (((Noticia)vo).getId() != null) {
                	((TraduccionNoticia) llista.get(i)).getId().setCodigoNoticia(((Noticia)vo).getId());
            	}
            }
            
            if (vo instanceof Pregunta) {
            	((TraduccionPregunta) llista.get(i)).setId(new TraduccionPreguntaPK());
            	((TraduccionPregunta) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Pregunta)vo).getId() != null) {
                	((TraduccionPregunta) llista.get(i)).getId().setCodigoPregunta(((Pregunta)vo).getId());
            	}
            }
            
            if (vo instanceof Respuesta) {
            	((TraduccionRespuesta) llista.get(i)).setId(new TraduccionRespuestaPK());
            	((TraduccionRespuesta) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Respuesta)vo).getId() != null) {
                	((TraduccionRespuesta) llista.get(i)).getId().setCodigoRespuesta(((Respuesta)vo).getId());
            	}
            }
            
            if (vo instanceof Temafaq) {
            	((TraduccionTemafaq) llista.get(i)).setId(new TraduccionTemafaqPK());
            	((TraduccionTemafaq) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Temafaq)vo).getId() != null) {
                	((TraduccionTemafaq) llista.get(i)).getId().setCodigoTema(((Temafaq)vo).getId());
            	}
            }
            
            if (vo instanceof Tipo) {
            	((TraduccionTipo) llista.get(i)).setId(new TraduccionTipoPK());
            	((TraduccionTipo) llista.get(i)).getId().setCodigoIdioma(lang.getLang());
            	if (((Tipo)vo).getId() != null) {
                	((TraduccionTipo) llista.get(i)).getId().setCodigoTipo(((Tipo)vo).getId());
            	}
            }
            
            if (vo instanceof Microsite) {
            	((TraduccionMicrosite) llista.get(i)).setId(new TraduccionMicrositePK());
            	((TraduccionMicrosite) llista.get(i)).getId().setCodigoIdioma(lang.getLang());            	
            	if (((Microsite)vo).getId() != null) {
            		((TraduccionMicrosite) llista.get(i)).getId().setCodigoMicrosite(((Microsite)vo).getId());
            	}
            }
            
            if (vo instanceof Contacto) {
            	((TraduccionFContacto) llista.get(i)).setId(new TraduccionFContactoPK());
            	((TraduccionFContacto) llista.get(i)).getId().setCodigoIdioma(lang.getLang());            	
            	if (((Contacto)vo).getId() != null) {
                	((TraduccionFContacto) llista.get(i)).getId().setCodigoFContacto(((Contacto)vo).getId());
            	}
            }
            
            traduccions.put(lang.getLang(), llista.get(i));
        }

        vo.setTraduccionMap(traduccions);
    }

    public static void describe(DynaActionForm form, ValueObject vo) throws Exception {
        testBeanUtils();
        //BeanUtils.populate(form, BeanUtils.describe(vo));
    }

    public static void describe(TraDynaActionForm form, Traducible2 vo) throws Exception {
        testBeanUtils();
        
        //BeanUtils.populate(form, BeanUtils.describe(vo));
        List<Traduccion> traducciones = (List) form.get("traducciones");
        List<?> langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
        for (int i = 0; i < langs.size(); i++) {
            String lang = (String) langs.get(i);
            Traduccion traduccion = vo.getTraduccion(lang);
            if (traduccion != null) {
                traducciones.set(i, traduccion);
            }
        }
    }

    private static void testBeanUtils() {
        Converter currentConverter = ConvertUtils.lookup(String.class);
        if (!(currentConverter instanceof HibernateDelegateConverter)) {
            Converter newConverter = new HibernateDelegateConverter(currentConverter);
            ConvertUtils.register(newConverter, String.class);
        }
    }
}
