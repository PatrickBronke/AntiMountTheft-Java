/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pbplugins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.risingworld.api.database.Database;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;

/**
 *
 * @author Patrick Bronke
 */
public class amtMember {

    private final antimounttheft plugin;
    private final int debug;
    private final DebugerLogger dl;
    private final Database db;

    public amtMember(antimounttheft plugin, int debug) {
        this.plugin = plugin;
        this.debug = debug;
        this.dl = plugin.dl;
        this.db = plugin.db;
    }

    public List<Long> getNpcForMemberFromDB(Npc npc) {
        ArrayList<Long> ids = new ArrayList<>();
        try (ResultSet result = db.executeQuery("SELECT * FROM 'Member' WHERE NpcID=" + npc.getGlobalID() + "; ")) {
            if (result != null) {
                while (result.next()) {
                    ids.add(result.getLong("Player"));
                }
            }
        } catch (SQLException ex) {

        }
        return ids;
    }
    
    public List<Integer> getMemberNPCs(Player player) {
        ArrayList<Integer> ids = new ArrayList<>();
        try (ResultSet result = db.executeQuery("SELECT * FROM 'Member' WHERE Player=" + player.getUID() + "; ")) {
            if (result != null) {
                while (result.next()) {
                    ids.add(result.getInt("NpcID"));
                }
            }
        } catch (SQLException ex) {

        }
        return ids;
    }

    public void addMemberToNpc(Npc npc, Player player) {
        if (!plugin.isPlayersNPC(player, npc) && !isNpcMember(player, npc)) {
            Connection connection = db.getConnection();
            PreparedStatement pstmt = null;
            List<Long> AttList = plugin.Attribute.NPC.get.NpcMember(npc);
            try {
                pstmt = connection.prepareStatement("INSERT INTO Member (NpcID, Player) VALUES (?, ?)");
                pstmt.setInt(1, npc.getGlobalID());
                pstmt.setLong(2, player.getUID());
                pstmt.executeUpdate();
                AttList.add(player.getUID());
            } catch (SQLException ex) {
                plugin.dl.info("[AktiveSing SQLite-ERR]" + ex.getMessage());
                plugin.dl.info("[AktiveSing SQLite-ERR]" + ex.getErrorCode());
            }
        }
    }
    
    public void removeAllMembersFromNPC(Npc npc){
        getNpcForMemberFromDB(npc).forEach((uid) -> {
            removeMemberFromNpc(npc, uid);
        });
    }

    public void removeMemberFromNpc(Npc npc, Player player) {
        if (isNpcMember(player, npc)) {
            removeMemberFromNpc(npc, player.getUID());
        }
    }

    public void removeMemberFromNpc(Npc npc, long player) {
        Connection connection = db.getConnection();
        List<Long> AttList = plugin.Attribute.NPC.get.NpcMember(npc);
        try {
            PreparedStatement prepLok = connection.prepareStatement("DELETE FROM 'Member' WHERE NpcID=" + npc.getGlobalID() + " AND Player=" + player + "; ");
            prepLok.executeUpdate();
            AttList.remove(player);
        } catch (SQLException e) {
            System.err.println("[" + plugin.getDescription("name") + "] " + "ERR Members " + e.getMessage());
        }

    }

    public boolean isNpcMember(Player player, Npc npc) {
        if (debug > 0) {
            dl.info("isPlayersNPC: " + String.valueOf(plugin.Attribute.NPC.get.NpcMember(npc).contains(player.getUID())));
        }
        return plugin.Attribute.NPC.get.NpcMember(npc).contains(player.getUID());
    }

}
