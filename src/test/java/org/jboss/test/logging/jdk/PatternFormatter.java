/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.logging.jdk;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.apache.log4j.spi.LoggingEvent;

/**
 * A log4j style pattern formatter.
 * 
 * @author <a href="mailto:cakalijp@Maritz.com">James P. Cakalic</a>
 * @author Ceki G&uuml;lc&uuml;
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1958 $
 */
public class PatternFormatter extends Formatter
{

   /**
    * Default pattern string for log output. Currently set to the
    * string <b>"%m%n"</b> which just prints the application supplied
    * message.
    */
   public final static String DEFAULT_CONVERSION_PATTERN = "%m%n";

   /**
    * A conversion pattern equivalent to the TTCCCLayout.
    * Current value is <b>%r [%t] %p %c %x - %m%n</b>.
    */
   public final static String TTCC_CONVERSION_PATTERN
      = "%r [%t] %p %c %x - %m%n";


   protected final int BUF_SIZE = 256;
   protected final int MAX_CAPACITY = 1024;

   private String pattern;

   private PatternConverter head;

   /**
    * Constructs a PatternLayout using the DEFAULT_LAYOUT_PATTERN.
    * <p/>
    * The default pattern just produces the application supplied message.
    */
   public PatternFormatter()
   {
      this(DEFAULT_CONVERSION_PATTERN);
   }

   /**
    * Constructs a PatternLayout using the supplied conversion pattern.
    * 
    * @param pattern the pattern
    */
   public PatternFormatter(String pattern)
   {
      this.pattern = pattern;
      head = createPatternParser((pattern == null) ? DEFAULT_CONVERSION_PATTERN :
         pattern).parse();
   }

   /**
    * Set the <b>ConversionPattern</b> option. This is the string which
    * controls formatting and consists of a mix of literal content and
    * conversion specifiers.
    * 
    * @param conversionPattern the conversion pattern
    */
   public void setConversionPattern(String conversionPattern)
   {
      pattern = conversionPattern;
      head = createPatternParser(conversionPattern).parse();
   }

   /**
    * Returns the value of the <b>ConversionPattern</b> option.
    * 
    * @return the conversion pattern
    */
   public String getConversionPattern()
   {
      return pattern;
   }

   /**
    * Does not do anything as options become effective
    */
   public void activateOptions()
   {
      // nothing to do.
   }

   /**
    * The PatternLayout does not handle the throwable contained within
    * {@link LoggingEvent LoggingEvents}. Thus, it returns
    * <code>true</code>.
    *
    * @since 0.8.4
    * 
    * @return true if the throwable is ignored
    */
   public boolean ignoresThrowable()
   {
      return true;
   }

   /**
    * Returns PatternParser used to parse the conversion string. Subclasses
    * may override this to return a subclass of PatternParser which recognize
    * custom conversion characters.
    *
    * @since 0.9.0
    * 
    * @param pattern the pattern
    * @return the parser
    */
   protected PatternParser createPatternParser(String pattern)
   {
      return new PatternParser(pattern);
   }


   /**
    * Produces a formatted string as specified by the conversion pattern.
    */
   public String format(LogRecord event)
   {
      // output buffer appended to when format() is invoked
      StringBuffer sbuf = new StringBuffer(BUF_SIZE);
      PatternConverter c = head;
      while (c != null)
      {
         c.format(sbuf, event);
         c = c.next;
      }
      return sbuf.toString();
   }
}
