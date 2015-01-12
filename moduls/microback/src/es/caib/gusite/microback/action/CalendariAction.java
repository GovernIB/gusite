package es.caib.gusite.microback.action;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Action para mostrar un calendario
 *
 * @struts.action
 *  name="dataForm"
 *  scope="request"
 *  validate="false"
 *  path="/moduls/calendario"
 *
 * @struts.action-forward
 *  name="success" path="/moduls/calendario.jsp"
 *
 */
public class CalendariAction extends BaseAction {

    protected static Log log = LogFactory.getLog(CalendariAction.class);

   public ActionForward doExecute(ActionMapping mapping, ActionForm form, 
   		HttpServletRequest request, HttpServletResponse response) throws Exception {

         Calendar calendari = new GregorianCalendar();
         int anyActual = calendari.get(Calendar.YEAR);

         String anys[] = new String[11];

         for (int i = 0; i < anys.length; i++) {
            anys[i] = String.valueOf(anyActual - 5 + i);
         }

         request.setAttribute("anys", anys);

         return (mapping.findForward("success"));
      }
}