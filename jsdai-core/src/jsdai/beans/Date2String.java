/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

package jsdai.beans;

import javax.swing.*;
import java.text.*;
import java.awt.*; 
import java.util.*;
import jsdai.lang.*;
import jsdai.dictionary.*;

public class Date2String {

	private static final String	dateAttrName = "date_component";
	private static final String	timeAttrName = "time_component";

	public static final String STEP = "yyyy-MM-dd'T'kk:mm:ss";
	public static final String USA  = "MM\\dd\\yyyy'T'kk:mm:ss";
	public static final String EURO = "dd\\MM\\yyyy'T'kk:mm:ss";
	
	public static DateFormat defaultFormat = new SimpleDateFormat(STEP);
	
	/**
	*	Method, that converts given date to STEP format. The
	*	same result can be achieved as with other convertDate method,
	*	but this one is a little bit faster.
	*/
	public static String convertDate(GregorianCalendar aCalendar) {
		synchronized(defaultFormat) {
			StringBuffer strBuf = new StringBuffer();
			FieldPosition fPosition = new FieldPosition(0);
			defaultFormat.format(aCalendar.getTime(), strBuf, fPosition);
			return strBuf.toString();
		}
	}

	/**
	*	A calendar with desired date set, is passed in
	*	for parsing.
	*
	*	@param pattern		Choose one from given three available patterns.
	*/
	public static String convertDate(GregorianCalendar aCalendar,
																		 String pattern) {
		DateFormat useFormat = null;
		if (pattern.equalsIgnoreCase(STEP))
			useFormat = defaultFormat;
		else
			useFormat = new SimpleDateFormat(pattern);
		synchronized(useFormat) {
			StringBuffer strBuf = new StringBuffer();
			FieldPosition fPosition = new FieldPosition(0);
			useFormat.format(aCalendar.getTime(), strBuf, fPosition);
			return strBuf.toString();
		}
	}
	
	/**
	*	A convenience method, which parses given date_time to Gregorian
	*	calendar, then converts it to string representation in STEP 
	*	format.
	*/
	public static String convertDate(EEntity aDateTime) throws SdaiException {
		GregorianCalendar parsedDate = parseDate(aDateTime);
		return convertDate(parsedDate);
	}
	

	/**
	*	A convenience method, which parses given date_time instance
	*	to either 'normal'/'readable' representation, or just
	*	puts the number of miliseconds as string of digits.
	*	Usefull, when transmitting date/time value to thin clients.
	*/
	public static String convertDate(EEntity aDateTime, boolean asLong) throws SdaiException {
		GregorianCalendar parsedDate = parseDate(aDateTime);
		if (!asLong) {
			return convertDate(parsedDate);
		}
		else {
			return parsedDate.getTime().getTime()+"";
		}
	}
	
	/**
	*	A method to read date_and_time instance and convert it into
	*	instance of GregorianCalendar.
	*/
	public static GregorianCalendar parseDate(EEntity aDateTime) throws SdaiException {
		// obtain time zone for gmt:
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		GregorianCalendar theInnerDateTime = new GregorianCalendar(gmt);
    	theInnerDateTime.set(Calendar.SECOND, 0);
    	
    	if (aDateTime == null)
    		return theInnerDateTime;
    		
    	EEntity theDateTime = aDateTime;
		EEntity theDate = null;
		EEntity theTime = null;

		// given instance must have two attributes: one for storing date
		// component, another - for storing time component.
		// We expect that the names of attributes will reflect this fact,
		// so if expectation is not fullfilled, we will simply issue
		// the warning.
		EAttribute dateAttr = null;
		EAttribute timeAttr = null;

		dateAttr = findAttribute(theDateTime, dateAttrName);		
		timeAttr = findAttribute(theDateTime, timeAttrName);
		
		// work over date component:
		if (dateAttr != null) {
			int status = theDateTime.testAttribute(dateAttr, null);
			if (status != 0) {
				theDate = (EEntity) theDateTime.get_object(dateAttr);
				parseDate(theDate, theInnerDateTime);
			}
		}
		else {
			try {
				// try to assume the entity itself represents a date
				// if this is not a case, nothing painfull will happen.
				parseDate(theDateTime, theInnerDateTime);
			}
			catch (SdaiException ex) {
				// exceptions are OK here, as we might have been
				// inserted instance of EDate into time parsing.
				// There is no other cheap method to test the type
				// of instance being served.
			}
		}
	
		// work over time component:
		if (timeAttr != null) {
			int status = theDateTime.testAttribute(timeAttr, null);
			if (status != 0) {
				theTime = (EEntity) theDateTime.get_object(timeAttr);
				parseTime(theTime, theInnerDateTime);
			}
		}
		else {
			try {
				// the same as for date_component
				parseTime(theDateTime, theInnerDateTime);
			}
			catch (SdaiException ex) {
				// exceptions are OK here, as we might have been
				// inserted instance of EDate into time parsing.
				// There is no other cheap method to test the type
				// of instance being served.
			}
		}
		
		return theInnerDateTime;
	}

