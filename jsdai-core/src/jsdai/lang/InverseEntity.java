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

/** Used to handle the inverseList capabilities of all Application Entities and Connector.
   For subtypes of SessionEntity and DictionaryEntity this functionality shall simply be ignored.
  When the dictionary entities are move out of jsdai.lang they will become ApplicationEntities and
  can use the functionality defined here.
*/
public abstract class InverseEntity extends SdaiCommon {
	Object inverseList;

	//------------------------------------------------------------------------------------
	/* inner class */
	static class Inverse {
		Object value;
		Object next;

		Inverse(Object value, Object next) {
			this.value = value;
			this.next = next;
		}
	}


	InverseEntity() {
		super();
	}

	/** add an CEntity to the inverseList */
	final protected void inverseAdd(InverseEntity value) {
//		synchronized (syncObject) {
		if (value == null) {
			throw new NullPointerException("null value in Inverse.add()");
		} if (inverseList == null) {
			inverseList = value;
		} else {
			inverseList = new Inverse(value, inverseList);
		}
//		} // syncObject
	}

	/** removes an CEntity from the inverseList */
	final protected void inverseRemove(InverseEntity value) {
//		synchronized (syncObject) {
		// old != null, otherwise NullPointerException is thrown
		if (value == null) {
			throw new NullPointerException("value == null");
		} else if (inverseList == null) {
			return;
		} else if (inverseList == value) {
			inverseList = null;
		} else {
			Inverse inv1 = (Inverse) inverseList;
			if (inv1.value == value) {
				inverseList = inv1.next;
				inv1.value = null;
				inv1.next = null;
			} else if (inv1.next == value) {
				inverseList = inv1.value;
				inv1.value = null;
				inv1.next = null;
			} else {
				Inverse inv2 = inv1;
				Inverse inv3 = (Inverse) inv2.next;
				while (true) {
					if (inv3 == null) {
						break;
					}
					if (inv3.value == value) {
						inv2.next = inv3.next;
						inv3.value = null;
						inv3.next = null;
						break;
					} else if (inv3.next == value) {
						inv2.next = inv3.value;
						inv3.value = null;
						inv3.next = null;
						break;
					} else {
						inv2 = inv3;
						inv3 = (Inverse) inv2.next;
					}
				}
			}
		}
//		} // syncObject
	}


	final protected void inverseRemoveSpecial(InverseEntity value) {
		if (value == null) {
			throw new NullPointerException("value == null");
		} else if (inverseList == null) {
			return;
		} else if (inverseList == value) {
			inverseList = null;
		} else {
			if (!(inverseList instanceof Inverse)) {
				return;
			}
			Inverse inv1 = (Inverse) inverseList;
			if (inv1.value == value) {
				inverseList = inv1.next;
				inv1.value = null;
				inv1.next = null;
			} else if (inv1.next == value) {
				inverseList = inv1.value;
				inv1.value = null;
				inv1.next = null;
			} else {
				Inverse inv2 = inv1;
				Inverse inv3 = (Inverse) inv2.next;
				while (true) {
					if (inv3 == null) {
						break;
					}
					if (inv3.value == value) {
						inv2.next = inv3.next;
						inv3.value = null;
						inv3.next = null;
						break;
					} else if (inv3.next == value) {
						inv2.next = inv3.value;
						inv3.value = null;
						inv3.next = null;
						break;
					} else {
						inv2 = inv3;
						inv3 = (Inverse) inv2.next;
					}
				}
			}
		}
	}


