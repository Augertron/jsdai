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

package jsdai.lang; 
import jsdai.dictionary.*;

/**
 The interface Aggregate represents a collection of arbitrary EXPRESS data types, 
 called members of this aggregate.
 Due to a special nature of the EXPRESS data types, especially the select data type,
 it is not possible to use java.util.Collection or Java arrays for this purpose.
 The interface Aggregate contains the access methods which are common for all
 aggregate classes and also the late binding member access methods for the
 basic data types (double, integer, enumeration, String, Binary), entity instances, 
 nested aggregates, defined data types or a selection thereof. <br><br>

 The aggregates created with the <code>new</code> operator are non-persistent;
 that is, they are not stored within a repository.
 The aggregates used as values of EXPRESS ENTITY attributes are persistent.
 They cannot be created with the <code>new</code> operator.
 Applications need to use special create operations for this. 
 This is also the case for nested aggregates.<br><br>

 The aggregates must be of one of the following EXPRESS types: SET, BAG, LIST or ARRAY.
 The type of an aggregate is determined when an <code>Aggregate</code> is created.
 When creating a non-persistent aggregate with the <code>new</code> operator, the type 
 always is LIST. Otherwise, the type of the <code>Aggregate</code> is determined
 by its usage as an ENTITY attribute or within a nested aggregate. The actual type 
 of an <code>Aggregate</code> can be retrieved with getAggregationType method
 which returns the dictionary description of every aggregate (or null if method 
 is applied to non-persistent list).<br><br>

 Each of types SET, BAG and LIST has a lower bound and, optionally, an upper bound.
 Indexing of members in aggregates of type SET, BAG and LIST starts from 1.
 Aggregates of type ARRAY always have a lower index and an upper index.
 The actual number of members in an aggregate can be retrieved with 
 getMemberCount method.<br><br>

 The members of aggregates can be accessed either by an integer index 
 or by a <a href="SdaiIterator.html">SdaiIterator</a> object. 
 An <code>SdaiIterator</code> is created for a specific <code>Aggregate</code> 
 with createIterator method. An <code>SdaiIterator</code> can be attached to 
 another aggregate using method attachIterator.<br><br>
 
 The aggregates are either ordered collections(LIST, ARRAY) or unordered ones(BAG, SET).
 While some operations in <code>Aggregate</code> are available for any type, others 
 are available only for specific types. The table given below indicates which operation is 
 available for which EXPRESS aggregation type. The access column (acc) indicates whether 
 the operation is defined on members specified by an index (i), specified by an 
 <code>SdaiIterator</code> (I) or for no specific member(-). 
 Some of the operations are available as methods on <code>SdaiIterator</code>.<br>

<center><table BORDER >
<tr><td>                             </td><td>acc</td><td>BAG</td><td>SET</td><td>LIST</td><td>ARRAY</td><td>see SDAI</td></tr>
<tr><td><b>General</b></td></tr>
<tr><td>createIterator                </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.3</td></tr>
<tr><td>attachIterator                </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>-</td></tr>
<tr><td>clear                         </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>-</td></tr>
<tr><td>getAggregationType            </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>-</td></tr>
<tr><td><b>Size, Bounds</b></td></tr>
<tr><td>getMemberCount                </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.1</td></tr>
<tr><td>getLowerBound                 </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.9</td></tr>
<tr><td>getUpperBound                 </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.10</td></tr>
<tr><td>getLowerIndex                 </td><td>-</td><td>-</td><td>-</td><td>-</td><td>X</td><td>10.17.3</td></tr>
<tr><td>getUpperIndex                 </td><td>-</td><td>-</td><td>-</td><td>-</td><td>X</td><td>10.17.4</td></tr>
<tr><td>SdaiIterator.getValueBound    </td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.8</td></tr>
<tr><td>getValueBoundByIndex          </td><td>i</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.15.4</td></tr>
<tr><td><b>Reading and testing members</b></td></tr>
<tr><td>testCurrentMember             </td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.17.2</td></tr>
<tr><td>testByIndex                   </td><td>i</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.17.1</td></tr>
<tr><td>isMember                      </td><td>-</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.2</td></tr>
<tr><td>getCurrentMember              </td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.7</td></tr>
<tr><td>getByIndex                    </td><td>i</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.15.1</td></tr>
<tr><td><b>Set, Unset</b></td></tr>
<tr><td>setCurrentMember              </td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.13.2</td></tr>
<tr><td>setByIndex                    </td><td>i</td><td>-</td><td>-</td><td>X</td><td>X</td><td>10.16.1</td></tr>
<tr><td>SdaiIterator.unset            </td><td>I</td><td>-</td><td>-</td><td>-</td><td>X</td><td>10.18.2</td></tr>
<tr><td>unsetValueByIndex             </td><td>i</td><td>-</td><td>-</td><td>-</td><td>X</td><td>10.18.1</td></tr>
<tr><td><b>Adding and removing members</b></td></tr>
<tr><td>addAfter                      </td><td>I</td><td>-</td><td>-</td><td>X</td><td>-</td><td>10.19.2</td></tr>
<tr><td>addBefore                     </td><td>I</td><td>-</td><td>-</td><td>X</td><td>-</td><td>10.19.1</td></tr>
<tr><td>addByIndex                    </td><td>i</td><td>-</td><td>-</td><td>X</td><td>-</td><td>10.19.3</td></tr>
<tr><td>addUnordered                  </td><td>-</td><td>X</td><td>X</td><td>-</td><td>-</td><td>10.14.1</td></tr>
<tr><td>SdaiIterator.remove           </td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.13.3</td></tr>
<tr><td>removeByIndex                 </td><td>i</td><td>-</td><td>-</td><td>X</td><td>-</td><td>10.19.7</td></tr>
<tr><td>removeUnordered               </td><td>-</td><td>X</td><td>X</td><td>-</td><td>-</td><td>10.14.3</td></tr>
<tr><td><b>Creating new member Aggregates</b></td></tr>
<tr><td>createAggregateAsCurrentMember</td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.13.1</td></tr>
<tr><td>createAggregateAfter          </td><td>I</td><td>-</td><td>-</td><td>X</td><td>-</td><td>10.19.5</td></tr>
<tr><td>createAggregateBefore         </td><td>I</td><td>-</td><td>-</td><td>X</td><td>-</td><td>10.19.4</td></tr>
<tr><td>createAggregateByIndex        </td><td>i</td><td>-</td><td>-</td><td>X</td><td>X</td><td>10.16.2</td></tr>
<tr><td>addAggregateByIndex           </td><td>i</td><td>-</td><td>-</td><td>X</td><td>-</td><td>10.19.6</td></tr>
<tr><td>createAggregateUnordered      </td><td>-</td><td>X</td><td>X</td><td>-</td><td>-</td><td>10.14.2</td></tr>
<tr><td><b>special on SdaiIterator</b></td></tr>
<tr><td>SdaiIterator.beginning        </td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.5</td></tr>
<tr><td>SdaiIterator.end              </td><td>I</td><td>-</td><td>-</td><td>X</td><td>X</td><td>10.15.2</td></tr>
<tr><td>SdaiIterator.next             </td><td>I</td><td>X</td><td>X</td><td>X</td><td>X</td><td>10.12.6</td></tr>
<tr><td>SdaiIterator.previous         </td><td>I</td><td>-</td><td>-</td><td>X</td><td>X</td><td>10.15.3</td></tr>
<tr><td><b>special on ARRAYs</b></td></tr>
<tr><td>reindexArray                  </td><td>-</td><td>-</td><td>-</td><td>-</td><td>X</td><td>10.18.3</td></tr>
<tr><td>resetArrayIndex               </td><td>-</td><td>-</td><td>-</td><td>-</td><td>X</td><td>10.18.4</td></tr>
</table></center> 

 <br>
 The operations to set or add members and, in addition, isMember and 
 removeUnordered are available as overloaded methods for values of type
 Object, int, double and boolean.
 The operations to read members are available as methods with name 
 having the appendix -Object, -Int, -Double, and -Boolean for the 
 corresponding value type.<br><br>

 The package <code>jsdai.lang</code> contains several implementations of
 <code>Aggregate</code> with early binding access. Their names all start with 
 the prefix "A". In the case of nested aggregates the prefixes are 
 "Aa", "Aaa" and so on. The classes implementing interface <code>Aggregate</code> 
 for the basic types are:
 <ul> <li><a href="A_binary.html">A_binary</a></li>
      <li><a href="A_boolean.html">A_boolean</a></li>
      <li><a href="A_enumeration.html">A_enumeration</a></li>
      <li><a href="A_integer.html">A_integer</a></li>
		<li><a href="A_double.html">A_double</a></li>
      <li><a href="A_string.html">A_string</a></li>
      <li><a href="AEntity.html">AEntity</a></li></ul>
      <li><a href="AEntitySelect.html">AEntitySelect</a></li>
</ul>
 Further specializations of <code>AEntity</code> can be found in the 
 early binding packages.<br>

 Implementations of the interface <code>Aggregate</code> for double 
 and triple nested aggregates of the basic types are:
 <ul> <li><a href="Aa_binary.html">Aa_binary</a></li>
      <li><a href="Aa_boolean.html">Aa_boolean</a></li>
      <li><a href="Aa_enumeration.html">Aa_enumeration</a></li>
      <li><a href="Aa_integer.html">Aa_integer</a></li>
		<li><a href="Aa_double.html">Aa_double</a></li>
      <li><a href="Aa_string.html">Aa_string</a></li>
      <li><a href="Aaa_binary.html">Aa_binary</a></li>
      <li><a href="Aaa_boolean.html">Aa_boolean</a></li>
      <li><a href="Aaa_enumeration.html">Aa_enumeration</a></li>
      <li><a href="Aaa_integer.html">Aa_integer</a></li>
		<li><a href="Aaa_double.html">Aa_double</a></li>
      <li><a href="Aaa_string.html">Aa_string</a></li>
</ul>
 There are also the following "early binding" aggregates for session objects:
 <ul> <li><a href="AEntityExtent.html">AEntityExtent</a></li>
      <li><a href="ASchemaInstance.html">ASchemaInstance</a></li>
      <li><a href="ASdaiModel.html">ASdaiModel</a></li>
      <li><a href="ASdaiRepository.html">ASdaiRepository</a></li>
</ul>

 * @see "ISO 10303-22::9.4.11 aggregate_instance"
 */ 
