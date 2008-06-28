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

package jsdai.beans;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import jsdai.lang.*;
import jsdai.mapping.*;
import jsdai.dictionary.*;
import jsdai.util.*;

public class EntityMappingBean extends SdaiPanel
{
    String ident = "   ";
    ASdaiModel dataDomain;
    ASdaiModel mappingDomain;
    int mode = EEntity.NO_RESTRICTIONS;
    
    EEntity entity = null;
    EEntity_mapping mapping = null;
    
    Vector textes 		= new Vector();
    Vector attributs 	= new Vector();
    Vector types		= new Vector();
    Vector vunsets		= new Vector();
    
    Vector attributes = new Vector();
    
    JPanel pattr = new JPanel();
    
    MappingInversesTableModel inversesModel;
    GoTable inverses;
    
    JLabel labelAlternatives = new JLabel("Alternatives");
    GoTextField t_instance = new GoTextField();
    JComboBox alternatives = new JComboBox();
    boolean lockAlternatives = false;
    
    EEntity_mapping processed_mapping = null;
    
    JScrollPane sb;
    
    boolean isLockSplits = false;
    int splitLocation = 0;
    boolean isDeveloperMode = false;
    
    ArrayList splits = new ArrayList();
    boolean lockChangeListener = false;
    
    public EntityMappingBean()
    {
        setLayout(new BorderLayout());
        
        JPanel both = new JPanel(new BorderLayout());
        JPanel north = new JPanel(new BorderLayout());
        ((BorderLayout)north.getLayout()).setHgap(2);
        
        JPanel labels = new JPanel(new GridLayout(0, 1));
        ((GridLayout)labels.getLayout()).setVgap(5);
        JLabel label;
        label = (JLabel)labels.add(new JLabel("Instance"));
        label.setHorizontalAlignment(JLabel.RIGHT);
        labels.add(labelAlternatives);
        labelAlternatives.setHorizontalAlignment(JLabel.RIGHT);
//        labelAlternatives.addMouseListener(mouseListener);
//        labelAlternatives.addKeyListener(keyListener);
        labelAlternatives.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                Object o[] =
                {alternatives.getSelectedItem(), mappingDomain};
                fireGo(new GoEvent(thisis, o, "EntityInstance"));
            }
        });
        north.add(labels, BorderLayout.WEST);
        
        JPanel texts = new JPanel(new GridLayout(0, 1));
        ((GridLayout)texts.getLayout()).setVgap(5);
//        t_instance.addMouseListener(mouseListener);
//        t_instance.addFocusListener(focusListener);
        t_instance.addGoListener(goListener);
        t_instance.setUnderlying(true);
        t_instance.setEditable(false);
        texts.add(t_instance);
        JPanel pas = new JPanel(new BorderLayout());
        alternatives.setPreferredSize(t_instance.getPreferredSize());
        alternatives.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                EntityMappingBean emb = (EntityMappingBean)thisis;
                if (alternatives.getSelectedIndex() != -1 && !lockAlternatives)
                {
                    emb.setInstanceAndMappingAndDomain(entity, (EEntity_mapping)alternatives.getSelectedItem(), dataDomain);
                }
            }
        });
//        alternatives.addItemListener(new ItemListener()
//        {
//            public void itemStateChanged(ItemEvent e)
//            {
//                if(e.getStateChange() == ItemEvent.SELECTED)
//                {
//                    EntityMappingBean emb = (EntityMappingBean)thisis;
//                    emb.setInstanceAndMappingAndDomain(entity, (EEntity_mapping)e.getItem(), dataDomain);
//                }
//            }
//        });
        //		alternatives.addItemListener(itemListener);
        //		alternatives.addActionListener(actionListener);
        //		alternatives.addActionListener(new ActionListener() {
        //			public void actionPerformed(ActionEvent e) {
        //				EntityMappingBean emb = (EntityMappingBean)thisis;
        //				if ((alternatives.getSelectedIndex() != -1) && !lockAlternatives) {
        //				    emb.setInstance_and_mapping(entity, (EEntity_mapping)alternatives.getSelectedItem());
        //				}
        //			}
        //		});
        alternatives.setRenderer(new SdaiCellRenderer());
        pas.add(alternatives, BorderLayout.CENTER);
