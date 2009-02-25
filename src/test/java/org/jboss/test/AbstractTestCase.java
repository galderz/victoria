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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jboss.logging.Logger;
import org.jboss.util.UnexpectedThrowable;

/**
 * An abstract Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 71003 $
 */
public abstract class AbstractTestCase extends TestCase
{
   /** The static log */
   private static final Logger staticLog = Logger.getLogger(AbstractTestCase.class); 
   
   /** The start time */
   long startTime;
   
   /**
    * Create a new abstract test case
    *
    * @param name the test name
    */
   public AbstractTestCase(String name)
   {
      super(name);
   }

   /**
    * Get the log for this test
    * 
    * @return the log
    */
   public abstract Logger getLog();
   
   public URL getResource(final String name)
   {
      return findResource(getClass(), name);
   }
   
   public static URL findResource(final Class clazz, final String name)
   {
      PrivilegedAction<URL> action = new PrivilegedAction<URL>()
      {
         public URL run()
         {
            return clazz.getResource(name);
         }
      };
      return AccessController.doPrivileged(action);
   }

   protected void setUp() throws Exception
   {
      log("Starting");
      startTime = System.currentTimeMillis();
   }

   protected void tearDown() throws Exception
   {
      getLog().debug(getName() + " took " + (System.currentTimeMillis() - startTime) + "ms");
      log("Stopping");
   }

   /**
    * Callback for configuring logging at the start of the test
    */
   protected void configureLogging()
   {
   }

   /**
    * Enable trace for a logging category
    *
    * @param name the logging category
    */
   protected abstract void enableTrace(String name);

   /**
    * Assert two float values are equal
    *
    * @param one the expected value
    * @param two the actual value
    */
   protected void assertEquals(float one, float two)
   {
      assertEquals(one, two, 0f);
   }

   /**
    * Assert two double values are equal
    *
    * @param one the expected value
    * @param two the actual value
    */
   protected void assertEquals(double one, double two)
   {
      assertEquals(one, two, 0f);
   }

   /**
    * Assert two arrays are equal
    * 
    * @param expected the expected array
    * @param actual the actual array
    */
   protected void assertEquals(Object[] expected, Object[] actual)
   {
      if (Arrays.equals(expected, actual) == false)
      {
         String expectedString = null;
         if (expected != null)
            expectedString = Arrays.asList(expected).toString();
         String actualString = null;
         if (actual != null)
            actualString = Arrays.asList(actual).toString();
         throw new AssertionFailedError("expected: " + expectedString + " actual: " + actualString);
      }
   }

   /**
    * Assert two arrays are equal
    * 
    * @param context the context
    * @param expected the expected array
    * @param actual the actual array
    */
   protected void assertEquals(String context, Object[] expected, Object[] actual)
   {
      if (Arrays.equals(expected, actual) == false)
      {
         String expectedString = null;
         if (expected != null)
            expectedString = Arrays.asList(expected).toString();
         String actualString = null;
         if (actual != null)
            actualString = Arrays.asList(actual).toString();
         throw new AssertionFailedError(context + " expected: " + expectedString + " actual: " + actualString);
      }
   }

   /**
    * Asserts a collection is empty
    * 
    * @param c the collection
    */
   protected void assertEmpty(Collection c)
   {
      assertNotNull(c);
      if (c.isEmpty() == false)
         throw new AssertionFailedError("Expected empty collection " + c);
   }

   /**
    * Asserts a collection is empty
    *
    * @param context the context
    * @param c the collection
    */
   protected void assertEmpty(String context, Collection c)
   {
      assertNotNull(c);
      if (c.isEmpty() == false)
         throw new AssertionFailedError(context);
   }
   
   /**
    * Assert an array is empty or null
    * 
    * @param array the array
    */
   protected static void assertEmpty(Object[] array)
   {
      if (array != null)
         assertEquals(Arrays.asList(array).toString(), 0, array.length);
   }

   /**
    * Check we have the expected exception
    * 
    * @param expected the excepted class of the exception
    * @param throwable the real exception
    */
   public static void checkThrowable(Class<? extends Throwable> expected, Throwable throwable)
   {
      if (expected == null)
         fail("Must provide an expected class");
      if (throwable == null)
         fail("Must provide a throwable for comparison");
      if (throwable instanceof AssertionFailedError || throwable instanceof AssertionError)
         throw (Error) throwable;
      if (expected.equals(throwable.getClass()) == false)
      {
         staticLog.error("Unexpected throwable", throwable);
         fail("Unexpected throwable: " + throwable);
      }
      else
      {
         staticLog.debug("Got expected " + expected.getName() + "(" + throwable + ")");
      }
   }
   
