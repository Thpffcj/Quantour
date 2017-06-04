package businessLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import businessLogicService.MarkovForecastService;
import data.StockData;
import dataService.StockDataService;
import po.StockPO;

public class MarkovForecast implements MarkovForecastService{
	/**
	 * 预测未来股票的复权收盘价所属的价格区间
	 * @param Name
	 * @param Begin
	 * @param End
	 * @param ForecastDays
	 */
	public ArrayList<String> CloseValueForecast(String Name,String Begin,String End,int ForecastDays){
//		int E1; 复权收盘价<21.30
//		int E2; 21.30≤复权收盘价<22.80
//		int E3; 22.80≤复权收盘价<24.30
//		int E4; 复权收盘价≥24.30
		ArrayList<String> result1 = new ArrayList<String>();
		
		double [][] AdjClose = new double [4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				AdjClose[i][j]=0;
			}
		}
		
		double [][] Initial = new double [1][4];
		double [][] Result = new double [1][4];
		for(int i=0;i<1;i++){
			for(int j=0;j<4;j++){
				Initial[i][j]=0;
			}
		}
		
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		if (ISNumber(Name)) {
			int Code = Integer.valueOf(Name);
			stockList = sds.getStockByCodeAndDate(Code, Begin, End);
		} else {
			stockList = sds.getStockByNameAndDate(Name, Begin, End);
		}
		
		int length = stockList.size();
		ArrayList<Double> AdjCloseList = new ArrayList<Double>();
		for(int i=0;i<length;i++){
			AdjCloseList.add(stockList.get(i).getAdjClose());
		}
		
		if(AdjCloseList.get(length-1)<21.30){
			Initial[0][0]=1;
		}else if(AdjCloseList.get(length-1)>=21.30 && AdjCloseList.get(length-1)<22.80){
			Initial[0][1]=1;
		}else if(AdjCloseList.get(length-1)>=22.80 && AdjCloseList.get(length-1)<24.30){
			Initial[0][2]=1;
		}else{
			Initial[0][3]=1;
		}
		
		for(int i=0;i<length-1;i++){
			if(AdjCloseList.get(i)<21.30 && AdjCloseList.get(i+1)<21.30){
				AdjClose[0][0]++;
			}
			
			if(AdjCloseList.get(i)<21.30 && (AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80)){
				AdjClose[0][1]++;
			}
			
			if(AdjCloseList.get(i)<21.30 && (AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30)){
				AdjClose[0][2]++;
			}
			
			if(AdjCloseList.get(i)<21.30 && AdjCloseList.get(i+1)>=24.30){
				AdjClose[0][3]++;
			}
			
			if((AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80) && AdjCloseList.get(i+1)<21.30){
				AdjClose[1][0]++;
			}
			
			if((AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80) && (AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80)){
				AdjClose[1][1]++;
			}
			
			if((AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80) && (AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30)){
				AdjClose[1][2]++;
			}
			
			if((AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80) && AdjCloseList.get(i+1)>=24.30){
				AdjClose[1][3]++;
			}
			
			if((AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30) && AdjCloseList.get(i+1)<21.30){
				AdjClose[2][0]++;
			}
			
			if((AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30) && (AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80)){
				AdjClose[2][1]++;
			}
			
			if((AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30) && (AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30)){
				AdjClose[2][2]++;
			}
			
			if((AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30) && AdjCloseList.get(i)>=24.30){
				AdjClose[2][3]++;
			}
			
			if(AdjCloseList.get(i)>=24.30 && AdjCloseList.get(i+1)<21.30){
				AdjClose[3][0]++;
			}
			
			if(AdjCloseList.get(i)>=24.30 && (AdjCloseList.get(i+1)>=21.30 && AdjCloseList.get(i+1)<22.80)){
				AdjClose[3][1]++;
			}
			
			if(AdjCloseList.get(i)>=24.30 && (AdjCloseList.get(i+1)>=22.80 && AdjCloseList.get(i+1)<24.30)){
				AdjClose[3][2]++;
			}
			
			if(AdjCloseList.get(i)>=24.30 && AdjCloseList.get(i)>=24.30){
				AdjClose[3][3]++;
			}
		}
		
