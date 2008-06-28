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

import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.exp2.EGToolKit;
import jsdai.lang.SdaiException;
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
				File oldFile = new File(rh.getRepository().getLocation() + ".old");
				File repo = new File(rh.getRepository().getLocation());
				EGToolKit.copyFile(file, tempFile);
				EGToolKit.copyFile(repo, tempRepo);
				SdaiRepository repoDict = session.linkRepository("", tempFile.getAbsolutePath());
				repoDict.openRepository();
				SdaiRepository repoEG = session.linkRepository("", tempRepo.getAbsolutePath());
				repoEG.openRepository();

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
				repoDict.closeRepository();
				repoDict.unlinkRepository();
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
}
