package avatar.util.system;

import avatar.global.enumMsg.system.SearchTimeEnum;
import avatar.util.checkParams.ErrorDealUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class TimeUtil {

    /**
     * 转换日期
     * @param time
     * @return
     */
    public static Date strToDate(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(time);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return null;
    }

    /**
     * 转换日期
     */
    public static String dateToStr(Date time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    /**
     * 获取现在的时间字符串
     * @return
     */
    public static String getNowTimeStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 把string类型的时间转成时间戳
     * @param time
     * @return
     */
    public static long strToLong(String time){
        long retTime = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(!StrUtil.checkEmpty(time)) {
                Date dateTime = sdf.parse(time);
                retTime = dateTime.getTime();
            }
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**
     * 获取时间范围的时间戳
     * @return
     */
    public static Date getTimeRange(int range){
        Calendar c = Calendar.getInstance();
        Date m = null;
        if(range == SearchTimeEnum.ONE_MONTH.getCode()){
            //一个月
            c.setTime(new Date());
            c.add(Calendar.MONTH, -SearchTimeEnum.ONE_MONTH.getMonth());
            m = c.getTime();
        }else if(range == SearchTimeEnum.THREE_MONTH.getCode()){
            //三个月
            c.setTime(new Date());
            c.add(Calendar.MONTH, -SearchTimeEnum.THREE_MONTH.getMonth());
            m = c.getTime();
        }else if(range == SearchTimeEnum.HALF_YEAR.getCode()){
            //半年
            c.setTime(new Date());
            c.add(Calendar.MONTH, -SearchTimeEnum.HALF_YEAR.getMonth());
            m = c.getTime();
        }
        return m;
    }

    /**
     * 获取当天的日期
     * @return
     */
    public static String getNowDay(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(new Date());
    }

    /**
     * 获取指定时间日期
     * @param time
     * @return
     */
    public static String getTimeForDay(String time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String day = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            day = sdfDay.format(date);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return day;
    }

    /**
     * 把时间戳转换成时间日期
     * @param time
     * @return
     */
    public static String longToDay(long time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return sdfDay.format(date);
    }

    /**
     * 把时间戳转换成时间日期
     */
    public static String longToSimpleDay(long time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(time);
        return sdfDay.format(date);
    }

    /**
     * 将字符串时间修改格式
     * @param time
     * @return
     */
    public static String strToDay(String time){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String str = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            str = sdfDay.format(date);
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return str;
    }

    /**
     * 把时间戳转换成详细日期
     * @param time
     * @return
     */
    public static String longToStr(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 把String类型的时间转换成年份
     * @param time
     * @return
     */
    public static String strToYear(String time){
        String year = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateTime = sdf.parse(time);
            year=String.format("%tY", dateTime);
        }catch(Exception e){
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**
     * 获取现在的时间
     * @returntest
     */
    public static long getNowTime(){
        return (new Date()).getTime();
    }

    /**
     * 获取N个月以后的时间
     * @return
     */
    public static String getNMonth(String time, int month){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date m = null;
        try {
            c.setTime(sdf.parse(time));
            c.add(Calendar.MONTH, month);
            m = c.getTime();
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return sdf.format(m);
    }

    //获取第一个24点时间
    public static Date getFirstDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);//24点
        calendar.set(Calendar.MINUTE, 00);//0分
        calendar.set(Calendar.SECOND, 00);//0秒
//        calendar.set(Calendar.HOUR_OF_DAY, 10);//24点
//        calendar.set(Calendar.MINUTE, 55);//0分
//        calendar.set(Calendar.SECOND, 00);//0秒
        Date date = calendar.getTime();//获取日期
        return date;
    }

    /**
     * 维护时间，方便睡觉
     * @return
     */
    public static Date repairTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);//24点
        calendar.set(Calendar.MINUTE, 00);//0分
        calendar.set(Calendar.SECOND, 00);//0秒
        Date date = calendar.getTime();//获取日期
        return date;
    }

    /**
     * 维护完成时间，方便睡觉
     * @return
     */
    public static Date completeTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13);//24点
        calendar.set(Calendar.MINUTE, 29);//0分
        calendar.set(Calendar.SECOND, 00);//0秒
        Date date = calendar.getTime();//获取日期
        return date;
    }

    public static Date getFirstDay1(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);//24点
        calendar.set(Calendar.MINUTE, 48);//0分
        calendar.set(Calendar.SECOND, 00);//0秒
        Date date = calendar.getTime();//获取日期
        return date;
    }

    // 增加或减少天数
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * 获取指定日期年份
     * @param time
     * @return
     */
    public static int getTimeYear(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int year = 0;
        try {
            Date date = sdf.parse(time);
            year = Integer.parseInt(String.format("%tY", date));
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**
     * 获取指定日期月份
     * @param time
     * @return
     */
    public static int getTimeMonth(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int year = 0;
        try {
            Date date = sdf.parse(time);
            year = Integer.parseInt(String.format("%tm", date));
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**
     * 获取指定日期的日期
     * @param time
     * @return
     */
    public static int getTimeDay(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int year = 0;
        try {
            Date date = sdf.parse(time);
            year = Integer.parseInt(String.format("%td", date));
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return year;
    }

    /**
     * 获取当天年份
     * @return
     */
    public static int getTodayYear(){
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.YEAR);
        return day;
    }

    /**
     * 获取当天月份
     * @return
     */
    public static int getTodayMonth(){
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.MONTH)+1;
        return day;
    }

    /**
     * 获取当天日期
     * @return
     */
    public static int getTodayDate(){
        Calendar cale = Calendar.getInstance();
        int day = cale.get(Calendar.DATE);
        return day;
    }

    /**
     * 获取当天时间
     * @return
     */
    public static String getTodayTime(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(new Date())+" 00:00:00";
    }

    /**
     * 获取当天日期
     * @return
     */
    public static String getTodayDay(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDay.format(new Date());
    }

    /**
     * 获取当天时间戳
     * @return
     */
    public static long getTodayLongTime(){
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            String time = sdfDay.format(new Date()) + " 00:00:00";
            retTime = sdf.parse(time).getTime();
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }


    /**
     * 获取当天星期，星期天是1
     * @return
     */
    public static int getTodatWeek(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    /**
     * 获取最近一个星期一
     * @return
     */
    public static String getNearlyMonday(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String retDay;
        c.setTime(new Date());
        int dayOfWeek = getTodatWeek();
        //星期一是第一天
        if(dayOfWeek==2){
            retDay = sdfDay.format(new Date())+" 00:00:00";
        }else{
            int diffWeek = 2-dayOfWeek;
            c.add(Calendar.DAY_OF_MONTH, diffWeek);
            Date date = c.getTime();
            retDay = sdfDay.format(date)+" 00:00:00";
        }
        return retDay;
    }

    /**
     * 获取一天后的时间
     * @param time
     * @return
     */
    public static long getNextDay(String time){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.DATE,1);
            Date retDate = c.getTime();
            retTime = retDate.getTime();
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**
     * 获取N个小时后的时间
     * @param time
     * @param hour
     * @return
     */
    public static long getNHour(String time, int hour) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.HOUR_OF_DAY,hour);
            Date retDate = c.getTime();
            retTime = retDate.getTime();
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**
     * 获取N个小时前的时间
     * @param time
     * @param hour
     * @return
     */
    public static String getBeforeNHour(String time, int hour) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retTime = "";
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.HOUR_OF_DAY,hour*-1);
            Date retDate = c.getTime();
            retTime = sdf.format(retDate);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**
     * 获取N小时前的日期
     * @param time
     * @param hour
     * @return
     */
    public static String getDayBeforeNHour(String time, int hour) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retTime = "";
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.HOUR_OF_DAY,hour*-1);
            Date retDate = c.getTime();
            retTime = sdfDay.format(retDate);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**
     * 获取N分钟前的时间
     * @param time
     * @param minute
     * @return
     */
    public static String getDayBeforeNMinute(String time, int minute) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retTime = "";
        try {
            Date date = sdf.parse(time);
            c.setTime(date);
            c.add(Calendar.MINUTE,minute*-1);
            Date retDate = c.getTime();
            retTime = sdf.format(retDate);
        } catch (ParseException e) {
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**
     * 获取时间戳
     * @param createTime
     * @return
     */
    public static long strToDayLongTime(String createTime) {
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long retTime = 0;
        try {
            Date newDate = sdfDay.parse(createTime);
            String newTime = sdfDay.format(newDate)+" 00:00:00";
            retTime = sdf.parse(newTime).getTime();
        }catch (Exception e){
            ErrorDealUtil.printError(e);
        }
        return retTime;
    }

    /**
     * 汇付宝提交单据时间
     * @return
     */
    public static String billTime() {
        SimpleDateFormat hfbSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return hfbSimpleDateFormat.format(date);
    }

    /**
     * 获取支付时间戳
     */
    public static String loadPayTime(){
        //时间戳
        Date date = new Date();
        long time = date.getTime();
        //mysq 时间戳只有10位 要做处理
        String dateline = time + "";
        dateline = dateline.substring(0, 10);
        return dateline;
    }
}
