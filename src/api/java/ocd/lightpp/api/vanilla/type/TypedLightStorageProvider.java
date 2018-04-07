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

package ocd.lightpp.api.vanilla.type;

import ocd.lightpp.api.util.IReleaseable;
import ocd.lightpp.api.vanilla.type.ContainerType.TypedContainer;
import ocd.lightpp.api.vanilla.world.ILightStorageProvider;

public class TypedLightStorageProvider<LD, LI, WI, LC extends IReleaseable, WC extends IReleaseable, T>
{
	public final CachedLightProviderType<LD, LI, WI, LC, WC> type;
	public final ILightStorageProvider<LD, ? extends LI, ? extends WI, LC, WC, T> provider;

	public TypedLightStorageProvider(
		final CachedLightProviderType<LD, LI, WI, LC, WC> type,
		final ILightStorageProvider<LD, ? extends LI, ? extends WI, LC, WC, T> provider
	)
	{
		this.type = type;
		this.provider = provider;
	}

	public TypedLightStorage<LD, LI, WI, LC, WC, T> createLightStorage()
	{
		return new TypedLightStorage<>(this.type, this.provider.createLightStorage());
	}

	public TypedContainer<LC> createLightContainer()
	{
		return new TypedContainer<>(this.type.lightContainerType, this.provider.createLightContainer());
	}

	public TypedContainer<WC> createWorldLightContainer()
	{
		return new TypedContainer<>(this.type.worldLightContainerType, this.provider.createWorldLightContainer());
	}
}
