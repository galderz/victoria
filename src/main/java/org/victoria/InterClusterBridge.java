package org.victoria;

import org.jboss.cache.Cache;

/**
 * InterClusterBridge.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class InterClusterBridge
{
   /** Configuration (Injected Dependency) */
   private BridgeConfiguration configuration;

   /** Cache (Injected Dependency) */
   private Cache cache;

   /** Cache proxy. */ 
   private CacheProxy proxy;
   
   public BridgeConfiguration getConfiguration()
   {
      return configuration;
   }

   public void setconfiguration(BridgeConfiguration configuration)
   {
      this.configuration = configuration;
   }

   public Cache getCache()
   {
      return cache;
   }

   public void setCache(Cache cache)
   {
      this.cache = cache;
   }
   
   public void create() throws Exception
   {
      proxy = new CacheProxy(configuration, cache);
      proxy.create();
   }
   
   public void start()
   {
      proxy.start();
   }

   public void stop()
   {
      proxy.stop();
   }
   
   public void destroy()
   {
      proxy.destroy();
   }
}
