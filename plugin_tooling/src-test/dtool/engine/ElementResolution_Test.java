/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.engine;

import org.junit.Test;

import dtool.ast.definitions.INamedElement;
import dtool.dub.BundlePath;

public class ElementResolution_Test extends CommonSemanticManagerTest {
	
	public final BundlePath LIB_FOO = bundlePath(SEMMODEL_TEST_BUNDLES, "lib_foo");
	public final BundlePath LIB_TPL = bundlePath(SEMMODEL_TEST_BUNDLES, "lib_tpl");
	
	@Test
	public void testResolveRef() throws Exception { testResolveRef$(); }
	public void testResolveRef$() throws Exception {
		
		___initSemanticManager();
		
		BundleResolution libFooSR = sm.getUpdatedResolution(LIB_FOO);
		
		INamedElement node = libFooSR.findContainedElement("test.native_ref");
	}
	
}