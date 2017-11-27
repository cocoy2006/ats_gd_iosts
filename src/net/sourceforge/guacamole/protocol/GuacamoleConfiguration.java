package net.sourceforge.guacamole.protocol;
	
	/* ***** BEGIN LICENSE BLOCK *****
	 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
	 *
	 * The contents of this file are subject to the Mozilla Public License Version
	 * 1.1 (the "License"); you may not use this file except in compliance with
	 * the License. You may obtain a copy of the License at
	 * http://www.mozilla.org/MPL/
	 *
12	 * Software distributed under the License is distributed on an "AS IS" basis,
13	 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
14	 * for the specific language governing rights and limitations under the
15	 * License.
16	 *
17	 * The Original Code is guacamole-common.
18	 *
19	 * The Initial Developer of the Original Code is
20	 * Michael Jumper.
21	 * Portions created by the Initial Developer are Copyright (C) 2010
22	 * the Initial Developer. All Rights Reserved.
23	 *
24	 * Contributor(s):
25	 *
26	 * Alternatively, the contents of this file may be used under the terms of
27	 * either the GNU General Public License Version 2 or later (the "GPL"), or
28	 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
29	 * in which case the provisions of the GPL or the LGPL are applicable instead
30	 * of those above. If you wish to allow use of your version of this file only
31	 * under the terms of either the GPL or the LGPL, and not to allow others to
32	 * use your version of this file under the terms of the MPL, indicate your
33	 * decision by deleting the provisions above and replace them with the notice
34	 * and other provisions required by the GPL or the LGPL. If you do not delete
35	 * the provisions above, a recipient may use your version of this file under
36	 * the terms of any one of the MPL, the GPL or the LGPL.
37	 *
38	 * ***** END LICENSE BLOCK ***** */
	
	import java.io.Serializable;
	import java.util.HashMap;
	
	/**
44	 * All information necessary to complete the initial protocol handshake of a
45	 * Guacamole session.
46	 *
47	 * @author Michael Jumper
48	 */
	public class GuacamoleConfiguration implements Serializable {
	
	    private static final long serialVersionUID = 1L;
	   
	    private String protocol;
	    private HashMap<String, String> parameters = new HashMap<String, String>();
	
	    /**
57	     * Returns the name of the protocol to be used.
58	     * @return The name of the protocol to be used.
59	     */
	    public String getProtocol() {
	        return protocol;
	    }
	
	    /**
65	     * Sets the name of the protocol to be used.
66	     * @param protocol The name of the protocol to be used.
67	     */
	    public void setProtocol(String protocol) {
	        this.protocol = protocol;
	    }
	
	    /**
	     * Returns the value set for the parameter with the given name, if any.
74	     * @param name The name of the parameter to return the value for.
75	     * @return The value of the parameter with the given name, or null if
76	     *         that parameter has not been set.
77	     */
	    public String getParameter(String name) {
	        return parameters.get(name);
	    }
	
	    /**
83	     * Sets the value for the parameter with the given name.
84	     *
85	     * @param name The name of the parameter to set the value for.
86	     * @param value The value to set for the parameter with the given name.
87	     */
	    public void setParameter(String name, String value) {
	        parameters.put(name, value);
	    }
	
	}