   /**
    * Check a throwable and rethrow if it doesn't match
    * 
    * @param expected the expected throwable
    * @param throwable the throwable
    * @throws Exception the thrown exception
    */
   public static void checkThrowableRethrow(Class<? extends Throwable> expected, Throwable throwable) throws Exception
   {
      assertNotNull(expected);
      assertNotNull(throwable);
      
      if (expected.equals(throwable.getClass()) == false)
      {
         if (throwable instanceof Exception)
            throw (Exception) throwable;
         else if (throwable instanceof Error)
            throw (Error) throwable;
         else
            throw new UnexpectedThrowable("UnexpectedThrowable", throwable);
      }
      else
      {
         staticLog.debug("Got expected " + expected.getName() + "(" + throwable + ")");
      }
   }

   /**
    * Check we have the expected deep exception
    * 
    * @param expected the excepted class of the exception
    * @param throwable the real exception
    */
   public static void checkDeepThrowable(Class<? extends Throwable> expected, Throwable throwable)
   {
      assertNotNull(expected);
      assertNotNull(throwable);
      
      while (throwable.getCause() != null)
         throwable = throwable.getCause();
      
      if (throwable instanceof AssertionFailedError || throwable instanceof AssertionError)
         throw (Error) throwable;
      if (expected.equals(throwable.getClass()) == false)
      {
         staticLog.error("Unexpected throwable", throwable);
         fail("Unexpected throwable: " + throwable);
      }
      else
      {
         staticLog.debug("Got expected " + expected.getName() + "(" + throwable + ")");
      }
   }
   
   /**
    * Check a deep throwable and rethrow if it doesn't match
    * 
    * @param expected the expected throwable
    * @param throwable the throwable
    * @throws Exception the thrown exception
    */
   public static void checkDeepThrowableRethrow(Class<? extends Throwable> expected, Throwable throwable) throws Exception
   {
      assertNotNull(expected);
      assertNotNull(throwable);
      
      Throwable original = throwable;
      
      while (throwable.getCause() != null)
         throwable = throwable.getCause();
      
      if (expected.equals(throwable.getClass()) == false)
      {
         if (original instanceof Exception)
            throw (Exception) original;
         else if (original instanceof Error)
            throw (Error) original;
         else
            throw new UnexpectedThrowable("UnexpectedThrowable", original);
      }
      else
      {
         staticLog.debug("Got expected " + expected.getName() + "(" + throwable + ")");
      }
   }

   /**
    * Serialize an object
    * 
    * @param object the object
    * @return the bytes
    * @throws Exception for any error
    */
   protected byte[] serialize(Serializable object) throws Exception
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      oos.close();
      return baos.toByteArray();
   }

   /**
    * Serialize an object
    *
    * @param bytes - the raw serialzied object data 
    * @return the bytes
    * @throws Exception for any error
    */
   protected Object deserialize(byte[] bytes) throws Exception
   {
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      ObjectInputStream ois = new ObjectInputStream(bais);
      return ois.readObject();
   }

   /**
    * Serialize/deserialize
    * 
    * @param <T> the expected type
    * @param value the value
    * @param expected the expected type
    * @return the result
    * @throws Exception for any problem
    */
   protected <T> T serializeDeserialize(Serializable value, Class<T> expected) throws Exception
   {
      byte[] bytes = serialize(value);
      Object result = deserialize(bytes);
      return assertInstanceOf(result, expected);
   }

   /**
    * Check we have the expected type
    *
    * @param <T> the expected type
    * @param o the object
    * @param expectedType the excepted class of the exception
    * @return the expected type
    */
   protected <T> T assertInstanceOf(Object o, Class<T> expectedType)
   {
      return assertInstanceOf(o, expectedType, true);
   }

   /**
    * Check we have the expected type
    *
    * @param <T> the expected type
    * @param o the object
    * @param expectedType the excepted class of the exception
    * @param allowNull whether the object can be null
    * @return the expected type
    */
   protected <T> T assertInstanceOf(Object o, Class<T> expectedType, boolean allowNull)
   {
      if (expectedType == null)
         fail("Null expectedType");

      if (o == null)
      {
         if (allowNull == false)
            fail("Null object not allowed.");
         else
            return null;
      }

      try
      {
         return expectedType.cast(o);
      }
      catch (ClassCastException e)
      {
         fail("Object " + o + " of class " + o.getClass().getName() + " is not an instanceof " + expectedType.getName());
         // should not reach this
         return null;
      }
   }

   /**
    * Raise an assertion failed error for an error
    *
    * @param reason the reason
    * @param cause the cause
    */
   protected void failure(String reason, Throwable cause)
   {
      getLog().error(reason, cause);
      if (cause instanceof AssertionFailedError)
         throw (AssertionFailedError) cause;
      Error error = new AssertionFailedError(reason);
      error.initCause(cause);
      throw error;
   }

   /**
    * Log an event with the given context
    *
    * @param context the context
    */   
   private void log(String context)
   {
      getLog().debug("==== " + context + " " + getName() + " ====");
   }
}
