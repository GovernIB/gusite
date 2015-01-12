package org.ibit.rol.sac.micropersistence.delegate;

/**
 * Define métodos estáticos para obtener delegates.
 * @author Indra
 */
public final class DelegateUtil {

    private DelegateUtil() {
    }

    public static ArchivoDelegate getArchivoDelegate() {
        return (ArchivoDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.ArchivoDelegate.class);
    }
    
    public static EstadisticaDelegate getEstadisticaDelegate() {
        return (EstadisticaDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.EstadisticaDelegate.class);
    }

    public static EstadisticaGroupDelegate getEstadisticaGroupDelegate() {
        return (EstadisticaGroupDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.EstadisticaGroupDelegate.class);
    }   
    
    public static ComponenteDelegate getComponentesDelegate() {
        return (ComponenteDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.ComponenteDelegate.class);
    }
    
    public static NoticiaDelegate getNoticiasDelegate() {
        return (NoticiaDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.NoticiaDelegate.class);
    }

    public static IdiomaDelegate getIdiomaDelegate() {
        return (IdiomaDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.IdiomaDelegate.class);
    }

    public static MenuDelegate getMenuDelegate() {
        return (MenuDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.MenuDelegate.class);
    }
    
    public static BannerDelegate getBannerDelegate() {
        return (BannerDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.BannerDelegate.class);
    }

    public static EncuestaDelegate getEncuestaDelegate() {
        return (EncuestaDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.EncuestaDelegate.class);
    }
    
    public static TemaDelegate getTemafaqDelegate() {
        return (TemaDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.TemaDelegate.class);
    }    

    public static FaqDelegate getFaqDelegate() {
        return (FaqDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.FaqDelegate.class);
    }  
    
    public static ActividadDelegate getActividadagendaDelegate() {
        return (ActividadDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.ActividadDelegate.class);
    }  

    public static AgendaDelegate getAgendaDelegate() {
        return (AgendaDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.AgendaDelegate.class);
    }

    public static ContactoDelegate getContactoDelegate() {
        return (ContactoDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.ContactoDelegate.class);
    }  

    public static ContenidoDelegate getContenidoDelegate() {
        return (ContenidoDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.ContenidoDelegate.class);
    }  
        
    public static MicrositeDelegate getMicrositeDelegate() {
        return (MicrositeDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.MicrositeDelegate.class);
    }
    
    public static TipoDelegate getTipoDelegate() {
        return (TipoDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.TipoDelegate.class);
    }         
    
    public static FrqssiDelegate getFrqssiDelegate() {
        return (FrqssiDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.FrqssiDelegate.class);
    }         
    
    public static TiposervicioDelegate getTiposervicioDelegate() {
        return (TiposervicioDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.TiposervicioDelegate.class);
    }         

    public static MProcedimientoDelegate getMProcedimientoDelegate() {
        return (MProcedimientoDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.MProcedimientoDelegate.class);
    } 
  
    public static RespuestaDatoDelegate getRespuestaDatoDelegate() {
        return (RespuestaDatoDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.RespuestaDatoDelegate.class);
    } 
    
    public static IndexerDelegate getIndexerDelegate() {
        return (IndexerDelegate) DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.IndexerDelegate.class);
    }    
    
    public static UsuarioDelegate getUsuarioDelegate() {
        return (UsuarioDelegate) DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.UsuarioDelegate.class);
    }  
    public static UsuarioEncuestaDelegate getUsuarioEncuestaDelegate() {
        return (UsuarioEncuestaDelegate) DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.UsuarioEncuestaDelegate.class);
    } 
    public static AccesibilidadDelegate getAccesibilidadDelegate() {
        return (AccesibilidadDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.AccesibilidadDelegate.class);
    }
    public static ConvocatoriaDelegate getConvocatoriaDelegate() {
        return (ConvocatoriaDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.ConvocatoriaDelegate.class);
    }
    public static LDistribucionDelegate getLlistaDistribucionDelegate() {
        return (LDistribucionDelegate)DelegateFactory.getDelegate(org.ibit.rol.sac.micropersistence.delegate.LDistribucionDelegate.class);
    }
}