/*
     名字：組隊任務
     地图：機智的測試
     描述：610030400
 */

function act() {
    rm.getPlayer().getMap().killAllMonsters(true);
    rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.MaplePacketCreator.serverNotice(6, "所有的 tirges 都消失了"));
}
