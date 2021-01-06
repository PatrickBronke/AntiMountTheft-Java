package de.pbplugins;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.Timer;
import net.risingworld.api.World;
import net.risingworld.api.database.Database;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;

public class antimounttheft extends Plugin {

    Database db;
    World world;

    private Config config;
    private SpezialNPC spNPC;
    float time;

    int debug;
    DebugerLogger dl;
    Server server;
    amtMember Member;
    amtAttribute Attribute;

    @Override
    public void onEnable() {

        world = getWorld();

        Attribute = new amtAttribute(this);
        spNPC = new SpezialNPC(this);

        registerEventListener(new PlayerEventCommand(this));
        registerEventListener(new PlayerEvent(this));
        registerEventListener(new EventListener(this));

        System.out.println("[" + getDescription("name") + "] Enabled");
        System.out.println("[" + getDescription("name") + "] Load Database...");
        db = getSQLiteConnection(getPath() + "/database/" + getDescription("name") + "-" + world.getName() + ".db");
        iniDB();
        System.out.println("[AntiMountTheft] Load config...");
        String[][] sysConfigArray = {
            //Name         Wert
            {"Debug", "0"},
            {"DebugLevel", "ALL"},
            {"HealthEffect", "25"},
            {"Spezial_Timer(sek)", "3600"}
        };
        config = new Config("System", sysConfigArray, this, 0);
        try {
            debug = Integer.parseInt(config.getValue("Debug"));
        } catch (NumberFormatException e1) {
            debug = 0;
        }

        try {
            time = Float.parseFloat(config.getValue("Spezial_Timer(sek)"));
        } catch (NumberFormatException e1) {
            time = 3600f;
        }

        server = getServer();
        dl = new DebugerLogger(this);
        Member = new amtMember(this, debug);
        if (debug > 0) {
            try {
                dl.createLog("log");
            } catch (IOException ex) {
            }
        }
        spNPC.setAllSpezialNPC();
        //MountTimer();
    }

    public Timer MountTimer() {
        Timer testTimer = new Timer(0.01f, 0f, -1, () -> {
            Collection<Npc> npcs = getWorld().getAllNpcs(null);
            npcs.stream().filter((npc) -> (hasNPCaPlayer(npc))).forEachOrdered((npc) -> {
                npc.setAlerted(false);
            });
        });
        testTimer.start();
        System.out.println("Timer start!");
        return testTimer;
    }

    public SpezialNPC SpezialNPC() {
        return spNPC;
    }

    public int HealthEffect() {
        int effect = 0;
        try {
            effect = Integer.parseInt(Config().getValue("HealthEffect"));
        } catch (NumberFormatException ex) {
            effect = 25;
            System.out.println("[AntiMounthTheft-Config-ERR] 'HealthEffect' must be a number!");
            server.getAllPlayers().stream().filter((player) -> (player.isAdmin())).forEachOrdered((player) -> {
                if (player != null) {
                    player.sendTextMessage("[#ff0000][AntiMounthTheft-Config-ERR] 'HealthEffect' must be a number!");
                }
            });
        }
        return effect;
    }

    public Config Config() {
        return config;
    }

    @Override
    public void onDisable() {
        System.out.println("[" + getDescription("name") + "] Disabled");
    }

    public boolean isPlayersNPC(Player player, Npc npc) {
        if (debug > 0) {
            dl.info("isPlayersNPC: " + String.valueOf(Attribute.Player.get.NpcOwned(player).contains(npc.getGlobalID())));
        }
        return Attribute.Player.get.NpcOwned(player).contains(npc.getGlobalID());
    }

    public boolean hasNPCaPlayer(Npc npc) {
        boolean prüfer = false;
        long uid = -1;
        try (ResultSet result = db.executeQuery("SELECT * FROM 'AMT' WHERE NpcID=" + npc.getGlobalID() + "; ")) {
            if (result != null) {
                uid = result.getLong("Player");
                if (debug > 0) {
                    dl.info("hasNPCaPlayer: result = -1");
                }

            }
        } catch (SQLException ex) {

        }
        if (uid > -1) {
            if (debug > 0) {
                dl.info("hasNPCaPlayer: uid > -1");
            }
            prüfer = true;
        }
        if (debug > 0) {
            dl.info("hasNPCaPlayer: " + String.valueOf(prüfer));
        }
        return prüfer;
    }

