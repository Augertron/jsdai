//$Id$
//  Author: Rob Bodington, Eurostep Limited
//  Owner:  Developed by Eurostep and supplied to NIST under contract.
//  Purpose:  JScript to generate the default XML for the module.
//     cscript mkmodule.js <module> 
//     e.g.
//     cscript mkmodule.js person
//     The script is called from mkmodule.ws


// ------------------------------------------------------------
// Global variables
// -----------------------------------------------------------

// NOTE NO LONGER REQUIRE THIS VARIABLE TO BE SET
// Change this to wherever you have installed the module repository
// Note the use of UNIX path descriptions as opposed to DOS
// var stepmodHome = "e:/My Documents/projects/nist_module_repo/stepmod";

// If you do not run mkmodule from 
var stepmodHome = "..";


var moduleClauses = new Array("main", "abstract", "isocover", "cover", "contents", 
			      "introduction", "foreword", 
			      "1_scope", "2_refs", "3_defs", "4_info_reqs", 
			      "5_main", "5_mim", "5_mapping", 
			      "a_short_names", "b_obj_reg", "c_arm_expg", 
			      "d_mim_expg", 
			      "e_exp", 
			      "e_exp_mim", "e_exp_mim_lf", 
			      "e_exp_arm", "e_exp_arm_lf", 
			      "f_guide", 
			      "biblio", "modindex", "p28xsd");

// If 1 then output user messages
var outputUsermessage = 1;


//------------------------------------------------------------
function userMessage(msg){
    if (outputUsermessage == 1)
	WScript.Echo(msg);
}


function ErrorMessage(msg){
    var objShell = WScript.CreateObject("WScript.Shell");
    if (outputUsermessage == 1)
	WScript.Echo(msg);
    objShell.Popup(msg);
}

function CheckStepHome() {
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    if (!fso.FolderExists(stepmodHome)) {
	ErrorMessage("The variable stepmodHome is incorrectly set in:\n"+
		     "stepmod/utils/mkmodule.js to:\n\n"+stepmodHome+
		     "\n\nNote: it is a UNIX rather then WINDOWS path i.e. uses / not \\");
	return(false);
    }
    if (!fso.FolderExists(stepmodHome+"/data/modules")) {
	ErrorMessage("You must run the script from the directory:\n"+
		     "stepmod/utils");
	return(false);
    }
    return(true);
}


function CheckArgs() {
    var cArgs = WScript.Arguments;
    var msg = "Incorrect arguments\n"+
	"  cscript mkmodule.js <module> <long_form>";
    if ((cArgs.length == 1) || (cArgs.length == 2)) {
	return(true);
    } else {
	ErrorMessage(msg);
	return(false);
    } 
}

function CheckModule(module) {
    var modFldr = GetModuleDir(module);
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    
    // check if the folder exists
    if (fso.FolderExists(modFldr)) 
	throw("Directory already exists for module: "+ module);
    return(true);
}

function schemaName(module,armOrMim) {
    var name = module.substr(0,1).toUpperCase()+
	module.substr(1,module.length)+
	"_"+armOrMim;
    return(name); 
}

function GetModuleDir(module) {
    return( stepmodHome+"/data/modules/"+module+"/");
}

// Create the nav directory and insert the developer file
function MakeNavFldr(module) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    var modFldr = GetModuleDir(module);
    var modNavFldr = modFldr+"nav/";
    if (!fso.FolderExists(modNavFldr)) {
	fso.CreateFolder(modNavFldr);
    }

    var developerXML = modNavFldr+"developer.xml";

    MakeNavFile(modNavFldr+"arm_descriptions.xml", module, "arm_descriptions.xsl");
    MakeNavFile(modNavFldr+"arm_long_form.xml", module, "arm_long_form.xsl");
    MakeNavFile(modNavFldr+"developer.xml", module, "developer.xsl");
    MakeNavFile(modNavFldr+"issues_dvlp.xml", module, "issues_dvlp.xsl");
    MakeNavFile(modNavFldr+"mapping_view.xml", module, "mapping_view.xsl");
    MakeNavFile(modNavFldr+"mapping_view_with_test.xml", module, "mapping_view_with_test.xsl");
    MakeNavFile(modNavFldr+"mim_descriptions.xml", module, "mim_descriptions.xsl");
    MakeNavFile(modNavFldr+"select_view.xml", module, "select_view.xsl");
    MakeNavFile(modNavFldr+"summary.xml", module, "summary.xsl");
    MakeNavFile(modNavFldr+"module_index.xml", module, "module_index.xsl");
    MakeNavFile(modNavFldr+"select_matrix_view.xml", module, "select_matrix_view.xsl");

    // MakeNavFile(modNavFldr+"arm_frame.xml", module,"nav_main.xsl","");
    // MakeNavFile(modNavFldr+"mim.xml", module, "mim.xsl");
    // MakeNavFile(modNavFldr+"mim_frame.xml", module,"nav_main.xsl","");

    //decided not to use these yet
