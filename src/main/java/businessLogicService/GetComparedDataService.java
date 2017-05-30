package businessLogicService;

import org.jfree.data.category.DefaultCategoryDataset;

public interface GetComparedDataService {
	
	public DefaultCategoryDataset getLowestValue(String Name1, String Name2, String Begin, String End);
	
	public String GetLowest(String Name1, String Name2, String Begin, String End);
	
	public DefaultCategoryDataset getHighestValue(String Name1, String Name2, String Begin, String End);
	
	public String GetHighest(String Name1, String Name2, String Begin, String End);
	
	public DefaultCategoryDataset getRoseAndDropValue(String Name1, String Name2, String Begin, String End);
	
	public String getRoseAndDrop(String Name1, String Name2, String Begin, String End);
	
	public DefaultCategoryDataset getCloseValue(String Name1, String Name2, String Begin, String End);
	
	public DefaultCategoryDataset getRateOfReturn(String Name1, String Name2, String Begin, String End);
	
	public String getVariance(String Name1, String Name2, String Begin, String End);
}
