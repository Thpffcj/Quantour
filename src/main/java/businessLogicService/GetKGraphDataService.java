package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

public interface GetKGraphDataService {

	public Map<String, ArrayList<String>> getKData(String condition, String beginDate, String endDate);
	
	public String getNameByCode(String code);

	public String getCodeByName(String name);
	
	public boolean IsLegalCode(String code);
	
	public Map<String, String[]> getMatchList(String enter);
}