//     MakeNavFile(modNavFldr+"scope_intro.xml", module, "scope_intro.xsl");
//     MakeNavFile(modNavFldr+"scope_intro_frame.xml", module,"nav_main.xsl","scope_intro");

//     MakeNavFile(modNavFldr+"summary_frame.xml", module,"nav_main.xsl","");

//     MakeNavFile(modNavFldr+"armepxg.xml", module, "armepxg.xsl");
//     MakeNavFile(modNavFldr+"armepxg_frame.xml", module,"nav_main.xsl","");

//     MakeNavFile(modNavFldr+"mimepxg.xml", module, "mimepxg.xsl");
//     MakeNavFile(modNavFldr+"mimepxg_frame.xml", module,"nav_main.xsl","");

//     MakeNavFile(modNavFldr+"mapping.xml", module, "mapping.xsl");
//     MakeNavFile(modNavFldr+"mapping_frame.xml", module,"nav_main.xsl","");

}


function MakeNavFile(file, module, stylesheet, body) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    if (!fso.FileExists(file)) { 
	userMessage("MakeNavFile"+file);
	fso.CreateTextFile(file, true);
	var f = fso.GetFile(file);
	var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
	ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	ts.WriteLine("<!-- $Id$ -->");
	ts.WriteLine("<?xml-stylesheet type=\"text/xsl\" href=\"../../../../nav/xsl/"+stylesheet+"\"?>");
	if (body) {
	    ts.WriteLine("<stylesheet_application directory=\""+module+"\" body=\""+body+"\"/>");
	} else {
	    ts.WriteLine("<stylesheet_application directory=\""+module+"\"/>");
	}
	ts.Close();
    }
}

// Create the dvlp directory and insert the projmg and issues file
function MakeDvlpFldr(module, update) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    var modFldr = GetModuleDir(module);
    var modDvlpFldr = modFldr+"dvlp/";
    var projmgXML = modDvlpFldr+"projmg.xml";
    var issuesXML = modDvlpFldr+"issues.xml";

    var objShell = WScript.CreateObject("WScript.Shell");
    var milestones;
