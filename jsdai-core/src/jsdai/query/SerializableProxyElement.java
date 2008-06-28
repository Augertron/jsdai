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

package jsdai.query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * Created: Thu May 29 18:53:41 2003
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public class SerializableProxyElement implements InvocationHandler {

	private static final Constructor ELEMENT_PROXY_CONSTRUCTOR;

	/*
	 * This is the list of methods that need to be overwritten by the proxy for DOM2
	 * and DOM3. If the there would appear a new DOM this list might have to be extended.  
	 */
	// IMPORTANT! This is a sorted list
	private static final String[] PROXIED_METHOD_NAMES = {
		"getAttribute",
		"getAttributeNode",
		"getAttributeNodeNS",
		"getAttributeNS",
		"getAttributes",
		"hasAttribute",
		"hasAttributeNS",
		"hasAttributes",
		"removeAttribute",
		"removeAttributeNode",
		"removeAttributeNS",
		"setAttribute",
		"setAttributeNode",
		"setAttributeNodeNS",
		"setAttributeNS",
	};

	private static final Method[] PROXIED_METHODS = new Method[PROXIED_METHOD_NAMES.length];

	static {
		try {
			Class elementProxyClass =
				Proxy.getProxyClass(SerializableProxyElement.class.getClassLoader(),
						new Class[] { Element.class });
			ELEMENT_PROXY_CONSTRUCTOR = elementProxyClass.getConstructor(new Class[] { InvocationHandler.class });
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}

		Method[] methods = SerializableProxyElement.class.getMethods();
		for (int i = 0; i < PROXIED_METHOD_NAMES.length; i++) {
			String methodName = PROXIED_METHOD_NAMES[i];
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				if(methodName.equals(method.getName())) {
					PROXIED_METHODS[i] = method;
					break;
				}
			}
		}
	}

	public final Element element;
	private NamedNodeMapImpl elementAttributes;

	private SerializableProxyElement(Element element) {
		this.element = element;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		int i = Arrays.binarySearch(PROXIED_METHOD_NAMES, method.getName());
		return i >= 0 ? PROXIED_METHODS[i].invoke(this, args) : method.invoke(element, args);
	}
	
	public static Element newProxy(Element element) {
		try {
			return (Element) ELEMENT_PROXY_CONSTRUCTOR.newInstance(new Object[] { new SerializableProxyElement(element) });
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	// Implementation of org.w3c.dom.Element

	/**
	 * Describe <code>getAttribute</code> method here.
	 *
	 * @param name a <code>String</code> value
	 * @return a <code>String</code> value
	 */
	public String getAttribute(String name) {
		if(elementAttributes == null) {
			elementAttributes = new NamedNodeMapImpl();
		}
		Node attrNode;
		return (attrNode = elementAttributes.getNamedItem(name)) != null ? 
			attrNode.getNodeValue() : null;
	}

	/**
	 * Describe <code>getAttributes</code> method here.
	 *
	 * @return a <code>NamedNodeMap</code> value
	 */
	public NamedNodeMap getAttributes() {
		return new NamedNodeMapImpl();
	}

	/**
	 * Describe <code>getAttributeNode</code> method here.
	 *
	 * @param name a <code>String</code> value
	 * @return an <code>Attr</code> value
	 */
	public Attr getAttributeNode(String name) {
		if(elementAttributes == null) {
			elementAttributes = new NamedNodeMapImpl();
		}
		return (Attr)elementAttributes.getNamedItem(name);
	}

	/**
	 * Describe <code>getAttributeNodeNS</code> method here.
	 *
	 * @param namespaceURI a <code>String</code> value
	 * @param localName a <code>String</code> value
	 * @return an <code>Attr</code> value
	 */
	public Attr getAttributeNodeNS(String namespaceURI, String localName) {
		if(elementAttributes == null) {
			elementAttributes = new NamedNodeMapImpl();
		}
		return (Attr)elementAttributes.getNamedItemNS(namespaceURI, localName);
	}

	/**
	 * Describe <code>getAttributeNS</code> method here.
	 *
	 * @param namespaceURI a <code>String</code> value
	 * @param localName a <code>String</code> value
	 * @return a <code>String</code> value
	 */
	public String getAttributeNS(String namespaceURI, String localName) {
		if(elementAttributes == null) {
			elementAttributes = new NamedNodeMapImpl();
		}
		Node attrNode;
		return (attrNode = elementAttributes.getNamedItemNS(namespaceURI, localName)) != null ? 
			attrNode.getNodeValue() : null;
	}

	/**
	 * Describe <code>hasAttribute</code> method here.
	 *
	 * @param name a <code>String</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean hasAttribute(String name) {
		if(elementAttributes == null) {
			elementAttributes = new NamedNodeMapImpl();
		}
		return elementAttributes.getNamedItem(name) != null;
	}

	/**
	 * Describe <code>hasAttributeNS</code> method here.
	 *
	 * @param namespaceURI a <code>String</code> value
	 * @param localName a <code>String</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean hasAttributeNS(String namespaceURI, String localName) {
		if(elementAttributes == null) {
			elementAttributes = new NamedNodeMapImpl();
		}
		return elementAttributes.getNamedItemNS(namespaceURI, localName) != null;
	}

	/**
	 * Describe <code>hasAttributes</code> method here.
	 *
	 * @return a <code>boolean</code> value
	 */
	public boolean hasAttributes() {
		if(elementAttributes == null) {
			elementAttributes = new NamedNodeMapImpl();
		}
		return elementAttributes.getLength() > 0;
	}

	/**
	 * Describe <code>removeAttribute</code> method here.
	 *
	 * @param name a <code>String</code> value
	 * @exception DOMException if an error occurs
	 */
	public void removeAttribute(String name) throws DOMException {
		elementAttributes = null;
		element.removeAttribute(name);
	}

	/**
	 * Describe <code>removeAttributeNode</code> method here.
	 *
	 * @param oldAttr an <code>Attr</code> value
	 * @return an <code>Attr</code> value
	 * @exception DOMException if an error occurs
	 */
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		elementAttributes = null;
		return element.removeAttributeNode(oldAttr);
	}

	/**
	 * Describe <code>removeAttributeNS</code> method here.
	 *
	 * @param namespaceURI a <code>String</code> value
	 * @param localName a <code>String</code> value
	 * @exception DOMException if an error occurs
	 */
	public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
		elementAttributes = null;
		element.removeAttributeNS(namespaceURI, localName);
	}

	/**
	 * Describe <code>setAttribute</code> method here.
	 *
	 * @param namespaceURI a <code>String</code> value
	 * @param value a <code>String</code> value
	 * @exception DOMException if an error occurs
	 */
	public void setAttribute(String name, String value) throws DOMException {
		elementAttributes = null;
		element.setAttribute(name, value);
	}

	/**
	 * Describe <code>setAttributeNode</code> method here.
	 *
	 * @param newAttr an <code>Attr</code> value
	 * @return an <code>Attr</code> value
	 * @exception DOMException if an error occurs
	 */
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		elementAttributes = null;
		return element.setAttributeNode(newAttr);
	}

	/**
	 * Describe <code>setAttributeNodeNS</code> method here.
	 *
	 * @param newAttr an <code>Attr</code> value
	 * @return an <code>Attr</code> value
	 * @exception DOMException if an error occurs
	 */
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		elementAttributes = null;
		return element.setAttributeNodeNS(newAttr);
	}

	/**
	 * Describe <code>setAttributeNS</code> method here.
	 *
	 * @param namespaceURI a <code>String</code> value
	 * @param qualifiedName a <code>String</code> value
	 * @param value a <code>String</code> value
	 * @exception DOMException if an error occurs
	 */
	public void setAttributeNS(String namespaceURI, String qualifiedName, 
							   String value) throws DOMException {
		elementAttributes = null;
		element.setAttributeNS(namespaceURI, qualifiedName, value);
	}

	private class NamedNodeMapImpl implements NamedNodeMap {
		NamedNodeMap attrNodes;
		List parentAttrList;

		public NamedNodeMapImpl() {
			Map attrMap = new HashMap();
			attrNodes = element.getAttributes();
			int attrLength = attrNodes.getLength();
			for(int i = 0; i < attrLength; i++) {
				Node attr = attrNodes.item(i);
				if("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {
					String attrLocalName = attr.getLocalName();
					attrMap.put(attrLocalName, attr);
				}
			}
			parentAttrList = new ArrayList();
			Node parentNode = element.getParentNode();
			Element localElement = parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE ? 
				(Element)parentNode : null;
			while(localElement != null) {
				NamedNodeMap attrLocalNodes = localElement.getAttributes();
				attrLength = attrLocalNodes.getLength();
				for(int i = 0; i < attrLength; i++) {
					Node attr = attrLocalNodes.item(i);
					if("http://www.w3.org/2000/xmlns/".equals(attr.getNamespaceURI())) {
						String attrLocalName = attr.getLocalName();
						if(!attrMap.containsKey(attrLocalName)) {
							parentAttrList.add(attr);
						}
					}
				}
				parentNode = localElement.getParentNode();
				localElement = parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE ? 
					(Element)parentNode : null;
			}
		}

		// Implementation of org.w3c.dom.NamedNodeMap

		/**
		 * Describe <code>getLength</code> method here.
		 *
		 * @return an <code>int</code> value
		 */
		public int getLength() {
			return attrNodes.getLength() + parentAttrList.size();
		}

		/**
		 * Describe <code>item</code> method here.
		 *
		 * @param index an <code>int</code> value
		 * @return a <code>Node</code> value
		 */
		public Node item(int index) {
			int origLength = attrNodes.getLength();
			try {
				return index < origLength ? 
					attrNodes.item(index) : (Node)parentAttrList.get(index - origLength);
			} catch(ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}

		/**
		 * Describe <code>getNamedItem</code> method here.
		 *
		 * @param name a <code>String</code> value
		 * @return a <code>Node</code> value
		 */
		public Node getNamedItem(String name) {
			Node origNode = attrNodes.getNamedItem(name);
			if(origNode == null) {
				int parentIndex = parentAttrList.indexOf(new NamedItemLookup(name));
				return parentIndex >= 0 ? (Node)parentAttrList.get(parentIndex) : null;
			} else {
				return origNode;
			}
		}

		/**
		 * Describe <code>setNamedItem</code> method here.
		 *
		 * @param node a <code>Node</code> value
		 * @return a <code>Node</code> value
		 * @exception DOMException if an error occurs
		 */
		public Node setNamedItem(Node node) throws DOMException {
			Node oldNode = attrNodes.setNamedItem(node);
			int parentIndex;
			if((parentIndex = parentAttrList.indexOf(new NamedItemLookup(node.getNodeName()))) >= 0) {
				parentAttrList.remove(parentIndex);
			}
			return oldNode;
		}

		/**
		 * Describe <code>removeNamedItem</code> method here.
		 *
		 * @param name a <code>String</code> value
		 * @return a <code>Node</code> value
		 * @exception DOMException if an error occurs
		 */
		public Node removeNamedItem(String name) throws DOMException {
			return attrNodes.removeNamedItem(name);
		}

		/**
		 * Describe <code>getNamedItemNS</code> method here.
		 *
		 * @param namespaceURI a <code>String</code> value
		 * @param localName a <code>String</code> value
		 * @return a <code>Node</code> value
		 */
		public Node getNamedItemNS(String namespaceURI, String localName) {
			Node origNode = attrNodes.getNamedItemNS(namespaceURI, localName);
			if(origNode == null) {
				int parentIndex = parentAttrList.indexOf(new NamedItemNSLookup(namespaceURI, localName));
				return parentIndex >= 0 ? (Node)parentAttrList.get(parentIndex) : null;
			} else {
				return origNode;
			}
		}

		/**
		 * Describe <code>setNamedItemNS</code> method here.
		 *
		 * @param node a <code>Node</code> value
		 * @return a <code>Node</code> value
		 * @exception DOMException if an error occurs
		 */
		public Node setNamedItemNS(Node node) throws DOMException {
			Node oldNode = attrNodes.setNamedItem(node);
			int parentIndex;
			if((parentIndex = parentAttrList.indexOf(new NamedItemNSLookup(node.getNamespaceURI(), 
																		   node.getLocalName()))) >= 0) {
				parentAttrList.remove(parentIndex);
			}
			return oldNode;
		}

		/**
		 * Describe <code>removeNamedItemNS</code> method here.
		 *
		 * @param namespaceURI a <code>String</code> value
		 * @param localName a <code>String</code> value
		 * @return a <code>Node</code> value
		 * @exception DOMException if an error occurs
		 */
		public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
			return attrNodes.removeNamedItemNS(namespaceURI, localName);
		}
		
	}

	private static class NamedItemLookup {
		String name;

		private NamedItemLookup(String name) {
			this.name = name;
		}

		public boolean equals(Object other) {
			Node otherNode = (Node)other;
			return name.equals(otherNode.getNodeName());
		}
	}

	private static class NamedItemNSLookup {
		String namespaceURI;
		String localName;

		private NamedItemNSLookup(String namespaceURI, String localName) {
			this.namespaceURI = namespaceURI;
			this.localName = localName;
		}

		public boolean equals(Object other) {
			Node otherNode = (Node)other;
			return namespaceURI.equals(otherNode.getNamespaceURI()) 
				&& localName.equals(otherNode.getLocalName());
		}
	}

} // SerializableProxyElement
