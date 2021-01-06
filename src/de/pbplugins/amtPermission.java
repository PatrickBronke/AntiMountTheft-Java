/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.pbplugins;

public class amtPermission {

    private antimounttheft plugin;
    private Config sysPerm;
    private int debug;

    public String Signs_Build_Free,
            Signs_Build_Heal,
            Signs_Build_Time,
            Signs_Build_Weather,
            Signs_Build_Warp,
            Signs_Build_SpawnNPC,
            Signs_Build_Gamemode,
            Signs_Build_Buy,
            Signs_Build_BuyPlot_Admin,
            Signs_Build_Sell,
            Signs_Build_SellAll,
            Signs_Build_Fly,
            Signs_Build_GetSize,
            Signs_Build_Ticket,
            Signs_Build_Transmitter,
            Signs_Build_Bank,
            Signs_Build_setGroup,
            Signs_Build_TeamSpeak,
            Signs_Build_Wetness,
            Signs_Build_GlobalChest_Buy,
            Signs_Build_GlobalChest_Expand,
            Signs_Build_Home_Buy,
            Signs_Build_Balance,
            Signs_Build_Spawn,
            Signs_Admin_EditSigns,
            Signs_Admin_Protection,
            Signs_Admin_AutoSave,
            Chest_Admin_SlotChange,
            Chest_Admin_Spezial_Create,
            Chest_Admin_Spezial_Open,
            Chest_Admin_Spezial_Remove,
            Chest_Admin_AddMember,
            Chest_Admin_RemoveMember,
            Home_Admin_NoMax,
            Teleport_Admin,
            Command_Admin_SetWarp,
            Command_Admin_DelWarp,
            Command_Admin_Warps,
            Command_Admin_Warp,
            Command_Admin_ProveAllSign,
            Command_Admin_Getatt,
            Command_Admin_EditWarp,
            Command_Admin_Telport,
            Command_Admin_GlobalChest,
            Command_Admin_Home,
            Signs_Build_AdminHelp_Text,
            Home_Price,
            Fly_Admin,
            Size_Admin,
            General_Support,
            ChatMailSystem_Admin;

    public amtPermission(antimounttheft plugin) {
        this.plugin = plugin;
        this.debug = plugin.debug;
    }

    public void setPermissionData() {
        System.out.println("[AktiveSign] Load permissions...");
        String[][] sysPermissionArray = {
            //Name         Wert
            {"Signs_Build_Free", ""},
            {"Signs_Build_Heal", ""},
            {"Signs_Build_Time", ""},
            {"Signs_Build_Weather", ""},
            {"Signs_Build_Warp", ""},
            {"Signs_Build_SpawnNPC", ""},
            {"Signs_Build_Gamemode", ""},
            {"Signs_Build_Buy", ""},
            {"Signs_Build_BuyPlot_Admin", ""},
            {"Signs_Build_Sell", ""},
            {"Signs_Build_SellAll", ""},
            {"Signs_Build_Fly", ""},
            {"Signs_Build_GetSize", ""},
            {"Signs_Build_Ticket", ""},
            {"Signs_Build_Transmitter", ""},
            {"Signs_Build_Bank", ""},
            {"Signs_Build_setGroup", ""},
            {"Signs_Build_TeamSpeak", ""},
            {"Signs_Build_Wetness", ""},
            {"Signs_Build_GlobalChest_Buy", ""},
            {"Signs_Build_GlobalChest_Expand", ""},
            {"Signs_Build_Home_Buy", ""},
            {"Signs_Build_Balance", ""},
            {"Signs_Admin_EditSigns", ""},
            {"Signs_Admin_Protection", ""},
            {"Signs_Admin_AutoSave", ""},
            {"Chest_Admin_SlotChange", ""},
            {"Chest_Admin_Spezial_Create", ""},
            {"Chest_Admin_Spezial_Open", ""},
            {"Chest_Admin_AddMember", ""},
            {"Chest_Admin_RemoveMember", ""},
            {"Home_Admin_NoMax", ""},
            {"Command_Admin_SetWarp", ""},
            {"Command_Admin_DelWarp", ""},
            {"Command_Admin_Warps", ""},
            {"Command_Admin_Warp", ""},
            {"Command_Admin_ProveAllSign", ""},
            {"Command_Admin_Getatt", ""},
            {"Command_Admin_EditWarp", ""},
            {"General_Support", ""},
            {"Chest_Admin_Spezial_Remove", ""},
            {"Signs_Build_Spawn", ""},
            {"Signs_Build_AdminHelp_Text", ""},
            {"Home_Price", ""},
            {"Fly_Admin", ""},
            {"Size_Admin", ""},
            {"Command_Admin_", ""},
            {"Command_Admin_", ""},
            {"ChatMailSystem_Admin", ""}
        //{"UseSign_Teleport", "true"}
        };

        sysPerm = new Config("Permission", sysPermissionArray, plugin, debug);
    }

