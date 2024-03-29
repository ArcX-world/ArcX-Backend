package avatar.apiregister.test.dragonTrain;

import avatar.entity.activity.dragonTrain.user.DragonTrainUserMsgEntity;
import avatar.facade.SystemEventHttpHandler;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.TestHttpCmdName;
import avatar.module.activity.dragonTrain.user.DragonTrainUserMsgDao;
import avatar.module.user.info.UserInfoDao;
import avatar.net.session.Session;
import avatar.util.LogUtil;
import avatar.util.basic.general.CheckUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化自研龙珠玛丽机玩家信息
 */
@Service
public class InitInnoDragonTrainUserMsgApi extends SystemEventHttpHandler<Session> {
    protected InitInnoDragonTrainUserMsgApi() {
        super(TestHttpCmdName.INIT_INNO_DRAGON_TRAIN_USER_MSG);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");//玩家ID
        int num = ParamsUtil.intParmas(map, "num");//龙珠数量
        if(CheckUtil.isTestEnv()) {
            //查询玩家信息
            if(UserInfoDao.getInstance().loadByUserId(userId)!=null){
                if(num<0 || num>6){
                    LogUtil.getLogger().info("初始化自研龙珠玛丽机数量错误-------");
                }else {
                    //查询玩家自研玛丽机信息
                    DragonTrainUserMsgEntity entity = DragonTrainUserMsgDao.getInstance().loadByUserId(userId);
                    entity.setDragonNum(num);
                    boolean flag = DragonTrainUserMsgDao.getInstance().update(entity);
                    if(!flag){
                        LogUtil.getLogger().info("初始化自研龙珠玛丽机玩家{}信息失败-------", userId);
                    }
                }
            }else{
                LogUtil.getLogger().info("初始化自研龙珠玛丽机玩家信息查询不到玩家{}的信息-------", userId);
            }
        }
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
    }
}
