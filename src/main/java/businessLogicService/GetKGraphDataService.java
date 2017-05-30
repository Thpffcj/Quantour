package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

public interface GetKGraphDataService {

	public Map<String, ArrayList<String>> getKData(String condition, String beginDate, String endDate);
	
}