public interface Aggregate extends SdaiEventSource, QuerySource {

/**
 * Returns the number of elements contained in this <code>Aggregate</code>.
 * If the type of this aggregate is a bounded array, then this number is
 * calculated according to the formula "upper_index" - "lower_index" + 1.
 * That is, in this case the size of the array is returned.
 * @return the number of elements in the aggregate.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @see "ISO 10303-22::10.12.1 Get member count"
 */
	int getMemberCount() throws SdaiException;

/**
 * Checks if the specified value is a member of this <code>Aggregate</code>.
 * A positive answer is returned if this value appears in the aggregate 
 * at least once.
 * If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be checked needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> In each of the following cases an <code>SdaiException</code> 
 * VT_NVLD is thrown:
 * <ul><li> if value is not of type <code>EEntity</code> though an 
 * aggregate is known to contain elements of only such type;
 * for example, such aggregates are produced by getInstances methods in 
 * class <code>SdaiModel</code>;
 * <li> if the specified value can be identified only having select path
 * but "select" array is either not provided or empty.</ul>
 * If select path is invalid, then <code>SdaiException</code> VA_NVLD
 * is thrown.

 * @param value the value to be checked for inclusion.
 * @param select select path corresponding to the value submitted.
 * @return <code>true</code> if the specified value is a member of this
 * aggregate, <code>false</code> otherwise.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isMember(int, EDefined_type[])
 * @see #isMember(double, EDefined_type[])
 * @see #isMember(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.12.2 Is member"
 */
	boolean isMember(Object value, EDefined_type select[]) throws SdaiException;

/**
 * Checks if the specified value of type <code>int</code>
 * is a member of this <code>Aggregate</code>.
 * A positive answer is returned if this value appears in the aggregate 
 * at least once.
 * This method is applicable for values of EXPRESS data types
 * INTEGER, LOGICAL and ENUMERATION.
 * If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value
 * to be checked needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul><li> VA_NSET if the value to be checked is, in fact, unset value;
 * <li> VT_NVLD if the specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid.</ul>

 * @param value the value to be checked for inclusion.
 * @param select select path corresponding to the value submitted.
 * @return <code>true</code> if the specified value is a member of this
 * aggregate, <code>false</code> otherwise.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isMember(Object, EDefined_type[])
 * @see #isMember(double, EDefined_type[])
 * @see #isMember(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.12.2 Is member"
 */
	boolean isMember(int value, EDefined_type select[]) throws SdaiException;

/**
 * Checks if the specified value of type <code>double</code>
 * is a member of this <code>Aggregate</code>.
 * A positive answer is returned if this value appears in the aggregate 
 * at least once.
 * This method is applicable for values of EXPRESS data types
 * REAL, and NUMBER.
 * If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value
 * to be checked needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul><li> VA_NSET if the value to be checked is, in fact, unset value;
 * <li> VT_NVLD if the specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid.</ul>

 * @param value the value to be checked for inclusion.
 * @param select select path corresponding to the value submitted.
 * @return <code>true</code> if the specified value is a member of this
 * aggregate, <code>false</code> otherwise.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isMember(Object, EDefined_type[])
 * @see #isMember(int, EDefined_type[])
 * @see #isMember(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.12.2 Is member"
 */
	boolean isMember(double value, EDefined_type select[]) throws SdaiException;

/**
 * Checks if the specified value of type <code>boolean</code>
 * is a member of this <code>Aggregate</code>.
 * A positive answer is returned if this value appears in the aggregate 
 * at least once.
 * This method is applicable for values of EXPRESS data type BOOLEAN.
 * If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value
 * to be checked needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> VT_NVLD if the specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid.</ul>

 * @param value the value to be checked for inclusion.
 * @param select select path corresponding to the value submitted.
 * @return <code>true</code> if the specified value is a member of this
 * aggregate, <code>false</code> otherwise.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #isMember(Object, EDefined_type[])
 * @see #isMember(int, EDefined_type[])
 * @see #isMember(double, EDefined_type[])
 * @see "ISO 10303-22::10.12.2 Is member"
 */
	boolean isMember(boolean value, EDefined_type select[]) throws SdaiException;

/**
 * Creates a new iterator for the aggregate instance, initially set as if a 
 * <code>Beginning</code> operation had been performed.
 * Thus, the iterator has no current member and for ordered collection
 * is positioned before the first member of the aggregate.
 * @return the newly created iterator over this <code>Aggregate</code>.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @see SdaiIterator#beginning
 * @see #attachIterator
 * @see "ISO 10303-22::10.12.3 Create iterator"
 */
	SdaiIterator createIterator() throws SdaiException;

/**
 * Attaches an existing iterator to this <code>Aggregate</code>.
 * The iterator is set as if a <code>Beginning</code> operation had been performed.
 * Thus, the iterator has no current member and for ordered collection
 * is positioned before the first member of the aggregate.
 * <p> This method is an extension of JSDAI, which is 
 * not a part of the standard. It allows to reuse an existing <code>SdaiIterator</code>
 * object created with <code>createIterator</code> method.
 * @param iter the iterator to be attached to this <code>Aggregate</code>.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @see #createIterator
 * @see "ISO 10303-22::10.12.3 Create iterator"
 */
	void attachIterator(SdaiIterator iter) throws SdaiException;

/**
 * Returns the value of the <b>population_dependent_bound</b> for
 * the lower bound or lower index of this <code>Aggregate</code>.
 * <p> This method is not implemented in current JSDAI version.
 * <code>SdaiException</code> EX_NSUP will be thrown if this method is invoked.
 * @return the value of the lower bound or lower index.
 * @throws SdaiException EX_NSUP, expression evaluation not supported.
 * @see <a href="../dictionary/EPopulation_dependent_bound.html">jsdai.dictionary.EPopulation_dependent_bound</a>
 * @see "ISO 10303-22::10.12.9 Get lower bound"
 */
	int getLowerBound() throws SdaiException;

/**
 * Returns the value of the <b>population_dependent_bound</b> for
 * the upper bound or upper index of this <code>Aggregate</code>.
 * <p> This method is not implemented in current JSDAI version.
 * <code>SdaiException</code> EX_NSUP will be thrown if this method is invoked.
 * @return the value of the upper bound or upper index.
 * @throws SdaiException EX_NSUP, expression evaluation not supported.
 * @see <a href="../dictionary/EPopulation_dependent_bound.html">jsdai.dictionary.EPopulation_dependent_bound</a>
 * @see "ISO 10303-22::10.12.9 Get lower bound"
 */
	int getUpperBound() throws SdaiException;

/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException FN_NAVL will be thrown if this method is invoked.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.4.14 SDAI query"
 */
	int query(String where, EEntity entity, AEntity result)
		throws SdaiException;



