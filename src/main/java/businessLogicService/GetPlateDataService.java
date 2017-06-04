package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

public interface GetPlateDataService {
	
	public Map<String, String[]> GetPlate(String plate_type);
	
	public ArrayList<String> GetPlateMessage(String plate_type,String plate);

}
