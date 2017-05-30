import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import businessLogicService.GetRSIDataService;

public class GetRSIData {

	@Test
	public void test() {
		GetRSIDataService rsi = new businessLogic.GetRSIData();
		Map<String, ArrayList<String>> data = new HashMap<>();
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> value = new ArrayList<>();
		
		data = rsi.getRSIGraphData("1", "3/1/05","7/6/05");
		date = data.get("date");
		value = data.get("value");
		System.out.println(date.get(0));
	}

}