	protected static void parseTime(EEntity theTime,
					GregorianCalendar theInnerDateTime)
		throws SdaiException
	{
		int status = 0;
		EAttribute hour = findAttribute(theTime, "hour_component");
		status = theTime.testAttribute(hour, null);
		if (status != 0) {
			int value = theTime.get_int(hour);
			theInnerDateTime.set(Calendar.HOUR_OF_DAY, value);
		}
		EAttribute minute = findAttribute(theTime, "minute_component");
		status = theTime.testAttribute(minute, null);
		if (status != 0) {
			int value = theTime.get_int(minute);
			theInnerDateTime.set(Calendar.MINUTE, value);
		}							
		EAttribute second = findAttribute(theTime, "second_component");
		status = theTime.testAttribute(second, null);
		if (status != 0) {
			int value = (int) theTime.get_double(second);
			theInnerDateTime.set(Calendar.SECOND, value);	
		}
		EAttribute zone = findAttribute(theTime, "zone");
		if (zone != null) {
			status = theTime.testAttribute(zone, null);
			if (status != 0) {
				EEntity offset = (EEntity) theTime.get_object(zone);
				int offsetTime = 0;
				EAttribute hourOffset = findAttribute(offset, "hour_offset");
				status = offset.testAttribute(hourOffset, null);
				if (status != 0) {
					offsetTime = offset.get_int(hourOffset);
					offsetTime = offsetTime * 60 * 60 * 1000;
				}
				EAttribute minuteOffset = findAttribute(offset, "minute_offset");
				status = offset.testAttribute(minuteOffset, null);
				if (status != 0) {
					offsetTime += offset.get_int(minuteOffset) * 60 * 1000;
				}
				EAttribute sense = findAttribute(offset, "sense");
				status = offset.testAttribute(sense, null);
				if (status != 0) {
					int senseEnumValue = offset.get_int(sense);
					if (senseEnumValue == 1)
						offsetTime *= -1;
				}
			}
		}					
	}
	
	protected static void parseDate(EEntity theDate,
					GregorianCalendar theInnerDateTime)
		throws SdaiException
	{
		int status = 0;
		EAttribute year = findAttribute(theDate, "year_component");
		if (year != null) {
			status = theDate.testAttribute(year, null);
			if (status != 0) {
				// it is expected type is integer.
				theInnerDateTime.set(Calendar.YEAR, theDate.get_int(year));
			}
		}
		// now test if we have month and day specified:
		EAttribute month = findAttribute(theDate, "month_component");
		if (month != null) {
			status = theDate.testAttribute(month, null);
			if (status != 0) {
				// it is expected type is integer:
				theInnerDateTime.set(Calendar.MONTH, theDate.get_int(month)-1);
			}
		}
		// for day it's not easy, because depending on the entity type
		// there are three different interpretations of the attribute with
		// the same name. So, first test if value is available, and then,
		// if value is available, find how to interpret it.
		EAttribute day = findAttribute(theDate, "day_component");
		if (day != null) {
			status = theDate.testAttribute(day, null);
			if (status != 0) {
				// it is expected type is integer:
				int dayValue = theDate.get_int(day);
				// now, if given instance was calendar_date, then set
				// value to DAY_OF_MONTH.
				if (isSubtypeOf("calendar_date", theDate)) 
						theInnerDateTime.set(Calendar.DAY_OF_MONTH, dayValue);
				// if given instance was ordinal_date, then set value to
				// DAY_OF_YEAR.
				else if (isSubtypeOf("ordinal_date", theDate))
					theInnerDateTime.set(Calendar.DAY_OF_YEAR, dayValue);
				// if given instance was Week_of_year_and_day_date, then
				// set value to DAY_OF_WEEK.
				else if (isSubtypeOf("week_of_year_and_day_date", theDate))
					theInnerDateTime.set(Calendar.DAY_OF_WEEK, dayValue);				
				else
					System.out.println("ERROR! Failed to recognized the "+
					"type of given instance, so day component will be not set.");
			}
		}		
		EAttribute week = findAttribute(theDate, "week_component");
		if (week != null) {
			status = theDate.testAttribute(week, null);
			if (status != 0) {
				// it is expected type is integer:
				int weekValue = theDate.get_int(week);
				theInnerDateTime.set(Calendar.WEEK_OF_YEAR, weekValue);
			}
		}	
	}

