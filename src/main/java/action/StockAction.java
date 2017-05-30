package action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import businessLogicService.GetKGraphDataService;
import businessLogicService.GetStockBLService;
import po.StockPO;
import po.StockUpsAndDownsPO;

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
	
	public String upsAndDowns(){
	
		String date = stock.getDate();
		
		ArrayList<StockUpsAndDownsPO> upList = new ArrayList<>();
		ArrayList<StockUpsAndDownsPO> downList = new ArrayList<>();
		StockUpsAndDownsPO up = new StockUpsAndDownsPO();
		StockUpsAndDownsPO down = new StockUpsAndDownsPO();
		up.setName("深发展A");
		up.setAmplitude("12%");
		upList.add(up);
		down.setName("深发展B");
		down.setAmplitude("-10%");
		downList.add(down);
		
		if(upList!=null && upList.size()>0){
			session.setAttribute("upStock_list", upList);
		}
		if(downList!=null && downList.size()>0){
			session.setAttribute("downStock_list", downList);
		}
		
		return "upsAndDowns_success";
	}
	
	public String getUpsAndDownsGraph() throws ParseException{
		
		String date = request.getParameter("date");
		int[] data;
//		if(date == null  || date.equals("")){
//			data = getStockBLService.getDistributionOfUpsAndDowns("03/01/05");
//		}else{
//			data = getStockBLService.getDistributionOfUpsAndDowns("03/01/05");
//		}
		
		JSONObject json = new JSONObject();
//		json.put("data", data);
		result = json.toString();
		return SUCCESS;
	}
	
	public String getKGraph(){
		
		String beginDate = request.getParameter("begindate"); 
		String endDate = request.getParameter("enddate"); 
		String code = request.getParameter("code"); 
		
//		System.out.println(code);
		Map<String, ArrayList<String>> KData = new HashMap<>();
		if(beginDate == null || beginDate.equals("")){
			if(code == null || code.equals("")){
				KData = getKGraphDataService.getKData("600649","2005-04-28","2005-04-29");
			}else{
//				System.out.println(code);
				KData = getKGraphDataService.getKData(code, null, null);
			}
			
		}else{
			if(code == null || code.equals("")){
				KData = getKGraphDataService.getKData("600649", beginDate, endDate);
			}else{
				KData = getKGraphDataService.getKData("600649", null, null);
			}
		}

		ArrayList<String> date = KData.get("KDate");
		ArrayList<String> open = KData.get("KOpen");
		ArrayList<String> close = KData.get("KClose");
		ArrayList<String> lowest = KData.get("KLowest");
		ArrayList<String> highest = KData.get("KHighest");
		ArrayList<String> volumn = KData.get("KVolumn");
		
		int days = KData.get("KDate").size();
		System.out.println(days);
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
		json.put("LName", "平安银行");
		json.put("LCode", "1");
		json.put("LClose", KClose[days-2]);
		json.put("LOpen", KOpen[days-1]);
		json.put("LHighest",  KHighest[days-1]);
		json.put("LLowest",  KLowest[days-1]);
		json.put("LVolumn", KVolumn[days-1]);
		json.put("LMoney",  df.format(Double.parseDouble(KVolumn[days-1])*KClose[days-1]/10000));
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
			VSDatas = getStockBLService.getVSData("1", "2", "2005-02-01","2005-07-01");
			date = getStockBLService.getDate("1", "2", "2005-02-01","2005-07-01");
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
}