	final protected void inverseRemoveAll(InverseEntity value) {
//		synchronized (syncObject) {
		// old != null, otherwise NullPointerException is thrown
		if (value == null) {
			throw new NullPointerException("value == null");
		} else if (inverseList == null) {
//			throw new NullPointerException("inverseList == null");
return;
		} else if (inverseList == value) {
			inverseList = null;
		} else {
			Inverse inv1 = (Inverse) inverseList;
			if (inv1.value == value) {
				inverseList = inv1.next;
//if (((CEntity)inv1.value).instance_identifier == 204902)
//System.out.println("  InverseEntity &&&&& new value: " +  inv1.next +
//"   type: " + inv1.next.getClass().getName() + 
//"   inverseList.value: " + ((Inverse)inv1.next).value +
//"  hashCode: " + ((Inverse)inv1.next).value.hashCode());
				inv1.value = null;
				inv1.next = null;
			} else if (inv1.next == value) {
				inverseList = inv1.value;
				inv1.value = null;
				inv1.next = null;
			} else {
				Inverse inv2 = inv1;
				Inverse inv3 = (Inverse) inv2.next;
				while (true) {
if (inv3 == null) {
break;
}
					if (inv3.value == value) {
						inv2.next = inv3.next;
						inv3.value = null;
						inv3.next = null;
					} else if (inv3.next == value) {
						inv2.next = inv3.value;
						inv3.value = null;
						inv3.next = null;
					} else {
						inv2 = inv3;
						inv3 = (Inverse) inv2.next;
					}
				}
			}
		}
//		} // syncObject
	}


	final protected void addToInverseList(InverseEntity instance) {
		instance.inverseAdd(this);
	}

	final protected void removeFromInverseList(InverseEntity instance) {
		instance.inverseRemove(this);
	}


	/** change ALL attributes referencing "old" to "new" */
	protected abstract void changeReferences(InverseEntity old, InverseEntity newer) throws SdaiException;

	final protected void changeInverseReferences(InverseEntity old, InverseEntity newer, 
			boolean replace_by_connector, boolean print_to_logo, boolean save4undo) throws SdaiException {
//		synchronized (syncObject) {
		Object o = inverseList;
		SdaiModel.Connector con;
		CEntity old_inst = (CEntity)old;
//long o_id = old_inst.instance_identifier;
//if (o_id == 69) {
//System.out.println("!!!!!!!!!!!!!!!!! old instance: " + old_inst);
//CEntity n_inst = (CEntity)newer;
//System.out.println("!!!!!!!!!!!!!!!!! new instance: " + n_inst);
//}
		SdaiSession se;
		SdaiModel mod;
		CEntity instance;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				InverseEntity inst = (InverseEntity) inv.value;
				if (replace_by_connector && ((CEntity)inst).owning_model != null &&
						((CEntity)inst).owning_model != old_inst.owning_model) {
					con = ((CEntity)inst).owning_model.newConnector(old_inst.owning_model, 
						((CEntityDefinition)old_inst.getInstanceType()).getCorrectName(), 
						old_inst.instance_identifier, (CEntity)inst);
					inst.changeReferences(old, con);
					((CEntity)inst).modified();
//if (o_id == 69) {
//CEntity ii1 = (CEntity)inst;
//System.out.println("!!!!! Case 1  referencing instance: " + ii1);
//}
				} else {
					if (save4undo) {
						instance = (CEntity)inst;
						instance.owning_model.repository.session.undoRedoModifyPrepare(instance);
					}
					inst.changeReferences(old, newer);
					if (inst instanceof CEntity) {
						((CEntity)inst).modified();
						if (print_to_logo) {
							if (old_inst.owning_model != null) {
								printWarningToLogo(old_inst.owning_model.repository.session, AdditionalMessages.RD_MINS, 
									((CEntity)old).instance_identifier, ((CEntity)inst).instance_identifier);
							} else {
								printWarningToLogo(null, AdditionalMessages.RD_MINS, 
									((CEntity)old).instance_identifier, ((CEntity)inst).instance_identifier);
							}
						}
					}
				}
				o = inv.next;
			} else {
				InverseEntity inst = (InverseEntity) o;
				if (replace_by_connector && ((CEntity)inst).owning_model != null &&
						((CEntity)inst).owning_model != old_inst.owning_model) {
					con = ((CEntity)inst).owning_model.newConnector(old_inst.owning_model, 
						((CEntityDefinition)old_inst.getInstanceType()).getCorrectName(), 
						old_inst.instance_identifier, (CEntity)inst);
					inst.changeReferences(old, con);
					((CEntity)inst).modified();
				} else {
					if (save4undo) {
						instance = (CEntity)inst;
						instance.owning_model.repository.session.undoRedoModifyPrepare(instance);
					}
					inst.changeReferences(old, newer);
					if (inst instanceof CEntity) {
						((CEntity)inst).modified();
						if (print_to_logo) {
							if (old_inst.owning_model != null) {
								printWarningToLogo(old_inst.owning_model.repository.session, AdditionalMessages.RD_MINS, 
									((CEntity)old).instance_identifier, ((CEntity)inst).instance_identifier);
							} else {
								printWarningToLogo(null, AdditionalMessages.RD_MINS, 
									((CEntity)old).instance_identifier, ((CEntity)inst).instance_identifier);
							}
						}
					}
				}
				o = null;
			}
		}
