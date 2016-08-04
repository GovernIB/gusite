package es.caib.gusite.microback.action;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.gusite.micromodel.EstadisticaGroup;
import es.caib.gusite.utilities.chart.DatosBean;
import es.caib.gusite.utilities.chart.SGNChartPastel;

/**
 * Action Base para visualizar un grafico.
 * 
 * Definición Struts:<BR>
 * action path="/grafico"
 * 
 *  @author - Indra
 * 
 */
public class GraficoAction extends Action {

	protected static Log log = LogFactory.getLog(GraficoAction.class);

    public final ActionForward execute(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
            throws Exception {

    		SGNChartPastel grafico = new SGNChartPastel(obtenervectordatos(request));
    		
            response.reset();
            response.setContentType("image/png");
            response.setHeader("Content-Disposition", "inline; filename=\"grafico\"");
            response.setContentLength(grafico.toByteArray().length);
            response.getOutputStream().write(grafico.toByteArray());

        return null;
    }
	
    private ArrayList<DatosBean> obtenervectordatos(HttpServletRequest request) {
    	ArrayList<?> lista = (ArrayList<?>)request.getSession().getAttribute("MVA_listaestadistica");
    	ArrayList<DatosBean> listadevuelta = new ArrayList<DatosBean>();
    	Iterator<?> iter = lista.iterator();
    	while (iter.hasNext()) {
    		EstadisticaGroup statg = (EstadisticaGroup)iter.next();
    		DatosBean datobean = new DatosBean();
    		datobean.setDato(statg.getNombreservicio());
    		datobean.setValor("" + statg.getAccesos());
    		listadevuelta.add(datobean);
    	}
    	return listadevuelta;
    	
    }
	
}
