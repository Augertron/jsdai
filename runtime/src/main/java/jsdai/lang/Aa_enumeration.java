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

import jsdai.dictionary.EAggregation_type;
import jsdai.dictionary.EDefined_type;

/**
 * Specialized class implementing double nested <code>Aggregate</code> for
 * members of the EXPRESS type ENUMERATION. See <a href="Aggregate.html">Aggregate</a>
 * for detailed description of methods whose specializations are given here.
 */
public class Aa_enumeration extends CAggregate {

  Aa_enumeration() {
    super();
  }

  Aa_enumeration(EAggregation_type provided_type, CEntity instance) {
    super((AggregationType) provided_type, instance);
  }

  /**
   * It is a specialization of {@link Aggregate#isMember(Object, EDefined_type []) isMember(Object, EDefined_type [])} method -
   * the first parameter is of type <code>A_enumeration</code> and the second parameter is dropped.
   *
   * @param value
   * @return
   */
  public boolean isMember(A_enumeration value) throws SdaiException {
    return isMember(value, null);
  }

  public boolean isMember(int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public boolean isMember(double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public boolean isMember(boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /* Operations with an ordered collection */

  /**
   * It is {@link Aggregate#getByIndexObject getByIndexObject} method
   * with return value of type <code>A_enumeration</code> instead of <code>Object</code>.
   *
   * @param index
   * @return
   */
  public A_enumeration getByIndex(int index) throws SdaiException {
    return (A_enumeration) getByIndexObject(index);
  }

  public void setByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void setByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void setByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /**
   * It is a specialization of {@link Aggregate#createAggregateByIndex(int, EDefined_type []) createAggregateByIndex(int,
   * EDefined_type [])} method - the return value is of type <code>A_enumeration</code> instead of <code>Object</code> and the
   * second parameter is dropped.
   *
   * @param index
   * @return
   */
  public A_enumeration createAggregateByIndex(int index) throws SdaiException {
    return (A_enumeration) createAggregateByIndex(index, null);
  }

  public void addByIndex(int index, int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addByIndex(int index, double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addByIndex(int index, boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /**
   * It is a specialization of {@link Aggregate#addAggregateByIndex(int, EDefined_type []) addAggregateByIndex(int,
   * EDefined_type [])} method - the return value is of type <code>A_enumeration</code> instead of <code>Object</code> and the
   * second parameter is dropped.
   *
   * @param index
   * @return
   */
  public A_enumeration addAggregateByIndex(int index) throws SdaiException {
    return (A_enumeration) addAggregateByIndex(index, null);
  }

  /* Operations with an unordered collection */

  public void addUnordered(int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addUnordered(double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addUnordered(boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /**
   * It is a specialization of {@link Aggregate#createAggregateUnordered(EDefined_type [])
   * createAggregateUnordered(EDefined_type [])} method - the return value is of type <code>A_enumeration</code> instead of
   * <code>Object</code> and the parameter is dropped.
   *
   * @return
   */
  public A_enumeration createAggregateUnordered() throws SdaiException {
    return (A_enumeration) createAggregateUnordered(null);
  }

  /**
   * It is a specialization of {@link Aggregate#removeUnordered(Object, EDefined_type []) removeUnordered(Object, EDefined_type
   * [])} method - the first parameter is of type <code>A_enumeration</code> and the second parameter is dropped.
   *
   * @param value
   */
  public void removeUnordered(A_enumeration value) throws SdaiException {
    removeUnordered(value, null);
  }

  public void removeUnordered(int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void removeUnordered(double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void removeUnordered(boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /* Operations using an iterator */

  /**
   * It is equivalent to {@link Aggregate#getCurrentMemberObject getCurrentMemberObject} method
   * with return value of type <code>A_enumeration</code> instead of <code>Object</code>.
   *
   * @param iter
   * @return
   */
  public A_enumeration getCurrentMember(SdaiIterator iter) throws SdaiException {
    return (A_enumeration) getCurrentMemberObject(iter);
  }

  public void setCurrentMember(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void setCurrentMember(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void setCurrentMember(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /**
   * It is a specialization of {@link Aggregate#createAggregateAsCurrentMember(SdaiIterator, EDefined_type [])
   * createAggregateAsCurrentMember(SdaiIterator, EDefined_type [])} method - the return value is of type
   * <code>A_enumeration</code> instead of <code>Object</code> and the second parameter is dropped.
   *
   * @param iter
   * @return
   */
  public A_enumeration createAggregateAsCurrentMember(SdaiIterator iter) throws SdaiException {
    return (A_enumeration) createAggregateAsCurrentMember(iter, null);
  }

  public void addBefore(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addBefore(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addBefore(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /**
   * It is a specialization of {@link Aggregate#createAggregateBefore(SdaiIterator, EDefined_type [])
   * createAggregateBefore(SdaiIterator, EDefined_type [])} method - the return value is of type <code>A_enumeration</code>
   * instead of <code>Object</code> and the second parameter is dropped.
   *
   * @param iter
   * @return
   */
  public A_enumeration createAggregateBefore(SdaiIterator iter) throws SdaiException {
    return (A_enumeration) createAggregateBefore(iter, null);
  }

  public void addAfter(SdaiIterator iter, int value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addAfter(SdaiIterator iter, double value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  public void addAfter(SdaiIterator iter, boolean value, EDefined_type select[]) throws SdaiException {
    throw new SdaiException(SdaiException.VT_NVLD);
  }

  /**
   * It is a specialization of {@link Aggregate#createAggregateAfter(SdaiIterator, EDefined_type [])
   * createAggregateAfter(SdaiIterator, EDefined_type [])} method - the return value is of type <code>A_enumeration</code>
   * instead of <code>Object</code> and the second parameter is dropped.
   *
   * @param iter
   * @return
   */
  public A_enumeration createAggregateAfter(SdaiIterator iter) throws SdaiException {
    return (A_enumeration) createAggregateAfter(iter, null);
  }

}
