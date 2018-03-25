/*
 * MIT License
 *
 * Copyright (c) 2017-2018 OverengineeredCodingDuo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ocd.lightpp.transformers;

import org.objectweb.asm.Opcodes;

import ocd.asmutil.MethodTransformer;
import ocd.asmutil.matchers.FieldMatcher;
import ocd.asmutil.transformers.LineInjector;
import ocd.lightpp.transformers.util.ObfuscationHelper;

public class TransformerSectionLightStorage extends MethodTransformer.Named
{
	private static final String CLASS_NAME = "net.minecraft.world.chunk.storage.ExtendedBlockStorage";

	private static final String NIBBLE_ARRAY_DESC = "Lnet/minecraft/world/chunk/NibbleArray;";

	private static final FieldMatcher.FieldDescriptor SKYLIGHT_ARRAY = ObfuscationHelper.createFieldMatcher(
		CLASS_NAME,
		"field_76685_h",
		NIBBLE_ARRAY_DESC
	);

	private static final FieldMatcher.FieldDescriptor BLOCKLIGHT_ARRAY = ObfuscationHelper.createFieldMatcher(
		CLASS_NAME,
		"field_76679_g",
		NIBBLE_ARRAY_DESC
	);

	public TransformerSectionLightStorage()
	{
		super(CLASS_NAME);

		this.addTransformer(
			"<init>",
			null,
			new LineInjector(
				new FieldMatcher(Opcodes.PUTFIELD, SKYLIGHT_ARRAY).or(new FieldMatcher(Opcodes.PUTFIELD, BLOCKLIGHT_ARRAY)),
				LineInjector.REMOVE
			)
		);
	}
}