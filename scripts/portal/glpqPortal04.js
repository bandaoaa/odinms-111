/*
     名字：組隊任務
     地图：團結的測試
     描述：610030500
 */

function enter(pi) {
    var y = pi.getPlayer().getJob();
    if (y > 500 && y < 540 || y > 1500 && y < 1520 || y > 3500 && y < 3520) {
        pi.playPortalSE();
        pi.getPlayer().changeMap(pi.getMap(610030550), pi.getMap(610030550).getPortal(1)); //海盜專精的房間
        return true;
    }
    pi.getClient().sendPacket(Packages.tools.packet.MaplePacketCreator.serverNotice(6, "海盜專精的房間只適用于海盜"));
    return false;
}
