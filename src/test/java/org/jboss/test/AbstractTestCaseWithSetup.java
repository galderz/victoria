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
package org.jboss.test;

import java.security.AccessController;
import java.security.PrivilegedAction;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.logging.Logger;

/**
 * An extension of AbstractTestCase that adds AbstractTestDelegate and
 * AbstractTestSetup delegate notions. The AbstractTestSetup integrates
 * with the junit.extensions.TestSetup setUp/tearDown callbacks to
 * create an AbstractTestDelegate. When a testcase is run as a class with
 * all conforming unit test methods run, a single class wide
 * AbstractTestDelegate is created by either the fist call to the setUp
 * method, or the suite AbstractTestSetup wrapper created by suite(Class).
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 62649 $
 */
public class AbstractTestCaseWithSetup extends AbstractTestCase
{
   /**
    * Single run setup
    */
   private AbstractTestSetup setup;

   /**
    * Create a new test case
    *
    * @param name the test name
    */
   public AbstractTestCaseWithSetup(String name)
   {
      super(name);
   }

   /**
    * Get the jboss logger.
    */
   public Logger getLog()
   {
      return getDelegate().getLog();
   }

   /**
    * Enable trace logging for the given category name.
    *
    * @param name - the logging category to enable trace level logging for.
    */
   protected void enableTrace(String name)
   {
      getDelegate().enableTrace(name);
   }

   /**
    * Get the delegate
    *
    * @return the delegate
    */
   protected AbstractTestDelegate getDelegate()
   {
      return AbstractTestSetup.delegate;
   }

   /**
    * Create a AbstractTestSetup wrapper for this class/instance to initialize
    * the AbstractTestDelegate if the AbstractTestSetup.delegate singleton
    * has not been initialized.
    *
    * @throws Exception
    */
   protected void setUp() throws Exception
   {
      // This is a single test run
      if (AbstractTestSetup.delegate == null)
      {
         setup = new AbstractTestSetup(this.getClass(), this);
         setup.setUp();
      }
      super.setUp();
   }

   protected void tearDown() throws Exception
   {
      super.tearDown();
      if (setup != null)
         setup.tearDown();
   }

   /**
    * Bootstrap the test for the case of running all tests in clazz.
    * This creates an AbstractTestSetup wrapper around TestSuite(clazz) to
    * initialize the AbstractTestDelegate once for the suite.
    *
    * @param clazz the test class
    * @return the bootstrap wrapper test
    */
   public static Test suite(Class clazz)
   {
      TestSuite suite = new TestSuite(clazz);
      return new AbstractTestSetup(clazz, suite);
   }

   /**
    * Suspend security manager.
    * 
    * @return current security manager instance
    */
   public static SecurityManager suspendSecurity()
   {
      return AccessController.doPrivileged(new PrivilegedAction<SecurityManager>()
      {
         public SecurityManager run()
         {
            SecurityManager result = System.getSecurityManager();
            System.setSecurityManager(null);
            return result;
         }
      });
   }

   /**
    * Resume / set security manager.
    *
    * @param securityManager security manager to set
    */
   public static void resumeSecurity(SecurityManager securityManager)
   {
      System.setSecurityManager(securityManager);
   }

}
