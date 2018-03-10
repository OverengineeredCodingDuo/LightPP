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

package ocd.lightpp.lighting.vanilla.light;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import ocd.lightpp.api.lighting.ILightAccess;
import ocd.lightpp.api.lighting.ILightAccess.VirtuallySourced;
import ocd.lightpp.api.lighting.ILightPropagator;
import ocd.lightpp.api.lighting.ILightTypeManager.ILightIterator;
import ocd.lightpp.api.lighting.ILightTypeManager.ILightMap;
import ocd.lightpp.api.vanilla.light.IVanillaLightDescriptor;
import ocd.lightpp.api.vanilla.light.IVanillaLightInterface;
import ocd.lightpp.api.vanilla.light.IVanillaLightMap;
import ocd.lightpp.api.vanilla.light.IVanillaLightSource;
import ocd.lightpp.api.vanilla.world.IVanillaWorldInterface;

public class VanillaLightPropagator implements ILightPropagator<IVanillaLightDescriptor, IVanillaLightMap, IVanillaLightInterface, IVanillaWorldInterface, IVanillaLightSource>
{

	private final int worldMaxLight;

	private IVanillaWorldInterface world;
	private IBlockAccess blockAccess;
	private BlockPos pos;
	private int opacity;

	private final ILightMap<IVanillaLightDescriptor, IVanillaLightMap> vLightMap;

	public VanillaLightPropagator(final int worldMaxLight)
	{
		this.worldMaxLight = worldMaxLight;
	}

	private void calcSpread(
		final EnumSkyBlock lightType,
		final EnumFacing dir,
		final int sourceLight,
		final IVanillaLightMap lightMap
	)
	{
		lightMap.add(
			lightType,
			sourceLight - (
				lightType == EnumSkyBlock.SKY && dir == EnumFacing.DOWN && sourceLight == EnumSkyBlock.SKY.defaultLightValue
					? 0
					: Math.max(this.opacity, 1)
			)
		);
	}

	private void prepareCalc(final ILightAccess<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface> lightAccess)
	{
		this.world = lightAccess.getWorldInterface();
		this.blockAccess = this.world.getWorld();
		this.pos = this.world.getPos();
		this.opacity = this.world.getBlockState().getLightOpacity(this.blockAccess, this.pos);
	}

	@Override
	public void calcSourceLight(
		final VirtuallySourced<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface, ? extends IVanillaLightSource> lightAccess,
		final IVanillaLightMap lightMap
	)
	{
		this.calcSourceLight(null, lightAccess, lightMap);
	}

	private void calcSourceLight(
		final @Nullable EnumSkyBlock lightType,
		final VirtuallySourced<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface, ? extends IVanillaLightSource> lightAccess,
		final IVanillaLightMap lightMap
	)
	{
		this.prepareCalc(lightAccess);

		if (this.opacity >= this.worldMaxLight)
			return;

		final @Nullable IVanillaLightSource vSources = lightAccess.getVirtualSources();

		if (vSources != null)
		{
			this.vLightMap.clear();

			if (lightType == null)
			{
				vSources.getLight(this.vLightMap.getInterface());

				for (final ILightIterator<IVanillaLightDescriptor> it = this.vLightMap.iterator(); it.next(); )
					lightMap.add(it.getDescriptor().getSkyBlock(), it.get() - this.opacity);
			}
			else
			{
				vSources.getLight(lightType, this.vLightMap.getInterface());

				lightMap.add(lightType, this.vLightMap.getInterface().get(lightType) - this.opacity);
			}
		}

		if (lightType != EnumSkyBlock.SKY)
			lightMap.add(EnumSkyBlock.BLOCK, this.world.getBlockState().getLightValue(this.blockAccess, this.pos));
	}

	@Override
	public boolean calcLight(
		final VirtuallySourced.NeighborAware<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface, ? extends IVanillaLightSource> lightAccess,
		final IVanillaLightMap lightMap
	)
	{
		this.calcSourceLight(lightAccess, lightMap);

		return this.calcNeighborLight(EnumSkyBlock.SKY, lightAccess, lightMap) & this.calcNeighborLight(EnumSkyBlock.BLOCK, lightAccess, lightMap);
	}

	@Override
	public boolean calcLight(
		final IVanillaLightDescriptor desc,
		final VirtuallySourced.NeighborAware<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface, ? extends IVanillaLightSource> lightAccess,
		final IVanillaLightMap lightMap
	)
	{
		final EnumSkyBlock lightType = desc.getSkyBlock();

		this.calcSourceLight(lightType, lightAccess, lightMap);

		return this.calcNeighborLight(lightType, lightAccess, lightMap);
	}

	private boolean calcNeighborLight(
		final EnumSkyBlock lightType,
		final ILightAccess.NeighborAware<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface> lightAccess,
		final IVanillaLightMap lightMap
	)
	{
		boolean allNeighborsLoaded = true;

		for (final EnumFacing dir : EnumFacing.VALUES)
		{
			final ILightAccess<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface> neighborLightAccess = lightAccess.getNeighbor(dir);

			if (neighborLightAccess.isLoaded())
				this.calcSpread(lightType, dir.getOpposite(), neighborLightAccess.getLightData().getLight(lightType), lightMap);
			else
				allNeighborsLoaded = false;
		}

		return allNeighborsLoaded || lightMap.get(lightType) >= this.worldMaxLight - Math.max(this.opacity, lightType == EnumSkyBlock.SKY ? 0 : 1);
	}

	@Override
	public void calcSpread(
		final IVanillaLightDescriptor desc,
		final EnumFacing dir,
		final int light,
		final ILightAccess<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface> lightAccess,
		final ILightAccess<? extends IVanillaLightInterface, ? extends IVanillaWorldInterface> neighborLightAccess,
		final IVanillaLightMap lightMap
	)
	{
		this.prepareCalc(neighborLightAccess);

		this.calcSpread(desc.getSkyBlock(), dir, light, lightMap);
	}
}