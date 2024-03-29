package avatar.apiregister.test.inno;

import avatar.entity.user.online.UserOnlineMsgEntity;
import avatar.facade.SystemEventHttpHandler;
import avatar.global.enumMsg.basic.errrorCode.ClientCode;
import avatar.global.linkMsg.http.TestHttpCmdName;
import avatar.module.user.online.UserOnlineMsgDao;
import avatar.net.session.Session;
import avatar.util.basic.general.CheckUtil;
import avatar.util.innoMsg.InnoProductSpecialUtil;
import avatar.util.sendMsg.SendMsgUtil;
import avatar.util.system.ParamsUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自研设备中奖
 */
@Service
public class InnoProductAwardApi extends SystemEventHttpHandler<Session> {
    protected InnoProductAwardApi() {
        super(TestHttpCmdName.INNO_PRODUCT_AWARD);
    }

    @Override
    public void method(Session session, Map<String, Object> map) throws Exception {
        int userId = ParamsUtil.intParmasNotNull(map, "dealUserId");//玩家ID
        int awardType = ParamsUtil.intParmasNotNull(map, "awardType");//设备奖励类型
        if(CheckUtil.isTestEnv()) {
            //查询在线设备
            List<UserOnlineMsgEntity> list = UserOnlineMsgDao.getInstance().loadByUserId(userId);
            if(list.size()>0){
                UserOnlineMsgEntity entity = list.get(0);
                int productId = entity.getProductId();//设备ID
                if(productId>0) {
                    //自研
                    InnoProductSpecialUtil.specialAward(userId, productId, awardType);
                }
            }
        }
        SendMsgUtil.sendBySessionAndMap(session, ClientCode.SUCCESS.getCode(), new HashMap<>());
    }

}
