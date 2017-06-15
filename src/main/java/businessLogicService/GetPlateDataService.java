package businessLogicService;

import java.util.ArrayList;
import java.util.Map;

public interface GetPlateDataService {
	
	public Map<String, String[]> GetPlate(String page,String plate_type);
	
	public ArrayList<String> GetPlateMessage(String plate_type,String plate);

	public Map<String, ArrayList<String>> getPlateKGraphData(String plate_type,String plate);
	
	public Map<String, String[]> getChildrenMessage(String page,String plate_type,String plate);
	
	public String[] GetMatchPlate(String enter, String plate_type);
	
	public boolean IsLegalPlate(String enter, String plate_type);
}
