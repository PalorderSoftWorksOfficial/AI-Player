package net.shasankp000.Entity;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public final class createFakePlayer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ai-player");

    private createFakePlayer() {
    }

    public static int createFake(String username, MinecraftServer server, Vec3d pos, double yaw, double pitch, RegistryKey<World> dimensionId, GameMode gamemode, boolean flying) {
        if (server == null || username == null || username.isBlank() || pos == null || dimensionId == null || gamemode == null) {
            return 0;
        }

        try {
            Class<?> botPlayerClass = Class.forName("hero.bane.herobot.bot.BotPlayer");
            Method createFake = botPlayerClass.getMethod("createFake", String.class, MinecraftServer.class, Vec3d.class, double.class, double.class, RegistryKey.class, GameMode.class, boolean.class);
            Object result = createFake.invoke(null, username, server, pos, yaw, pitch, dimensionId, gamemode, flying);
            if (result instanceof Integer value && value > 0 && server.getPlayerManager().getPlayer(username) != null) {
                return 1;
            }
        } catch (ReflectiveOperationException e) {
            LOGGER.error("HeroBot spawn bridge failed for {}", username, e);
        }

        return 0;
    }
}