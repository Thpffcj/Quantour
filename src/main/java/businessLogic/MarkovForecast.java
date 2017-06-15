package businessLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import businessLogicService.MarkovForecastService;
import data.StockData;
import dataService.StockDataService;
import po.StockPO;

public class MarkovForecast implements MarkovForecastService{
	
	//注入股票查询的Dao
	private StockDataService stockDataService;
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}
	
	/**
	 * 预测未来股票的复权收盘价所属的价格区间
	 * @param Name
	 * @param Begin
	 * @param End
	 * @param ForecastDays
	 */
	public Map<String, ArrayList<Double>> CloseValueForecast(String Name,String Begin,String End,int ForecastDays){		
		
		Map<String, ArrayList<Double>> data = new HashMap<>();
		
		ArrayList<Double> up = new ArrayList<Double>();
		ArrayList<Double> down = new ArrayList<Double>();
		
		double [][] AdjClose = new double [8][8];
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				AdjClose[i][j]=0;
			}
		}
		
		double [][] Initial = new double [1][8];
		double [][] Result = new double [1][8];
		//初始化初始状态向量
		for(int i=0;i<1;i++){
			for(int j=0;j<8;j++){
				Initial[i][j]=0;
			}
		}
		
//		StockDataService stockDataService = new StockData();
		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
		if (ISNumber(Name)) {
			int Code = Integer.valueOf(Name);
			stockList = stockDataService.getStockByCodeAndDate(Code, Begin, End);
		} else {
			stockList = stockDataService.getStockByNameAndDate(Name, Begin, End);
		}
		
		int length = stockList.size();
		ArrayList<Double> AdjCloseList = new ArrayList<Double>();
		for(int i=0;i<length;i++){
			AdjCloseList.add(stockList.get(i).getAdjClose());
		}
		
		Collections.reverse(AdjCloseList);
		