					/*		Operations with an ordered collection			*/

/**
 * Returns the value (of type <code>Object</code>) of the member at the specified index
 * position in this <code>Aggregate</code>. Indexing starts from 1.
 * The method is valid not only for ordered collections (EXPRESS types
 * ARRAY and LIST) but also for unordered ones (EXPRESS types SET and BAG).
 * This feature is an extension of the standard.
 * @param index the index or position from which the value is asked.
 * @return the value at the specified position in this <code>Aggregate</code>.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getByIndexInt
 * @see #getByIndexDouble
 * @see #getByIndexBoolean
 * @see "ISO 10303-22::10.15.1 Get by index"
 */
	Object getByIndexObject(int index) throws SdaiException;

/**
 * Returns the value (of type <code>int</code>) of the member at the specified index
 * position in this <code>Aggregate</code>. Indexing starts from 1.
 * The method is valid not only for ordered collections (EXPRESS types
 * ARRAY and LIST) but also for unordered ones (EXPRESS types SET and BAG).
 * This feature is an extension of the standard.
 * <p> If value to be returned is not of Java <code>int</code> type, then
 * SdaiException VT_NVLD is thrown. Applying method to an aggregate
 * that can contain elements only of type <code>EEntity</code> results in
 * SdaiException FN_NAVL. Such aggregates, for example, are produced by
 * getInstances methods in class <code>SdaiModel</code>.
 * @param index the index or position from which the value is asked.
 * @return the value at the specified position in this <code>Aggregate</code>.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getByIndexObject
 * @see #getByIndexDouble
 * @see #getByIndexBoolean
 * @see "ISO 10303-22::10.15.1 Get by index"
 */
	int getByIndexInt(int index) throws SdaiException;

/**
 * Returns the value (of type <code>double</code>) of the member at the specified index
 * position in this <code>Aggregate</code>. Indexing starts from 1.
 * The method is valid not only for ordered collections (EXPRESS types
 * ARRAY and LIST) but also for unordered ones (EXPRESS types SET and BAG).
 * This feature is an extension of the standard.
 * <p> If value to be returned is not of Java <code>double</code> type, then
 * SdaiException VT_NVLD is thrown. Applying method to an aggregate
 * that can contain elements only of type <code>EEntity</code> results in
 * SdaiException FN_NAVL. Such aggregates, for example, are produced by
 * getInstances methods in class <code>SdaiModel</code>.
 * @param index the index or position from which the value is asked.
 * @return the value at the specified position in this <code>Aggregate</code>.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getByIndexObject
 * @see #getByIndexInt
 * @see #getByIndexBoolean
 * @see "ISO 10303-22::10.15.1 Get by index"
 */
	double getByIndexDouble(int index) throws SdaiException;

/**
 * Returns the value (of type <code>boolean</code>) of the member at the specified index
 * position in this <code>Aggregate</code>. Indexing starts from 1.
 * The method is valid not only for ordered collections (EXPRESS types
 * ARRAY and LIST) but also for unordered ones (EXPRESS types SET and BAG).
 * This feature is an extension of the standard.
 * <p> If value to be returned is not of Java <code>boolean</code> type, then
 * SdaiException VT_NVLD is thrown. Applying method to an aggregate
 * that can contain elements only of type <code>EEntity</code> results in
 * SdaiException FN_NAVL. Such aggregates, for example, are produced by
 * getInstances methods in class <code>SdaiModel</code>.
 * @param index the index or position from which the value is asked.
 * @return the value at the specified position in this <code>Aggregate</code>.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getByIndexObject
 * @see #getByIndexInt
 * @see #getByIndexDouble
 * @see "ISO 10303-22::10.15.1 Get by index"
 */
	boolean getByIndexBoolean(int index) throws SdaiException;

/**
 * This method is not implemented in current JSDAI version.
 * <p>
 * SdaiException EX_NSUP will be thrown if this method is invoked.
 * @param index the index position of the aggregate element for which
 * the value bound is to be returned.
 * @return the value bound.
 * @throws SdaiException EX_NSUP, expression evaluation not supported.
 * @see "ISO 10303-22::10.15.4 Get value bound by index"
 */
  int getValueBoundByIndex(int index) throws SdaiException;

/**
 * Assigns a new value of type <code>Object</code> to a member of this
 * <code>Aggregate</code> at the specified index position. The method is
 * valid only if this aggregate is an ordered collection (EXPRESS types
 * ARRAY or LIST). If the old value is an aggregate instance, then it is
 * deleted along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not ARRAY or LIST;
 * <li> VA_NSET if null is passed for the value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty, or
 * its type is <code>Aggregate</code>;
 * in the latter case <code>createAggregateByIndex</code> method shall be applied;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param index the index or position for the member whose value is set.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setByIndex(int, int, EDefined_type[])
 * @see #setByIndex(int, double, EDefined_type[])
 * @see #setByIndex(int, boolean, EDefined_type[])
 * @see #createAggregateByIndex
 * @see "ISO 10303-22::10.16.1 Put by index"
 */
	void setByIndex(int index, Object value, EDefined_type select[]) throws SdaiException;

/**
 * Assigns a new value of type <code>int</code> to a member of this 
 * <code>Aggregate</code> at the specified index position. The method is
 * valid only if this aggregate is an ordered collection (EXPRESS types 
 * ARRAY or LIST). If the old value is an aggregate instance, then it is
 * deleted along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not ARRAY or LIST;
 * <li> VA_NSET if unset value is passed as a value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid.
 * </ul>

 * @param index the index or position for the member whose value is set.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setByIndex(int, Object, EDefined_type[])
 * @see #setByIndex(int, double, EDefined_type[])
 * @see #setByIndex(int, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.16.1 Put by index"
 */
	void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException;

/**
 * Assigns a new value of type <code>double</code> to a member of this 
 * <code>Aggregate</code> at the specified index position. The method is
 * valid only if this aggregate is an ordered collection (EXPRESS types 
 * ARRAY or LIST). If the old value is an aggregate instance, then it is
 * deleted along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not ARRAY or LIST;
 * <li> VA_NSET if unset value is passed as a value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid.
 * </ul>

 * @param index the index or position for the member whose value is set.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setByIndex(int, Object, EDefined_type[])
 * @see #setByIndex(int, int, EDefined_type[])
 * @see #setByIndex(int, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.16.1 Put by index"
 */
	void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException;

/**
 * Assigns a new value of type <code>boolean</code> to a member of this 
 * <code>Aggregate</code> at the specified index position. The method is
 * valid only if this aggregate is an ordered collection (EXPRESS types 
 * ARRAY or LIST). If the old value is an aggregate instance, then it is
 * deleted along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not ARRAY or LIST;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid.
 * </ul>

 * @param index the index or position for the member whose value is set.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setByIndex(int, Object, EDefined_type[])
 * @see #setByIndex(int, int, EDefined_type[])
 * @see #setByIndex(int, double, EDefined_type[])
 * @see "ISO 10303-22::10.16.1 Put by index"
 */
	void setByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException;

/**
 * Creates a new, empty aggregate instance and assigns it to a member of this
 * <code>Aggregate</code> at the specified index position. The method is
 * valid only if this aggregate is an ordered collection (EXPRESS types
 * ARRAY or LIST). If the old value is an aggregate instance, then it is
 * deleted along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type,
 * then the select path identifying the type of the aggregate requested
 * to create as a member of this <code>Aggregate</code>
 * needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not ARRAY or LIST;
 * <li> VT_NVLD if the type of the aggregate elements is select type
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param index the index or position for the member which is replaced
 * by the newly created aggregate.
 * @param select select path identifying the type of the aggregate to be created.
 * @return the newly created empty aggregate.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.16.2 Create aggregate instance by index"
 */
  Aggregate createAggregateByIndex(int index, EDefined_type select[])
	 throws SdaiException;



