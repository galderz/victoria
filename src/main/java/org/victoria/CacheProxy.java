package org.victoria;

import org.jboss.cache.Cache;
import org.jboss.logging.Logger;

/**
 * Listener.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class CacheProxy
{
   private static final Logger log = Logger.getLogger(CacheProxy.class);
   
   private Cache cache;

   public Cache getCache()
   {
      return cache;
   }

   public void setCache(Cache cache)
   {
      this.cache = cache;
   }
   
}
