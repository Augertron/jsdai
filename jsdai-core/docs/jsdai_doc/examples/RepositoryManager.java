// RepositoryManager.java
// Copyright (c) LKSoftWare GmbH, 2000-2003. All Rights Reserved.
// This software is provided "AS IS," without any warranty

import jsdai.lang.*;
import jsdai.dictionary.*;

public class RepositoryManager {
	String schemaName;
	boolean fimportFromFile;
	String fileNameImport;
	boolean fexportToFile;
	String repositoryNameExport, fileNameExport, repositoryNameImport;
	boolean fdeleteRepository;
	String repositoryNameDel;
	boolean flistRepositories;
	boolean fprintRepository;
	String repositoryNamePrint;
	boolean fcreateRepository;
	String repositoryNameCreate, schemaNameCreate;
	boolean fprintLangVersion;
	String args[];
	String repositoryNameCreateModel, modelNameCreateModel, classNameCreateModel;
	boolean fcreateModel;
	String repositoryNameCreateSchema, schemaNameCreateSchema, classNameCreateSchema;
	boolean fcreateSchema;
	String repositoryNameDeleteModel, modelNameDeleteModel;
	boolean fdeleteModel;
	String repositoryNameDeleteSchema, schemaNameDeleteSchema;
	boolean fdeleteSchema;
	String repositoryNameAddModel, modelNameAddModel, schemaNameAddModel;
	boolean faddModel;
	String repositoryNameRemoveModel, modelNameRemoveModel, schemaNameRemoveModel;
	boolean fremoveModel;
	SdaiSession session;
	SdaiTransaction transaction;

	static final String REPS[] = {"TheRepository", "Basic"};

	public static void main(String args[]) {
		RepositoryManager mtc = new RepositoryManager(args);
		mtc.run();
		}

	public RepositoryManager(String a[]) {
		args = a;
		}

	public void run() {
		try {
			if (!testParameters(args)) {
				return;
				}
			session = SdaiSession.openSession();
			transaction = session.startTransactionReadWriteAccess();
			if (fimportFromFile) {
				importFromFile();
				}
			if (fexportToFile) {
				exportToFile();
				}
			if (fdeleteRepository) {
				deleteRepository();
				}
			if (flistRepositories) {
				listRepositories();
				}
			if (fcreateRepository) {
				createRepository();
				}
			if (fprintLangVersion) {
				printLangVersion();
				}
			if (fcreateModel) {
				createModel();
				}
			if (fdeleteModel) {
				deleteModel();
				}
			if (fcreateSchema) {
				createSchema();
				}
			if (fdeleteSchema) {
				deleteSchema();
				}
			if (faddModel) {
				addModel();
				}
			if (fremoveModel) {
				removeModel();
				}
			transaction.commit();
			}
		catch (Throwable e) {
			e.printStackTrace();
			return;
			}
		}

