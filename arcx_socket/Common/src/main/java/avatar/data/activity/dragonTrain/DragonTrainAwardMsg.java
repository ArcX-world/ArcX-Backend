package avatar.data.activity.dragonTrain;


import avatar.entity.activity.dragonTrain.info.DragonTrainWheelIconMsgEntity;

/**
 * 龙珠玛丽机奖励信息
 */
public class DragonTrainAwardMsg {
    //最终奖励值
    private int resultAwardNum;

    //转轮图标信息
    private DragonTrainWheelIconMsgEntity wheelIconMsg;

    public int getResultAwardNum() {
        return resultAwardNum;
    }

    public void setResultAwardNum(int resultAwardNum) {
        this.resultAwardNum = resultAwardNum;
    }

    public DragonTrainWheelIconMsgEntity getWheelIconMsg() {
        return wheelIconMsg;
    }

    public void setWheelIconMsg(DragonTrainWheelIconMsgEntity wheelIconMsg) {
        this.wheelIconMsg = wheelIconMsg;
    }
}
