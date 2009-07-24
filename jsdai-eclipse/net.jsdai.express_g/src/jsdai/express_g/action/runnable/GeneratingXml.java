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

package jsdai.express_g.action.runnable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import jsdai.SExpress_g_schema.EGraphics_diagram;
import jsdai.SExpress_g_schema.EProperty;
import jsdai.SExtended_dictionary_schema.SExtended_dictionary_schema;
import jsdai.express_g.SdaieditPlugin;
import jsdai.express_g.editors.ModelHandler;
import jsdai.express_g.editors.ModelNameValidator;
import jsdai.express_g.editors.RepositoryChanger;
import jsdai.express_g.editors.RepositoryHandler;
import jsdai.express_g.editors.SchemaNameValidator;
import jsdai.express_g.editors.SdaiEditor;
import jsdai.express_g.exp2.ui.PropertySharing;
import jsdai.express_g.util.xml.ExpressXmlConverter;
import jsdai.express_g.util.xml.IPAdder;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Mantas Balnys
 *
 */
public class GeneratingXml extends RunnableOnView {

	private int editMode;

 	static IProject fProject = null;

	/**
	 * 
	 * @param editor
	 * @param viewer
	 * @param editMode
	 */
	public GeneratingXml(SdaiEditor editor, Viewer viewer, int editMode) {
		super(editor, viewer);
		this.editMode = editMode;
	}
	
	private String getSchemaTypeExtension() {
		return "_EXPRESS_G_DATA";
	}

