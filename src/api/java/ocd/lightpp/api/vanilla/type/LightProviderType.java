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

import ocd.lightpp.api.vanilla.world.ILightProvider;

public class LightProviderType<LD, LI, WI>
{
	public final LightInterfaceType<LD, LI> lightInterfaceType;
	public final WorldLightInterfaceType<WI> worldLightInterfaceType;

	public LightProviderType(final LightInterfaceType<LD, LI> lightInterfaceType, final WorldLightInterfaceType<WI> worldLightInterfaceType)
	{
		this.lightInterfaceType = lightInterfaceType;
		this.worldLightInterfaceType = worldLightInterfaceType;
	}

	public static class TypedLightProvider<LD, LI, WI>
	{
		public final LightProviderType<LD, LI, WI> type;
		public final ILightProvider<LD, LI, WI> provider;

		public TypedLightProvider(
			final LightProviderType<LD, LI, WI> type,
			final ILightProvider<LD, LI, WI> provider
		)
		{
			this.type = type;
			this.provider = provider;
		}
	}
}
