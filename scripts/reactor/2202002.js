/*
     名字：玩具城
     地图：玩具工廠&amp;lt;B工程4&gt;
     描述：220030400
 */

function act() {
    var map = rm.getPlayer().getQuestNAdd(Packages.server.quest.MapleQuest.getInstance(3238)).getStatus() == 1 ? 922000020 : 922000009;
    rm.getPlayer().changeMap(rm.getMap(map), rm.getMap(map).getPortal(0));
    rm.getClient().getSession().write(Packages.tools.packet.MaplePacketCreator.serverNotice(6, "由於未知的力量，你被轉移至該區域"));
}
