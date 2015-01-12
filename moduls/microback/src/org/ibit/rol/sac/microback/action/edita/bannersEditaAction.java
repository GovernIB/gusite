package org.ibit.rol.sac.microback.action.edita;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.ibit.rol.sac.microback.action.BaseAction;
import org.ibit.rol.sac.microback.actionform.formulario.bannerForm;
import org.ibit.rol.sac.microback.utils.VOUtils;
import org.ibit.rol.sac.micromodel.Banner;
import org.ibit.rol.sac.micromodel.Microsite;
import org.ibit.rol.sac.micromodel.TraduccionBanner;
import org.ibit.rol.sac.micropersistence.delegate.BannerDelegate;
import org.ibit.rol.sac.micropersistence.delegate.DelegateUtil;
import org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate;

/**
 * Action que edita los banners de un microsite <BR>
 * <P>
 * 	Definición Struts:<BR>
 *  action path="/bannerEdita" <BR> 
 *  name="bannerForm" <BR> 
 *  input="/bannersAcc.do"  <BR>
 *	scope="session" <BR>
 *  unknown="false" <BR>
 *  forward name="detalle" path="/detalleBanner.jsp" <BR>
 *  
 *  @author Indra
 */
public class bannersEditaAction extends BaseAction 
{
    /**
     * This is the main action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @return 
     */
	
	protected static Log log = LogFactory.getLog(bannersEditaAction.class);
	
    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	BannerDelegate bdBanner = DelegateUtil.getBannerDelegate();
    	Banner ban=null;
    	bannerForm f = (bannerForm) form;
    	
  
    	if(request.getParameter("modifica")!=null || request.getParameter("anyade")!=null) {

        	if (f.get("id") == null) {  
        		ban = new Banner(); // Es Alta
            } else {  // Es modificacion
            	ban = bdBanner.obtenerBanner((Long)f.get("id"));
            	//************COMPROBACION DE IDES*************
            	if (ban.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
            }
        	
        ban.setIdmicrosite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId());
        ban.setFcaducidad(f.getFcaducidad());
        ban.setFpublicacion(f.getFpublicacion());
        ban.setVisible(""+f.get("visible"));

           List tradform = (List) f.get("traducciones");
           List langs = DelegateUtil.getIdiomaDelegate().listarLenguajes();
           for (int i = 0; i < langs.size(); i++) {
        	   TraduccionBanner trad = (TraduccionBanner)ban.getTraduccion(""+langs.get(i));
        	   TraduccionBanner bantrad= (TraduccionBanner)tradform.get(i);
        	   if (trad != null) {
        		   bantrad.setImagen(trad.getImagen());
        		   tradform.set(i, bantrad);
        	   }
           }
           VOUtils.populate(ban, f);  // form --> bean
           
   		   IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
           List lang = idiomaDelegate.listarLenguajes();
           FormFile[] aux =  (FormFile[]) f.get("ficheros");
           String[] auxnom =  (String[]) f.get("ficherosnom");
           Long[] auxid =  (Long[]) f.get("ficherosid");
           boolean[] auxbor = (boolean[]) f.get("ficherosbor");
           
           for (int i=0;i<aux.length;i++) {
               TraduccionBanner  traduccion = (TraduccionBanner) ban.getTraduccion(""+lang.get(i));
               if (traduccion!=null) {
            	   if (archivoValido(aux[i]))  traduccion.setImagen(populateArchivo(traduccion.getImagen(), aux[i], null, null));
            	   else if (auxbor[i])         traduccion.setImagen(null);

            	   if (traduccion.getImagen() != null) 
            		   if (auxnom[i].length()>0) 
            			   traduccion.getImagen().setNombre(auxnom[i]);
               }
               ban.setTraduccion(""+lang.get(i), traduccion);
           }

       	bdBanner.grabarBanner(ban);

       	//log.info("Creado/Actualizado " + ban.getId());
      
       	if(request.getParameter("anyade")!=null) 
       		addMessage(request, "mensa.nuevobanner");
       	if(request.getParameter("modifica")!=null)	
       		addMessage(request, "mensa.modifbanner");	
       	
   		addMessage(request, "mensa.editarbanner", "" + ban.getId().longValue());
       	addMessage(request, "mensa.listabanners");
       	
       	return mapping.findForward("info");
               
       }
        
        //********************************************************
        //********************** EDITAMOS ************************
        //********************************************************
        if (request.getParameter("id")!=null) {     
            Long id = new Long(""+request.getParameter("id"));
           	
                if (bdBanner.checkSite(((Microsite)request.getSession().getAttribute("MVS_microsite")).getId(),id)) {
                	addMessage(request, "info.seguridad");
                	return mapping.findForward("info");
                }

                ban = bdBanner.obtenerBanner(id);
                //************COMPROBACION DE IDES*************
            	if (ban.getIdmicrosite().longValue()!=((Microsite)request.getSession().getAttribute("MVS_microsite")).getId().longValue())
            	{
            		addMessage(request, "peticion.error");
                    return mapping.findForward("info");
            	}
            	//*********************************************
                bannerForm fdet=(bannerForm) form;
               
                fdet.setFcaducidad(ban.getFcaducidad());
                fdet.setFpublicacion(ban.getFpublicacion());
                fdet.set("visible",ban.getVisible());
                
                VOUtils.describe(fdet, ban);  // bean --> form

                IdiomaDelegate idiomaDelegate = DelegateUtil.getIdiomaDelegate();
                List lang = idiomaDelegate.listarLenguajes();
                String[] auxnom = new String[lang.size()];
                Long[] auxid =  new Long[lang.size()];
                for (int i=0;i<lang.size();i++) {
                    TraduccionBanner  traduccion = (TraduccionBanner) ban.getTraduccion(""+lang.get(i));
                    if (traduccion!=null)
                    if (traduccion.getImagen()!=null) {
                    	auxnom[i]=traduccion.getImagen().getNombre();
                    	auxid[i]=traduccion.getImagen().getId();
                    }
                }
                fdet.set("ficherosnom",auxnom);
                fdet.set("ficherosid",auxid);
                
            return mapping.findForward("detalle");

        }

        addMessage(request, "peticion.error");
        return mapping.findForward("info");
    }
}

