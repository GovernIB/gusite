package es.caib.gusite.utilities.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CacheUtilExternal
{
  private static CacheUtilExternal singleton;
  private Log log = LogFactory.getLog(CacheUtilExternal.class);

  protected CacheUtilExternal()
    throws Exception
  {
    this.log.info("Creando nueva instancia de CacheUtilExternal");
  }

  public static CacheUtilExternal getCurrentInstance()
    throws Exception
  {
    if (singleton == null)
      try
      {
        singleton = new CacheUtilExternal();
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

  public Object getObjetoCacheado(String paramString, Object paramObject)
    throws Exception
  {
    Cache localCache = getInstance(paramString);
    Element localElement = localCache.get((Serializable)paramObject);
    if (localElement != null)
      return localCache.get((Serializable)paramObject).getValue();
    return null;
  }

  public void eliminaObjectoCacheado(String paramString, Object paramObject)
    throws Exception
  {
    Cache localCache = getInstance(paramString);
    Element localElement = localCache.get((Serializable)paramObject);
    if (localElement != null)
      localCache.remove((Serializable)paramObject);
  }

  public long eliminaObjetosExpirados(String paramString)
    throws CacheException
  {
    Cache localCache = getInstance(paramString);
    long l = 0L;
    List localList = localCache.getKeys();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
      try
      {
        String str = (String)localIterator.next();
        Element localElement = localCache.get(str);
        if (localCache.isExpired(localElement))
        {
          localCache.remove(str);
          l += 1L;
        }
      }
      catch (Exception localException)
      {
      }
    return l;
  }

  public long eliminaObjetosExpirados4AllCaches()
    throws CacheException
  {
    long l = 0L;
    String[] arrayOfString = obtenerNombresCache();
    for (int i = 0; i < arrayOfString.length; i++)
      l += eliminaObjetosExpirados(arrayOfString[i]);
    return l;
  }

  public void eliminaObjetosTodos(String paramString)
    throws CacheException, IllegalStateException, IOException
  {
    Cache localCache = getInstance(paramString);
    localCache.removeAll();
  }

  public Cache obtenerCacheExterna(String paramString)
    throws CacheException
  {
    CacheManager localCacheManager = CacheManager.getInstance();
    Cache localCache = null;
    if (localCacheManager.cacheExists(paramString))
      localCache = localCacheManager.getCache(paramString);
    return localCache;
  }

  public String[] obtenerNombresCache()
    throws IllegalStateException, CacheException
  {
    return CacheManager.getInstance().getCacheNames();
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
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.cache.CacheUtilExternal
 * JD-Core Version:    0.6.2
 */