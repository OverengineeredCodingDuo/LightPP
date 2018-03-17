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

import org.objectweb.asm.Type;

import ocd.lightpp.transformers.util.InitInjector;
import ocd.lightpp.transformers.util.InvokeInjector;
import ocd.lightpp.transformers.util.LineReplacer;
import ocd.lightpp.transformers.util.LocalIndexedVarCapture;
import ocd.lightpp.transformers.util.LocalTypedVarCapture;
import ocd.lightpp.transformers.util.MethodClassTransformer;
import ocd.lightpp.transformers.util.MethodMatcher;
import ocd.lightpp.util.NameRef;

public class TransformerChunkLightStorage extends MethodClassTransformer
{
	private static final String CLASS_NAME = "net.minecraft.world.chunk.Chunk";

	private static final String INIT_EMPTY_NAME = "initEmptyLightStorage";
	private static final String INIT_EMPTY_DESC = "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V";
	private static final String INIT_NAME = "initLightStorage";
	private static final String INIT_DESC = "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;)V";
	private static final String INIT_READ_NAME = "initLightStorageRead";
	private static final String INIT_READ_DESC = "(Lnet/minecraft/world/chunk/storage/ExtendedBlockStorage;I)V";

	private static final String INIT_PRIMER_DESC = "(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/ChunkPrimer;II)V";
	private static final String READ_NAME = "func_186033_a";
	private static final String READ_DESC = "(Lnet/minecraft/network/PacketBuffer;IZ)V";
	private static final String SET_BLOCK_STATE_NAME = "func_177436_a";
	private static final String SET_BLOCK_STATE_DESC = "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;";

	private static final String READ_PACKET_NAME = "readPacketData";
	private static final String READ_PACKET_DESC = "(Lnet/minecraft/network/PacketBuffer;)V";

	public TransformerChunkLightStorage()
	{
		super(CLASS_NAME);

		this.addTransformer("<init>", INIT_PRIMER_DESC, false,
			new InitInjector().addInjector(
				NameRef.EXTENDED_BLOCK_STORAGE_NAME,
				null,
				InitInjector.CAPTURE_THIS.andThen(
					new InvokeInjector(
						INIT_EMPTY_NAME,
						INIT_EMPTY_DESC,
						false
					)
				)
			)
		);

		this.addTransformer(READ_NAME, READ_DESC, true,
			new InitInjector().addInjector(
				NameRef.EXTENDED_BLOCK_STORAGE_NAME,
				null,
				InitInjector.CAPTURE_THIS.andThen(new LocalIndexedVarCapture(Type.INT_TYPE, 2)).andThen(
					new InvokeInjector(
						INIT_READ_NAME,
						INIT_READ_DESC,
						false
					)
				)
			).andThen(
				new LineReplacer().addProcessor(
					new MethodMatcher(
						NameRef.EXTENDED_BLOCK_STORAGE_NAME,
						NameRef.GET_BLOCK_LIGHT_NAME,
						NameRef.GET_BLOCK_LIGHT_DESC,
						true
					),
					new LocalTypedVarCapture(NameRef.EXTENDED_BLOCK_STORAGE_NAME).andThen(
						new LocalIndexedVarCapture(NameRef.PACKET_BUFFER_NAME, 1)).andThen(
						new InvokeInjector(
							NameRef.ISERIALIZABLE_NAME,
							READ_PACKET_NAME,
							READ_PACKET_DESC,
							false,
							true
						)
					)
				).addProcessor(
					new MethodMatcher(
						NameRef.EXTENDED_BLOCK_STORAGE_NAME,
						NameRef.GET_SKY_LIGHT_NAME,
						NameRef.GET_SKY_LIGHT_DESC,
						true
					)
				)
			)
		);

		this.addTransformer(SET_BLOCK_STATE_NAME, SET_BLOCK_STATE_DESC, true,
			new InitInjector().addInjector
				(
					NameRef.EXTENDED_BLOCK_STORAGE_NAME,
					null,
					InitInjector.CAPTURE_THIS.andThen(
						new InvokeInjector(
							INIT_NAME,
							INIT_DESC,
							false
						)
					)
				)
		);
	}
}