					/*		Operations with an array			*/

/**
 * Checks if a value is set for the member of this <code>Aggregate</code>
 * at the specified index position, and if so, returns the flag indicating
 * the type of this value. Indexing starts from 1.
 * The method is valid not only for EXPRESS type ARRAY but also for other
 * types: LIST, SET and BAG. This feature is an extension of the standard.
 * The possible return values are the following:
 * <ul>
 * <li> 0 for unset (has a sense only for EXPRESS type ARRAY);
 * <li> 1 for Object type;
 * <li> 2 for int type;
 * <li> 3 for double type;
 * <li> 4 for boolean type;
 * </ul>
 * The case of return value 2 encompasses the cases when the aggregate value is of
 * EXPRESS type INTEGER, ENUMERATION and LOGICAL.
 * For the same value each of the methods <code>testByIndex</code>, 
 * <code>testCurrentMember</code> and 
 * {@link EEntity#testAttribute EEntity.testAttribute} 
 * returns the same type indicator.

 * <p> If the type of the aggregate elements is select type and the
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the array "select" (the second method's parameter) of
 * sufficient size for storing the select path corresponding to the current
 * member value at the specified index position must be submitted.
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in Technical corrigendum
 * of ISO 10303-21:11.1.8).
 * The method returns the array "select" with defined data types
 * stored in it. The number of elements in this array is either specified
 * by the position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * If "select" array is not provided or is of insufficient size,
 * then SdaiException VT_NVLD and, respectively, VA_NVLD is thrown.

 * <p> The usage of this method is similar as in Example 1 provided 
 * for {@link EEntity#testAttribute EEntity.testAttribute} method. 
 * The early binding usage of the "Test by index" operation is similar 
 * as in Example 2 given in the description of the same method.

 * @param index the index or position for the member whose value is to be tested.
 * @param select an array provided for storing the select path corresponding to the
 * member value to be tested.
 * @return the flag indicating either the type of the member value or the fact
 * that the specified aggregate member is unset.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #testByIndex(int)
 * @see "ISO 10303-22::10.17.1 Test by index"
 */
	int testByIndex(int index, EDefined_type select[]) throws SdaiException;

/**
 * Checks if a value is set for the member of this <code>Aggregate</code>
 * at the specified index position, and if so, returns the flag indicating
 * the type of this value. Indexing starts from 1.
 * The method is valid not only for EXPRESS type ARRAY but also for other
 * types: LIST, SET and BAG. This feature is an extension of the standard.
 * The possible return values are the following:
 * <ul>
 * <li> 0 for unset (has a sense only for EXPRESS type ARRAY);
 * <li> 1 for Object type;
 * <li> 2 for int type;
 * <li> 3 for double type;
 * <li> 4 for boolean type;
 * </ul>
 * The case of return value 2 encompasses the cases when the aggregate value is of
 * EXPRESS type INTEGER, ENUMERATION and LOGICAL.

 * <p> If the type of the aggregate elements is select type, then
 * SdaiException AI_NVLD is thrown.

 * @param index the index or position for the member whose value is to be tested.
 * @return the flag indicating either the type of the member value or the fact
 * that the specified aggregate member is unset.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #testByIndex(int, EDefined_type [])
 * @see "ISO 10303-22::10.17.1 Test by index"
 */
	int testByIndex(int index) throws SdaiException;

/**
 * Returns the value of the <b>population_dependent_bound</b> for
 * the lower index of this <code>Aggregate</code> when its type is ARRAY.
 * <p> This method is not implemented in current JSDAI version.
 * <code>SdaiException</code> FN_NAVL will be thrown if this method is invoked.
 * @return the value of the lower index.
 * @throws SdaiException FN_NAVL, function not available.
 * @see <a href="../dictionary/EPopulation_dependent_bound.html">jsdai.dictionary.EPopulation_dependent_bound</a>
 * @see "ISO 10303-22::10.17.3 Get lower index"
 */
  int getLowerIndex() throws SdaiException;

/**
 * Returns the value of the <b>population_dependent_bound</b> for
 * the upper index of this <code>Aggregate</code> when its type is ARRAY.
 * <p> This method is not implemented in current JSDAI version.
 * <code>SdaiException</code> FN_NAVL will be thrown if this method is invoked.
 * @return the value of the upper index.
 * @throws SdaiException FN_NAVL, function not available.
 * @see <a href="../dictionary/EPopulation_dependent_bound.html">jsdai.dictionary.EPopulation_dependent_bound</a>
 * @see "ISO 10303-22::10.17.4 Get upper index"
 */
  int getUpperIndex() throws SdaiException;

/**
 * Changes the value of a member at the specified index position of this
 * <code>Aggregate</code> of type ARRAY so that this aggregate has no 
 * value at that position. A subsequent application of 
 * <code>testByIndex</code> methods at this index position will return 
 * value 0. The method is valid only if the type of this aggregate is ARRAY.
 * If this condition is violated, then <code>SdaiException</code>
 * AI_NVLD is thrown. If the old value is an aggregate instance, then
 * it is deleted along with any nested aggregate instances contained by it.

 * @param index the array index of the member to be unset.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.18.1 Unset value by index"
 */
  void unsetValueByIndex(int index) throws SdaiException;

/**
 * Resizes this <code>Aggregate</code>, provided its type is ARRAY,
 * according to the value of the <b>population_dependent_bound</b> for
 * the lower index and/or upper index.
 * <p> This method is not implemented in current JSDAI version.
 * <code>SdaiException</code> EX_NSUP will be thrown if this method is invoked.
 * @throws SdaiException EX_NSUP, expression evaluation not supported.
 * @see <a href="../dictionary/EPopulation_dependent_bound.html">jsdai.dictionary.EPopulation_dependent_bound</a>
 * @see "ISO 10303-22::10.18.3 Reindex array"
 */
  void reindexArray() throws SdaiException;

/**
 * Resets upper and lower indices for this <code>Aggregate</code>, provided its type is ARRAY,
 * according to the new index values based on the <b>population_dependent_bound</b>.
 * After reindexing, elements at invalid index positions are no longer accessible.
 * Elements at new index positions have unset values.
 * <p> This method is not implemented in current JSDAI version.
 * <code>SdaiException</code> FN_NAVL will be thrown if this method is invoked.
 * @param lower the new lower index value.
 * @param upper the new upper index value.
 * @throws SdaiException FN_NAVL, function not available.
 * @see <a href="../dictionary/EPopulation_dependent_bound.html">jsdai.dictionary.EPopulation_dependent_bound</a>
 * @see "ISO 10303-22::10.18.4 Reset array index"
 */
  void resetArrayIndex(int lower, int upper) throws SdaiException;



