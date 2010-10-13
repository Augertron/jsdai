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

package jsdai.express_g.util.repocopy;

import java.io.File;

import jsdai.SExpress_g_schema.EPage_reference_bundle;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.lang.AEntity;
import jsdai.lang.ASdaiModel;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author Mantas Balnys
 *
 */
public class RepoCopy {
	
	public static void synchronizedRepoCopy(RepositoryHandler rh,
			SdaiRepository repo, IProgressMonitor progress)
			throws SdaiException {
		
		/**@regcheck*/
			
		//System.out.println("RepoCopy2");
//		EGEPlugin.getDefault().getWorkbench().getDisplay().syncExec(new RepoCopy(rh, repo, progress));

		try {
/*us			ElementListSelectionDialog userSelection = new ElementListSelectionDialog(
					null, new LabelProvider());
			Object[] userQuest = new Object[]{
					"Use schemas in new repository and apply created layouts for them",
					"Get absolutely new copy of repository and loose created layouts"};//,
					//"(experimental) Apply created layout for new schemas and try to copy unexisting schemas"};
			userSelection.setElements(userQuest);
			userSelection.setTitle("Importing repository");
			userSelection.setInitialSelections(new Object[]{userQuest[1]});
			if (userSelection.open() == Window.OK) {
				Object rez = userSelection.getFirstResult();*/
/*				if (rez.equals(userQuest[2])) {
					try {
						ASdaiModel models = rh.getRepository().getModels();
						int totalWork = models.getMemberCount();
					    int worked = 0;
						SdaiIterator mit = models.createIterator();
						while (mit.next()) {
							worked++;
							SdaiModel model = models.getCurrentMember(mit);
							String modelName = model.getName();
							EGEPlugin.log(rh.getProjectPath(), "MODEL (" + worked + "/" + totalWork + "): " + model);
							System.out.println("MODEL (" + worked + "/" + totalWork + "): " + model);
							if (!model.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")
									&& (repo.findSdaiModel(modelName) == null)) {

								try {
									SdaiModel newModel = repo.createSdaiModel(modelName, model.getUnderlyingSchema());
									switch (model.getMode()) {
										case SdaiModel.NO_ACCESS :
											model.startReadWriteAccess();
										break;
										case SdaiModel.READ_ONLY :
											model.promoteSdaiModelToRW();
										break;
									}
									switch (newModel.getMode()) {
										case SdaiModel.NO_ACCESS :
											newModel.startReadWriteAccess();
										break;
										case SdaiModel.READ_ONLY :
											newModel.promoteSdaiModelToRW();
										break;
									}
									newModel.copyInstances(model.getInstances());
									EGEPlugin.log(rh.getProjectPath(), "copied");
									System.out.println("copied");
								} catch (SdaiException sex) {
									sex.printStackTrace();
									EGEPlugin.log(rh.getProjectPath(), sex);
								}
							} else {
								
							}
						}
					} catch (SdaiException sex) {
						sex.printStackTrace();
						EGEPlugin.log(rh.getProjectPath(), sex);
					}
				}*/
//us				if (!rez.equals(userQuest[1])) {
					RepositoryMerger merger = new RepositoryMerger(repo, rh.getRepository());
					merger.run(progress);
//us				}
				if (progress != null)
					progress.subTask("Finishing...");
				if (progress == null || !progress.isCanceled()) {
					if (repo.isModified())
						repo.getSession().getActiveTransaction().commit();
					rh.close();
					File from = new File(repo.getLocation());
					File to = new File(rh.getRepository().getLocation());
					EGToolKit.copyFile(from, to);
					rh.init();
				}
				if (progress != null) progress.subTask("Updating...");
				
//us			}
		} catch (SdaiException sex) {
			SdaieditPlugin.log(sex);
		}
	}