	public boolean testParameters(String args[]) {
		boolean printHelp = false;
		boolean printParam = false;
		int i;
		for (i = 0; i < args.length; i++) {
			if (args[i].equals("-h") || args[i].equals("-?")) {
				printHelp = true;
				}
			else {
			if (args[i].equals("-s")) {
				printParam = true;
				}
			else {
			if (args[i].equals("-l")) {
				flistRepositories = true;
				}
			else {
			if (args[i].equals("-v")) {
				fprintLangVersion = true;
				}
			else {
			if (args[i].equals("-d")) {
				if (i + 1 < args.length) {
					if (repositoryNameDel != null) {
						System.out.println("Warning: Repository name redefinition \"" + args[i + 1] + "\".");
						}
					repositoryNameDel = args[i+1];
					fdeleteRepository = true;
					i++;
					}
				else {
					System.out.println("Repository name must be specified after key -d.");
					return false;
					}
				}
			else {
			if (args[i].equals("-i")) {
				if (i + 1 >= args.length) {
					System.out.println("Physical file location must be specified after key -i.");
					return false;
				}
				int increase = 0;
				if (i + 1 < args.length) {
					if (fileNameImport != null) {
						System.out.println("Warning: Import physical file location redefinition \"" + args[i + 1] + "\".");
					}
					fileNameImport = args[i+1];
					increase++;
				}
				if (i + 2 < args.length) {
					if (repositoryNameImport != null) {
						System.out.println("Warning: Import repository name redefinition \"" + args[i + 2] + "\".");
					}
					repositoryNameImport = args[i+2];
					increase++;
				}
				fimportFromFile = true;
				i += increase;
			} 
/*			else {
			if (args[i].equals("-e")) {
				if (i + 2 < args.length) {
					if (repositoryNameExport != null) {
						System.out.println("Warning: Export repository name redefinition \"" + args[i + 1] + "\".");
						}
					if (fileNameExport != null) {
						System.out.println("Warning: Export file name redefinition \"" + args[i + 2] + "\".");
						}
					repositoryNameExport = args[i+1];
					fileNameExport = args[i+2];
					fexportToFile = true;
					i += 2;
					}
				else {
					System.out.println("Repository and file name must be specified after key -e.");
					return false;
					}
				}
*/
			else {
			if (args[i].equals("-c")) {
				if (i + 1 < args.length) {
					if (repositoryNameCreate != null) {
						System.out.println("Warning: Create repository name redefinition \"" + args[i + 1] + "\".");
						}
					repositoryNameCreate = args[i+1];
					fcreateRepository = true;
					i++;
					}
				else {
					System.out.println("Repository name must be specified after key -c.");
					return false;
					}
				}
			else {
			if (args[i].equals("-cm")) {
				if (i + 3 < args.length) {
					if (repositoryNameCreateModel != null) {
						System.out.println("Warning: Create model, repository name redefinition \"" + args[i + 1] + "\".");
						}
					if (modelNameCreateModel != null) {
						System.out.println("Warning: Create model, model name redefinition \"" + args[i + 2] + "\".");
						}
					if (classNameCreateModel != null) {
						System.out.println("Warning: Create model, schema class name redefinition \"" + args[i + 3] + "\".");
						}
					repositoryNameCreateModel = args[i+1];
					modelNameCreateModel = args[i+2];
					classNameCreateModel = args[i+3];
					fcreateModel = true;
					i += 3;
					}
				else {
					System.out.println("Repository, model name and express schema class name must be specified after key -cm.");
					return false;
					}
				}
/*			else {
			if (args[i].equals("-cs")) {
				if (i + 3 < args.length) {
					if (repositoryNameCreateSchema != null) {
						System.out.println("Warning: Create schema instance, repository name redefinition \"" + args[i + 1] + "\".");
						}
					if (schemaNameCreateSchema != null) {
						System.out.println("Warning: Create schema instance, schema name redefinition \"" + args[i + 2] + "\".");
						}
					if (classNameCreateSchema != null) {
						System.out.println("Warning: Create schema instance, express schema class name redefinition \"" + args[i + 3] + "\".");
						}
					repositoryNameCreateSchema = args[i+1];
					schemaNameCreateSchema = args[i+2];
					classNameCreateSchema = args[i+3];
					fcreateSchema = true;
					i += 3;
					}
				else {
					System.out.println("Repository, model and express schema class name must be specified after key -cm.");
					return false;
					}
				}
*/
			else {
			if (args[i].equals("-dm")) {
				if (i + 2 < args.length) {
					if (repositoryNameDeleteModel != null) {
						System.out.println("Warning: Delete model, repository name redefinition \"" + args[i + 1] + "\".");
						}
					if (modelNameDeleteModel != null) {
						System.out.println("Warning: Delete model, model name redefinition \"" + args[i + 2] + "\".");
						}
					repositoryNameDeleteModel = args[i+1];
					modelNameDeleteModel = args[i+2];
					fdeleteModel = true;
					i += 2;
					}
				else {
					System.out.println("Repository and model name must be specified after key -dm.");
					return false;
					}
				}
/*			else {
			if (args[i].equals("-ds")) {
				if (i + 2 < args.length) {
					if (repositoryNameDeleteSchema != null) {
						System.out.println("Warning: Delete schema instance, repository name redefinition \"" + args[i + 1] + "\".");
						}
					if (schemaNameDeleteSchema != null) {
						System.out.println("Warning: Delete schema instance, schema name redefinition \"" + args[i + 2] + "\".");
						}
					repositoryNameDeleteSchema = args[i+1];
					schemaNameDeleteSchema = args[i+2];
					fdeleteSchema = true;
					i += 2;
					}
				else {
					System.out.println("Repository and model name must be specified after key -dm.");
					return false;
					}
				}
			else {
			if (args[i].equals("-am")) {
				if (i + 3 < args.length) {
					if (repositoryNameAddModel != null) {
						System.out.println("Warning: Add model, repository name redefinition \"" + args[i + 1] + "\".");
						}
					if (schemaNameAddModel != null) {
						System.out.println("Warning: Add model, schema name redefinition \"" + args[i + 2] + "\".");
						}
					if (modelNameAddModel != null) {
						System.out.println("Warning: Add model, model name redefinition \"" + args[i + 3] + "\".");
						}
					repositoryNameAddModel = args[i+1];
					schemaNameAddModel = args[i+2];
					modelNameAddModel = args[i+3];
					faddModel = true;
					i += 3;
					}
				else {
					System.out.println("Repository, model name and schema instance name must be specified after key -am.");
					return false;
					}
				}
			else {
			if (args[i].equals("-rm")) {
				if (i + 3 < args.length) {
					if (repositoryNameRemoveModel != null) {
						System.out.println("Warning: Remove model, repository name redefinition \"" + args[i + 1] + "\".");
						}
					if (schemaNameRemoveModel != null) {
						System.out.println("Warning: Remove model, schema instance name redefinition \"" + args[i + 2] + "\".");
						}
					if (modelNameRemoveModel != null) {
						System.out.println("Warning: Remove model, model name redefinition \"" + args[i + 3] + "\".");
						}
					repositoryNameRemoveModel = args[i+1];
					schemaNameRemoveModel = args[i+2];
					modelNameRemoveModel = args[i+3];
					fremoveModel = true;
					i += 3;
					}
				else {
					System.out.println("Repository, model and schema instance name must be specified after key -rm.");
					return false;
					}
				}
			else {
			if (args[i].equals("-p")) {
				if (i + 1 < args.length) {
					if (repositoryNamePrint != null) {
						System.out.println("Warning: Repository name redefinition \"" + args[i + 1] + "\".");
						}
					repositoryNamePrint = args[i+1];
					fprintRepository = true;
					i++;
					}
				else {
					System.out.println("Repository name must be specified after key -p.");
					return false;
					}
				}*/
			else {
				System.out.println("Warning: Urecognized parameter \"" + args[i] + "\".");
				}}}}}}}}} // fun isn't
			}
		boolean rv = true;
		if (!(fimportFromFile || fexportToFile || fdeleteRepository || flistRepositories || fprintRepository || fcreateRepository || fprintLangVersion || fcreateModel || fdeleteModel || fcreateSchema || fdeleteSchema || faddModel || fremoveModel)) {
			rv = false;
			printHelp = true;
			}
		if (printHelp) {
			printHelp();
			}
		if (printParam) {
			printParameters();
			}
		return rv;
		}

