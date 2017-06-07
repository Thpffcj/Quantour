package businessLogic;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.web.jsf.FacesContextUtils;

import businessLogicService.GetPlateDataService;
import dataService.StockDataService;
import po.StockPO;
import vo.WebCookieVO;

public class GetPlateData implements GetPlateDataService {
	private StockDataService stockDataService;

	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}

	public Map<String, String[]> GetPlate(String page,String plate_type) {
		ArrayList<String> platelist = stockDataService.getPlate(plate_type);
		System.out.println(platelist);
		int n1 = Integer.valueOf(page)*10;
		int n2 = 0;
		if((n1+10)>platelist.size()){
			n2 = platelist.size();
		}else{
			n2 = n1+10;
		}
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
		int k = 0;
		for (int i = n1; i < n2; i++) {
			System.out.println(i);
			ArrayList<String> result = GetPlateMessage(plate_type, platelist.get(i));
			index[k] = Integer.toString(i + 1);
			platename[k] = platelist.get(i);
			plate_fluctuation[k] = result.get(0);
			volume[k] = result.get(1);
			up_num[k] = result.get(2);
			down_num[k] = result.get(3);
			average_price[k] = result.get(4);
			max_code[k] = result.get(5);
			max_price[k] = result.get(6);
			max_fluctuation[k] = result.get(7);
			k++;
		}
		String[] length = new String[1];
		if(platelist.size()%10==0){
			length[0] = Integer.toString(platelist.size()/10);
		}else{
			length[0] = Integer.toString(platelist.size()/10+1);
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
		platedata.put("length", length);
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
		result.add(df.format(plate_volume/10000));
		result.add(Integer.toString(up_num));
		result.add(Integer.toString(down_num));
		result.add(df.format(average_price));
		result.add(max_code);
		result.add(df.format(max_price));
		result.add(df.format(max_fluctuation * 100));
		return result;
	}
	
	public Map<String, ArrayList<String>> getPlateKGraphData(String plate_type,String plate){
		ArrayList<String> codelist = stockDataService.getCodeByPlate(plate_type, plate);
		LocalDate begindate = LocalDate.of(2017, 5, 2);
		LocalDate enddate = LocalDate.of(2017, 5, 25);
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		ArrayList<String> KDate = new ArrayList<>();
		ArrayList<String> KOpen = new ArrayList<>();
		ArrayList<String> KHigh = new ArrayList<>();
		ArrayList<String> KLow = new ArrayList<>();
		ArrayList<String> KClose = new ArrayList<>();
		ArrayList<String> KVolume = new ArrayList<>();
		double open = 0.0;
		double high = 0.0;
		double low = 100.0;
		double close = 0.0;
		double volume = 0.0;
		while(begindate.isBefore(enddate)||begindate.isEqual(enddate)){
			for(int i=0;i<codelist.size();i++){
				ArrayList<StockPO> list = stockDataService.getStockByCodeAndDate(Integer.valueOf(codelist.get(i)), 
						begindate.toString(), begindate.toString());
				if(list.size()>0){
					open = open+list.get(0).getOpen()*Double.valueOf(list.get(0).getVolume());
					high = high+list.get(0).getHigh()*Double.valueOf(list.get(0).getVolume());
					low = low+list.get(0).getLow()*Double.valueOf(list.get(0).getVolume());
					close = close+list.get(0).getClose()*Double.valueOf(list.get(0).getVolume());
					volume = volume+Double.valueOf(list.get(0).getVolume());
				}
			}
			if(volume!=0.0){
				KDate.add(begindate.toString());
				KOpen.add(df.format(open/volume));
				KHigh.add(df.format(high/volume));
				KLow.add(df.format(low/volume));
				KClose.add(df.format(close/volume));
				KVolume.add(df.format(volume));
				open = 0.0;
				high = 0.0;
				low = 100.0;
				close = 0.0;
				volume = 0.0;
			}
			System.out.println(begindate);
			begindate = begindate.plusDays(1);
		}
		
		Map<String, ArrayList<String>> result = new HashMap<>();
		result.put("KDate", KDate);
		result.put("KOpen", KOpen);
		result.put("KHigh", KHigh);
		result.put("KLow", KLow);
		result.put("KClose", KClose);
		result.put("KVolume", KVolume);
		return result;
	}
	
	public Map<String, String[]> getChildrenMessage(String page,String plate_type,String plate){
		Map<String, String[]> result = new HashMap<>();
		ArrayList<String> codelist = stockDataService.getCodeByPlate(plate_type, plate);
		int n1 = Integer.valueOf(page)*10;
		int n2 = 0;
		if((n1+10)>codelist.size()){
			n2 = codelist.size();
		}else{
			n2 = n1+10;
		}
		String[] index = new String[codelist.size()];
		String[] code = new String[codelist.size()];
		String[] name = new String[codelist.size()];
		String[] open = new String[codelist.size()];
		String[] close =new String[codelist.size()];
		String[] high = new String[codelist.size()];
		String[] low = new String[codelist.size()];
		String[] fluct = new String[codelist.size()];
		String[] volume = new String[codelist.size()];
		int k = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i=n1;i<n2;i++){
			index[k] = Integer.toString(i+1);
			code[k] = codelist.get(i);
			name[k] = stockDataService.getNameByCode(Integer.valueOf(code[k]));
			ArrayList<StockPO> po = stockDataService.getStockByCodeAndDate(Integer.valueOf(code[k]), "2017-05-25", "2017-05-25");
			if(po.size()>0){
				open[k] = df.format(po.get(0).getOpen());
				high[k] = df.format(po.get(0).getHigh());
				low[k] = df.format(po.get(0).getLow());
				close[k] = df.format(po.get(0).getClose());
				String yesterday = time.format(getDayBefore("2017-05-25", Integer.valueOf(code[k])));
				if(yesterday.equals("2017-05-25")){
					fluct[k] = "- -";
				}else{
					ArrayList<StockPO> pos = stockDataService.getStockByCodeAndDate(Integer.valueOf(code[k]), yesterday, yesterday);
					fluct[k] = df.format((po.get(0).getClose()-pos.get(0).getClose())/pos.get(0).getClose()*100);
				}
				volume[k] = po.get(0).getVolume();
			}else{
				open[k] = "- -";
				close[k] = "- -";
				high[k] = "- -";
				low[k] = "- -";
				fluct[k] = "- -";
				volume[k] = "- -";
			}
			k++;
		}
		result.put("index", index);
		result.put("code", code);
		result.put("name", name);
		result.put("open", open);
		result.put("high", high);
		result.put("low", low);
		result.put("close", close);
		result.put("fluct", fluct);
		result.put("volume", volume);
		return result;
	}
	
	private Date getDayBefore(String today, int code) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		if(code==603580||code==2876){
			try {
				return time.parse(today);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String yesterday = today;
		Date dayBefore = new Date();
		double volume;
		try {
			do {
				dayBefore = time.parse(yesterday);
				Calendar cal = Calendar.getInstance();
				cal.setTime(dayBefore);
				cal.add(Calendar.DATE, -1);
				yesterday = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
				dayBefore = time.parse(yesterday);
				volume = stockDataService.getVolumeByDateAndCode(code, yesterday);
			} while (volume == 0.0);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayBefore;
	}
	
	public static void main(String[] args){
		System.out.println(LocalDate.now().plusDays(1).toString());
		System.out.println();
	}

}
