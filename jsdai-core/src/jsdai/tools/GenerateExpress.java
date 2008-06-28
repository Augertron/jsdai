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

package jsdai.tools;

import jsdai.dictionary.*;
import java.io.*;
import jsdai.lang.*;

public class GenerateExpress {

	PrintWriter pw;

	public GenerateExpress() {
		pw = new PrintWriter(System.out, true);
	}

	public void printBound(EBound bound) throws SdaiException {
		if (bound == null) {
			pw.print("?");
		} else if (bound instanceof EInteger_bound) {
			pw.print(((EInteger_bound)bound).getBound_value(null));
		} else {
			pw.print("??");
		}
	}

	public void printType(EEntity type) throws SdaiException {
		if (type instanceof EAggregation_type) {
			EAggregation_type at = (EAggregation_type) type;
			boolean fUnique = false;
			boolean fOptional = false;
			EBound bound1 = null;
			EBound bound2 = null;
			if (type instanceof EVariable_size_aggregation_type) {
				EVariable_size_aggregation_type vt = (EVariable_size_aggregation_type) type;
				bound1 = vt.getLower_bound(null);
				if (vt.testUpper_bound(null)) {
					bound2 = vt.getUpper_bound(null);
				}				
				if (type instanceof ESet_type) {
					pw.print("SET [");
				} else if (type instanceof EBag_type) {
					pw.print("BAG [");
				} else if (type instanceof EList_type) {
					EList_type lt = (EList_type) type;
					fUnique = lt.getUnique_flag(null);
					pw.print("LIST [");
				} else {
					pw.println("???? [");
				}
			} else if (type instanceof EArray_type) {
				EArray_type rt = (EArray_type) type;
				fUnique = rt.getUnique_flag(null);
				fOptional = rt.getOptional_flag(null);
				bound1 = rt.getLower_index(null);
				if (rt.testUpper_index(null)) {
					bound2 = rt.getUpper_index(null);
				}
				pw.print("ARRAY [");
			} else {
				pw.println("???? [");
			}
			printBound(bound1);
			pw.print(":");
			printBound(bound2);
			pw.print("]");
			if (fOptional) {
				pw.print(" OPTIONAL ");
			}
			if (fUnique) {
				pw.print(" UNIQUE ");
			}
			pw.print(" OF ");

			EEntity domain = at.getElement_type(null);
			/*
			switch (at.testElement_type()) {
			case EAggregation_type.sElement_typeSet_type:
				domain = at.getElement_type((ESet_type)null);
				break;
			case EAggregation_type.sElement_typeBag_type:
				domain = at.getElement_type((EBag_type)null);
				break;
			case EAggregation_type.sElement_typeList_type:
				domain = at.getElement_type((EList_type)null);
				break;
			case EAggregation_type.sElement_typeArray_type:
				domain = at.getElement_type((EArray_type)null);
				break;
			case EAggregation_type.sElement_typeNumber_type:
				domain = at.getElement_type((ENumber_type)null);
				break;
			case EAggregation_type.sElement_typeInteger_type:
				domain = at.getElement_type((EInteger_type)null);
				break;
			case EAggregation_type.sElement_typeReal_type:
				domain = at.getElement_type((EReal_type)null);
				break;
			case EAggregation_type.sElement_typeBoolean_type:
				domain = at.getElement_type((EBoolean_type)null);
				break;
			case EAggregation_type.sElement_typeLogical_type:
				domain = at.getElement_type((ELogical_type)null);
				break;
			case EAggregation_type.sElement_typeBinary_type:
				domain = at.getElement_type((EBinary_type)null);
				break;
			case EAggregation_type.sElement_typeString_type:
				domain = at.getElement_type((EString_type)null);
				break;
			case EAggregation_type.sElement_typeDefined_type:
				domain = at.getElement_type((EDefined_type)null);
				break;
			case EAggregation_type.sElement_typeEntity_definition:
				domain = at.getElement_type((EEntity_definition)null);
				break;
			default:
				domain = null;
			}
			*/
			printType(domain);
		} else if (type instanceof ENamed_type) {
			pw.print(((ENamed_type)type).getName(null));
		} else if (type instanceof ENumber_type) {
			pw.print("NUMBER");
		} else if (type instanceof EInteger_type) {
			pw.print("INTEGER");
		} else if (type instanceof EReal_type) {
			pw.print("REAL");
		} else if (type instanceof EBoolean_type) {
			pw.print("BOOLEAN");
		} else if (type instanceof ELogical_type) {
			pw.print("LOGICAL");
		} else if (type instanceof EBinary_type) {
			pw.print("BINARY");
		} else if (type instanceof EString_type) {
			pw.print("STRING");
		} else if (type instanceof ESelect_type) {
			pw.print("SELECT (");
			ANamed_type ant = ((ESelect_type) type).getSelections(null);
			SdaiIterator it = ant.createIterator();
			if (it.next()) {
				pw.println();
				boolean fGoOn = true;
				while (fGoOn) {
					ENamed_type nt = ant.getCurrentMember(it);
					pw.print("\t" + nt.getName(null));
					if (it.next()) {
						pw.println(",");
					} else {
						pw.print(" )");
						fGoOn = false;
					}
				}
			} else {
				pw.print(" )");
			}
		} else if (type instanceof EEnumeration_type) {
			pw.print("ENUMERATION (");
			A_string ant = ((EEnumeration_type) type).getElements(null);
			int iDim =  ant.getMemberCount();
			if (iDim >= 1) {
				pw.println();
				int i = 1;
				while (i <= iDim) {
					pw.print("\t" + ant.getByIndex(i));
					if (i < iDim) {
						pw.println(",");
					} else {
						pw.print(" )");
					}
					i++;
				}
			} else {
				pw.print(" )");
			}
		} else {
			pw.print("???");
		}
	}

