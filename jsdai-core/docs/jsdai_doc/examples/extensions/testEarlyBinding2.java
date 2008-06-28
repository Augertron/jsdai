import jsdai.lang.*;
import jsdai.dictionary.*;
import jsdai.mapping.*;
import jsdai.MAp214.*;
import jsdai.SProduct_definition_schema.*;

/**
*	This example demonstrates usage of early binding mapping classes.
*	Given repository is opened, for each item (ARM entity!) is printed out:
*	name and all versions (again, ARM entity!) for that item are found 
*	and printed out.
*
*/
public class testEarlyBinding2 {
	public static final void main (String args[])  throws SdaiException, Exception {
		if (args.length != 1) {
			System.out.println("USAGE: java testEarlyBinding existing_repo_name");
			return;
		}
		System.out.println("Test application of early binding mapping classes.");
		SdaiSession session = SdaiSession.openSession();

		SdaiTransaction trx = session.startTransactionReadWriteAccess();		
		// import a physical file into a new temporary repository
		SdaiRepository repo = session.importClearTextEncoding("", args[0], null);

		ASdaiModel domain = repo.getModels();
		SdaiModel model = domain.getByIndex(1);

		AEntity instances = MItem.findInstances(domain, null);

		int no_of_instances = instances.getMemberCount();
		System.out.println("Number of received instances is "+no_of_instances);
		System.out.println(instances);
		int expectedRetType = -1;
		for (int i=1;i<=no_of_instances;i++) {
			EEntity instance = (EEntity) instances.getByIndexEntity(i);
			System.out.println("----------------------------------------");
			 // print out the id of instance
 			String id = MItem.getId(domain, instance, (String) null);
 			System.out.println("ID is "+ id);
					
			// print out the name of instance
			expectedRetType = MItem.testName(domain, instance);
			switch (expectedRetType) {
				case MItem.NAME__STRING:
					//put appropriate call to getXX method here.
					String name = MItem.getName(domain, instance, (String) null);
					System.out.println("Name is "+ name);
				break;
			default:
				System.out.println("Name attribute not set!");
			}

			// now, find all item_version's for this item:
			System.out.println("Known versions for this item are: ");
			AEntity allVersions = MItem_version.usedinAssociated_item(domain, instance);
			SdaiIterator it = allVersions.createIterator();
			while (it.next()) {
				EEntity version = (EEntity) allVersions.getCurrentMemberObject(it);
				// print out the id of item_version instance
				System.out.println("Version ID is "+ MItem_version.getId(domain, version, (String) null));
				
				// now, let us print out all known views of this item_version:
				System.out.println("\tKnown views on this version are:");
				AEntity allVersionViews = MDesign_discipline_item_definition.usedinAssociated_item_version(domain, version);
				for (int j=1;j<=allVersionViews.getMemberCount();j++) {
					EEntity versionView = allVersionViews.getByIndexEntity(j);
					// let us print out id for this instance
					expectedRetType = MDesign_discipline_item_definition.testId(domain, versionView);
					switch (expectedRetType) {
						case MDesign_discipline_item_definition.ID__STRING:
							//put appropriate call to getXX method here.
							String vID = MDesign_discipline_item_definition.getId(domain, versionView, (String) null);
							System.out.println("\t\tID of view on version is "+ vID);
						break;
					default:
						System.out.println("\t\tID attribute of view on version is not set!");
					}
					// now, let's try to recognize this view as more specific: 
					if (MAssembly_definition.is(domain, versionView, null)) {
						System.out.println("\t\tView was recognized as assembly_definition.");
						continue;
					}
					if (MCollection_definition.is(domain, versionView, null)) {
						System.out.println("\t\tView was recognized as collection_definition.");
						continue;
					}
					if (MMating_definition.is(domain, versionView, null)) {
						System.out.println("\t\tView was recognized as mating_definition.");
						continue;
					}
					if (MProcess_state.is(domain, versionView, null)) {
						System.out.println("\t\tView was recognized as process_state.");
						continue;
					}
					// none of the cases above was true, so we have just a ddid instance.
				}
			}
		}
		trx.endTransactionAccessCommit();
		repo.closeRepository();
		repo.deleteRepository();

		System.out.println();
		System.out.println("Done");
		session.closeSession();
	}
}