//    if (typeof(update) == 'undefined') {
//	milestones = objShell.Popup("Do want module milestones?",0,"Creating module milestones", 36);
//	if (milestones == 6) {
//	    milestones=true;
//	} else {
//	    milestones=false;
//	}
//    } else {
//	milestones=false;
//    }
    var f,ts;
    if (!fso.FolderExists(modDvlpFldr)) 
	fso.CreateFolder(modDvlpFldr);
    if (!fso.FileExists(projmgXML)) { 
	fso.CreateTextFile(projmgXML, true);
	f = fso.GetFile(projmgXML);
	ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
	ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	ts.WriteLine("<!-- $Id$ -->");
	ts.WriteLine("<?xml-stylesheet type=\"text/xsl\" href=\"../../../../xsl/projmg/projmg.xsl\"?>");
  	ts.WriteLine("<!DOCTYPE management SYSTEM \"../../../../dtd/projmg/projmg.dtd\">");
	ts.WriteLine("<management module=\""+module+"\"");
	ts.WriteLine("  percentage_complete=\"0\"");
	ts.WriteLine("  issues=\"issues.xml\">");
	ts.WriteLine("");
	ts.WriteLine("  <developers>");
	ts.WriteLine("    <developer ref=\"\"/>");
	ts.WriteLine("  </developers>");
	ts.WriteLine("");
	if (!milestones) ts.WriteLine("<!-- ");
	ts.WriteLine("  <module.milestones>");
	ts.WriteLine("    <milestone");
	ts.WriteLine("      name=\"M1\"");
	ts.WriteLine("      description=\"Requirements allocated to module\"");
	ts.WriteLine("      status=\"not-achieved\"");
	ts.WriteLine("      planned_date=\"\"");
	ts.WriteLine("      predicted_date=\"\"");
	ts.WriteLine("      achieved_date=\"\">");
	ts.WriteLine("    </milestone>");
	ts.WriteLine("");
	ts.WriteLine("    <milestone");
	ts.WriteLine("      name=\"M2\"");
	ts.WriteLine("      description=\"Draft for team review\"");
	ts.WriteLine("      status=\"not-achieved\"");
	ts.WriteLine("      planned_date=\"\"");
	ts.WriteLine("      predicted_date=\"\"");
	ts.WriteLine("      achieved_date=\"\">");
	ts.WriteLine("    </milestone>");
	ts.WriteLine("");
	ts.WriteLine("    <milestone");
	ts.WriteLine("      name=\"M3\"");
	ts.WriteLine("      description=\"Ready for QC\"");
	ts.WriteLine("      status=\"not-achieved\"");
	ts.WriteLine("      planned_date=\"\"");
	ts.WriteLine("      predicted_date=\"\"");
	ts.WriteLine("      achieved_date=\"\">");
	ts.WriteLine("    </milestone>");
	ts.WriteLine("");
	ts.WriteLine("    <milestone");
	ts.WriteLine("      name=\"M4\"");
	ts.WriteLine("      description=\"QC form completed\"");
	ts.WriteLine("      status=\"not-achieved\"");
	ts.WriteLine("      planned_date=\"\"");
	ts.WriteLine("      predicted_date=\"\"");
	ts.WriteLine("      achieved_date=\"\">");
	ts.WriteLine("    </milestone>");
	ts.WriteLine("");
	ts.WriteLine("    <milestone");
	ts.WriteLine("      name=\"M5\"");
	ts.WriteLine("      description=\"Submitted for ballot\"");
	ts.WriteLine("      status=\"not-achieved\"");
	ts.WriteLine("      planned_date=\"\"");
	ts.WriteLine("      predicted_date=\"\"");
	ts.WriteLine("      achieved_date=\"\">");
	ts.WriteLine("    </milestone>");
	ts.WriteLine("");
	ts.WriteLine("    <milestone");
	ts.WriteLine("      name=\"M6\"");
	ts.WriteLine("      description=\"Ballot comments resolved\"");
	ts.WriteLine("      status=\"not-achieved\"");
	ts.WriteLine("      planned_date=\"\"");
	ts.WriteLine("      predicted_date=\"\"");
	ts.WriteLine("      achieved_date=\"\">");
	ts.WriteLine("    </milestone>");
	ts.WriteLine("");
	ts.WriteLine("    <milestone");
	ts.WriteLine("      name=\"M7\"");
	ts.WriteLine("      description=\"Published as TS\"");
	ts.WriteLine("      status=\"not-achieved\"");
	ts.WriteLine("      planned_date=\"\"");
	ts.WriteLine("      predicted_date=\"\"");
	ts.WriteLine("      achieved_date=\"\">");
	ts.WriteLine("    </milestone>");
	ts.WriteLine("  </module.milestones>");
	ts.WriteLine("");
	ts.WriteLine("  <module.checklist>");
	ts.WriteLine("    <status section=\"module.header\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("    </status>");
	ts.WriteLine("    ");
	ts.WriteLine("    <status section=\"module.intro\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("        Reviewed by PLCS");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("    ");
	ts.WriteLine("    <status section=\"mapping\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"arm.xml\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"arm_expressg\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"arm.exp\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"arm_lf.exp\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"mim.xml\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"mim_expressg\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"mim.exp\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("");
	ts.WriteLine("    <status section=\"mim_lf.exp\"");
	ts.WriteLine("      by=\"\"");
	ts.WriteLine("      date=\"\"");
	ts.WriteLine("      status=\"in-work\">");
	ts.WriteLine("      <description>");
	ts.WriteLine("      </description>");
	ts.WriteLine("    </status>");
	ts.WriteLine("  </module.checklist>");
	if (!milestones) ts.WriteLine("-->");
	ts.WriteLine("</management>");
	ts.Close();
    }
    if (!fso.FileExists(issuesXML)) {
	fso.CreateTextFile(issuesXML, true );
	f = fso.GetFile(issuesXML);
	ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
	ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	ts.WriteLine("<!-- $Id$ -->");
	ts.WriteLine("<?xml-stylesheet type=\"text/xsl\" href=\"../../../../xsl/projmg/issues_file.xsl\"?>");
  	ts.WriteLine("<!DOCTYPE issues SYSTEM \"../../../../dtd/projmg/issues.dtd\">");
	ts.WriteLine("<issues module=\""+module+"\">");
	ts.WriteLine("");
	ts.WriteLine("<!--");
	ts.WriteLine("Description of how the issues files is given in: stepmod\\help\\issues.htm");
	ts.WriteLine(" id - an identifer of the isssue unique to this file");
	ts.WriteLine(" type - the primary XML element in module.xml that the issue is against.");
	ts.WriteLine("        Either: ");
	ts.WriteLine("            general | keywords | contacts | purpose |");
	ts.WriteLine("            inscope | outscope | normrefs | definition |");
	ts.WriteLine("            abbreviations | arm | armexpg | arm_lf |");
	ts.WriteLine("            armexpg_lf | mapping_table | mim  | mimexpg |");
	ts.WriteLine("            mim_lf | mimexpg_lf | usage_guide | bibliography");
	ts.WriteLine(" linkend - the target of the comment ");
	ts.WriteLine(" category - editorial | minor_technical | major_technical | repository ");
	ts.WriteLine(" by - person raising the issue");
	ts.WriteLine(" date - date issue raised yy-mm-dd");
	ts.WriteLine(" status - status of issue. Either \"open\" or \"closed\"");
	ts.WriteLine(" seds - if \"yes\" then the issue has been raised as a SEDS.");
	ts.WriteLine("        The id should be the id of the SEDS in the SC4 database");
	ts.WriteLine("");
	ts.WriteLine("Comment - is a comment raised by someone about the issue");
	ts.WriteLine("");
	ts.WriteLine("<issue");
	ts.WriteLine("  id=\"\"");
	ts.WriteLine("  type=\"\"");
	ts.WriteLine("  linkend=\"\"");
	ts.WriteLine("  category=\"\"");
	ts.WriteLine("  by=\"\"");
	ts.WriteLine("  date=\"\"");
	ts.WriteLine("  status=\"open\"");
	ts.WriteLine("  seds=\"no\">");
	ts.WriteLine("  <description>");
    	ts.WriteLine("");
	ts.WriteLine("  </description>");
    	ts.WriteLine("");
    	ts.WriteLine("<comment");
	ts.WriteLine("   by=\"\" ");
    	ts.WriteLine("   date=\"\">");
	ts.WriteLine("<description>");
    	ts.WriteLine("</description>");
	ts.WriteLine("</comment>");
	ts.WriteLine(" </issue>");
	ts.WriteLine("-->");
	ts.WriteLine("");
	ts.WriteLine("<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->");
	ts.WriteLine("<!-- +++++++++++++++++++   ISSUES                  ++++++++++++++ -->");
	ts.WriteLine("<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->");
	ts.WriteLine("");
	ts.WriteLine("</issues>");
	ts.Close();
    }
}


function MakeModuleClause(module, clause) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var modFldr = GetModuleDir(module);
    var modSysFldr = modFldr+"sys/";
    var clauseXML = modSysFldr+clause+".xml";
    var clauseXSL = "sect_"+clause+".xsl";

    var fso = new ActiveXObject("Scripting.FileSystemObject");
    if (fso.FolderExists(modSysFldr)) {
	if (!fso.FileExists(clauseXML)) { 
	    //userMessage("Creating "+clauseXML);
	    fso.CreateTextFile( clauseXML, true );
	    var f = fso.GetFile(clauseXML);
	    var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
	    
	    ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    ts.WriteLine("<!-- $Id$ -->");
	    ts.WriteLine("<!DOCTYPE module_clause SYSTEM \"../../../../dtd/module_clause.dtd\">");
	    ts.WriteLine("<?xml-stylesheet type=\"text/xsl\"");
	    ts.WriteLine("href=\"../../../../xsl/" + clauseXSL + "\" ?>");
	    ts.WriteLine("<module_clause directory=\"" + module + "\"/>");
	    
	    ts.Close();
	}
    }
}


function MakeModuleXML(module, long_form, partNo) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var modFldr = GetModuleDir(module);
    var armOrMimXML = modFldr+"module.xml";

    //userMessage("Creating "+armOrMimXML);
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    fso.CreateTextFile( armOrMimXML, true );
    var f = fso.GetFile(armOrMimXML);
    var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
    
    ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ts.WriteLine("<!-- $Id$ -->");
    ts.WriteLine("<!DOCTYPE module SYSTEM \"../../../dtd/module.dtd\">");
    //ts.WriteLine("<?xml-stylesheet type=\"text/xsl\"");
    //ts.WriteLine("href=\"../../../xsl/express.xsl\" ?>");
    ts.WriteLine("<!-- Generated by mkmodule.js, Eurostep Limited, http://www.eurostep.com -->");
    ts.WriteLine(" <!-- ");
    ts.WriteLine("     To view the module in IExplorer, open: sys/1_scope.xml");
    ts.WriteLine("      -->");
    ts.WriteLine("<module");
    ts.WriteLine("   name=\"" + module + "\"");
    ts.WriteLine("   part=\""+partNo+"\"");
    ts.WriteLine("   version=\"1\"");
    ts.WriteLine("   sc4.working_group=\"12\"");
    ts.WriteLine("   wg.number=\"00000\"");
    ts.WriteLine("   wg.number.arm=\"\"");
    ts.WriteLine("   wg.number.mim=\"\"");

//    ts.WriteLine("   wg.number.ballot_package=\"\"");
//    ts.WriteLine("   wg.number.ballot_comment=\"\"");

    ts.WriteLine("   checklist.internal_review=\"\"");
    ts.WriteLine("   checklist.project_leader=\"\"");
    ts.WriteLine("   checklist.convener=\"\"");
    ts.WriteLine("   status=\"CD-TS\"");
    ts.WriteLine("   language=\"E\"");
    ts.WriteLine("   publication.year=\"\"");
    ts.WriteLine("   published=\"n\"");
    var rcsdate = "$"+"Date: $";
    ts.WriteLine("   rcs.date=\""+rcsdate+"\"");
    var rcsrevision = "$"+"Revision: $";

    //ts.WriteLine("   rcs.revision=\""+rcsrevision+"\">");
    
    ts.WriteLine("   rcs.revision=\""+rcsrevision+"\"");
    ts.WriteLine("   development.folder=\"dvlp\">");
    ts.WriteLine("");
    ts.WriteLine(" <keywords>");
    ts.WriteLine("    module");
    ts.WriteLine(" </keywords>");
    ts.WriteLine("");
    ts.WriteLine("<!-- the abstract for the module. If not provided, the XSL will use the in scope -->");
    ts.WriteLine(" <abstract>");
    ts.WriteLine("    <li>xxxxx</li>");
    ts.WriteLine(" </abstract>");

    ts.WriteLine("");
    ts.WriteLine(" <!-- Reference to contacts detailed in stepmod/data/basic/contacts.xml -->");
    ts.WriteLine(" <contacts>");
    // for pdmmodules
    //ts.WriteLine("   <projlead ref=\"pdmmodules.projlead\"/>");
    //ts.WriteLine("   <editor ref=\"pdmmodules.editor\"/>");

    ts.WriteLine("   <projlead ref=\"xxx\"/>");
    ts.WriteLine("   <editor ref=\"xxx\"/>");
    ts.WriteLine(" </contacts>");
    ts.WriteLine("");
    ts.WriteLine(" <!-- Introduction -->");
    ts.WriteLine(" <!-- The introduction should start as shown: -->");
    ts.WriteLine(" <purpose>");
    ts.WriteLine("   <p>");
    ts.WriteLine("     This part of ISO 10303 specifies an application module for the");
    ts.WriteLine("     representation of ");
    ts.WriteLine("   </p>");
    ts.WriteLine(" </purpose>");
    ts.WriteLine("");
    ts.WriteLine(" <!-- Items in scope -->");
    ts.WriteLine(" <inscope>");
    ts.WriteLine("   <li>xxxxx</li>");
    ts.WriteLine(" </inscope>");
    ts.WriteLine("");
    ts.WriteLine(" <!-- Items out of scope -->");
    ts.WriteLine(" <outscope>");
    ts.WriteLine("   <li>xxxx</li>");
    ts.WriteLine(" </outscope>");
    ts.WriteLine("");
    ts.WriteLine("<!--");
    ts.WriteLine(" <normrefs/>");
    ts.WriteLine("");
    ts.WriteLine(" <definition/>");
    ts.WriteLine("");
    ts.WriteLine(" <abbreviations/>");
    ts.WriteLine("-->");
    ts.WriteLine("");
    ts.WriteLine(" <!-- Clause 4 ARM  -->");
    ts.WriteLine(" <arm>");
    ts.WriteLine("   <!-- Note ARM short form EXPRESS is in arm.xml -->");
    // UOFs no longer required
    //ts.WriteLine("   <!-- Units of functionality -->");
    //ts.WriteLine("   <uof name=\"-\">");
    //ts.WriteLine("     <description>");
    //ts.WriteLine("     </description>");
    //ts.WriteLine("     <uof.ae entity=\"-\"/>");
    //ts.WriteLine("   </uof>");
    ts.WriteLine("");
    ts.WriteLine("   <!-- Short form EXPRESS-G -->");
    ts.WriteLine("   <express-g>");
    ts.WriteLine("     <imgfile file=\"armexpg1.xml\"/>");
    ts.WriteLine("   </express-g>");
    ts.WriteLine(" </arm>");
    ts.WriteLine("");
    if (long_form == true) {
	ts.WriteLine(" <!-- ARM long form (optional) -->");
	ts.WriteLine(" <!-- If not required, delete this section and the following files:");
	ts.WriteLine("          arm_lf.xml");
	ts.WriteLine("          armexpg_lf1.gif");
	ts.WriteLine("          armexpg_lf1.xml -->");
	ts.WriteLine(" <arm_lf>");
	ts.WriteLine("   <!-- Note ARM long form EXPRESS is in arm_lf.xml -->");
	ts.WriteLine("   <express-g>");
	ts.WriteLine("     <imgfile file=\"armexpg_lf1.xml\"/>");
	ts.WriteLine("   </express-g>");
	ts.WriteLine(" </arm_lf>");
    }
    ts.WriteLine("");
    ts.WriteLine(" <!-- Clause 5.1 Mapping specification -->");
    ts.WriteLine(" <mapping_table>");
    ts.WriteLine("   <ae entity=\"xx\"/>");
    ts.WriteLine(" </mapping_table>");

    ts.WriteLine("");
    ts.WriteLine(" <!-- Clause 5.2 MIM -->");
    ts.WriteLine(" <mim>");
    ts.WriteLine("   <!--  Note MIM short form express is in mim.xml -->");
    ts.WriteLine("   <express-g>");
    ts.WriteLine("     <imgfile file=\"mimexpg1.xml\"/>");
    ts.WriteLine("   </express-g>");
    ts.WriteLine(" </mim>");
    ts.WriteLine("");

    if (long_form == true) {
	ts.WriteLine(" <!-- MIM long form (optional) -->");
	ts.WriteLine(" <!-- If not required, delete this section and the following files:");
	ts.WriteLine("          mim_lf.xml");
	ts.WriteLine("          mimexpg_lf1.gif");
	ts.WriteLine("          mimexpg_lf1.xml -->");
	ts.WriteLine(" <mim_lf>");
	ts.WriteLine("   <!-- Note MIM long form EXPRESS is in mim_lf.xml -->");
	ts.WriteLine("   <express-g>");
	ts.WriteLine("     <imgfile file=\"mimexpg_lf1.xml\"/>");
	ts.WriteLine("   </express-g>");
	ts.WriteLine(" </mim_lf>");
	ts.WriteLine("");
    }
    ts.WriteLine("</module>");
    ts.Close();
}

