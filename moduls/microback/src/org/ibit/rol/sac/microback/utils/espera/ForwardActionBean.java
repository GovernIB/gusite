package org.ibit.rol.sac.microback.utils.espera;

/**
 * This class is a utility class that goes along with the
 * LongWaitRequestProcessor and helps in processing requests that require
 * long transaction support.
 * @author Indra
 */
public class ForwardActionBean {

  private String actionPath;

  public void setActionPath(String v) {
    this.actionPath = v;
  }

  public String getActionPath() {
    return this.actionPath;
  }
}