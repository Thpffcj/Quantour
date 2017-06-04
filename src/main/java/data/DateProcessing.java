package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dataService.DateProcessingService;

public class DateProcessing implements DateProcessingService {

	
    public Date strToDate(String today) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得当前日期是星期几
     *
     * @param date
     * @return
     */
    public int getWeekOfDate(String date) {
        Calendar c = Calendar.getInstance();
        c.setTime(strToDate(date));
        int d = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (d == 0) {
            d += 7;
        }
        return d;

    }

    /**
     * 将date类转化为规定格式string
     *
     * @param date
     * @return
     */
    public String dateToStr(Date date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 计算和某个个日期相隔若干天的日期
     *
     * @param endDate
     * @param offset
     * @return 日期
     */
    public String count(String endDate, int offset) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        date = strToDate(endDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, offset);
        date = c.getTime();
        String beginDate = sdf.format(date);
        return beginDate;
    }

    /**
     * 获得两个日期间的所有日期
     *
     * @param beginDate
     * @param endDate
     * @return 日期的列表
     * @throws ParseException 
     */
    public ArrayList<String> splitDays(String beginDate, String endDate) throws ParseException {
   
    	ArrayList<String> days = new ArrayList<String>();
        if (beginDate.compareTo(endDate) <= 0) {
            String date = new String(beginDate);
            while (date.compareTo(endDate) < 0) {
                days.add(date);
                date = count(date, 1);
            }
            days.add(endDate);

            if (days.size() != 0) {
                return days;
            }
        }

        return null;

    }
    
    /**
     * 获得从昨天开始最近的一个工作日
     *
     * @return 工作日字符串
     */
    public String getRecentWorkDay(String date) {
        String today = dateToStr(new Date());

        //获取今天的星期
        int weekDayNumber = getWeekOfDate(date);

        //周2-6,最新的数据是昨天的
        if (weekDayNumber >= 2 && weekDayNumber <= 6) {
            return count(today, -1);
            //周天,最新的数据是这个周五的
        } else if (weekDayNumber == 7) {
            return count(today, -2);
            //周1,最新的数据是上个周五的
        } else {
            return count(today, -3);
        }
    }
    
    public static void main(String[] args) throws ParseException {

    	DateProcessing dp = new DateProcessing();
//   	System.out.println(dp.count("2017-05-12", 20));
    	System.out.println(dp.splitDays("2005-04-28","2005-04-29"));
	}
}
