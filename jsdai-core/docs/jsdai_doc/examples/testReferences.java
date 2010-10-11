import jsdai.SAp203_configuration_controlled_3d_design_of_mechanical_parts_and_assemblies_mim.*;
import jsdai.SContract_schema.*;
import jsdai.SIda_step_aim_schema.SIda_step_aim_schema;

import jsdai.lang.*;

public class testReferences {
   public static final void main(String argv[]) throws  SdaiException, Exception {

   	SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadWriteAccess();

  		// make sure we are not creating repository which already exists
  		// delete any old repositories with 'testRef' name
   	ASdaiRepository allRepos = session.getKnownServers();
   	SdaiIterator it = allRepos.createIterator();
   	SdaiRepository repo = null;
   	while (it.next()) {
   		SdaiRepository r = allRepos.getCurrentMember(it);
   		if (r.getName().equalsIgnoreCase("testRef")) {
   			r.deleteRepository();
   			break;
   		}
   	}
		repo = session.importClearTextEncoding("testRef", "example.stp", null);
		//  repo.openRepository() -- already open after import

		// now, repository contains 1 model. Get it.
		ASdaiModel models = repo.getModels();
		SdaiModel oldModel = models.getByIndex(1);
		//oldModel.startReadWriteAccess(); // already open after import
		// Add one more model
		SdaiModel newModel = repo.createSdaiModel("testModel", SIda_step_aim_schema.class);
		newModel.startReadWriteAccess();
		// great. Add several instances to new model:
		EContract contract = (EContract) newModel.createEntityInstance(CContract.class);
		EContract_type contractType = (EContract_type) newModel.createEntityInstance(CContract_type.class);

		// NOTE: we are using a number of setXX methods, where first parameter is null.
		// The first parameter may be used to diferentiate between overloaded methods,
		// when we have redeclared attribute.

		// set properties of new instances:
		contract.setName(null, "c1");
		contract.setPurpose(null, "purpose of c1");
		contract.setKind(null,  contractType);
		contractType.setDescription(null, "type for c1");
		// now, let's add new instance to imported model
		ECc_design_contract designContract = (ECc_design_contract) oldModel.createEntityInstance(CCc_design_contract.class);
		// link together:
		designContract.setAssigned_contract(null, contract);

 		// now, tell to export this repository to file:
 		repo.exportClearTextEncoding("result.stp");

/*
		// NOTE: before uncommenting the following lines, please run Stepbook
		// and try to open this new repository.
		// now, add schema instance:
		SchemaInstance schInstance = repo.createSchemaInstance("schema1", SConfig_control_design.class);
		// link schema instance and models together:
		schInstance.addSdaiModel(oldModel);
		schInstance.addSdaiModel(newModel);
*/
   	// commit changes afterwards
   	trans.commit();


   }

}
