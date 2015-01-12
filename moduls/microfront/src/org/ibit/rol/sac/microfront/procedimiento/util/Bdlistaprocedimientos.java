package org.ibit.rol.sac.microfront.procedimiento.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.ibit.rol.sac.microfront.Microfront;
import org.ibit.rol.sac.microfront.base.Bdbase;
import org.ibit.rol.sac.microfront.base.bean.ErrorMicrosite;
import org.ibit.rol.sac.microfront.estadistica.util.StatManager;
import org.ibit.rol.sac.microfront.procedimiento.bean.Procedimiento2;
import org.ibit.rol.sac.micromodel.Estadistica;
import org.ibit.rol.sac.micromodel.MProcedimiento;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MProcedimientoDelegate;
import org.ibit.rol.sac.model.ProcedimientoLocal;
import org.ibit.rol.sac.model.TraduccionProcedimientoLocal;
import org.ibit.rol.sac.persistence.delegate.ProcedimientoDelegate;

/**
 * Clase Bdlistaprocedimientos. Manejador de la petición de listado de procedimientos.
 * 
 * Mete en una lista los procedimientos de la unidad administrativa del microsite.
 * De ese listado se quitarán los que se hayan seleccionado en el backoffice del microsite.
 * 
 * @author Indra
 */

public class Bdlistaprocedimientos extends Bdbase {

	protected static Log log = LogFactory.getLog(Bdlistaprocedimientos.class);
	
	private List<?> listaprocedimientos; 	//los que vienen de la ua
	private ArrayList<Procedimiento2> listaprocedimientosfinal = new ArrayList<Procedimiento2>();	//lista final de procedimientos
	private Hashtable<String, String> listamprocedimientos = new Hashtable<String, String>();
	private boolean error = false;
	private HttpServletRequest req;
	
	public Bdlistaprocedimientos(HttpServletRequest request, ActionForm form) throws Exception {
		super(request);
		if ((microsite!=null) && (existeServicio(Microfront.RPROCEDIMIENTO))) {
			req=request;
			crearlistado();
		} else {
			beanerror = new ErrorMicrosite("Error", "En estos momentos no es posible mostrar la página solicitada.");
			error=true;
		}
	}

	private void crearlistado() {
		try {

	        prepararlistado();
	        
			//si llegamos aqui.... todo ok, así que grabamos la estadistica
			if (idsite.longValue()==0) error=true;
			 else if ("no".equals(""+req.getSession().getAttribute("MVS_stat"))) log.info("Skip Estadistica, preview conten"); 
				 		else req.getSession().getServletContext().setAttribute("bufferStats", 
								StatManager.grabarestadistica(idsite,Microfront.RPROCEDIMIENTO, super.publico, 
										(List<Estadistica>) req.getSession().getServletContext().getAttribute("bufferStats")));     
		} catch (Exception e) {
			log.error(e.getMessage());
			beanerror = new ErrorMicrosite("Error", "Se ha producido un error desconocido en el listado de procedimientos.");
			error=true;
			listaprocedimientosfinal = null;
		}

	}
	

	/**
	 * Método privado que recoge el listado de procedimientos de la ua. Recoge también listado de mprocedimientos y finalmente
	 * prepara el listado que se mostrará al usuario.
	 *
	 */
	private void prepararlistado() throws Exception {
		
		//pasar el listado de mprocedimientos a hash
		MProcedimientoDelegate bdprocedimiento = DelegateUtil.getMProcedimientoDelegate();
		MProcedimiento mprocedimiento = bdprocedimiento.obtenerMProcedimientobyMic(super.idsite);
		
		String procs=mprocedimiento.getProcedimientos();
    	if (procs!=null) {
    		StringTokenizer st=new StringTokenizer(procs,Microfront.separatorwords);
    		int n=st.countTokens();
    		for (int i=0;i<n;i++) {
    			String idproc = st.nextToken(); 
    			listamprocedimientos.put(idproc,idproc);
    		}
    	}
		
		ProcedimientoDelegate proc_del = org.ibit.rol.sac.persistence.delegate.DelegateUtil.getProcedimientoDelegate();
		listaprocedimientos = proc_del.listarProcedimientosPublicosUA( new Long(super.microsite.getUnidadAdministrativa()) );		
    	
		Iterator<?> iter = listaprocedimientos.iterator();
		//recorrer los procedimientos
		while (iter.hasNext()) {
				ProcedimientoLocal procloc = (ProcedimientoLocal)iter.next();
				Procedimiento2 proc2 = new Procedimiento2();
				if (!listamprocedimientos.contains("" + procloc.getId())) {
					
					TraduccionProcedimientoLocal trapro=(TraduccionProcedimientoLocal)procloc.getTraduccion(super.idioma.toLowerCase());
					if ((trapro!=null) && (trapro.getNombre()!=null)) {
						proc2.setId(procloc.getId());
						proc2.setNombre( trapro.getNombre()  );
						proc2.setFechaPublicacion(procloc.getFechaPublicacion());
					
						listaprocedimientosfinal.add(proc2);
					}
				}
		}

	}
		
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public ArrayList<Procedimiento2> getListaprocedimientosfinal() {
		return listaprocedimientosfinal;
	}

	public void setListaprocedimientosfinal(ArrayList<Procedimiento2> listaprocedimientosfinal) {
		this.listaprocedimientosfinal = listaprocedimientosfinal;
	}

	/**
	 * Implementacion del método abstracto.
	 * Se le indica que estamos en el servicio de procedimientos.
	 */
	public String setServicio() {
		return Microfront.RPROCEDIMIENTO;
	}

}
