package businessLogic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ParameterCalculation {

	public String getMaxDrawdownLevel(ArrayList<Double> valueCollection){
		DecimalFormat df = new DecimalFormat("0.00%");
		if(valueCollection.size() == 0){
			return "0";
		}
		double diff = 0;
		double max = valueCollection.get(0);
		for (int temp = 1; temp < valueCollection.size(); temp++) {
			if (valueCollection.get(temp) - max < diff)
				diff = valueCollection.get(temp) - max;
			if (valueCollection.get(temp) > max)
				max = valueCollection.get(temp);
		}
//		System.out.println("最大回撤率max=" + (diff / max) * 100);
		return df.format(-diff);
	}

	/**
	 * 计算数据的标准差
	 * @param data
	 * @return
	 */
	public double getStandardDeviation(ArrayList<Double> data){
		double sum = 0;
		double avg = 0;
		double variance = 0;
		for(int i=0; i<data.size(); i++){
			sum += data.get(i);
		}
		avg = sum/data.size();
		for(int i=0; i<data.size(); i++){
			variance += Math.pow((data.get(i)-avg), 2);
		}
		variance = variance/data.size();
		return Math.sqrt(variance);
	}
	
	public double getBetaCoefficient(ArrayList<Double> marketIncome, ArrayList<Double> strategicIncome){
		
		if(marketIncome.size() == 0 || strategicIncome.size() == 0 || marketIncome.size() == 1 || strategicIncome.size() == 1){
			return 0.0;
		}
		
//		for(int i=0; i<marketIncome.size(); i++){
//			System.out.println(marketIncome.get(i));
//		}
//		
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		
		double beta;
		//市场收益的方差
		double variance = 0;
		//策略收益和市场收益的协方差
		double covariance = 0;
		double marketMean = 0;
		double strategMean = 0;
		
		//计算市场收益和策略收益的均值
		for(int i=0; i<Math.min(strategicIncome.size(), marketIncome.size()); i++){
			marketMean += marketIncome.get(i);
			strategMean += strategicIncome.get(i);
		}
		marketMean = marketMean/marketIncome.size();
		strategMean = strategMean/strategicIncome.size();
		
		//计算市场收益的方差,策略收益和市场收益的协方差
		for(int i=0; i<Math.min(strategicIncome.size(), marketIncome.size()); i++){
			variance += Math.pow((marketIncome.get(i)-marketMean), 2);
			covariance += (marketIncome.get(i)-marketMean)*(strategicIncome.get(i)-marketMean);
		}
		variance = variance/(marketMean/marketIncome.size()-1);
		covariance = covariance/(marketMean/marketIncome.size()-1);
//		System.out.println(covariance+" "+variance);
		String temp = numberFormat.format(covariance/variance);
//		System.out.println(temp);
		beta = Double.parseDouble(temp);
		return beta;
	}
	
	public String getAlphaCoefficient(double yearRateOfReturn, ArrayList<Double> marketIncome, ArrayList<Double> strategicIncome){
		DecimalFormat df = new DecimalFormat("0.00%");
		//无风险利率
		double r = 0.0325;
		double R = 0;
		for(int i=0; i<marketIncome.size(); i++){
			R += marketIncome.get(i);
		}
		R = R/marketIncome.size();
		double beta = getBetaCoefficient(marketIncome, strategicIncome);
		double Alpha = (yearRateOfReturn-r)-beta*(R-r);
		return df.format(Alpha);
	}
	
	public double getSharpeRatio(ArrayList<Double> strategicIncome){
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		double temp = 0;
		//无风险利率
		double r = 0.0325;
		double standardDeviation = getStandardDeviation(strategicIncome);
		for(int i=0; i<strategicIncome.size(); i++){
			temp += strategicIncome.get(i);
		}
		temp = temp/strategicIncome.size();
		double result = (temp - r)/standardDeviation;
		return Double.parseDouble(numberFormat.format(result));
	}

}
