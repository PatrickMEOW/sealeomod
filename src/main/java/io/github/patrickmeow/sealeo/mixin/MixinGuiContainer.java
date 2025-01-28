package io.github.patrickmeow.sealeo.mixin;

import io.github.patrickmeow.sealeo.events.SlotClickEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.patrickmeow.sealeo.utils.Utils.post;
@Mixin(value = GuiContainer.class, priority = 1000)
public class MixinGuiContainer {

    @Inject(method = "handleMouseClick", at = @At(value = "HEAD"), cancellable = true)
    public void handleMouseClick(Slot slot, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if(slot == null) return;
        GuiContainer $this = (GuiContainer) (Object) this;
        SlotClickEvent event = new SlotClickEvent($this, slot, slotId);
        post(event);
        if(event.isCanceled()) {
            ci.cancel();
        }
    }
}