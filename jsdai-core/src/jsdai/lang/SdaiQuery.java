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

/**
 * This interface is a way to deal with SDAI queries.
 * The query first has to be created from <code>SdaiSession</code>.
 * The parameters of a query can be set using <code>setParameter</code>.
 * The query can be then <code>execute</code>d. Prior to execution
 * Query source and domain can be set. Alternatively they can be supplied
 * in <code>execute</code> method. Domain is used to override the
 * domain defined in <code>QuerySource</code> (see {@link QuerySource#getQuerySourceDomain}).
 * If query source nor domain are defined the query is executed on a predefined
 * query source which is implementation specific. The preferred way to get
 * the results is <code>getResultSet</code>.
 *
 * @author <a href="mailto: antanas.masevicius@lksoft.lt> Antanas Masevicius </a>
 * @version $Revision$
 * @see SdaiSession#newQuery(org.w3c.dom.Document)
 * @see SdaiSession#newQuery(org.w3c.dom.Element)
 */
public interface SdaiQuery {

	/**
	 * Sets the default query source to be
	 * later used be {@link #execute()} method.
	 *
	 * @param qs The <code>QuerySource</code> to set
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void setQuerySource(QuerySource qs) throws SdaiException;

	/**
	 * Sets a query domain. This method makes it possible to override
	 * the domain defined in a <code>QuerySource</code>.
	 *
	 * @param domain The domain to set
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void setDomain(ASdaiModel domain) throws SdaiException;

	/**
	 * Executes this query on a default or predefined query source.
	 *
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void execute() throws SdaiException;

	/**
	 * Executes this query on the supplied query source.
	 *
	 * @param qs The <code>QuerySource</code> to execute this query on
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void execute(QuerySource qs) throws SdaiException;

	/**
	 * Executes this query on the supplied query source and domain.
	 *
	 * @param qs The <code>QuerySource</code> to execute this query on
	 * @param domain The domain which overrides query source domain
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void execute(QuerySource qs, ASdaiModel domain) throws SdaiException;

	/**
	 * Returns result by name as <code>AEntity</code>. The query has to be
	 * executed before the result can be obtained.
	 *
	 * @param name The name of the result. All result names can be obtained
	 *             from {@link #getResultNames}
	 * @return <code>AEntity</code> containing entity instances of this result
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @deprecated This method does not support <code>&lt;items&gt;</code>
	 *             constraint. For queries V1.1 the preferred way is to use
	 *             {@link #getResultSet(String)}.
	 */
	public AEntity getResult(String name) throws SdaiException;

	/**
	 * Returns result by index as <code>AEntity</code>. The query has to be
	 * executed before the result can be obtained.
	 *
	 * @param index The 1 based index of the result. The number of the results
	 *              is the size of the array returned by {@link #getResultNames}
	 * @return <code>AEntity</code> containing entity instances of this result
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 * @deprecated This method does not support <code>&lt;items&gt;</code>
	 *             constraint. For queries V1.1 the preferred way is to use
	 *             {@link #getResultSet(int)}.
	 */
	public AEntity getResult(int index) throws SdaiException;

	/**
	 * Returns result set by name. The query has to be
	 * executed before the result set can be obtained.
	 *
	 * @param name The name of the result. All result names can be obtained
	 *             from {@link #getResultNames}
	 * @return <code>QueryResultSet</code> containing the result values
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public QueryResultSet getResultSet(String name) throws SdaiException;

	/**
	 * Returns result set by index. The query has to be
	 * executed before the result set can be obtained.
	 *
	 * @param index The 1 based index of the result. The number of the results
	 *              is the size of the array returned by {@link #getResultNames}
	 * @return <code>QueryResultSet</code> containing the result values
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public QueryResultSet getResultSet(int index) throws SdaiException;

	/**
	 * Returns all result names.
	 *
	 * @return The array of all result names
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public String[] getResultNames() throws SdaiException;

	/**
	 * Sets the parameter of this query.
	 *
	 * @param name The name of the parameter
	 * @param value The value of the parameter
	 * @exception SdaiException if an error occurs during the operation
	 *                          or in underlying JSDAI operations
	 */
	public void setParameter(String name, Object value) throws SdaiException;
	//public void close() throws SdaiException;

}

