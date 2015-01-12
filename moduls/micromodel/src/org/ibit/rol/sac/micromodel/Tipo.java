package org.ibit.rol.sac.micromodel;

/**
 * Clase Tipo. Bean que define un Tipo. 
 * Modela la tabla de BBDD GUS_TPNOTI.
 * @author Indra
 */
public class Tipo extends Traducible{
	
	private static final long serialVersionUID = -4636608744934761537L;
	public static String AUTENTICACION_EXPLICITA_CAIB = "C";
	public static String AUTENTICACION_NO = "N";
	public static String AUTENTICACION_STANDARD = "S";

	public static String EJB_LOCAL = "L";
	public static String EJB_REMOTO = "R";

	public static String TIPO_FICHA = "0";
	public static String TIPO_LINK = "1";
	public static String TIPO_DOCUMENTOS = "2";
	public static String TIPO_CONEXIO_EXTERNA = "3";
	public static String TIPO_FOTO = "4";

	private Long id;
    private Long idmicrosite;
    private int tampagina;
    private String tipopagina;
    private String tipoelemento;
    private String buscador;
    private String orden;
    private String clasificacion;
    
    //nuevos atributos para conectar el tipo
    private String xurl; 
    private String xjndi; 
    private String xlocalizacion; 
    private String xauth; 
    private String xusr; 
    private String xpwd; 
    private String xid; 
    
	private int fotosporfila;
    public Long getIdmicrosite() {
		return idmicrosite;
	}

	public void setIdmicrosite(Long idmicrosite) {
		this.idmicrosite = idmicrosite;
	}

	public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

	public void addTraduccionMap(String lang, TraduccionTipo traduccion) {
        setTraduccion(lang, traduccion);
    }

	public int getTampagina() {
		return tampagina;
	}

	public void setTampagina(int tampagina) {
		this.tampagina = tampagina;
	}

	public String getTipoelemento() {
		return tipoelemento;
	}

	public void setTipoelemento(String tipoelemento) {
		this.tipoelemento = tipoelemento;
	}

	public String getTipopagina() {
		return tipopagina;
	}

	public void setTipopagina(String tipopagina) {
		this.tipopagina = tipopagina;
	}

	public String getBuscador() {
		return buscador;
	}

	public void setBuscador(String buscador) {
		this.buscador = buscador;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public String getXurl() {
		return xurl;
	}

	public void setXurl(String xurl) {
		this.xurl = xurl;
	}

	public String getXjndi() {
		return xjndi;
	}

	public void setXjndi(String xjndi) {
		this.xjndi = xjndi;
	}

	public String getXlocalizacion() {
		return xlocalizacion;
	}

	public void setXlocalizacion(String xlocalizacion) {
		this.xlocalizacion = xlocalizacion;
	}

	public String getXauth() {
		return xauth;
	}

	public void setXauth(String xauth) {
		this.xauth = xauth;
	}

	public String getXusr() {
		return xusr;
	}

	public void setXusr(String xusr) {
		this.xusr = xusr;
	}

	public String getXpwd() {
		return xpwd;
	}

	public void setXpwd(String xpwd) {
		this.xpwd = xpwd;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	} 	

	public int getFotosporfila() {
		return fotosporfila;
	}

	public void setFotosporfila(int fotosporfila) {
		this.fotosporfila = fotosporfila;
	}
}