package de.pbplugins;

import java.util.List;
import net.risingworld.api.Timer;
import net.risingworld.api.objects.Npc;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Vector3f;

public class amtAttribute {

    private final antimounttheft plugin;
    public attPlayer Player;
    public attNpc NPC;

    public amtAttribute(antimounttheft plugin) {
        this.plugin = plugin;
        this.Player = new attPlayer();
        this.NPC = new attNpc();

    }

    public class attNpc {

        public final String spTyp; //Int
        public final String spPos; // Vector3f
        public final String spTimer; //Timer
        public final String spHasPlayer; //Boolean
        public final String NpcMember; //List<UID>
        public get get;
        public set set;

        public attNpc() {
            NpcMember = plugin.getDescription("name") + "-" + "NPC" + "-" + "NpcMember";
            spTyp = plugin.getDescription("name") + "-" + "NPC" + "-" + "spTyp";
            spPos = plugin.getDescription("name") + "-" + "NPC" + "-" + "spPos";
            
            spTimer = plugin.getDescription("name") + "-" + "NPC" + "-" + "spTimer";
            spHasPlayer = plugin.getDescription("name") + "-" + "NPC" + "-" + "spHasPlayer";
            
            get = new get();
            set = new set();
        }

        public class get {

            public int spTyp(Npc npc) {
                return (int) npc.getAttribute(spTyp);
            }

            public Vector3f spPos(Npc npc) {
                return (Vector3f) npc.getAttribute(spPos);
            }

            public boolean spHasPlayer(Npc npc) {
                return (boolean) npc.getAttribute(spHasPlayer);
            }

            public List<Long> NpcMember(Npc npc) {
                return (List<Long>) npc.getAttribute(NpcMember);
            }

            public Timer spTimer(Npc npc) {
                return (Timer) npc.getAttribute(spTimer);
            }

        }

        public class set {
            
            public void spTyp(Npc npc, int typ){
                npc.setAttribute(spTyp, typ);
            }
            
            public void spPos(Npc npc, Vector3f pos) {
                npc.setAttribute(spPos, pos);
            }

            public void spHasPlayer(Npc npc, boolean player) {
                npc.setAttribute(spHasPlayer, player);
            }

            public void spTimer(Npc npc, Timer time) {
                npc.setAttribute(spTimer, time);
            }

        }
    }

    public class attPlayer {

        public final String NpcOwned; // List<Int>
        private final String MountNow; //Int
        private final String spOwned; // Int
        private final String amtSelect; //int

        public get get;
        public set set;

        public attPlayer() {
            NpcOwned = plugin.getDescription("name") + "-" + "Player" + "-" + "NpcOwned";
            MountNow = plugin.getDescription("name") + "-" + "Player" + "-" + "MountNow";
            spOwned = plugin.getDescription("name") + "-" + "Player" + "-" + "spOwned";
            amtSelect = plugin.getDescription("name") + "-" + "Player" + "-" + "amtSelect";
            
            get = new get();
            set = new set();
        }

        public class get {
            
            public List<Integer> NpcOwned(Player player){
                return (List<Integer>)player.getAttribute(NpcOwned);
            }
            
            public int MountNow(Player player){
                return (int)player.getAttribute(MountNow);
            }
            
            public int spOwned(Player player){
                return (int)player.getAttribute(spOwned);
            }
            
            public int amtSelect(Player player){
                return (int)player.getAttribute(amtSelect);
            }

        }

        public class set {
            
            public void MountNow(Player player, int mount){
                player.setAttribute(MountNow, mount);
            }
            
            public void spOwned(Player player, int npc){
                player.setAttribute(spOwned, npc);
            }
            
            public void amtSelect(Player player, int value){
                player.setAttribute(amtSelect, value);
            }

        }
    }

}
