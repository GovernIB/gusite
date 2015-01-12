package es.caib.gusite.microback.actionform;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;

/**
 * Extensión de un ActionForm para representar el formulario de fecha
 * 
 * @author Indra
 */
public class DataForm extends ActionForm {


	private static final long serialVersionUID = 7482505374445298945L;
	private Calendar calendari = null;
   private String field = null;

   public DataForm() {
      calendari = new GregorianCalendar();
      calendari.setFirstDayOfWeek(Calendar.MONDAY);
   }

   /* Mètodes get i set */
   public int getAny() { return calendari.get(Calendar.YEAR); }
   public void setAny(int any) { calendari.set(Calendar.YEAR, any); }

   public int getMes() { return calendari.get(Calendar.MONTH); }
   public void setMes(int mes) { calendari.set(Calendar.MONTH, mes); }

   public int getDia() { return calendari.get(Calendar.DAY_OF_MONTH); }
   public void setDia(int dia) { calendari.set(Calendar.DAY_OF_MONTH, dia); }

   public String getField() { return field; }
   public void setField(String field) { this.field = field; }

   /* Gets d'informació adicional */
   public Date getData() { return calendari.getTime(); }

   public int getFirstDayPos() {
      // Backup del dia
      int diaActual = calendari.get(Calendar.DAY_OF_MONTH);

      // Fixam el primer dia del mes i obtenim la posicio
      int primerDia = calendari.getActualMinimum(Calendar.DAY_OF_MONTH);
      calendari.set(Calendar.DAY_OF_MONTH, primerDia);

      int firstDayPos = calendari.get(Calendar.DAY_OF_WEEK);
      // Restauram el dia
      calendari.set(Calendar.DAY_OF_MONTH, diaActual);

      // Els EUA i el diumenge.
      firstDayPos = ( (firstDayPos == 1) ? (7) : (firstDayPos - 1) );
      return firstDayPos;
   }

   public int getLastDay() {
      return calendari.getActualMaximum(Calendar.DAY_OF_MONTH);
   }

   /* Sobreescriure métodes de ActionForm */
   /* (non-Javadoc)
    * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
    */
   public void reset(ActionMapping mapping, HttpServletRequest request) {
      calendari = new GregorianCalendar();
      calendari.setFirstDayOfWeek(Calendar.MONDAY);
      field = null;
   }

   /* (non-Javadoc)
    * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
    */
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

      ActionErrors errors = new ActionErrors();
      if (field == null) {
         errors.add("field", new ActionError("error.field") );
      }

      return errors;
   }
}