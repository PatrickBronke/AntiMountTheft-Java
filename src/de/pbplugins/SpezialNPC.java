package de.pbplugins;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.risingworld.api.Timer;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Npc.Type;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Vector3f;

public class SpezialNPC {

    private final antimounttheft plugin;
    private List<Integer> spNPC;
    private final String Orange = "[#ffa500]", Rot = "[#ff0000]";
    private int debug;
    List<String> types = new ArrayList<>();

    public SpezialNPC(antimounttheft plugin) {
        this.plugin = plugin;
        spNPC = new ArrayList<>();
        this.debug = plugin.debug;

        types.add("fixpos");
        types.add("timed");
        types.add("useall");
        types.add("other");
    }

    public List<Integer> getList() {
        return spNPC;
    }

    public String setTypIntToString(int typ) {
        String typS = null;
        switch (typ) {
            case 1:
                typS = "fixpos";
                break;
            case 2:
                typS = "useall";
                break;
            case 3:
                typS = "timed";
                break;
            case 4:
                typS = "other";
                break;
            /*case 5:
                typS = "";
                break;
            case 6:
                typS = "";
                break;*/
            default:
                break;
        }
        return typS;
    }

    public int setTypStringToInt(String typ) {
        int typI = -1;
        switch (typ) {
            case "fixpos":
                typI = 1;
                break;
            case "useall":
                typI = 2;
                break;
            case "timed":
                typI = 3;
                break;
            case "other":
                typI = 4;
                break;
            /*case "":
                typI = 5;
                break;
            case "":
                typI = 6;
                break;*/
            default:
                break;
        }
        return typI;
    }

    public int getSpezialTypFromDB(Npc npc) {
        int typ = -99;
        try (ResultSet result = plugin.db.executeQuery("SELECT * FROM 'Spezial' WHERE NpcID=" + npc.getGlobalID() + "; ")) {
            if (result != null) {
                typ = result.getInt("Typ");
            }
        } catch (SQLException ex) {
            plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
            plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
        }
        return typ;
    }

    public Vector3f getSpezialPosFromDB(Npc npc) {
        float posX = 0;
        float posY = 0;
        float posZ = 0;
        Vector3f pos = null;
        try (ResultSet result = plugin.db.executeQuery("SELECT * FROM 'Spezial' WHERE NpcID=" + npc.getGlobalID() + "; ")) {
            if (result != null) {
                posX = result.getFloat("PosX");
                posY = result.getFloat("PosY");
                posZ = result.getFloat("PosZ");
            }
        } catch (SQLException ex) {
            plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
            plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
        }
        pos = new Vector3f(posX, posY, posZ);
        return pos;
    }