	public void printParameters() {
		for (int i = 0; i < args.length; i++) {
			System.out.print(i + ")" + args[i] + " ");
			}
		System.out.println();
		if (fimportFromFile) {
			System.out.println("Import file name=" + fileNameImport);
			}
		if (fexportToFile) {
			System.out.println("Export repositry and file names=" + repositoryNameExport + " " + fileNameExport);
			}
		if (fdeleteRepository) {
			System.out.println("Repository to delete=" + repositoryNameDel);
			}
		if (flistRepositories) {
			System.out.println("List repositories.");
			}
		if (fprintRepository) {
			System.out.println("Repository " + repositoryNamePrint + " contains.");
			}
		if (fprintLangVersion) {
			System.out.println("Print lang version.");
			}
		if (fcreateRepository) {
			System.out.println("Create empty repository " + repositoryNamePrint + ".");
			}
		if (fcreateModel) {
			System.out.println("Create model, repository=" + repositoryNameCreateModel + " model=" + modelNameCreateModel + " express schema=" + classNameCreateModel);
			}
		if (fdeleteModel) {
			System.out.println("Delete model, repository=" + repositoryNameDeleteModel + " model=" + modelNameDeleteModel);
			}
		if (fcreateSchema) {
			System.out.println("Create schema, repository=" + repositoryNameCreateSchema + " schema=" + schemaNameCreateSchema + " express schema=" + classNameCreateSchema);
			}
		if (fdeleteSchema) {
			System.out.println("Delete schema, repository=" + repositoryNameDeleteSchema + " schema=" + schemaNameDeleteSchema);
			}
		if (faddModel) {
			System.out.println("Add model, repository=" + repositoryNameAddModel + " model=" + modelNameAddModel + " schema=" + schemaNameAddModel);
			}
		if (fremoveModel) {
			System.out.println("Remove model, repository=" + repositoryNameRemoveModel + " model=" + modelNameRemoveModel + " schema=" + schemaNameRemoveModel);
			}
		}