//		} // syncObject
	}


	final protected void setModifiedFlag() throws SdaiException {
		Object o = inverseList;
		CEntity inst;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				inst = (CEntity) inv.value;
				inst.modified();
				o = inv.next;
			} else {
				inst = (CEntity) o;
				inst.modified();
				o = null;
			}
		}
	}


	final protected void moveReferences(InverseEntity old, InverseEntity newer, boolean save4undo) throws SdaiException {
		Object o = inverseList;
		CEntity inst;
		CEntity old_inst = (CEntity)old;
		CEntity newer_inst = (CEntity)newer;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				inst = (CEntity) inv.value;
				if (inst.owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((inst.owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, inst.owning_model);
				}
				if (save4undo) {
					inst.owning_model.repository.session.undoRedoModifyPrepare(inst);
				}
				old_inst.instance_identifier = -old_inst.instance_identifier;
				newer_inst.instance_identifier = -newer_inst.instance_identifier;
				inst.changeReferences(old, newer);
				newer_inst.instance_identifier = -newer_inst.instance_identifier;
				if (old_inst.instance_identifier<0) {
					old_inst.instance_identifier = -old_inst.instance_identifier;
					newer.inverseAdd(inst);
				}
				o = inv.next;
			} else {
				inst = (CEntity) o;
				if (inst.owning_model == null) {
					throw new SdaiException(SdaiException.EI_NEXS);
				}
				if ((inst.owning_model.mode & SdaiModel.MODE_MODE_MASK) != SdaiModel.READ_WRITE) {
					throw new SdaiException(SdaiException.MX_NRW, inst.owning_model);
				}
				if (save4undo) {
					inst.owning_model.repository.session.undoRedoModifyPrepare(inst);
				}
				old_inst.instance_identifier = -old_inst.instance_identifier;
				newer_inst.instance_identifier = -newer_inst.instance_identifier;
				inst.changeReferences(old, newer);
				newer_inst.instance_identifier = -newer_inst.instance_identifier;
				if (old_inst.instance_identifier<0) {
					old_inst.instance_identifier = -old_inst.instance_identifier;
					newer.inverseAdd(inst);
				}
				o = null;
			}
		}
	}


	final protected void unsetInverseReferences(boolean replace_by_connector) throws SdaiException {
		Object o = inverseList;
		SdaiModel.Connector con;
		CEntity inst;
		CEntity old_inst = (CEntity)this;
		while (o != null) {
			Inverse inv = null;
			if (o instanceof Inverse) {
				inv = (Inverse) o;
				inst = (CEntity) inv.value;
				if (inst.owning_model != null && inst.owning_model != old_inst.owning_model && 
						!inst.owning_model.closingAll) {
					if (replace_by_connector) {
						con = inst.owning_model.newConnector(old_inst.owning_model, 
							((CEntityDefinition)old_inst.getInstanceType()).getCorrectName(), 
							old_inst.instance_identifier, inst);
						inst.changeReferences(old_inst, con);
					} else {
						inst.changeReferences(old_inst, null);
					}
				}
				inv.value = null;
				o = inv.next;
			} else {
				inst = (CEntity) o;
				if (inst.owning_model != null &&
						inst.owning_model != old_inst.owning_model && !inst.owning_model.closingAll) {
					if (replace_by_connector) {
						con = inst.owning_model.newConnector(old_inst.owning_model, 
							((CEntityDefinition)old_inst.getInstanceType()).getCorrectName(), 
							old_inst.instance_identifier, inst);
						inst.changeReferences(old_inst, con);
					} else {
						inst.changeReferences(old_inst, null);
					}
				}
				if (inv != null) {
					inv.next = null;
				} else {
					inverseList = null;
				}
				o = null;
			}
		}
	}


	final protected void deleteInverseReferences(boolean delete_repo) throws SdaiException {
		CEntity inv_owner = (CEntity)this;
		SdaiRepository repo = inv_owner.owning_model.repository;
		Object o = inverseList;
		SdaiModel.Connector con;
		SdaiModel own_mod;
		while (o != null) {
			Inverse inv = null;
			if (o instanceof Inverse) {
				inv = (Inverse) o;
				InverseEntity inst = (InverseEntity) inv.value;
				own_mod = ((CEntity)inst).owning_model;
				if (own_mod != null && own_mod.repository != repo) {
					if (delete_repo) {
						inst.changeReferences(inv_owner, null);
					} else {
						con = own_mod.newConnector(inv_owner.owning_model, 
							((CEntityDefinition)inv_owner.getInstanceType()).getCorrectName(), 
							inv_owner.instance_identifier, (CEntity)inst);
						inst.changeReferences(inv_owner, con);
					}
				}
				inv.value = null;
				o = inv.next;
			} else {
				InverseEntity inst = (InverseEntity) o;
				own_mod = ((CEntity)inst).owning_model;
				if (own_mod != null && own_mod.repository != repo) {
					if (delete_repo) {
						inst.changeReferences(inv_owner, null);
					} else {
						con = own_mod.newConnector(inv_owner.owning_model, 
							((CEntityDefinition)inv_owner.getInstanceType()).getCorrectName(), 
							inv_owner.instance_identifier, (CEntity)inst);
						inst.changeReferences(inv_owner, con);
					}
				}
				if (inv != null) {
					inv.next = null;
				} else {
					inverseList = null;
				}
				o = null;
			}
		}
	}


