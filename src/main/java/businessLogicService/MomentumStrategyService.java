package businessLogicService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import vo.quantify.DistributionHistogramVO;
import vo.quantify.MeanReturnRateVO;
import vo.quantify.MeanReversionVO;

public interface MomentumStrategyService {

	public Map<String, ArrayList<String>> getMStrategyComparedGraph(String Begin, String End, int existTime, int holdTime) throws ParseException;

	public Map<String, ArrayList<String>> getMStrategyWinningGraph(boolean isHold, int Time, String Begin, String End) throws ParseException;

	public Map<String, ArrayList<String>> getMStrategyExtraProfitGraph(boolean isHold, int Time, String Begin, String End) throws ParseException;

	public DefaultCategoryDataset getMStrategyYieldGraph(String Begin, String End, int holdTime, int existTime) throws ParseException;

	//获得年化收益率,基准年化收益率,最大回撤,阿尔法,贝塔,夏普比率
	public MeanReversionVO getParameter();
		
	//获得相对强弱计算周期,获得超额收益,获得1年内胜率
	public ArrayList<MeanReturnRateVO> getCalculationCycle();
		
	//获得正收益次数,负收益次数,胜率
	public DistributionHistogramVO getDistributionHistogram();
}
