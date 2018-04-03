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
 */

package ocd.lightpp.api.vanilla.world;

import java.util.function.Supplier;

import ocd.lightpp.api.lighting.ILightCollectionDescriptor;
import ocd.lightpp.api.util.IEmpty;

public interface ILightQueueDataset<LD, Q extends IEmpty>
{
	Q get(LD desc);

	LD getDesc();

	Q getQueue();

	/**
	 * Discards the current queue iff it is {@link IEmpty#isEmpty() empty}
	 * Returns one of the queues that have been {@link #get(LD) requested} since they were last discarded
	 */
	boolean next();

	interface Provider<LD>
	{
		<Q extends IEmpty> ILightQueueDataset<LD, Q> get(Supplier<Q> queueProvider);
	}

	interface ILightCollectionQueueDataset<LD, LCD extends ILightCollectionDescriptor<LD>, Q extends IEmpty> extends ILightQueueDataset<LCD, Q>
	{
		Q get();

		interface Provider<LD, LCD extends ILightCollectionDescriptor<LD>>
		{
			<Q extends IEmpty> ILightCollectionQueueDataset<LD, LCD, Q> get(Supplier<Q> queueProvider);
		}
	}
}