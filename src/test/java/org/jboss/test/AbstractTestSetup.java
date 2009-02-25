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

import junit.extensions.TestSetup;
import junit.framework.Test;

/**
 * An AbstractTestSetup.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 62657 $
 */
public class AbstractTestSetup extends TestSetup
{
   /** The test class */
   protected Class clazz;
   
   /** The last delegate */
   protected static AbstractTestDelegate delegate;
   
   /**
    * Create a new TestSetup.
    * 
    * @param clazz the test class
    * @param test the test wrapper
    */
   public AbstractTestSetup(Class clazz, Test test)
   {
      super(test);
      this.clazz = clazz;
   }

   /**
    * Create a delegate by calling AbstractTestDelegate.getDelegate(clazz)
    * to allow for a test specific delegate.
    * This method then delegates to the AbstractTestDelegate.setUp method.
    * @throws Exception
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      delegate = AbstractTestDelegate.getDelegate(clazz);
      delegate.setUp();
   }

   /**
    * This method then delegates to the AbstractTestDelegate.tearDown method.
    * @throws Exception
    */
   protected void tearDown() throws Exception
   {
      if (delegate != null)
         delegate.tearDown();
      delegate = null;
   }
}
