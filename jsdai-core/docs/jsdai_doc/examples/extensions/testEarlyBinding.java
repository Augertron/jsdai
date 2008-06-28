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
public class testEarlyBinding {
	public static final void main (String args[])  throws SdaiException, Exception {
		if (args.length != 1) {
			System.out.println("USAGE: java testEarlyBinding existing_repo_name");
			return;
		}
		System.out.println("Test application of early binding mapping classes.");
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trx = session.startTransactionReadWriteAccess();
		SdaiRepository repo = session.linkRepository(args[0],null);
		repo.openRepository();
		ASdaiModel domain = repo.getModels();
		SdaiModel model = domain.getByIndex(1);

		model.startReadWriteAccess();

		AEntity instances = MItem.findInstances(domain, null);

		int no_of_instances = instances.getMemberCount();
		System.out.println("Number of received instances is "+no_of_instances);
		System.out.println(instances);
		int expectedRetType = -1;
		for (int i=1;i<=no_of_instances;i++) {
			EEntity instance = (EEntity) instances.getByIndexEntity(i);
			System.out.println("----------------------------------------");
			System.out.println("instance is "+instance);
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
				System.out.println("Version is "+version);
			}
		}
	}
}
