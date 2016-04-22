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
package melnorme.util.swt.components.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.LabelledFieldWidget;
import melnorme.utilbox.fields.Field;

public class TextFieldWidget extends LabelledFieldWidget<String> {
	
	protected int defaultTextStyle = SWT.SINGLE | SWT.BORDER;
	protected Text text;
	
	public TextFieldWidget(String labelText) {
		this(labelText, SWT.SINGLE | SWT.BORDER);
	}
	
	public TextFieldWidget(String labelText, int textStyle) {
		super(labelText, Option_AllowNull.NO, "");
		this.defaultTextStyle = textStyle;
	}
	
	public TextFieldWidget(Field<String> field, String labelText) {
		super(field, labelText);
	}
	
	public void setDefaultTextStyle(int defaultTextStyle) {
		this.defaultTextStyle = defaultTextStyle;
	}
	
	public void setMultiLineStyle() {
		this.defaultTextStyle |= SWT.MULTI;
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		createContents_Label(topControl);
		createContents_Text(topControl);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layout2Controls_expandLast(label, text);
	}
	
	protected void createContents_Text(Composite topControl) {
		text = createText_2(topControl);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				setFieldValueFromControl(text.getText());
				
				handleFieldValueAndControlChanged();
			}
		});
	}
	
	protected Text createText_2(Composite topControl) {
		return new Text(topControl, defaultTextStyle);
	}
	
	protected void handleFieldValueAndControlChanged() {
	}
	
	@Override
	public Text getFieldControl() {
		return text;
	}
	
	@Override
	protected void doUpdateWidgetFromValue() {
		text.setText(getFieldValue());
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		SWTUtil.setEnabledIfOk(label, enabled);
		SWTUtil.setEnabledIfOk(text, enabled);
	}
	
}