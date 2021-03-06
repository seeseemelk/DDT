/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package org.dsource.ddt.debug.ui;

import org.dsource.ddt.ui.tabgroup.DeeMainLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

import melnorme.lang.ide.debug.ui.AbstractLangDebugTabGroup;

public class DeeDebugTabGroup extends AbstractLangDebugTabGroup {
	
	@Override
	protected ILaunchConfigurationTab createMainLaunchConfigTab() {
		return new DeeMainLaunchConfigurationTab();
	}
	
}