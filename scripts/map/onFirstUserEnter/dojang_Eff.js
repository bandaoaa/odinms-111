/*
     名字：武陵道場
     地图：武陵道場1樓
     描述：925020100
 */

function start() {
    var stage = Math.floor(ms.getPlayer().getMap().getId() / 100) % 100;
    var realstage = stage - ((stage / 6) | 0);

    time = realstage < 6 ? 5 : realstage < 11 ? 6 : realstage < 16 ? 7 : realstage < 21 ? 8 : realstage < 26 ? 9 : realstage < 31 ? 10 : realstage < 36 ? 15 : 20;

    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.getClock(time * 60));
    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.environmentChange("Dojang/start", 4));
    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.environmentChange("dojang/start/stage", 3));
    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.environmentChange("dojang/start/number/" + realstage, 3));
    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.trembleEffect(0, 1));
    ms.dispose();
}
