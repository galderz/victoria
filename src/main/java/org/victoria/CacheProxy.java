package org.victoria;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.cache.Cache;
import org.jboss.cache.notifications.annotation.ViewChanged;
import org.jboss.cache.notifications.event.ViewChangedEvent;
import org.jboss.logging.Logger;
import org.jgroups.Address;
import org.jgroups.View;

/**
 * Listener.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class CacheProxy
{
   private static final Logger log = Logger.getLogger(CacheProxy.class);
   
   private final BridgeConfiguration configuration;

   private final Cache cache;
   
   private CacheNotificationsListener listener;
   
   public CacheProxy(BridgeConfiguration configuration, Cache cache)
   {
      this.configuration = configuration;
      this.cache = cache;
   }
   
   public void create() throws Exception
   {
      listener = new CacheNotificationsListener(configuration);
      listener.create(this);
      cache.addCacheListener(listener);
   }
   
   public void start()
   {
      listener.setAddress(cache.getLocalAddress());
   }
   
   public void stop()
   {
      listener.stop();
   }
   
   public void destroy()
   {
      cache.removeCacheListener(listener);
      listener.destroy();
   }   
}