/**
  This method implements an auxiliary operation used in substituteInstance() method. 
  It runs through all instances in the inverse list and processes all references 
  to the current instance.
 */
	final protected Value checkInverseReferences(CEntity_definition new_def) throws SdaiException {
//		synchronized (syncObject) {
		CEntity old = (CEntity)this;
		Object o = inverseList;
		Value val;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				val = processInverseReference((CEntity)inv.value, old, new_def);
				if (val != null) {
					return val;
				}
				o = inv.next;
			} else {
				val = processInverseReference((CEntity)o, old, new_def);
				if (val != null) {
					return val;
				}
				o = null;
			}
		}
		return null;
//		} // syncObject
	}


/**
  This method implements an auxiliary operation used in substituteInstance() method. 
  It runs through all attributes of the specified entity instance (first parameter) and checks their values. 
  If a value is found which is a reference to the entity instance (second parameter) that is an instance 
  being replaced by a new instance (third parameter), then it is checked if the type of this substitute 
  is compatible with the type of the attribute to which this value-reference is set.
  If compatible, then "true" is returned; otherwise "false".
 */
	private Value processInverseReference(CEntity ref_owner, CEntity old, CEntity_definition new_def) 
			throws SdaiException {
		CEntityDefinition owner_def = (CEntityDefinition)ref_owner.getInstanceType();
		StaticFields staticFields = StaticFields.get();
		if (ref_owner == null) {
			String base = SdaiSession.line_separator + "Entity instance user not found." + 
				SdaiSession.line_separator + "   Instance to be substituted: " + old + 
				SdaiSession.line_separator + "   New instance definition: " + new_def;
			throw new SdaiException(SdaiException.SY_ERR, base);
		} else if (ref_owner.owning_model == null) {
			String base = SdaiSession.line_separator + "Entity instance user is invalid." + 
				SdaiSession.line_separator + "   Instance to be substituted: " + old + 
				SdaiSession.line_separator + "   Its user's persistent label: " + ref_owner.instance_identifier + 
				SdaiSession.line_separator + "   New instance definition: " + new_def;
			throw new SdaiException(SdaiException.SY_ERR, base);
		}
		ref_owner.owning_model.prepareAll(staticFields.entity_values, (CEntity_definition)owner_def);
		ref_owner.getAll(staticFields.entity_values);
//ref_owner.owning_model.print_entity_values(staticFields.entity_values, 56);

		for (int i = 0; i < owner_def.noOfPartialEntityTypes; i++) {
			EntityValue pval = staticFields.entity_values.entityValues[i];
			if (pval == null) {
				continue;
			}
			CEntity_definition p_def = pval.def;
			CExplicit_attribute[] expl_attrs = ((CEntityDefinition)p_def).takeExplicit_attributes();
			for (int j = 0; j < p_def.noOfPartialAttributes; j++) {
				Value val = pval.values[j];
				Value res = val.examine_value(ref_owner, old, new_def, (DataType)expl_attrs[j].getDomain(null), expl_attrs[j]);
//System.out.println("InverseEntity ********  res.reference: " + res.reference);
				if (res != null) {
					return res;
				} 
			}
		}
		return null;
	}


	static void printWarningToLogo(SdaiSession session, String text, long inst_ident, long referencing_inst_ident) 
			throws SdaiException {
		if (session != null && session.logWriterSession != null) {
			session.printlnSession(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INSM + inst_ident + SdaiSession.line_separator + 
				AdditionalMessages.RD_INSR + referencing_inst_ident);
		} else {
			SdaiSession.println(text + SdaiSession.line_separator +
//				AdditionalMessages.RD_PHFI + PhFileReader.phys_file + SdaiSession.line_separator +
				AdditionalMessages.RD_INSM + inst_ident + SdaiSession.line_separator + 
				AdditionalMessages.RD_INSR + referencing_inst_ident);
		}
	}


