package io.github.patrickmeow.sealeo.mixin;


import io.github.patrickmeow.sealeo.events.PacketEvent;

import io.github.patrickmeow.sealeo.utils.Utils;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import static io.github.patrickmeow.sealeo.utils.Utils.post;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "channelRead0*", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void packetReceived(ChannelHandlerContext p_channelRead0_1_, Packet<?> packet, CallbackInfo ci) {
        PacketEvent event = new PacketEvent.PacketReceiveEvent(packet);
        if(post(event)) ci.cancel();
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        PacketEvent event = new PacketEvent.PacketSendEvent(packet);
        if(post(event))  {
            ci.cancel();
            //System.out.println("Canceled");
        }
    }
}