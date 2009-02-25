package org.jboss.cache.marshall;

import org.jboss.cache.factories.ComponentRegistry;

/**
 * BridgeVersionAwareMarshaller.
 * 
 * @author <a href="mailto:rnd zamarreno com">Galder Zamarreno</a>
 */
public class BridgeVersionAwareMarshaller extends VersionAwareMarshaller
{
   public BridgeVersionAwareMarshaller()
   {
      injectComponents(new ComponentRegistry(null, null));
   }

}