// Methods below are for debugging purposes
	final void printInverses() {
System.out.println("InverseEntity ------------ printing inverse list");
		Object o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				CEntity inst = (CEntity) inv.value;
System.out.println("InverseEntity Place 1  inst: #" + inst.instance_identifier + 
"   its model: " + inst.owning_model);
//				System.out.println("\t instance: " + inv.value);
				o = inv.next;
			} else {
				CEntity inst = (CEntity) o;
System.out.println("InverseEntity Place 2  inst: #" + inst.instance_identifier + 
"   its model: " + inst.owning_model);
//				System.out.println("\t instance: " + o);
				o = null;
			}
		}
	}

	protected String getInverses() {
		String str="";
		Object o = inverseList;
		CEntity ent;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				ent = (CEntity)inv.value;
				String s=ent.toString();
				str=str+s+"         ";
				o = inv.next;
			} else {
				ent = (CEntity)o;
				String s2=ent.toString();
				str=str+s2+"         ";
				o = null;
			}
		}
		return str;
	}

	final protected void checkInverses() throws SdaiException {
		Object o = inverseList;
		CEntity old_inst = (CEntity)this;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				InverseEntity inst = (InverseEntity) inv.value;
if (inst==null)
System.out.println("InverseEntity Place 1  inst is NULL    this instance: " + old_inst + 
"   its model: " + old_inst.owning_model.name);
				o = inv.next;
			} else {
				InverseEntity inst = (InverseEntity) o;
if (inst==null)
System.out.println("InverseEntity Place 2  inst is NULL    this instance: " + old_inst + 
"   its model: " + old_inst.owning_model.name);
				o = null;
			}
		}
	}

	protected int countInverses() {
		int count = 0;
		Object o = inverseList;
		while (o != null) {
			if (o instanceof Inverse) {
				Inverse inv = (Inverse) o;
				count++;
				o = inv.next;
			} else {
				count++;
				o = null;
			}
		}
		return count;
	}


}
