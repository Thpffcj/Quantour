package businessLogicService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import vo.quantify.DistributionHistogramVO;
import vo.quantify.MeanReturnRateVO;
import vo.quantify.MeanReversionVO;

public interface MeanReversionService {

	public Map<String, ArrayList<String>> getMeanReversionGraphData(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end) throws ParseException;

	public DefaultCategoryDataset GetMeanReturnRateGraphData(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin,String end);

	public DefaultCategoryDataset GetMeanWinningPercentageGraphData(String section, ArrayList<String> stockPool, int shares, int holdPeriod, int formingPeriod, String begin, String end);
	
	public DefaultCategoryDataset GetDistributionHistogramGraphData(String section, ArrayList<String> stockPool, int shares,
			int holdPeriod, int formingPeriod, String begin, String end);
	
	//获得年化收益率,基准年化收益率,最大回撤,阿尔法,贝塔,夏普比率
	public MeanReversionVO getParameter();
	
	//获得相对强弱计算周期,获得超额收益,获得1年内胜率
	public ArrayList<MeanReturnRateVO> getCalculationCycle();
	
	//获得正收益次数,负收益次数,胜率
	public DistributionHistogramVO getDistributionHistogram();
	
}
