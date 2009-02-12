package org.victoria;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.cache.Cache;
import org.jboss.cache.notifications.annotation.CacheListener;
import org.jboss.cache.notifications.annotation.NodeModified;
import org.jboss.cache.notifications.annotation.ViewChanged;
import org.jboss.cache.notifications.event.NodeEvent;
import org.jboss.cache.notifications.event.ViewChangedEvent;
import org.jboss.logging.Logger;
import org.jgroups.Address;
import org.jgroups.View;

/**
 * CacheNotificationListener.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
@CacheListener
public class CacheNotificationsListener
{
   private static final Logger log = Logger.getLogger(CacheNotificationsListener.class);
   
   private final AtomicBoolean active = new AtomicBoolean(false);
   
   private final BridgeConfiguration configuration;
   
   private BridgeReplicator replicator;
   
   private Address address;
   
   public CacheNotificationsListener(BridgeConfiguration configuration)
   {
      this.configuration = configuration;
   }
   
   public void create(CacheProxy proxy) throws Exception
   {
      replicator = new BridgeReplicator(configuration);
      replicator.create(proxy);
   }
   
   public void start() throws Exception
   {
      replicator.start();
   }

   public void stop()
   {
      // Note: Does not seem to get called at least when the node is brought down.
      log.debug("Stopped being the coordinator so stop the inter cluster channel.");
      replicator.stop();
   }
   
   public void destroy()
   {
      replicator.destroy();
   }
   
   public void setAddress(Address address)
   {
      this.address = address;
   }
   
   @NodeModified
   public void callback(NodeEvent e) throws Exception
   {
      if (active.get())
      {
         replicator.replicate(e);
      }
   }

   @ViewChanged
   public void viewChanged(ViewChangedEvent vce) throws Exception
   {
      View newView = vce.getNewView();

      // When node is coordinator, the replicator is active
      boolean amICoordinator = isCoordinator(newView, address);
      
      // Set activeness if there was a change in who's coordinator:
      // If active = true and tmp = false (is no longer coordinator), then we set active = false.
      // If active = false and tmp = true (is now coordinator), then we set active = true
      boolean changed = active.compareAndSet(!amICoordinator, amICoordinator);
      
      // Verify if the coordinator changed and whether I became coordinator or stopped being one. 
      toBeOrNotToBeCoordinator(changed);
   }
   
   protected void toBeOrNotToBeCoordinator(boolean changed) throws Exception
   {
      if (changed)
      {
         if (active.get())
         {
            start();
         }
         else
         {
            stop();
         }
      }      
   }
   
   protected boolean isCoordinator(View newView, Address localAddress)
   {
      if (newView != null && localAddress != null)
      {
         Vector<Address> mbrs = newView.getMembers();
         boolean isCoord = mbrs != null && mbrs.size() > 0 && localAddress.equals(mbrs.firstElement()); 
         log.trace("Am I coordinator? " + isCoord);
         return isCoord;
      }
      
      log.trace("I'm not coordinator.");
      return false;
   }   
}
