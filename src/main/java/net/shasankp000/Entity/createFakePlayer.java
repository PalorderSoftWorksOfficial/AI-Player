package net.shasankp000.Entity;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class createFakePlayer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ai-player");

    private createFakePlayer() {
    }

    public static int createFake(String username, MinecraftServer server, Vec3d pos, double yaw, double pitch, RegistryKey<World> dimensionId, GameMode gamemode, boolean flying) {
        if (server == null || username == null || username.isBlank() || pos == null || dimensionId == null || gamemode == null) {
            return 0;
        }

        ServerCommandSource source = server.getCommandSource().withSilent().withLevel(4);
        String command = String.format(Locale.ROOT,
                "playerspawn %s at %.3f %.3f %.3f facing %.3f %.3f in %s on %s",
                username,
                pos.x,
                pos.y,
                pos.z,
                yaw,
                pitch,
                toCommandGameMode(gamemode),
                dimensionId.getValue());

        int result = server.getCommandManager().executeWithPrefix(source, command);
        if (result <= 0) {
            LOGGER.warn("HeroBot spawn command failed for {}", username);
            return 0;
        }

        if (server.getPlayerManager().getPlayer(username) == null) {
            LOGGER.warn("HeroBot spawn succeeded but {} was not found in the player list", username);
            return 0;
        }

        return 1;
    }

    private static String toCommandGameMode(GameMode gamemode) {
        return switch (gamemode) {
            case SURVIVAL -> "survival";
            case CREATIVE -> "creative";
            case ADVENTURE -> "adventure";
            case SPECTATOR -> "spectator";
        };
    }
}