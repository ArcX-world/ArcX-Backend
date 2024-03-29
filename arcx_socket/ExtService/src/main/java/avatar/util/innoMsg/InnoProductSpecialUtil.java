package avatar.util.innoMsg;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.global.linkMsg.websocket.SelfInnoWebsocketInnerCmd;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.product.InnoParamsUtil;

/**
 * 自研设备特殊信息
 */
public class InnoProductSpecialUtil {
    /**
     * 特殊中奖
     */
    public static void specialAward(int userId, int productId, int awardType){
        //查询设备信息
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        String host = productInfoEntity.getIp();//设备IP
        int port = Integer.parseInt(productInfoEntity.getPort());//设备端口
        String linkMsg = SyncInnoConnectUtil.linkMsg(host, port);//链接信息
        int uId = SyncInnoConnectUtil.loadHostId(host, port+"");
        //推送操作信息
        SyncInnoClient client = SyncInnoOperateUtil.loadClient(host, port, linkMsg);
        SendInnoInnerMsgUtil.sendClientMsg(client, SelfInnoWebsocketInnerCmd.P2S_OPERATE_MSG, uId,
                InnoParamsUtil.initProductOperateMsg(productId, productInfoEntity, userId, awardType));
    }
}