	/**
	*	A helper method, used to find some attribute.
	*
	*/
	public static EAttribute findAttribute(EEntity instance,
												 String attrName) throws SdaiException {
		EEntity_definition eDef = instance.getInstanceType();
		return findAttribute(eDef, attrName);
	}

	public static EAttribute findAttribute(EEntity_definition eDef,
												 String attrName) throws SdaiException {
		if (!eDef.testExplicit_attributes(null)) {
			return null;
		}
		AExplicit_attribute allAttr = eDef.getExplicit_attributes(null);
		SdaiIterator it = allAttr.createIterator();
		while (it.next()) {
			EExplicit_attribute test = allAttr.getCurrentMember(it);
			String name = test.getName(null);
			if (name.equalsIgnoreCase(attrName))
				return test;
		}
		// now, visit supertypes to see maybe this attribute is there.
		if (!eDef.testSupertypes(null))
			return null;
		else {
			AEntity_definition aSuper = eDef.getSupertypes(null);
			it = aSuper.createIterator();
			while (it.next()) {
				EEntity_definition superType = aSuper.getCurrentMember(it);
				EAttribute test = findAttribute(superType, attrName);
				if (test != null)
					return test;
			}
		}
		return null;
	}

	/**
	*	A helper method to determine, if given instance is equal or
	*	is a subtype of entity definition, specified through typeName.
	*/
	public static boolean isSubtypeOf(String typeName,
										 EEntity instance) throws SdaiException {
		EEntity_definition eDef = instance.getInstanceType();
		return isSubtypeOf(typeName, eDef);
	}
	
	/**
	*	A helper method. 
	*
	*/
	public static boolean isSubtypeOf(String typeName, 
								EEntity_definition eDef) throws SdaiException {
		String name = eDef.getName(null);
		if (name.equalsIgnoreCase(typeName))
			return true;
		else {
			if (!eDef.testSupertypes(null))
				return false;
			else {
				AEntity_definition aSuper = eDef.getSupertypes(null);
				SdaiIterator it = aSuper.createIterator();
				while (it.next()) {
					EEntity_definition superType = aSuper.getCurrentMember(it);
					boolean test = isSubtypeOf(typeName, superType);
					if (test)
						return true;
				}
				return false;
			}
		}
	}
	
	public static EEntity_definition findDefinition(SdaiModel metaOwner,
								String eDefinitionName) throws SdaiException {
		if (metaOwner.getMode() == SdaiModel.NO_ACCESS)
			metaOwner.startReadOnlyAccess();
			
		//AEntity allTypes = metaOwner.getInstances(CEntity_declaration$used_declaration.class);
		AEntity allTypes = metaOwner.getInstances(CEntity_definition.class);
    	SdaiIterator it = allTypes.createIterator();
    	while (it.next()) {
    		//EDeclaration decl = (EDeclaration) allTypes.getCurrentMemberEntity(it);
    		//EEntity definition = decl.getDefinition(null);
    		EEntity definition = allTypes.getCurrentMemberEntity(it);
    		if (definition instanceof EEntity_definition) {
    			EEntity_definition test = (EEntity_definition) definition;
    			if (test.getName(null).equals(eDefinitionName))
    				return test;
    		}
    	}
    	throw new SdaiException(SdaiException.VA_NVLD, "Failed to locate specified"+
    		" entity_definition "+eDefinitionName +" in SystemRepository model "+
    		metaOwner.getName());
	}

