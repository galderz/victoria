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

import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;
import java.text.FieldPosition;
import java.text.ParsePosition;

// Contributors: Arndt Schoenewald <arndt@ibm23093i821.mc.schoenewald.de>

/**
 * Formats a {@link java.util.Date} in the format "YYYY-mm-dd HH:mm:ss,SSS" for example
 * "1999-11-27 15:49:37,459".
 * <p/>
 * <p>Refer to the <a
 * href=http://www.cl.cam.ac.uk/~mgk25/iso-time.html>summary of the
 * International Standard Date and Time Notation</a> for more
 * information on this format.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Andrew Vajoczki
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1958 $
 */
public class ISO8601DateFormat extends AbsoluteTimeDateFormat
{
   private static final long serialVersionUID = 1;

   public ISO8601DateFormat()
   {
   }

   public ISO8601DateFormat(TimeZone timeZone)
   {
      super(timeZone);
   }

   static private long lastTime;
   static private char[] lastTimeString = new char[20];

   /**
    * Appends a date in the format "YYYY-mm-dd HH:mm:ss,SSS"
    * to <code>sbuf</code>. For example: "1999-11-27 15:49:37,459".
    *
    * @param sbuf the <code>StringBuffer</code> to write to
    */
   public StringBuffer format(Date date, StringBuffer sbuf,
      FieldPosition fieldPosition)
   {

      long now = date.getTime();
      int millis = (int) (now % 1000);

      if ((now - millis) != lastTime)
      {
         // We reach this point at most once per second
         // across all threads instead of each time format()
         // is called. This saves considerable CPU time.

         calendar.setTime(date);

         int start = sbuf.length();

         int year = calendar.get(Calendar.YEAR);
         sbuf.append(year);

         String month;
         switch (calendar.get(Calendar.MONTH))
         {
            case Calendar.JANUARY:
               month = "-01-";
               break;
            case Calendar.FEBRUARY:
               month = "-02-";
               break;
            case Calendar.MARCH:
               month = "-03-";
               break;
            case Calendar.APRIL:
               month = "-04-";
               break;
            case Calendar.MAY:
               month = "-05-";
               break;
            case Calendar.JUNE:
               month = "-06-";
               break;
            case Calendar.JULY:
               month = "-07-";
               break;
            case Calendar.AUGUST:
               month = "-08-";
               break;
            case Calendar.SEPTEMBER:
               month = "-09-";
               break;
            case Calendar.OCTOBER:
               month = "-10-";
               break;
            case Calendar.NOVEMBER:
               month = "-11-";
               break;
            case Calendar.DECEMBER:
               month = "-12-";
               break;
            default:
               month = "-NA-";
               break;
         }
         sbuf.append(month);

         int day = calendar.get(Calendar.DAY_OF_MONTH);
         if (day < 10)
            sbuf.append('0');
         sbuf.append(day);

         sbuf.append(' ');

         int hour = calendar.get(Calendar.HOUR_OF_DAY);
         if (hour < 10)
         {
            sbuf.append('0');
         }
         sbuf.append(hour);
         sbuf.append(':');

         int mins = calendar.get(Calendar.MINUTE);
         if (mins < 10)
         {
            sbuf.append('0');
         }
         sbuf.append(mins);
         sbuf.append(':');

         int secs = calendar.get(Calendar.SECOND);
         if (secs < 10)
         {
            sbuf.append('0');
         }
         sbuf.append(secs);

         sbuf.append(',');

         // store the time string for next time to avoid recomputation
         sbuf.getChars(start, sbuf.length(), lastTimeString, 0);
         lastTime = now - millis;
      }
      else
      {
         sbuf.append(lastTimeString);
      }


      if (millis < 100)
         sbuf.append('0');
      if (millis < 10)
         sbuf.append('0');

      sbuf.append(millis);
      return sbuf;
   }

   /**
    * This method does not do anything but return <code>null</code>.
    */
   public Date parse(java.lang.String s, ParsePosition pos)
   {
      return null;
   }
}