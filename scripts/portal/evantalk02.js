/*
     名字：夢中
     地图：夢見的森林入口
     描述：900010000
 */

function enter(pi) {
    if (pi.getPlayer().getInfoQuest(22013).indexOf("mo02=o") != -1) {
        return false;
    }
    pi.getPlayer().updateInfoQuest(22013, pi.getPlayer().getInfoQuest(22013) + ";mo02=o");
    pi.getClient().sendPacket(Packages.tools.packet.EtcPacket.EffectPacket.AranTutInstructionalBalloon("Effect/OnUserEff.img/guideEffect/evanTutorial/evanBalloon02"));
    return true;
}
