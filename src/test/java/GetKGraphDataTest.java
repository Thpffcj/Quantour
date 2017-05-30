import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.junit.Test;

import businessLogic.GetKGraphData;
import businessLogic.GraphUtil;
import businessLogicService.GetKGraphDataService;
import data.StockData;
import dataService.StockDataService;
import po.StockPO;

public class GetKGraphDataTest {

	GetKGraphDataService gkd = new GetKGraphData();
	GraphUtil util = new GraphUtil();
	
//	@Test
//	public void test() {
//		ArrayList<StockPO> stockList = new ArrayList<StockPO>();
//		OHLCSeriesCollection oh = gkd.getKGraphData("1", "1/20/14", "4/29/14");
//		System.out.println(oh.getSeries(0));
//	}
	
	@Test
	public void testGetDateOfSuspension(){
		try {
			ArrayList<Date> dates = util.GetDateOfSuspension("1", "1/20/14", "4/29/14");
			for(int i=0; i<dates.size(); i++){
				System.out.println(dates.get(i));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
