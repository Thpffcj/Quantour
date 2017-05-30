package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;

import vo.quantify.MeanReversionVO;

public interface GetRSIDataService {

	public Map<String, ArrayList<String>> getRSIGraphData(String condition, String begin, String end);
	
	public DefaultCategoryDataset GetRSIBackTestGraphData(String section, ArrayList<String> stockPool, String begin, String end);
	
	public MeanReversionVO getParameter();
	
	public ArrayList<String> getSuggest();
}