   public void printHelp() {
		System.out.println("Repository manager usage: java RepositoryManager [options]");
		System.out.println("Where options include:");
		System.out.println("	-h | -?		Get this help.");
		System.out.println("	-s		Show interpretation of parameters.");
		System.out.println("	-l		List all repositories.");
		System.out.println("	-v		Show sdai version.");
//		System.out.println("	-p <repository>	Print repository contents.");
		System.out.println("	-c <repository>	Creates empty repository.");
		System.out.println("	-d <repository>	Delete repository.");
		System.out.println("	-i <file>	Create new repository by importing physical file.");
//		System.out.println("	-e <repository> <file>	Exprot data from repository to physical file.");
		System.out.println("	-cm <repository> <model> <schema class name>	Creates emty model.");
//		System.out.println("	-cs <repository> <schema_instance> <schema class name>	Creates emty schmea instance.");
		System.out.println("	-dm <repository> <model>	Delete model.");
//		System.out.println("	-ds <repository> <schema_instance>	Delete schema instance.");
//		System.out.println("	-am <repository> <schema_instance> <model>	Add model to schema instance.");
//		System.out.println("	-rm <repository> <schema_instance> <model>	Remove model to schema instance.");
		System.out.println("Set repository location by java key 'sdai.repos', by default it is set to current directory.");
		}

