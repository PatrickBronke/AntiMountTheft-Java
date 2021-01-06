package de.pbplugins;

import java.util.ArrayList;
import java.util.List;
import net.risingworld.api.Timer;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerCommandEvent;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Npc.Type;
import net.risingworld.api.objects.Player;

public class PlayerEventCommand implements Listener {

    private final antimounttheft plugin;
    private final String Rot = "[#ff0000]", Grün = "[#00ff00]", Orange = "[#ffa500]";

    public PlayerEventCommand(antimounttheft plugin) {
        this.plugin = plugin;
    }

    @EventMethod
    public void onPlayerCommandEvent(PlayerCommandEvent event) {
        Player player = event.getPlayer();
        String command = event.getCommand();
        String[] cmd = command.split(" ");
        List<Integer> ids = plugin.Attribute.Player.get.NpcOwned(player);

        if (cmd.length >= 2) {
            if (cmd[0].toLowerCase().equals("/amt")) {
                if (cmd.length == 3) {

                    if (cmd[1].toLowerCase().equals("setname")) {
                        String name = cmd[2];
                        if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                            Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.MountNow(player));
                            if (npc != null) {
                                npc.setName(name);
                                player.sendTextMessage(Grün + "The name of this NPC is changed!");
                            }
                        } else if (plugin.Attribute.Player.get.MountNow(player) == -2) {
                            player.sendTextMessage(Rot + "Only the Owner can change the name of this NPC!");
                        } else {
                            if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                if (npc != null) {
                                    if (plugin.isPlayersNPC(player, npc)) {
                                        npc.setName(name);
                                        player.sendTextMessage(Grün + "The name of this NPC is changed!");
                                    } else {
                                        player.sendTextMessage(Rot + "Only the Owner can change the name of this NPC!");
                                    }
                                }
                            } else {
                                player.sendTextMessage(Rot + "You musst sit on an NPC or select one!");
                            }
                        }
                    }
                    if (cmd[1].toLowerCase().equals("addmember")) {
                        String pn2 = cmd[2];
                        if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                            Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.MountNow(player));
                            if (npc != null) {
                                if (plugin.server.isPlayerConnected(pn2)) {
                                    Player p2 = plugin.server.getPlayer(pn2);
                                    if (p2 != null) {
                                        plugin.Member.addMemberToNpc(npc, p2);
                                        player.sendTextMessage(Grün + "You added member " + cmd[2]);
                                    }
                                } else {
                                    player.sendTextMessage(Rot + "Player " + cmd[2] + "is not online!");
                                }
                            }
                        } else if (plugin.Attribute.Player.get.MountNow(player) == -2) {
                            player.sendTextMessage(Rot + "Only the Owner can add a member to this NPC!");
                        } else {
                            if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                if (npc != null) {
                                    if (plugin.isPlayersNPC(player, npc)) {
                                        if (plugin.server.isPlayerConnected(pn2)) {
                                            Player p2 = plugin.server.getPlayer(pn2);
                                            if (p2 != null) {
                                                plugin.Member.addMemberToNpc(npc, p2);
                                                player.sendTextMessage(Grün + "You added member " + cmd[2]);
                                            }
                                        } else {
                                            player.sendTextMessage(Rot + "Player " + cmd[2] + "is not online!");
                                        }
                                    } else {
                                        player.sendTextMessage(Rot + "Only the Owner can add a member to this NPC!");
                                    }
                                }
                            } else {
                                player.sendTextMessage(Rot + "You musst sit on an NPC or select one!");
                            }
                        }
                    }
                    if (cmd[1].toLowerCase().equals("delmember")) {
                        String pn2 = cmd[2];
                        if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                            Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.MountNow(player));
                            if (npc != null) {
                                if (plugin.server.isPlayerConnected(pn2)) {
                                    Player p2 = plugin.server.getPlayer(pn2);
                                    if (p2 != null) {
                                        plugin.Member.removeMemberFromNpc(npc, p2);
                                        player.sendTextMessage(Grün + "You removed the member " + cmd[2]);
                                    }
                                } else {
                                    player.sendTextMessage(Rot + "Player " + cmd[2] + "is not online!");
                                }
                            }
                        } else if (plugin.Attribute.Player.get.MountNow(player) == -2) {
                            player.sendTextMessage(Rot + "Only the Owner can remove a member from this NPC!");
                        } else {
                            if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                if (npc != null) {
                                    if (plugin.isPlayersNPC(player, npc)) {
                                        if (plugin.server.isPlayerConnected(pn2)) {
                                            Player p2 = plugin.server.getPlayer(pn2);
                                            if (p2 != null) {
                                                plugin.Member.removeMemberFromNpc(npc, p2);
                                                player.sendTextMessage(Grün + "You removed the member " + cmd[2]);
                                            }
                                        } else {
                                            player.sendTextMessage(Rot + "Player " + cmd[2] + "is not online!");
                                        }
                                    } else {
                                        player.sendTextMessage(Rot + "Only the Owner can remove a member from this NPC!");
                                    }
                                }
                            } else {
                                player.sendTextMessage(Rot + "You musst sit on an NPC or select one!");
                            }
                        }
                    }

                    if (cmd[1].toLowerCase().equals("ismy")) {
                        int id = Integer.parseInt(cmd[2]);
                        if (ids.contains(id)) {
                            player.sendTextMessage(Grün + "True");
                        } else {
                            player.sendTextMessage(Rot + "False");
                        }
                    }

                    if (cmd[1].toLowerCase().equals("fix")) {
                        Npc npc;
                        if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                            npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                            if (npc.getType() != Type.Mount) {
                                if (cmd[2].toLowerCase().equals("true")) {
                                    npc.setLocked(true);
                                    player.sendTextMessage(Grün + "NPC is now locked!");
                                } else {
                                    npc.setLocked(false);
                                    player.sendTextMessage(Grün + "NPC is no longer locked!");
                                }
                            }
                        }
                    }

                }
                if (cmd.length == 2) {
                    if (cmd[1].toLowerCase().equals("rotate")) {
                        if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                            Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                            if (npc != null) {
                                npc.setViewDirection(player.getViewDirection());
                            }
                        }

                    }

                    if (cmd[1].toLowerCase().equals("free")) {
                        if (player.getMount() == null) {
                            if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                if (npc != null) {
                                    if (plugin.hasNPCaPlayer(npc)) {
                                        if (plugin.isPlayersNPC(player, npc) || player.isAdmin()) {
                                            if (plugin.SpezialNPC().isSpezialNPC(npc)) {
                                                plugin.SpezialNPC().removeSpezialNPC(npc);
                                            }
                                            plugin.removeNPC(npc);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (cmd[1].toLowerCase().equals("move")) {
                        if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                            Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                            if (npc != null) {
                                npc.moveTo(player.getPosition());
                            }
                        }
                    }
                    if (cmd[1].toLowerCase().equals("getpos")) {
                        if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                            Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                            if (npc != null) {
                                player.sendTextMessage(Grün + "The NPC-Position is: " + npc.getPosition());
                            } else {

                            }
                        }
                    }

                    if (cmd[1].toLowerCase().equals("cancel")) {
                        plugin.Attribute.Player.set.amtSelect(player, -1);
                        player.sendTextMessage(Grün + "You cancel the selection!");
                    }

                    if (cmd[1].toLowerCase().equals("att")) {
                        player.sendTextMessage(Orange + "MountNow: " + plugin.Attribute.Player.get.MountNow(player));
                        player.sendTextMessage(Orange + "Selection: " + plugin.Attribute.Player.get.amtSelect(player));
                        player.sendTextMessage(Orange + "NpcOwned-List:");
                        player.sendTextMessage(Orange + "--------------------");
                        ids.forEach((id) -> {
                            player.sendTextMessage(Orange + String.valueOf(id));
                        });
                        player.sendTextMessage(Orange + "--------------------");
                    }

                    if (cmd[1].toLowerCase().equals("resttime")) {
                        Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.spOwned(player));
                        if (npc != null) {
                            if (plugin.Attribute.NPC.get.spTyp(npc) == 3) {
                                Timer timer = plugin.Attribute.NPC.get.spTimer(npc);
                                float rest = (plugin.time - timer.getTick());
                                int restNeu = (int) rest / 60;

                                player.sendTextMessage(Orange + "You have " + restNeu + " min time!");
                            }
                        } else {
                            player.sendTextMessage(Rot + "You must have a Spezial-NPC with time!");
                        }
                    }

                    if (cmd[1].toLowerCase().equals("isadmin")) {
                        if (player.isAdmin()) {
                            player.sendTextMessage(Grün + "You are Admin!");
                        } else {
                            player.sendTextMessage(Rot + "You are not Admin!");
                        }
                    }

                    if (cmd[1].toLowerCase().equals("help")) {
                        player.sendTextMessage(Orange + "AntiMountTheft-Help");
                        player.sendTextMessage(Orange + "-----------------------------");
                        player.sendTextMessage(Orange + "/amt getlist - List your NPCs");
                        player.sendTextMessage(Orange + "/amt gethere [ID] - Teleport the NPC to your position");
                        player.sendTextMessage(Orange + "/amt addmember [Player] - Add a Member");
                        player.sendTextMessage(Orange + "/amt delmember [Player] - Delete a Member");
                        player.sendTextMessage(Orange + "/amt setname [Name] - Change the Name of the NPC");
                        player.sendTextMessage(Orange + "/amt resttime  - Show the rest of time for the Spezial-NPC");
                        player.sendTextMessage(Orange + "/amt select (id)  - Select an NPC! With 'id': Select NPC with ID");
                        player.sendTextMessage(Orange + "/amt cancel  - Cancel selection");
                        player.sendTextMessage(Orange + "/amt rotate  - Set NPCs view direction to Players");
                        player.sendTextMessage(Orange + "/amt move  - The NPC move to you");
                        player.sendTextMessage(Orange + "/amt fix [false|true]  - Lock an NPC (NPC can't move)");
                        player.sendTextMessage(Orange + "/amt getpos - Lock an NPC (NPC can't move)");
                        player.sendTextMessage(Orange + "/amt free - Make NPC wild");
                        if (player.isAdmin()) {
                            player.sendTextMessage(Rot + "----------Only Admin---------");
                            player.sendTextMessage(Rot + "/amt [setspezial|setsp] [Type] - Set the option 'Spezial-NPC'");
                            player.sendTextMessage(Rot + "/amt [removespezial|rsp] - Remove the option 'Spezial-NPC'");
                            player.sendTextMessage(Rot + "/amt changepos - Change position from a spezial NPC");
                        }
                        player.sendTextMessage(Orange + "-----------------------------");
                    }

                    if (cmd[1].toLowerCase().equals("getlist")) {
                        Npc npc;
                        player.sendTextMessage(Orange + "---------Owned---------");
                        for (Integer id : ids) {
                            npc = plugin.world.getNpc(id);
                            if (npc != null) {
                                player.sendTextMessage(Orange + "(ID : " + id + ") Name: " + npc.getName());
                            }
                        }
                        player.sendTextMessage(Orange + "--------Member---------");
                        for (Integer id : plugin.Member.getMemberNPCs(player)) {
                            npc = plugin.world.getNpc(id);
                            if (npc != null) {
                                player.sendTextMessage(Orange + "(ID : " + id + ") Name: " + npc.getName());
                            }
                        }
                        player.sendTextMessage(Orange + "-----------------------");
                    }

                    if (cmd[1].toLowerCase().equals("getinfo")) {
                        Npc npc = null;
                        if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                            int id = plugin.Attribute.Player.get.MountNow(player);
                            npc = plugin.world.getNpc(id);
                        } else if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                            npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                        }
                        
                        if (npc != null) {
                            player.sendTextMessage("----NPC-Info----");
                            player.sendTextMessage("ID: " + npc.getInfoID());
                            player.sendTextMessage("Name: " + npc.getName());
                            player.sendTextMessage("Type: " + npc.getType());
                            player.sendTextMessage("Health: " + npc.getHealth());
                            player.sendTextMessage("Lock: " + npc.isLocked());
                            player.sendTextMessage("Sleeping: " + npc.isSleeping());
                        }
                    }

                    if (cmd[1].toLowerCase().equals("changepos")) {
                        if (player.isAdmin()) {
                            if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                                int id = plugin.Attribute.Player.get.MountNow(player);
                                Npc npc = plugin.world.getNpc(id);
                                if (npc != null) {
                                    if (plugin.SpezialNPC().isSpezialNPC(npc)) {
                                        if (plugin.Attribute.NPC.get.spTyp(npc) == 1 || plugin.Attribute.NPC.get.spTyp(npc) == 3) {

                                            plugin.SpezialNPC().setNpcPos(npc, player);
                                            player.sendTextMessage(Grün + "You have changed the NPC-Position!");
                                        }
                                    } else {
                                        player.sendTextMessage(Rot + "This is not a spezial NPC!");
                                    }
                                }
                            } else {
                                if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                    Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                    if (npc != null && !player.isFlying()) {
                                        npc.setPosition(player.getPosition());
                                        plugin.SpezialNPC().setNpcPos(npc, player);
                                    } else {
                                        player.sendTextMessage(Rot + "The NPC does not exist or you fly!");
                                    }

                                } else {
                                    player.sendTextMessage(Rot + "You must sit on the NPC or select one!");
                                }
                            }
                        } else {
                            if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                if (npc != null && !player.isFlying()) {
                                    npc.setPosition(player.getPosition());
                                }
                            }
                        }
                    }
                }
                if (cmd[1].toLowerCase().equals("setspezial") || cmd[1].toLowerCase().equals("setsp")) {
                    if (player.isAdmin()) {
                        if (cmd.length == 3) {
                            List<String> types = new ArrayList<>();
                            types.add("fixpos");
                            types.add("timed");
                            types.add("useall");
                            if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                                int id = plugin.Attribute.Player.get.MountNow(player);
                                Npc npc = plugin.world.getNpc(id);
                                if (npc != null) {
                                    if (types.contains(cmd[2])) {
                                        plugin.SpezialNPC().addSpezialNPC(npc, cmd[2], player);
                                        player.sendTextMessage(Orange);
                                    } else {
                                        player.sendTextMessage(Rot + "'" + cmd[2] + "' is not an AntiMountTheft-Spezial-Typ!");
                                        player.sendTextMessage(Rot + "Use: fixpos, fixpostime, useall, timed");
                                    }
                                }
                            } else {
                                if (plugin.Attribute.Player.get.amtSelect(player) >= 0 && !player.isFlying()) {
                                    Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                    if (npc != null) {
                                        if (npc.getType() == Type.Mount) {
                                            if (types.contains(cmd[2])) {
                                                plugin.SpezialNPC().addSpezialNPC(npc, cmd[2], player);
                                            } else {
                                                player.sendTextMessage(Rot + "'" + cmd[2] + "' is not an AntiMountTheft-Spezial-Typ!");
                                                player.sendTextMessage(Rot + "Use: fixpos, fixpostime, useall, timed");
                                            }
                                        } else {
                                            player.sendTextMessage(Rot + "This NPC is not mountable! Please use no 'AntiMountTheft-Spezial-Typs'! (only: /amt " + cmd[1] + ")");
                                        }
                                    }
                                } else {
                                    if (player.isFlying()) {
                                        player.sendTextMessage(Rot + "Please do not fly!");
                                    } else {
                                        player.sendTextMessage(Rot + "You must sit on the NPC or select one!");
                                    }
                                }
                            }
                        } else if (cmd.length == 2) {
                            if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                                int id = plugin.Attribute.Player.get.MountNow(player);
                                Npc npc = plugin.world.getNpc(id);
                                if (npc != null) {
                                    plugin.SpezialNPC().addSpezialNPC(npc, "other", player);
                                } else {
                                    player.sendTextMessage(Rot + "NPC dose not exist!");
                                }
                            } else {
                                if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                    int id = plugin.Attribute.Player.get.amtSelect(player);
                                    Npc npc = plugin.world.getNpc(id);
                                    if (npc != null) {
                                        plugin.SpezialNPC().addSpezialNPC(npc, "other", player);
                                    } else {
                                        player.sendTextMessage(Rot + "NPC dose not exist!");
                                    }
                                } else {
                                    player.sendTextMessage(Rot + "You must sit on the NPC or select one!");
                                }
                            }
                        }
                    } else {
                        player.sendTextMessage(Rot + "You are not an Admin!");
                    }
                }
                if ((cmd[1].toLowerCase().equals("removespezial") && cmd.length == 2) || (cmd[1].toLowerCase().equals("rsp") && cmd.length == 2)) {
                    if (player.isAdmin()) {
                        if (plugin.Attribute.Player.get.MountNow(player) >= 0) {
                            int id = plugin.Attribute.Player.get.MountNow(player);
                            Npc npc = plugin.world.getNpc(id);
                            if (npc != null) {
                                plugin.Attribute.NPC.set.spTyp(npc, -1);
                                plugin.Attribute.NPC.set.spPos(npc, null);
                                plugin.SpezialNPC().removeSpezialNPC(npc);
                                player.sendTextMessage(Grün + "You have removed the Spezial-NPC");
                            }
                        } else {
                            if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                                Npc npc = plugin.world.getNpc(plugin.Attribute.Player.get.amtSelect(player));
                                if (npc != null) {
                                    plugin.Attribute.NPC.set.spTyp(npc, -1);
                                    plugin.Attribute.NPC.set.spPos(npc, null);
                                    plugin.SpezialNPC().removeSpezialNPC(npc);
                                    player.sendTextMessage(Grün + "You have removed the Spezial-NPC");
                                }
                            } else {
                                player.sendTextMessage(Rot + "You must sit on the NPC or select one!");
                            }
                        }
                    } else {
                        player.sendTextMessage(Rot + "You are not an Admin!");
                    }
                }

                if (cmd[1].toLowerCase().equals("select")) {
                    if (cmd.length == 2) {
                        plugin.Attribute.Player.set.amtSelect(player, -2);
                        player.sendTextMessage(Orange + "Please interact with an NPC!");
                    } else if (cmd.length == 3) {
                        int id = -1;
                        boolean prüfer = false;
                        try {
                            id = Integer.parseInt(cmd[2]);
                            prüfer = true;
                        } catch (NumberFormatException ex) {
                            player.sendTextMessage(Rot + "The id must be a number!");
                        }
                        if (prüfer) {
                            Npc npc = plugin.world.getNpc(id);
                            if (npc != null) {

                                //TODO Prüfen, ob alle Attribute vorhanden sind                                
                                if (plugin.isPlayersNPC(player, npc) || player.isAdmin()) {
                                    plugin.Attribute.Player.set.amtSelect(player, npc.getGlobalID());
                                    player.sendTextMessage(Orange + "You select the NPC! ID: " + npc.getGlobalID());
                                }
                            }
                        }
                    }
                }
                if (cmd[1].toLowerCase().equals("gethere")) {
                    if (cmd.length == 3) {
                        int id = Integer.parseInt(cmd[2]);
                        Npc npc = plugin.world.getNpc(id);
                        if (plugin.isPlayersNPC(player, npc) || plugin.Member.isNpcMember(player, npc) || player.isAdmin()) {
                            npc.setPosition(player.getPosition());
                        }
                    } else if (cmd.length == 2) {
                        if (plugin.Attribute.Player.get.amtSelect(player) >= 0) {
                            int id = plugin.Attribute.Player.get.amtSelect(player);
                            Npc npc = plugin.world.getNpc(id);
                            if (plugin.isPlayersNPC(player, npc) || plugin.Member.isNpcMember(player, npc) || player.isAdmin()) {
                                npc.setPosition(player.getPosition());
                            }
                        }
                    }
                }
            }
        }
    }
}
