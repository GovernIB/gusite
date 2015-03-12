package es.caib.gusite.microback.ajax;

import es.caib.gusite.micromodel.PersonalizacionPlantilla;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcerda on 27/02/2015.
 */
public class AjaxPlantillasPerAction extends Action {

    protected static Log log = LogFactory.getLog(AjaxPlantillasPerAction.class);

    private static final String PLANTILLA_VOID = "Plantilla en blanc";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String txtrespuesta = "";
        String sel = request.getParameter("select");
        Long idPlantilla = Long.parseLong(sel);

        try {
            List<PersonalizacionPlantilla> personalizacionesPlantilla = new ArrayList<PersonalizacionPlantilla>();
            personalizacionesPlantilla.addAll(DelegateUtil.getPlantillaDelegate().obtenerPlantilla(idPlantilla).getPersonalizacionesPlantilla());

            JSONArray jsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("id", 0);
            json.put("titulo", PLANTILLA_VOID);
            json.put("plantilla", sel);
            jsonArray.put(json);
            for (PersonalizacionPlantilla pp : personalizacionesPlantilla) {
                json = new JSONObject();
                json.put("id", pp.getId());
                json.put("titulo", pp.getTitulo());
                json.put("plantilla", sel);
                jsonArray.put(json);
            }

            txtrespuesta += jsonArray;
            response.reset();
            response.setContentType("application/json");
            response.setContentLength(txtrespuesta.getBytes().length);
            response.getOutputStream().write(txtrespuesta.getBytes());

        } catch (IOException e) {
            log.error("Error de escritura", e);
        } catch (DelegateException e) {
            log.error("Error en EJB", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
