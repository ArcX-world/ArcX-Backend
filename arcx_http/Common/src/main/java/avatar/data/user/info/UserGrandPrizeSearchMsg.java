package avatar.data.user.info;

/**
 * 玩家设备大奖查询信息
 */
public class UserGrandPrizeSearchMsg {
    //设备大奖图片
    private String pzPic;

    //设备大奖数量
    private int pzQt;

    public String getPzPic() {
        return pzPic;
    }

    public void setPzPic(String pzPic) {
        this.pzPic = pzPic;
    }

    public int getPzQt() {
        return pzQt;
    }

    public void setPzQt(int pzQt) {
        this.pzQt = pzQt;
    }
}