//		for(int i=0;i<length;i++){
//			System.out.println("-"+AdjCloseList.get(i));
//		}
		
		double[] sortedList = new double[length];
		sortedList = Sort(AdjCloseList);
		int range = length/8;
		if(range>0){
		double Q1=(sortedList[range-1]+sortedList[range])/2;
		double Q0=2*sortedList[0]-Q1;
		range=(2*length)/8;
		double Q2=(sortedList[range-1]+sortedList[range])/2;
		range=(3*length)/8;
		double Q3=(sortedList[range-1]+sortedList[range])/2;
		range=(4*length)/8;
		double Q4=(sortedList[range-1]+sortedList[range])/2;
		range=(5*length)/8;
		double Q5=(sortedList[range-1]+sortedList[range])/2;
		range=(6*length)/8;
		double Q6=(sortedList[range-1]+sortedList[range])/2;
		range=(7*length)/8;
		double Q7=(sortedList[range-1]+sortedList[range])/2;
		double Q8=2*sortedList[length-1]-Q7;
		//确定收盘价区间范围
//		double minAdjClose,maxAdjClose;
//		minAdjClose=AdjCloseList.get(0);
//		maxAdjClose=AdjCloseList.get(0);
//		for(int i=0;i<length;i++){
//			if(AdjCloseList.get(i)>=maxAdjClose){
//				maxAdjClose=AdjCloseList.get(i);
//			}
//			
//			if(AdjCloseList.get(i)<=minAdjClose){
//				minAdjClose=AdjCloseList.get(i);
//			}
//		}
		
//		System.out.println("--------------"+maxAdjClose);
//		System.out.println("------"+minAdjClose);
		
//		double Q0=minAdjClose-(maxAdjClose-minAdjClose)/8;
//		double Q1=minAdjClose+(maxAdjClose-minAdjClose)/8;
//		double Q2=minAdjClose+(maxAdjClose-minAdjClose)/4;
//		double Q3=minAdjClose+((maxAdjClose-minAdjClose)/8)*3;
//		double Q4=minAdjClose+(maxAdjClose-minAdjClose)/2;
//		double Q5=minAdjClose+((maxAdjClose-minAdjClose)/8)*5;
//		double Q6=minAdjClose+((maxAdjClose-minAdjClose)/8)*6;
//		double Q7=minAdjClose+((maxAdjClose-minAdjClose)/8)*7;
//		double Q8=maxAdjClose+(maxAdjClose-minAdjClose)/8;
		
//		System.out.println("--------------"+length);
//		System.out.println("--------------"+Q1);
//		System.out.println("------"+Q2);
//		System.out.println("--------------"+Q3);
//		System.out.println("------"+Q4);
//		System.out.println("--------------"+Q5);
//		System.out.println("------"+Q6);
//		System.out.println("--------------"+Q7);
//		System.out.println("------"+Q8);
		
		if(AdjCloseList.get(length-1)<Q1){
			Initial[0][0]=1;
		}else if(AdjCloseList.get(length-1)>=Q1 && AdjCloseList.get(length-1)<Q2){
			Initial[0][1]=1;
		}else if(AdjCloseList.get(length-1)>=Q2 && AdjCloseList.get(length-1)<Q3){
			Initial[0][2]=1;
		}else if(AdjCloseList.get(length-1)>=Q3 && AdjCloseList.get(length-1)<Q4){
			Initial[0][3]=1;
		}else if(AdjCloseList.get(length-1)>=Q4 && AdjCloseList.get(length-1)<Q5){
			Initial[0][4]=1;
		}else if(AdjCloseList.get(length-1)>=Q5 && AdjCloseList.get(length-1)<Q6){
			Initial[0][5]=1;
		}else if(AdjCloseList.get(length-1)>=Q6 && AdjCloseList.get(length-1)<Q7){
			Initial[0][6]=1;
		}else{
			Initial[0][7]=1;
		}
		
//		for(int i=0;i<8;i++){
//			System.out.println("----"+Initial[0][i]);
//		}
		
		for(int i=0;i<length-1;i++){
			//数组第一行
			if(AdjCloseList.get(i)<Q1 && AdjCloseList.get(i+1)<Q1){
				AdjClose[0][0]++;
			}
			
			if(AdjCloseList.get(i)<Q1 && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[0][1]++;
			}
			
			if(AdjCloseList.get(i)<Q1 && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[0][2]++;
			}
			
			if(AdjCloseList.get(i)<Q1 && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[0][3]++;
			}
			
			if(AdjCloseList.get(i)<Q1 && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[0][4]++;
			}
			
			if(AdjCloseList.get(i)<Q1 && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[0][5]++;
			}
			
			if(AdjCloseList.get(i)<Q1 && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[0][6]++;
			}
			
			if(AdjCloseList.get(i)<Q1 && AdjCloseList.get(i+1)>=Q7){
				AdjClose[0][7]++;
			}
			
			//数组第二行
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && AdjCloseList.get(i+1)<Q1){
				AdjClose[1][0]++;
			}
			
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[1][1]++;
			}
			
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[1][2]++;
			}
			
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[1][3]++;
			}
			
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[1][4]++;
			}
			
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[1][5]++;
			}
			
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[1][6]++;
			}
			
			if((AdjCloseList.get(i)>=Q1 && AdjCloseList.get(i)<Q2) && AdjCloseList.get(i+1)>=Q7){
				AdjClose[1][7]++;
			}
			
			//数组第三行
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && AdjCloseList.get(i+1)<Q1){
				AdjClose[2][0]++;
			}
			
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[2][1]++;
			}
			
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[2][2]++;
			}
			
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[2][3]++;
			}
			
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[2][4]++;
			}
			
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[2][5]++;
			}
			
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[2][6]++;
			}
			
			if((AdjCloseList.get(i)>=Q2 && AdjCloseList.get(i)<Q3) && AdjCloseList.get(i+1)>=Q7){
				AdjClose[2][7]++;
			}
			
			//数组第四行
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && AdjCloseList.get(i+1)<Q1){
				AdjClose[3][0]++;
			}
			
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[3][1]++;
			}
			
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[3][2]++;
			}
			
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[3][3]++;
			}
			
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[3][4]++;
			}
			
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[3][5]++;
			}
			
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[3][6]++;
			}
			
			if((AdjCloseList.get(i)>=Q3 && AdjCloseList.get(i)<Q4) && AdjCloseList.get(i+1)>=Q7){
				AdjClose[3][7]++;
			}
			
			//数组第五行
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && AdjCloseList.get(i+1)<Q1){
				AdjClose[4][0]++;
			}
			
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[4][1]++;
			}
			
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[4][2]++;
			}
			
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[4][3]++;
			}
			
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[4][4]++;
			}
			
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[4][5]++;
			}
			
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[4][6]++;
			}
			
			if((AdjCloseList.get(i)>=Q4 && AdjCloseList.get(i)<Q5) && AdjCloseList.get(i+1)>=Q7){
				AdjClose[4][7]++;
			}
			
			//数组第六行
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && AdjCloseList.get(i+1)<Q1){
				AdjClose[5][0]++;
			}
			
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[5][1]++;
			}
			
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[5][2]++;
			}
			
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[5][3]++;
			}
			
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[5][4]++;
			}
			
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[5][5]++;
			}
			
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[5][6]++;
			}
			
			if((AdjCloseList.get(i)>=Q5 && AdjCloseList.get(i)<Q6) && AdjCloseList.get(i+1)>=Q7){
				AdjClose[5][7]++;
			}
			
			//数组第七行
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && AdjCloseList.get(i+1)<Q1){
				AdjClose[6][0]++;
			}
			
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[6][1]++;
			}
			
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[6][2]++;
			}
			
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[6][3]++;
			}
			
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[6][4]++;
			}
			
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[6][5]++;
			}
			
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[6][6]++;
			}
			
			if((AdjCloseList.get(i)>=Q6 && AdjCloseList.get(i)<Q7) && AdjCloseList.get(i+1)>=Q7){
				AdjClose[6][7]++;
			}
			
			//数组第八行
			if(AdjCloseList.get(i)>=Q7 && AdjCloseList.get(i+1)<Q1){
				AdjClose[7][0]++;
			}
			
			if(AdjCloseList.get(i)>=Q7 && (AdjCloseList.get(i+1)>=Q1 && AdjCloseList.get(i+1)<Q2)){
				AdjClose[7][1]++;
			}
			
			if(AdjCloseList.get(i)>=Q7 && (AdjCloseList.get(i+1)>=Q2 && AdjCloseList.get(i+1)<Q3)){
				AdjClose[7][2]++;
			}
			
			if(AdjCloseList.get(i)>=Q7 && (AdjCloseList.get(i+1)>=Q3 && AdjCloseList.get(i+1)<Q4)){
				AdjClose[7][3]++;
			}
			
			if(AdjCloseList.get(i)>=Q7 && (AdjCloseList.get(i+1)>=Q4 && AdjCloseList.get(i+1)<Q5)){
				AdjClose[7][4]++;
			}
			
			if(AdjCloseList.get(i)>=Q7 && (AdjCloseList.get(i+1)>=Q5 && AdjCloseList.get(i+1)<Q6)){
				AdjClose[7][5]++;
			}
			
			if(AdjCloseList.get(i)>=Q7 && (AdjCloseList.get(i+1)>=Q6 && AdjCloseList.get(i+1)<Q7)){
				AdjClose[7][6]++;
			}
			
			if(AdjCloseList.get(i)>=Q7 && AdjCloseList.get(i+1)>=Q7){
				AdjClose[7][7]++;
			}
		}
		
		//初始化转移状态矩阵
		double [][] TransitionProbability = new double [8][8];
		double [][] temp = new double [8][8];
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if((AdjClose[i][0]+AdjClose[i][1]+AdjClose[i][2]+AdjClose[i][3]+AdjClose[i][4]+AdjClose[i][5]+AdjClose[i][6]+AdjClose[i][7])==0){
					TransitionProbability[i][j]=0;
				}else{
					TransitionProbability[i][j]=AdjClose[i][j]/(AdjClose[i][0]+AdjClose[i][1]+AdjClose[i][2]+AdjClose[i][3]+AdjClose[i][4]+AdjClose[i][5]+AdjClose[i][6]+AdjClose[i][7]);
				}
			}
		}