//        JButton b = new JButton("Change");
//        b.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                EntityMappingBean emb = (EntityMappingBean)thisis;
//                if (alternatives.getSelectedIndex() != -1)
//                {
//                    emb.setInstanceAndMappingAndDomain(entity, (EEntity_mapping)alternatives.getSelectedItem(), dataDomain);
//                }
//            }
//        });
//        b.setPreferredSize(new Dimension((int)b.getPreferredSize().getWidth(), 0));
//        pas.add(b, BorderLayout.EAST);
        texts.add(pas);
        north.add(texts, BorderLayout.CENTER);
        both.add(north, BorderLayout.NORTH);
        
        pattr.setLayout(new BoxLayout(pattr, BoxLayout.Y_AXIS));
        pattr.setBorder(new MatteBorder(1, 0, 0, 0, Color.darkGray));
        JPanel scroll = new JPanel(new BorderLayout());
        scroll.add(pattr, BorderLayout.NORTH);
        
        JPanel pa = new JPanel(new BorderLayout());
        pa.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), "Users"));
        
        inversesModel = new MappingInversesTableModel();
        TableSorter sorter = new TableSorter(inversesModel);
        inverses = new GoTable(sorter);
        sorter.addMouseListenerToHeaderInTable(inverses);
        inverses.setUnderlying(true);
        inverses.setReturnCount(2);
        inversesModel.fireTableStructureChanged();
        inverses.setBackground(getBackground());
        inverses.addGoListener(goListener);
        //		inverses.addMouseListener(mouseListener);
        //		inverses.getSelectionModel().addListSelectionListener(listListener);
        //		inverses.addFocusListener(focusListener);
        //		inverses.addKeyListener(findKeyListener);
        //		inverses.addKeyListener(keyListener);
        
        pa.add(new JScrollPane(inverses), BorderLayout.CENTER);
        scroll.add(pa, BorderLayout.CENTER);
        
        sb = new JScrollPane(scroll);
        sb.getVerticalScrollBar().setUnitIncrement(10);
        sb.getHorizontalScrollBar().setUnitIncrement(10);
        both.add(sb, BorderLayout.CENTER);
        add(both, BorderLayout.CENTER);
        
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton get = new JButton("Get");
        get.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Vector selection = new Vector();
                    getSelected(selection);
                    if (selection.size() > 0)
                    {
                        SdaiSession.getSession().getClipboard().addByIndex(SdaiSession.getSession().getClipboard().getMemberCount()+1, selection.elementAt(selection.size()-1), null);
                    }