	public static void synchronizedRepoCopy(RepositoryHandler rh, String fileName,
			IProgressMonitor progress) throws SdaiException {
		//System.out.println("RepoCopy");
//		rh.getRepository().exportClearTextEncoding("G:\\_BUGS\\rh_as_found.pf");

		RepositoryChanger changer = new RepositoryChanger(rh.getModels(), "Copying repository");
		String msg = rh.startRWChanger(changer);
		if (msg != null) {
			MessageDialog.openWarning(null, "Copying repository", msg);
		} else try {
			//			String file = rh.getProjectPath() + File.separator + "Internal" +
			// File.separator + "ExpressCompilerRepo" + File.separator +
			// "ExpressCompilerRepo.sdai";
			File file = new File(fileName);
			if (file.exists()) {
				SdaiSession session = SdaiSession.openSession();
				SdaiTransaction transaction;
				if (session.testActiveTransaction()) {
					transaction = session.getActiveTransaction();
				} else {
					transaction = session.startTransactionReadWriteAccess();
				}

				File tempFile = EGToolKit.getTempFile(null, "sdai");
				File tempRepo = EGToolKit.getTempFile(null, "sdai");

//---------debug 

//System.out.println("--- Printing from repository extracted from RepositoryHandler ----");


SdaiRepository repo_rr = rh.getRepository();

		// repo_rr.openRepository();



			ASdaiModel models_rr = repo_rr.getModels();
			SdaiIterator msit_rr = models_rr.createIterator();
			while (msit_rr.next()) {
				SdaiModel model_rr = models_rr.getCurrentMember(msit_rr);
				if (model_rr.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
//						if (model.getMode() == SdaiModel.NO_ACCESS) model.startReadWriteAccess();
						if (model_rr.getMode() == SdaiModel.NO_ACCESS) model_rr.startReadOnlyAccess();
//System.out.println("model name: " + model_rr.getName());
//System.out.println("<0-1>: " + model_rr.getInstances(EPage_reference_bundle.class));

				IndependentSdaiIterator it_rr = new IndependentSdaiIterator();
  			AEntity list_rr = model_rr.getInstances(EPage_reference_bundle.class);
//System.out.println("instance count: " + list_rr.getMemberCount());
  			it_rr.setEntities(list_rr);
  			int count_rr = 0;
  			while (it_rr.hasNext()) {
     			EPage_reference_bundle itnext_rr = (EPage_reference_bundle)it_rr.next();
//System.out.println("bundle " + ++count_rr + ": " + itnext_rr);
   			} 




				}	
			}



//---------end-debug


				File oldFile = new File(rh.getRepository().getLocation() + ".old");
				File repo = new File(rh.getRepository().getLocation());
				EGToolKit.copyFile(file, tempFile);
				EGToolKit.copyFile(repo, tempRepo);
				SdaiRepository repoDict = session.linkRepository("", tempFile.getAbsolutePath());
				repoDict.openRepository();
//		repoDict.exportClearTextEncoding("G:\\_BUGS\\repoDict.pf");
				SdaiRepository repoEG = session.linkRepository("", tempRepo.getAbsolutePath());
//		repoEG.exportClearTextEncoding("G:\\_BUGS\\repoEGbeforeStart.pf");
				repoEG.openRepository();

//------------------debug
//System.out.println("--- Printing from repoEG ----");


			ASdaiModel models_rr2 = repoEG.getModels();
			SdaiIterator msit_rr2 = models_rr2.createIterator();
			while (msit_rr2.next()) {
				SdaiModel model_rr2 = models_rr2.getCurrentMember(msit_rr2);
				if (model_rr2.getUnderlyingSchemaString().equalsIgnoreCase("EXPRESS_G_SCHEMA")) {
						if (model_rr2.getMode() == SdaiModel.NO_ACCESS) model_rr2.startReadWriteAccess();
//						if (model_rr2.getMode() == SdaiModel.NO_ACCESS) model_rr2.startReadOnlyAccess();
//System.out.println("model name: " + model_rr2.getName());
//System.out.println("<0-2>: " + model_rr2.getInstances(EPage_reference_bundle.class));

				IndependentSdaiIterator it_rr2 = new IndependentSdaiIterator();
  			AEntity list_rr2 = model_rr2.getInstances(EPage_reference_bundle.class);
//System.out.println("instance count: " + list_rr2.getMemberCount());
  			it_rr2.setEntities(list_rr2);
  			int count_rr2 = 0;
  			while (it_rr2.hasNext()) {
     			EPage_reference_bundle itnext_rr2 = (EPage_reference_bundle)it_rr2.next();
//System.out.println("bundle " + ++count_rr2 + ": " + itnext_rr2);
   			} 




				}	
			}


//------------------end-debug


				// copying
				try {
					RepositoryMerger merger = new RepositoryMerger(repoDict, repoEG);
					merger.run(progress);
				} catch (SdaiException sex) {
					SdaieditPlugin.log(sex);
					SdaieditPlugin.console(sex.toString());
				}

				if (progress != null) progress.subTask("Finishing...");

				if (repoDict.isModified()) repoDict.getSession().getActiveTransaction().commit();
	//	repoDict.exportClearTextEncoding("G:\\_BUGS\\repoDict2.pf");
				repoDict.closeRepository();
				repoDict.unlinkRepository();
//				repoEG.exportClearTextEncoding("G:\\_BUGS\\repoEG2.pf");
				repoEG.closeRepository();
				repoEG.unlinkRepository();
				
				if (progress == null || !progress.isCanceled()) {
					rh.close();
					EGToolKit.copyFile(repo, oldFile);
					EGToolKit.copyFile(tempFile, repo);
					rh.init();
				}
				session.closeSession();

				EGToolKit.delTempFile(tempFile);
				EGToolKit.delTempFile(tempRepo);
			}
			changer.done();
			rh.endRWChanger(changer);
		} catch (RuntimeException t) {
			changer.done();
			rh.endRWChanger(changer);
			throw t;
		} catch (SdaiException t) {
			changer.done();
			rh.endRWChanger(changer);
			throw t;
		}
		
//		rh.update();
	}