	public void printType(EDefined_type type) throws SdaiException {
		pw.print("TYPE " + type.getName(null) + " = ");
		EEntity domain = type.getDomain(null);
		/*
		switch (type.testDomain()) {
		case EDefined_type.sDomainSet_type:
			domain = type.getDomain((ESet_type)null);
			break;
		case EDefined_type.sDomainBag_type:
			domain = type.getDomain((EBag_type)null);
			break;
		case EDefined_type.sDomainList_type:
			domain = type.getDomain((EList_type)null);
			break;
		case EDefined_type.sDomainArray_type:
			domain = type.getDomain((EArray_type)null);
			break;
		case EDefined_type.sDomainNumber_type:
			domain = type.getDomain((ENumber_type)null);
			break;
		case EDefined_type.sDomainInteger_type:
			domain = type.getDomain((EInteger_type)null);
			break;
		case EDefined_type.sDomainReal_type:
			domain = type.getDomain((EReal_type)null);
			break;
		case EDefined_type.sDomainBoolean_type:
			domain = type.getDomain((EBoolean_type)null);
			break;
		case EDefined_type.sDomainLogical_type:
			domain = type.getDomain((ELogical_type)null);
			break;
		case EDefined_type.sDomainBinary_type:
			domain = type.getDomain((EBinary_type)null);
			break;
		case EDefined_type.sDomainString_type:
			domain = type.getDomain((EString_type)null);
			break;
		case EDefined_type.sDomainDefined_type:
			domain = type.getDomain((EDefined_type)null);
			break;
		case EDefined_type.sDomainEnumeration_type:
			domain = type.getDomain((EEnumeration_type)null);
			break;
		case EDefined_type.sDomainSelect_type:
			domain = type.getDomain((ESelect_type)null);
			break;
		default:
			domain = null;
		}
		*/
		printType(domain);
		pw.println(";");
		pw.println("END_ENTITY; -- " + type.getName(null));
	}

