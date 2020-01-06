package mods.betterfoliage.client.render

import mods.betterfoliage.BetterFoliage
import mods.betterfoliage.BetterFoliageMod
import mods.betterfoliage.client.Client
import mods.betterfoliage.client.config.Config
import mods.betterfoliage.client.integration.ShadersModIntegration
import mods.octarinecore.client.render.CombinedContext
import mods.octarinecore.client.render.RenderDecorator
import net.minecraft.block.material.Material
import net.minecraft.tags.BlockTags
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import org.apache.logging.log4j.Level.DEBUG

class RenderAlgae : RenderDecorator(BetterFoliageMod.MOD_ID, BetterFoliageMod.bus) {

    val noise = simplexNoise()

    val algaeIcons = spriteSet { idx -> ResourceLocation(BetterFoliageMod.MOD_ID, "blocks/better_algae_$idx") }
    val algaeModels = modelSet(64) { idx -> RenderGrass.grassTopQuads(Config.algae.heightMin, Config.algae.heightMax)(idx) }

    override fun isEligible(ctx: CombinedContext) =
        Config.enabled && Config.algae.enabled &&
        ctx.state(up2).material == Material.WATER &&
        ctx.state(up1).material == Material.WATER &&
        BlockTags.DIRT_LIKE.contains(ctx.state.block) &&
        ctx.biome.category.let { it == Biome.Category.OCEAN || it == Biome.Category.BEACH || it == Biome.Category.RIVER } &&
        noise[ctx.pos] < Config.algae.population

    override fun render(ctx: CombinedContext) {
        ctx.render()
        if (!ctx.isCutout) return
        val rand = ctx.semiRandomArray(3)
        ShadersModIntegration.grass(ctx, Config.algae.shaderWind) {
            ctx.render(
                algaeModels[rand[2]],
                icon = { _, qi, _ -> algaeIcons[rand[qi and 1]] }
            )
        }
    }
}