		double [][] TransitionProbability = new double [4][4];
		double [][] temp = new double [4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				TransitionProbability[i][j]=AdjClose[i][j]/(AdjClose[i][0]+AdjClose[i][1]+AdjClose[i][2]+AdjClose[i][3]);
			}
		}

		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				if(i==j)
					temp[i][j]=1;
				else
					temp[i][j]=0;
			}
		}
		
		for(int i=0;i<ForecastDays;i++){
			temp=MatrixMul1(TransitionProbability, temp);
			Result=MatrixMul2(Initial, temp);
			int Final=FindMax1(Result);
			
			int a=i+1;
			if(Final==0){
				result1.add(a + " " +"null"+ " " + "21.30");
			}else if(Final==1){
				result1.add(a + " " +"21.30"+ " " + "22.80");
			}else if(Final==2){
				result1.add(a + " " +"22.80"+ " " + "23.40");
			}else{
				result1.add(a + " " +"23.40"+ " " + "null");
			}
		}
		
		return result1;
	}
	
	/**
	 * 预测未来股票复权收盘价的上升或下降趋势
	 * @param Name
	 * @param Begin
	 * @param End
	 * @param ForecastDays
	 */
	public ArrayList<String> UpAndDownForecast(String Name,String Begin,String End,int ForecastDays){
		int E1=0;//上升状态的日期数
		int E2=0;//持平状态的日期数
		int E3=0;//下降状态的日期数
		
		ArrayList<String> result2 = new ArrayList<String>();
		
		double [][] Status = new double [3][3];
		double [][] temp = new double [3][3];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				Status[i][j]=0;
			}
		}
		temp=Status;
		temp[0][0]=1;
		temp[1][1]=1;
		temp[2][2]=1;
		
		double [][] Initial = new double [1][3];
		double [][] Result = new double [1][3];
		for(int i=0;i<1;i++){
			for(int j=0;j<3;j++){
				Initial[i][j]=0;
			}
		}
		
		StockDataService sds = new StockData();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		if (ISNumber(Name)) {
			int Code = Integer.valueOf(Name);
			String Origin = GetOrigin(Code, Begin);
			stockList = sds.getStockByCodeAndDate(Code, Origin, End);
		} else {
			String Origin = GetOrigin(Name, Begin);
			stockList = sds.getStockByNameAndDate(Name, Origin, End);
		}
		
		int length = stockList.size();
		ArrayList<Double> AdjCloseList = new ArrayList<Double>();
		for(int i=0;i<length;i++){
			AdjCloseList.add(stockList.get(i).getAdjClose());
		}
		
		int [] DayStatus = new int [length-1];
		for(int i=0;i<length-1;i++){
			if(AdjCloseList.get(i)<AdjCloseList.get(i+1)){
				DayStatus[i] = 1;
			}else if(AdjCloseList.get(i)==AdjCloseList.get(i+1)){
				DayStatus[i] = 2;
			}else if(AdjCloseList.get(i)>AdjCloseList.get(i+1)){
				DayStatus[i] = 3;
			}
		}
		
		if(DayStatus[0]==1){
			Initial[0][0]=1;
		}else if(DayStatus[0]==2){
			Initial[0][1]=1;
		}else{
			Initial[0][2]=1;
		}
		for(int i=0;i<length-2;i++){
			if(DayStatus[i]==1){
				E1++;
			}else if(DayStatus[i]==2){
				E2++;
			}else{
				E3++;
			}
		}
		
		for(int i=0;i<length-2;i++){
			if(DayStatus[i]==1 && DayStatus[i+1]==1){
				Status[0][0]++;
			}
			
			if(DayStatus[i]==1 && DayStatus[i+1]==2){
				Status[0][1]++;
			}
			
			if(DayStatus[i]==1 && DayStatus[i+1]==3){
				Status[0][2]++;
			}
			
			if(DayStatus[i]==2 && DayStatus[i+1]==1){
				Status[1][0]++;
			}
			
			if(DayStatus[i]==2 && DayStatus[i+1]==2){
				Status[1][1]++;
			}
			
			if(DayStatus[i]==2 && DayStatus[i+1]==3){
				Status[1][2]++;
			}
			
			if(DayStatus[i]==3 && DayStatus[i+1]==1){
				Status[2][0]++;
			}
			
			if(DayStatus[i]==3 && DayStatus[i+1]==2){
				Status[2][1]++;
			}
			
			if(DayStatus[i]==3 && DayStatus[i+1]==3){
				Status[2][2]++;
			}
		}
		
		for(int i=0;i<3;i++){
			if(Status[0][i]!=0){
				Status[0][i]=Status[0][i]/E1;
			}
		}
		
		for(int i=0;i<3;i++){
			if(Status[1][i]!=0){
				Status[1][i]=Status[0][i]/E2;
			}
		}
		
		for(int i=0;i<3;i++){
			if(Status[2][i]!=0){
				Status[2][i]=Status[0][i]/E3;
			}
		}
		
		
		
		for(int i=0;i<ForecastDays;i++){
			temp=MatrixMul3(Status, temp);
			Result=MatrixMul4(Initial, temp);
			int Final=FindMax1(Result);
			
			int a=i+1;
			if(Final==0){
				result2.add(a+" "+"up");
			}else if(Final==1){
				result2.add(a+" "+"fair");
			}else if(Final==2){
				result2.add(a+" "+"down");
			}
		}
		
		return result2;
	}
	
	/**
	 * 判断一个字符串是否为数字
	 * 
	 * @param s
	 * @return 结果
	 */
	public boolean ISNumber(String s) {
		try {
			int n = Integer.valueOf(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 4*4矩阵乘法
	 * @param Pro1
	 * @param Pro2
	 * @return
	 */
	public double[][] MatrixMul1(double [][] Pro1,double [][] Pro2){
		double[][] result=new double[4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				result[i][j]=Pro1[i][0]*Pro2[0][j]+Pro1[i][1]*Pro2[1][j]+Pro1[i][2]*Pro2[2][j]+Pro1[i][3]*Pro2[3][j];
			}
		}
		return result;
	}
	
	/**
	 * 1*4矩阵与4*4矩阵的乘法
	 * @param Pro1
	 * @param Pro2
	 * @return
	 */
	public double[][] MatrixMul2(double [][] Pro1,double [][] Pro2){
		double[][] result=new double[1][4];
		for(int i=0;i<1;i++){
			for(int j=0;j<4;j++){
				result[i][j]=Pro1[i][0]*Pro2[0][j]+Pro1[i][1]*Pro2[1][j]+Pro1[i][2]*Pro2[2][j]+Pro1[i][3]*Pro2[3][j];
			}
		}
		return result;
	}
	
	/**
	 * 找到1*4矩阵中最大的一位数
	 * @param Pro
	 * @return
	 */
	public int FindMax1(double [][] Pro){
		int result=0;
		for(int i=0;i<3;i++){
			if(Pro[0][i]<Pro[0][i+1]){
				result=i+1;
			}else{
				result=i;
			}
		}
		return result;
	}
	
	/**
	 * 得到上一个有效工作日
	 * 
	 * @param Begin
	 * @return 上一个有效工作日字符串表示
	 */
	public String GetOrigin(int Code,String Begin) {
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		String Origin = Begin;
		double volume;
		try {

			// SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			// String week = sdf.format(origin);
			//
			// if (week.equals("星期一")) {
			// Calendar cal = Calendar.getInstance();
			// cal.setTime(origin);
			// cal.add(Calendar.DATE, -3);
			// Origin = (new
			// SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
			// } else {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
				volume = sds.getVolumeByDateAndCode(Code, Origin);
			} while (volume == 0);

			// }

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Origin;
	}

	public String GetOrigin(String Name,String Begin) {
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("MM/dd/yy");
		String Origin = Begin;
		double volume;
		try {

			// SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			// String week = sdf.format(origin);
			//
			// if (week.equals("星期一")) {
			// Calendar cal = Calendar.getInstance();
			// cal.setTime(origin);
			// cal.add(Calendar.DATE, -3);
			// Origin = (new
			// SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
			// } else {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("MM/dd/yy")).format(cal.getTime());
				volume = sds.getVolumeByDateAndName(Name, Origin);
			} while (volume == 0);

			// }

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Origin;
	}
	
	/**
	 * 3*3矩阵乘法
	 * @param Pro1
	 * @param Pro2
	 * @return
	 */
	public double[][] MatrixMul3(double [][] Pro1,double [][] Pro2){
		double[][] result=new double[3][3];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				result[i][j]=Pro1[i][0]*Pro2[0][j]+Pro1[i][1]*Pro2[1][j]+Pro1[i][2]*Pro2[2][j];
			}
		}
		return result;
	}
	
	/**
	 * 1*3矩阵与3*3矩阵的乘法
	 * @param Pro1
	 * @param Pro2
	 * @return
	 */
	public double[][] MatrixMul4(double [][] Pro1,double [][] Pro2){
		double[][] result=new double[1][3];
		for(int i=0;i<1;i++){
			for(int j=0;j<3;j++){
				result[i][j]=Pro1[i][0]*Pro2[0][j]+Pro1[i][1]*Pro2[1][j]+Pro1[i][2]*Pro2[2][j];
			}
		}
		return result;
	}
	
	/**
	 * 找到1*3矩阵中最大的一位数
	 * @param Pro
	 * @return
	 */
	public int FindMax2(double [][] Pro){
		int result=0;
		for(int i=0;i<2;i++){
			if(Pro[0][i]<Pro[0][i+1]){
				result=i+1;
			}else{
				result=i;
			}
		}
		return result;
	}
}