					/*		Operations with a list			*/

/**
 * Adds a new member of type <code>Object</code> at the specified index position to this
 * <code>Aggregate</code>, provided its type is LIST. The method is
 * invalid for aggregates of other EXPRESS types.
 * If the specified index is equal to the list size plus one, the value submitted 
 * is appended to the end of the list. If the specified index is greater than the above
 * indicated value, then <code>SdaiException</code> IX_NVLD is thrown.

 * <p> If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> VA_NSET if null is passed for the value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty, or
 * its type is <code>Aggregate</code>;
 * in the latter case <code>addAggregateByIndex</code> method shall be applied;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param index the list position for the new member.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addByIndex(int, int, EDefined_type[])
 * @see #addByIndex(int, double, EDefined_type[])
 * @see #addByIndex(int, boolean, EDefined_type[])
 * @see #createAggregateByIndex
 * @see "ISO 10303-22::10.19.3 Add by index"
 */
	void addByIndex(int index, Object value, EDefined_type select[])
    throws SdaiException;

/**
 * Adds a new member of type <code>int</code> at the specified index position to this
 * <code>Aggregate</code>, provided its type is LIST. The method is
 * invalid for aggregates of other EXPRESS types.
 * If the specified index is equal to the list size plus one, the value submitted 
 * is appended to the end of the list. If the specified index is greater than the above
 * indicated value, then <code>SdaiException</code> IX_NVLD is thrown.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param index the list position for the new member.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addByIndex(int, Object, EDefined_type[])
 * @see #addByIndex(int, double, EDefined_type[])
 * @see #addByIndex(int, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.19.3 Add by index"
 */
	void addByIndex(int index, int value, EDefined_type select[])
    throws SdaiException;

/**
 * Adds a new member of type <code>double</code> at the specified index position to this
 * <code>Aggregate</code>, provided its type is LIST. The method is
 * invalid for aggregates of other EXPRESS types.
 * If the specified index is equal to the list size plus one, the value submitted 
 * is appended to the end of the list. If the specified index is greater than the above
 * indicated value, then <code>SdaiException</code> IX_NVLD is thrown.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param index the list position for the new member.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addByIndex(int, Object, EDefined_type[])
 * @see #addByIndex(int, int, EDefined_type[])
 * @see #addByIndex(int, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.19.3 Add by index"
 */
	void addByIndex(int index, double value, EDefined_type select[])
    throws SdaiException;

/**
 * Adds a new member of type <code>boolean</code> at the specified index position to this
 * <code>Aggregate</code>, provided its type is LIST. The method is
 * invalid for aggregates of other EXPRESS types.
 * If the specified index is equal to the list size plus one, the value submitted 
 * is appended to the end of the list. If the specified index is greater than the above
 * indicated value, then <code>SdaiException</code> IX_NVLD is thrown.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param index the list position for the new member.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addByIndex(int, Object, EDefined_type[])
 * @see #addByIndex(int, int, EDefined_type[])
 * @see #addByIndex(int, double, EDefined_type[])
 * @see "ISO 10303-22::10.19.3 Add by index"
 */
	void addByIndex(int index, boolean value, EDefined_type select[])
    throws SdaiException;

/**
 * Creates a new, empty aggregate instance and adds this instance at the
 * specified index position to this <code>Aggregate</code>, provided the
 * type of this <code>Aggregate</code> is LIST. The method is invalid for
 * aggregates of other EXPRESS types. If the specified index is equal to the
 * list size plus one, the aggregate created is appended to the end of the list.
 * If the specified index is greater than the above indicated value, then
 * <code>SdaiException</code> IX_NVLD is thrown.

 * <p> If the type of the aggregate elements is select type,
 * then the select path identifying the type of the aggregate requested
 * to create and add to this <code>Aggregate</code> needs to be submitted
 * (through the second method's parameter). Otherwise, null to the second
 * parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> VT_NVLD if the type of the aggregate elements is select type
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param index the list position for the aggregate created.
 * @param select select path identifying the type of the aggregate to be created.
 * @return the newly created empty aggregate.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.19.6 Add aggregate instance by index"
 */
	Aggregate addAggregateByIndex(int index, EDefined_type select[])
    throws SdaiException;

/**
 * Removes the member at the specified index position from this
 * <code>Aggregate</code>, provided the type of this <code>Aggregate</code> is LIST. The method is
 * invalid for aggregates of other EXPRESS types.
 * If the specified list member is an aggregate instance, then that aggregate instance is
 * deleted along with all nested aggregate instances contained by it.

 * <p> IF the aggregate is not LIST, then <code>SdaiException</code> AI_NVLD is thrown.

 * @param index the list position of the member to be removed.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException IX_NVLD, index invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @see "ISO 10303-22::10.19.7 Remove by index"
 */
	void removeByIndex(int index) throws SdaiException;