	public static void getDateTime(EEntity theDateTime,
						GregorianCalendar theInnerDateTime) throws SdaiException {
		EEntity theDate = null;
		EEntity theTime = null;
	
		EAttribute dateAttr = null;
		EAttribute timeAttr = null;

		dateAttr = findAttribute(theDateTime, dateAttrName);		
		timeAttr = findAttribute(theDateTime, timeAttrName);

		// work over date component:
		if (dateAttr != null) {
			int status = theDateTime.testAttribute(dateAttr, null);
			if (status == 0) {
				// the basic assumption behind this code is that date_and_time
				// and calendar_date can be created in the same SdaiModel AND
				// their meta definitions reside in the same SdaiModel in
				// SystemRepository.

				// attribute not set. By default, create a calendar_date:
				SdaiModel owner = theDateTime.findEntityInstanceSdaiModel();
				EEntity_definition dateTimeDef = theDateTime.getInstanceType();
				SdaiModel metaOwner = dateTimeDef.findEntityInstanceSdaiModel();
				EEntity_definition calendarDateDef = findDefinition(metaOwner, "calendar_date");
				EEntity calDate = owner.createEntityInstance(calendarDateDef);
				theDateTime.set(dateAttr, calDate, null);
			}
			theDate = (EEntity) theDateTime.get_object(dateAttr);
			EAttribute year = findAttribute(theDate, "year_component");
			if (year != null) {
				theDate.set(year, theInnerDateTime.get(Calendar.YEAR), null);
			}
			EAttribute month = findAttribute(theDate, "month_component");
			if (month != null) {
				theDate.set(month, theInnerDateTime.get(Calendar.MONTH) + 1, null);
			}
			EAttribute day = findAttribute(theDate, "day_component");
			if (day != null) {
				// find where to take the value of day component:
				if (isSubtypeOf("calendar_date", theDate)) 
					theDate.set(day, theInnerDateTime.get(Calendar.DAY_OF_MONTH), null);
				else if (isSubtypeOf("ordinal_date", theDate))
					theDate.set(day, theInnerDateTime.get(Calendar.DAY_OF_YEAR), null);
				else if (isSubtypeOf("week_of_year_and_day_date", theDate))
					theDate.set(day, theInnerDateTime.get(Calendar.DAY_OF_WEEK), null);				
				else
					System.out.println("ERROR! Failed to recognized the "+
					"type of given instance, so day component will be not read.");
			}
			EAttribute week = findAttribute(theDate, "week_component");
			if (week != null) {
				theDate.set(week, theInnerDateTime.get(Calendar.WEEK_OF_YEAR), null);
			}	
		}
		else
			System.out.println("ERROR! Failed to find attribute, representing "+
				"date!"+theDateTime);

		if (timeAttr != null) {
			int status = theDateTime.testAttribute(timeAttr, null);
			if (status == 0) {
				// the basic assumption behind this code is that date_and_time
				// and local_time can be created in the same SdaiModel AND
				// their meta definitions reside in the same SdaiModel in
				// SystemRepository.

				// attribute not set. By default, create a local_time:
				SdaiModel owner = theDateTime.findEntityInstanceSdaiModel();
				EEntity_definition dateTimeDef = theDateTime.getInstanceType();
				SdaiModel metaOwner = dateTimeDef.findEntityInstanceSdaiModel();
				EEntity_definition localTimeDef = findDefinition(metaOwner, "local_time");
				EEntity localTime = owner.createEntityInstance(localTimeDef);
				theDateTime.set(timeAttr, localTime, null);
				
				// now, set the utc for the time:
				EEntity_definition timeOffsetDef = findDefinition(metaOwner, "coordinated_universal_time_offset");
				EEntity offset = owner.createEntityInstance(timeOffsetDef);
				EAttribute tzAttr = findAttribute(localTime, "zone");
				localTime.set(tzAttr, offset, null);
				// specify that utc is 'equal' to gmt
				EAttribute senseAttr = findAttribute(offset, "sense");
				offset.set(senseAttr, 2, null);
				EAttribute hourOffset_cuto = findAttribute(offset, "hour_offset");
				offset.set(hourOffset_cuto, 0, null);
			}
			theTime = (EEntity) theDateTime.get_object(timeAttr);
			EAttribute hour = findAttribute(theTime, "hour_component");
			if (hour != null)
				theTime.set(hour, theInnerDateTime.get(Calendar.HOUR_OF_DAY), null);

			EAttribute minute = findAttribute(theTime, "minute_component");
			if (minute != null)
				theTime.set(minute, theInnerDateTime.get(Calendar.MINUTE), null);

			EAttribute second = findAttribute(theTime, "second_component");
			if (second != null)
				theTime.set(second, (double) theInnerDateTime.get(Calendar.SECOND), null);
		}
		else
			System.out.println("ERROR! Failed to find attribute, representing "+
				"time!"+theDateTime);
									
	}

}