	public static void synchronizedRepoCopyForInsertingDiagram(RepositoryHandler rh, String fileName, String diagramName,
			IProgressMonitor progress) throws SdaiException {

//System.out.println("TARGET: " + rh.getRepository().getLocation());
//System.out.println("SOURCE: " + fileName);

		//System.out.println("RepoCopy");
		RepositoryChanger changer = new RepositoryChanger(rh.getModels(), "Copying repository");
		String msg = rh.startRWChanger(changer);
//System.out.println("MSG: " + msg);
		if (msg != null) {
			MessageDialog.openWarning(null, "Copying repository", msg);
		} else try {
			//			String file = rh.getProjectPath() + File.separator + "Internal" +
			// File.separator + "ExpressCompilerRepo" + File.separator +
			// "ExpressCompilerRepo.sdai";
			File file = new File(fileName);
			if (file.exists()) {
				SdaiSession session = SdaiSession.openSession();
				SdaiTransaction transaction;
				if (session.testActiveTransaction()) {
					transaction = session.getActiveTransaction();
				} else {
					transaction = session.startTransactionReadWriteAccess();
				}

//System.out.println("<REACHED-01> ");


				File tempSourceFile = EGToolKit.getTempFile(null, "sdai");
				File tempTargetFile = EGToolKit.getTempFile(null, "sdai");
				File oldTargetFile = new File(rh.getRepository().getLocation() + ".old");
				File targetRepoFile = new File(rh.getRepository().getLocation());
				EGToolKit.copyFile(file, tempSourceFile);
				EGToolKit.copyFile(targetRepoFile, tempTargetFile);
				SdaiRepository repoSource = session.linkRepository("", tempSourceFile.getAbsolutePath());
				repoSource.openRepository();
				SdaiRepository repoTarget = session.linkRepository("", tempTargetFile.getAbsolutePath());
				repoTarget.openRepository();

				// inserting diagram
				try {
					DiagramInserter inserter = new DiagramInserter(repoSource, repoTarget, diagramName);
					inserter.run(progress);
				} catch (SdaiException sex) {
					SdaieditPlugin.log(sex);
					SdaieditPlugin.console(sex.toString());
				}

				if (progress != null) progress.subTask("Finishing...");

				// this will not happen
				// if (repoSource.isModified()) repoSource.getSession().getActiveTransaction().commit();
				
				// perhaps do this for target
				//if (repoTarget.isModified()) repoTarget.getSession().getActiveTransaction().commit();
				repoTarget.getSession().getActiveTransaction().commit();
				
				repoSource.closeRepository();
				repoSource.unlinkRepository();
				repoTarget.closeRepository();
				repoTarget.unlinkRepository();
				
				if (progress == null || !progress.isCanceled()) {
					rh.close();
					EGToolKit.copyFile(targetRepoFile, oldTargetFile);
					EGToolKit.copyFile(tempTargetFile, targetRepoFile);
					rh.init();
				}
				session.closeSession();

				EGToolKit.delTempFile(tempSourceFile);
				EGToolKit.delTempFile(tempTargetFile);
			}
			changer.done();
			rh.endRWChanger(changer);
		} catch (RuntimeException t) {
			changer.done();
			rh.endRWChanger(changer);
			throw t;
		} catch (SdaiException t) {
			changer.done();
			rh.endRWChanger(changer);
			throw t;
		}
		
//		rh.update();
	}


}
