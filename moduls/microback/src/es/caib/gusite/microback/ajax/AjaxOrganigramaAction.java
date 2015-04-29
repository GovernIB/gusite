package es.caib.gusite.microback.ajax;

import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.OrganigramaProvider;
import es.caib.gusite.plugins.organigrama.UnidadListData;

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
import java.util.Collection;

/**
 * Author by at4.net
 */
public class AjaxOrganigramaAction extends Action {

    protected static Log log = LogFactory.getLog(AjaxOrganigramaAction.class);


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String node = request.getParameter("node");
        String lang = "ca";
		OrganigramaProvider op = PluginFactory.getInstance().getOrganigramaProvider();
		Collection<UnidadListData> unidades;
		Collection<UnidadListData> unidadesHijas;
        try {
            if (node != null && !"".equals(node)) {
                Long idUa = Long.parseLong(node);
                unidades = op.getUnidadesHijas(idUa, lang);
            } else {
            	unidades = op.getUnidadesPrincipales(lang);
            }

            JSONArray jsonArray = new JSONArray();
            JSONObject json;

            for (UnidadListData unidad : unidades) {
                json = new JSONObject();
                json.put("label", unidad.getNombre());
                json.put("id", unidad.getId());
                unidadesHijas = op.getUnidadesHijas(unidad.getId(), lang);
                if (unidadesHijas.size() > 0) {
                    json.put("load_on_demand", true);
                }
                
                jsonArray.put(json);
            }

            String txtrespuesta = "";
            txtrespuesta += jsonArray;
            response.reset();
            response.setContentType("application/json");
            response.setContentLength(txtrespuesta.getBytes().length);
            response.getOutputStream().write(txtrespuesta.getBytes());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        } catch (PluginException e) {
            log.error(e.getMessage(), e);
		}
        return null;
    }
}