//		for(int i=0;i<8;i++){
//			for(int j=0;j<8;j++){
//				System.out.println("-"+TransitionProbability[i][j]);
//			}
//		}
		
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
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
			
//			for(int k=0;k<8;k++){
//				for(int j=0;j<8;j++){
//					System.out.println("-"+temp[k][j]);
//				}
//			}
//			
//			for(int j=0;j<8;j++){
//				System.out.println("--"+Result[0][j]);
//			}
//			
//			System.out.println("---"+Final);
			
//			int a=i+1;
			if(Final==0){
//				result1.add(a + " " +0+ " " +Q1);
				down.add(Q0);
				up.add(Q1);
			}else if(Final==1){
//				result1.add(a + " " +Q1+ " " +Q2);
				down.add(Q1);
				up.add(Q2);
			}else if(Final==2){
//				result1.add(a + " " +Q2+ " " +Q3);
				down.add(Q2);
				up.add(Q3);
			}else if(Final==3){
//				result1.add(a + " " +Q3+ " " +Q4);
				down.add(Q3);
				up.add(Q4);
			}else if(Final==4){
//				result1.add(a + " " +Q4+ " " +Q5);
				down.add(Q4);
				up.add(Q5);
			}else if(Final==5){
//				result1.add(a + " " +Q5+ " " +Q6);
				down.add(Q5);
				up.add(Q6);
			}else if(Final==6){
//				result1.add(a + " " +Q6+ " " +Q7);
				down.add(Q6);
				up.add(Q7);
			}else{
//				result1.add(a + " " +Q7+ " " +Q8);
				down.add(Q7);
				up.add(Q8);
			}
		}
		}
		
		data.put("up", up);
		data.put("down", down);
		return data;
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
		
		//初始化初始状态向量
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
				Status[1][i]=Status[1][i]/E2;
			}
		}
		
		for(int i=0;i<3;i++){
			if(Status[2][i]!=0){
				Status[2][i]=Status[2][i]/E3;
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
	 * 排序算法
	 * @param list
	 * @return 储存排序结果的数组
	 */
	public double[] Sort(ArrayList<Double> list){
		int length=list.size();
		double [] temp = new double[length];
		double [] result = new double[length];
		for(int i=0;i<length;i++){
			temp[i]=list.get(i);
		}
		
		double temp1;
		for(int i=0;i<length;i++){
			for(int j=i;j<length;j++){
				if(temp[i]>temp[j]){
					temp1=temp[i];
					temp[i]=temp[j];
					temp[j]=temp1;
				}
			}
		}
		
		result=temp;
		return result;
	}
	/**
	 * 8*8矩阵乘法
	 * @param Pro1
	 * @param Pro2
	 * @return
	 */
	public double[][] MatrixMul1(double [][] Pro1,double [][] Pro2){
		double[][] result=new double[8][8];
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				result[i][j]=Pro1[i][0]*Pro2[0][j]+Pro1[i][1]*Pro2[1][j]+Pro1[i][2]*Pro2[2][j]+Pro1[i][3]*Pro2[3][j]+Pro1[i][4]*Pro2[4][j]+Pro1[i][5]*Pro2[5][j]+Pro1[i][6]*Pro2[6][j]+Pro1[i][7]*Pro2[7][j];
			}
		}
		return result;
	}
	
	/**
	 * 1*8矩阵与8*8矩阵的乘法
	 * @param Pro1
	 * @param Pro2
	 * @return
	 */
	public double[][] MatrixMul2(double [][] Pro1,double [][] Pro2){
		double[][] result=new double[1][8];
		for(int i=0;i<1;i++){
			for(int j=0;j<8;j++){
				result[i][j]=Pro1[i][0]*Pro2[0][j]+Pro1[i][1]*Pro2[1][j]+Pro1[i][2]*Pro2[2][j]+Pro1[i][3]*Pro2[3][j]+Pro1[i][4]*Pro2[4][j]+Pro1[i][5]*Pro2[5][j]+Pro1[i][6]*Pro2[6][j]+Pro1[i][7]*Pro2[7][j];
			}
		}
		return result;
	}
	
	/**
	 * 找到1*8矩阵中最大的一位数
	 * @param Pro
	 * @return
	 */
	public int FindMax1(double [][] Pro){
		int result=0;
		for(int i=0;i<8;i++){
			if(Pro[0][result]<Pro[0][i]){
				result=i;
			}
		}
		return result;
	}
	
	/**
	 * 得到上一个有效工作日
	 * @param Code
	 * @param Begin
	 * @return 上一个有效工作日字符串表示
	 */
	public String GetOrigin(int Code,String Begin) {
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
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
			// SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
			// } else {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
				volume = sds.getVolumeByDateAndCode(Code, Origin);
			} while (volume == 0);

			// }

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Origin;
	}

	/**
	 * 得到上一个有效工作日
	 * @param Name
	 * @param Begin
	 * @return 上一个有效工作日字符串表示
	 */
	public String GetOrigin(String Name,String Begin) {
		StockDataService sds = new StockData();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
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
			// SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
			// } else {
			do {
				Date origin = time.parse(Origin);
				Calendar cal = Calendar.getInstance();
				cal.setTime(origin);
				cal.add(Calendar.DATE, -1);
				Origin = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
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
		for(int i=0;i<3;i++){
			if(Pro[0][result]<Pro[0][i]){
				result=i;
			}
		}
		return result;
	}
}