/*
     名字：轉蛋機
     地图：勇士之村
     描述：102000000
 */

var z0 = [2430307, 2430309, 2430311, 2430313, 3010028, 3010040, 3010041, 3010043, 3010046];

var z1 = [2043000, 2043001, 2043002, 2043004, 2043005, 2043006, 2043007, 2043008, 2043009, 2043010, 2043015, 2043016, 2043017, 2043018, 2043019];

var z2 = [2043100, 2043101, 2043102, 2043104, 2043105, 2043110, 2043111, 2043112, 2043113, 2043114];

var z3 = [2043200, 2043201, 2043202, 2043204, 2043205, 2043210, 2043211, 2043212, 2043213, 2043214];

var z4 = [2044000, 2044001, 2044002, 2044004, 2044005, 2044010, 2044011, 2044012, 2044013, 2044014];

var z5 = [2044100, 2044101, 2044102, 2044104, 2044105, 2044110, 2044111, 2044112, 2044113, 2044114];

var z6 = [2044200, 2044201, 2044202, 2044204, 2044205, 2044210, 2044211, 2044212, 2044213, 2044214];

var z7 = [2044300, 2044301, 2044302, 2044304, 2044305, 2044310, 2044311, 2044312, 2044313, 2044314];

var z8 = [2044400, 2044401, 2044402, 2044404, 2044405, 2044410, 2044411, 2044412, 2044413, 2044414];

var z9 = [2290000, 2290001, 2290002, 2290003, 2290004, 2290005, 2290006, 2290007, 2290008, 2290009, 2290010, 2290011, 2290012, 2290013, 2290014, 2290015, 2290016, 2290017, 2290018, 2290019, 2290020, 2290021, 2290022, 2290023, 2290096, 2290125, 2290204, 2290205, 2290290, 2290291, 2290292, 2290293];

function start() {
    if (cm.getPlayer().itemQuantity(5220000) || cm.getPlayer().itemQuantity(5451000))
        cm.sendSimple("欢迎使用转蛋机进行抽奖服务，你想要使用它吗？#b\r\n\r\n#L0#使用转蛋机。#l\r\n#L2#查看转蛋机奖品。#l");
    else
        cm.sendSimple("欢迎使用转蛋机进行抽奖服务，你想要使用它吗？#b\r\n\r\n#L1#什么是转蛋机。#l\r\n#L2#查看转蛋机奖品。#l");
}

function action(mode, type, selection) {
    switch (selection) {
        case 0:
            var rand = Math.floor(Math.random() * 100);
            y = rand < 1 ? z0 : rand < 10 ? z1 : rand < 20 ? z2 : rand < 30 ? z3 : rand < 40 ? z4 : rand < 50 ? z5 : rand < 60 ? z6 : rand < 70 ? z7 : rand < 80 ? z8 : z9;
            z = cm.gainGachaponItem(y[Math.floor(Math.random() * y.length)], 1);
            if (z != -1) {
                cm.gainItem(cm.getPlayer().itemQuantity(5220000) && cm.getPlayer().getMap().getId() == 102000000 ? 5220000 : 5451000, -1);
                cm.sendOk("你获得了一个#b#t" + z + "##k。");
                cm.dispose();
                return;
            }
            cm.sendOk("请确认是不是你的背包的空间不够。");
            break;
        case 1:
            cm.sendOk("使用转蛋机可以获得稀有的卷轴、时装、椅子、宠物和其他很酷的物品！只需要进入#r游戏商城#k购买#b快乐百宝券#k就可以随\r\n机抽到它。");
            break;
        case 2:
            y0 = y1 = y2 = y3 = y4 = y5 = y6 = y7 = y8 = y9 = "";
            for (var x0 = 0; x0 < z0.length; x0++)
                y0 += "#v" + z0[x0] + ":#";
            for (var x1 = 0; x1 < z1.length; x1++)
                y1 += "#v" + z1[x1] + ":#";
            for (var x2 = 0; x2 < z2.length; x2++)
                y2 += "#v" + z2[x2] + ":#";
            for (var x3 = 0; x3 < z3.length; x3++)
                y3 += "#v" + z3[x3] + ":#";
            for (var x4 = 0; x4 < z4.length; x4++)
                y4 += "#v" + z4[x4] + ":#";
            for (var x5 = 0; x5 < z5.length; x5++)
                y5 += "#v" + z5[x5] + ":#";
            for (var x6 = 0; x6 < z6.length; x6++)
                y6 += "#v" + z6[x6] + ":#";
            for (var x7 = 0; x7 < z7.length; x7++)
                y7 += "#v" + z7[x7] + ":#";
            for (var x8 = 0; x8 < z8.length; x8++)
                y8 += "#v" + z8[x8] + ":#";
            for (var x9 = 0; x9 < z9.length; x9++)
                y9 += "#v" + z9[x9] + ":#";
            cm.sendOk("稀有物品：\r\n\r\n" + y0 + "\r\n单手剑卷轴：\r\n\r\n" + y1 + "\r\n单手斧卷轴：\r\n\r\n" + y2 + "\r\n单手棍卷轴：\r\n\r\n" + y3 + "\r\n双手剑卷轴：\r\n\r\n" + y4 + "\r\n双手斧卷轴：\r\n\r\n" + y5 + "\r\n双手棍卷轴：\r\n\r\n" + y6 + "\r\n枪卷軸：\r\n\r\n" + y7 + "\r\n矛卷轴：\r\n\r\n" + y8 + "\r\n技能册：\r\n\r\n" + y9);
            break;
        case 3:
            cm.sendOk("轉蛋機的使用券在楓之谷商城提供，可以使用樂豆點數或楓葉點數購買。點擊螢幕右下角的紅色商店，訪問#r楓之谷商城#k，您可以在商城中購買到轉蛋券。");
    }
    cm.dispose();
}
