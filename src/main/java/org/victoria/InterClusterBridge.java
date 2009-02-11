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
   private BridgeConfiguration bridgeConfiguration;

   /** Cache (Injected Dependency) */
   private Cache cache;

   /** Cache proxy. */ 
   private CacheProxy cacheProxy;
   
   public BridgeConfiguration getBridgeConfiguration()
   {
      return bridgeConfiguration;
   }

   public void setBridgeConfiguration(BridgeConfiguration bridgeConfiguration)
   {
      this.bridgeConfiguration = bridgeConfiguration;
   }

   public Cache getCache()
   {
      return cache;
   }

   public void setCache(Cache cache)
   {
      this.cache = cache;
   }
   
   public void create()
   {
      cacheProxy = new CacheProxy(bridgeConfiguration, cache);
      cacheProxy.create();
   }
   
   public void start()
   {
      cacheProxy.start();
   }

   public void stop()
   {
      cacheProxy.stop();
   }
   
   public void destroy()
   {
      cacheProxy.destroy();
   }
}
