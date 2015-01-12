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

public class DelegateExceptionHandler extends ExceptionHandler
{
  protected static Log log = LogFactory.getLog(DelegateExceptionHandler.class);

  public ActionForward execute(Exception paramException, ExceptionConfig paramExceptionConfig, ActionMapping paramActionMapping, ActionForm paramActionForm, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException
  {
    ActionForward localActionForward;
    if (paramExceptionConfig.getPath() != null)
      localActionForward = new ActionForward(paramExceptionConfig.getPath());
    else
      localActionForward = paramActionMapping.getInputForward();
    ActionError localActionError;
    if (paramException.getMessage().indexOf("Could not execute JDBC batch update") > 0)
      localActionError = new ActionError("webcaib.errors.fkconstraint");
    else
      localActionError = new ActionError(paramExceptionConfig.getKey(), paramException.getMessage());
    String str = localActionError.getKey();
    paramHttpServletRequest.setAttribute("org.apache.struts.action.EXCEPTION", paramException);
    super.storeException(paramHttpServletRequest, str, localActionError, localActionForward, paramExceptionConfig.getScope());
    return localActionForward;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.struts.DelegateExceptionHandler
 * JD-Core Version:    0.6.2
 */