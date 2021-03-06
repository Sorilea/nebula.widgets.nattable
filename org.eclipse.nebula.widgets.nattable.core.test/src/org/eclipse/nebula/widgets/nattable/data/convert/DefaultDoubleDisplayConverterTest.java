/*******************************************************************************
 * Copyright (c) 2012 Original authors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package org.eclipse.nebula.widgets.nattable.data.convert;


import java.text.NumberFormat;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultDoubleDisplayConverterTest {

	private DefaultDoubleDisplayConverter doubleConverter = new DefaultDoubleDisplayConverter();
	
	private static Locale defaultLocale;
	
	@BeforeClass
	public static void setup() {
		defaultLocale = Locale.getDefault();
		Locale.setDefault(new Locale("en"));
	}
	
	@AfterClass
	public static void tearDown() {
		Locale.setDefault(defaultLocale);
	}
	
	@Test
	public void testNonNullDataToDisplay() {
		Assert.assertEquals("123.0", doubleConverter.canonicalToDisplayValue(Double.valueOf("123")));
		Assert.assertEquals("23.5", doubleConverter.canonicalToDisplayValue(Double.valueOf("23.5")));
	}
	
	@Test
	public void testNullDataToDisplay() {
		Assert.assertNull(doubleConverter.canonicalToDisplayValue(null));
	}
	
	@Test
	public void testNonNullDisplayToData() {
		Assert.assertEquals(Double.valueOf("123"), doubleConverter.displayToCanonicalValue("123"));
		Assert.assertEquals(Double.valueOf("23.5"), doubleConverter.displayToCanonicalValue("23.5"));
	}
	
	@Test
	public void testNullDisplayToData() {
		Assert.assertNull(doubleConverter.displayToCanonicalValue(""));
	}

	@Test(expected=ConversionFailedException.class)
	public void testConversionException() {
		doubleConverter.displayToCanonicalValue("abc");
	}
	
	@Test
	public void testLocalizedDisplayConversion() {
		NumberFormat original = doubleConverter.getNumberFormat();
		NumberFormat localized = NumberFormat.getInstance(Locale.GERMAN);
		localized.setMinimumFractionDigits(1);
		localized.setMaximumFractionDigits(2);

		doubleConverter.setNumberFormat(localized);
		Assert.assertEquals("123,0", doubleConverter.canonicalToDisplayValue(Double.valueOf("123")));
		
		doubleConverter.setNumberFormat(original);
	}
	
	@Test
	public void testLocalizedCanonicalConversion() {
		NumberFormat original = doubleConverter.getNumberFormat();
		NumberFormat localized = NumberFormat.getInstance(Locale.GERMAN);
		localized.setMinimumFractionDigits(1);
		localized.setMaximumFractionDigits(2);

		doubleConverter.setNumberFormat(localized);
		Object result = doubleConverter.displayToCanonicalValue("123,5");
		Assert.assertTrue(result instanceof Double);
		Assert.assertEquals(123.5, result);
		
		doubleConverter.setNumberFormat(original);
	}
}