					/*		Operations with an unordered collection			*/

/**
 * Adds a new member of type <code>Object</code> to this <code>Aggregate</code>,
 * provided the type of this <code>Aggregate</code> is different than ARRAY.
 * Processing of the LIST type is an extension of the functionality of 
 * <code>Add unordered</code> operation specified in ISO 10303-22::10.14.1.
 * If this case, a new member is added at the end of the list.
 * The method is invalid for aggregates of EXPRESS ARRAY type.

 * <p> If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be added needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is ARRAY;
 * <li> VA_NSET if null is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty, or
 * its type is <code>Aggregate</code>; in the latter case
 * <code>createAggregateUnordered</code> method shall be applied;
 * <li> VA_NVLD if select path is invalid;
 * <li> FN_NAVL if aggregate is read-only. Such aggregates, for example, 
 * are produced by getInstances methods in class <code>SdaiModel</code>.
 * </ul>

 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addUnordered(int, EDefined_type[])
 * @see #addUnordered(double, EDefined_type[])
 * @see #addUnordered(boolean, EDefined_type[])
 * @see #createAggregateUnordered
 * @see "ISO 10303-22::10.14.1 Add unordered"
 */
	void addUnordered(Object value, EDefined_type select[]) throws SdaiException;

/**
 * Adds a new member of type <code>int</code> to this <code>Aggregate</code>,
 * provided the type of this <code>Aggregate</code> is different than ARRAY.
 * Processing of the LIST type is an extension of the functionality of 
 * <code>Add unordered</code> operation specified in ISO 10303-22::10.14.1.
 * If this case, a new member is added at the end of the list.
 * The method is invalid for aggregates of EXPRESS ARRAY type.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is ARRAY;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addUnordered(Object, EDefined_type[])
 * @see #addUnordered(double, EDefined_type[])
 * @see #addUnordered(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.14.1 Add unordered"
 */
	void addUnordered(int value, EDefined_type select[]) throws SdaiException;

/**
 * Adds a new member of type <code>double</code> to this <code>Aggregate</code>,
 * provided the type of this <code>Aggregate</code> is different than ARRAY.
 * Processing of the LIST type is an extension of the functionality of 
 * <code>Add unordered</code> operation specified in ISO 10303-22::10.14.1.
 * If this case, a new member is added at the end of the list.
 * The method is invalid for aggregates of EXPRESS ARRAY type.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is ARRAY;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addUnordered(Object, EDefined_type[])
 * @see #addUnordered(int, EDefined_type[])
 * @see #addUnordered(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.14.1 Add unordered"
 */
	void addUnordered(double value, EDefined_type select[]) throws SdaiException;

/**
 * Adds a new member of type <code>boolean</code> to this <code>Aggregate</code>,
 * provided the type of this <code>Aggregate</code> is different than ARRAY.
 * Processing of the LIST type is an extension of the functionality of 
 * <code>Add unordered</code> operation specified in ISO 10303-22::10.14.1.
 * If this case, a new member is added at the end of the list.
 * The method is invalid for aggregates of EXPRESS ARRAY type.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is ARRAY;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addUnordered(Object, EDefined_type[])
 * @see #addUnordered(int, EDefined_type[])
 * @see #addUnordered(double, EDefined_type[])
 * @see "ISO 10303-22::10.14.1 Add unordered"
 */
	void addUnordered(boolean value, EDefined_type select[]) throws SdaiException;

/**
 * Creates a new, empty aggregate instance and adds this instance
 * to this <code>Aggregate</code>, provided the type of this
 * <code>Aggregate</code> is either SET or BAG. The method is invalid
 * for aggregates of other EXPRESS types, that is, ARRAY and LIST.

 * <p> If the type of the aggregate elements is select type,
 * then the select path identifying the type of the aggregate requested
 * to create and add to this <code>Aggregate</code> needs to be submitted
 * (through the method's parameter). Otherwise, null to the method's
 * parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not SET or BAG;
 * <li> VT_NVLD if the type of the aggregate elements is select type
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param select select path identifying the type of the aggregate to be created.
 * @return the newly created empty aggregate.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.14.2 Create aggregate instance unordered"
 */
	Aggregate createAggregateUnordered(EDefined_type select[]) throws SdaiException;

/**
 * Removes one occurrence of the specified value of type <code>Object</code>
 * from this <code>Aggregate</code>, provided the type of this
 * <code>Aggregate</code> is either SET or BAG. The method is invalid for
 * aggregates of other EXPRESS types, that is, ARRAY and LIST.
 * If the member being removed is an aggregate instance, then that aggregate 
 * instance is deleted along with all nested aggregate instances
 * contained by it.

 * <p> If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value to be
 * removed needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not SET or BAG;
 * <li> VA_NSET if null is passed for the value to be removed;
 * <li> VT_NVLD if the specified value can be identified only having
 * select path but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * <li> VA_NEXS if the specified value was not found in this aggregate;
 * <li> FN_NAVL if aggregate is read-only. Such aggregates, for example, 
 * are produced by getInstances methods in class <code>SdaiModel</code>.
 * </ul>

 * @param value the value to be removed.
 * @param select select path corresponding to the value submitted for removal.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #removeUnordered(int, EDefined_type[])
 * @see #removeUnordered(double, EDefined_type[])
 * @see #removeUnordered(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.14.3 Remove unordered"
 */
	void removeUnordered(Object value, EDefined_type select[]) throws SdaiException;

/**
 * Removes one occurrence of the specified value of type <code>int</code>
 * from this <code>Aggregate</code>, provided the type of this
 * <code>Aggregate</code> is either SET or BAG. The method is invalid for
 * aggregates of other EXPRESS types, that is, ARRAY and LIST.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value to be
 * removed needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not SET or BAG;
 * <li> VA_NSET if unset value is passed as a value to be removed;
 * <li> VT_NVLD if the specified value can be identified only having
 * select path but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * <li> VA_NEXS if the specified value was not found in this aggregate;
 * </ul>

 * @param value the value to be removed.
 * @param select select path corresponding to the value submitted for removal.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #removeUnordered(Object, EDefined_type[])
 * @see #removeUnordered(double, EDefined_type[])
 * @see #removeUnordered(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.14.3 Remove unordered"
 */
	void removeUnordered(int value, EDefined_type select[]) throws SdaiException;

/**
 * Removes one occurrence of the specified value of type <code>double</code>
 * from this <code>Aggregate</code>, provided the type of this
 * <code>Aggregate</code> is either SET or BAG. The method is invalid for
 * aggregates of other EXPRESS types, that is, ARRAY and LIST.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value to be
 * removed needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not SET or BAG;
 * <li> VA_NSET if unset value is passed as a value to be removed;
 * <li> VT_NVLD if the specified value can be identified only having
 * select path but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * <li> VA_NEXS if the specified value was not found in this aggregate;
 * </ul>

 * @param value the value to be removed.
 * @param select select path corresponding to the value submitted for removal.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #removeUnordered(Object, EDefined_type[])
 * @see #removeUnordered(int, EDefined_type[])
 * @see #removeUnordered(boolean, EDefined_type[])
 * @see "ISO 10303-22::10.14.3 Remove unordered"
 */
	void removeUnordered(double value, EDefined_type select[]) throws SdaiException;

/**
 * Removes one occurrence of the specified value of type <code>boolean</code>
 * from this <code>Aggregate</code>, provided the type of this
 * <code>Aggregate</code> is either SET or BAG. The method is invalid for
 * aggregates of other EXPRESS types, that is, ARRAY and LIST.

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value to be
 * removed needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not SET or BAG;
 * <li> VT_NVLD if the specified value can be identified only having
 * select path but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * <li> VA_NEXS if the specified value was not found in this aggregate;
 * </ul>

 * @param value the value to be removed.
 * @param select select path corresponding to the value submitted for removal.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NEXS, value does not exist.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #removeUnordered(Object, EDefined_type[])
 * @see #removeUnordered(int, EDefined_type[])
 * @see #removeUnordered(double, EDefined_type[])
 * @see "ISO 10303-22::10.14.3 Remove unordered"
 */
	void removeUnordered(boolean value, EDefined_type select[]) throws SdaiException;


