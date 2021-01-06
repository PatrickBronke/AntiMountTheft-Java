package de.pbplugins;

import java.util.List;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.npc.NpcDeathEvent;
import net.risingworld.api.events.npc.NpcDeathEvent.Cause;
import net.risingworld.api.events.npc.NpcSpawnEvent;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;

public class EventListener implements Listener {

    private final antimounttheft plugin;

    public EventListener(antimounttheft plugin) {
        this.plugin = plugin;
    }

    @EventMethod
    public void onNpcDeathEvent(NpcDeathEvent event) {
        Npc npc = event.getNpc();
        Cause cause = event.getCause();
        Player player;
        
        int npcID = npc.getGlobalID();
        String npcName = npc.getName();

        if (cause == Cause.KilledByPlayer) {
            if (plugin.hasNPCaPlayer(npc)) {
                player = (Player) event.getKiller();
                if (plugin.isPlayersNPC(player, npc)) {
                    event.setCancelled(false);
                } else {
                    event.setCancelled(true);
                    if (plugin.SpezialNPC().isSpezialNPC(npc)) {
                        npc.setInvincible(true);
                    }
                }
            } else {
                event.setCancelled(false);
            }
        } else {
            if (plugin.SpezialNPC().isSpezialNPC(npc)) {
                event.setCancelled(true);
                npc.setInvincible(true);
            } else {
                event.setCancelled(false);
            }
        }

        if (!event.isCancelled()) {
            if (plugin.hasNPCaPlayer(npc)) {
                List<Long> members = plugin.Attribute.NPC.get.NpcMember(npc);
                List<Long> owners = plugin.getNpcOwners(npc);
                Player player2;
                if (members != null) {
                    for (long m : members) {
                        player2 = plugin.server.getPlayer(m);
                        if (player2 != null) {
                            plugin.Member.removeMemberFromNpc(npc, player2);
                            player2.sendTextMessage("Your Npc " + npcName + " is died!");
                        } else {
                            plugin.Member.removeMemberFromNpc(npc, m);
                        }
                    }
                }
                if (owners != null) {
                    for (long o : owners) {
                        player2 = plugin.server.getPlayer(o);
                        if (player2 != null) {
                            int index = 0;
                            for (int id : plugin.Attribute.Player.get.NpcOwned(player2)){
                                if (id != npcID){
                                   index =+1;
                                }
                            }
                            plugin.Attribute.Player.get.NpcOwned(player2).remove(index);
                            player2.sendTextMessage("Your Npc " + npcName + " is died!");
                        }
                    }
                }
            }
        }
    }

    @EventMethod
    public void onNpcSpawnEvent(NpcSpawnEvent event) {
        Npc npc = event.getNpc();
        if (npc.getType() == Npc.Type.Mount) {
            plugin.Attribute.NPC.set.spTyp(npc, 0);
            plugin.Attribute.NPC.set.spPos(npc, null);
            npc.setAttribute(plugin.Attribute.NPC.NpcMember, plugin.Member.getNpcForMemberFromDB(npc));
            if (plugin.SpezialNPC().isSpezialNPC(npc)) {
                int typ = plugin.SpezialNPC().getSpezialTypFromDB(npc);
                plugin.Attribute.NPC.set.spTyp(npc, typ);
                if (typ == 1 || typ == 3) {
                    plugin.Attribute.NPC.set.spPos(npc, plugin.SpezialNPC().getSpezialPosFromDB(npc));
                }
            }
        }
    }

}
