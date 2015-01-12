package es.caib.gusite.utilities.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheUtil
{
  private static double MEMORY_LIMIT = 0.75D;
  public static int STATUS_ACTIVO = 0;
  public static int STATUS_REMOVING = 1;
  public static int STATUS_INACTIVO = 2;
  private static long MINUTOS_TIMETOLIFE_DEFAULT = 10L;
  private static long MINUTOS_TIMETOIDLE_DEFAULT = 5L;
  private static long MINUTOS_DISKEXPIRYTHREAD_DEFAULT = 2L;
  private static int TAMANO_MAXOBJETOS_DEFAULT = -1;
  private static int TAMANO_DEFAULT = 400;
  private Map mapCachesMaxObjetos = new HashMap();
  public static final String CACHE_DEFAULT = "caibCacheDefault";
  private int _contadorInterno4GC = 0;
  private int _limiteContadorInicialInterno4GC = 700;
  private int _limiteContadorInterno4GC = 700;
  private int _status = STATUS_INACTIVO;
  private long _lastContadorGC = 0L;
  private Date _lastGC = null;
  private Object spoolLock = new Object();
  private static CacheUtil singleton;
  private Log log = LogFactory.getLog(CacheUtil.class);

  protected CacheUtil()
    throws Exception
  {
    crearListenerMemoria();
    this._contadorInterno4GC = 0;
    this._lastContadorGC = 0L;
    this._lastGC = null;
    this._status = STATUS_ACTIVO;
    this.log.info("Creando nueva instancia de CacheUtil " + hashCode());
  }

  public Cache crearCache()
    throws CacheException
  {
    return crearCache("caibCacheDefault", TAMANO_DEFAULT, TAMANO_MAXOBJETOS_DEFAULT, false, false, MINUTOS_TIMETOLIFE_DEFAULT * 60L, MINUTOS_TIMETOIDLE_DEFAULT * 60L, false, MINUTOS_DISKEXPIRYTHREAD_DEFAULT * 60L);
  }

  public Cache crearCache(String paramString, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, long paramLong1, long paramLong2, boolean paramBoolean3, long paramLong3)
    throws CacheException
  {
    Cache localCache = null;
    if ((paramInt1 != -1) && (paramInt1 < paramInt2))
      paramInt1 = paramInt2;
    CacheManager localCacheManager = CacheManager.getInstance();
    if (localCacheManager.cacheExists(paramString))
    {
      localCache = localCacheManager.getCache(paramString);
    }
    else
    {
      System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
      localCacheManager.addCache(new Cache(paramString, paramInt2, paramBoolean1, paramBoolean2, paramLong1, paramLong2, paramBoolean3, paramLong3));
      localCache = localCacheManager.getCache(paramString);
      this.mapCachesMaxObjetos.put(paramString, new Long(paramInt1));
      this._limiteContadorInterno4GC = (this._limiteContadorInicialInterno4GC * this.mapCachesMaxObjetos.size());
    }
    return localCache;
  }

  private void crearListenerMemoria()
  {
    MemoryWarningSystem.setPercentageUsageThreshold(MEMORY_LIMIT);
    MemoryWarningSystem localMemoryWarningSystem = new MemoryWarningSystem();
    localMemoryWarningSystem.addListener(new MemoryWarningSystem.Listener()
    {
      public void memoryUsageLow(long paramAnonymousLong1, long paramAnonymousLong2)
      {
        double d = paramAnonymousLong1 / paramAnonymousLong2;
        CacheUtil.this.log.warn("TRZ: Porcentaje memoria usado (" + d + ") excede del limite fijado (" + CacheUtil.MEMORY_LIMIT + ")");
        CacheUtil.this._status = CacheUtil.STATUS_INACTIVO;
      }
    });
  }

  public static CacheUtil getCurrentInstance()
    throws Exception
  {
    if (singleton == null)
      try
      {
        singleton = new CacheUtil();
      }
      catch (Exception localException)
      {
        throw localException;
      }
    return singleton;
  }

  public long estimaOcupacionDisco(String paramString)
    throws CacheException, IOException
  {
    long l = -1L;
    Cache localCache = getInstance(paramString);
    if ((localCache.getCacheConfiguration().isDiskPersistent()) || (localCache.getCacheConfiguration().isOverflowToDisk()))
    {
      String str1 = System.getProperty("java.io.tmpdir");
      int i = str1.lastIndexOf(File.separator) == str1.length() - 1 ? 1 : 0;
      String str2 = str1 + (i != 0 ? "" : File.separator) + paramString + ".data";
      File localFile = new File(str2);
      if (localFile.exists())
        l = localFile.length();
    }
    return l;
  }

  public void eliminaObjetosExpirados(String paramString)
    throws CacheException
  {
    Cache localCache = getInstance(paramString);
    int i = 0;
    List localList = localCache.getKeysWithExpiryCheck();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
      try
      {
        String str = (String)localIterator.next();
        Element localElement = localCache.get(str);
        if (localCache.isExpired(localElement))
        {
          localCache.remove(str);
          i++;
        }
      }
      catch (Exception localException)
      {
      }
    this.log.info("Elementos expirados y eliminados forzosamente: " + i);
  }

  public Object getObjeto(String paramString1, String paramString2)
    throws Exception
  {
    Cache localCache = getInstance(paramString1);
    Element localElement = localCache.get(paramString2);
    if (localElement != null)
      return localCache.get(paramString2).getValue();
    return null;
  }

  public void putObjeto(String paramString1, String paramString2, Object paramObject)
    throws Exception
  {
    Cache localCache = getInstance(paramString1);
    int i = ((Long)this.mapCachesMaxObjetos.get(paramString1)).intValue();
    if ((this._status == STATUS_ACTIVO) && ((i == -1) || (localCache.getSize() < i)))
      localCache.put(new Element(paramString2, (Serializable)paramObject));
  }

  public void removeAllRegions()
  {
    synchronized (this.spoolLock)
    {
      this._status = STATUS_REMOVING;
      Iterator localIterator = this.mapCachesMaxObjetos.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        try
        {
          vaciarRegion(str);
        }
        catch (Exception localException)
        {
          this.log.warn("No se ha podido eliminar todas las cachés");
        }
        this.log.info("Eliminada cache: " + str);
      }
      this._status = STATUS_ACTIVO;
    }
  }

  public Cache obtenerCache(String paramString)
    throws CacheException
  {
    Cache localCache = getInstance(paramString);
    return localCache;
  }

  public List obtenerKeysObjetos(String paramString)
    throws CacheException
  {
    Cache localCache = getInstance(paramString);
    return localCache.getKeys();
  }

  public String statsToString(String paramString)
    throws CacheException
  {
    Cache localCache = getInstance(paramString);
    StringBuilder localStringBuilder = new StringBuilder("");
    
    /* TODO amartin: adaptar
    int i = 0;
    if (localCache.getHitCount() > 0)
      i = localCache.getHitCount() * 100 / (localCache.getHitCount() + localCache.getMissCountExpired() + localCache.getMissCountNotFound());
    
    localStringBuilder.append("\nCacheUtil Status: ").append(this._status).append("\n");
    localStringBuilder.append("[").append(paramString).append("]\ngetSize: ").append(localCache.getSize());
    localStringBuilder.append("\nExito: ").append(i).append("%");
    localStringBuilder.append("\ncalculateInMemorySize: ").append(localCache.calculateInMemorySize());
    localStringBuilder.append("\ngetHitCount: ").append(localCache.getHitCount());
    localStringBuilder.append("\ngetMissCountExpired: ").append(localCache.getMissCountExpired());
    localStringBuilder.append("\ngetMissCountNotFound: ").append(localCache.getMissCountNotFound());
    localStringBuilder.append("\ngetDiskStoreSize: ").append(localCache.getDiskStoreSize());
    localStringBuilder.append("\ngetDiskStoreHitCount: ").append(localCache.getDiskStoreHitCount());
    localStringBuilder.append("\ngetMemoryStoreHitCount: ").append(localCache.getMemoryStoreHitCount());
    localStringBuilder.append("\ngetMemoryStoreSize: ").append(localCache.getMemoryStoreSize());
    localStringBuilder.append("\nisDiskPersistent: ").append(localCache.isDiskPersistent());
    */
    
    return localStringBuilder.toString();
  }

  public static void shutDown()
    throws CacheException
  {
    CacheManager.getInstance().shutdown();
  }

  public int getStatus()
  {
    return this._status;
  }

  public void setStatus(int paramInt)
  {
    if ((paramInt == STATUS_ACTIVO) || (paramInt == STATUS_REMOVING) || (paramInt == STATUS_INACTIVO))
      this._status = paramInt;
    else
      this._status = STATUS_ACTIVO;
  }

  public long getLastContadorGC()
  {
    return this._lastContadorGC;
  }

  public Date getLastGC()
  {
    return this._lastGC;
  }

  private Cache getInstance(String paramString)
    throws CacheException
  {
    Cache localCache = null;
    CacheManager localCacheManager = CacheManager.getInstance();
    if (localCacheManager.cacheExists(paramString))
      localCache = localCacheManager.getCache(paramString);
    return localCache;
  }

  private void vaciarRegion(String paramString)
    throws IllegalStateException, IOException, CacheException
  {
    synchronized (this.spoolLock)
    {
      CacheManager.getInstance().getCache(paramString).removeAll();
    }
  }

  /** @deprecated */
  private void stabilizeMemory4GC()
  {
    this._contadorInterno4GC += 1;
    if (this._contadorInterno4GC > this._limiteContadorInterno4GC)
    {
      System.gc();
      this._lastGC = new Date();
      this._lastContadorGC += 1L;
      this._contadorInterno4GC = 0;
      this._status = STATUS_ACTIVO;
    }
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.cache.CacheUtil
 * JD-Core Version:    0.6.2
 */