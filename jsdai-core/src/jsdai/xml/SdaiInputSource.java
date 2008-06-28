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

package jsdai.xml;

import jsdai.lang.ASchemaInstance;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SchemaInstance;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import org.xml.sax.InputSource;

/**
 * This class is a single JDAI data input source for an XML entity.
 * It encapsulates JSDAI information which is transferred to readers
 * that process JSDAI populations. The object should either be
 * constructed with non default constructor or have at least one <code>set</code>
 * method called before it is passed to JSDAI reader.
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 * @see InstanceReader
 */

public class SdaiInputSource extends InputSource {

	private SdaiRepository repository;
	private SchemaInstance schInstance;
	private ASchemaInstance schInstances;
	private SdaiModel model;
	private ASdaiModel[] models;

	/**
	 * Creates a new empty SDAI input source. At least one <code>set</code>
	 * should be called afterward to put the object into valid state.
	 */
	public SdaiInputSource() { }

	/**
	 * Creates a new <code>SdaiRepository</code> input source.
	 *
	 * @param repository The <code>SdaiRepository</code>
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public SdaiInputSource(SdaiRepository repository) throws SdaiException {
		setRepository(repository);
	}

	/**
	 * Creates a new <code>SchemaInstance</code> input source.
	 *
	 * @param schInstance The <code>SchemaInstance</code>
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public SdaiInputSource(SchemaInstance schInstance) throws SdaiException {
		setSchemaInstance(schInstance);
	}

	/**
	 * Creates a new input source of the <code>SchemaInstance</code> aggregate.
	 *
	 * @param schInstances The <code>SchemaInstance</code> aggregate
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public SdaiInputSource(ASchemaInstance schInstances) throws SdaiException {
		setSchemaInstances(schInstances);
	}

	/**
	 * Creates a new <code>SdaiModel</code> input source.
	 *
	 * @param model The <code>SdaiModel</code>
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public SdaiInputSource(SdaiModel model) throws SdaiException {
		setModel(model);
	}

	/**
	 * Creates a new input source of the <code>SdaiModel</code> aggregate.
	 *
	 * @param models The <code>SdaiModel</code> aggregate
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public SdaiInputSource(ASdaiModel models) throws SdaiException {
		setModels(models);
	}

	/**
	 * Sets <code>SdaiRepository</code> as the input source.
	 *
	 * @param repository The <code>SdaiRepository</code>
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public void setRepository(SdaiRepository repository)
	throws SdaiException {
		this.repository = repository;
		this.model = null;
		this.models = new ASdaiModel[] { repository.getModels() };
		this.schInstance = null;
		this.schInstances = repository.getSchemas();
	}

	/**
	 * Returns the <code>SdaiRepository</code> of this input source.
	 * The repository is set by {@link #SdaiInputSource(SdaiRepository)}
	 * or {@link #setRepository}.
	 *
	 * @return The repository on null if repository was not set
	 */
	public SdaiRepository getRepository() {
		return repository;
	}

	/**
	 * Sets <code>SchemaInstance</code> as the input source.
	 *
	 * @param schInstance The <code>SchemaInstance</code>
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public void setSchemaInstance(SchemaInstance schInstance) throws SdaiException {
		this.schInstance = schInstance;
		this.schInstances = null;
		this.model = null;
		this.models = new ASdaiModel[] { schInstance.getAssociatedModels() };
		this.repository = schInstance.getRepository();
	}

	/**
	 * Returns the <code>SchemaInstance</code> of this input source.
	 * The schema instance is set by {@link #SdaiInputSource(SchemaInstance)}
	 * or {@link #setSchemaInstance}.
	 *
	 * @return The schema instance on null if schema instance was not set
	 */
	public SchemaInstance getSchemaInstance() {
		return schInstance;
	}

	/**
	 * Sets aggregate of <code>SchemaInstances</code> as the input source.
	 *
	 * @param schInstances The <code>SchemaInstance</code> aggregate
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public void setSchemaInstances(ASchemaInstance schInstances) throws SdaiException {
		this.schInstances = schInstances;
		this.repository = null;
		this.model = null;
		this.models = new ASdaiModel[schInstances.getMemberCount()];
		SdaiIterator schInstIter = schInstances.createIterator();
		int modelIdx = 0;
		while(schInstIter.next()) {
			SchemaInstance currSchInst = schInstances.getCurrentMember(schInstIter);
			if(this.repository == null) {
				this.repository = currSchInst.getRepository();
			} else if(this.repository != currSchInst.getRepository()) {
				throw new SdaiException(SdaiException.RP_NEXS, 
										"All schema instances should belong to the same repository");
			}
			this.models[modelIdx++] = currSchInst.getAssociatedModels();
		}
	}

	/**
	 * Returns the <code>SchemaInstance</code> aggregate of this input source.
	 * The schema instance aggregate is set by {@link #SdaiInputSource(SdaiRepository)},
	 * {@link #SdaiInputSource(ASchemaInstance)}, {@link #setRepository},
	 * or {@link #setSchemaInstances}.
	 *
	 * @return The schema instance aggregate on null if schema instance aggregate was not set
	 */
	public ASchemaInstance getSchemaInstances() {
		return schInstances;
	}

	/**
	 * Sets <code>SdaiModel</code> as the input source.
	 *
	 * @param model The <code>SdaiModel</code>
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public void setModel(SdaiModel model) throws SdaiException {
		this.model = model;
		this.models = null;
		this.repository = model.getRepository();
		this.schInstance = null;
		this.schInstances = null;
	}

	/**
	 * Returns the <code>SdaiModel</code> of this input source.
	 * The model is set by {@link #SdaiInputSource(SdaiModel)}
	 * or {@link #setModel}.
	 *
	 * @return The model on null if model was not set
	 * @return a <code>SdaiModel</code> value
	 */
	public SdaiModel getModel() {
		return model;
	}

	/**
	 * Sets aggregate of <code>SdaiModel</code> as the input source.
	 *
	 * @param models The <code>SdaiModel</code> aggregate
	 * @exception SdaiException if an error occurs in underlying JSDAI operations
	 */
	public void setModels(ASdaiModel models) throws SdaiException {
		this.model = null;
		this.models = new ASdaiModel[] { models };
		SdaiIterator modelIter = models.createIterator();
		while(modelIter.next()) {
			SdaiModel currModel = models.getCurrentMember(modelIter);
			if(this.repository == null) {
				this.repository = currModel.getRepository();
			} else if(this.repository != currModel.getRepository()) {
				throw new SdaiException(SdaiException.RP_NEXS, 
										"All models should belong to the same repository");
			}
		}
		this.schInstance = null;
		this.schInstances = null;
	}

	/**
	 * Returns the <code>SdaiModel</code> aggregates of this input source.
	 * The model aggregates are set by {@link #SdaiInputSource(SdaiRepository)},
	 * {@link #SdaiInputSource(SchemaInstance)}, {@link #SdaiInputSource(ASchemaInstance)},
	 * {@link #SdaiInputSource(ASdaiModel)}, {@link #setRepository}, {@link #setSchemaInstance},
	 * {@link #setSchemaInstances} or {@link #setModels}.
	 *
	 * @return The array of model aggregates on null if model aggregates were not set
	 */
	public ASdaiModel[] getModels() {
		return models;
	}

} // SdaiInputSource
