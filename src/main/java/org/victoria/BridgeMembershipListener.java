package org.victoria;

import org.jboss.logging.Logger;
import org.jgroups.Address;
import org.jgroups.MembershipListener;
import org.jgroups.View;

/**
 * BridgeMembershipListener.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class BridgeMembershipListener implements MembershipListener
{
   private static final Logger log = Logger.getLogger(BridgeMembershipListener.class);

   public void block()
   {
   }

   public void suspect(Address suspectedMbr)
   {
      log.debug("Member suspected : " + suspectedMbr);
   }

   public void viewAccepted(View newView)
   {
      log.debug("New inter cluster view: " + newView);

   }
}
