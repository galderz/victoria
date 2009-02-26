package org.victoria;

import org.jboss.cache.Cache;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * InterClusterBridgeTest.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class InterClusterBridgeTest extends MicrocontainerTest
{
   public InterClusterBridgeTest(String name)
   {
      super(name);
   }
   
   public void testOne()
   {
      Cache cacheA1 = (Cache)getBean("CacheClusterA1");
      // Cache cacheA2 = (Cache)getBean("CacheClusterA2");
      
      System.out.println(cacheA1);
      // System.out.println(cacheA2);
   }
}