    public void setValue() {
        Signs_Build_Free = sysPerm.getValue("Signs_Build_Free");
        Signs_Build_Heal = sysPerm.getValue("Signs_Build_Heal");
        Signs_Build_Spawn = sysPerm.getValue("Signs_Build_Spawn");
        Signs_Build_AdminHelp_Text = sysPerm.getValue("Signs_Build_AdminHelp_Text");
        Signs_Build_Time = sysPerm.getValue("Signs_Build_Time");
        Signs_Build_Weather = sysPerm.getValue("Signs_Build_Weather");
        Signs_Build_Warp = sysPerm.getValue("Signs_Build_Warp");
        Signs_Build_SpawnNPC = sysPerm.getValue("Signs_Build_SpawnNPC");
        Signs_Build_Gamemode = sysPerm.getValue("Signs_Build_Gamemode");
        Signs_Build_Buy = sysPerm.getValue("Signs_Build_Buy");
        Signs_Build_BuyPlot_Admin = sysPerm.getValue("Signs_Build_BuyPlot_Admin");
        Signs_Build_Sell = sysPerm.getValue("Signs_Build_Sell");
        Signs_Build_SellAll = sysPerm.getValue("Signs_Build_SellAll");
        Signs_Build_Fly = sysPerm.getValue("Signs_Build_Fly");
        Signs_Build_GetSize = sysPerm.getValue("Signs_Build_GetSize");
        Signs_Build_Ticket = sysPerm.getValue("Signs_Build_Ticket");
        Signs_Build_Transmitter = sysPerm.getValue("Signs_Build_Transmitter");
        Signs_Build_Bank = sysPerm.getValue("Signs_Build_Bank");
        Signs_Build_setGroup = sysPerm.getValue("Signs_Build_setGroup");
        Signs_Build_TeamSpeak = sysPerm.getValue("Signs_Build_TeamSpeak");
        Signs_Build_Wetness = sysPerm.getValue("Signs_Build_Wetness");
        Signs_Build_GlobalChest_Buy = sysPerm.getValue("Signs_Build_GlobalChest_Buy");
        Signs_Build_GlobalChest_Expand = sysPerm.getValue("Signs_Build_GlobalChest_Expand");
        Signs_Build_Home_Buy = sysPerm.getValue("Signs_Build_Home_Buy");
        Signs_Build_Balance = sysPerm.getValue("Signs_Build_Balance");
        Signs_Admin_EditSigns = sysPerm.getValue("Signs_Admin_EditSigns");
        Signs_Admin_Protection = sysPerm.getValue("Signs_Admin_Protection");
        Signs_Admin_AutoSave = sysPerm.getValue("Signs_Admin_AutoSave");
        Chest_Admin_SlotChange = sysPerm.getValue("Chest_Admin_SlotChange");
        Chest_Admin_Spezial_Create = sysPerm.getValue("Chest_Admin_Spezial_Create");
        Chest_Admin_Spezial_Open = sysPerm.getValue("Chest_Admin_Spezial_Open");
        Chest_Admin_Spezial_Remove = sysPerm.getValue("Chest_Admin_Spezial_Remove");
        Chest_Admin_AddMember = sysPerm.getValue("Chest_Admin_AddMember");
        Chest_Admin_RemoveMember = sysPerm.getValue("Chest_Admin_RemoveMember");
        Home_Admin_NoMax = sysPerm.getValue("Home_Admin_NoMax");
        Home_Price = sysPerm.getValue("Home_Price");
        Teleport_Admin = sysPerm.getValue("Teleport_Admin");
        ChatMailSystem_Admin = sysPerm.getValue("ChatMailSystem_Admin");
        
        Command_Admin_SetWarp = sysPerm.getValue("Command_Admin_SetWarp");
        Command_Admin_DelWarp = sysPerm.getValue("Command_Admin_DelWarp");
        Command_Admin_EditWarp = sysPerm.getValue("Command_Admin_EditWarp");
        Command_Admin_Warps = sysPerm.getValue("Command_Admin_Warps");
        Command_Admin_Warp = sysPerm.getValue("Command_Admin_Warp");
        Command_Admin_ProveAllSign = sysPerm.getValue("Command_Admin_ProveAllSign");
        Command_Admin_Getatt = sysPerm.getValue("Command_Admin_Getatt");
        
        Fly_Admin = sysPerm.getValue("Fly_Admin");
        Size_Admin = sysPerm.getValue("Size_Admin");
        
        General_Support = sysPerm.getValue("General_Support");
    }

}
