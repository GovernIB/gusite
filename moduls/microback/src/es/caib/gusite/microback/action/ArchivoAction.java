package es.caib.gusite.microback.action;

import java.text.Normalizer;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.micromodel.Archivo;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micromodel.Usuario;
import es.caib.gusite.micropersistence.delegate.ArchivoDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateException;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.UsuarioDelegate;
import es.caib.gusite.micropersistence.util.ArchivoUtil;

/**
 * Action Base para enviar un archivo
 * <P>
 *
 * Definición Struts:<BR>
 * action path="/archivo"<BR>
 *
 * @author - Indra
 */
public class ArchivoAction extends BaseAction {

	protected static Log log = LogFactory.getLog(ArchivoAction.class);
	private static ResourceBundle rb = ResourceBundle.getBundle("sac-microback-messages");

	@Override
	public final ActionForward doExecute(final ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		final Microsite microsite = (Microsite) request.getSession().getAttribute("MVS_microsite");

		if ("exportarArchivosMicrosites".equals(request.getParameter("accion"))) {

			// Reseteamos valores de log.
			request.getSession().removeAttribute("mensaje");
			// Reaprovechamos el código de la clase MicroLog, aunque el nombre no sea el más
			// adecuado siendo esto una exportación...
			request.getSession().removeAttribute("MVS_importprocessor");
			request.getSession().removeAttribute("MVS_fechaexportprocessor");

			// recoger usuario.....
			if (request.getSession().getAttribute("MVS_usuario") == null) {

				final UsuarioDelegate usudel = DelegateUtil.getUsuarioDelegate();
				final Usuario usu = usudel.obtenerUsuariobyUsername(request.getRemoteUser());
				request.getSession().setAttribute("MVS_usuario", usu);

			}

			// Obtenemos la propiedad de sistema donde se exportarán los BLOB para mostrarla
			// al usuario.
			request.setAttribute("rutaArchivosEnFileSystem",
					System.getProperty("es.caib.gusite.archivos.rutaArchivosEnFileSystem"));

			return mapping.findForward("exportarArchivosMicrosites");

		} else if ("exportarArchivosDeTodosLosMicrosites".equals(request.getParameter("accion"))) {

			try {

				// Si nos lo solicitan vía properties, almacenamos los archivos en Filesystem.
				if (ArchivoUtil.almacenarEnFilesystem())
					ArchivoUtil.exportarArchivosDeTodosLosMicrosites(request);

				final String mensaje = "<strong>" + rb.getString("micro.exportar.todos") + "</strong>";
				request.getSession().setAttribute("mensaje", mensaje);

				final Date fecha = new Date();
				request.getSession().setAttribute("MVS_fechaexportprocessor", fecha);

				return mapping.findForward("exportarArchivosMicrosites");

			} catch (final DelegateException e) {

				log.error(e);

			}

		} else if (request.getParameter("id") != null) {

			final Long id = new Long(request.getParameter("id"));
			final ArchivoDelegate archi = DelegateUtil.getArchivoDelegate();

			if (archi.checkSite(microsite.getId(), id)) {
				addMessage(request, "info.seguridad");
				return mapping.findForward("info");
			}

			final Archivo archivo = archi.obtenerArchivo(id);

			if (archivo != null) {

				response.reset();
                String nombreNormalize= normalizador(archivo.getNombre());
				if (!forzarDownload(mapping, form, request)) {
					response.setContentType(archivo.getMime());
					// poner normalize en el nombre
					response.setHeader("Content-Disposition", "inline; filename=\"" + nombreNormalize + "\"");
				} else {
					response.setContentType("application/octet-stream");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreNormalize + "\"");
				}

				response.setContentLength(new Long(archivo.getPeso()).intValue());
				byte[] datos = null;
				final String nombre = archivo.getNombre();
				try {
					archivo.setNombre(obtenerAcentoAbierto(nombre));
					datos = archi.obtenerContenidoFichero(archivo);
				} catch (final Exception e) {
					archivo.setNombre(obtenerAcentoCerrado(nombre));
					datos = archi.obtenerContenidoFichero(archivo);
				} finally {
					response.getOutputStream().write(datos);
				}
				// final byte[] datos = archi.obtenerContenidoFichero(archivo);
				// response.getOutputStream().write(datos);

			}

		}

		return null;

	}

	public String obtenerAcentoAbierto(final String text) {

		final String text1 = text.replaceAll("a¿", "á"); // correcto
		final String text2 = text1.replaceAll("A¿", "Á"); // correcto
		final String text3 = text2.replaceAll("e¿", "é"); // correcto
		final String text4 = text3.replaceAll("E¿", "É"); // correcto
		final String text5 = text4.replaceAll("i¿", "í"); // CORRECTO
		final String text6 = text5.replaceAll("I¿", "Í"); // CORRECTO
		final String text7 = text6.replaceAll("o¿", "ó"); // correcto
		final String text8 = text7.replaceAll("O¿", "Ó"); // correcto
		final String text9 = text8.replaceAll("u¿", "ú"); // CORRECTO
		final String text10 = text9.replaceAll("U¿", "Ú"); // correcto
		return text10;
	}

	public String obtenerAcentoCerrado(final String text10) {
		final String text11 = text10.replaceAll("a¿", "à"); // correcto
		final String text12 = text11.replaceAll("A¿", "À"); // correcto
		final String text13 = text12.replaceAll("e¿", "è"); // correcto
		final String text14 = text13.replaceAll("E¿", "È"); // correcto
		final String text15 = text14.replaceAll("i¿", "ì");
		final String text16 = text15.replaceAll("I¿", "Ì");
		final String text17 = text16.replaceAll("o¿", "ò"); // correcto
		final String text18 = text17.replaceAll("O¿", "Ò"); // CORRECTO
		final String text19 = text18.replaceAll("u¿", "ù");
		final String text20 = text19.replaceAll("U¿", "Ù");

		return text20;
	}

	  private static String normalizador(String str)
	  {
	    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replaceAll(" ", "_");
	   }
	public boolean forzarDownload(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request)
			throws Exception {

		// FIXME amartin: ¿qué proceso de decisión habría que realizar aquí? Hay un
		// return false directamente.
		return false;

	}

}
