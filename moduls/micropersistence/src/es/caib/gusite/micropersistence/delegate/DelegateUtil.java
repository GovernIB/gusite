package es.caib.gusite.micropersistence.delegate;


/**
 * Define métodos estáticos para obtener delegates.
 * 
 * @author Indra
 */
public final class DelegateUtil {

	private DelegateUtil() {
	}

	public static ArchivoDelegate getArchivoDelegate() {
		return (ArchivoDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.ArchivoDelegate.class);
	}

	public static EstadisticaDelegate getEstadisticaDelegate() {
		return (EstadisticaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.EstadisticaDelegate.class);
	}

	public static EstadisticaGroupDelegate getEstadisticaGroupDelegate() {
		return (EstadisticaGroupDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.EstadisticaGroupDelegate.class);
	}

	public static ComponenteDelegate getComponentesDelegate() {
		return (ComponenteDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.ComponenteDelegate.class);
	}

	public static NoticiaDelegate getNoticiasDelegate() {
		return (NoticiaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.NoticiaDelegate.class);
	}

	public static IdiomaDelegate getIdiomaDelegate() {
		return (IdiomaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.IdiomaDelegate.class);
	}

	public static MenuDelegate getMenuDelegate() {
		return (MenuDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.MenuDelegate.class);
	}

	public static EncuestaDelegate getEncuestaDelegate() {
		return (EncuestaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.EncuestaDelegate.class);
	}

	public static TemaDelegate getTemafaqDelegate() {
		return (TemaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.TemaDelegate.class);
	}

	public static FaqDelegate getFaqDelegate() {
		return (FaqDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.FaqDelegate.class);
	}

	public static ActividadDelegate getActividadagendaDelegate() {
		return (ActividadDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.ActividadDelegate.class);
	}

	public static AgendaDelegate getAgendaDelegate() {
		return (AgendaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.AgendaDelegate.class);
	}

	public static ContactoDelegate getContactoDelegate() {
		return (ContactoDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.ContactoDelegate.class);
	}

	public static ContenidoDelegate getContenidoDelegate() {
		return (ContenidoDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.ContenidoDelegate.class);
	}

	public static MicrositeDelegate getMicrositeDelegate() {
		return (MicrositeDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.MicrositeDelegate.class);
	}

	public static TipoDelegate getTipoDelegate() {
		return (TipoDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.TipoDelegate.class);
	}

	public static FrqssiDelegate getFrqssiDelegate() {
		return (FrqssiDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.FrqssiDelegate.class);
	}

	public static TiposervicioDelegate getTiposervicioDelegate() {
		return (TiposervicioDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.TiposervicioDelegate.class);
	}

	public static RespuestaDatoDelegate getRespuestaDatoDelegate() {
		return (RespuestaDatoDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.RespuestaDatoDelegate.class);
	}

	public static UsuarioDelegate getUsuarioDelegate() {
		return (UsuarioDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.UsuarioDelegate.class);
	}

	public static UsuarioEncuestaDelegate getUsuarioEncuestaDelegate() {
		return (UsuarioEncuestaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.UsuarioEncuestaDelegate.class);
	}

	public static AccesibilidadDelegate getAccesibilidadDelegate() {
		return (AccesibilidadDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.AccesibilidadDelegate.class);
	}

	public static ConvocatoriaDelegate getConvocatoriaDelegate() {
		return (ConvocatoriaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.ConvocatoriaDelegate.class);
	}

	public static LDistribucionDelegate getLlistaDistribucionDelegate() {
		return (LDistribucionDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.LDistribucionDelegate.class);
	}

	public static AuditoriaDelegate getAuditoriaDelegate() {
		return (AuditoriaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.AuditoriaDelegate.class);
	}

	public static PlantillaDelegate getPlantillaDelegate() {
		return (PlantillaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.PlantillaDelegate.class);
	}

	public static TemaFrontDelegate getTemaFrontDelegate() {
		return (TemaFrontDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.TemaFrontDelegate.class);
	}

	public static ArchivoTemaFrontDelegate getArchivoTemaFrontDelegate() {
		return (ArchivoTemaFrontDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.ArchivoTemaFrontDelegate.class);
	}

	public static VersionDelegate getVersionDelegate() {
		return (VersionDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.VersionDelegate.class);
	}

	public static PersonalizacionPlantillaDelegate getPersonalizacionPlantillaDelegate() {
		return (PersonalizacionPlantillaDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.PersonalizacionPlantillaDelegate.class);
	}
	
	public static SolrDelegate getSolrDelegate() {
		return (SolrDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.SolrDelegate.class);
	}
	
	public static SolrPendienteDelegate getSolrPendienteDelegate() {
		return (SolrPendienteDelegate) DelegateFactory
				.getDelegate(es.caib.gusite.micropersistence.delegate.SolrPendienteDelegate.class);
	}

}