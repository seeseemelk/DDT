/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.ast.definitions;

import melnorme.lang.tooling.ast.ILanguageElement;
import melnorme.lang.tooling.engine.scoping.CommonScopeLookup;
import melnorme.lang.tooling.engine.scoping.CommonScopeLookup.ScopeNameResolution;
import melnorme.lang.tooling.engine.scoping.IScopeElement;
import melnorme.lang.tooling.engine.scoping.ScopeTraverser;
import dtool.ast.declarations.DeclarationImport;

public class MembersScopeElement implements IScopeElement {
	
	protected Iterable<? extends ILanguageElement> membersIterable;
	
	public MembersScopeElement(Iterable<? extends ILanguageElement> membersIterable) {
		this.membersIterable = membersIterable;
	}
	
	@Override
	public ScopeTraverser getScopeTraverser() {
		return new ScopeTraverser(membersIterable, true, true) {
			@Override
			protected void doEvaluateNode(ScopeNameResolution scopeResolution, boolean isSecondaryScope, 
					ILanguageElement node) {
				
				if(node instanceof DeclarationImport) {
					return;  // Ignore imports when scope is a used as a members scope
				}
				
				super.doEvaluateNode(scopeResolution, isSecondaryScope, node);
			}
			
			@Override
			public void evaluateSuperScopes(CommonScopeLookup lookup) {
				resolveLookupInSuperScopes(lookup);
			}
		};
	}
	
	@SuppressWarnings("unused")
	public void resolveLookupInSuperScopes(CommonScopeLookup lookup) {
		// Default: do nothing
	}
	
}