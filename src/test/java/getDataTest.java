import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import data.StockData;
import dataService.StockDataService;
import po.StockPO;

public class getDataTest {

	@Test
	public void test() {
		ArrayList<StockPO> stocklist = new ArrayList<StockPO>();
		StockDataService sd = new StockData();
		stocklist = sd.getStockByCodeAndDate(1, "1/20/14", "4/29/14");
		for(int i=0; i<stocklist.size(); i++){
			System.out.println(stocklist.get(i).getVolume());
		}
	}

}