/*
//						Object result = instances[ii].getMappedAttribute((EAttribute_mapping)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels());
 
                                        Vector selection = new Vector();
                                        getSelected(selection);
                                        System.out.println(selection);
                                        if (selection.size() > 0) {
                                                SdaiSession.getSession().getClipboard().addByIndex(SdaiSession.getSession().getClipboard().getMemberCount()+1, selection.elementAt(selection.size()-1), null);
                                        }*/
                } catch (SdaiException h)
                {processMessage(h);}
            }
        });
        //		bar.add(get);
        JButton set = new JButton("Set");
        set.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    sdaiNew();
                    //					entity.setMappedAttribute((EAttribute_mapping)encodeToken(tokens[1]), ((ASchemaInstance)encodeToken(tokens[2])).getAssociatedModels(), ((ASchemaInstance)encodeToken(tokens[3])).getAssociatedModels(), tokens[4]);
                } catch (SdaiException h)
                {processMessage(h);}
            }
        });
        //		bar.add(set);
        JButton unset = new JButton("Unset");
        unset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        //		bar.add(unset);
        add(bar, BorderLayout.SOUTH);
    }
    
    public EntityMappingBean(EEntity entity, EEntity_mapping mapping, ASdaiModel domain) throws SdaiException
    {
        this();
        setInstanceAndMappingAndDomain(entity, mapping, domain);
    }
    
	public void setInstanceAndMappingAndDomain(EEntity entity, EEntity_mapping mapping, ASdaiModel domain) {
		Class[] types = new Class[3];
		types[0] = EEntity.class;
		types[1] = EEntity_mapping.class;
		types[2] = ASdaiModel.class;

		Object[] params = new Object[3];
		params[0] = entity;
		params[1] = mapping;
		params[2] = domain;

		MappingOperationsThreadManager.invokeMethod(this, "setInstanceAndMappingAndDomainSingleThreaded",
													types, params);
	}

    public void setInstanceAndMappingAndDomainSingleThreaded(EEntity entity, EEntity_mapping mapping, ASdaiModel domain)
    {
        try
        {
            //       if ((this.entity == entity) && (this.mapping == mapping)) return;
            if ((entity == null) || (mapping == null))
            {
                Debug.println("Wrong parametres entity :"+entity+", mapping :"+mapping);
                return;
            }
            this.entity = entity;
            this.mapping = mapping;
            this.dataDomain = domain;
            SdaiModel model = domain.getByIndex(1);
            mappingDomain = MappingOperations.getMappingDomain(SdaiSession.getSession().getMappings(model.getUnderlyingSchema()));
            //			alternatives.setModel(new AggregateListModel(entity.findEntityMappings(dataDomain, mappingDomain)));
            AEntity_mapping mappings = new AEntity_mapping();
            try
            {
                entity.findEntityMappings(dataDomain, mappingDomain, mappings, mode);
            } catch (SdaiException ex)
            {
                System.out.println("Alternatives");
                processMessage(ex);
            }
            lockAlternatives = true;
            alternatives.setModel(new AggregateListModel(mappings));
            alternatives.setSelectedItem(mapping);
            lockAlternatives = false;
            t_instance.setLink(entity);
            t_instance.setText(entity.toString());
            
            pattr.removeAll();
            textes.removeAllElements();
            attributs.removeAllElements();
            types.removeAllElements();
            vunsets.removeAllElements();
            attributes.removeAllElements();
            
            buildAttributes(mapping.getSource(null), mapping.getSource(null), new Vector());
            if (isLockSplits)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        JSplitPane sp = (JSplitPane)splits.get(0);
                        sp.setDividerLocation(sp.getWidth()-getMaxSizeSplit());
                    }
                });
            }
            sb.getVerticalScrollBar().setValue(0);
            refreshData();
            //			showSettedAttributeAlternatives();
            setEditEnable(false);
            //			paintAll(getGraphics());
        } catch (SdaiException ex)
        { processMessage(ex); }
    }
    
    private void showSettedAttributeAlternatives() throws SdaiException
    {
        for (int i = 0; i < attributes.size(); i++)
        {
            AttributeMappingPanel amp = (AttributeMappingPanel)attributes.get(i);
            //			amp.showSettedAttributeAlternatives();
        }
    }
    
    private void buildAttributes(EEntity_definition definition, EEntity_definition basic, Vector molis) throws SdaiException
    {
        //printing attributes of supertypes
        AEntity_definition definitions = definition.getSupertypes(null);
        SdaiIterator it_definitions = definitions.createIterator();
        while (it_definitions.next())
        {
            EEntity_definition ed = definitions.getCurrentMember(it_definitions);
            if (molis.indexOf(ed) == -1)
            {
                buildAttributes(ed, basic, molis);
                molis.add(ed);
            }
        }
        //printing attributes of this definition
        BorderLayout bl = new BorderLayout();
        bl.setHgap(5);
        JPanel panel = new JPanel(bl);
        panel.setBorder(new TitledBorder(new MatteBorder(1, 0, 0, 0, new Color(153, 153, 153)), definition.getName(null)));
        GridLayout gl = new GridLayout(0, 1);
        gl.setVgap(2);
        JPanel plabels = new JPanel(gl);
        JPanel ptexts = new JPanel(gl);
        JPanel ptypes = new JPanel(gl);
        JPanel punsets = new JPanel(gl);
        
        AExplicit_attribute attributes = definition.getExplicit_attributes(null);
        SdaiIterator it_attributes = attributes.createIterator();
        while (it_attributes.next())
        {
            EExplicit_attribute ea = attributes.getCurrentMember(it_attributes);
            //			if (!SimpleOperations.testRedeclaring(ea, basic, null)) {
            buildAttribute(plabels, ptexts, ptypes, punsets, ea, definition);
            //			}
        }
        AAttribute iattributes = definition.getAttributes(null, null);
        SdaiIterator it_iattributes = iattributes.createIterator();
        while (it_iattributes.next())
        {
            EAttribute attribute = iattributes.getCurrentMember(it_iattributes);
//             if ((attribute instanceof EExplicit_attribute) && (SimpleOperations.getRedeclaring(attribute) != null))
//             {
//                 //				if (!SimpleOperations.testRedeclaring(attribute, basic, null)) {
//                 buildAttribute(plabels, ptexts, ptypes, punsets, attribute, definition);
//                 //				}
//             } else
			if (attribute instanceof EInverse_attribute)
            {
                //				if (!SimpleOperations.testRedeclaring(attribute, basic, null)) {
                buildAttribute(plabels, ptexts, ptypes, punsets, attribute, definition);
                //				}
            } else if (attribute instanceof EDerived_attribute)
            {
                //				if (!SimpleOperations.testRedeclaring(attribute, basic, null)) {
                buildAttribute(plabels, ptexts, ptypes, punsets, attribute, definition);
                //				}
            }
        }
        
        panel.add(plabels, BorderLayout.WEST);
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splits.add(sp);
        if (isLockSplits)
        {
            //		   sp.setDividerLocation(splitLocation);
            sp.addPropertyChangeListener(new PropertyChangeListener()
            {
                public void propertyChange(PropertyChangeEvent e)
                {
                    if (!lockChangeListener && e.getPropertyName().equals("dividerLocation"))
                    {
                        lockChangeListener = true;
                        for (int i = 0; i < splits.size(); i++)
                        {
                            JSplitPane split = (JSplitPane)splits.get(i);
                            if (split != e.getSource())
                            {
                                JSplitPane thisPane = (JSplitPane)e.getSource();
                                splitLocation = split.getWidth()-(thisPane.getWidth()-thisPane.getDividerLocation());
                                split.setDividerLocation(((splitLocation > 0)&&(splitLocation < split.getWidth()))?splitLocation:0);
                            }
                        }
                        lockChangeListener = false;
                    }
                }
            });
        }
        sp.setDividerSize(5);
        sp.setBorder(null);
        sp.setRightComponent(ptexts);
        sp.setLeftComponent(ptypes);
        panel.add(sp, BorderLayout.CENTER);
        panel.add(punsets, BorderLayout.EAST);
        pattr.add(panel);
    }
    
    private void buildAttribute(JPanel plabels, JPanel ptexts, JPanel ptypes, JPanel punsets, EAttribute attribute, EEntity_definition definition) throws SdaiException
    {
        //labels
        //		JLabel label = new JLabel(ident+SimpleOperations.getAttributeName(attribute));
        //		label.setHorizontalAlignment(JLabel.RIGHT);
        //		label.setToolTipText(SimpleOperations.getAttributeDomainString(attribute));
        //		plabels.add(label);
        //types
        GoLabel ltype = new GoLabel(ident+SimpleOperations.getAttributePrefix(attribute)+" "
        +SimpleOperations.getAttributeName(attribute)+": "+SimpleOperations.getAttributeDomainString(attribute));
        if (isDeveloperMode)
        {
            ltype.setUnderlying(true);
            ltype.setLink(SimpleOperations.getAttributeDomain(attribute));
            ltype.setPreferredSize(new Dimension(0, (int)ltype.getPreferredSize().getWidth()));
        }
        //		ltype.addMouseListener(mouseListener);
        //		ltype.addKeyListener(keyListener);
        ltype.setMinimumSize(new Dimension(0, 0));
        types.add(ltype);
        ptypes.add(ltype);
        //texts
        //		GoTextField textField = new GoTextField();
        //		textField.setEditable(false);
        //		textes.add(textField);
        AttributeMappingPanel panelA = new AttributeMappingPanel(attribute, mapping, mode);
        panelA.setEditable(false);
        panelA.addGoListener(goListener);
        attributes.add(panelA);
        ptexts.add(panelA);
        //unsets
        JTextField unsetField = new JTextField();
        vunsets.add(unsetField);
        punsets.add(unsetField);
        unsetField.setBorder(null);
        unsetField.setEditable(false);
        //for attribute alternatives
        //		Vector mappings = MappingOperations.getAttributeMappingAlternatives(attribute, mapping);
        //		if (mappings.size() > 1) {
        //			JPanel p = new JPanel(new GridLayout(1, 2));
        //			JComboBox cb = new JComboBox();
        //			cb.setModel(new DefaultComboBoxModel(mappings));
        //			cb.setRenderer(new SdaiCellRenderer());
        //			cb.setMinimumSize(new Dimension(0, 0));
        //			cb.setPreferredSize(new Dimension((int)textField.getPreferredSize().getWidth(), (int)textField.getPreferredSize().getHeight()));
        //			cb.addActionListener(attrActionListener);
        //
        ///*			JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        //			sp.setDividerSize(5);
        //			sp.setDividerLocation(StrictMath.abs(sp.getWidth()*0.5));
        //			sp.setBorder(null);
        //			sp.setRightComponent(cb);
        //			sp.setLeftComponent(textField);*/
        //
        //			p.add(textField);
        //			p.add(cb);
        //			ptexts.add(p);
        //			attributs.add(cb);
        //		} else if (mappings.size() < 1) {
        //			textField.setEnabled(false);
        //			ptexts.add(textField);
        //			attributs.add(attribute);
        //		} else {
        //			textField.setToolTipText(SimpleOperations.getDomainString(MappingOperations.getTarget((EAttribute_mapping)(EAttribute_mapping)mappings.elementAt(0))));
        //			ptexts.add(textField);
        //			attributs.add((EAttribute_mapping)mappings.elementAt(0));
        //		}
        //		textField.addMouseListener(mouseListener);
        //		textField.addFocusListener(focusListener);
        //		textField.addKeyListener(keyListener);
    }
    
    //	ActionListener attrActionListener = new ActionListener() {
    //		public void actionPerformed(ActionEvent e) {
    //			try {
    //				JComboBox c = (JComboBox)e.getSource();
    //				JTextField tf = (JTextField)textes.elementAt(attributs.indexOf(c));
    //				JTextField uf = (JTextField)vunsets.elementAt(attributs.indexOf(c));
    //				uf.setText((entity.testMappedAttribute((EAttribute_mapping)c.getSelectedItem(), dataDomain, mappingDomain))?"":"$");
    //				tf.setToolTipText(SimpleOperations.getDomainString(MappingOperations.getTarget((EAttribute_mapping)c.getSelectedItem())));
    //				tf.setText(MappingOperations.getMappedAttributeString(entity, (EAttribute_mapping)c.getSelectedItem(), dataDomain, mappingDomain));
    //			} catch (SdaiException h) { processMessage(h); }
    //		}
    //	};

	public void refreshData() {
		MappingOperationsThreadManager.invokeMethod(this, "refreshDataSingleThreaded", null, null);
	}

    public void refreshDataSingleThreaded()
    {
        try
        {
            t_instance.setText(entity.toString());
            if (mapping != null)
            {
                refreshAttributes();
                AAttribute_mapping result2 = new AAttribute_mapping();
                
                //changed with Alfas method
                //				AEntity result1 = MappingOperations.findMappingInversesForInstance(entity, mapping, dataDomain, mappingDomain, mode, result2);
                
                try
                {
                    AEntity result1 = entity.findMappedUsers(mapping, null, dataDomain, mappingDomain, result2, mode);
                    inversesModel.setInverses(result1, result2);
                    inverses.setPreferredScrollableViewportSize(new Dimension(0, inverses.getRowHeight()*((inverses.getRowCount()<20)?inverses.getRowCount()+1:20)));
                } catch (SdaiException ex)
                {
                    inversesModel.setInverses(new AEntity(), new AAttribute_mapping());
                    processMessage(ex);
                }
                //				MappingOperations.removeSubtypeInverses(result1, result2);
                
                //				AEntity result1 = MappingOperations.getInverses(entity, mapping, dataDomain, mappingDomain, result2);
                
            }
            //			paintAll(getGraphics());
        } catch (SdaiException ex)
        { processMessage(ex); }
    }
    
    private void refreshAttributes() throws SdaiException
    {
        for (int i = 0; i < attributes.size(); i++)
        {
            AttributeMappingPanel panelA = (AttributeMappingPanel)attributes.get(i);
            panelA.setInstance(entity, dataDomain, mappingDomain);
            //			Object o = attributs.elementAt(i);
            //			EAttribute_mapping attr_m = null;
            //			JTextField t = (JTextField)textes.elementAt(i);
            //			JTextField u = (JTextField)vunsets.elementAt(i);
            //			if (o instanceof EAttribute) {
            //				t.setText("NO MAPPING FOR THIS ATTRIBUTE");
            //				u.setText("$");
            //			} else {
            //				if (o instanceof EAttribute_mapping) {
            //					attr_m = (EAttribute_mapping)o;
            //				} else if (o instanceof JComboBox) {
            //					JComboBox j = (JComboBox)o;
            //					attr_m = (EAttribute_mapping)j.getSelectedItem();
            //				}
            //				if (entity.testMappedAttribute(attr_m, dataDomain, mappingDomain)) {
            //					t.setText(MappingOperations.getMappedAttributeString(entity, attr_m, dataDomain, mappingDomain));
            //					u.setText("");
            //				} else {
            //					t.setText("");
            //					u.setText("$");
            //				}
            //			}
            //			t.setPreferredSize(new Dimension(0, (int)t.getPreferredSize().getHeight()));
        }
    }
    
    public EEntity getInstance() throws SdaiException
    {
        return entity;
    }
    
    public EEntity_mapping getMapping() throws SdaiException
    {
        return mapping;
    }
    
    public String getTreeLeave() throws SdaiException
    {
        if ((entity == null) || (mapping == null))
        {
            return "";
        } else
        {
            return entity.findEntityInstanceSdaiModel().getRepository().getName()+"/"+entity.findEntityInstanceSdaiModel().getName()
            +"/"+MappingOperations.findSchema_mappingFor(mapping).getId(null)+"/"+mapping.getSource(null).getName(null)+"/"+entity.getPersistentLabel();
        }
    }
    
    public int getMode()  throws SdaiException
    {
        return mNOT_USE;
    }
    
    public void getSelected(Vector result) throws SdaiException
    {
        if (lastSelection == t_instance)
        {
            result.add(entity.getInstanceType());
        } else if (lastSelection == labelAlternatives)
        {
            EEntity_mapping em = (EEntity_mapping)alternatives.getSelectedItem();
            result.add(em);
        } else if (lastSelection instanceof JComboBox)
        {
            JComboBox j = (JComboBox)lastSelection;
            processed_mapping = (EEntity_mapping)j.getSelectedItem();
            result.add(entity);
        } else if (lastSelection instanceof JLabel)
        {
            for (int i = 0; i < types.size(); i++)
            {
                if (lastSelection == types.elementAt(i))
                {
                    Object o = attributs.elementAt(i);
                    if (o instanceof JComboBox)
                    {
                        JComboBox j = (JComboBox)o;
                        EAttribute_mapping a = (EAttribute_mapping)j.getSelectedItem();
                        result.add(a.getSource(null));
                    } else
                    {
                        EAttribute_mapping a = (EAttribute_mapping)o;
                        result.add(a.getSource(null));
                    }
                }
            }
        } else if (lastSelection instanceof JTextField)
        {
            processed_mapping = null;
            for (int i = 0; i < textes.size(); i++)
            {
                if (lastSelection == textes.elementAt(i))
                {
                    Object o = attributs.elementAt(i);
                    EAttribute_mapping attr = null;
                    if (o instanceof EAttribute)
                    {
                        result.add(null);
                        return;
                    } else if (o instanceof EAttribute_mapping)
                    {
                        attr = (EAttribute_mapping)o;
                    } else
                    {
                        JComboBox j = (JComboBox)o;
                        attr = (EAttribute_mapping)j.getSelectedItem();
                    }
                    Object maps = MappingOperations.getMappedAttributeObject(entity, attr, dataDomain, mappingDomain, mode);
                    
                    EEntity target = MappingOperations.getTarget(attr);
                    if (target instanceof EAggregation_type)
                    {
                        target = SimpleOperations.getBase_typeDomain(((EAggregation_type)target).getElement_type(null));
                    }
                    if ((maps instanceof EEntity) && !(target instanceof EEntity_definition))
                    {
                        target = ((EEntity)maps).getInstanceType();
                    }
                    
                    EEntity source =  SimpleOperations.getAttributeDomain(attr.getSource(null));
                    if (source instanceof EAggregation_type)
                    {
                        source = SimpleOperations.getBase_typeDomain(((EAggregation_type)source).getElement_type(null));
                    }
                    
                    if ((target instanceof EEntity_definition) && (source instanceof EEntity_definition))
                    {
                        AEntity_mapping mapps = MappingOperations.getMappingForARM_And_AIM((EEntity_definition)target, (EEntity_definition)source, mappingDomain, new AEntity_mapping());
                        if (mapps.getMemberCount() > 0)
                        {
                            processed_mapping = mapps.getByIndex(1);
                        } else
                        {
                            ((EEntity)maps).findEntityMappings(dataDomain, mappingDomain, mapps, mode);
                            if (mapps.getMemberCount() > 0)
                            {
                                processed_mapping = mapps.getByIndex(1);
                            }
                        }
                    } else if ((target instanceof EEntity_definition) && (source instanceof ESelect_type))
                    {
                        if (attr.testData_type(null))
                        {
                            ANamed_type ant = attr.getData_type(null);
                            if (ant.getMemberCount() > 0)
                            {
                                ENamed_type nt = ant.getByIndex(1);
                                if (nt instanceof EEntity_definition)
                                {
                                    AEntity_mapping mapps = MappingOperations.getMappingForARM_And_AIM((EEntity_definition)target, (EEntity_definition)nt, mappingDomain, new AEntity_mapping());
                                    if (mapps.getMemberCount() > 0)
                                    {
                                        processed_mapping = mapps.getByIndex(1);
                                    } else
                                    {
                                        ((EEntity)maps).findEntityMappings(dataDomain, mappingDomain, mapps, mode);
                                        if (mapps.getMemberCount() > 0)
                                        {
                                            processed_mapping = mapps.getByIndex(1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    if (maps instanceof Aggregate)
                    {
                        result.add(maps);
                        //						processed_mapping = null;
                    } else if (maps instanceof ArrayList)
                    {
                        result.add(SimpleOperations.arrayToAEntity(((ArrayList)maps).toArray()));
                    } else if (maps instanceof EEntity)
                    {
                        //						processed_mapping = MappingOperations.findMappingForARM(((EEntity)maps).findEntityMappings(dataDomain, mappingDomain), (EEntity_definition)SimpleOperations.getAttributeDomain(attr.getSource(null)));
                        //						processed_mapping = ((EEntity)maps).findEntityMappings(dataDomain, mappingDomain).getByIndex(1);
                        result.add(maps);
                    }
                    return;
                }
            }
        } else
        {
            result.add(inverses.getModel().getValueAt(inverses.getSelectedRow(), -2));
            processed_mapping = (EEntity_mapping)inverses.getModel().getValueAt(inverses.getSelectedRow(), -1);
        }
    }
    
    public void setSelected(Vector agg) throws SdaiException
    {
    }
    
    public boolean isSelected() throws SdaiException
    {
        boolean result = false;
        return result;
    }
    
    public void sdaiEdit() throws SdaiException
    {
        super.sdaiEdit();
        for (int i = 0; i < textes.size(); i++)
        {
            ((JTextField)textes.elementAt(i)).setEditable(true);
        }
    };
    
    public void sdaiAccept() throws SdaiException
    {
        super.sdaiAccept();
        SimpleOperations.enshureReadWriteModel(entity.findEntityInstanceSdaiModel());
        for (int i = 0; i < textes.size(); i++)
        {
            EAttribute_mapping attribute = null;
            Object select = attributs.elementAt(i);
            if (select instanceof JComboBox)
            {
                JComboBox jc = (JComboBox)select;
                attribute = (EAttribute_mapping)jc.getSelectedItem();
            } else if (select instanceof EAttribute_mapping)
            {
                attribute = (EAttribute_mapping)select;
            }
        }
        for (int i = 0; i < textes.size(); i++)
        {
            ((JTextField)textes.elementAt(i)).setEditable(false);
        }
    };
    
    public void sdaiCancel() throws SdaiException
    {
        super.sdaiCancel();
        pattr.removeAll();
        refreshData();
        //		paintAll(getGraphics());
        for (int i = 0; i < textes.size(); i++)
        {
            ((JTextField)textes.elementAt(i)).setEditable(false);
        }
    };
    
    public void sdaiNew() throws SdaiException
    {
        if (lastSelection instanceof JTextField)
        {
            JTextField tf = (JTextField)lastSelection;
            if (textes.indexOf(lastSelection) != -1)
                tf.setText(getClipboardElement().toString());
        }
        //		paintAll(getGraphics());
    };
    
    public void sdaiDestroy() throws SdaiException
    {
    }
    
    public void setEditEnable(boolean editable)
    {
        for (int i = 0; i < attributes.size(); i++)
        {
            AttributeMappingPanel value = (AttributeMappingPanel)attributes.get(i);
            value.setEditable(editable);
        }
    }
    
    public EEntity_mapping getProcessed_mapping()
    {
        return processed_mapping;
    }
    
    public String copyContentsAsText()
    {
        String result = "";
        try
        {
            GregorianCalendar cal = new GregorianCalendar();
            Date date = cal.getTime();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-"+(cal.get(Calendar.ZONE_OFFSET/3600000)));
            result += "=== SdaiEdit {"+SdaiSession.getSession().getSdaiImplementation().getLevel()
            +"} - InstanceMapping - "+dateFormatter.format(date)+newLine;
            result += "Location: "+getTreeLeave()+newLine;
            result += "Instance:\t"+t_instance.getText()+newLine;
            result += "Alternatives="+((alternatives.getSelectedItem()!=null)?mapping.getSource(null).getName(null):"")+"(";
            ListModel lm = alternatives.getModel();
            boolean first = true;
            for (int i = 0; i < lm.getSize(); i++)
            {
                EEntity_mapping em = (EEntity_mapping)lm.getElementAt(i);
                if (!em.equals(alternatives.getSelectedItem()))
                {
                    if (first)
                    {
                        first = false;
                    } else
                    {
                        result += ", ";
                    }
                    result += em.getSource(null).getName(null);
                }
            }
            result += ")"+newLine;
            EEntity_definition ed = null;
            EEntity_definition ed2 = null;
            ArrayList supertypes = SimpleOperations.getSupertypes(mapping.getSource(null), new ArrayList());
            int is = -1;
            for (int i = 0; i < attributs.size(); i++)
            {
                Object o = attributs.elementAt(i);
                EAttribute_mapping attr_m = null;
                JTextField text = (JTextField)textes.elementAt(i);
                JLabel label = (JLabel)types.elementAt(i);
                EAttribute attribute = null;
                if (o instanceof EAttribute)
                {
                    attribute = (EAttribute)o;
                } else if (o instanceof EAttribute_mapping)
                {
                    attribute = ((EAttribute_mapping)o).getSource(null);
                } else if (o instanceof JComboBox)
                {
                    JComboBox j = (JComboBox)o;
                    attribute = ((EAttribute_mapping)j.getSelectedItem()).getSource(null);
                }
                if (ed != attribute.getParent_entity(null))
                {
                    is++;
                    ed2 = (EEntity_definition)supertypes.get(is);
                    ed = attribute.getParent_entity(null);
                    boolean inline = true;
                    while (ed != ed2)
                    {
                        //						if (inline) {
                        //							inline = false;
                        //						} else {
                        //						}
                        result += "-"+ed2.getName(null)+"-"+newLine;
                        is++;
                        ed2 = (EEntity_definition)supertypes.get(is);
                    }
                    result += "-"+ed.getName(null)+"-"+newLine;
                }
                String value = "";
                if (((JTextField)vunsets.get(i)).getText().equals("$"))
                {
                    value = "$";
                } else
                {
                    value = text.getText();
                }
                
                result += attribute.getName(null)+"\t"+label.getText()+"=\t"+value+newLine;
            }
            result += "-Users-"+newLine;
            //			for (int j = 0; j < inverses.getColumnCount(); j++) {
            //				result += inverses.getColumnName(j)+"\t";
            //			}
            for (int i = 0; i < inverses.getRowCount(); i++)
            {
                for (int j = 0; j < inverses.getColumnCount(); j++)
                {
                    result += inverses.getValueAt(i, j)+"\t";
                }
                result += newLine;
            }
        } catch (SdaiException ex)
        { processMessage(ex); }
        
        return result;
    }
    
    public void setProperties(Properties props)
    {
        isLockSplits = Boolean.valueOf(props.getProperty("entityMappingPage.isLockSplits", String.valueOf(isLockSplits))).booleanValue();
        splitLocation = Integer.parseInt(props.getProperty("entityMappingPage.splitLocation", String.valueOf(splitLocation)));
        isDeveloperMode = Boolean.valueOf(props.getProperty("entityMappingPage.isDeveloperMode", String.valueOf(isDeveloperMode))).booleanValue();
        mode = Integer.parseInt(props.getProperty("mapping.mode", String.valueOf(mode)));
    }
    
    public void getProperties(Properties props)
    {
        props.setProperty("entityMappingPage.isLockSplits", String.valueOf(isLockSplits));
        props.setProperty("entityMappingPage.splitLocation", String.valueOf(splitLocation));
        props.setProperty("entityMappingPage.isDeveloperMode", String.valueOf(isDeveloperMode));
        props.setProperty("mapping.mode", String.valueOf(mode));
    }
    
    GoListener goListener = new GoListener()
    {
        public void goPerformed(GoEvent e)
        {
            try
            {
                Object value = e.getValue();
                if (value instanceof EEntity)
                {
                    Object s[] = new Object[2];
                    s[0] = value;
                    s[1] = dataDomain;
                    fireGo(new GoEvent(thisis, s, "EntityInstance"));
                } else if (value instanceof Object[])
                {
                    Object s[] = (Object[])value;
                    if ((s[0] instanceof ArrayList) || (s[0] instanceof AEntity))
                    {
                        Object o[] = new Object[3];
                        o[0] = s[0];
                        o[1] = s[1];
                        o[2] = dataDomain;
                        fireGo(new GoEvent(thisis, o, "AggregateMapping"));
                    } else if (s[0] instanceof EEntity)
                    {
                        Object o[] = new Object[3];
                        //Debug.println("Before "+s[1]);
                        EEntity en = (EEntity)s[0];
                        AEntity_mapping aem = new AEntity_mapping();
                        MappingOperations.getMostSpecificMappings(en, ((s[1] != null)?((EEntity_mapping)s[1]).getSource(null):null), dataDomain, mappingDomain, aem);
                        o[1] = (aem.getMemberCount() > 0)?aem.getByIndex(1):null;
                        //Debug.println("After "+o[1]);
                        o[0] = s[0];
                        //						o[1] = s[1];
                        o[2] = dataDomain;
                        fireGo(new GoEvent(thisis, o, "InstanceMapping"));
                    } else
                    {
                        Debug.println("Not supported type: "+value.getClass().getName()+". Value = "+value);
                    }
                } else
                {
                    Debug.println("Not supported type: "+value.getClass().getName()+". Value = "+value);
                }
            } catch (SdaiException ex)
            {
                processMessage(ex);
            }
        }
    };
    
    public ASdaiModel getDataDomain()
    {
        return dataDomain;
    }
    
    private int getMaxSizeSplit()
    {
        int max = Short.MAX_VALUE;
        for (int i = 0; i < splits.size(); i++)
        {
            JSplitPane split = (JSplitPane)splits.get(i);
            int c = (int)(split.getWidth() - split.getLeftComponent().getPreferredSize().getWidth());
            if (c < max)
            {
                max = c;
            }
        }
        return max;
    }
}
