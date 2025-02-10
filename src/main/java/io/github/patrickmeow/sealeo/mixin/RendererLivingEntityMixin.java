package io.github.patrickmeow.sealeo.mixin;

import io.github.patrickmeow.sealeo.features.impl.render.HitColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.FloatBuffer;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin<T extends EntityLivingBase> extends Render<T> {
    protected RendererLivingEntityMixin(RenderManager renderManager) {
        super(renderManager);
    }

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 0))
    public FloatBuffer setRed(FloatBuffer instance, float v, EntityLivingBase entity) {
        if(HitColor.INSTANCE.getEnabled()) {
            if (entity instanceof EntityZombie) {
                instance.put(HitColor.INSTANCE.getColorRed());
            } else {
                instance.put(v);
            }

        } else {
            instance.put(v);
        }
        return instance;
    }

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 1))
    public FloatBuffer setGreen(FloatBuffer instance, float v, EntityLivingBase entity) {
        if(HitColor.INSTANCE.getEnabled()) {
            if (entity instanceof EntityZombie) {
                instance.put(HitColor.INSTANCE.getColorGreen());
            } else {
                instance.put(v);
            }

        } else {
            instance.put(v);
        }
        return instance;
    }

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 2))
    public FloatBuffer setBlue(FloatBuffer instance, float v, EntityLivingBase entity) {
        if(HitColor.INSTANCE.getEnabled()) {
            if (entity instanceof EntityZombie) {
                instance.put(HitColor.INSTANCE.getColorBlue());
            } else {
                instance.put(v);
            }

        } else {
            instance.put(v);
        }
        return instance;
    }

    @Redirect(method = "setBrightness", at = @At(value = "INVOKE", target = "Ljava/nio/FloatBuffer;put(F)Ljava/nio/FloatBuffer;", ordinal = 3))
    public FloatBuffer setAlpha(FloatBuffer instance, float v, EntityLivingBase entity) {

        if(HitColor.INSTANCE.getEnabled()) {
            if (entity instanceof EntityZombie) {
                instance.put(HitColor.INSTANCE.getAlpha());
            } else {
                instance.put(v);
            }

        } else {
            instance.put(v);
        }
        return instance;
    }
}
