/*******************************************************************************
 * Copyright (c) 2011 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.ast.references;

import melnorme.lang.tooling.ast.CommonASTNode;
import melnorme.lang.tooling.ast_actual.ASTNodeTypes;
import melnorme.lang.tooling.context.ISemanticContext;
import melnorme.lang.tooling.engine.scoping.CommonScopeLookup;
import melnorme.lang.tooling.engine.scoping.IScopeElement;
import melnorme.lang.tooling.symbols.IConcreteNamedElement;
import melnorme.lang.tooling.symbols.IImportableUnit;
import melnorme.lang.tooling.symbols.INamedElement;
import dtool.ast.declarations.ImportSelective;
import dtool.ast.declarations.ImportSelective.IImportSelectiveSelection;

// TODO: retire this element in favor of RefIdentifier?
public class RefImportSelection extends CommonRefIdentifier implements IImportSelectiveSelection {
	
	public ImportSelective impSel; // non-structural member
	
	public RefImportSelection(String identifier) {
		super(identifier);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.REF_IMPORT_SELECTION;
	}
	
	public ImportSelective getImportSelectiveContainer() {
		return impSel;
	}
	
	@Override
	protected CommonASTNode doCloneTree() {
		return new RefImportSelection(identifier);
	}
	
	@Override
	public void doPerformNameLookup(CommonScopeLookup lookup) {
		RefModule refMod = getImportSelectiveContainer().getModuleRef();
		INamedElement targetModule = resolvedConcreteModule(refMod, lookup.context);
		if(targetModule instanceof IImportableUnit) {
			IImportableUnit module = (IImportableUnit) targetModule;
			IScopeElement importableScope = module.getImportableScope();
			lookup.evaluateScope(importableScope);
		}
	}
	
	protected IConcreteNamedElement resolvedConcreteModule(RefModule refMod, ISemanticContext context) {
		INamedElement target = refMod.resolveTargetElement(context);
		if(target == null) {
			return null;
		}
		return target.resolveConcreteElement(context);
	}
	
}