function MakeExpressG(module, expgfile, title) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var modFldr = GetModuleDir(module);
    var expg = modFldr+expgfile;
    var expg_gif = expgfile.replace(".xml",".gif");

    var fso = new ActiveXObject("Scripting.FileSystemObject");
    fso.CreateTextFile( expg, true );
    var f = fso.GetFile(expg);
    var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
    
    ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ts.WriteLine("<!-- $Id$ -->");
    ts.WriteLine("<!DOCTYPE imgfile.content SYSTEM \"../../../dtd/text.ent\">");
    ts.WriteLine("<?xml-stylesheet type=\"text/xsl\"");
    ts.WriteLine("    href=\"../../../xsl/imgfile.xsl\"?>");
    ts.WriteLine("<imgfile.content");
    ts.WriteLine("  module=\""+module+"\"");
    ts.WriteLine("  file=\""+expgfile+"\">");
    ts.WriteLine("  <img src=\""+expg_gif+"\">");
    ts.WriteLine("  </img>");
    ts.WriteLine("</imgfile.content>");
    ts.Close();

    // Copy across underconstruction image
    expg_gif = expg.replace(".xml",".gif");
    fso.CopyFile(stepmodHome+"/images/underconstruction.gif", expg_gif);
}


function MakeExpress(module, armOrMim) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var modFldr = GetModuleDir(module);
    var armOrMimExp = modFldr+armOrMim+".exp";

    //userMessage("Creating "+armOrMimXML);
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    fso.CreateTextFile( armOrMimExp, true );
    var f = fso.GetFile(armOrMimExp);
    var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
    
    ts.WriteLine("(*");
    ts.WriteLine("   $Id$");
    ts.Write("   N - ISO/CD-TS - 10303- ");
    ts.Write(module);
    ts.Write(" - EXPRESS ");
    ts.Write(armOrMim.toUpperCase());
    ts.WriteLine("*)");
    ts.WriteLine("(* UNDER DEVELOPMENT *)");
    var schema = schemaName(module,armOrMim);
    ts.WriteLine("SCHEMA "+schema+";");
    ts.WriteLine("END_SCHEMA;");
    ts.Close();
}


