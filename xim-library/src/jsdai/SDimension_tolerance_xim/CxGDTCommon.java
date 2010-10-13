package jsdai.SDimension_tolerance_xim;

import jsdai.SExtended_geometric_tolerance_xim.EExternally_defined_dimension_representation;
import jsdai.SExtended_geometric_tolerance_xim.ELimit_dimension_representation;
import jsdai.SExtended_geometric_tolerance_xim.ESingular_dimension_representation;
import jsdai.SGeometry_schema.EPlacement;
import jsdai.SProduct_property_definition_schema.AProperty_definition;
import jsdai.SProduct_property_definition_schema.CProperty_definition;
import jsdai.SProduct_property_definition_schema.EProperty_definition;
import jsdai.SProduct_property_representation_schema.AProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.CProperty_definition_representation;
import jsdai.SProduct_property_representation_schema.EProperty_definition_representation;
import jsdai.SQualified_measure_schema.EDescriptive_representation_item;
import jsdai.SQualified_measure_schema.EMeasure_representation_item;
import jsdai.SRepresentation_schema.ARepresentation;
import jsdai.SRepresentation_schema.ARepresentation_item;
import jsdai.SRepresentation_schema.CRepresentation;
import jsdai.SRepresentation_schema.ERepresentation;
import jsdai.SRepresentation_schema.ERepresentation_item;
import jsdai.SShape_dimension_schema.ADimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.CShape_dimension_representation;
import jsdai.SShape_dimension_schema.EDimensional_characteristic_representation;
import jsdai.SShape_dimension_schema.EShape_dimension_representation;
import jsdai.lang.EEntity;
import jsdai.lang.SdaiContext;
import jsdai.lang.SdaiException;
import jsdai.libutil.CxAP210ARMUtilities;
import jsdai.util.LangUtils;

public class CxGDTCommon {
/* No longer used
	public static EShape_aspect getSuitableShape_aspect(SdaiContext context, EEntity value) throws SdaiException {
		ERepresentation suitableRep = null;
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinItems(null, (ERepresentation_item)value, context.domain, ar);
		for(int i=1,count=ar.getMemberCount(); i<=count; i++){
			ERepresentation er = ar.getByIndex(i);
			// if this item is not alone here - it is not suitable and maybe ambiguous, so we need to create another one
			if(er.getItems(null).getMemberCount() == 1){
				suitableRep = er;
				break;
			}
		}
		if(suitableRep == null){
			suitableRep = CxAP210ARMUtilities.createRepresentation(context, "", false);
			ARepresentation_item items = suitableRep.getItems(null);
			items.addUnordered(value);
		}
		return getSuitableShape_aspect(context, suitableRep);
	}
	
	public static EShape_aspect getSuitableShape_aspect(SdaiContext context, ERepresentation suitableRep) throws SdaiException {
		AProperty_definition_representation apdr = new AProperty_definition_representation();
		CProperty_definition_representation.usedinUsed_representation(null, suitableRep, context.domain, apdr);
		EShape_aspect esa = null;
		for(int i=1,count=apdr.getMemberCount(); i<=count; i++){
			EProperty_definition_representation epdr = apdr.getByIndex(i);
			EEntity ee = epdr.getDefinition(null);
			if(ee instanceof EProperty_definition){
				EProperty_definition epd = (EProperty_definition)ee;
				EEntity ee2 = epd.getDefinition(null);
				if(ee2 instanceof EShape_aspect){
					return (EShape_aspect)ee2;
				}
			}
		}
		if(esa == null){
			EProperty_definition_representation epdr = (EProperty_definition_representation)
				context.working_model.createEntityInstance(CProperty_definition_representation.definition);
			epdr.setUsed_representation(null, suitableRep);
			EProperty_definition epd = (EProperty_definition)
				context.working_model.createEntityInstance(CProperty_definition.definition);
			epd.setName(null, "");
			epdr.setDefinition(null, epd);
			esa = (EShape_aspect)
				context.working_model.createEntityInstance(CShape_aspect.definition);
			esa.setName(null, "");
			esa.setProduct_definitional(null, ELogical.UNKNOWN);
			epd.setDefinition(null, esa);
		}
		return esa;
	}
*/
	public static void setOrientation(SdaiContext context, EEntity armEntity, EPlacement ep)
	throws SdaiException {
		ep.setName(null, "orientation");
		ARepresentation ar = new ARepresentation();
		CRepresentation.usedinItems(null, ep, context.domain, ar);
		ERepresentation suitableRep;
		if(ar.getMemberCount() > 0){
			suitableRep = ar.getByIndex(1);
		}else{
			suitableRep = (ERepresentation) 
				CxAP210ARMUtilities.createRepresentation(context, "", false);
			suitableRep.getItems(null).addUnordered(ep);
		}
		// PD
		LangUtils.Attribute_and_value_structure[] pdStructure = {
				new LangUtils.Attribute_and_value_structure(
						CProperty_definition.attributeDefinition(null), armEntity)
		};
		EProperty_definition epd = (EProperty_definition) 
			LangUtils.createInstanceIfNeeded(context,
					CProperty_definition.definition, pdStructure);
		if(!epd.testName(null)){
			epd.setName(null, "");
		}
		// PDR
		EProperty_definition_representation epdr = (EProperty_definition_representation)
			context.working_model.createEntityInstance(CProperty_definition_representation.definition);
		epdr.setDefinition(null, epd);
		epdr.setUsed_representation(null, suitableRep);
	}
	
