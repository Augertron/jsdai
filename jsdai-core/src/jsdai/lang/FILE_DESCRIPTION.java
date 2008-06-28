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

class FILE_DESCRIPTION {
	A_string description;
	String implementation_level;

	static final String DEFAULT_DESCRIPTION_MEMBER = "";
	static final String DEFAULT_IMPLEMENTATION_LEVEL = "2;1";

	FILE_DESCRIPTION(SdaiRepository repo) throws SdaiException {
		description = new A_string(SdaiSession.listTypeSpecial, repo);
	}



	private void set_description(Value value, SdaiSession ss) throws SdaiException { 
		if (value.tag != PhFileReader.EMBEDDED_LIST) {
			if (value.tag == PhFileReader.STRING) {
				description.addByIndexPrivate(1, value.string);
				AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_DWHE);
			} else {
				description.addByIndexPrivate(1, DEFAULT_DESCRIPTION_MEMBER);
				AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_DBHE);
			}
			return;
		}
		int found = 0;
		boolean non_string = false;
		for (int i = 0; i < value.integer; i++) {
			Value value_in_list = value.nested_values[i];
			if (value_in_list.tag == PhFileReader.STRING) {
				found++;
				description.addByIndexPrivate(found, value_in_list.string);
			} else {
				non_string = true;
			}
		}
		if (non_string) {
			AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_DNHE);
		}
		if (found == 0) {
			description.addByIndexPrivate(1, DEFAULT_DESCRIPTION_MEMBER);
			AdditionalMessages.printWarningToLogo(ss, AdditionalMessages.RD_DLHE);
		}
	}


	int write(EntityValue inst) throws SdaiException {
		int count = inst.count < 0 ? 0 : inst.count;
		int res = 0;
		switch (count) {
			case 0:
				write0(inst);
				break;
			case 1:
				if (inst.values[0].tag == PhFileReader.EMBEDDED_LIST) {
					set_description(inst.values[0], inst.owning_session);
				} else if (inst.values[0].tag == PhFileReader.STRING) {
					description.addByIndexPrivate(1, inst.values[0].string);
					AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_DWHE);
				} else {
					description.addByIndexPrivate(1, DEFAULT_DESCRIPTION_MEMBER);
					AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_DBHE);
				}
				implementation_level = DEFAULT_IMPLEMENTATION_LEVEL;
				AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_ILHE);
				break;
			case 2:
				set_description(inst.values[0], inst.owning_session);
				if (inst.values[1].tag == PhFileReader.STRING) {
					implementation_level = inst.values[1].string;
				} else {
					implementation_level = DEFAULT_IMPLEMENTATION_LEVEL;
					AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_IWHE);
				}
				break;
			default:
				int i;
				int list_ind = -1;
				int str_count = 0;
				String str1 = null, str2 = null, last_str = null;
				for (i = 0; i < count; i++) {
					if (inst.values[i].tag == PhFileReader.EMBEDDED_LIST) {
						list_ind = i;
					} else if (inst.values[i].tag == PhFileReader.STRING) {
						if (str_count == 0) {
							str1 = inst.values[i].string;
						} else if (str_count == 1) {
							str2 = inst.values[i].string;
						}
						last_str = inst.values[i].string;
						str_count++;
					}
				}
				if (list_ind >= 0) {
					set_description(inst.values[list_ind], inst.owning_session);
					if (last_str != null) {
						implementation_level = last_str;
					} else {
						implementation_level = DEFAULT_IMPLEMENTATION_LEVEL;
						AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_IWHE);
					}
					break;
				}
				if (str_count < count) {
					AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_DNHE);
				}
				switch (str_count) {
					case 0:
						write0(inst);
						break;
					case 1:
						description.addByIndexPrivate(1, str1);
						AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_DWHE);
						implementation_level = DEFAULT_IMPLEMENTATION_LEVEL;
						AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_IWHE);
						break;
					case 2:
						description.addByIndexPrivate(1, str1);
						AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_DWHE);
						implementation_level = str2;
						break;
					default:
						int k = 0;
						AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_DWHE);
						for (i = 0; i < count; i++) {
							if (inst.values[i].tag == PhFileReader.STRING) {
								k++;
								description.addByIndexPrivate(k, inst.values[i].string);
								if (k == str_count - 1) {
									break;
								}
							}
						}
						implementation_level = last_str;
				}
		}
		return 0;
	}


	void write0(EntityValue inst) throws SdaiException {
		description.addByIndexPrivate(1, DEFAULT_DESCRIPTION_MEMBER);
		AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_DEHE);
		implementation_level = DEFAULT_IMPLEMENTATION_LEVEL;
		AdditionalMessages.printWarningToLogo(inst.owning_session, AdditionalMessages.RD_ILHE);
	}


	void print() throws SdaiException
	{
		SdaiSession.println("*****Attributes of an instance of the entity " +
				"FILE_DESCRIPTION*****");
		SdaiSession.println("");
		SdaiSession.println("   description" );
		SdaiIterator it = description.createIterator();
		while (it.next()) {
			SdaiSession.println(description.getCurrentMember(it));
		}
		SdaiSession.println("   implementation_level");
		SdaiSession.println(implementation_level);
		SdaiSession.println("");
	}



}

