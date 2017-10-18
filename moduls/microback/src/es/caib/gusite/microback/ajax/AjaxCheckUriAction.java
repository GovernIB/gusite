package es.caib.gusite.microback.ajax;

import es.caib.gusite.microback.actionform.formulario.formularioconForm;
import es.caib.gusite.microback.utils.Cadenas;
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

    private static final String OPEN = "<datos><dato>";
    private static final String CLOSE = "</dato></datos>";
    private static final String ERROR = "error";
    private static final String BUSCAR_NUEVA = "buscar";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String txtrespuesta="<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        String uri = Cadenas.string2uri(request.getParameter("URI"));
        String type = request.getParameter("type");
        String idioma = request.getParameter("idioma");
        String site = request.getParameter("site");
        Long id = (long)0;
        if (request.getParameter("id") != null && !request.getParameter("id").equals("")) {
        	id = Long.parseLong(request.getParameter("id"));
        }
        UriType currentType = UriType.valueOf(type.toUpperCase());

        txtrespuesta += OPEN;
        txtrespuesta += check(uri, currentType, site, idioma, id, 0);
        txtrespuesta += CLOSE;

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

    /**
     * Método que valida la uri de un tipo. 
     * @param uri
     * @param type
     * @param site
     * @param idioma
     * @param id
     * @param count
     * @return
     */
    public String check(String uri, UriType type, String site, String idioma, Long id, Integer count) {

        String msg = uri;
        try {
            switch (type) {
                case MIC_URI:
                    MicrositeDelegate micrositeDelegate = DelegateUtil.getMicrositeDelegate();
                    Microsite microsite = micrositeDelegate.obtenerMicrositebyUri(uri);
                    if (microsite != null && !microsite.getId().equals(id)) {
                        msg = BUSCAR_NUEVA;
                    }
                    break;

                case CID_URI:
                    ContenidoDelegate contenidoDelegate = DelegateUtil.getContenidoDelegate();
                    Contenido contenido = contenidoDelegate.obtenerContenidoDesdeUri(idioma, uri, site);
                    if (contenido != null && !contenido.getId().equals(id)) {
                        msg = BUSCAR_NUEVA;
                    }
                    break;

                case EID_URI:
                    EncuestaDelegate encuestaDelegate = DelegateUtil.getEncuestaDelegate();
                    Encuesta encuesta = encuestaDelegate.obtenerEncuestaDesdeUri(idioma, uri, site);
                    if (encuesta != null && !encuesta.getId().equals(id)) {
                        msg = BUSCAR_NUEVA;
                    }
                    break;

                case NID_URI:
                    NoticiaDelegate noticiaDelegate = DelegateUtil.getNoticiasDelegate();
                    Noticia noticia = noticiaDelegate.obtenerNoticiaDesdeUri(idioma, uri, site);
                    if (noticia != null && !noticia.getId().equals(id)) {
                        msg = BUSCAR_NUEVA;
                    }
                    break;

                case TPI_URI:
                    TipoDelegate tipoDelegate = DelegateUtil.getTipoDelegate();
                    Tipo tipo = tipoDelegate.obtenerTipoDesdeUri(idioma, uri, site);
                    if (tipo != null && !tipo.getId().equals(id)) {
                        msg = BUSCAR_NUEVA;
                    }
                    break;

                case FTR_URI:
                    TemaFrontDelegate tfDelegate = DelegateUtil.getTemaFrontDelegate();
                    TemaFront temaFront = tfDelegate.obtenerTemabyUri(uri);
                    if (temaFront != null && !temaFront.getId().equals(id)) {
                        msg = BUSCAR_NUEVA;
                    }
                    break;
                    
                case FCO_URI:
                    ContactoDelegate fcDelegate = DelegateUtil.getContactoDelegate();
                    Contacto contacto = fcDelegate.obtenerContactoByUri(idioma, uri, site);
                    if (contacto != null && !contacto.getId().equals(id)) {
                        msg = BUSCAR_NUEVA;
                    }
                    break;    
                    
                default:
                    msg = ERROR;
                    break;
            }
            if (msg.equals(BUSCAR_NUEVA)) {
                msg = check(uri + "_" + count.toString(), type, site, idioma, id, count++);
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
        TPI_URI,
        FTR_URI,
        FCO_URI
    }
}
