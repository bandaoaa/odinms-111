/*
     名字：航海中
     地图：前往猴子方向
     描述：912060100
 */

function start() {
    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.UIPacket.IntroEnableUI(1));

    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.EffectPacket.ShowWZEffect("Effect/Direction4.img/cannonshooter/Scene00"));
    ms.getClient().sendPacket(Packages.tools.packet.EtcPacket.EffectPacket.ShowWZEffect("Effect/Direction4.img/cannonshooter/out00"));
    ms.dispose();
}
