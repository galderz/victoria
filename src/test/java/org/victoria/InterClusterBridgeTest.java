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
//      Cache cacheA2 = (Cache)getBean("CacheClusterA2");
      
      System.out.println("CacheA1 members: " + cacheA1.getMembers());
      System.out.println("CacheA1 contents: " + cacheA1);

//      System.out.println("CacheA2 members: " + cacheA2.getMembers());
//      System.out.println("CacheA2 contents: " + cacheA2);
   }
}
