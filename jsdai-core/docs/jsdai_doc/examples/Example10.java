// Example10.java
// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty

//  Simple SDAI application program.
//  Reads an ap203 physical file,
//  prints instances of product entity, finds and prints their users
//  product_definition_formation_with_specified_source entity instances

import jsdai.lang.*;
import jsdai.SConfig_control_design.*;
import jsdai.SProduct_definition_schema.*;
import jsdai.SApplication_context_schema.*;
public class Example10 {
	public static final void main(String argv[]) throws SdaiException {

		// first open a session and specify the desired AP
		SdaiSession session = SdaiSession.openSession();

		// start a read/write transaction to allow importClearTextEncoding
		SdaiTransaction transaction = session.startTransactionReadWriteAccess();

		// import a physical file into a new repository
		SdaiRepository repo = session.importClearTextEncoding(null, argv[0], null);
	
		// SdaiRepository is already open after importClearTextEncoding
		// repo.openRepository();

		// find the only one SdaiModel in it (data_section)
		ASdaiModel models = repo.getModels();
		SdaiIterator it_models = models.createIterator();
		if (it_models.next()) {
			SdaiModel model = models.getCurrentMember(it_models);

			// SdaiModel is already in read/write access after importClearTextEncoding
			// model.startReadOnlyAccess();

			System.out.println("Model <" + model.getName() + "> found");

			// find all instances of entity type "product"
			AProduct products = (AProduct) model.getInstances(EProduct.class);

			// List all products(id, name, description)
			System.out.println("");
			System.out.println("Instances of entity \"product\": ");
			SdaiIterator it_products = products.createIterator();
			while (it_products.next()) {
				EProduct product = products.getCurrentMember(it_products);
				System.out.println("");
				System.out.println("INSTANCE: " + product.getPersistentLabel());
				System.out.print("id: " + product.getId(null));
				System.out.print(", name: " + product.getName(null));
				System.out.println(", description: " + (product.testDescription(null) ? product.getDescription(null) : "$"));

				// List the discipline types the product belongs to
				AProduct_context pc_set = product.getFrame_of_reference(null);
				System.out.println("\tframe_of_reference - SET of " + pc_set.getMemberCount() + ":");
				SdaiIterator it_pc = pc_set.createIterator();
				while (it_pc.next()) {
					EProduct_context pc = pc_set.getCurrentMember(it_pc);
					System.out.print("\tname: " + pc.getName(null));
					System.out.println(", discipline_type: " + pc.getDiscipline_type(null));
				}

				// follow the implicit inverse relation "of_product" to get
				//  all product_definition_formations of the product
				AProduct_definition_formation formations =
					new AProduct_definition_formation();
				CProduct_definition_formation.usedinOf_product(null, product, null, formations);

				// List all product_definition_formations of the product
				SdaiIterator iter_formations =  formations.createIterator();
				while (iter_formations.next()) {
					EProduct_definition_formation formation = (EProduct_definition_formation)formations.getCurrentMember(iter_formations);
					System.out.println("product user: product_definition_formation instance " + formation.getPersistentLabel());
					System.out.print("\tID: " + formation.getId(null));
					System.out.print(", description: " + (formation.testDescription(null) ? formation.getDescription(null) : "$");

					// mention the specified_source if available
					if (formation.isKindOf(CProduct_definition_formation_with_specified_source.class)) {
						EProduct_definition_formation_with_specified_source fwss =
						  (EProduct_definition_formation_with_specified_source) formation;
						System.out.println(", make_or_buy: " + fwss.getMake_or_buy(null));
					} else {
						System.out.println(", no specified_source available");
					}
				}
			}

			// read/write access or read-only access to SdaiModel is ended with closeRepository()
			// model.endReadWriteAccess();
			// model.endReadOnlyAccess();
		} else {
			System.out.println("error: No SdaiModel found");
		}

		transaction.endTransactionAccessCommit();
		repo.closeRepository();
		repo.deleteRepository();

		System.out.println();
		System.out.println("Done");
		session.closeSession();
	}
}
