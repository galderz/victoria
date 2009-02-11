package org.victoria;

import java.net.URL;

import org.jboss.cache.config.Configuration;

/**
 * Configuration.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class BridgeConfiguration extends Configuration
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 9061155253100029447L;
   
   private String clusterConfig;
   
   private URL jgroupsConfigFile;

   @Override
   public String getClusterConfig()
   {
      return clusterConfig;
   }

   public URL getJgroupsConfigFile()
   {
      return jgroupsConfigFile;
   }

   @Override
   public void setClusterConfig(String clusterConfig)
   {
      this.clusterConfig = clusterConfig;
   }

   @Override
   public void setJgroupsConfigFile(URL jgroupsConfigFile)
   {
      this.jgroupsConfigFile = jgroupsConfigFile;
   }
}
