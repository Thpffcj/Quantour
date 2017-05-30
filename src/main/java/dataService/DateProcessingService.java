package dataService;

import java.text.ParseException;
import java.util.ArrayList;

public interface DateProcessingService {

	public ArrayList<String> splitDays(String beginDate, String endDate) throws ParseException;
	
	public String count(String endDate, int offset);
}
