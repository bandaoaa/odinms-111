package server;

import java.util.LinkedList;
import java.util.List;

import client.inventory.Item;
import client.inventory.ItemFlag;
import constants.GameConstants;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import client.commands.CommandProcessor;
import constants.ServerConstants.CommandType;
import handling.world.World;

import java.lang.ref.WeakReference;

import tools.packet.EtcPacket;
import tools.packet.EtcPacket.InteractionPacket;
import tools.packet.MaplePacketCreator;
import tools.packet.PlayerShopPacket;

public class MapleTrade {

    private MapleTrade partner = null;
    private final List<Item> items = new LinkedList<Item>();
    private List<Item> exchangeItems;
    private int meso = 0, exchangeMeso = 0;
    private boolean locked = false, inTrade = false;
    private final WeakReference<MapleCharacter> chr;
    private final byte tradingslot;

    public MapleTrade(final byte tradingslot, final MapleCharacter chr) {
        this.tradingslot = tradingslot;
        this.chr = new WeakReference<MapleCharacter>(chr);
    }

    public final void CompleteTrade() {
        if (exchangeItems != null) { // just to be on the safe side...
            List<Item> itemz = new LinkedList<Item>(exchangeItems);
            for (final Item item : itemz) {
                short flag = item.getFlag();

                if (ItemFlag.KARMA_EQ.check(flag)) {
                    item.setFlag((short) (flag - ItemFlag.KARMA_EQ.getValue()));
                } else if (ItemFlag.KARMA_USE.check(flag)) {
                    item.setFlag((short) (flag - ItemFlag.KARMA_USE.getValue()));
                }
                MapleInventoryManipulator.addFromDrop(chr.get().getClient(), item, false);
            }
            exchangeItems.clear();
        }
        if (exchangeMeso > 0) {
            chr.get().gainMeso(exchangeMeso - GameConstants.getTaxAmount(exchangeMeso), false, false);
        }
        exchangeMeso = 0;


        chr.get().getClient().sendPacket(InteractionPacket.TradeMessage(tradingslot, (byte) 0x07));
    }

    public final void cancel(final MapleClient c, final MapleCharacter chr) {
        cancel(c, chr, 0);
    }

    public final void cancel(final MapleClient c, final MapleCharacter chr, final int unsuccessful) {
        if (items != null) { // just to be on the safe side...
            List<Item> itemz = new LinkedList<Item>(items);
            for (final Item item : itemz) {
                MapleInventoryManipulator.addFromDrop(c, item, false);
            }
            items.clear();
        }
        if (meso > 0) {
            chr.gainMeso(meso, false, false);
        }
        meso = 0;


        c.sendPacket(InteractionPacket.getTradeCancel(tradingslot, unsuccessful));
    }

    public final boolean isLocked() {
        return locked;
    }

    public final void setMeso(final int meso) {
        if (locked || partner == null || meso <= 0 || this.meso + meso <= 0) {
            return;
        }
        if (chr.get().getMeso() >= meso) {
            chr.get().gainMeso(-meso, false, false);
            this.meso += meso;
            chr.get().getClient().sendPacket(InteractionPacket.getTradeMesoSet((byte) 0, this.meso));
            if (partner != null) {//InteractionPacket
                partner.getChr().getClient().sendPacket(InteractionPacket.getTradeMesoSet((byte) 1, this.meso));
            }
        }
        //chr.get().getClient().sendPacket(MaplePacketCreator.enableActions());
    }

    public final void addItem(final Item item) {
        if (locked || partner == null) {
            return;
        }
        items.add(item);
        chr.get().getClient().sendPacket(InteractionPacket.getTradeItemAdd((byte) 0, item));
        if (partner != null) {
            partner.getChr().getClient().sendPacket(InteractionPacket.getTradeItemAdd((byte) 1, item));
        }
    }

