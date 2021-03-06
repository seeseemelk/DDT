/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.context;

import java.util.Collections;

import melnorme.lang.tooling.ast.CommonLanguageElement;
import dtool.engine.AbstractBundleResolution;
import dtool.engine.CommonSemanticManagerTest.Tests_DToolServer;
import dtool.engine.StandardLibraryResolution;
import dtool.engine.StandardLibraryResolution.MissingStandardLibraryResolution;

/**
 * A mock semantic resolution. This implementation finds no modules.
 */
public class EmptySemanticResolution extends AbstractBundleResolution {
	
	protected final MissingStandardLibraryResolution stdLib;
	
	public EmptySemanticResolution() {
		super(new Tests_DToolServer().getSemanticManager(), Collections.EMPTY_LIST);
		stdLib = new MissingStandardLibraryResolution(manager);
	}
	
	@Override
	public StandardLibraryResolution getStdLibResolution() {
		return stdLib;
	}
	
	@Override
	public ISemanticContext getContainingSemanticContext(CommonLanguageElement languageElement) {
		ISemanticContext context = super.getContainingSemanticContext(languageElement);
		if(context == null) {
			return this;
		}
		return context;
	}
	
}