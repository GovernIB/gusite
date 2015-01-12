package es.caib.gusite.utilities.cache;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;

public class MemoryWarningSystem
{
  private final Collection<Listener> listeners = new ArrayList();
  private static final MemoryPoolMXBean tenuredGenPool = findTenuredGenPool();

  public MemoryWarningSystem()
  {
    MemoryMXBean localMemoryMXBean = ManagementFactory.getMemoryMXBean();
    NotificationEmitter localNotificationEmitter = (NotificationEmitter)localMemoryMXBean;
    localNotificationEmitter.addNotificationListener(new NotificationListener()
    {
      public void handleNotification(Notification paramAnonymousNotification, Object paramAnonymousObject)
      {
        if (paramAnonymousNotification.getType().equals("java.management.memory.threshold.exceeded"))
        {
          long l1 = MemoryWarningSystem.tenuredGenPool.getUsage().getMax();
          long l2 = MemoryWarningSystem.tenuredGenPool.getUsage().getUsed();
          Iterator localIterator = MemoryWarningSystem.this.listeners.iterator();
          while (localIterator.hasNext())
          {
            MemoryWarningSystem.Listener localListener = (MemoryWarningSystem.Listener)localIterator.next();
            localListener.memoryUsageLow(l2, l1);
          }
        }
      }
    }
    , null, null);
  }

  public boolean addListener(Listener paramListener)
  {
    return this.listeners.add(paramListener);
  }

  public boolean removeListener(Listener paramListener)
  {
    return this.listeners.remove(paramListener);
  }

  public static void setPercentageUsageThreshold(double paramDouble)
  {
    if ((paramDouble <= 0.0D) || (paramDouble > 1.0D))
      throw new IllegalArgumentException("Porcentaje incorrecto.");
    long l1 = tenuredGenPool.getUsage().getMax();
    long l2 = (long)(l1 * paramDouble);
    tenuredGenPool.setUsageThreshold(l2);
  }

  private static MemoryPoolMXBean findTenuredGenPool()
  {
    Iterator localIterator = ManagementFactory.getMemoryPoolMXBeans().iterator();
    while (localIterator.hasNext())
    {
      MemoryPoolMXBean localMemoryPoolMXBean = (MemoryPoolMXBean)localIterator.next();
      if ((localMemoryPoolMXBean.getType() == MemoryType.HEAP) && (localMemoryPoolMXBean.isUsageThresholdSupported()))
        return localMemoryPoolMXBean;
    }
    throw new AssertionError("No se puede encontrar la memoria `tenured spaceÂ´");
  }

  public static abstract interface Listener
  {
    public abstract void memoryUsageLow(long paramLong1, long paramLong2);
  }
}

/* Location:           C:\Temp\caib-utilities\caib-utilities.jar
 * Qualified Name:     es.caib.utilities.cache.MemoryWarningSystem
 * JD-Core Version:    0.6.2
 */