    public List<Long> getNpcOwners(Npc npc) {
        List<Long> ids = new ArrayList<>();
        try (ResultSet result = db.executeQuery("SELECT * FROM 'AMT' WHERE NpcID=" + npc.getGlobalID() + "; ")) {
            if (result != null) {
                while (result.next()) {
                    ids.add(result.getLong("Player"));
                    if (debug > 0) {
                        dl.info("hasNPCaPlayer: result = -1");
                    }
                }
            }
        } catch (SQLException ex) {

        }
        return ids;
    }

    public boolean addOwnerToNPC(Npc npc, Player player) {
        boolean prüfer = false;
        List<Integer> AttList = Attribute.Player.get.NpcOwned(player);
        if (debug > 0) {
            dl.info("[AntiMountTheft]onPlayerMountNpcEvent: Neuer NPC wird eingetragen");
        }
        Connection connection = db.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO AMT (NpcID, Player) VALUES (?, ?)");
            pstmt.setInt(1, npc.getGlobalID());
            pstmt.setLong(2, player.getUID());
            pstmt.executeUpdate();
            AttList.add(npc.getGlobalID());
            prüfer = true;
        } catch (SQLException ex) {
            dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
            dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
        }
        return prüfer;
    }

    public void removeNPC(Npc npc) {

        Connection connection = db.getConnection();
        try {
            PreparedStatement prepLok = connection.prepareStatement("DELETE FROM 'AMT' WHERE NpcID=" + npc.getGlobalID() + "; ");
            prepLok.executeUpdate();
        } catch (SQLException ex) {
            dl.info("[AntiMountTheft SQLite-ERR]" + ex.getMessage());
            dl.info("[AntiMountTheft SQLite-ERR]" + ex.getErrorCode());
        }

        List<Long> members = Attribute.NPC.get.NpcMember(npc);
        List<Long> owners = getNpcOwners(npc);
        Player player2;
        if (members != null) {
            for (long m : members) {
                player2 = server.getPlayer(m);
                if (player2 != null) {
                    Member.removeMemberFromNpc(npc, player2);
                    if (player2.isConnected()) {
                        player2.sendTextMessage("[#ffa500]" + "NPC " + npc.getName() + " has been released");
                    }
                } else {
                    Member.removeMemberFromNpc(npc, m);
                }
            }
        }
        if (owners != null) {
            for (long o : owners) {
                player2 = server.getPlayer(o);
                if (player2 != null) {
                    Attribute.Player.get.NpcOwned(player2).remove(npc.getGlobalID());
                    if (player2.isConnected()) {
                        player2.sendTextMessage("[#ffa500]" + "NPC " + npc.getName() + " has been released");
                    }
                }
            }
        }
        Attribute.NPC.get.NpcMember(npc).clear();
        Attribute.NPC.set.spHasPlayer(npc, false);
        npc.setLocked(false);
        npc.setName("");
    }

    public List<Integer> getNpcIDfromDB(Player player) {
        ArrayList<Integer> ids = new ArrayList<>();
        try (ResultSet result = db.executeQuery("SELECT * FROM 'AMT' WHERE Player=" + player.getUID() + "; ")) {
            if (result != null) {
                while (result.next()) {
                    ids.add(result.getInt("NpcID"));
                    if (debug > 0) {
                        dl.info("getNpcIDfromDB: add Player '" + player.getName() + "' NPC ID: " + result.getInt("NpcID"));
                    }
                }
            }
        } catch (SQLException ex) {

        }
        return ids;
    }

    private void iniDB() {
        db.execute("CREATE TABLE IF NOT EXISTS AMT ("
                + "ID INTEGER PRIMARY KEY NOT NULL, " //AUTOINCREMENT
                + "NpcID INTEGER, "
                + "Player BIGINT "
                + "); ");
        db.execute("CREATE TABLE IF NOT EXISTS Member ("
                + "ID INTEGER PRIMARY KEY NOT NULL, " //AUTOINCREMENT
                + "NpcID INTEGER, "
                + "Player BIGINT "
                + "); ");
        db.execute("CREATE TABLE IF NOT EXISTS Spezial ("
                + "ID INTEGER PRIMARY KEY NOT NULL, " //AUTOINCREMENT
                + "NpcID INTEGER, "
                + "Typ INTEGER, "
                + "PosX FLOAT, "
                + "PosY FLOAT, "
                + "PosZ FLOAT "
                + "); ");
    }

}