    public final void chat(final String message) {
        if (!CommandProcessor.processCommand(chr.get().getClient(), message, CommandType.TRADE)) {
            chr.get().dropMessage(-2, chr.get().getName() + " : " + message);
            if (partner != null) {
                partner.getChr().getClient().sendPacket(PlayerShopPacket.shopChat(chr.get().getName() + " : " + message, 1));
            }
        }
        if (chr.get().getClient().isMonitored()) { //Broadcast info even if it was a command.
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, chr.get().getName() + " said in trade with " + partner.getChr().getName() + ": " + message));
        } else if (partner != null && partner.getChr() != null && partner.getChr().getClient().isMonitored()) {
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, chr.get().getName() + " said in trade with " + partner.getChr().getName() + ": " + message));
        }
    }


    public final void chatAuto(final String message) {
        chr.get().dropMessage(-2, message);
        if (partner != null) {
            partner.getChr().getClient().sendPacket(PlayerShopPacket.shopChat(message, 1));
        }
        if (chr.get().getClient().isMonitored()) { //Broadcast info even if it was a command.
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, chr.get().getName() + " said in trade [Automated] with " + partner.getChr().getName() + ": " + message));
        } else if (partner != null && partner.getChr() != null && partner.getChr().getClient().isMonitored()) {
            World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, chr.get().getName() + " said in trade [Automated] with " + partner.getChr().getName() + ": " + message));
        }
    }


    public final MapleTrade getPartner() {
        return partner;
    }

    public final void setPartner(final MapleTrade partner) {
        if (locked) {
            return;
        }
        this.partner = partner;
    }

    public final MapleCharacter getChr() {
        return chr.get();
    }

    public final int getNextTargetSlot() {
        if (items.size() >= 9) {
            return -1;
        }
        int ret = 1; //first slot
        for (Item item : items) {
            if (item.getPosition() == ret) {
                ret++;
            }
        }
        return ret;
    }

    public boolean inTrade() {
        return inTrade;
    }

    public final boolean setItems(final MapleClient c, final Item item, byte targetSlot, final int quantity) {
        int target = getNextTargetSlot();
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (partner == null || target == -1 || GameConstants.isPet(item.getItemId()) || isLocked() || (GameConstants.getInventoryType(item.getItemId()) == MapleInventoryType.EQUIP && quantity != 1)) {
            return false;
        }
        final short flag = item.getFlag();
        if (ItemFlag.UNTRADEABLE.check(flag) || ItemFlag.LOCK.check(flag)) {
            c.sendPacket(MaplePacketCreator.enableActions());
            return false;
        }
        if (ii.isDropRestricted(item.getItemId()) || ii.isAccountShared(item.getItemId())) {
            if (!(ItemFlag.KARMA_EQ.check(flag) || ItemFlag.KARMA_USE.check(flag))) {
                c.sendPacket(MaplePacketCreator.enableActions());
                return false;
            }
        }
        Item tradeItem = item.copy();
        if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
            tradeItem.setQuantity(item.getQuantity());
            MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(item.getItemId()), item.getPosition(), item.getQuantity(), true);
        } else {
            tradeItem.setQuantity((short) quantity);
            MapleInventoryManipulator.removeFromSlot(c, GameConstants.getInventoryType(item.getItemId()), item.getPosition(), (short) quantity, true);
        }
        if (targetSlot < 0) {
            targetSlot = (byte) target;
        } else {
            for (Item itemz : items) {
                if (itemz.getPosition() == targetSlot) {
                    targetSlot = (byte) target;
                    break;
                }
            }
        }
        tradeItem.setPosition(targetSlot);
        addItem(tradeItem);
        return true;
    }

    private final int check() { //0 = fine, 1 = invent space not, 2 = pickupRestricted
        if (chr.get().getMeso() + exchangeMeso < 0) {
            return 1;
        }

        if (exchangeItems != null) {
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            byte eq = 0, use = 0, setup = 0, etc = 0, cash = 0;
            for (final Item item : exchangeItems) {
                switch (GameConstants.getInventoryType(item.getItemId())) {
                    case EQUIP:
                        eq++;
                        break;
                    case USE:
                        use++;
                        break;
                    case SETUP:
                        setup++;
                        break;
                    case ETC:
                        etc++;
                        break;
                    case CASH: // Not allowed, probably hacking
                        cash++;
                        break;
                }
                if (ii.isPickupRestricted(item.getItemId()) && chr.get().haveItem(item.getItemId(), 1, true, true)) {
                    return 2;
                }
            }
            if (chr.get().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq || chr.get().getInventory(MapleInventoryType.USE).getNumFreeSlot() < use || chr.get().getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup || chr.get().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc || chr.get().getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash) {
                return 1;
            }
        }

        return 0;
    }

    public final static void completeTrade(final MapleCharacter c) {
        final MapleTrade local = c.getTrade();
        final MapleTrade partner = local.getPartner();

        if (partner == null || local.locked) {
            return;
        }
        local.locked = true; // Locking the trade
        partner.getChr().getClient().sendPacket(InteractionPacket.getTradeConfirmation());

        partner.exchangeItems = new LinkedList<Item>(local.items); // Copy this to partner's trade since it's alreadt accepted
        partner.exchangeMeso = local.meso; // Copy this to partner's trade since it's alreadt accepted

        if (partner.isLocked()) { // Both locked
            int lz = local.check(), lz2 = partner.check();
            if (lz == 0 && lz2 == 0) {
                local.CompleteTrade();
                partner.CompleteTrade();
            } else {
                // NOTE : IF accepted = other party but inventory is full, the item is lost.
                partner.cancel(partner.getChr().getClient(), partner.getChr(), lz == 0 ? lz2 : lz);
                local.cancel(c.getClient(), c, lz == 0 ? lz2 : lz);
            }
            partner.getChr().setTrade(null);
            c.setTrade(null);
        }
    }

    public static final void cancelTrade(final MapleTrade Localtrade, final MapleClient c, final MapleCharacter chr) {
        Localtrade.cancel(c, chr);

        final MapleTrade partner = Localtrade.getPartner();
        if (partner != null && partner.getChr() != null) {
            partner.cancel(partner.getChr().getClient(), partner.getChr());
            partner.getChr().setTrade(null);
        }
        chr.setTrade(null);
    }

    public static final void startTrade(final MapleCharacter c) {
        if (c.getTrade() == null) {
            c.setTrade(new MapleTrade((byte) 0, c));
            c.getClient().sendPacket(InteractionPacket.getTradeStart(c.getClient(), c.getTrade(), (byte) 0));
        } else {
            c.getClient().sendPacket(MaplePacketCreator.serverNotice(5, "您目前已經在交易了。"));
        }

    }

    public static final void startCashTrade(final MapleCharacter c) {
        if (c.getTrade() == null) {
            c.setTrade(new MapleTrade((byte) 0, c));
            c.getClient().sendPacket(InteractionPacket.getCashTradeStart(c.getClient(), c.getTrade(), (byte) 0));
        } else {
            c.getClient().sendPacket(MaplePacketCreator.serverNotice(5, "正在在交易中。"));
        }
    }

    public static final void inviteTrade(final MapleCharacter c1, final MapleCharacter c2) {
        if (c1 == null || c1.getTrade() == null) {
            return;
        }
        if (c2 != null && c2.getTrade() == null) {
            c2.setTrade(new MapleTrade((byte) 1, c2));
            c2.getTrade().setPartner(c1.getTrade());
            c1.getTrade().setPartner(c2.getTrade());
            c2.getClient().sendPacket(InteractionPacket.getTradeInvite(c1));
        } else {
            c1.getClient().sendPacket(MaplePacketCreator.serverNotice(5, "對方正在忙碌中。"));
            cancelTrade(c1.getTrade(), c1.getClient(), c1);
        }
    }

    public static final void inviteCashTrade(final MapleCharacter c1, final MapleCharacter c2) {
        if (c1 == null || c1.getTrade() == null) {
            return;
        }
        if (c2 != null && c2.getTrade() == null) {
            c2.setTrade(new MapleTrade((byte) 1, c2));
            c2.getTrade().setPartner(c1.getTrade());
            c1.getTrade().setPartner(c2.getTrade());
            c2.getClient().sendPacket(InteractionPacket.getCashTradeInvite(c1));
        } else {
            c1.getClient().sendPacket(MaplePacketCreator.serverNotice(5, "另一位玩家正在交易中。"));
            cancelTrade(c1.getTrade(), c1.getClient(), c1);
        }
    }

    public static final void visitTrade(final MapleCharacter c1, final MapleCharacter c2) {
        if (c2 != null && c1.getTrade() != null && c1.getTrade().getPartner() == c2.getTrade() && c2.getTrade() != null && c2.getTrade().getPartner() == c1.getTrade()) {
            // We don't need to check for map here as the user is found via MapleMap.getCharacterById()
            c1.getTrade().inTrade = true;
            c2.getClient().sendPacket(PlayerShopPacket.shopVisitorAdd(c1, 1));
            c1.getClient().sendPacket(InteractionPacket.getTradeStart(c1.getClient(), c1.getTrade(), (byte) 1));
            c1.dropMessage(-2, "[系統] : 進行交易時，請注意物品的分類。");
            c1.dropMessage(-2, "[系統] : 進行楓幣交易時，清注意手續費。");
            c2.dropMessage(-2, "[系統] : 進行交易時，請注意物品的分類。");
            c2.dropMessage(-2, "[系統] : 進行楓幣交易時，清注意手續費。");
        } else {
            c1.getClient().sendPacket(MaplePacketCreator.serverNotice(5, "本次交易被取消了。"));
        }
    }

    public static final void visitCashTrade(final MapleCharacter c1, final MapleCharacter c2) {
        if (c2 != null && c1.getTrade() != null && c1.getTrade().getPartner() == c2.getTrade() && c2.getTrade() != null && c2.getTrade().getPartner() == c1.getTrade()) {
            // We don't need to check for map here as the user is found via MapleMap.getCharacterById()
            c1.getTrade().inTrade = true;
            c2.getClient().sendPacket(PlayerShopPacket.shopVisitorAdd(c1, 1));
            c1.getClient().sendPacket(InteractionPacket.getCashTradeStart(c1.getClient(), c1.getTrade(), (byte) 1));
        } else {
            c1.getClient().sendPacket(MaplePacketCreator.serverNotice(5, "本次交易被取消了。"));
        }
    }

    public static final void declineTrade(final MapleCharacter c) {
        final MapleTrade trade = c.getTrade();
        if (trade != null) {
            if (trade.getPartner() != null) {
                MapleCharacter other = trade.getPartner().getChr();
                if (other != null && other.getTrade() != null) {
                    other.getTrade().cancel(other.getClient(), other);
                    other.setTrade(null);
                    other.dropMessage(5, c.getName() + " 拒絕了交易申請。");
                }
            }
            trade.cancel(c.getClient(), c);
            c.setTrade(null);
        }
    }
}
