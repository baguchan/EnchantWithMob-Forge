package baguchan.enchantwithmob;

import baguchan.enchantwithmob.api.IEnchantCap;
import baguchan.enchantwithmob.capability.MobEnchantHandler;
import baguchan.enchantwithmob.message.MobEnchantedMessage;
import baguchan.enchantwithmob.mobenchant.MobEnchant;
import baguchan.enchantwithmob.registry.MobEnchants;
import baguchan.enchantwithmob.registry.ModItems;
import baguchan.enchantwithmob.utils.MobEnchantCombatRules;
import baguchan.enchantwithmob.utils.MobEnchantUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = EnchantWithMob.MODID)
public class CommonEventHandler {

    /*
     * this event handle the Ender dragon mob enchant
     */
    @SubscribeEvent
    public static void onEnderDragonSpawn(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof IEnchantCap cap && event.getEntity() instanceof EnderDragon livingEntity) {
            LevelAccessor world = event.getWorld();
            if (!world.isClientSide()) {
                if (!cap.getEnchantCap().hasEnchant()) {
                    if (isSpawnAlwayEnchantableAncientEntity(livingEntity)) {
                        int i = 0;
                        float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                        switch (world.getDifficulty()) {
                            case EASY:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 30);

                                MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, true);
                                break;
                            case NORMAL:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(15)) * difficultScale, 1, 60);

                                MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, true);
                                break;
                            case HARD:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(20)) * difficultScale, 1, 100);

                                MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, true);
                                break;
                        }
                        livingEntity.setHealth(livingEntity.getMaxHealth());
                    }

                    // On add MobEnchant Alway Enchantable Mob
                    if (isSpawnAlwayEnchantableEntity(livingEntity)) {
                        int i = 0;
                        float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                        switch (world.getDifficulty()) {
                            case EASY:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

                                MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, false);
                                break;
                            case NORMAL:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

                                MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, false);
                                break;
                            case HARD:
                                i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

                                MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, false);
                                break;
                        }

                        livingEntity.setHealth(livingEntity.getMaxHealth());
                    }
                }
            }
        }
    }

    /*
     * handle the Normal Entity Mob Enchant
     */
    @SubscribeEvent
    public static void onSpawnEntity(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getEntity() instanceof IEnchantCap cap) {
            LevelAccessor world = event.getWorld();
            if (!world.isClientSide()) {
                LivingEntity livingEntity = event.getEntityLiving();

                if (isSpawnAlwayEnchantableAncientEntity(livingEntity)) {
                    int i = 0;
                    float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                    switch (world.getDifficulty()) {
                        case EASY:
                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 30);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, true);
                            break;
                        case NORMAL:
                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(15)) * difficultScale, 1, 60);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, true);
                            break;
                        case HARD:
                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(20)) * difficultScale, 1, 100);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, true);
                            break;
                    }

                    livingEntity.setHealth(livingEntity.getMaxHealth());
                }

                // On add MobEnchant Alway Enchantable Mob
                if (isSpawnAlwayEnchantableEntity(livingEntity)) {
                    int i = 0;
                    float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                    switch (world.getDifficulty()) {
                        case EASY:
                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, false);
                            break;
                        case NORMAL:
                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, false);
                            break;
                        case HARD:
                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, false, false);
                            break;
                    }

                    livingEntity.setHealth(livingEntity.getMaxHealth());
                }


                if (EnchantConfig.COMMON.naturalSpawnEnchantedMob.get() && isSpawnEnchantableEntity(event.getEntity())) {

                    if (!(livingEntity instanceof Animal) && !(livingEntity instanceof WaterAnimal) || EnchantConfig.COMMON.spawnEnchantedAnimal.get()) {
                        if (event.getSpawnReason() != MobSpawnType.BREEDING && event.getSpawnReason() != MobSpawnType.CONVERSION && event.getSpawnReason() != MobSpawnType.STRUCTURE && event.getSpawnReason() != MobSpawnType.MOB_SUMMONED) {
                            if (world.getRandom().nextFloat() < (EnchantConfig.COMMON.difficultyBasePercent.get() * world.getDifficulty().getId()) + world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() * EnchantConfig.COMMON.effectiveBasePercent.get()) {
                                if (!world.isClientSide()) {
                                    int i = 0;
                                    float difficultScale = world.getCurrentDifficultyAt(livingEntity.blockPosition()).getEffectiveDifficulty() - 0.2F;
                                    switch (world.getDifficulty()) {
                                        case EASY:
                                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 20);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, true, false);
                                            break;
                                        case NORMAL:
                                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(5)) * difficultScale, 1, 40);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, true, false);
                                            break;
                                        case HARD:
                                            i = (int) Mth.clamp((5 + world.getRandom().nextInt(10)) * difficultScale, 1, 50);

                                            MobEnchantUtils.addRandomEnchantmentToEntity(livingEntity, cap, world.getRandom(), i, true, true, false);
                                            break;
                                    }

                                    livingEntity.setHealth(livingEntity.getMaxHealth());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isSpawnAlwayEnchantableEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && EnchantConfig.COMMON.ALWAY_ENCHANTABLE_MOBS.get().contains(ForgeRegistries.ENTITIES.getKey(entity.getType()).toString());
    }

    private static boolean isSpawnAlwayEnchantableAncientEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && EnchantConfig.COMMON.ALWAY_ENCHANTABLE_ANCIENT_MOBS.get().contains(ForgeRegistries.ENTITIES.getKey(entity.getType()).toString());
    }

    private static boolean isSpawnEnchantableEntity(Entity entity) {
        return !(entity instanceof Player) && !(entity instanceof ArmorStand) && !(entity instanceof Boat) && !(entity instanceof Minecart) && !EnchantConfig.COMMON.ENCHANT_ON_SPAWN_EXCLUSION_MOBS.get().contains(ForgeRegistries.ENTITIES.getKey(entity.getType()).toString());
    }

    @SubscribeEvent
    public static void onUpdateEnchanted(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        if (livingEntity instanceof IEnchantCap cap) {
            for (MobEnchantHandler enchantHandler : cap.getEnchantCap().getMobEnchants()) {
                enchantHandler.getMobEnchant().tick(livingEntity, enchantHandler.getEnchantLevel());
            }

        }
        ;
    }


    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

            if (attacker instanceof IEnchantCap cap) {
                if (cap.getEnchantCap().hasEnchant()) {
                    int mobEnchantLevel = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.STRONG.get());
                    int mobEnchantSize = cap.getEnchantCap().getMobEnchants().size();

                    //make snowman stronger
                    if (!livingEntity.isDamageSourceBlocked(event.getSource()) && event.getAmount() == 0) {
                        event.setAmount(MobEnchantCombatRules.getDamageAddition(1, mobEnchantLevel, mobEnchantSize));
                    } else if (event.getAmount() > 0) {
                        event.setAmount(MobEnchantCombatRules.getDamageAddition(event.getAmount(), mobEnchantLevel, mobEnchantSize));
                    }
                }

                if (cap.getEnchantCap().hasEnchant() && MobEnchantUtils.findMobEnchantFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.POISON.get())) {
                    int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.POISON.get());

                    if (event.getAmount() > 0) {
                        if (attacker.getRandom().nextFloat() < i * 0.125F) {
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), attacker);
                        }
                    }
                }


            }
        }

        if (livingEntity instanceof IEnchantCap cap) {
            if (!event.getSource().isBypassInvul() && cap.getEnchantCap().hasEnchant()) {
                int mobEnchantLevel = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.PROTECTION.get());
                int mobEnchantSize = cap.getEnchantCap().getMobEnchants().size();

                event.setAmount(MobEnchantCombatRules.getDamageReduction(event.getAmount(), mobEnchantLevel, mobEnchantSize));
            }
            if (event.getSource().getDirectEntity() != null) {
               if (cap.getEnchantCap().hasEnchant()) {
                   int i = MobEnchantUtils.getMobEnchantLevelFromHandler(cap.getEnchantCap().getMobEnchants(), MobEnchants.THORN.get());

                   if (event.getSource().getDirectEntity() instanceof LivingEntity && !event.getSource().isProjectile() && !event.getSource().isMagic() && livingEntity.getRandom().nextFloat() < i * 0.1F) {
                       LivingEntity attacker = (LivingEntity) event.getSource().getDirectEntity();

                       attacker.hurt(DamageSource.thorns(livingEntity), MobEnchantCombatRules.getThornDamage(event.getAmount(), cap.getEnchantCap()));
                   }
               }
            }
        }
    }




    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getItemStack();
        Entity entityTarget = event.getTarget();

        if (stack.getItem() == ModItems.MOB_ENCHANT_BOOK.get() && !event.getPlayer().getCooldowns().isOnCooldown(stack.getItem())) {
            if (entityTarget instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) entityTarget;
                if (MobEnchantUtils.hasMobEnchant(stack)) {

                    if (target instanceof IEnchantCap cap) {
                        boolean flag = MobEnchantUtils.addItemMobEnchantToEntity(stack, target, event.getPlayer(), cap);

                        if (flag) {
                            event.getEntity().playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                            stack.hurtAndBreak(1, event.getPlayer(), (entity) -> entity.broadcastBreakEvent(event.getHand()));

                            event.getPlayer().getCooldowns().addCooldown(stack.getItem(), 60);

                            event.setCancellationResult(InteractionResult.SUCCESS);
                            event.setCanceled(true);
                        } else {
                            event.getPlayer().displayClientMessage(new TranslatableComponent("enchantwithmob.cannot.enchant"), true);
                            event.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);
                            event.setCancellationResult(InteractionResult.FAIL);
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }

        if (stack.getItem() == ModItems.MOB_UNENCHANT_BOOK.get() && !event.getPlayer().getCooldowns().isOnCooldown(stack.getItem())) {
            if (entityTarget instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) entityTarget;

                if (target instanceof IEnchantCap cap) {
                    MobEnchantUtils.removeMobEnchantToEntity(target, cap);
                    event.getEntity().playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);

                    stack.hurtAndBreak(1, event.getPlayer(), (entity) -> entity.broadcastBreakEvent(event.getHand()));

                    event.getPlayer().getCooldowns().addCooldown(stack.getItem(), 80);

                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
            }
        }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack stack1 = event.getLeft();
        ItemStack stack2 = event.getRight();

        if (stack1.getItem() == ModItems.MOB_ENCHANT_BOOK.get() && stack2.getItem() == ModItems.MOB_ENCHANT_BOOK.get()) {
            Map<MobEnchant, Integer> map = MobEnchantUtils.getEnchantments(stack1);

            Map<MobEnchant, Integer> map1 = MobEnchantUtils.getEnchantments(stack2);
            boolean flag2 = false;
            boolean flag3 = false;

            for (MobEnchant enchantment1 : map1.keySet()) {
                if (enchantment1 != null) {
                    int i2 = map.getOrDefault(enchantment1, 0);
                    int j2 = map1.get(enchantment1);
                    j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                    boolean flag1 = true;

                    for (MobEnchant enchantment : map.keySet()) {
                        if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
                            flag1 = false;
                        }
                    }

                    if (!flag1) {
                        flag3 = true;
                    } else {
                        flag2 = true;
                        if (j2 > enchantment1.getMaxLevel()) {
                            j2 = enchantment1.getMaxLevel();
                        }

                        map.put(enchantment1, j2);
                        int k3 = 0;
                        switch (enchantment1.getRarity()) {
                            case COMMON:
                                k3 = 1;
                                break;
                            case UNCOMMON:
                                k3 = 2;
                                break;
                            case RARE:
                                k3 = 4;
                                break;
                            case VERY_RARE:
                                k3 = 8;
                        }
                    }
                }
            }
            if (!stack1.isEmpty()) {
                int k2 = stack1.getBaseRepairCost();
                if (!stack2.isEmpty() && k2 < stack2.getBaseRepairCost()) {
                    k2 = stack2.getBaseRepairCost();
                }

                ItemStack stack3 = new ItemStack(stack1.getItem());

                MobEnchantUtils.setEnchantments(map, stack3);
                stack3.setRepairCost(4 + k2);
                event.setOutput(stack3);
                event.setCost(4 + k2);
                event.setMaterialCost(1);
            }
        }
    }

    @SubscribeEvent
    public static void onExpDropped(LivingExperienceDropEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof IEnchantCap cap) {
            if (cap.getEnchantCap().hasEnchant()) {
                if (cap.getEnchantCap().isAncient()) {
                    event.setDroppedExperience(event.getDroppedExperience() + MobEnchantUtils.getExperienceFromMob(cap) * 5);
                } else {
                    event.setDroppedExperience(event.getDroppedExperience() + MobEnchantUtils.getExperienceFromMob(cap));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        if (player instanceof ServerPlayer) {
            if (player instanceof IEnchantCap cap) {
                for (int i = 0; i < cap.getEnchantCap().getMobEnchants().size(); i++) {
                    EnchantWithMob.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new MobEnchantedMessage(player, cap.getEnchantCap().getMobEnchants().get(i)));

                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player playerEntity = event.getPlayer();
        if (playerEntity instanceof IEnchantCap cap) {
            if (!playerEntity.level.isClientSide()) {
                for (int i = 0; i < cap.getEnchantCap().getMobEnchants().size(); i++) {
                    EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new MobEnchantedMessage(playerEntity, cap.getEnchantCap().getMobEnchants().get(i)));
                }
            }
        }
        ;
    }

    @SubscribeEvent
    public static void bringBackEnchant(EntityJoinWorldEvent event) {
        Entity livingEntity = event.getEntity();
        if (livingEntity instanceof IEnchantCap cap) {
            if (!event.getWorld().isClientSide()) {
                //Sync Client Enchant
                for (int i = 0; i < cap.getEnchantCap().getMobEnchants().size(); i++) {
                    MobEnchantedMessage message = new MobEnchantedMessage(livingEntity, cap.getEnchantCap().getMobEnchants().get(i));
                    EnchantWithMob.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), message);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getPlayer();
        if (!event.isWasDeath()) {
            ((IEnchantCap) oldPlayer).getEnchantCap().getMobEnchants().forEach(mobEnchantHandler -> {
                ((IEnchantCap) newPlayer).getEnchantCap().addMobEnchant(newPlayer, mobEnchantHandler.getMobEnchant(), mobEnchantHandler.getEnchantLevel());
            });
        }
    }
}