	private String getSchemaExtension() {
		String text;
		if ((editMode & PropertySharing.MODE_LONGFORM_MASK) != 0) {
			if ((editMode & PropertySharing.MODE_LAYOUT_COMPLETE_MASK) != 0)
				text = "_complete_long";
			else
				text = "_partial_long";
		} else {
			if ((editMode & PropertySharing.MODE_LAYOUT_COMPLETE_MASK) != 0)
				text = "_complete_short";
			else
				text = "_partial_short";
		}
		text += "_layout";
		return text;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,	InterruptedException {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection)selection).getFirstElement();
			if (obj instanceof IResource) {
					fProject = ((IResource)obj).getProject();
			} else {
				System.out.println("selection element is NOT a resource: " + obj);
			}
			if (obj instanceof ModelHandler) {
				ModelHandler mhDict = null;
				SdaiModel model = null;
				RepositoryHandler rph = null;
				SdaiRepository repo = null;
				SdaiSession session = null;
				SdaiModel work = null;
				boolean flag_is_active = false;
				try {
					mhDict = (ModelHandler)obj;
					model = mhDict.getModel();
					rph = mhDict.getRepositoryHandler();
					repo = rph.getRepository();
					flag_is_active = repo.isActive();
					if (!flag_is_active)
					    repo.openRepository();
					session = repo.getSession();
        	        work = repo.createSdaiModel("working", SExtended_dictionary_schema.class);

//				System.out.println("<>Model: " + mdl);

//				File path = new File(moduleDir, xmlFile);
//				ExpressXmlConverter.convertSchemaInstance(schema, path.getPath());


  //      		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
  //            IProject fProject = root.getProject();
//			System.out.println("<>repo: " + rph.getRepoPath());
            String xml_file = model.getName().toLowerCase();
            int end_index = xml_file.lastIndexOf("_dictionary_data");
            if (end_index  >0) xml_file = xml_file.substring(0,end_index);
            String xml_path = rph.getRepoPath().toOSString();
            int last_slash = xml_path.lastIndexOf(File.separator);
            if (last_slash > 0) xml_path = xml_path.substring(0,last_slash);
            xml_path = xml_path + File.separator + xml_file + ".xml"; 				
//			System.out.println("<>xml: " + xml_path);
            String informal_propositions = "";
            if (fProject != null) {
            	informal_propositions = fProject.getLocation().toOSString() + File.separator + "informal_propositions.txt";
          	}
            String document_reference = fProject.getLocation().toOSString() + File.separator + "document_reference.txt";
System.out.println("informal_propositions: " + informal_propositions);

            fProject = null;
            String project_name = fProject.getName();

            ExpressXmlConverter.convertModel(model, session, repo, work, xml_path, "kukubebe", true);
System.out.println("before IPAdder");
            IPAdder.addIPsForSchema(xml_file, xml_path, informal_propositions, null);
System.out.println("after IPAdder");
       		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath xml_location = rph.getRepoPath().removeLastSegments(1);
       		// System.out.println("<location> : " + xml_location);
       		IResource xml_res = (root.findContainersForLocation(xml_location))[0];
			xml_res.refreshLocal(1, monitor);
			
			
			
				}
				catch (Exception e) {
					System.out.println("<exception>: " +  e);
					SdaieditPlugin.log(e);
					e.printStackTrace();
				}
				finally {
				  
				  try {
					work.deleteSdaiModel();
					if (!flag_is_active) repo.closeRepository();
				   } catch (SdaiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }	
								
}
		
				/*
				RepositoryHandler rh = mhDict.getRepositoryHandler();
				RepositoryChanger changer = new RepositoryChanger(new String[]{mhDict.getName()}, "Creating diagram");
				String msg = rh.startROChanger(changer);
				if (msg != null) {
					MessageDialog.openWarning(getShell(), "Create Diagram Action", msg);
				} else {
					InputDialog input = new InputDialog(getShell(), 
							"Express-G Editor", "Enter name for diagram", mhDict.getName() + getSchemaExtension(), 
							new ModelNameValidator(rh.getRepository(), getSchemaTypeExtension()));

					if (input.open() == InputDialog.OK) try {
						String schemaName = input.getValue();
						SdaiRepository repository = rh.getRepository();
				        SdaiModel modelEG = repository.createSdaiModel(schemaName.toUpperCase() + getSchemaTypeExtension(), jsdai.SExpress_g_schema.SExpress_g_schema.class);
				        modelEG.startReadWriteAccess();
				        
				        EGraphics_diagram schemaG = (EGraphics_diagram)modelEG.createEntityInstance(EGraphics_diagram.class);
				        schemaG.setComment(null, schemaName);
				        schemaG.setDic_schema(null, mhDict.getSchema_definition());
				        EProperty p = (EProperty)modelEG.createEntityInstance(EProperty.class);
				        p.setName(null, PropertySharing.PROP_EDITING_MODE);
				        p.setData(null, String.valueOf(editMode));

				        rh.update();
				        refresh();
				        selectInView(rh.getModelHandler(schemaName));
					} catch (SdaiException sex) {
						SdaieditPlugin.log(sex);
						SdaieditPlugin.console(sex.toString());
					}
				}
				changer.done();
				rh.endROChanger(changer);
				*/
			} else if (obj instanceof RepositoryHandler) {
				RepositoryHandler rh = (RepositoryHandler)obj;
				System.out.println("<>REPOSITORY: " + rh);
				/*
				try {
					RepositoryHandler rh = (RepositoryHandler)obj;
					InputDialog input = new InputDialog(getShell(), 
							"Express-G Editor", "Enter name for diagram", rh.getRepository().getName() + getSchemaExtension(), 
							new SchemaNameValidator(rh.getModels()));

					if (input.open() == InputDialog.OK) {
						String schemaName = input.getValue();
						SdaiRepository repository = rh.getRepository();
						SdaiModel modelEG = repository.createSdaiModel(schemaName.toUpperCase() + "_EXPRESS_G_DATA", jsdai.SExpress_g_schema.SExpress_g_schema.class);
						modelEG.startReadWriteAccess();
					        
						EGraphics_diagram schemaG = (EGraphics_diagram)modelEG.createEntityInstance(EGraphics_diagram.class);
						schemaG.setComment(null, schemaName);
				        EProperty p = (EProperty)modelEG.createEntityInstance(EProperty.class);
				        p.setName(null, PropertySharing.PROP_EDITING_MODE);
				        p.setData(null, String.valueOf(editMode));

						rh.update();
						refresh();
						selectInView(rh.getModelHandler(schemaName));
					}
				} catch (SdaiException sex) {
					SdaieditPlugin.log(sex);
					SdaieditPlugin.console(sex.toString());
				}
				*/
			} else {
//				System.err.println("wrong item for NewDiagram action: " + obj);
			}
		} else {
//			System.err.println("wrong selection for NewDiagram action: " + selection);
		}
	}


	/* (non-Javadoc)
	 * @see jsdai.express_g.action.runnable.AbstractRunnable#acceptsCurrentSelection()
	 */
	public boolean acceptsCurrentSelection() {
		ISelection selection = getSelection();
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection)selection).getFirstElement();
			if (obj instanceof ModelHandler) {
				ModelHandler mh = (ModelHandler)obj;
				if (mh.getDiagram_definition() == null)
					return true;
			} else
			if (obj instanceof RepositoryHandler) {
				if ((editMode & PropertySharing.MODE_LONGFORM_MASK) == 0)
					return true;
			}
		}
		return false;
	}
}