	public static void unsetOrientation(SdaiContext context, EEntity armEntity) throws SdaiException {
		AProperty_definition apd = new AProperty_definition();
		CProperty_definition.usedinDefinition(null, armEntity, context.domain, apd);
		for(int i=1; i<=apd.getMemberCount(); i++){
			EProperty_definition epd = apd.getByIndex(i);
			AProperty_definition_representation apdr = new AProperty_definition_representation();
			CProperty_definition_representation.usedinDefinition(null, epd, context.domain, apdr);
			for(int j=1,count2=apdr.getMemberCount(); j<=count2; j++){
				EProperty_definition_representation epdr = apdr.getByIndex(j);
				epdr.deleteApplicationInstance();
			}
		}
	}
	
	public static EShape_dimension_representation setDimension_value(SdaiContext context,EEntity armEntity, ERepresentation_item value) throws SdaiException {
//		if(value instanceof EDimension_value_with_limitation){
//			EDimension_value_with_limitation edvwl = (EDimension_value_with_limitation)value;
//			edvwl.setToleranced_dimension(null, armEntity);
//			return;
//		}
		// According last agreement - there should be just one DCR/SDR per one user
		// TODO - cache it
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		EDimensional_characteristic_representation edcr;
		EShape_dimension_representation esdr;
		if(adcr.getMemberCount() > 0){
			edcr = adcr.getByIndex(1);
			esdr = edcr.getRepresentation(null);
		}
		else{
			edcr = (EDimensional_characteristic_representation)context.working_model.createEntityInstance(CDimensional_characteristic_representation.definition);
			edcr.setDimension(null, armEntity);
			esdr = (EShape_dimension_representation) 
				CxAP210ARMUtilities.createRepresentation(context, CShape_dimension_representation.definition, "", false);
			edcr.setRepresentation(null, esdr);
		}
		if(value != null){
			esdr.getItems(null).addUnordered(value);
		}
		return esdr;
	}

