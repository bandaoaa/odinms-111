/*
     名字：可可島
     地图：火箭出發
     描述：912060200
 */

function enter(pi) {
    pi.getClient().sendPacket(Packages.tools.packet.EtcPacket.UIPacket.getDirectionInfo(3, 0));
    pi.openNpc(1096012);
    return true;
}
