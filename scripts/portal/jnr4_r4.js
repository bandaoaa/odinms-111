/*
     名字：隱藏地圖
     地图：研究室走廊
     描述：926110300
 */

function enter(pi) {
    pi.playPortalSE();
    pi.getPlayer().changeMap(pi.getMap(926110304), pi.getMap(926110304).getPortal(2)); //研究室104號
    return true;
}
