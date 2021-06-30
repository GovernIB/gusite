package es.caib.gusite.microback.action.buscaordena;

import es.caib.gusite.microback.actionform.busca.BuscaOrdenaAuditoriasActionForm;
import es.caib.gusite.micromodel.Auditoria;
import es.caib.gusite.micromodel.AuditoriaNombre;
import es.caib.gusite.micromodel.Idioma;
import es.caib.gusite.micromodel.Microsite;
import es.caib.gusite.micropersistence.delegate.AuditoriaDelegate;
import es.caib.gusite.micropersistence.delegate.DelegateUtil;
import es.caib.gusite.micropersistence.delegate.MicrositeDelegate;
import es.caib.gusite.plugins.PluginException;
import es.caib.gusite.plugins.PluginFactory;
import es.caib.gusite.plugins.organigrama.UnidadData;
import es.caib.gusite.plugins.organigrama.UnidadListData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Action que prepara el listado de Auditorias <BR>
 * <P>
 *   Definición Struts:<BR>
 *   action path="/auditorias" <BR>
 *   name="buscaOrdenaAuditoriasActionForm" <BR>
 *   forward name="listarAuditorias" path="/listaAuditorias.jsp"
 *
 * Created by tcerda on 16/12/2014.
 */
public class BuscaOrdenaAuditoriasAction extends Action {

    protected static Log log = LogFactory.getLog(BuscaOrdenaAuditoriasAction.class);

    private static Collection<UnidadListData> cacheListaUAs = null;
    private static Date fechaUltimaComprobacionCacheUas = null;
    private static final int MAX_MILISEGUNDOS_COMPROBACION_UAS = 300000; // 5 minutos

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        AuditoriaDelegate auditoriaDelegate = DelegateUtil.getAuditoriaDelegate();
        auditoriaDelegate.init();

        //Podemos recibir datos de filtro u ordenación del listado
        BuscaOrdenaAuditoriasActionForm f = (BuscaOrdenaAuditoriasActionForm) form;

        // Indicamos la página a visualizar
        if (request.getParameter("pagina") != null) {
            auditoriaDelegate.setPagina(Integer.parseInt(request.getParameter("pagina")));
        } else {
            auditoriaDelegate.setPagina(1);
        }

        Date date = (f.getDate() != null && f.getDate() != "") ? new SimpleDateFormat("dd/mm/yyyy").parse(f.getDate()) : null;
        List<Auditoria> lista = auditoriaDelegate.listarAuditorias(f.getEntity(), f.getIdEntity(), f.getUser(), date, f.getMicro());
        request.setAttribute("parametros_pagina", auditoriaDelegate.getParametros());

        List<AuditoriaNombre> listaVista = new ArrayList<AuditoriaNombre>(lista.size());
        for (Auditoria auditoria : lista) {
            listaVista.add(new AuditoriaNombre(auditoria));
        }

        if (lista.size() != 0) {
            request.setAttribute("listado", listaVista);
        } else {
            f.setFiltro("");
            f.setEntity("");
            f.setIdEntity("");
            f.setUser("");
            f.setDate("");
            f.setMicro("");
        }

        // Generamos el mapa de idUA => nombreUA (así sólo hacemos una llamda al WS).
        Collection<UnidadListData> listaUAs = obtenerListaUAsCacheada();
        Map<Serializable, String> mapaUAs = new HashMap<Serializable, String>();

        for (UnidadListData ua : listaUAs) {
            mapaUAs.put(ua.getId(), ua.getNombre());
        }

        MicrositeDelegate microde = DelegateUtil.getMicrositeDelegate();
        List listaresultante = microde.listarMicrositesThin();
        Iterator iter = listaresultante.iterator();

        while (iter.hasNext()) {
            Microsite mic = (Microsite) iter.next();
            String nombreUA = mapaUAs.get(new Long(mic.getUnidadAdministrativa()));
            mic.setNombreUA(nombreUA);
        }

        request.setAttribute("listatodos", listaresultante);

        return mapping.findForward("listarAuditorias");
    }

    private static Collection<UnidadListData> obtenerListaUAsCacheada() throws PluginException {




            cacheListaUAs = PluginFactory.getInstance().getOrganigramaProvider().getUnidades(Idioma.getIdiomaPorDefecto());



        return cacheListaUAs;
    }
}