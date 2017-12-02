/*
 * MIT License
 *
 * Copyright (c) 2017-2017 OverengineeredCodingDuo
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

package ocd.lightpp.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import ocd.lightpp.api.lighting.ILightHandler;
import ocd.lightpp.api.lighting.ILightManager;
import ocd.lightpp.api.lighting.ILightPropagator;
import ocd.lightpp.lighting.vanilla.VanillaLightHandler;
import ocd.lightpp.lighting.vanilla.VanillaLightPropagator;

@Mixin(World.class)
public abstract class VanillaLightManager implements ILightManager, ILightPropagator.Factory
{
	@Override
	public ILightHandler createLightHandler()
	{
		return new VanillaLightHandler((World) (Object) this);
	}

	@Override
	public ILightPropagator create(final EnumSkyBlock lightType)
	{
		return lightType == EnumSkyBlock.SKY ? new VanillaLightPropagator.Sky() : new VanillaLightPropagator.Block();
	}
}