	public void importFromFile() {
		try {
			System.out.println("Importing repository from file " + fileNameImport);
//			SdaiRepository rep = session.importClearTextEncoding(fileNameImport, null, null);
			SdaiRepository rep = session.importClearTextEncoding(repositoryNameImport, fileNameImport, null);
			ASdaiModel models = rep.getModels();
			SdaiIterator i = models.createIterator();
			while(i.next()) {
				SdaiModel m = models.getCurrentMember(i);
				if (m.getMode() == SdaiModel.NO_ACCESS) {
					m.startReadOnlyAccess();
				}
			}
		}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void exportToFile() {
		try {
			System.out.println("Exporting repository " + repositoryNameExport + " to file " + fileNameExport);
			SdaiRepository rep = session.linkRepository(repositoryNameExport, null);
			rep.openRepository();
			ASdaiModel models = rep.getModels();
			SdaiIterator i = models.createIterator();
			while(i.next()) {
				SdaiModel m = models.getCurrentMember(i);
				m.startReadOnlyAccess();
				}
			rep.exportClearTextEncoding(fileNameExport);
			//rep.close();
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void deleteRepository() {
		try {
			System.out.println("Deleting repository " + repositoryNameDel);
			SdaiRepository rep = session.linkRepository(repositoryNameDel, null);
			rep.openRepository();
			/*
			ASdaiModel models = rep.getModels();
			SdaiIterator i = models.createIterator();
			while(i.next()) {
				SdaiModel m = models.getCurrentMember(i);
				m.startReadOnlyAccess();
				}
			*/
			rep.deleteRepository();
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void listRepositories() {
		try {
			System.out.println("List of repositories");
			ASdaiRepository repos = session.getKnownServers();
			SdaiIterator i2 = repos.createIterator();
			repositoriesLoop: while (i2.next()) {
				SdaiRepository r = repos.getCurrentMember(i2);
				for (int i = 0; i < REPS.length; i++) {
					if (r.getName().equals(REPS[i])) {
						System.out.println("Standard repository: " + r.getName() );
						continue repositoriesLoop;
						}
					}
				System.out.println("Repository: " + r.getName() );
				try {
					r.openRepository();
					ASdaiModel models = r.getModels();
					SdaiIterator im = models.createIterator();
					while (im.next()) {
						SdaiModel m = models.getCurrentMember(im);
						System.out.print("Model: " + m.getName() );
						ESchema_definition sd = m.getUnderlyingSchema();
						System.out.println(" schema=" + m.getName() );
						}
					ASchemaInstance sis = r.getSchemas();
					SdaiIterator is = sis.createIterator();
					while (is.next()) {
						SchemaInstance si = sis.getCurrentMember(is);
						System.out.print("SchemaInstance: " + si.getName() );
						ESchema_definition sd = si.getNativeSchema();
						System.out.println(" schema=" + sd.getName(null) );
						}
					r.closeRepository();
					}
				catch (SdaiException e) {
					// may be already opened
					}
				}
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void printRepository() {
		try {
			System.out.println("Printing repository " + repositoryNamePrint);
			SdaiRepository rep = session.linkRepository(repositoryNamePrint, null);
			rep.openRepository();
			ASdaiModel models = rep.getModels();
			SdaiIterator i = models.createIterator();
			while(i.next()) {
				SdaiModel m = models.getCurrentMember(i);
				m.startReadOnlyAccess();
				System.out.println("Model " + m.getName());
				Aggregate ae = m.getInstances();
				SdaiIterator ii = ae.createIterator();
				while(ii.next()) {
					EEntity be = (EEntity)ae.getCurrentMemberObject(ii);
					System.out.println(be.toString());
					}
				}
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void createRepository() {
		try {
			System.out.println("Creating repository " + repositoryNameCreate);
			SdaiRepository rep = session.createRepository(repositoryNameCreate, null);
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void printLangVersion() {
		try {
			System.out.println("Lang implementation version.");
   		Implementation imp = session.getSdaiImplementation();
			System.out.println("Implementation name: " +  imp.getName());
			System.out.println("Implementation level: " + imp.getLevel());
			System.out.println("Sdai version: " + imp.getSdaiVersion());
			System.out.println("Binding version: " + imp.getBindingVersion());
			System.out.println("Implementation class: " + imp.getImplementationClass());
			System.out.println("Transaction level: " + imp.getTransactionLevel());
			System.out.println("Expression level: " + imp.getExpressionLevel());
			System.out.println("Domain equivalence level: " + imp.getDomainEquivalenceLevel());
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void createModel() {
		try {
			System.out.println("Creating model " + modelNameCreateModel);
			SdaiRepository rep = session.linkRepository(repositoryNameCreateModel, null);
			rep.openRepository();
			Class sc = Class.forName(classNameCreateModel);
			SdaiModel model = rep.createSdaiModel(modelNameCreateModel, sc);
			model.startReadWriteAccess();
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			}
		}

	public void deleteModel() {
		try {
			System.out.println("Deleting model " + modelNameDeleteModel);
			SdaiRepository rep = session.linkRepository(repositoryNameDeleteModel, null);
			rep.openRepository();
			ASdaiModel models = rep.getModels();
			SdaiIterator i = models.createIterator();
			while(i.next()) {
				SdaiModel model = models.getCurrentMember(i);
				model.startReadWriteAccess();
				if (model.getName().equals(modelNameDeleteModel)) {
					//rep.deleteModel(model);
					model.deleteSdaiModel();
					}
				}
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void createSchema() {
		try {
			System.out.println("Creating schema " + repositoryNamePrint);
			SdaiRepository rep = session.linkRepository(repositoryNameCreateSchema, null);
			rep.openRepository();
			Class sc = Class.forName(classNameCreateSchema);
			SchemaInstance schema = rep.createSchemaInstance(schemaNameCreateSchema, sc);
			//schema.startReadWriteAccess();
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			}
		}

	public void deleteSchema() {
		try {
			System.out.println("Deleting schema " + schemaNameDeleteSchema);
			SdaiRepository rep = session.linkRepository(repositoryNameDeleteSchema, null);
			rep.openRepository();
			ASchemaInstance schemas = rep.getSchemas();
			SdaiIterator i = schemas.createIterator();
			while(i.next()) {
				SchemaInstance schema = schemas.getCurrentMember(i);
				//schema.startReadWriteAccess();
				if (schema.getName().equals(schemaNameDeleteSchema)) {
					//rep.deleteSchema(schema);
					schema.delete();
					}
				}
			}
		catch(SdaiException e) {
			e.printStackTrace();
			}
		}

	public void addModel() {
		}

	public void removeModel() {
		}
	}