function MakeIndexXML(module) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var modFldr = GetModuleDir(module);
    var indexXML = modFldr+"index.xml";

    //userMessage("Creating "+indexXML);
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    fso.CreateTextFile( indexXML, true );
    var f = fso.GetFile(indexXML);
    var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);    
    ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ts.WriteLine("<!-- $Id$ -->");
    ts.WriteLine("<!DOCTYPE module_clause SYSTEM \"../../../dtd/module_clause.dtd\">");
    ts.WriteLine("<?xml-stylesheet type=\"text/xsl\" href=\"../../../xsl/index.xsl\" ?>");
    ts.WriteLine("<!-- do not edit this file -->");
    ts.WriteLine("<module_clause directory=\"index\"/>");
    ts.Close();
}


function MakeExpressXML(module, armOrMim) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var modFldr = GetModuleDir(module);
    var armOrMimXML = modFldr+armOrMim+".xml";

    //userMessage("Creating "+armOrMimXML);
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    fso.CreateTextFile( armOrMimXML, true );
    var f = fso.GetFile(armOrMimXML);
    var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
    
    ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ts.WriteLine("<!-- $Id$ -->");
    ts.WriteLine("<!DOCTYPE express SYSTEM \"../../../dtd/express.dtd\">");
    ts.WriteLine("<?xml-stylesheet type=\"text/xsl\"");
    ts.WriteLine("href=\"../../../xsl/express.xsl\" ?>");
    ts.WriteLine("<express");
    ts.WriteLine("   language_version=\"2\"");
    ts.WriteLine("   rcs.date=\"$Date$\"");
    ts.WriteLine("   rcs.revision=\"$Revision$\">");
    var schema = schemaName(module,armOrMim);
    ts.WriteLine("  <schema name=\""+schema+"\">");
    ts.WriteLine("  </schema>");
    ts.WriteLine("</express>");
    ts.Close();
}


