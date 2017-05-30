import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.junit.Test;

import businessLogic.GetKGraphData;
import businessLogicService.GetKGraphDataService;
import po.StockPO;

public class GetAverageDataTest {

	GetKGraphDataService gkd = new GetKGraphData();
	
	@Test
	public void test() {
		
//		TimeSeriesCollection timeSeriesCollection = gkd.getAverageData("1", "1/20/14", "4/29/14");
//		System.out.println(timeSeriesCollection.getSeriesCount());
	}

}
