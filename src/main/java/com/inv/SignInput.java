package com.inv;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.inv.Inventories.Potions.PotionsEvents;
import com.inv.Utilities.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SignInput {

    private static final int ACTION_INDEX = 9;
    private static final String NBT_FORMAT = "{\"text\":\"%s\"}";
    private static final String NBT_BLOCK_ID = "minecraft:sign";

    private static HashMap<Player, Inventory> pinv = new HashMap<Player, Inventory>();
    private static HashMap<Player, BlockPosition> pos = new HashMap<Player, BlockPosition>();

    public SignInput(Player p, Inventory inv, String msg) {
        PotionsEvents.addWait(p.getName());
        p.closeInventory();
        pinv.put(p, inv);
        open(p, msg);
    }

    public void open(Player player, String msg) {
        PotionsEvents.addWait(player.getName());
        if (!player.isOnline()) {
            return;
        }
        Location location = player.getLocation();
        BlockPosition position;
        position = new BlockPosition(location.getBlockX(), location.getBlockY() + (255 - location.getBlockY()), location.getBlockZ());
        pos.put(player, position);

        player.sendBlockChange(position.toLocation(location.getWorld()), Material.OAK_SIGN.createBlockData());

        PacketContainer openSign = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        PacketContainer signData = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TILE_ENTITY_DATA);

        openSign.getBlockPositionModifier().write(0, position);

        NbtCompound signNBT = (NbtCompound) signData.getNbtModifier().read(0);
        signNBT.put("Text" + 1, String.format(NBT_FORMAT, msg));

        signNBT.put("x", position.getX());
        signNBT.put("y", position.getY());
        signNBT.put("z", position.getZ());
        signNBT.put("id", NBT_BLOCK_ID);

        signData.getBlockPositionModifier().write(0, position);
        signData.getIntegers().write(0, ACTION_INDEX);
        signData.getNbtModifier().write(0, signNBT);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, signData);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSign);
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public static void listen() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(CustomFoodPlugin.instance, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                final Player p = event.getPlayer();
                event.setCancelled(true);
                String total = "";
                for (String str : event.getPacket().getStringArrays().read(0)) {
                    total += str;
                }
                final String finalTotal = total;
                Bukkit.getScheduler().runTaskLater(CustomFoodPlugin.instance, new Runnable() {
                    public void run() {
                        if (pinv.containsKey(p)) {
                            Inventory inv = pinv.get(p);
                            ItemStack item = inv.getItem(13);
                            boolean head = false;
                            if (item != null && item.getType() != Material.AIR) {
                                ItemMeta mitem = item.getItemMeta();
                                if (finalTotal.startsWith("Enter Item's name:")) {
                                    mitem.setDisplayName(Config.replaceText(finalTotal.replaceFirst("Enter Item's name:", ""), new Pair<>("&", "§")));
                                } else if (finalTotal.startsWith("Enter a head name:")) {
                                    if (item.getType() == Material.PLAYER_HEAD) {
                                        String oName = finalTotal.replaceFirst("Enter a head name:", "");
                                        if (oName.length() <= 16 && oName.length() >= 3) {
                                            SkullMeta hitem = (SkullMeta) item.getItemMeta();
                                            hitem.setOwner(oName);
                                            item.setItemMeta(hitem);
                                        } else {
                                            Config.sendMessage(p, Config.getInvalidHeadNameMessage());
                                        }
                                        head = true;
                                    }
                                } else if (finalTotal.startsWith("Enter item's lore:")) {
                                    ArrayList<String> arr = new ArrayList<>(Arrays.asList(finalTotal.replaceFirst("Enter item's lore:", "").split("%n")));
                                    ArrayList<String> reparr = new ArrayList<>();
                                    for (String st : arr) {
                                        reparr. add(st.replaceAll("&", "§"));
                                    }
                                    mitem.setLore(reparr);
                                }
                                if (!head)
                                    item.setItemMeta(mitem);
                                inv.setItem(13, item);
                            }
                            p.openInventory(inv);
                        }
                        pinv.remove(p);
                    }
                }, 1L);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (p.isOnline()) {
                        p.sendBlockChange(pos.get(p).toLocation(p.getWorld()), pos.get(p).toLocation(p.getWorld()).getBlock().getBlockData());
                        pos.remove(p);
                    }
                }, 2L);
            }
        });
    }
}
