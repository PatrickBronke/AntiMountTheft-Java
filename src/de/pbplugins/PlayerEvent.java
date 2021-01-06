package de.pbplugins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.risingworld.api.Timer;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.general.ShutdownEvent;
import net.risingworld.api.events.npc.NpcSpawnEvent;
import net.risingworld.api.events.player.PlayerChangePositionEvent;
import net.risingworld.api.events.player.PlayerConnectEvent;
import net.risingworld.api.events.player.PlayerDisconnectEvent;
import net.risingworld.api.events.player.PlayerDismountNpcEvent;
import net.risingworld.api.events.player.PlayerHitNpcEvent;
import net.risingworld.api.events.player.PlayerMountNpcEvent;
import net.risingworld.api.events.player.PlayerNpcInteractionEvent;
import net.risingworld.api.events.player.PlayerNpcInventoryAccessEvent;
import net.risingworld.api.objects.Inventory;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Vector3f;

public class PlayerEvent implements Listener {

    private final antimounttheft plugin;
    private int debug;
    private final String Rot = "[#ff0000]", Grün = "[#00ff00]", Orange = "[#ffa500]";

    public PlayerEvent(antimounttheft plugin) {
        this.plugin = plugin;
        this.debug = plugin.debug;
    }

    @EventMethod
    public void onPlayerMountNpcEvent(PlayerMountNpcEvent event) {
        Player player = event.getPlayer();
        Npc npc = event.getNpc();
        if (!npc.hasAttribute(plugin.Attribute.NPC.spTyp)) {
            plugin.Attribute.NPC.set.spTyp(npc, -1);
        }
        if (!npc.hasAttribute(plugin.Attribute.NPC.spPos)) {
            plugin.Attribute.NPC.set.spPos(npc, null);
        }

        if (!npc.hasAttribute(plugin.Attribute.NPC.NpcMember)) {
            npc.setAttribute(plugin.Attribute.NPC.NpcMember, plugin.Member.getNpcForMemberFromDB(npc));
        }

        if (debug > 0) {
            plugin.dl.info("[AntiMountTheft] onPlayerMountNpc - isSpezialNPC: " + plugin.SpezialNPC().isSpezialNPC(npc));
        }
        if (plugin.SpezialNPC().isSpezialNPC(npc)) {
            if (!npc.isInvincible()) {
                npc.setInvincible(true);
            }
            int typ = plugin.SpezialNPC().getSpezialTypFromDB(npc);
            plugin.Attribute.NPC.set.spTyp(npc, typ);
            if (typ == 1) {
                plugin.Attribute.NPC.set.spPos(npc, plugin.SpezialNPC().getSpezialPosFromDB(npc));
                if (debug > 0) {
                    plugin.dl.info("[AntiMountTheft] NPC (ID: " + npc.getGlobalID() + ") Spezial Pos: " + plugin.Attribute.NPC.get.spPos(npc));
                }
            }
            if (typ == 3) {
                if (plugin.Attribute.NPC.get.spPos(npc) == null) {
                    plugin.Attribute.NPC.set.spPos(npc, plugin.SpezialNPC().getSpezialPosFromDB(npc));
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] NPC (ID: " + npc.getGlobalID() + ") Spezial Pos: " + plugin.Attribute.NPC.get.spPos(npc));
                    }
                }

                if (plugin.Attribute.NPC.get.spTimer(npc) == null) {
                    Timer timer = plugin.SpezialNPC().getNewSpezialTimer(npc, player);
                    plugin.Attribute.NPC.set.spTimer(npc, timer);
                }

            }
            if (player.isAdmin()) {
                plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
                player.sendTextMessage(Orange + "You select the Spezial-NPC!");
                player.sendTextMessage(Orange + "ID: " + npc.getGlobalID());
                player.sendTextMessage(Orange + "Type: " + plugin.SpezialNPC().setTypIntToString(plugin.Attribute.NPC.get.spTyp(npc)));
            }
        }

        if (!plugin.hasNPCaPlayer(npc)) {
            if (plugin.addOwnerToNPC(npc, player)) {
                event.setCancelled(false);
                plugin.Attribute.Player.set.MountNow(player, npc.getGlobalID());
                plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
                player.sendTextMessage(Grün + "This NPC is now your NPC!");
                player.sendTextMessage(Orange + "You select the NPC! ID: " + npc.getGlobalID());
                if (debug > 0) {
                    plugin.dl.info("[AntiMountTheft] MountNow = " + plugin.Attribute.Player.get.MountNow(player));
                }
            }
        } else if (plugin.isPlayersNPC(player, npc)) {
            if (debug > 0) {
                plugin.dl.info("[AntiMountTheft] onPlayerMountNpcEvent: isPlayersNPC = true");
            }
            event.setCancelled(false);
            plugin.Attribute.Player.set.MountNow(player, npc.getGlobalID());
            plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
            player.sendTextMessage(Orange + "You select the NPC! ID: " + npc.getGlobalID());

            if (plugin.SpezialNPC().isSpezialNPC(npc)) {

                plugin.Attribute.Player.set.spOwned(player, npc.getGlobalID());
            }

        } else if (plugin.Member.isNpcMember(player, npc)) {
            if (debug > 0) {
                plugin.dl.info("[AntiMountTheft] onPlayerMountNpcEvent: isPlayersNPC = true");
            }
            event.setCancelled(false);
            plugin.Attribute.Player.set.MountNow(player, -2);
            plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
            player.sendTextMessage(Orange + "You select the NPC! ID: " + npc.getGlobalID());

        } else if (plugin.Attribute.NPC.get.spTyp(npc) != -1) {

            if (plugin.Attribute.Player.get.spOwned(player) == -1 || plugin.Attribute.Player.get.spOwned(player) == npc.getGlobalID()) {
                if (!plugin.Attribute.NPC.get.spHasPlayer(npc)) {
                    event.setCancelled(false);
                    plugin.Attribute.NPC.set.spHasPlayer(npc, true);
                    plugin.Attribute.Player.set.MountNow(player, npc.getGlobalID());
                    plugin.Attribute.Player.set.spOwned(player, npc.getGlobalID());

                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] MountNow = " + plugin.Attribute.Player.get.MountNow(player));
                        plugin.dl.info("[AntiMountTheft] spOwned = " + plugin.Attribute.Player.get.spOwned(player));
                    }

                    if (plugin.Attribute.NPC.get.spTyp(npc) == 3 && !plugin.Attribute.NPC.get.spTimer(npc).isActive()) {
                        plugin.Attribute.NPC.get.spTimer(npc).start();
                    }
                } else {
                    event.setCancelled(true);
                    player.sendTextMessage(Rot + "This spezial npc have a player!");
                }
            } else {
                event.setCancelled(true);
                player.sendTextMessage(Rot + "You can get only one SpezialNpc!");
            }

        } else if (player.isAdmin()) {
            if (plugin.debug > 0) {
                plugin.dl.info("[AntiMountTheft] onPlayerMountNpcEvent: Player = Admin");
            }
            event.setCancelled(false);
            plugin.Attribute.Player.set.MountNow(player, npc.getGlobalID());
            plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
            player.sendTextMessage(Orange + "You select the NPC! ID: " + npc.getGlobalID());

        } else {
            if (plugin.debug > 0) {
                plugin.dl.info("[AntiMountTheft] onPlayerMountNpcEvent: Event abgebrochen: 'Not your NPC!'");
            }
            event.setCancelled(true);
            player.sendTextMessage(Rot + "This is not your NPC!");
        }

        if (plugin.debug > 0) {
            plugin.dl.info("[AntiMountTheft] onPlayerMountNpcEvent: setCancelled " + event.isCancelled());
        }
    }

    @EventMethod
    public void onPlayerDismountNpcEvent(PlayerDismountNpcEvent event) {
        Player player = event.getPlayer();
        if (debug > 0) {
            plugin.dl.info("[AntiMountTheft] onPlayerDismountNpcEvent");
        }
        plugin.Attribute.Player.set.MountNow(player, -1);
        if (debug > 0) {
            plugin.dl.info("[AntiMountTheft] onPlayerDismountNpcEvent: MountNow" + plugin.Attribute.Player.get.MountNow(player));
        }
    }

    @EventMethod
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        Player player = event.getPlayer();
        if (debug > 0) {
            plugin.dl.info("[AntiMountTheft] spOwned = " + plugin.Attribute.Player.get.spOwned(player));
        }
        if (plugin.Attribute.Player.get.spOwned(player) != -1) {
            if (debug > 0) {
                plugin.dl.info("[AntiMountTheft] spOwned != -1");
            }
            Npc npc;
            npc = plugin.world.getNpc(plugin.Attribute.Player.get.spOwned(player));
            if (npc != null && plugin.SpezialNPC().isSpezialNPC(npc)) {
                if (debug > 0) {
                    plugin.dl.info("[AntiMountTheft] npc ID = " + npc.getGlobalID() + " " + npc.getName());
                }
                plugin.Attribute.NPC.set.spHasPlayer(npc, false);
                if (plugin.Attribute.NPC.get.spTyp(npc) == 1) {
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] spTyp = 1");
                    }
                    Vector3f pos = plugin.Attribute.NPC.get.spPos(npc);
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] Npc Pos Old: " + npc.getPosition());
                    }
                    npc.setPosition(pos);
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] Npc Pos New: " + npc.getPosition());
                    }
                } else if (plugin.Attribute.NPC.get.spTyp(npc) == 2) {
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] spTyp = 2");
                    }
                    Vector3f pos = plugin.Attribute.NPC.get.spPos(npc);
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] Npc Pos Old: " + npc.getPosition());
                    }
                    npc.setPosition(pos);
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] Npc Pos New: " + npc.getPosition());
                    }
                    if (plugin.Attribute.NPC.get.spTimer(npc) != null) {
                        plugin.Attribute.NPC.get.spTimer(npc).kill();
                    }
                }

            }
        }
    }

    @EventMethod
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        Player player = event.getPlayer();
        List<Integer> ids = plugin.getNpcIDfromDB(player);
        player.setAttribute(plugin.Attribute.Player.NpcOwned, ids);
        plugin.Attribute.Player.set.MountNow(player, -1);
        plugin.Attribute.Player.set.spOwned(player, -1);
        plugin.Attribute.Player.set.amtSelect(player, -1);
        if (plugin.debug > 0) {
            plugin.dl.info("[AntiMountTheft] Player Connect");
            plugin.dl.info("[AntiMountTheft] onPlayerConnectEvent: Liste NPCs");
            plugin.dl.info("[AntiMountTheft] onPlayerConnectEvent: ---------------------");
            ids.forEach((id) -> {
                plugin.dl.info("[AntiMountTheft] onPlayerConnectEvent: " + id);
            });
        }
    }

    @EventMethod
    public void onShutdownEvent(ShutdownEvent event) {
        plugin.SpezialNPC().getList().forEach((id) -> {
            Npc npc = plugin.world.getNpc(id);
            if (npc != null) {
                if (plugin.Attribute.NPC.get.spTyp(npc) == 1) {
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] NPC (ID: " + id + ") Old Pos: " + npc.getPosition());
                    }
                    npc.setPosition(plugin.Attribute.NPC.get.spPos(npc));
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] NPC (ID: " + id + ") New Pos: " + npc.getPosition());
                    }
                } else if (plugin.Attribute.NPC.get.spTyp(npc) == 3) {
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] NPC (ID: " + id + ") Old Pos: " + npc.getPosition());
                    }
                    npc.setPosition(plugin.Attribute.NPC.get.spPos(npc));
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] NPC (ID: " + id + ") New Pos: " + npc.getPosition());
                    }
                    if (plugin.Attribute.NPC.get.spTimer(npc) != null) {
                        plugin.Attribute.NPC.get.spTimer(npc).kill();
                    }
                }
            }
        });

    }

    @EventMethod
    public void onPlayerNpcInteractionEvent(PlayerNpcInteractionEvent event) {
        Player player = event.getPlayer();
        Npc npc = event.getNpc();

        if (player.getEquippedItem() == null || player.getEquippedItem().getDefinition().getID() != 490) {
            if (plugin.Attribute.Player.get.amtSelect(player) == -2) {
                if (plugin.SpezialNPC().isSpezialNPC(npc)) {

                    if (!npc.hasAttribute(plugin.Attribute.NPC.spTyp)) {
                        plugin.Attribute.NPC.set.spTyp(npc, 0);
                    }
                    if (!npc.hasAttribute(plugin.Attribute.NPC.spPos)) {
                        plugin.Attribute.NPC.set.spPos(npc, null);
                    }

                    if (!npc.hasAttribute(plugin.Attribute.NPC.NpcMember)) {
                        npc.setAttribute(plugin.Attribute.NPC.NpcMember, plugin.Member.getNpcForMemberFromDB(npc));
                    }

                    if (!npc.isInvincible()) {
                        npc.setInvincible(true);
                    }
                    int typ = plugin.SpezialNPC().getSpezialTypFromDB(npc);
                    if (plugin.Attribute.NPC.get.spTyp(npc) == 0) {
                        plugin.Attribute.NPC.set.spTyp(npc, typ);
                    }
                    if (typ == 1 && plugin.Attribute.NPC.get.spPos(npc) == null) {
                        plugin.Attribute.NPC.set.spPos(npc, plugin.SpezialNPC().getSpezialPosFromDB(npc));
                        if (debug > 0) {
                            plugin.dl.info("[AntiMountTheft] NPC (ID: " + npc.getGlobalID() + ") Spezial Pos: " + plugin.Attribute.NPC.get.spPos(npc));
                        }
                    }
                    if (typ == 3) {
                        if (plugin.Attribute.NPC.get.spPos(npc) == null) {
                            plugin.Attribute.NPC.set.spPos(npc, plugin.SpezialNPC().getSpezialPosFromDB(npc));
                            if (debug > 0) {
                                plugin.dl.info("[AntiMountTheft] NPC (ID: " + npc.getGlobalID() + ") Spezial Pos: " + plugin.Attribute.NPC.get.spPos(npc));
                            }
                        }

                        if (plugin.Attribute.NPC.get.spTimer(npc) == null) {
                            Timer timer = plugin.SpezialNPC().getNewSpezialTimer(npc, player);
                            plugin.Attribute.NPC.set.spTimer(npc, timer);
                        }

                    }
                    if (player.isAdmin()) {
                        plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
                        player.sendTextMessage(Orange + "You select the Spezial-NPC!");
                        player.sendTextMessage(Orange + "ID: " + npc.getGlobalID());
                        player.sendTextMessage(Orange + "Type: " + plugin.SpezialNPC().setTypIntToString(plugin.Attribute.NPC.get.spTyp(npc)));
                    }
                }
                if (!plugin.hasNPCaPlayer(npc)) {
                    if (plugin.addOwnerToNPC(npc, player)) {
                        npc.setAttribute(plugin.Attribute.NPC.NpcMember, plugin.Member.getNpcForMemberFromDB(npc));
                        player.sendTextMessage(Grün + "This NPC is now your NPC!");
                        player.sendTextMessage(Orange + "You select the NPC! ID: " + npc.getGlobalID());
                        plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
                        event.setCancelled(false);
                    }
                } else {

                    if (plugin.isPlayersNPC(player, npc) || plugin.Member.isNpcMember(player, npc) || player.isAdmin()) {
                        player.sendTextMessage(Orange + "You select the NPC! ID: " + npc.getGlobalID());
                        plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                    }

                }
            }
        } else {
            if (player.getEquippedItem().getDefinition().getID() == 490) {
                int health = npc.getHealth();
                int effect = plugin.HealthEffect();
                if (health < 100) {
                    if (health + effect >= 100) {
                        npc.setHealth(100);
                    } else {
                        npc.setHealth(health + effect);
                    }
                    player.getInventory().removeItem(player.getInventory().getQuickslotFocus(), Inventory.SlotType.Quickslot, 1);
                    player.showStatusMessage("NPC's health now " + npc.getHealth(), 3);
                } else {
                    player.sendTextMessage("NPC's health is maximum!");
                }
            }

        }

    }

    @EventMethod
    public void onPlayerNpcInventoryAccessEvent(PlayerNpcInventoryAccessEvent event) {
        Npc npc = event.getNpc();
        Player player = event.getPlayer();

        if (plugin.hasNPCaPlayer(npc)) {
            if (plugin.isPlayersNPC(player, npc) || player.isAdmin()) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventMethod
    public void onPlayerHitNpcEvent(PlayerHitNpcEvent event) {
        Player player = event.getPlayer();
        Npc npc = event.getNpc();
        int healNow = npc.getHealth() - event.getDamage();
        if (event.getDamage() > npc.getHealth()){
            event.setDamage((short)npc.getHealth());
        }
        if (!event.isCancelled()) {
            player.showStatusMessage("NPC Heal: " + (npc.getHealth() - event.getDamage()) + " (-" + event.getDamage() + ")", 3);
        }
    }
}
