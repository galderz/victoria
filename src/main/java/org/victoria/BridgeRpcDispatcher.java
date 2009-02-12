package org.victoria;

import org.jboss.cache.notifications.event.NodeEvent;
import org.jgroups.Channel;
import org.jgroups.blocks.RpcDispatcher;

/**
 * BridgeRpcDispatcher.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class BridgeRpcDispatcher extends RpcDispatcher
{
   private CacheProxy proxy;
   
   public BridgeRpcDispatcher(Channel channel, CacheProxy serverObj)
   {
      super(channel, null, new BridgeMembershipListener(), serverObj);
      this.proxy = serverObj;
   }

   public void replicate(NodeEvent e) throws Exception
   {
      
   }
}
