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

package jsdai.SProduct_categorization_xim;

/**
 * @author Giedrius Liutkus
 * @version $Revision$
 * $
 */

import jsdai.SProduct_definition_schema.*;
import jsdai.lang.*;
// import jsdai.util.*;

public class CxProduct_category_hierarchy
   extends CProduct_category_hierarchy {

   public void createAimData(SdaiContext context) throws SdaiException {
/*TODO      if (getModified()) {
         setModified(false);
      }
      else {
         return;
      }*/
		// 		commonForAIMInstanceCreation(context);
      setTemp("AIM", CProduct_category_relationship.definition);

      setMappingConstraints(context, this);

   }

   /**
    * Sets/creates data for mapping constraints.
    *
    * <p>
    *  mapping_constraints;
    * 	product_category_relationship
    *    {product_category_relationship.name = 'hierarchy'}
    *  end_mapping_constraints;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void setMappingConstraints(SdaiContext context, EProduct_category_hierarchy armEntity) throws SdaiException {
   	armEntity.setName(null, "");
   }

   /**
    * Unsets/deletes data for mapping constraints.
    *
    * <p>
    *  mapping_constraints;
    * 	product_category_relationship
    *    {product_category_relationship.name = 'hierarchy'}
    *  end_mapping_constraints;
    * </p>
    * @param context SdaiContext.
    * @param armEntity arm entity.
    * @throws SdaiException
    */
   public static void unsetMappingConstraints(SdaiContext context, EProduct_category_hierarchy armEntity) throws SdaiException {
   	armEntity.unsetName(null);
   }
   

}
