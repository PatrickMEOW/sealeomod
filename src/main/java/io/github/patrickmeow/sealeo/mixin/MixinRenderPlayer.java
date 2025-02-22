package io.github.patrickmeow.sealeo.mixin;

import io.github.patrickmeow.sealeo.features.impl.render.ModelScale;
import io.github.patrickmeow.sealeo.utils.Utils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {

    @Shadow protected abstract void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime);

    @Inject(method = "preRenderCallback(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V", at = @At("TAIL"))
    private void onPreRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime, CallbackInfo ci) {
        if(!ModelScale.INSTANCE.getEnabled()) { return; }
        ModelScale.INSTANCE.preRenderCallbackScaleHook(entitylivingbaseIn);
    }
}