	public void printEntity(EEntity_definition entity) throws SdaiException {
		pw.println("ENTITY " + entity.getName(null));
		AEntity supertypes = entity.getGeneric_supertypes(null);
		if (!entity.getInstantiable(null)) {
			pw.println("\tABSTRACT SUPERTYPE;");
		}
		if (supertypes.getMemberCount() > 0) {
			pw.print("\tSUBTYPE OF (");
			SdaiIterator iter1 = supertypes.createIterator();
			boolean fFirst = true;
			while (iter1.next()) {
				EEntity_definition supertype = (EEntity_definition)supertypes.getCurrentMemberObject(iter1);
				if (fFirst) {
					fFirst = false;
				} else {
					pw.println(", ");
				}
				pw.print(supertype.getName(null));
			}
			pw.println(");");
		}

		// go through the attributes
		AAttribute attributes = entity.getAttributes(null, null);
		SdaiIterator iter2 = attributes.createIterator();
		int iExplicit = 0;
		int iDerived = 0;
		int iInverse = 0;
		while (iter2.next()) {
			EAttribute attribute = attributes.getCurrentMember(iter2);
			if (attribute instanceof EExplicit_attribute) {
				EExplicit_attribute ea = (EExplicit_attribute) attribute;
				iExplicit++;
				pw.print("\t" + attribute.getName(null) + ": ");
				if (ea.getOptional_flag(null)) {
					pw.print("OPTIONAL ");
				}
				EEntity domain = ea.getDomain(null);
				/*switch (ea.testDomain()) {
				case EExplicit_attribute.sDomainSet_type:
					domain = ea.getDomain((ESet_type)null);
					break;
				case EExplicit_attribute.sDomainBag_type:
					domain = ea.getDomain((EBag_type)null);
					break;
				case EExplicit_attribute.sDomainList_type:
					domain = ea.getDomain((EList_type)null);
					break;
				case EExplicit_attribute.sDomainArray_type:
					domain = ea.getDomain((EArray_type)null);
					break;
				case EExplicit_attribute.sDomainNumber_type:
					domain = ea.getDomain((ENumber_type)null);
					break;
				case EExplicit_attribute.sDomainInteger_type:
					domain = ea.getDomain((EInteger_type)null);
					break;
				case EExplicit_attribute.sDomainReal_type:
					domain = ea.getDomain((EReal_type)null);
					break;
				case EExplicit_attribute.sDomainBoolean_type:
					domain = ea.getDomain((EBoolean_type)null);
					break;
				case EExplicit_attribute.sDomainLogical_type:
					domain = ea.getDomain((ELogical_type)null);
					break;
				case EExplicit_attribute.sDomainBinary_type:
					domain = ea.getDomain((EBinary_type)null);
					break;
				case EExplicit_attribute.sDomainString_type:
					domain = ea.getDomain((EString_type)null);
					break;
				case EExplicit_attribute.sDomainDefined_type:
					domain = ea.getDomain((EDefined_type)null);
					break;
				case EExplicit_attribute.sDomainEntity_definition:
					domain = ea.getDomain((EEntity_definition)null);
					break;
				default:
					domain = null;
				}
				*/
				printType(domain);
				pw.println(";");
			}
			if (attribute instanceof EDerived_attribute) {
				iDerived++;
			}
			if (attribute instanceof EInverse_attribute) {
				iInverse++;
			}
		}
		if (iDerived > 0) {
			pw.println("DERIVE");
			iter2.beginning();
			while (iter2.next()) {
				EAttribute attribute = attributes.getCurrentMember(iter2);
				if (attribute instanceof EDerived_attribute) {
					EDerived_attribute da = (EDerived_attribute) attribute;
					pw.print("\t" + attribute.getName(null) + ": ");
					EEntity domain = da.getDomain(null);
					/*
					switch (da.testDomain()) {
					case 1:
						domain = da.getDomain((ESimple_type)null);
						break;
					case 2:
						domain = da.getDomain((EAggregation_type)null);
						break;
					case 3:
						domain = da.getDomain((ENamed_type)null);
						break;
					default:
						domain = null;
					}
					*/
					printType(domain);
					pw.println(";");
				}
			}
		}
		if (iInverse > 0) {
			pw.print("INVERSE");
			iter2.beginning();
			while (iter2.next()) {
				EAttribute attribute = attributes.getCurrentMember(iter2);
				if (attribute instanceof EInverse_attribute) {
					pw.println("\t" + attribute.getName(null) + ": " + ";");
				}
			}
		}
		
	
		pw.println("END_ENTITY; -- " + entity.getName(null));
	}

	public void printSchema(ESchema_definition schema) throws SdaiException {
		String s = schema.getName(null);
		pw.println("SCHEMA " + s + ";");
		AType_declaration types = schema.getType_declarations(null, null);
		SdaiIterator iter1 = types.createIterator();
		while (iter1.next()) {
			EDefined_type type = (EDefined_type)types.getCurrentMember(iter1).getDefinition(null);
			printType(type);			
			pw.println();
		}
		AEntity_declaration entities = schema.getEntity_declarations(null, null);
		SdaiIterator iter2 = entities.createIterator();
		while (iter2.next()) {
			EEntity_definition entity = (EEntity_definition)entities.getCurrentMember(iter2).getDefinition(null);
			printEntity(entity);			
			pw.println();
		}
		pw.println("END_SCHEMA; -- " + schema.getName(null));
	}

	public void printSchema(SdaiModel model) throws SdaiException {
		pw.println("/*  model = " + model.getName() + "  */");
		pw.println();
		if (model.getMode() == 0) {
			model.startReadOnlyAccess();
		}
		ESchema_definition schema = model.getDefinedSchema();
		printSchema(schema);			
		pw.println();
	}

	public void printSchemas(ASdaiModel models) throws SdaiException {
		SdaiIterator iter = models.createIterator();
		while (iter.next()) {
			SdaiModel model = models.getCurrentMember(iter);
			printSchema(model);			
		}
	}


	public void printAllSchemas() throws SdaiException {
		SdaiSession session = SdaiSession.getSession();
		SchemaInstance si = session.getDataDictionary();
		printSchemas(si.getAssociatedModels());
	}


	public static final void main(String argv[]) throws SdaiException {

		if (argv.length != 0) { 
			System.out.println("USAGE: java GenerateExpress");
			return;
		}
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadOnlyAccess();
		
		GenerateExpress ge = new GenerateExpress();
		System.out.println("ImportExport start");
		ge.printAllSchemas();
		System.out.println("ImportExport end");
		session.closeSession();
	}


}