function NameModule(module) {

    // Use a regular expression to replace leading and trailing 
    // spaces with the empty string
     var modName = module.replace(/(^\s*)|(\s*$)/g, "");

    // name must all be lower case
     modName = modName.toLowerCase();
    // no whitespace replace with  _
    re = / /g;
    modName = modName.replace(re, "_");
    
    // no - replace with  _
    re = /-/g;
    modName = modName.replace(re, "_");

    re = /__*/g;
    modName = modName.replace(re, "_");
    return(modName);
} 


// update all the sub folders of a module.
function UpdateModule(module) {
    // make sure module has a valid name
    module = NameModule(module);
    var modFldr = GetModuleDir(module);
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    if (fso.FolderExists(modFldr)) {
	for (var i=0; i<moduleClauses.length; i++) {
	    MakeModuleClause(module, moduleClauses[i]);
	}
	MakeIndexXML(module);
	MakeDvlpFldr(module, "no");
	MakeNavFldr(module);
    } else {
	ErrorMessage(module+" does not exist as module");
    }
}

function MakeModule(module, long_form, partNo) {
    // make sure module has a valid name
    module = NameModule(module);
    var modFldr = GetModuleDir(module);
    var modSysFldr = modFldr+"sys/";
    var fso = new ActiveXObject("Scripting.FileSystemObject");

    userMessage("Creating module "+module);
    try {
	CheckModule(module);

	fso.CreateFolder(modFldr);
	fso.CreateFolder(modSysFldr);	
	MakeDvlpFldr(module);
	MakeNavFldr(module);
	for (var i=0; i<moduleClauses.length; i++) {
	    MakeModuleClause(module, moduleClauses[i]);
	}
	MakeIndexXML(module);
	MakeExpressG(module,"armexpg1.xml");
	MakeExpressG(module,"mimexpg1.xml");
	MakeExpressXML(module,"arm");
	MakeExpressXML(module,"mim");
	MakeExpress(module,"arm");
	MakeExpress(module,"mim");
	
	if (long_form == true) {
	    MakeExpressG(module,"armexpg_lf1.xml");
	    MakeExpressG(module,"mimexpg_lf1.xml");
	    MakeExpressXML(module,"arm_lf");
	    MakeExpressXML(module,"mim_lf");
	    MakeExpress(module,"arm_lf");
	    MakeExpress(module,"mim_lf");
	    MakeP28files(module);
	}

	MakeModuleXML(module, long_form, partNo);
	userMessage("Created module:   "+module);
    }
    catch(e) {
	ErrorMessage(e);
    }    
}

