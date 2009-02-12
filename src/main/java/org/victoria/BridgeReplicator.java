package org.victoria;

import java.net.URL;

import org.jboss.cache.marshall.BridgeVersionAwareMarshaller;
import org.jboss.cache.marshall.Marshaller;
import org.jboss.cache.notifications.event.NodeEvent;
import org.jboss.logging.Logger;
import org.jgroups.Channel;
import org.jgroups.JChannel;

/**
 * Replicator.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class BridgeReplicator
{
   private static final Logger log = Logger.getLogger(BridgeReplicator.class);
   
   private final BridgeConfiguration configuration;
   
   private Channel channel;
   
   private BridgeRpcDispatcher rpcDispatcher;
   
   public BridgeReplicator(BridgeConfiguration bridgeConfiguration)
   {
      this.configuration = bridgeConfiguration;
   }
   
   public void create(CacheProxy proxy) throws Exception
   {
      if (configuration.getJgroupsConfigFile() != null)
      {
         URL url = configuration.getJGroupsConfigFile();
         if (log.isTraceEnabled())
         {
            log.trace("Taking cluster configuration from " + url);
         }
         channel = new JChannel(url);
      }
      else
      {
         String clusterConfig = configuration.getClusterConfig();
         if (log.isTraceEnabled())
         {
            log.trace("Using directly defined cluster configuration: " + clusterConfig);
         }
         channel = new JChannel(clusterConfig); 
      }
      
      channel.setOpt(Channel.LOCAL, false);
      channel.setOpt(Channel.AUTO_RECONNECT, true);
      // Todo: Shall new inter cluster bridge nodes request the state? TBD.  
      channel.setOpt(Channel.AUTO_GETSTATE, false);
      channel.setOpt(Channel.BLOCK, true);
      
      rpcDispatcher = new BridgeRpcDispatcher(channel, proxy);
      Marshaller marshaller = new BridgeVersionAwareMarshaller();
      rpcDispatcher.setRequestMarshaller(marshaller);
      rpcDispatcher.setResponseMarshaller(marshaller);
   }

   public void start() throws Exception
   {
      channel.connect(configuration.getClusterName());
   }

   public void stop()
   {
      channel.disconnect();
   }
   
   public void destroy()
   {
      channel.close();
   }
   
   public void replicate(NodeEvent e) throws Exception
   {
      rpcDispatcher.replicate(e);
   }
}
