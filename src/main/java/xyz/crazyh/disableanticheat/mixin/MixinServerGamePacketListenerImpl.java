package xyz.crazyh.disableanticheat.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {
    @Shadow
    private int aboveGroundTickCount;

    @Shadow
    private int aboveGroundVehicleTickCount;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void restrictFloatingBits(CallbackInfo ci) {
        if (aboveGroundTickCount > 70) {
            aboveGroundTickCount--;
        }
        if (aboveGroundVehicleTickCount > 70) {
            aboveGroundVehicleTickCount--;
        }
    }

    @Redirect(
            method ="handleMoveVehicle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"
            )
    )
    private boolean playerVehicle(ServerGamePacketListenerImpl serverPlayNetworkHandler) {
        return true;
    }

    @Redirect(
            method = "handleMovePlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"
            )
    )
    private boolean playerItself(ServerGamePacketListenerImpl instance) {
        return true;
    }
}
