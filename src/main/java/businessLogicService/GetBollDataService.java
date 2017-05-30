package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;

import vo.quantify.MeanReversionVO;

public interface GetBollDataService {

	public Map<String, ArrayList<String>> getBollData(String condition, String begin, String end);
	
	public Map<String, ArrayList<String>> getAverageData(String Code, String Begin, String End);
	
	public DefaultCategoryDataset GetBollBackTestGraphData(String section, ArrayList<String> stockPool, String begin, String end);
	
	public MeanReversionVO getParameter();
	
	public ArrayList<String> getSuggest();
}
