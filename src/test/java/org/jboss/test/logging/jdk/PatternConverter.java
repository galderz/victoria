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

import java.util.logging.LogRecord;

/**
 * <p>PatternConverter is an abtract class that provides the
 * formatting functionality that derived classes need.
 * <p/>
 * <p>Conversion specifiers in a conversion patterns are parsed to
 * individual PatternConverters. Each of which is responsible for
 * converting a logging event in a converter specific manner.
 *
 * @author <a href="mailto:cakalijp@Maritz.com">James P. Cakalic</a>
 * @author Ceki G&uuml;lc&uuml;
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1958 $
 */
public abstract class PatternConverter
{
   public PatternConverter next;
   int min = -1;
   int max = 0x7FFFFFFF;
   boolean leftAlign = false;

   protected PatternConverter()
   {
   }

   protected PatternConverter(FormattingInfo fi)
   {
      min = fi.min;
      max = fi.max;
      leftAlign = fi.leftAlign;
   }

   /**
    * Derived pattern converters must override this method in order to
    * convert conversion specifiers in the correct way.
    * 
    * @param event the event
    * @return the converted event
    */
   abstract
   protected String convert(LogRecord event);

   /**
    * A template method for formatting in a converter specific way.
    * 
    * @param sbuf the string buffer
    * @param e the log recored
    */
   public void format(StringBuffer sbuf, LogRecord e)
   {
      String s = convert(e);

      if (s == null)
      {
         if (0 < min)
            spacePad(sbuf, min);
         return;
      }

      int len = s.length();

      if (len > max)
         sbuf.append(s.substring(len - max));
      else if (len < min)
      {
         if (leftAlign)
         {
            sbuf.append(s);
            spacePad(sbuf, min - len);
         }
         else
         {
            spacePad(sbuf, min - len);
            sbuf.append(s);
         }
      }
      else
         sbuf.append(s);
   }

   static String[] SPACES = {" ", "  ", "    ", "        ", //1,2,4,8 spaces
      "                ", // 16 spaces
      "                                "}; // 32 spaces

   /**
    * Fast space padding method.
    * 
    * @param sbuf the string buffer
    * @param length the length
    */
   public void spacePad(StringBuffer sbuf, int length)
   {
      while (length >= 32)
      {
         sbuf.append(SPACES[5]);
         length -= 32;
      }

      for (int i = 4; i >= 0; i--)
      {
         if ((length & (1 << i)) != 0)
         {
            sbuf.append(SPACES[i]);
         }
      }
   }

}