    public boolean changeTypInDB(Npc npc, int typ) {
        boolean prüfer = false;
        if (isSpezialNPC(npc)) {
            prüfer = true;
            Connection connection = plugin.db.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement("UPDATE 'Spezial' SET Typ= ? WHERE NpcID=" + npc.getGlobalID());
                pstmt.setInt(1, typ);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
            }
        }
        return prüfer;
    }

    public boolean addSpezialNPC(Npc npc, String type, Player player) {
        boolean prüfer = false;
        int typI = -1;
        String typKlein = type.toLowerCase();
        if (npc.getType() == Type.Mount && types.contains(typKlein)) {
            typI = setTypStringToInt(type);
        } else if (npc.getType() != Type.Mount) {
            typI = 4;
        }
        if (typI > 0) {
            Connection connection = plugin.db.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement("INSERT INTO 'Spezial' (NpcID, Typ) VALUES (?, ?)");
                pstmt.setInt(1, npc.getGlobalID());
                pstmt.setInt(2, typI);
                pstmt.executeUpdate();
                spNPC.add(npc.getGlobalID());
                prüfer = true;
            } catch (SQLException ex) {
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
            }
            player.sendTextMessage("[#00ff00]" + "Spezial-NPC set!");
            plugin.Attribute.Player.set.spOwned(player, npc.getGlobalID());
            plugin.Attribute.NPC.set.spTyp(npc, typI);
            if (typI == 1 || typI == 3) {
                setNpcPos(npc, player);
            }
        }
        return prüfer;
    }

    public void setNpcPos(Npc npc, Player player) {
        if (isSpezialNPC(npc)) {
            Connection connection = plugin.db.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement("UPDATE 'Spezial' SET PosX= ?, PosY= ?, PosZ= ? WHERE NpcID=" + npc.getGlobalID());
                pstmt.setFloat(1, npc.getPosition().x);
                pstmt.setFloat(2, npc.getPosition().y);
                pstmt.setFloat(3, npc.getPosition().z);
                pstmt.executeUpdate();
                player.sendTextMessage(Orange + "Position of this NPC set!");
                plugin.Attribute.NPC.set.spPos(npc, npc.getPosition());
                if (debug >= 1) {
                    plugin.dl.info("spPos = " + plugin.Attribute.NPC.get.spPos(npc));
                }
            } catch (SQLException ex) {
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
            }
        } else {
            player.sendTextMessage(Rot + "NPC is not a Spezial-NPC!");
        }
    }

    public void removeSpezialNPC(Npc npc) {
        if (isSpezialNPC(npc)) {
            Connection connection = plugin.db.getConnection();
            try {
                PreparedStatement prepLok = connection.prepareStatement("DELETE FROM 'Spezial' WHERE NpcID=" + npc.getGlobalID() + "; ");
                prepLok.executeUpdate();
                spNPC.remove(npc.getGlobalID());
                plugin.Attribute.NPC.set.spPos(npc, null);
                plugin.Attribute.NPC.set.spTimer(npc, null);
                plugin.Attribute.NPC.set.spTyp(npc, 0);
            } catch (SQLException ex) {
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
                plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
            }
        }
    }

    public void setAllSpezialNPC() {
        int id;
        try (ResultSet result = plugin.db.executeQuery("SELECT * FROM 'Spezial'")) {
            if (result != null) {
                while (result.next()) {
                    id = result.getInt("NpcID");
                    spNPC.add(id);
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] Add SpezialNPC: " + id);
                    }
                }
            }

        } catch (SQLException ex) {
            plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
            plugin.dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
        }
        setAllAttribute();
    }

    public boolean isSpezialNPC(Npc npc) {

        return spNPC.contains(npc.getGlobalID());
    }

    public void setAllAttribute() {
        Npc npc;
        for (int id : spNPC) {
            npc = plugin.world.getNpc(id);
            if (npc != null) {
                plugin.Attribute.NPC.set.spTyp(npc, getSpezialTypFromDB(npc));
                plugin.Attribute.NPC.set.spHasPlayer(npc, false);
                plugin.Attribute.NPC.set.spTimer(npc, null);
                npc.setInvincible(true);
                if (plugin.Attribute.NPC.get.spTyp(npc) == 1) {
                    plugin.Attribute.NPC.set.spPos(npc, getSpezialPosFromDB(npc));
                    if (debug > 0) {
                        plugin.dl.info("[AntiMountTheft] SpezialNPC (ID: " + id + " set Fixpos: " + plugin.Attribute.NPC.get.spPos(npc));
                    }
                }
            }
        }
    }

    public Timer getNewSpezialTimer(Npc npc, Player player) {
        Timer spTimer = new Timer(plugin.time, 0f, 1, () -> {
            Vector3f pos = plugin.Attribute.NPC.get.spPos(npc);
            npc.setPosition(pos);
            player.sendTextMessage(Rot + "NPC-Time is up!");
            plugin.Attribute.NPC.set.spHasPlayer(npc, false);
            killTimer(npc);
        });

        return spTimer;
    }

    private void killTimer(Npc npc) {
        plugin.Attribute.NPC.get.spTimer(npc).kill();
    }

}
