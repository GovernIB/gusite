package es.caib.gusite.microback.ajax;

import es.caib.gusite.micromodel.*;
import es.caib.gusite.micropersistence.delegate.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Action que simplemente se encarga de devolver si una URI es válida
 *
 * @author tcerda on 30/01/2015.
 */
public class AjaxCheckUriAction extends Action {

    protected static Log log = LogFactory.getLog(AjaxCheckUriAction.class);

    private static final String EXISTE = "<datos><dato>existe</dato></datos>";
    private static final String OK = "<datos><dato>OK</dato></datos>";
    private static final String ERROR = "<datos><dato>ERROR</dato></datos>";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String txtrespuesta="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        String uri = request.getParameter("URI");
        String type = request.getParameter("type");
        String idioma = request.getParameter("idioma");
        UriType currentType = UriType.valueOf(type.toUpperCase());

        txtrespuesta += check(uri, currentType, idioma);

        response.reset();
        response.setContentType("text/xml;charset=utf-8");
        response.setHeader("Content-Disposition", "inline; filename=ajaxCheckUri");
        response.setContentLength(txtrespuesta.getBytes().length);
        try {
            response.getOutputStream().write(txtrespuesta.getBytes());

        } catch (IOException e) {
            log.error("Error de escritura", e);
        }
        return null;
    }

    private String check(String uri, UriType type, String idioma) {

        String msg;
        try {
            switch (type) {
                case MIC_URI:
                    MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
                    Microsite microsite = micrositeDelegate.obtenerMicrositebyUri(uri);
                    msg = (microsite == null) ? OK : EXISTE;
                    break;

                case CID_URI:
                    ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
                    Contenido contenido = contenidoDelegate.obtenerContenidoDesdeUri(idioma, uri);
                    msg = (contenido.getTraducciones().isEmpty()) ? OK : EXISTE;
                    break;

                case EID_URI:
                    EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
                    Encuesta encuesta = encuestaDelegate.obtenerEncuestaDesdeUri(idioma, uri);
                    msg = (encuesta.getTraducciones().isEmpty()) ? OK : EXISTE;
                    break;

                case NID_URI:
                    NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
                    Noticia noticia = noticiaDelegate.obtenerNoticiaDesdeUri(idioma, uri);
                    msg = (noticia.getTraducciones().isEmpty()) ? OK : EXISTE;
                    break;

                case TPI_URI:
                    TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
                    Tipo tipo = tipoDelegate.obtenerTipoDesdeUri(idioma, uri);
                    msg = (tipo.getTraducciones().isEmpty()) ? OK : EXISTE;
                    break;

                default:
                    msg = ERROR;
                    break;
            }

        } catch (DelegateException e) {
            log.error("Error de acceso", e);
            msg = ERROR;
        }
        return msg;
    }

    public enum UriType {
        MIC_URI,
        CID_URI,
        EID_URI,
        NID_URI,
        TPI_URI
    }
}
