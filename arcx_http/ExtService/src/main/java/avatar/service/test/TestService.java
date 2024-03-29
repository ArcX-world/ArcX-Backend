package avatar.service.test;

import avatar.global.basicConfig.basic.LocalSolanaConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.activity.WelfareUtil;
import avatar.util.basic.CheckUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.thirdpart.SolanaUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试接口实现类
 */
public class TestService {
    /**
     * 清除签到信息
     */
    public static void clearSignMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        if(CheckUtil.isTestEnv()) {
            int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");//玩家ID
            int continueDay = ParamsUtil.intParmas(map, "continueDay");//持续天数
            //签到奖励
            WelfareUtil.resetUserSignMsg(userId, continueDay);
        }else{
            LogUtil.getLogger().info("线上无法调用清除福利奖励玩家信息处理--------");
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), dataMap);
    }

    /**
     * 服务测试
     */
    public static void serverTest(Map<String, Object> map, Session session) {
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
//        GameShiftUtil.register(1000004,"7856176776@qq.com");
//        GameShiftUtil.loadWalletAddress(100003);
//        SolanaUtil.createAccount(LocalSolanaConfigMsg.walleyAccount, SolanaUtil.usdtMintPubkey());
//        SolanaUtil.accountBalance("Ask2c73HoBMjDsP1JFZzF1niUf5bEBnMq9qTCz8hRgQC");
//        SolanaUtil.accountBalance("4bKD4RuruZDvGhDBsKKnYczU4dfXvGDsgLtQGKN1EEQy");
//        System.out.println(GameShiftUtil.getBalance(100002));
//        SolanaUtil.accountBalance("4bKD4RuruZDvGhDBsKKnYczU4dfXvGDsgLtQGKN1EEQy");
        SolanaUtil.accountBalance("CEQkasup5F2mWNoEC9oEhyWspsDtLuZxeTdXJxXaMrTQ");
    }
}
