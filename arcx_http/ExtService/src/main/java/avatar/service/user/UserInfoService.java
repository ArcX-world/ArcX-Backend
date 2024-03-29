package avatar.service.user;

import avatar.global.basicConfig.basic.ConfigMsg;
import avatar.global.enumMsg.system.ClientCode;
import avatar.module.user.opinion.UserOpinionDao;
import avatar.net.session.Session;
import avatar.util.checkParams.CheckParamsUtil;
import avatar.util.checkParams.UserCheckParamsUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import avatar.util.user.UserOpinionUtil;
import avatar.util.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家信息接口实现类
 */
public class UserInfoService {

    /**
     * 更新玩家信息
     */
    public static void updateUserInfo(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        int status = UserCheckParamsUtil.updateUserInfo(map);//状态
        if(ParamsUtil.isSuccess(status)) {
            //处理登录
            int userId = ParamsUtil.userId(map);//玩家ID
            String nickName = ParamsUtil.stringParmasNotNull(map, "plyNm");//玩家昵称
            int sex = ParamsUtil.intParmasNotNull(map, "sex");//性别
            //处理玩家信息
            UserUtil.updateUserInfo(userId, nickName, sex);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 更新玩家密码
     */
    public static void updateUserPassword(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        int status = UserCheckParamsUtil.updateUserPassword(map);//状态
        if(ParamsUtil.isSuccess(status)) {
            //处理登录
            int userId = ParamsUtil.userId(map);//玩家ID
            String email = ParamsUtil.stringParmasNotNull(map, "email");//玩家昵称
            String password = ParamsUtil.stringParmasNotNull(map, "pwd");//密码
            //处理玩家信息
            UserUtil.updateUserPassword(userId, email, password);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 玩家信息
     */
    public static void userInfo(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        int status = CheckParamsUtil.checkAccessToken(map);//状态
        if(ParamsUtil.isSuccess(status)) {
            //处理登录
            int userId = ParamsUtil.userId(map);//玩家ID
            //填充玩家信息
            UserUtil.loadUserInfo(userId, dataMap);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 玩家帮助与反馈
     */
    public static void userOpinion(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.userOpinion(map);
        if(ParamsUtil.isSuccess(status)) {
            int userId = ParamsUtil.userId(map);//玩家ID
            String content = ParamsUtil.stringParmasNotNull(map, "fbCnt");//反馈内容
            String imgUrl = ParamsUtil.stringParmas(map, "fbPct");//照片URL
            //判断是否超出次数
            int num = UserOpinionDao.getInstance().loadDbByUserId(userId);
            if(num>= ConfigMsg.userMaxOpinion){
                status = ClientCode.OPINION_DAILY_MAX.getCode();//上限
            }else{
                boolean flag = UserOpinionDao.getInstance().insert(userId, content, imgUrl);
                if(!flag){
                    status = ClientCode.FAIL.getCode();//失败
                }
            }
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

    /**
     * 联系信息
     */
    public static void communicateMsg(Map<String, Object> map, Session session) {
        Map<String, Object> dataMap = new HashMap<>();//内容参数信息
        //检测参数
        int status = UserCheckParamsUtil.communicateMsg(map);
        if(ParamsUtil.isSuccess(status)) {
            String email = ParamsUtil.stringParmasNotNull(map, "email");//邮箱
            UserOpinionUtil.dealCommunicateMsg(email);
        }
        //推送结果
        SendMsgUtil.sendBySessionAndMap(session, status, dataMap);
    }

}
