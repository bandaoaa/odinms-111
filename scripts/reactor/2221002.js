/*
     名字：隱藏地圖
     地图：深山凶宅
     描述：222010401
 */

function act() {
    rm.getPlayer().getMap().spawnMonsterOnGroundBelow(Packages.server.life.MapleLifeFactory.getMonster(7130402), new java.awt.Point(-340, 140));
    rm.getPlayer().getMap().broadcastMessage(Packages.tools.packet.MaplePacketCreator.serverNotice(6, "綠色鬼怪出現了"));
}

//2022051蕎麥蒟蒻
