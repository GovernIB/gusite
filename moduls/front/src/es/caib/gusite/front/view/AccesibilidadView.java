package es.caib.gusite.front.view;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Información en cuanto a la accesibilidad del web
 * @author at4.net
 *
 */
@TemplateView(TemplateView.ACCESIBILIDAD)
public class AccesibilidadView extends PageView {
	
	private static final String FECHA_REVISION_ACCESIBILIDAD = "es.caib.gusite.fecha.revision.accesibilidad";
	 
	/**
	 * Fecha revisión accesibilidad
	 */
	@Variable("MVS_fecha_revision_acc")
	public String getFechaRevision() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			Date date = sdf.parse(System.getProperty(FECHA_REVISION_ACCESIBILIDAD));
			return sdf.format(date);
		} catch (Exception e) {
	         return null;
		}
	}
	
}
