package org.ibit.rol.sac.microback.action.edita;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.MProcedimientoActionForm;
import org.ibit.rol.sac.micromodel.MProcedimiento;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.MProcedimientoDelegate;

import org.ibit.rol.sac.persistence.delegate.ProcedimientoDelegate;

/**
 * Action que se utiliza para listar procedimientos administrativos y seleccionar los que no se desean
 * visualizar en los microsites <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/procedimientos" <BR> 
 *  name="MProcedimientoActionForm" <BR> 
 *  input="/menuAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/listaProcedimientos.jsp"
 *  
 *  @author Indra
 */
public class MProcedimientosEditaAction extends BaseAction{

	protected static Log log = LogFactory.getLog(MProcedimientosEditaAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MProcedimientoDelegate bdprocedimiento = DelegateUtil.getMProcedimientoDelegate();
		MProcedimiento mprocedimiento = null;
		MProcedimientoActionForm f = (MProcedimientoActionForm)form;
		
		/***************************************************************/
		/***************      GRAVAR               *********************/
		/***************************************************************/
		if(request.getParameter("Grabar")!=null) {
			
			mprocedimiento = new MProcedimiento();

			if (f.getId() != null) {  
            	mprocedimiento.setId(f.getId()); // es edicion
            }
			
			mprocedimiento.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
			
        	//recogemos los seleccionados
        	String procs="";
        	String[] procs_sel=(String[]) f.getProcedimientos();
	        if (procs_sel!=null) {
	        	for (int i=0;i<procs_sel.length;i++)
		        		procs+=procs_sel[i]+";";
		        if (procs.length()>0)
		        		procs=procs.substring(0,procs.length()-1);
	        }
        	mprocedimiento.setProcedimientos(procs);
			
			bdprocedimiento.grabarMProcedimiento(mprocedimiento);
			
			//log.info("Creado/Actualizado mprocedimiento " + mprocedimiento.getId());
	       	
	   		addMessage(request, "mensa.grabarmprocedimiento");
	       	addMessage(request, "mensa.volvermprocedimientos");
	       	return mapping.findForward("info");
			
		}
		
		/***************************************************************/
		/**********    VISUALIZAMOS LOS PROCEDIMIENTOS    **************/
		/***************************************************************/
		ProcedimientoDelegate proc_del = org.ibit.rol.sac.persistence.delegate.DelegateUtil.getProcedimientoDelegate();
		request.setAttribute("procedimientosUA", proc_del.listarProcedimientosPublicosUA( new Long( ((Microsite)request.getSession().getAttribute("MVS_microsite")).getUnidadAdministrativa()) ) );		
	
		mprocedimiento = bdprocedimiento.obtenerMProcedimientobyMic( ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId() );
		
		f.reset(mapping,request);
		
		if (mprocedimiento!=null) { 
			//si existe... editaremos
			
	    	String procs=mprocedimiento.getProcedimientos();
	    	String str[]=null;
	    	if (procs!=null) {
	    		StringTokenizer st=new StringTokenizer(procs,";");
	    		int n=st.countTokens();
	    		str= new String[n];
	    		for (int i=0;i<n;i++) {
	    			str[i]=st.nextToken();
	    		}
	    	}
	    	f.setProcedimientos(str);
	    	
	    	f.setIdmicrosite( ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId() );
	    	f.setId(mprocedimiento.getId());
		} else {
			f.setIdmicrosite( ((Microsite)request.getSession().getAttribute("MVS_microsite")).getId() );
		}
    	return mapping.findForward("detalle");
    	
	}
	
}
