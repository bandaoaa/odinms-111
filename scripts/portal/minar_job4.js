/*
     名字：神木村
     地图：山羊峽谷1
     描述：240010500
 */

function enter(pi) {

    pi.playPortalSE();
    pi.getPlayer().changeMap(pi.getMap(240010501), pi.getMap(240010501).getPortal(1)); //祭司之林
    return true;
}
