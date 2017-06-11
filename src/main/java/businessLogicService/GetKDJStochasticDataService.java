package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import vo.quantify.MeanReversionVO;

public interface GetKDJStochasticDataService {

	public Map<String, ArrayList<String>> getKDJStochasticData(String condition, String begin, String end);
	
	public DefaultCategoryDataset GetKDJStochasticBackTestGraphData(String section, ArrayList<String> stockPool, String begin, String end);

	public MeanReversionVO getParameter();
	
	public String[] getSuggest();
}
