package es.caib.gusite.utilities.struts;

import es.caib.gusite.utilities.cadenas.Cadenas;
import es.caib.gusite.utilities.struts.exception.CaibException;

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

public class CaibExceptionHandler extends ExceptionHandler
{
  private Log log = LogFactory.getLog(CaibExceptionHandler.class);

  public ActionForward execute(Exception paramException, ExceptionConfig paramExceptionConfig, ActionMapping paramActionMapping, ActionForm paramActionForm, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException
  {
    ActionError localActionError = null;
    ActionForward localActionForward;
    if (paramExceptionConfig.getPath() != null)
      localActionForward = new ActionForward(paramExceptionConfig.getPath());
    else
      localActionForward = paramActionMapping.getInputForward();
    if ((paramException instanceof CaibException))
    {
      CaibException localCaibException = (CaibException)paramException;
      localActionError = new ActionError("webcaib.errors.caib.generico", Cadenas.CRtoBR(paramException.toString()));
    }
    else
    {
      localActionError = new ActionError("webcaib.errors.nocaib", paramException.getMessage());
    }
    String str = localActionError.getKey();
    paramHttpServletRequest.setAttribute("org.apache.struts.action.EXCEPTION", paramException);
    super.storeException(paramHttpServletRequest, str, localActionError, localActionForward, paramExceptionConfig.getScope());
    return localActionForward;
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.struts.CaibExceptionHandler
 * JD-Core Version:    0.6.2
 */