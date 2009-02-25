package org.victoria;

import java.io.NotSerializableException;
import java.util.Set;

import org.jboss.cache.commands.ReplicableCommand;
import org.jboss.cache.commands.remote.ReplicateCommand;
import org.jboss.cache.commands.write.PutDataMapCommand;
import org.jboss.cache.commands.write.PutKeyValueCommand;
import org.jboss.cache.commands.write.RemoveKeyCommand;
import org.jboss.cache.commands.write.RemoveNodeCommand;
import org.jboss.cache.notifications.event.NodeEvent;
import org.jboss.cache.notifications.event.NodeModifiedEvent;
import org.jgroups.Channel;
import org.jgroups.Message;
import org.jgroups.blocks.GroupRequest;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Buffer;
import org.jgroups.util.RspList;
import org.jboss.logging.Logger;

/**
 * BridgeRpcDispatcher.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class BridgeRpcDispatcher extends RpcDispatcher
{
   private static final Logger log = Logger.getLogger(BridgeRpcDispatcher.class);
   
   private CacheProxy proxy;
   
   private boolean trace;
   
   public BridgeRpcDispatcher(Channel channel, CacheProxy serverObj)
   {
      super(channel, null, new BridgeMembershipListener(), serverObj);
      this.proxy = serverObj;
      this.trace = log.isTraceEnabled();
   }

   public void replicate(NodeEvent e) throws Exception
   {
      Buffer buf;
      try
      {
         ReplicableCommand command = nodeEventToCommand(e);
         buf = req_marshaller.objectToBuffer(command);
      }
      catch (Exception ex)
      {
         log.error("Failure to marshal argument(s)", ex);
         throw new RuntimeException("Failure to marshal argument(s)", ex);
      }

      Message msg = new Message();
      msg.setBuffer(buf);

      RspList retval = castMessage(null, msg, GroupRequest.GET_NONE, 0, false);

      if (retval == null)
      {
         throw new NotSerializableException("RpcDispatcher returned a null.  This is most often caused by args for " + e + " not being serializable.");
      }
   }
   
   @Override
   public Object handle(Message req)
   {
      log.info("Message received " + req);
      
      if (req == null || req.getLength() == 0)
      {
         log.error("Message or message buffer is null");
         return null;
      }

      try
      {
         ReplicableCommand command = (ReplicableCommand) req_marshaller.objectFromByteBuffer(req.getBuffer(), req.getOffset(), req.getLength());
         Object execResult = executeCommand(command, req);
         if (trace) log.trace("Command : " + command + " executed, result is: " + execResult);
         return execResult;
      }
      catch (Throwable t)
      {
         if (trace) log.trace("Problems invoking command.", t);
         return t;
      }
   }
   
   protected Object executeCommand(ReplicableCommand cmd, Message req) throws Throwable
   {
      if (cmd == null)
      {
         throw new NullPointerException("Unable to execute a null command!  Message was " + req);
      }
      
      if (trace) log.trace("Executing command: " + cmd + " [sender=" + req.getSrc() + "]");
      
      return proxy.execute(cmd);
   }
   
   protected ReplicableCommand nodeEventToCommand(NodeEvent e)
   {
      if (e instanceof NodeModifiedEvent)
      {
         NodeModifiedEvent nme = (NodeModifiedEvent)e;
         ReplicableCommand command = null;
         switch(nme.getModificationType())
         {
            case PUT_DATA:
            case PUT_MAP:
               command = new PutDataMapCommand(null, nme.getFqn(), nme.getData());
               break;
            case REMOVE_DATA:
               Set keySet = nme.getData().keySet();
               if (keySet.size() > 1)
               {
                  throw new RuntimeException("Remove key from fqn with more than one key???" + e);
               }
               command = new RemoveKeyCommand(null, nme.getFqn(), keySet.iterator().next());
               break;
         }
         
         return new ReplicateCommand(command);
      }
      else
      {
         throw new RuntimeException("Not implemented!!");
      }
   }
}
