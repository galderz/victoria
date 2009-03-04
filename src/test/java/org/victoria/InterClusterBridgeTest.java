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
   private Cache cacheA1;
   private Cache cacheA2;
   private Cache cacheB1;
   private Cache cacheB2;
   private Cache cacheC1;
   private Cache cacheC2;
   
   public InterClusterBridgeTest(String name)
   {
      super(name);
   }
   
   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      
      cacheA1 = (Cache)getBean("CacheClusterA1");
      cacheA2 = (Cache)getBean("CacheClusterA2");
      cacheB1 = (Cache)getBean("CacheClusterB1");
      cacheB2 = (Cache)getBean("CacheClusterB2");
      cacheC1 = (Cache)getBean("CacheClusterC1");
      cacheC2 = (Cache)getBean("CacheClusterC2");
   }

   public void testOne()
   {      
      System.out.println("CacheA1 members: " + cacheA1.getMembers());
      System.out.println("CacheA1 contents: " + cacheA1);

      System.out.println("CacheA2 members: " + cacheA2.getMembers());
      System.out.println("CacheA2 contents: " + cacheA2);
      
      
      System.out.println("CacheB1 members: " + cacheB1.getMembers());
      System.out.println("CacheB1 contents: " + cacheB1);

      System.out.println("CacheB2 members: " + cacheB2.getMembers());
      System.out.println("CacheB2 contents: " + cacheB2);

      System.out.println("CacheB1 members: " + cacheC1.getMembers());
      System.out.println("CacheB1 contents: " + cacheC1);

      System.out.println("CacheB2 members: " + cacheC2.getMembers());
      System.out.println("CacheB2 contents: " + cacheC2);
   }
   
   
}
