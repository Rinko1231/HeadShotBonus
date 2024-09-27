package com.rinko1231.headshotbonus;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(HeadshotBonus.MOD_ID)
public class HeadshotBonus {
    public static final String MOD_ID = "headshotbonus";

    public HeadshotBonus() {
        MinecraftForge.EVENT_BUS.register(this);
        HeadshotConfig.setup();
    }

    private void NotifyHeadshooter (Projectile projectile)
    {
        if (!HeadshotConfig.shouldNotifyShooter.get()) return;
        if (projectile.getOwner() instanceof  Player player)
            player.displayClientMessage(Component.translatable("info.headshotbonus.shooter"), true);
    }
    private void NotifyVictim (Entity entity)
    {
        if (!HeadshotConfig.shouldNotifyVictim.get()) return;
        if (projectile.getOwner() == null) return;
        if (entity instanceof  Player player)
            player.displayClientMessage(Component.translatable("info.headshotbonus.victim"), true);
    }
    private boolean isHead (Projectile projectile, LivingEntity entity)
    {
        Vec3 eyePos = entity.getEyePosition();
        // 获取生物的碰撞箱顶端位置
        double headTopY = entity.getBoundingBox().maxY;
        // 计算视线到碰撞箱顶部的距离作为判定范围的半径
        double r = headTopY - eyePos.y;
        // 获取弹射物命中位置
        Vec3 hitVec = projectile.position();
        return  (hitVec.y >= (eyePos.y - r)) && projectile.getOwner()!= null ;
    }

    private boolean canBeHeadshot (LivingEntity entity)
    {
        String entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        boolean notAnimal= !(entity instanceof Animal);
        return (!HeadshotConfig.entityBlacklist.get().contains(entityId))&&notAnimal;
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // 获取受到伤害的实体
        LivingEntity entity = event.getEntity();
        if (canBeHeadshot(entity) ){
        // 检查伤害来源是否为弹射物
         if (event.getSource().getDirectEntity() instanceof Projectile projectile) {
             String projectileId = ForgeRegistries.ENTITY_TYPES.getKey(projectile.getType()).toString();
            if (isHead(projectile,entity) && !HeadshotConfig.projectileBlacklist.get().contains(projectileId)) {
                event.setAmount((float) (event.getAmount() * HeadshotConfig.headshotMultiplier.get()));
                NotifyHeadshooter(projectile);
                NotifyVictim(entity);
            }
          }
        }

    }


}