//List all directories that do not have a support directory
// i.e. nav dvlp sys
function MakeSupportDirs(support) {
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    var modulesFldr = fso.GetFolder(stepmodHome+"/data/modules");
    var fc = new Enumerator(modulesFldr.SubFolders);
    // Iterate though all the modules
    for (; !fc.atEnd(); fc.moveNext()) {
	var f = fc.item();
	var supportFldr = f+"\\"+support;
	if (!fso.FolderExists(supportFldr)) {
	    userMessage(supportFldr);
	}
    }
}

//------------------------------------------------------------
// P28 support files
function MakeP28files(module) {
    var ForReading = 1, ForWriting = 2, ForAppending = 8;
    var TristateUseDefault = -2, TristateTrue = -1, TristateFalse = 0;
    var modFldr = GetModuleDir(module);

    // impl.xml
    var implXML = modFldr+"impl.xml";
    userMessage("Creating "+implXML);
    var fso = new ActiveXObject("Scripting.FileSystemObject");
    fso.CreateTextFile(implXML, true );
    var f = fso.GetFile(implXML);
    var ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
    ts.WriteLine("<!-- $Id$ -->");
    ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ts.WriteLine("<!DOCTYPE implementation_methods SYSTEM \"../../../dtd/impl.dtd\">");
    ts.WriteLine("<?xml-stylesheet type=\"text/xsl\" href=\"../../../xsl/p28xsd/impl.xsl\"?>");
    ts.WriteLine("<implementation_methods module=\""+module+"\">");
    ts.WriteLine("        <p28xsd/>");
    ts.WriteLine("</implementation_methods>");
    ts.Close();

    // p28_config.xml
    var p28configXML = modFldr+"p28_config.xml";
    fso.CreateTextFile(p28configXML, true );
    f = fso.GetFile(p28configXML);
    ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
    ts.WriteLine("<!-- $Id$ -->");
    ts.WriteLine("<!-- NOTE This contains a list of instantiable ANDOR permutations.  ");
    ts.WriteLine("It will work if empty (as above) but will then create XSD as though the EXPRESS ");
    ts.WriteLine("contained only ONEOF graphs.  -->");
    ts.WriteLine("<ex:configuration id=\""+module+"\" xmlns:ex=\"urn:iso10303-28:ex\"/>");
    ts.Close();

    // stepmod_namespace.xml
    var stepmodNamespaceXML = modFldr+"stepmod_namespace.xml";
    fso.CreateTextFile(stepmodNamespaceXML, true );
    f = fso.GetFile(stepmodNamespaceXML);
    var moduleName = module.substr(0,1).toUpperCase()+module.substr(1,module.length);
    ts = f.OpenAsTextStream(ForWriting, TristateUseDefault);
    ts.WriteLine("<!-- $Id$ -->");
    ts.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    ts.WriteLine("<dummy xmlns:pdm=\"urn:iso10303-28:xs/"+moduleName+"\" schema_name=\""+moduleName+"\" ns_prefix_name=\""+module+"\"/>");   

}

