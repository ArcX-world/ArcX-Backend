package avatar.task.product.general;

import avatar.entity.product.info.ProductInfoEntity;
import avatar.module.product.info.ProductInfoDao;
import avatar.util.LogUtil;
import avatar.util.product.ProductUtil;
import avatar.util.user.UserWeightUtil;
import com.yaowan.game.common.scheduler.ScheduledTask;

/**
 * 设备玩家权重NA值定时器
 */
public class ProductUserWeightNaTask extends ScheduledTask {
    //设备ID
    private int productId;

    //玩家ID
    private int userId;

    //na值
    private long naNum;

    public ProductUserWeightNaTask(int productId, int userId, long naNum) {
        super("设备玩家权重NA值定时器");
        this.productId = productId;
        this.userId = userId;
        this.naNum = naNum;
    }

    @Override
    public void run() {
        //查询设备信息
        ProductInfoEntity productInfoEntity = ProductInfoDao.getInstance().loadByProductId(productId);
        int productType = productInfoEntity.getProductType();//设备类型
        int secondType = productInfoEntity.getSecondType();//设备二级分类
        LogUtil.getLogger().error("添加玩家{}{}游戏设备{}的权重NA值{}------", userId,  productType, productId, naNum);
        if(ProductUtil.isInnoProduct(productId)) {
            UserWeightUtil.addUserInnoNaNum(userId, secondType, naNum);
        }
    }
}
