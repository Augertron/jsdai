// Example11.java
// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty

//  Simple SDAI application program.
//  Generates a very basic ap203 population and
//  writes it out into a p21 file.

import java.io.*;
import jsdai.lang.*;
import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.*;
import jsdai.SApplication_context_schema.*;
import jsdai.SIda_step_aim_schema.SIda_step_aim_schema;
import jsdai.SProduct_definition_schema.*;

public class Example11 {
	public static final void main(String argv[]) throws SdaiException {

		if (argv == null || argv.length != 1) {
			System.out.println("usage:");
			System.out.println("  Example11 output_file");
			return;
		}

		// redirect the J-SDAI system log to System.out
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));

		// first open a session and specify the desired AP
		SdaiSession session = SdaiSession.openSession();

		// start a read/write transaction to allow importClearTextEncoding
		SdaiTransaction transaction = session.startTransactionReadWriteAccess();

		SdaiRepository repo = session.createRepository("", null);
		repo.openRepository();
		A_string descriptions = repo.getDescription();
		descriptions.addByIndex(1, "Examp1e program to generate a very basic AP203 p21 file");
		A_string authors = repo.getAuthor();
		authors.addByIndex(1, "Lothar Klein");
		A_string organizations = repo.getOrganization();
		organizations.addByIndex(1, "LKSoftWare GmbH");
		repo.setOriginatingSystem(session.getSdaiImplementation().getName() + " " +
		                           session.getSdaiImplementation().getLevel() );
		repo.setAuthorization("Lothar Klein");

		SdaiModel model = repo.createSdaiModel("Model1", SIda_step_aim_schema.class);
		model.startReadWriteAccess();

		EApplication_context app_context = (EApplication_context)
		  model.createEntityInstance(CApplication_context.class);
		app_context.setApplication(null, "CONFIGURATION MANAGEMENT");

		EApplication_protocol_definition app_protocol = (EApplication_protocol_definition)
		  model.createEntityInstance(CApplication_protocol_definition.class);
		app_protocol.setStatus(null, "INTERNATIONAL STANDARD");
		app_protocol.setApplication_interpreted_model_schema_name(null, "CONFIG_CONTROL_DESIGN");
		app_protocol.setApplication_protocol_year(null, 1994);
		app_protocol.setApplication(null, app_context);

		EMechanical_context mechanical = (EMechanical_context)
		  model.createEntityInstance(CMechanical_context.class);
		mechanical.setName(null, "CONFIGURATION CONTROL DESIGN");
		mechanical.setFrame_of_reference(null, app_context);
		mechanical.setDiscipline_type(null, "MECHANICAL");

		EProduct product = (EProduct) model.createEntityInstance(CProduct.class);
		product.setId(null, "TestId");
		product.setName(null, "TestName");
		product.setDescription(null, "TestDescription");
		AProduct_context contexts = product.createFrame_of_reference(null);
		contexts.addUnordered(mechanical);

		repo.exportClearTextEncoding(argv[0]);

		transaction.endTransactionAccessAbort();
		repo.closeRepository();
		repo.deleteRepository();

		System.out.println();
		System.out.println("Done");
		session.closeSession();
	}
}
