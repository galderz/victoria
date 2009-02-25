/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.test.security;

import java.lang.reflect.Constructor;
import java.io.FilePermission;
import java.security.AllPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

/**
 * A Security Policy Plugin.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 72125 $
 */
public abstract class PolicyPlugin extends Policy
{
   /** No permissions */
   private static PermissionCollection none;

   /** All file permissions */
   private static PermissionCollection fileRead;

   /** All permissions */
   private static PermissionCollection all;

   /**
    * Get the security plugin. This queries for the sytem property
    * org.jboss.test.security.PolicyPlugin to determine the PolicyPlugin
    * implementation class. If no such property exist the default
    * org.jboss.test.security.TestsPolicyPlugin implementation is used.
    * 
    * @see PolicyPlugin
    * @see TestsPolicyPlugin
    * 
    * @param clazz - the unit testcase class
    * @return the security policy plugin
    * @throws Exception for any error
    */
   public static PolicyPlugin getInstance(Class clazz)
      throws Exception
   {
      String policyClassName = System.getProperty("org.jboss.test.security.PolicyPlugin",
         "org.jboss.test.security.TestsPolicyPlugin");
      return getInstance(clazz, policyClassName);
   }

   /**
    * Get the security plugin. With the specified name.
    * The class must implement {@link PolicyPlugin} with a constructor
    * that takes the test class as a single parameter
    * 
    * @see PolicyPlugin
    * @see TestsPolicyPlugin
    * 
    * @param clazz - the unit testcase class
    * @param policyName - the policy name
    * @return the security policy plugin
    * @throws Exception for any error
    */
   public static PolicyPlugin getInstance(Class clazz, String policyName)
      throws Exception
   {
      Class policyClass  = Thread.currentThread().getContextClassLoader().loadClass(policyName);
      Class[] sig = {Class.class};
      Constructor ctor = policyClass.getConstructor(sig);
      Object[] args = {clazz};
      return (PolicyPlugin) ctor.newInstance(args);
   }

   /**
    * No-op implementation
    */
   public void refresh()
   {
   }

   /**
    * The empty Permissions none.
    * @return none class ivar
    */
   protected PermissionCollection noPermissions()
   {
      if (none == null)
         none = new Permissions();
      return none;
   }

   /**
    * Create a PermissionCollection with read for all files permission
    * @return the fileRead class ivar
    */
   protected PermissionCollection fileReadPermissions()
   {
      if (fileRead == null)
      {
         fileRead = new Permissions();
         fileRead.add(new FilePermission("<<ALL FILES>>", "read"));
      }
      return fileRead;
   }

   /**
    * A PermissionCollection with the special AllPermission that enables
    * all access.
    * 
    * @see AllPermission
    * @return the all class ivar
    */
   protected PermissionCollection allPermissions()
   {
      if (all == null)
      {
         all = new Permissions();
         all.add(new AllPermission());
      }
      return all;
   }
}