					/*		Operations using an iterator			*/

/**
 * Checks if a value is set for the member of this <code>Aggregate</code>
 * at the position specified by an iterator, and if so, returns the flag
 * indicating the type of this value. The method is valid not only for
 * EXPRESS type ARRAY but also for other types: SET, BAG and LIST (except
 * non-persistent one). This feature is an extension of the standard.
 * The possible return values are the following:
 * <ul>
 * <li> 0 for unset (has a sense only for EXPRESS type ARRAY);
 * <li> 1 for Object type;
 * <li> 2 for int type;
 * <li> 3 for double type;
 * <li> 4 for boolean type;
 * </ul>
 * The case of return value 2 encompasses the cases when the aggregate value is of
 * EXPRESS type INTEGER, ENUMERATION and LOGICAL.
 * For the same value each of the methods <code>testCurrentMember</code>, 
 * <code>testByIndex</code> and 
 * {@link EEntity#testAttribute EEntity.testAttribute} 
 * returns the same type indicator.

 * <p> If the type of the aggregate elements is select type and the
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the array "select" (the second method's parameter) of
 * sufficient size for storing the select path corresponding to the current
 * member value specified by an iterator must be submitted.
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in Technical corrigendum
 * of ISO 10303-21:11.1.8).
 * The method returns the array "select" with defined data types
 * stored in it. The number of elements in this array is either specified
 * by the position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * If "select" array is not provided or is of insufficient size,
 * then SdaiException VT_NVLD and, respectively, VA_NVLD is thrown.
 * Passing null value for the iterator results in SdaiException IR_NEXS.

 * <p> The usage of this method is similar as in Example 1 provided 
 * for {@link EEntity#testAttribute EEntity.testAttribute} method. 
 * The early binding usage of the "Test current member" operation is similar 
 * as in Example 2 given in the description of the same method.

 * @param iter the iterator specifying the aggregate member to be tested.
 * @param select an array provided for storing the select path corresponding to the
 * member value being tested.
 * @return the flag indicating either the type of the member value or the fact
 * that the specified aggregate member is unset.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #testCurrentMember(SdaiIterator)
 * @see "ISO 10303-22::10.17.2 Test current member"
 */
	int testCurrentMember(SdaiIterator iter, EDefined_type select[]) throws SdaiException;

/**
 * Checks if a value is set for the member of this <code>Aggregate</code>
 * at the position specified by an iterator, and if so, returns the flag
 * indicating the type of this value. The method is valid not only for
 * EXPRESS type ARRAY but also for other types: SET, BAG and LIST (except
 * non-persistent one). This feature is an extension of the standard.
 * The possible return values are the following:
 * <ul>
 * <li> 0 for unset (has a sense only for EXPRESS type ARRAY);
 * <li> 1 for Object type;
 * <li> 2 for int type;
 * <li> 3 for double type;
 * <li> 4 for boolean type;
 * </ul>
 * The case of return value 2 encompasses the cases when the aggregate value is of
 * EXPRESS type INTEGER, ENUMERATION and LOGICAL.

 * <p> If the type of the aggregate elements is select type, then
 * SdaiException AI_NVLD is thrown.
 * Passing null value for the iterator results in SdaiException IR_NEXS.

 * @param iter the iterator specifying the aggregate member to be tested.
 * @return the flag indicating either the type of the member value or the fact
 * that the specified aggregate member is unset.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #testCurrentMember(SdaiIterator, EDefined_type [])
 * @see "ISO 10303-22::10.17.2 Test current member"
 */
	int testCurrentMember(SdaiIterator iter) throws SdaiException;

/**
 * Returns the value (of type <code>Object</code>) of the member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * If null is passed to the method's parameter, then SdaiException
 * IR_NEXS is thrown.
 * @param iter the iterator specifying the aggregate member to return.
 * @return the member of this aggregate referenced by the iterator.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getCurrentMemberInt
 * @see #getCurrentMemberDouble
 * @see #getCurrentMemberBoolean
 * @see "ISO 10303-22::10.12.7 Get current member"
 */
	Object getCurrentMemberObject(SdaiIterator iter) throws SdaiException;

/**
 * Returns the value (of type <code>int</code>) of the member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * <p> If value to be returned is not of Java <code>int</code> type, then
 * SdaiException VT_NVLD is thrown. Passing null to the method's parameter
 * results in SdaiException IR_NEXS.
 * @param iter the iterator specifying the aggregate member to return.
 * @return the member value referenced by the iterator.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getCurrentMemberObject
 * @see #getCurrentMemberDouble
 * @see #getCurrentMemberBoolean
 * @see "ISO 10303-22::10.12.7 Get current member"
 */
	int getCurrentMemberInt(SdaiIterator iter) throws SdaiException;

/**
 * Returns the value (of type <code>double</code>) of the member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * <p> If value to be returned is not of Java <code>double</code> type, then
 * SdaiException VT_NVLD is thrown. Passing null to the method's parameter
 * results in SdaiException IR_NEXS.
 * @param iter the iterator specifying the aggregate member to return.
 * @return the member value referenced by the iterator.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getCurrentMemberObject
 * @see #getCurrentMemberInt
 * @see #getCurrentMemberBoolean
 * @see "ISO 10303-22::10.12.7 Get current member"
 */
	double getCurrentMemberDouble(SdaiIterator iter) throws SdaiException;

/**
 * Returns the value (of type <code>boolean</code>) of the member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * <p> If value to be returned is not of Java <code>boolean</code> type, then
 * SdaiException VT_NVLD is thrown. Passing null to the method's parameter
 * results in SdaiException IR_NEXS.
 * @param iter the iterator specifying the aggregate member to return.
 * @return the member value referenced by the iterator.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NDEF, SdaiModel access not defined.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #getCurrentMemberObject
 * @see #getCurrentMemberInt
 * @see #getCurrentMemberDouble
 * @see "ISO 10303-22::10.12.7 Get current member"
 */
	boolean getCurrentMemberBoolean(SdaiIterator iter) throws SdaiException;

/**
 * Creates a new, empty aggregate instance and assigns it to a member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * If the old value is an aggregate instance, then it is
 * deleted along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type,
 * then the select path identifying the type of the aggregate requested
 * to create as a member of this <code>Aggregate</code>
 * needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> IR_NEXS if iterator is not provided;
 * <li> VT_NVLD if the type of the aggregate elements is select type
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * to be replaced by the newly created aggregate.
 * @param select select path identifying the type of the aggregate to be created.
 * @return the newly created empty aggregate.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see "ISO 10303-22::10.13.1 Create aggregate instance as current member"
 */
	Aggregate createAggregateAsCurrentMember(SdaiIterator iter, EDefined_type select[])
		throws SdaiException;

/**
 * Assigns a new value of type <code>Object</code> to a member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * If the old value is an aggregate instance, then it is deleted
 * along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be assigned needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if null is passed for the value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty, or
 * its type is <code>Aggregate</code>;
 * in the latter case <code>createAggregateAsCurrentMember</code>
 * method shall be applied;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * to assign the specified value.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setCurrentMember(SdaiIterator, int, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, double, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, boolean, EDefined_type[])
 * @see #createAggregateAsCurrentMember
 * @see "ISO 10303-22::10.13.2 Put current member"
 */
	void setCurrentMember(SdaiIterator iter, Object value, EDefined_type select[])
		throws SdaiException;

/**
 * Assigns a new value of type <code>int</code> to a member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * If the old value is an aggregate instance, then it is deleted
 * along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value to be assigned
 * needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if unset value is passed as a value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * to assign the specified value.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setCurrentMember(SdaiIterator, Object, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, double, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.13.2 Put current member"
 */
	void setCurrentMember(SdaiIterator iter, int value, EDefined_type select[])
		throws SdaiException;

/**
 * Assigns a new value of type <code>double</code> to a member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * If the old value is an aggregate instance, then it is deleted
 * along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value to be assigned
 * needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if unset value is passed as a value to be assigned;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * to assign the specified value.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setCurrentMember(SdaiIterator, Object, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, int, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.13.2 Put current member"
 */
	void setCurrentMember(SdaiIterator iter, double value, EDefined_type select[])
		throws SdaiException;

/**
 * Assigns a new value of type <code>boolean</code> to a member of this
 * <code>Aggregate</code> at the position specified by an iterator.
 * If the old value is an aggregate instance, then it is deleted
 * along with any nested aggregate instances contained by it.

 * <p> If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value to be assigned
 * needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> IR_NEXS if iterator is not provided;
 * <li> VT_NVLD if value cannot be assigned because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * to assign the specified value.
 * @param value the value to be assigned.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException IR_NSET, current member is not defined.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #setCurrentMember(SdaiIterator, Object, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, int, EDefined_type[])
 * @see #setCurrentMember(SdaiIterator, double, EDefined_type[])
 * @see "ISO 10303-22::10.13.2 Put current member"
 */
	void setCurrentMember(SdaiIterator iter, boolean value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>Object</code> immediately before the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the beginning and the list is nonempty, then
 * the new member is inserted before the first member of the list, and the
 * iterator is positioned to reference the new member.
 * Otherwise, the new member is appended to the list, and the iterator
 * is left at the end of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if null is passed for the value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty, or
 * its type is <code>Aggregate</code>;
 * in the latter case <code>createAggregateBefore</code>
 * method shall be applied;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * before which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addBefore(SdaiIterator, int, EDefined_type[])
 * @see #addBefore(SdaiIterator, double, EDefined_type[])
 * @see #addBefore(SdaiIterator, boolean, EDefined_type[])
 * @see #createAggregateBefore
 * @see "ISO 10303-22::10.19.1 Add before current member"
 */
	void addBefore(SdaiIterator iter, Object value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>int</code> immediately before the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the beginning and the list is nonempty, then
 * the new member is inserted before the first member of the list, and the
 * iterator is positioned to reference the new member.
 * Otherwise, the new member is appended to the list, and the iterator
 * is left at the end of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * before which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addBefore(SdaiIterator, Object, EDefined_type[])
 * @see #addBefore(SdaiIterator, double, EDefined_type[])
 * @see #addBefore(SdaiIterator, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.19.1 Add before current member"
 */
	void addBefore(SdaiIterator iter, int value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>double</code> immediately before the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the beginning and the list is nonempty, then
 * the new member is inserted before the first member of the list, and the
 * iterator is positioned to reference the new member.
 * Otherwise, the new member is appended to the list, and the iterator
 * is left at the end of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * before which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addBefore(SdaiIterator, Object, EDefined_type[])
 * @see #addBefore(SdaiIterator, int, EDefined_type[])
 * @see #addBefore(SdaiIterator, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.19.1 Add before current member"
 */
	void addBefore(SdaiIterator iter, double value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>boolean</code> immediately before the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the beginning and the list is nonempty, then
 * the new member is inserted before the first member of the list, and the
 * iterator is positioned to reference the new member.
 * Otherwise, the new member is appended to the list, and the iterator
 * is left at the end of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type,
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * before which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addBefore(SdaiIterator, Object, EDefined_type[])
 * @see #addBefore(SdaiIterator, int, EDefined_type[])
 * @see #addBefore(SdaiIterator, double, EDefined_type[])
 * @see "ISO 10303-22::10.19.1 Add before current member"
 */
	void addBefore(SdaiIterator iter, boolean value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>Object</code> immediately after the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the end and the list is nonempty, then the new member is
 * appended to the list, and the iterator is positioned to reference the new member.
 * Otherwise, the new member is inserted before the first member of the list, and
 * the iterator is left at the beginning of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type and the 
 * set of possible leaf types is not a subset of the set of entity
 * data types, then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if null is passed for the value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty, or
 * its type is <code>Aggregate</code>;
 * in the latter case <code>createAggregateAfter</code>
 * method shall be applied;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * after which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addAfter(SdaiIterator, int, EDefined_type[])
 * @see #addAfter(SdaiIterator, double, EDefined_type[])
 * @see #addAfter(SdaiIterator, boolean, EDefined_type[])
 * @see #createAggregateAfter
 * @see "ISO 10303-22::10.19.2 Add after current member"
 */
	void addAfter(SdaiIterator iter, Object value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>int</code> immediately after the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the end and the list is nonempty, then the new member is
 * appended to the list, and the iterator is positioned to reference the new member.
 * Otherwise, the new member is inserted before the first member of the list, and
 * the iterator is left at the beginning of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * after which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addAfter(SdaiIterator, Object, EDefined_type[])
 * @see #addAfter(SdaiIterator, double, EDefined_type[])
 * @see #addAfter(SdaiIterator, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.19.2 Add after current member"
 */
	void addAfter(SdaiIterator iter, int value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>double</code> immediately after the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the end and the list is nonempty, then the new member is
 * appended to the list, and the iterator is positioned to reference the new member.
 * Otherwise, the new member is inserted before the first member of the list, and
 * the iterator is left at the beginning of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VA_NSET if unset value is passed as a value to be added;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * after which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NSET, value not set.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addAfter(SdaiIterator, Object, EDefined_type[])
 * @see #addAfter(SdaiIterator, int, EDefined_type[])
 * @see #addAfter(SdaiIterator, boolean, EDefined_type[])
 * @see "ISO 10303-22::10.19.2 Add after current member"
 */
	void addAfter(SdaiIterator iter, double value, EDefined_type select[])
		throws SdaiException;

/**
 * Adds a new member of type <code>boolean</code> immediately after the member of this
 * <code>Aggregate</code> placed at the position specified by an iterator, provided 
 * the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the end and the list is nonempty, then the new member is
 * appended to the list, and the iterator is positioned to reference the new member.
 * Otherwise, the new member is inserted before the first member of the list, and
 * the iterator is left at the beginning of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type, 
 * then the select path corresponding to the value
 * to be added needs to be submitted (through the third method's parameter).
 * Otherwise, null to the third parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VT_NVLD if value cannot be added because either its type 
 * is wrong for elements of this aggregate, or the 
 * specified value can be identified only having select path
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * after which the specified value has to be added.
 * @param value the value to be added.
 * @param select select path corresponding to the value submitted.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #addAfter(SdaiIterator, Object, EDefined_type[])
 * @see #addAfter(SdaiIterator, int, EDefined_type[])
 * @see #addAfter(SdaiIterator, double, EDefined_type[])
 * @see "ISO 10303-22::10.19.2 Add after current member"
 */
	void addAfter(SdaiIterator iter, boolean value, EDefined_type select[])
		throws SdaiException;

/**
 * Creates a new, empty aggregate instance and adds it immediately before 
 * the member of this <code>Aggregate</code> placed at the position specified by
 * an iterator, provided the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the beginning and the list is nonempty, then
 * the new aggregate instance is inserted before the first member of the list,
 * and the iterator is positioned to reference this instance.
 * Otherwise, the new aggregate instance is appended to the list, and the
 * iterator is left at the end of the list with no member referenced. 

 * <p> If the type of the aggregate elements is select type,
 * then the select path identifying the type of the aggregate requested
 * to create and add as a member of this <code>Aggregate</code>
 * needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VT_NVLD if the type of the aggregate elements is select type
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * before which the new aggregate instance has to be added.
 * @param select select path identifying the type of the aggregate to be created.
 * @return the newly created empty aggregate.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #createAggregateAfter
 * @see "ISO 10303-22::10.19.4 Create aggregate instance before current member"
 */
	Aggregate createAggregateBefore(SdaiIterator iter, EDefined_type select[])
		throws SdaiException;	

/**
 * Creates a new, empty aggregate instance and adds it immediately after 
 * the member of this <code>Aggregate</code> placed at the position specified by
 * an iterator, provided the type of this <code>Aggregate</code> is LIST.
 * The method is invalid for aggregates of other EXPRESS types.
 * If iterator has no current member, then this method behaves as follows.
 * If iterator is at the end and the list is nonempty, then the new aggregate
 * instance is appended to the list, and the iterator is positioned to reference
 * this instance. Otherwise, the new aggregate instance is inserted before
 * the first member of the list, and the iterator is left at the beginning
 * of the list with no member referenced.

 * <p> If the type of the aggregate elements is select type,
 * then the select path identifying the type of the aggregate requested
 * to create and add as a member of this <code>Aggregate</code>
 * needs to be submitted (through the second method's parameter).
 * Otherwise, null to the second parameter can be passed.
 * Select path is a sequence of defined data types needed to specify a 
 * value of a select data type (as defined in ISO 10303-21:11.1.8).
 * The number of defined data types in the array "select" 
 * giving the select path is either specified by the
 * position of the first null value in this array or is equal 
 * to the length of this array if it contains no null value.
 * For a definition of the select path and a detailed example see
 * {@link EEntity#set(EAttribute, Object, EDefined_type[]) set(EAttribute, Object, EDefined_type[])}

 * <p> The list of cases when <code>SdaiException</code> is thrown 
 * contains the following items:
 * <ul>
 * <li> AI_NVLD if the type of the aggregate is not LIST;
 * <li> IR_NEXS if iterator is not provided;
 * <li> VT_NVLD if the type of the aggregate elements is select type
 * but "select" array is either not provided or empty;
 * <li> VA_NVLD if select path is invalid;
 * </ul>

 * @param iter the iterator specifying the position of the aggregate member
 * after which the new aggregate instance has to be added.
 * @param select select path identifying the type of the aggregate to be created.
 * @return the newly created empty aggregate.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException AI_NVLD, aggregate instance invalid.
 * @throws SdaiException VA_NVLD, value invalid.
 * @throws SdaiException VT_NVLD, value type invalid.
 * @throws SdaiException IR_NEXS, iterator does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 * @throws SdaiException SY_ERR, underlying system error.
 * @see #createAggregateBefore
 * @see "ISO 10303-22::10.19.5 Create aggregate instance after current member"
 */
	Aggregate createAggregateAfter(SdaiIterator iter, EDefined_type select[])
		throws SdaiException;

/**
 * Removes all members from this <code>Aggregate</code> if its
 * EXPRESS type is either LIST, or SET, or BAG, and unsets all members
 * of this <code>Aggregate</code> if its type is ARRAY.
 * After execution of this method the length of the aggregate,
 * provided its type is not bounded ARRAY, is zero. When method is applied
 * to an aggregate which is read-only (as, for example, produced by
 * getInstances methods in class <code>SdaiModel</code>), then SdaiException
 * FN_NAVL is thrown.
 * <p> This method is an extension of JSDAI, which is 
 * not a part of the standard.
 * @throws SdaiException TR_NAVL, transaction currently not available.
 * @throws SdaiException TR_EAB, transaction ended abnormally.
 * @throws SdaiException MX_NRW, SDAI-model access not read-write.
 * @throws SdaiException EI_NEXS, entity instance does not exist.
 * @throws SdaiException AI_NEXS, aggregate instance does not exist.
 * @throws SdaiException FN_NAVL, function not available.
 */
	void clear() throws SdaiException;

/**
 * Returns the aggregation type for this <code>Aggregate</code>.
 * Every instance of <code>Aggregate</code> has its own aggregation type
 * which provides an information about the type of the aggregate members and
 * EXPRESS type of the aggregate itself. This information is contained in
 * the data dictionary. If an aggregate is a non-persistent list,
 * then null is returned.
 * <p> This method is an extension of JSDAI, which is 
 * not a part of the standard.
 * @return the aggregation type this <code>Aggregate</code> is based on.
 * @see <a href="../dictionary/EAggregation_type.html">jsdai.dictionary.EAggregation_type</a>
 */
	EAggregation_type getAggregationType() throws SdaiException;
}
