package com.rinko1231.headshotbonus;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;


public class HeadshotConfig
{
    public static ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> entityBlacklist;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> projectileBlacklist;
    public static ForgeConfigSpec.DoubleValue headshotMultiplier;
    public static ForgeConfigSpec.BooleanValue shouldNotifyShooter;
    public static ForgeConfigSpec.BooleanValue shouldNotifyVictim;
    static
    {
        BUILDER.push("Headshot Bonus Config");

        entityBlacklist = BUILDER
                .comment("Animals have been included")
                .defineList("Entity Blacklist", List.of("minecraft:ender_dragon"),
                        element -> element instanceof String);

        projectileBlacklist = BUILDER
                .comment("Projectiles that do not trigger headshots")
                .defineList("Projectile Blacklist", List.of("minecraft:snowball"),
                        element -> element instanceof String);

        headshotMultiplier = BUILDER
                .defineInRange("Headshot damage Multiplier", 2,1,114514d);

        shouldNotifyShooter = BUILDER
                .define("Whether to notify the one who shoots the head", true);

        shouldNotifyVictim = BUILDER
                .define("Whether to notify the victim", false);

        SPEC = BUILDER.build();
    }

    public static void setup()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "HeadshotBonus.toml");
    }


}