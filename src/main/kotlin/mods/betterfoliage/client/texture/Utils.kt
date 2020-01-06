@file:JvmName("Utils")
package mods.betterfoliage.client.texture

import mods.betterfoliage.client.resource.Identifier
import mods.octarinecore.client.resource.Atlas
import mods.octarinecore.client.resource.get
import mods.octarinecore.client.resource.loadImage
import net.minecraft.resources.IResourceManager
import java.io.IOException

fun blendRGB(rgb1: Int, rgb2: Int, weight1: Int, weight2: Int): Int {
    val r = (((rgb1 shr 16) and 255) * weight1 + ((rgb2 shr 16) and 255) * weight2) / (weight1 + weight2)
    val g = (((rgb1 shr 8) and 255) * weight1 + ((rgb2 shr 8) and 255) * weight2) / (weight1 + weight2)
    val b = ((rgb1 and 255) * weight1 + (rgb2 and 255) * weight2) / (weight1 + weight2)
    val a = (rgb1 shr 24) and 255
    val result = ((a shl 24) or (r shl 16) or (g shl 8) or b).toInt()
    return result
}

fun IResourceManager.loadSprite(id: Identifier) = this.get(id)?.loadImage() ?: throw IOException("Cannot load resource $id")