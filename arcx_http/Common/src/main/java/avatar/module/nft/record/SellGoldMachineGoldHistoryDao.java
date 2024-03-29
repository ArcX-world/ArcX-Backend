package avatar.module.nft.record;

import avatar.entity.nft.SellGoldMachineGoldHistoryEntity;
import avatar.util.GameData;
import avatar.util.system.SqlUtil;
import avatar.util.system.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 售币机售币记录数据接口
 */
public class SellGoldMachineGoldHistoryDao {
    private static final SellGoldMachineGoldHistoryDao instance = new SellGoldMachineGoldHistoryDao();
    public static final SellGoldMachineGoldHistoryDao getInstance(){
        return instance;
    }

    //=========================db===========================

    /**
     * 添加
     */
    public boolean insert(SellGoldMachineGoldHistoryEntity entity){
        return GameData.getDB().insert(entity);
    }

    /**
     * 盈利总额
     */
    public double loadDbEarn(String nftCode) {
        String sql = "select sum(real_earn) from sell_gold_machine_gold_history where nft_code=?";
        List<String> list = GameData.getDB().listString(sql, new Object[]{nftCode});
        String amountStr = StrUtil.strListNum(list);
        return StrUtil.truncateFourDecimal(Double.parseDouble(amountStr));
    }

    /**
     * 查询NFT报告分页数据
     */
    public List<SellGoldMachineGoldHistoryEntity> loadDbReport(String nftCode, int pageNum, int pageSize) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select * from sell_gold_machine_gold_history ");
        //查询参数
        SqlUtil.equalsParamStr(sqlBuilder, "nft_code", nftCode, 0);
        //排序
        sqlBuilder.append(" order by create_time desc ");
        //分页
        sqlBuilder.append(SqlUtil.pageMsg(pageNum, pageSize));
        List<SellGoldMachineGoldHistoryEntity> list = GameData.getDB().list(SellGoldMachineGoldHistoryEntity.class,
                sqlBuilder.toString(), new Object[]{});
        return list==null?new ArrayList<>():list;
    }
}
