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

package jsdai.lang; import jsdai.dictionary.*;

class FILE_NAME
{
	static final String DEFAULT_AUTHOR_MEMBER = "-";
	static final String DEFAULT_ORGANIZATION_MEMBER = "-";
	static final String DEFAULT_PREPROCESSOR_VERSION = "";
	static final String DEFAULT_ORIGINATING_SYSTEM = "";
	static final String DEFAULT_AUTHORIZATION = "";
	static final String DEFAULT_REPOSITORY_NAME = "REPO";

	String name;
	String time_stamp;
	A_string author;
	A_string organization;
	String preprocessor_version;
	String originating_system;
	String authorization;

	String repository_name;



	FILE_NAME(SdaiRepository repo) throws SdaiException {
		author = new A_string(SdaiSession.listTypeSpecial, repo);
		organization = new A_string(SdaiSession.listTypeSpecial, repo);
	}



	private void set_author(Value value, SdaiSession ss) throws SdaiException { 
		if (value.tag != PhFileReader.EMBEDDED_LIST) {
			if (value.tag == PhFileReader.STRING) {
				author.addByIndexPrivate(1, value.string);
				AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_APHE);
			} else {
				author.addByIndexPrivate(1, DEFAULT_AUTHOR_MEMBER);
				AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_ABHE);
			}
			return;
 		}
		int found = 0;
		boolean non_string = false;
		for (int i = 0; i < value.integer; i++) {
			Value value_in_list = value.nested_values[i];
			if (value_in_list.tag == PhFileReader.STRING) {
				found++;
				author.addByIndexPrivate(found, value_in_list.string);
			} else {
				non_string = true;
			}
		}
		if (non_string) {
			AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_ANHE);
		}
		if (found == 0) {
			author.addByIndexPrivate(1, DEFAULT_AUTHOR_MEMBER);
			AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_ALHE);
		}
	}


	private void set_organization(Value value, SdaiSession ss) throws SdaiException { 
		if (value.tag != PhFileReader.EMBEDDED_LIST) {
			if (value.tag == PhFileReader.STRING) {
				organization.addByIndexPrivate(1, value.string);
				AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_OPHE);
			} else {
				organization.addByIndexPrivate(1, DEFAULT_ORGANIZATION_MEMBER);
				AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_OBHE);
			}
			return;
		}
		int found = 0;
		boolean non_string = false;
		for (int i = 0; i < value.integer; i++) {
			Value value_in_list = value.nested_values[i];
			if (value_in_list.tag == PhFileReader.STRING) {
				found++;
				organization.addByIndexPrivate(found, value_in_list.string);
			} else {
				non_string = true;
			}
		}
		if (non_string) {
			AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_ONHE);
		}
		if (found == 0) {
			organization.addByIndexPrivate(1, DEFAULT_ORGANIZATION_MEMBER);
			AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_OLHE);
		}
	}


	private String checkName(String name_str) throws SdaiException {
		int i;
		int count = 0;
		boolean deleted = false;
		boolean changed = false;
		int ln = name_str.length();
		byte [] name = new byte[ln];
		for (i = 0; i < ln; i++) {
			name[i] = (byte)name_str.charAt(i);
		}
		for (i = 0; i < ln; i++) {
			byte s = name[i];
			if ('A' <= s && s <= 'Z' || 'a' <= s && s <= 'z') {
				if (deleted) {
					name[count] = name[i];
					count++;
				}
				continue;
			} 
			if (!('0' <= s && s <= '9' || s == PhFileReader.UNDERSCORE)) {
				name[i] = PhFileReader.UNDERSCORE;
				changed = true;
			}
			if (count > 0) {
				if (deleted) {
					name[count] = name[i];
					count++;
				}
			} else if (i == 0) {
				changed = true;
				deleted = true;
			}
		}
		if (deleted) {
			ln = count;
		}
		if (!changed && ln > 0) {
			return name_str;
		} else if (ln == 0) {
			return null;
		} else {
			return new String(name, 0, ln);
		}
	}


	int write(EntityValue inst) throws SdaiException {
		if (inst.count < 7) {
			return PhFileReader.TOO_LESS_VALUES;
		}
		if (inst.values[0].tag != PhFileReader.STRING) {
			return PhFileReader.INCORRECT_VALUE;
		}
		name = inst.values[0].string;
		String str = checkName(inst.values[0].string);
		if (str != null) {
			repository_name = str;
		} else {
			repository_name = DEFAULT_REPOSITORY_NAME;
		}

		if (inst.values[1].tag != PhFileReader.STRING) {
			return PhFileReader.INCORRECT_VALUE;
		}
		time_stamp = inst.values[1].string;
		if (time_stamp.length() > 0) {
			char [] ch = time_stamp.substring(0,1).toCharArray();
			if (time_stamp.length() > 10 && time_stamp.substring(10,11).equals(" ") && ch[0] > '0' && ch[0] <= '9') {
				time_stamp = time_stamp.substring(0, 10) + time_stamp.substring(11);
			}
		} else {
			long date = System.currentTimeMillis();
			time_stamp = inst.owning_session.cal.longToTimeStamp(date);
			AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_TSHE);
		}

		set_author(inst.values[2], inst.owning_session);

		set_organization(inst.values[3], inst.owning_session);

		if (inst.values[4].tag == PhFileReader.MISSING) {
			preprocessor_version = DEFAULT_PREPROCESSOR_VERSION;
			AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_PRHE);
		} else if (inst.values[4].tag != PhFileReader.STRING) {
			preprocessor_version = DEFAULT_PREPROCESSOR_VERSION;
			AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_PWHE);
		} else {
			preprocessor_version =inst.values[4].string;
		}

		if (inst.values[5].tag == PhFileReader.MISSING) {
			originating_system = DEFAULT_ORIGINATING_SYSTEM;
			AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_OSHE);
		} else if (inst.values[5].tag != PhFileReader.STRING) {
			originating_system = DEFAULT_ORIGINATING_SYSTEM;
			AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_OWHE);
		} else {
			originating_system = inst.values[5].string;
		}

		if (inst.values[6].tag == PhFileReader.MISSING) {
			authorization = DEFAULT_AUTHORIZATION;
			AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_AUHE);
		} else if (inst.values[6].tag != PhFileReader.STRING) {
			authorization = DEFAULT_AUTHORIZATION;
			AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_AWHE);
		} else {
			authorization = inst.values[6].string;
		}

		return 0;
	}


	void print() throws SdaiException {
		SdaiSession.println("*****Attributes of an instance of the entity " +
				"FILE_NAME*****");
		SdaiSession.println("");

		SdaiSession.println("   name");
		SdaiSession.println(name);

		SdaiSession.println("   time stamp");
		SdaiSession.println(time_stamp);

		SdaiSession.println("   author");
		SdaiIterator it = author.createIterator();
		while (it.next()) {
			SdaiSession.println(author.getCurrentMember(it));
		}

		SdaiSession.println("   organization");
		it = organization.createIterator();
		while (it.next()) {
			SdaiSession.println(organization.getCurrentMember(it));
		}

		SdaiSession.println("   preprocessor version");
		SdaiSession.println(preprocessor_version);

		SdaiSession.println("   originating system");
		SdaiSession.println(originating_system);

		SdaiSession.println("   authorization");
		SdaiSession.println(authorization);

		SdaiSession.println("");
	}

}

