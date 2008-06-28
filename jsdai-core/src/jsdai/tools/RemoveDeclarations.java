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

package jsdai.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jsdai.SExtended_dictionary_schema.AEntity_definition;
import jsdai.SExtended_dictionary_schema.AEntity_or_view_definition;
import jsdai.SExtended_dictionary_schema.AInterfaced_declaration;
import jsdai.SExtended_dictionary_schema.EEntity_definition;
import jsdai.SExtended_dictionary_schema.EInterfaced_declaration;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;

/**
 * RemoveDeclarations.java
 *
 *
 * Created: Thu Apr 14 16:52:10 2005
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public final class RemoveDeclarations {
	private static final String DICTIONARY_DATA_SUFFIX = "_DICTIONARY_DATA";
	private static final int DICTIONARY_DATA_SUFFIX_LEN = DICTIONARY_DATA_SUFFIX.length();

    /** Holds session we are using
     */    
    private SdaiSession sdaiSession;

	private SdaiRepository repository;
    
    /** Express Compiler Repository name
     */    
    protected static final String repositoryName = "ExpressCompilerRepo";

	public RemoveDeclarations(SdaiSession sdaiSession, SdaiRepository repository) {
        this.sdaiSession = sdaiSession;
		this.repository = repository;
    }
	
	public static void main(String[] args) throws SdaiException, IOException {
        boolean showHelp = false;
		Collection excludeMatchers = new ArrayList();
		String binariesDir = null;
		String trackChangesFile = null;

        if(args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if(args[i].equals("-exclude")) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
                    excludeMatchers.add(Pattern.compile(args[i], Pattern.CASE_INSENSITIVE).matcher(""));
				} else if(args[i].equals("-write_binaries")) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
                    binariesDir = args[i];
                } else if(args[i].equals("-track")) {
                    if(++i == args.length) {
                        showHelp = true;
                        break;
                    }
                    trackChangesFile = args[i];
                } else {
                    showHelp = true;
                    break;
				}
			}
        } else {
            showHelp = true;
		}
        if(showHelp) {
            System.out.println("Usage:");
            System.out.println("  java jsdai.tools.RemoveDeclarations [<options>]");
            System.out.println("Options are:");
            System.out.println("  -exclude <regex>             Model name to exclude as case insensitive");
			System.out.println("                               regular expression. This option can be repeated.");
            System.out.println("  -write_binaries <directory>  Writes out updated binary files to directory");
			System.out.println("                               tree under sperified directory");
            System.out.println("  [-track track_changes_property_file]");
			System.out.println("                               Track the changes in dictionary model binary");
			System.out.println("                               files into specified property file");
            return;
        } else {
			SdaiSession sdaiSession = SdaiSession.openSession();
			SdaiTransaction transaction = sdaiSession.startTransactionReadWriteAccess();
			SdaiRepository expressRepository = sdaiSession.linkRepository(repositoryName, null);
			expressRepository.openRepository();

			if(trackChangesFile != null) {
				RepositoryChanges.rememberRepositoryState(expressRepository);
			}

			RemoveDeclarations removeDeclarations = new RemoveDeclarations(sdaiSession, expressRepository);
			removeDeclarations.remove(excludeMatchers);
			if(binariesDir != null) {
				removeDeclarations.copyBinaries(binariesDir);
			}

			if(trackChangesFile != null) {
				RepositoryChanges.trackRepositoryChanges(expressRepository, trackChangesFile);
			}

			expressRepository.closeRepository();
			transaction.endTransactionAccessCommit();
		}
	}

	private void remove(Collection excludeMatchers) throws SdaiException {
		ASdaiModel models = repository.getModels();
		for(SdaiIterator i = models.createIterator(); i.next(); ) {
			SdaiModel model = models.getCurrentMember(i);
			String modelName = model.getName();
			if(!modelName.startsWith("_") && !modelName.startsWith("SDAI_")
			   && modelName.endsWith(DICTIONARY_DATA_SUFFIX)) {

				boolean process = true;
				for(Iterator j = excludeMatchers.iterator(); j.hasNext(); ) {
					Matcher excludeMatcher = (Matcher)j.next();
					excludeMatcher.reset(modelName);
					if(excludeMatcher.matches()) {
						process = false;
						System.out.println(" Retaining " + modelName + " declarations.");
						break;
					}
				}
				if(process) {
					removeForModel(model);
				}
			}
		}
		sdaiSession.getActiveTransaction().commit();
	}

	private void removeForModel(SdaiModel model) throws SdaiException {
		int modelMode = model.getMode();
		if(modelMode == SdaiModel.NO_ACCESS) {
			model.startReadWriteAccess();
		} else if(modelMode == SdaiModel.READ_ONLY) {
			model.promoteSdaiModelToRW();
		}
		Set allSupertypes = new HashSet();
		AEntity_definition entityDefinitions = (AEntity_definition)model.getInstances(EEntity_definition.class);
		for(SdaiIterator i = entityDefinitions.createIterator(); i.next(); ) {
			EEntity_definition entityDefinition = entityDefinitions.getCurrentMember(i);
			addSupertypes(entityDefinition, allSupertypes);
		}
		AInterfaced_declaration declarations = (AInterfaced_declaration)model.getInstances(EInterfaced_declaration.class);
		Set declarationsForDeletion = new HashSet();
		for(SdaiIterator i = declarations.createIterator(); i.next(); ) {
			EInterfaced_declaration declaration = declarations.getCurrentMember(i);
			if(!allSupertypes.contains(declaration.getDefinition(null))) {
				declarationsForDeletion.add(declaration);
			}
		}
		for(Iterator i = declarationsForDeletion.iterator(); i.hasNext(); ) {
			EEntity declaration = (EEntity)i.next();
			declaration.deleteApplicationInstance();
		}
	}

	private static void addSupertypes(EEntity_definition entityDefinition, Set allSupertypes) throws SdaiException {
		AEntity_or_view_definition supertypes = entityDefinition.getGeneric_supertypes(null);
		for(SdaiIterator j = supertypes.createIterator(); j.next(); ) {
			EEntity_definition supertypeDefinition = (EEntity_definition)supertypes.getCurrentMember(j);
			allSupertypes.add(supertypeDefinition);
			addSupertypes(supertypeDefinition, allSupertypes);
		}
	}

	private void copyBinaries(String binariesDir) throws SdaiException, IOException {
		File baseDir = new File(binariesDir, "jsdai");
		StringBuffer nameBuf = new StringBuffer("S");
		ASdaiModel models = repository.getModels();
		for(SdaiIterator i = models.createIterator(); i.next(); ) {
			SdaiModel model = models.getCurrentMember(i);
			String modelName = model.getName();
			if(!modelName.startsWith("_") && !modelName.startsWith("SDAI_")
			   && modelName.endsWith(DICTIONARY_DATA_SUFFIX)) {

				URL fromURL = model.getLocationURL();
				nameBuf.setLength(1);
				nameBuf.append(Character.toUpperCase(modelName.charAt(0)));
				final int nameLen = modelName.length() - DICTIONARY_DATA_SUFFIX_LEN;
				for(int j = 1; j < nameLen; j++) {
					nameBuf.append(Character.toLowerCase(modelName.charAt(j)));
				}
				File binaryDir = new File(baseDir, nameBuf.toString());
				binaryDir.mkdirs();
				File toFile = new File(binaryDir, modelName);

				InputStream in = fromURL.openStream();
				FileOutputStream out = new FileOutputStream(toFile);
				byte[] buffer = new byte[8 * 1024];
				int count = 0;
				do {
					out.write(buffer, 0, count);
					count = in.read(buffer, 0, buffer.length);
				} while (count != -1);

				in.close();
				out.close();
			}
		}
	}

} // RemoveDeclarations
/*
Local Variables:
compile-command: "ant -emacs -find build-main.xml run.removedeclarations"
End:
*/
