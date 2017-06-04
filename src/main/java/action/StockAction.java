package action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.input.DemuxInputStream;
import org.json.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import businessLogicService.GetKGraphDataService;
import businessLogicService.GetStockBLService;
import po.StockNameAndUpsPO;
import po.StockPO;
import po.UpsAndDownsPO;
import vo.WebCookieVO;

public class StockAction extends SuperAction implements ModelDriven<StockPO> {

	private static final long serialVersionUID = 1L;
	
	private StockPO stock = new StockPO(); 
	public StockPO getModel() {
		return stock;
	}
	
	private GetKGraphDataService getKGraphDataService;
	public void setGetKGraphDataService(GetKGraphDataService getKGraphDataService) {
		this.getKGraphDataService = getKGraphDataService;
	}
	
	private GetStockBLService getStockBLService;
	public void setGetStockBLService(GetStockBLService getStockBLService) {
		this.getStockBLService = getStockBLService;
	}

	public String result;
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getUpsAndDownsGraph(){
		
		String date = request.getParameter("date");
		System.out.println(date);
		UpsAndDownsPO upsAndDownsPO = new UpsAndDownsPO();
		ArrayList<StockNameAndUpsPO> ups = new ArrayList<>();
		ArrayList<StockNameAndUpsPO> downs = new ArrayList<>();
		int[] data;
		
		if(date == null  || date.equals("")){
			upsAndDownsPO = getStockBLService.getDistributionOfUpsAndDowns("2015-05-05");
		}else{
			upsAndDownsPO = getStockBLService.getDistributionOfUpsAndDowns(date);
		}
		
		data = upsAndDownsPO.getUpsAndDowns();
		ups = upsAndDownsPO.getUps_Max();
		downs = upsAndDownsPO.getDowns_Max();
		
		int days = ups.size();
		String[] upName = new String[days];
		Double[] upAmplitude = new Double[days];
		String[] downName = new String[days];
		Double[] downAmplitude = new Double[days];
		for(int i=0; i<days; i++){
			upName[i] = ups.get(i).getName();
			upAmplitude[i] = ups.get(i).getUps();
			downName[i] = downs.get(i).getName();
			downAmplitude[i] = downs.get(i).getUps();
		}
		
		JSONObject json = new JSONObject();
		json.put("Data", data);
		json.put("UpName", upName);
		json.put("UpAmplitude", upAmplitude);
		json.put("DownName", downName);
		json.put("DownAmplitude", downAmplitude);
		result = json.toString();
		return SUCCESS;
	}
	
	public String getKGraph(){
		
		String beginDate = request.getParameter("begindate"); 
		String endDate = request.getParameter("enddate"); 
//		String code = request.getParameter("code");
		String code = WebCookieVO.getSearchCode();
		
//		System.out.println(code);
		Map<String, ArrayList<String>> KData = new HashMap<>();
		if(code == null || code.equals("")){
			KData = getKGraphDataService.getKData("000001", beginDate, endDate);
		}else{				
			KData = getKGraphDataService.getKData(code, beginDate, endDate);
		}

		ArrayList<String> date = KData.get("KDate");
		ArrayList<String> open = KData.get("KOpen");
		ArrayList<String> close = KData.get("KClose");
		ArrayList<String> lowest = KData.get("KLowest");
		ArrayList<String> highest = KData.get("KHighest");
		ArrayList<String> volumn = KData.get("KVolumn");
		
		int days = KData.get("KDate").size();
		String[] KDate = new String[days];
		Double[] KOpen = new Double[days];
		Double[] KClose = new Double[days];
		Double[] KLowest = new Double[days];
		Double[] KHighest = new Double[days];
		String[] KVolumn = new String[days];
		
		for(int i=0; i<days; i++){
			KDate[i] = date.get(i);
			KOpen[i] = Double.valueOf(open.get(i));
			KClose[i] = Double.valueOf(close.get(i));
			KLowest[i] = Double.valueOf(lowest.get(i));
			KHighest[i] = Double.valueOf(highest.get(i));
			KVolumn[i] = volumn.get(i);
		}
		
		DecimalFormat df = new DecimalFormat("0.00");
		JSONObject json = new JSONObject();
		json.put("LName", getKGraphDataService.getNameByCode(code));
		if(code==null||code.equals("")){
			json.put("LCode", "000001");
		}else{
			json.put("LCode", code);
		}
		json.put("LClose", KClose[days-2]);
		json.put("LOpen", KOpen[days-1]);
		json.put("LHighest",  KHighest[days-1]);
		json.put("LLowest",  KLowest[days-1]);
		json.put("LVolumn", KVolumn[days-1]);
		json.put("LMoney",  df.format(Double.parseDouble(KVolumn[days-1])*KClose[days-1]/10000));
		json.put("LFluct", df.format((KClose[days-1]-KClose[days-2])/KClose[days-2]*100));
		json.put("Date", KDate);
		json.put("Open", KOpen);
		json.put("Close", KClose);
		json.put("Lowest", KLowest);
		json.put("Highest", KHighest);
		json.put("Volumn", KVolumn);
		result = json.toString();
		return "success";
	}
	
