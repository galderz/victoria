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

import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

/**
 * Formats a {@link Date} in the format "dd MMM YYYY HH:mm:ss,SSS" for example,
 * "06 Nov 1994 15:49:37,459".
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1958 $
 */
public class DateTimeDateFormat extends AbsoluteTimeDateFormat
{
   private static final long serialVersionUID = 1;

   String[] shortMonths;

   public DateTimeDateFormat()
   {
      super();
      shortMonths = new DateFormatSymbols().getShortMonths();
   }

   public DateTimeDateFormat(TimeZone timeZone)
   {
      this();
      setCalendar(Calendar.getInstance(timeZone));
   }

   /**
    * Appends to <code>sbuf</code> the date in the format "dd MMM YYYY
    * HH:mm:ss,SSS" for example, "06 Nov 1994 08:49:37,459".
    *
    * @param sbuf the string buffer to write to
    */
   public StringBuffer format(Date date, StringBuffer sbuf,
      FieldPosition fieldPosition)
   {

      calendar.setTime(date);

      int day = calendar.get(Calendar.DAY_OF_MONTH);
      if (day < 10)
         sbuf.append('0');
      sbuf.append(day);
      sbuf.append(' ');
      sbuf.append(shortMonths[calendar.get(Calendar.MONTH)]);
      sbuf.append(' ');

      int year = calendar.get(Calendar.YEAR);
      sbuf.append(year);
      sbuf.append(' ');

      return super.format(date, sbuf, fieldPosition);
   }

   /**
    * This method does not do anything but return <code>null</code>.
    */
   public Date parse(java.lang.String s, ParsePosition pos)
   {
      return null;
   }
}

