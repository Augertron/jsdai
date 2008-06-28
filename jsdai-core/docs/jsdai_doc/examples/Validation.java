// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty

// This simple extension of the java.awt.Frame	class
// contains all the elements necessary to act as the
// main window of an application.

import java.io.*;
import jsdai.lang.*;
import jsdai.dictionary.*;

public class Validation {

	AAttribute aat1 = new AAttribute();
	AAttribute aat2 = new AAttribute();
	AAttribute aat3 = new AAttribute();
	AAttribute aat4 = new AAttribute();
	AAttribute aat5 = new AAttribute();
	SdaiIterator iterator;

	private void listAttributes(AAttribute aat, String message) throws SdaiException {
		if (iterator == null) {
			iterator = aat1.createIterator();
		}
		aat.attachIterator(iterator);
		while (iterator.next()){
			EAttribute eat = (EAttribute)aat.getCurrentMemberObject(iterator);
			System.out.println(" \t attribute \"" + eat.getName(null) + "\" " + message);
		}
	}

	boolean validateInstance(EEntity instance) throws SdaiException {
		//aat1.clear();
		//aat2.clear();
		//aat3.clear();
		//aat4.clear();
		//aat5.clear();
		AAttribute aat1 = new AAttribute();
		AAttribute aat2 = new AAttribute();
		AAttribute aat3 = new AAttribute();
		AAttribute aat4 = new AAttribute();
		AAttribute aat5 = new AAttribute();

		if ( (!instance.validateRequiredExplicitAttributesAssigned(aat1))
		  || (instance.validateExplicitAttributesReferences(aat2)==0 )
		  || (instance.validateAggregatesSize(aat3)==0)
		  || (!instance.validateAggregatesUniqueness(aat4))
		  || (!instance.validateArrayNotOptional(aat5))) {
			PrintWriter pw = SdaiSession.getLogWriter();
			pw.println(instance.toString());
			listAttributes(aat1, "value not set");
			listAttributes(aat2, "reference not set");
			listAttributes(aat3, "aggregate size do not meet constraints");
			listAttributes(aat4, "aggregate uniqueness is not satisfied");
			listAttributes(aat5, "aggregate has an optional instances");
			return false;
		} else {
			return true;
		}
	}

	// returns no. of instances with errors
	int validateInstances(Aggregate instances) throws SdaiException {
		int errors = 0;
		SdaiIterator iterator = instances.createIterator();
		while (iterator.next()){
			EEntity instance = (EEntity)instances.getCurrentMemberObject(iterator);
			if (!validateInstance(instance)) {
				errors++;
			}
		}
		return errors;
	}

	static public void main(String argv[]) throws SdaiException {
		System.out.println("Validate test example. Copyright 1999-2003, LKSoftWare GmbH");
		if (argv.length != 1) {
			System.out.println("USAGE: java Validate p21_file");
			return;
		}
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadWriteAccess();

		SdaiRepository repo = session.importClearTextEncoding("", argv[0], null);

		Validation validation = new Validation();

		ASdaiModel models = repo.getModels();
		SdaiIterator it1 = models.createIterator();
		while (it1.next()) {
			SdaiModel model = models.getCurrentMember(it1);
			Aggregate instances = model.getInstances();
			System.out.println("Testing model \"" + model.getName() + "\"");
			System.out.println("no. of instances: " + instances.getMemberCount());
			int errors = validation.validateInstances(instances);
			System.out.println("no. of erroneous instances: " + errors);
		 }

		trans.endTransactionAccessAbort();
		repo.closeRepository();
		repo.deleteRepository();
		session.closeSession();
	}
}
