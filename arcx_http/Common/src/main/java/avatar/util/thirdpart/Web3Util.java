package avatar.util.thirdpart;

import avatar.data.thirdpart.Web3WalletMsg;
import avatar.entity.user.thirdpart.Web3AxcAccountEntity;
import avatar.module.user.thirdpart.Web3AxcAccountDao;
import avatar.util.system.StrUtil;
import avatar.util.system.TimeUtil;

/**
 * web 3.0工具类
 */
public class Web3Util {

    /**
     * 填充web3 axc代币账号实体信息
     */
    private static Web3AxcAccountEntity initWeb3AxcAccountEntity(int userId, Web3WalletMsg msg) {
        Web3AxcAccountEntity entity = new Web3AxcAccountEntity();
        entity.setUserId(userId);//玩家ID
        entity.setAxcAccount(msg.getAta());//axc账号
        entity.setAccountPk(msg.getAccount().getPk());//账号公钥
        entity.setAccountSk(msg.getAccount().getSk());//账号私钥
        entity.setCreateTime(TimeUtil.getNowTimeStr());//创建时间
        return entity;
    }

    /**
     * 添加AXC代币账号+钱包
     */
    public static void addAxcAccount(int userId) {
        //查询账号信息
        Web3AxcAccountEntity entity = Web3AxcAccountDao.getInstance().loadByMsg(userId);
        if(entity==null){
            //创建账号
            Web3WalletMsg msg = SolanaUtil.createAccount("", SolanaUtil.axcMintPubkey());
            //注册账号信息
            if(!StrUtil.checkEmpty(msg.getAta()) && !StrUtil.checkEmpty(msg.getAccount().getPk()) &&
                    !StrUtil.checkEmpty(msg.getAccount().getSk())){
                Web3AxcAccountDao.getInstance().insert(initWeb3AxcAccountEntity(userId, msg));
            }
        }
    }
}
