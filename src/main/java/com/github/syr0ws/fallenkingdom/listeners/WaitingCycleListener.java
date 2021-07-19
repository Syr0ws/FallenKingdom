package com.github.syr0ws.fallenkingdom.listeners;

import com.github.syr0ws.fallenkingdom.game.controller.FKController;
import com.github.syr0ws.fallenkingdom.game.model.FKModel;
import com.github.syr0ws.universe.events.GamePlayerJoinEvent;
import com.github.syr0ws.universe.game.model.GamePlayer;
import com.github.syr0ws.universe.game.model.mode.DefaultModeType;
import com.github.syr0ws.universe.modules.lang.LangService;
import com.github.syr0ws.universe.modules.lang.messages.impl.Text;
import com.github.syr0ws.universe.settings.types.MutableSetting;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class WaitingCycleListener implements Listener {

    private final FKController controller;
    private final FKModel model;
    private final LangService service;

    public WaitingCycleListener(FKController controller, FKModel model, LangService service) {

        if(controller == null)
            throw new IllegalArgumentException("FKController cannot be null.");

        if(model == null)
            throw new IllegalArgumentException("FKModel cannot be null.");

        if(service == null)
            throw new IllegalArgumentException("LangService cannot be null.");

        this.controller = controller;
        this.model = model;
        this.service = service;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        MutableSetting<Integer> setting = this.model.getSettings().getMaxPlayersSetting();

        int online = Bukkit.getOnlinePlayers().size();

        // Max number of online players not reached.
        if(online < setting.getValue()) return;

        Text text = this.service.getMessage("kick-full-message", Text.class);

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, text.getText());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {
        GamePlayer player = event.getGamePlayer();
        this.controller.setMode(player, DefaultModeType.WAITING);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPortalTravel(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }
}
