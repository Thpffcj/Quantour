package businessLogic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import businessLogicService.GetPlateDataService;
import dataService.StockDataService;
import po.StockPO;

public class GetPlateData implements GetPlateDataService {
	private StockDataService stockDataService;

	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}

	public Map<String, String[]> GetPlate(String plate_type) {
		ArrayList<String> platelist = stockDataService.getPlate(plate_type);
		System.out.println(platelist);
		String[] index = new String[platelist.size()];
		String[] platename = new String[platelist.size()];
		String[] plate_fluctuation = new String[platelist.size()];
		String[] volume = new String[platelist.size()];
		String[] up_num = new String[platelist.size()];
		String[] down_num = new String[platelist.size()];
		String[] average_price = new String[platelist.size()];
		String[] max_code = new String[platelist.size()];
		String[] max_price = new String[platelist.size()];
		String[] max_fluctuation = new String[platelist.size()];
		for (int i = 0; i < platelist.size(); i++) {
			System.out.println(i);
			ArrayList<String> result = GetPlateMessage(plate_type, platelist.get(i));
			index[i] = Integer.toString(i + 1);
			platename[i] = platelist.get(i);
			plate_fluctuation[i] = result.get(0);
			volume[i] = result.get(1);
			up_num[i] = result.get(2);
			down_num[i] = result.get(3);
			average_price[i] = result.get(4);
			max_code[i] = result.get(5);
			max_price[i] = result.get(6);
			max_fluctuation[i] = result.get(7);
		}
		System.out.println("end");
		Map<String, String[]> platedata = new HashMap<>();
		platedata.put("index", index);
		platedata.put("name", platename);
		platedata.put("fluctuation", plate_fluctuation);
		platedata.put("volume", volume);
		platedata.put("up_num", up_num);
		platedata.put("down_num", down_num);
		platedata.put("price", average_price);
		platedata.put("max_code", max_code);
		platedata.put("max_price", max_price);
		platedata.put("max_fluctuation", max_fluctuation);
		return platedata;
	}

	public ArrayList<String> GetPlateMessage(String plate_type, String plate) {
		ArrayList<String> codelist = stockDataService.getCodeByPlate(plate_type, plate);
		System.out.println(plate+codelist);
		int up_num = 0;
		int down_num = 0;
		double plate_volume = 0.0;
		double plate_fluctuation = 0.0;
		double average_price = 0.0;
		String max_code = "";
		double max_price = 0.0;
		double max_fluctuation = -100.0;
		for (int i = 0; i < codelist.size(); i++) {
			ArrayList<StockPO> poList = stockDataService.getStockByCodeAndDate(Integer.valueOf(codelist.get(i)),
					"2017-05-23", "2017-05-25");
			if (poList.size() >= 2) {
				double fluctuation = (poList.get(0).getClose() - poList.get(1).getClose()) / poList.get(1).getClose();
				if (fluctuation > 0) {
					up_num++;
				} else if (fluctuation < 0) {
					down_num++;
				}
				plate_volume = plate_volume + Double.valueOf(poList.get(0).getVolume());
				plate_fluctuation = plate_fluctuation + Double.valueOf(poList.get(0).getVolume()) * fluctuation;
				average_price = average_price + poList.get(0).getClose();
				if (fluctuation > max_fluctuation) {
					max_code = poList.get(0).getCode();
					max_price = poList.get(0).getClose();
					max_fluctuation = fluctuation;
				}
			}
		}
		plate_fluctuation = plate_fluctuation / plate_volume;
		average_price = average_price / codelist.size();

		DecimalFormat df = new DecimalFormat("0.00");
		ArrayList<String> result = new ArrayList<>();
		result.add(df.format(plate_fluctuation * 100));
		result.add(Double.toString(plate_volume));
		result.add(Integer.toString(up_num));
		result.add(Integer.toString(down_num));
		result.add(df.format(average_price));
		result.add(max_code);
		result.add(df.format(max_price));
		result.add(df.format(max_fluctuation * 100));
		return result;
	}

}