	public String getVSGraph(){
		
		String beginDate = request.getParameter("begindate"); 
		String endDate = request.getParameter("enddate"); 
		String code1 = request.getParameter("code1"); 
		String code2 = request.getParameter("code2"); 
		
		Map<String, ArrayList<Double>> VSDatas = new HashMap<>();
		ArrayList<String> date = new ArrayList<>();
//		System.out.println(beginDate);
		if(beginDate == null  || beginDate.equals("")){
			VSDatas = getStockBLService.getVSData("1", "10", "2005-02-01","2005-07-01");
			date = getStockBLService.getDate("1", "10", "2005-02-01","2005-07-01");
		}else{
			VSDatas = getStockBLService.getVSData(code1, code2, beginDate, endDate);
			date = getStockBLService.getDate(code1, code2, beginDate, endDate);
		}
		
		ArrayList<Double> close1 = new ArrayList<>();
		ArrayList<Double> close2 = new ArrayList<>();
		
		String[] name = new String[2];
		Iterator it = VSDatas.entrySet().iterator();
		int n = 0;
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			name[n] = (String) entry.getKey();
			n++;
		}
		close1 = VSDatas.get(name[0]);
		close2 = VSDatas.get(name[1]);
		
		int days = date.size();
		String[] dates = new String[days];
		Double[] stock1 = new Double[days];
		Double[] stock2 = new Double[days];
		
		for(int i=0; i<days; i++){
			dates[i] = date.get(i);
			stock1[i] = close1.get(i);
			stock2[i] = close2.get(i);
		}
		
		JSONObject json = new JSONObject();
		json.put("Name", name);
		json.put("Date", dates);
		json.put("Stock1", stock1);
		json.put("Stock2", stock2);
		result = json.toString();
		return SUCCESS;
	}
	
	public String saveSearchCode(){
		String code = request.getParameter("code");
		WebCookieVO.setSearchCode(code);
		System.out.println(code);
		return "success";
	}
	
	public String getStockMessage(){
		ArrayList<StockPO> poList = getStockBLService.getStockMessage();
		System.out.println(poList.size());
		String[] index = new String[poList.size()];
		String[] code = new String[poList.size()];
		String[] name = new String[poList.size()];
		String[] open = new String[poList.size()];
		String[] high = new String[poList.size()];
		String[] low = new String[poList.size()];
		String[] close = new String[poList.size()];
		String[] fluctuation = new String[poList.size()];
		String[] volume = new String[poList.size()];
		
		for(int i=0;i<poList.size();i++){
			DecimalFormat df = new DecimalFormat("0.00");
			
			index[i] = Integer.toString(i+1);
			code[i] = poList.get(i).getCode();
			System.out.println("------------enter-------------");
			name[i] = getStockBLService.getNameByCode(code[i]);
			open[i] = df.format(poList.get(i).getOpen());
			high[i] = df.format(poList.get(i).getHigh());
			low[i] = df.format(poList.get(i).getLow());
			close[i] = df.format(poList.get(i).getClose());
			volume[i] = poList.get(i).getVolume();
			System.out.println("------------1end-------------");
			ArrayList<StockPO> lastday = getStockBLService.getLastStockByCode(code[i]);
			System.out.println("------------end-------------");
			System.out.println(i+" "+lastday);
			double fluct = (poList.get(i).getClose()-lastday.get(0).getClose())/lastday.get(0).getClose();
			fluctuation[i] = df.format(fluct*100);
		}
		
		JSONObject json = new JSONObject();
		json.put("index", index);
		json.put("code", code);
		json.put("name", name);
		json.put("open", open);
		json.put("high", high);
		json.put("low", low);
		json.put("close", close);
		json.put("fluctuation", fluctuation);
		json.put("volume", volume);
		result = json.toString();
		return "success";
	}
}
