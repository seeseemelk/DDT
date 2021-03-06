/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package mmrnmhrm.ui.editor;

import java.nio.file.Path;

import org.eclipse.ui.texteditor.ITextEditor;

import melnorme.lang.ide.core.DeeToolPreferences;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorToolOperation;
import melnorme.lang.tooling.ToolingMessages;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class DeeFmtOperation extends AbstractEditorToolOperation<String> {
	
	protected final ToolManager toolMgr = LangCore.getToolManager();
	
	public DeeFmtOperation(ITextEditor editor) {
		super("Format", editor);
	}
	
	@Override
	protected String doBackgroundToolResultComputation(IOperationMonitor om)
			throws CommonException, OperationCancellation, OperationSoftFailure {
		
		Path rustFmt = DeeToolPreferences.DFMT_PATH.getDerivedValue(project);
		
		ArrayList2<String> cmdLine = ArrayList2.create(rustFmt.toString());
		
		ProcessBuilder pb = new ProcessBuilder(cmdLine);
		// set directory, for fmt to look for the config file in folders parent chain
		pb.directory(getInputLocation().getParent().toFile());
		
		String input = doc.get();
		ExternalProcessResult result = toolMgr.runEngineTool(pb, input, om);
		int exitValue = result.exitValue;
		
		if(exitValue != 0) {
			String stdErr = result.getStdErrBytes().toUtf8String();
			String firstStderrLine = StringUtil.splitString(stdErr, '\n')[0].trim();
			
			String errorMessage = ToolingMessages.PROCESS_CompletedWithNonZeroValue("dfmt", exitValue) + "\n" +
					firstStderrLine;
			throw new OperationSoftFailure(errorMessage);
		}
		
		// formatted file is in stdout
		return result.getStdOutBytes().toUtf8String();
	}
	
	@Override
	protected void handleResultData(String resultData) throws CommonException {
		if(resultData != null) {
			setEditorTextPreservingCarret(resultData);
		}
	}
	
}