	public static void unsetAuxiliary(SdaiContext context, EEntity armEntity)throws SdaiException{
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			ARepresentation_item items = esdr.getItems(null);
			for(int j=1; j<=items.getMemberCount();j++){
				ERepresentation_item item = items.getByIndex(j);
				if(item instanceof EDescriptive_representation_item){
					if(item.getName(null).equals("dimensional note")){
						EDescriptive_representation_item edri = (EDescriptive_representation_item)item;
						String desc = edri.getDescription(null);
						if(desc.equals("auxiliary")){
							edri.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}
		
	public static void unsetTheoretical_exact(SdaiContext context, EEntity armEntity)throws SdaiException{
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			ARepresentation_item items = esdr.getItems(null);
			for(int j=1; j<=items.getMemberCount();j++){
				ERepresentation_item item = items.getByIndex(j);
				if(item instanceof EDescriptive_representation_item){
					if(item.getName(null).equals("dimensional note")){
						EDescriptive_representation_item edri = (EDescriptive_representation_item)item;
						String desc = edri.getDescription(null);
						if(desc.equals("theoretical")){
							edri.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}
		
	public static void unsetNotes(SdaiContext context, EEntity armEntity)throws SdaiException{
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			ARepresentation_item items = esdr.getItems(null);
			for(int j=1; j<=items.getMemberCount();j++){
				ERepresentation_item item = items.getByIndex(j);
				if(item instanceof EDescriptive_representation_item){
					if(item.getName(null).equals("dimensional note")){
						EDescriptive_representation_item edri = (EDescriptive_representation_item)item;
						String desc = edri.getDescription(null);
						if((!desc.equals("theoretical"))&&(!desc.equals("auxiliary"))){
							edri.deleteApplicationInstance();
						}
					}
				}
			}
		}
	}
	
	public static void unsetUpper_range(SdaiContext context, EEntity armEntity)throws SdaiException{
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			ARepresentation_item items = esdr.getItems(null);
			for(int j=1; j<=items.getMemberCount();){
				ERepresentation_item item = items.getByIndex(j);
				if(item instanceof EMeasure_representation_item){
					String name = item.getName(null);
					if(name.equals("upper range")){
						items.removeUnordered(item);
					}else{
						j++;
					}
				}else{
					j++;
				}
			}
		}
	}
	
	public static void unsetLower_range(SdaiContext context, EEntity armEntity)throws SdaiException{
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			ARepresentation_item items = esdr.getItems(null);
			for(int j=1; j<=items.getMemberCount();){
				ERepresentation_item item = items.getByIndex(j);
				if(item instanceof EMeasure_representation_item){
					String name = item.getName(null);
					if(name.equals("lower range")){
						items.removeUnordered(item);
					}else{
						j++;
					}
				}else{
					j++;
				}
			}
		}
	}	
	
	public static void unsetSingle_value(SdaiContext context, EEntity armEntity)throws SdaiException{
		ADimensional_characteristic_representation adcr = new ADimensional_characteristic_representation();
		CDimensional_characteristic_representation.usedinDimension(null, armEntity, context.domain, adcr);
		for(int i=1,count=adcr.getMemberCount(); i<=count; i++){
			EDimensional_characteristic_representation edcr = adcr.getByIndex(i);
			EShape_dimension_representation esdr = edcr.getRepresentation(null);
			ARepresentation_item items = esdr.getItems(null);
			for(int j=1; j<=items.getMemberCount();){
				ERepresentation_item item = items.getByIndex(j);
				if(item instanceof EMeasure_representation_item){
					String name = item.getName(null);
					if((name.equals("lower range"))||(name.equals("upper range"))){
						j++;
					}else{
						items.removeUnordered(item);
					}
				}else{
					j++;
				}
			}
		}
	}
	
	public static boolean isSubtypeNonModifiable(EShape_dimension_representation esdr) {
		// All those subtypes redeclares items, so we can't use them
	    if(esdr instanceof EExternally_defined_dimension_representation){
	    	return true;
	    }
	    if(esdr instanceof ELimit_dimension_representation){
	    	return true;
	    }
	    if(esdr instanceof ESingular_dimension_representation){
	    	return true;
	    }
//	    if(esdr instanceof ETolerance_range){
//	    	return true;
//	    }
		return false;
	}
	
}
