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

import java.util.Hashtable;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import jsdai.util.MappingOperations;

/**
 * This class serves as context for several JSDAI operations.
 * The main purposes of this class are the following:
 * <ul>
 * <li>Express environment for expressions.
 * The instance of SdaiContext class is the first parameter in the methods
 * that correspond to express functions, procedures, rules and derived attributes.</li>
 * <li>Execution context for some mapping read or write operations</li>
 * <li>Execution context for Express-X operations</li>
 * <li>Schema information for extensible selects, extensible enumerations, and
 * population dependent bounds</li>
 * </ul>
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */
public class SdaiContext {
	/**
		Provides the schema for typeOf()
	*/
	public ESchema_definition schema; 

	/**
	 * Provides the domain for usedin in expressions, mapping operations, etc.
	 */
	public ASdaiModel domain; 

	/**
	 * Provides the mapping domain for mapping operations
	 */
	public ASdaiModel mappingDomain;

	/** 
	 * Used when temporary instances are needed to be created during the evaluation of expressions.
	 * The non-temporary application model may be provided if needed. Working model is also
	 * used in some mapping operations.
	 */
	public SdaiModel working_model;

    /**
	 * Provides the aggregate of working models.
	 */
	public ASdaiModel working_modelAggr;

	/**
	 * Provides ARM model used by some mapping operations.
	 */
	public SdaiModel mappedWorkingModel;

	/**
	 * Provides uof list used by some mapping operations.
	 */
	public EUof_mapping uofs[];

	/**
	 * Provides schema mapping for Express-X operations.
	 */
	public ESchema_mapping schemaMapping;

	/**
	 * Provides source model for Express-X map.
	 */
	public SdaiModel src_model;

	/**
	 * Provides target model for Express-X map.
	 */
	public SdaiModel tar_model;

	/**
	 * Provides hash table structure to store target data set for Express-X map schema
	 */
	public Hashtable mapHashTable;

	CEntity ent_instances [];
	int aggr_size;
	boolean empty_aggr;
	static final int NUMBER_OF_INSTANCES = 16;

	//--- constructors ------------
	/*
		Are they needed? Is an empty default constructor enough? 
		Values of the attributes most likely must be always set from outside, and not all three needed at once in many
		cases, 
	*/

	/**
	 * Creates an empty <code>SdaiContext</code>.
	 */
	public SdaiContext() {	}

	/**
	 * Creates a new <code>SdaiContext</code>.
	 *
	 * @param inst The entity instance to base the context on
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public SdaiContext(jsdai.lang.EEntity inst)  throws SdaiException {
		// maybe a separate model is needed?
		working_model = inst.findEntityInstanceSdaiModel();
        makeWorkingModelAggr();
		// if working model is a spec model, then the following is not true:
		schema = working_model.getUnderlyingSchema();
		// something goes here, but what? 
		// if any function is invoked from derived attributes only, or from other functions - probably null?
		// the user may want to use express functions himself, then he can provide any domain.
		domain = null;
		mappingDomain = resolveMappingDomain();
	}

    /**
	 * Creates a new <code>SdaiContext</code> based on working model.
	 *
	 * @param workingModel The working <code>SdaiModel</code> to base the context on
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 * @since 3.6.0
	 */
	public SdaiContext(SdaiModel workingModel)  throws SdaiException {
		this.working_model = workingModel;
        makeWorkingModelAggr();
		// if working model is a spec model, then the following is not true:
		schema = working_model.getUnderlyingSchema();
		domain = working_modelAggr;
		mappingDomain = resolveMappingDomain();
	}

    /**
	 * Returns true, if provided UOF is found in context's usable UOF's
	 * @param uofName The name uf UOF
	 * @return True if uof was found and false if uof was not found
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public boolean isUofMapping(String uofName) throws SdaiException {
        if (uofs != null) {
            for (int i = 0; i < uofs.length; i++) {
                if (uofs[i] != null && uofs[i].getName(null).equals(uofName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Sets the list of uofs for mapping operations.
	 *
	 * @param values String array with the names of uofs
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public void setUofMappings(String[] values) throws SdaiException {
        if (values == null) {
            uofs = null;
            return;
        }

        uofs = new EUof_mapping[values.length];

        AUof_mapping auofs = schemaMapping.getUofs(null);
        int index = 0;
        for (int i = 0; i < values.length; i++) {
            SdaiIterator uofIt = auofs.createIterator();
            while (uofIt.next()) {
                EUof_mapping uof =  auofs.getCurrentMember(uofIt);
                if (uof.getName(null).equals(values[i])) {
                    uofs[index] = uof;
                    index++;
                }
            }
        }
    }

	protected ASdaiModel resolveMappingDomain() {
		try {
			SdaiSession sdaiSession = SdaiSession.getSession();
			ASchema_mapping schemaMappings = sdaiSession.getMappings(schema);
            if (schemaMappings.getMemberCount() > 0) {
                schemaMapping = schemaMappings.getByIndex(1);
            }
			return MappingOperations.getMappingDomain(schemaMappings);
		} catch (SdaiException e) {}

		return null;
	}

	protected void makeWorkingModelAggr() throws SdaiException {
		working_modelAggr = new ASdaiModel();
		if (working_model != null) {
			working_modelAggr.addByIndex(1, working_model, null);
		}
	}

	/**
	 * Creates a new <code>SdaiContext</code>.
	 *
	 * @param a_schema The schema definition of the context
	 * @param a_domain The domain of the context
	 * @param a_working_model The working model of the context
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public SdaiContext(ESchema_definition a_schema, ASdaiModel a_domain, SdaiModel a_working_model) throws SdaiException {
		schema = a_schema;
		working_model = a_working_model;
		if (schema == null) {
			if (a_working_model == null) {
				String base = SdaiSession.line_separator + AdditionalMessages.EE_CONC;
				throw new SdaiException(SdaiException.SY_ERR, base);
			}
			schema = a_working_model.getUnderlyingSchema();
		}
		if (schema != null) {
			mappingDomain = resolveMappingDomain();
		}
		makeWorkingModelAggr();
		domain = a_domain;
		if(domain == null) {
			domain = working_modelAggr;
		}
	}

}
