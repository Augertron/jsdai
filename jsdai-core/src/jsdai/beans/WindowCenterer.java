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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Class that centeres dialog windows and shows popup menus.
 */
public final class WindowCenterer
{
    /**
     * Shows dialog centered to parent component.
     * @param parent parent component.
     * @param child dialog window to center.
     */
    public static void showCentered(Component parent, Component child)
    {
        showCenteredImpl(parent, child);
    }
    
    /**
     * Shows dialog centered to parent component.
     * @param child dialog window to center.
     */
    public static void showCenteredOnParent(Component child)
    {
        showCenteredImpl(child.getParent(), child);
    }
    
    /**
     * Shows dialog centered to parent component.
     * @param parent parent component.
     * @param child dialog window to center.
     */
    private static void showCenteredImpl(Component parent, Component child)
    {
        Point loc = parent.getLocationOnScreen();
        
        child.setLocation(
            calculate(
                parent.getToolkit().getScreenSize(),
                new Rectangle(loc, parent.getSize()),
                child.getSize()
            )
        );

        ComponentListener repainter = new ParentRepainter(getTopParent(parent));
        
        child.addComponentListener(repainter);
        
        child.setVisible(true);
        
        child.removeComponentListener(repainter);
    }
  
    /**
     * Shows a window or a dialog cetered on screen
     *
     * @param comp component to show centered on screen
     */
    public static void showCenteredOnScreen(Component comp)
    {
        // center on screen
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winDim = comp.getBounds();
        
        comp.setLocation
        (
            (screenDim.width - winDim.width) / 2,
            (screenDim.height - winDim.height) / 2
        );
        
        comp.setVisible(true);
    }
    
    /**
     * Calculate dialog parent's center point.
     * @param screenSize screen size.
     * @param parent bounds rectangle.
     * @param childDim child's size.
     * @return parent's center point.
     */
    private static Point calculate(Dimension screenSize,
                                   Rectangle parentBnd,
                                   Dimension childDim)
    {
        Point ret = new Point();
        
        // Calculate center as if screen is unlimited.
        ret.x = parentBnd.x + (parentBnd.width - childDim.width) / 2;
        ret.y = parentBnd.y + (parentBnd.height - childDim.height) / 2;

        // Check if child goes out of screen.
        if (ret.x < 0)
            ret.x = 0;
        
        if (ret.y < 0)
            ret.y = 0;
        
        int diff = ret.x + childDim.width - screenSize.width;
        
        if (diff > 0)
            ret.x -= diff;
        
        diff = ret.y + childDim.height - screenSize.height;
        
        if (diff > 0)
            ret.y -= diff;
        
        return ret;
    }
    
    /**
     * Shows popup menu in visible screen area.
     * @param menu popup menu to show.
     * @param component parent component.
     * @param iposx popup x position.
     * @param iposx popup y position.
     */
    public static void showPopup(JPopupMenu menu,
                                 Component component,
                                 int iposx,
                                 int iposy)
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect   = menu.getBounds();
        Point location   = component.getLocationOnScreen();
        int iPopupX = iposx + rect.width + location.x;
        int iPopupY = iposy + rect.height + location.y + 30; //30 to get rid of the system toolbar
        int ix = iPopupX > screen.width  ? iposx - rect.width  : iposx;
        int iy = iPopupY > screen.height ? iposy - rect.height : iposy;

        menu.show(component, ix, iy);
    }
    
    /**
     * Gets the top parent of component.
     * @param parent parent component parent.
     * @return top parent component.
     */
    public static Component getTopParent(Component parent)
    {
        Component par = parent.getParent();
        Component topParent = parent;

        while (par != null && !(topParent instanceof SdaiPanel))
        {
            if (par != null)
            {
                topParent = par;
            }
            par = par.getParent();
        }
        
        return topParent;
    }
    
    /**
     * Class used to repaint dialog parent.
     * Workaround for repaint optimization bug
     * under NT4.0 when parent under dialog .
     */
    private static class ParentRepainter
        implements ComponentListener
    {
        /**
         * Parent component.
         */
        private Component mParent;
        
        /**
         * Parent zone covered by dialog.
         */
        private Rectangle mDarkZone;
        
        /**
         * Temporary rectangle for efficiency.
         */
        private Rectangle mTmpr;
        
        /**
         * Horisontal position of child/owner intersection.
         */
        private int dx;
        
        /**
         * Vertical position of child/owner intersection.
         */
        private int dy;
        
        /**
         * Constructs repainter object.
         * @param parent parent component to repaint.
         */
        public ParentRepainter(Component parent)
        {
            mParent = parent;
            mDarkZone = new Rectangle();
            mTmpr = mDarkZone;
        }
        
        /**
         * Calculate covered zone and dialog and parent intersection's location.
         * @param ev component event.
         */
        public void componentShown(ComponentEvent ev)
        {
            // Calculate the dark zone
            Component child = (Component)ev.getSource();
            Rectangle pBounds = mParent.getBounds();
            
            mDarkZone = pBounds.intersection(child.getBounds(mTmpr));
            dx = pBounds.x;
            dy = pBounds.y;
        }
        
        /**
         * Repaint parent if child component is hiddes
         * and covered zone is not empty.
         * @param ev component event.
         */
        public void componentHidden(ComponentEvent ev)
        {
            if (!mDarkZone.isEmpty())
            {
                repaintParent();
            }
        }
        
        /**
         * Repaint parent and calculate covered zone if
         * child resizes.
         * @param ev component event.
         */
        public void componentResized(ComponentEvent ev)
        {
            if (!mDarkZone.isEmpty())
            {
                repaintParent();
                
                // calculate new dark zone
                Component child = (Component)ev.getSource();
                mDarkZone = mDarkZone.intersection(child.getBounds(mTmpr));
            }
        }
        
        /**
         * Repaint parent and calculate covered zone if
         * child moves.
         * @param ev component event.
         */
        public void componentMoved(ComponentEvent ev)
        {
            if (!mDarkZone.isEmpty())
            {
                repaintParent();
                
                // calculate new dark zone
                Component child = (Component)ev.getSource();
                mDarkZone = mDarkZone.intersection(child.getBounds(mTmpr));
            }
        }
        
        /**
         * Repaint parent.
         */
        private void repaintParent()
        {
            mParent.repaint
            (
                mDarkZone.x - dx,
                mDarkZone.y - dy,
                mDarkZone.width,
                mDarkZone.height
            );
        }
    }
}