//------------------------------------------------------------

function Main() {
    var cArgs = WScript.Arguments;
    if (CheckArgs() && CheckStepHome() ) {
	var cArgs = WScript.Arguments;
	var module=cArgs(0);
	var long_form=false;
	if (cArgs.length == 2) {
	    long_form=true;
	}
	try {
	    MakeModule(module, long_form, 'xxxx');
	}
	catch(e) {
	    ErrorMessage(e);
	}
    }
}

function MainDvlpWindow(module) {
    var objShell = WScript.CreateObject("WScript.Shell");
    var modName = NameModule(module);
    if (modName.length > 1) {

	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var modFldr = GetModuleDir(modName);
	var modDvlpFldr = modFldr+"dvlp/";
	if (!fso.FolderExists(modFldr)) {
	    objShell.Popup("Module "+modName+" does not exist");
	} else {
	    var intRet = objShell.Popup("You are about to create a dvlp directory in module: "+module,0, "Creating DVLP", 49);
	    if (intRet == 1) {
		// OK
		MakeDvlpFldr(module);
		objShell.Popup("Created dvlp folder: "+modDvlpFldr+"\n"+
			       "  ProjMg: stepmod/data/modules/"+modName+"/dvlp/projmg.xml\n"+
			       "  Issues: stepmod/data/modules/"+modName+"/dvlp/issues.xml\n"+
			       "  Add    development.folder=\"dvlp\" to module.xml\n",
			       0, "Created module", 64);
	    }
	}
    } else {
	objShell.Popup("You must enter a module name");
    }
}


function MainNavWindow(module) {
    var objShell = WScript.CreateObject("WScript.Shell");
    var modName = NameModule(module);
    if (modName.length > 1) {

	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var modFldr = GetModuleDir(modName);
	var modNavFldr = modFldr+"nav/";
	if (!fso.FolderExists(modFldr)) {
	    objShell.Popup("Module "+modName+" does not exist");
	} else {
	    var intRet = objShell.Popup("You are about to create a nav directory in module: "+module,0, "Creating NAV", 49);
	    if (intRet == 1) {
		// OK
		MakeNavFldr(module);
		objShell.Popup("Created nav folder: "+modNavFldr+"\n"+
			       "  ProjMg: stepmod/data/modules/"+modName+"/nav/projmg.xml\n"+
			       "  Issues: stepmod/data/modules/"+modName+"/nav/issues.xml\n"+
			       "  Add    development.folder=\"nav\" to module.xml\n",
			       0, "Created module", 64);
	    }
	}
    } else {
	objShell.Popup("You must enter a module name");
    }
}

function MainUpdateWindow(module) {
    var objShell = WScript.CreateObject("WScript.Shell");
    var modName = NameModule(module);
    if (modName.length > 1) {

	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var modFldr = GetModuleDir(modName);
	if (!fso.FolderExists(modFldr)) {
	    objShell.Popup("Module "+modName+" does not exist");
	} else {
	    var intRet = objShell.Popup("You are about to update the sub directories (sys, dvlp, nav) of the module: "+module+"\nThese are system files",0, "Updating module", 49);
	    if (intRet == 1) {
		// OK
		UpdateModule(module);
		objShell.Popup("Update the sys,dvlp and nav folders of "+modName, 0, "Updated module", 64);
	    }
	}
    } else {
	objShell.Popup("You must enter a module name");
    }
}



function MainWindow(module) {
    var objShell = WScript.CreateObject("WScript.Shell");
    var modName = NameModule(module);
    if (modName.length > 1) {
	var long_form = false;	
    	MakeModule(module, long_form, 'xxxx');
    } else {
	objShell.Popup("You must enter a module name");
    }
}


//Main();


//MakeP28files("test1");
//MakeDvlpFldr("contract");
//MakeNavFldr("activity");
//MakeModuleClause("activity","abstract");
//UpdateModule("catalog_data_information");
//    MakeNavFile(modNavFldr+"select_matrix_view.xml", module, "select_matrix_view.xsl");
