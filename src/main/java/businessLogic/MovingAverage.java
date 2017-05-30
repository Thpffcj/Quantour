package businessLogic;

import java.util.ArrayList;

public class MovingAverage {
	
	/**
	 * 
	 * @param adjCloses  股票复权收盘价集合
	 * @param days       几日的均线图
	 * @param beforeDays 查询日期前股票数据的天数
	 * @return
	 */
	public ArrayList<Double> getAveData(ArrayList<Double> adjCloses, int days, int beforeDays){
//		System.out.println(days+" "+beforeDays);
//		System.out.println(adjCloses.size());
		if(adjCloses.size() == days-1){
			return null;
		}
		ArrayList<Double> needCloses= new ArrayList<>();
		if(beforeDays>=days-1){
			double aveClose = 0.0;
			for(int i=0; i<days; i++){
				aveClose +=	adjCloses.get(i);
			}
			aveClose = aveClose/days;
			needCloses.add(aveClose);
			for(int i=days; i<adjCloses.size(); i++){
				aveClose = aveClose*days;
				aveClose = aveClose + adjCloses.get(i) - adjCloses.get(i-days);
				aveClose = aveClose/days;
				needCloses.add(aveClose);
			}
		}
		else{
			for(int i=beforeDays ;i<=adjCloses.size()-days+beforeDays;i++){
				double aveClose = 0.0;
				for(int j=0; j<days; j++){
					aveClose += adjCloses.get(i-beforeDays+j);
				}
				aveClose = aveClose/days;
				needCloses.add(aveClose);
			}
		}
		return needCloses;
	}
}
