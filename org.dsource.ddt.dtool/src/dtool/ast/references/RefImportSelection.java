package dtool.ast.references;

import java.util.Collection;

import dtool.ast.ASTCodePrinter;
import dtool.ast.ASTNodeTypes;
import dtool.ast.IASTVisitor;
import dtool.ast.SourceRange;
import dtool.ast.declarations.ImportSelective;
import dtool.ast.declarations.ImportSelective.IImportSelectiveSelection;
import dtool.ast.definitions.DefUnit;
import dtool.refmodel.DefUnitSearch;
import dtool.refmodel.PrefixDefUnitSearch;
import dtool.refmodel.pluginadapters.IModuleResolver;

// TODO: retire this element in favor of RefIdentifier?
public class RefImportSelection extends NamedReference implements IImportSelectiveSelection {
	
	public final String name;
	
	public ImportSelective impSel; // non-structural member
	
	public RefImportSelection(String name, SourceRange sourceRange) {
		initSourceRange(sourceRange);
		this.name = name;
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.REF_IMPORT_SELECTION;
	}
	
	@Override
	public void accept0(IASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);
	}
	
	@Override
	public String getReferenceName() {
		return name;
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.append(name);
	}
	
	@Override
	public String toStringAsElement() {
		return name;
	}
	
	@Override
	public Collection<DefUnit> findTargetDefUnits(IModuleResolver moduleResolver, boolean findOneOnly) {
		DefUnitSearch search = new DefUnitSearch(name, this, findOneOnly, moduleResolver);
		RefModule refMod = impSel.getModuleRef();
		Collection<DefUnit> targetmodules = refMod.findTargetDefUnits(moduleResolver, findOneOnly);
		CommonRefQualified.findDefUnitInMultipleDefUnitScopes(targetmodules, search);
		return search.getMatchDefUnits();
	}
	
	@Override
	public void doSearch(PrefixDefUnitSearch search) {
		RefModule refMod = impSel.getModuleRef();
		Collection<DefUnit> targetModules = refMod.findTargetDefUnits(search.getModResolver(), false);
		CommonRefQualified.findDefUnitInMultipleDefUnitScopes(targetModules, search);
	}
	
}