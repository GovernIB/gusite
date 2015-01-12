package es.caib.gusite.utilities.struts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

public class ExcepcionGlobalHandler extends ExceptionHandler
{
  protected static Log log = LogFactory.getLog(ExcepcionGlobalHandler.class);

  public ActionForward execute(Exception paramException, ExceptionConfig paramExceptionConfig, ActionMapping paramActionMapping, ActionForm paramActionForm, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException
  {
    ActionForward localActionForward;
    if (paramExceptionConfig.getPath() != null)
      localActionForward = new ActionForward(paramExceptionConfig.getPath());
    else
      localActionForward = paramActionMapping.getInputForward();
    ActionError localActionError = new ActionError("webcaib.errors.presentacion", paramException.getMessage());
    log.error("Error en action/pagina de webcaib: " + paramException.getMessage());
    String str = localActionError.getKey();
    paramHttpServletRequest.setAttribute("org.apache.struts.action.EXCEPTION", paramException);
    super.storeException(paramHttpServletRequest, str, localActionError, localActionForward, paramExceptionConfig.getScope());
    return localActionForward;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.struts.ExcepcionGlobalHandler
 * JD-Core Version:    0.6.2
 */