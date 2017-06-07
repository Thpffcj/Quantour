package action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.plaf.synth.SynthSpinnerUI;

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
			upsAndDownsPO = getStockBLService.getDistributionOfUpsAndDowns("2017-05-25");
		}else{
			upsAndDownsPO = getStockBLService.getDistributionOfUpsAndDowns(date);
		}
		
		data = upsAndDownsPO.getUpsAndDowns();
		ups = upsAndDownsPO.getUps_Max();
		downs = upsAndDownsPO.getDowns_Max();
		
		int days = ups.size();
		String[] upName = new String[days];
		String[] upAmplitude = new String[days];
		String[] downName = new String[days];
		String[] downAmplitude = new String[days];
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i=0; i<days; i++){
			upName[i] = ups.get(i).getName();
			upAmplitude[i] = df.format(ups.get(i).getUps()*100)+"%";
			downName[i] = downs.get(i).getName();
			downAmplitude[i] = df.format(downs.get(i).getUps()*100)+"%";
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
		String code = WebCookieVO.getSearchCode();
		
		Map<String, ArrayList<String>> KData = new HashMap<>();
		if(code == null || code.equals("")){
			KData = getKGraphDataService.getKData("000001", beginDate, endDate);
		}else{				
			KData = getKGraphDataService.getKData(code, beginDate, endDate);
		}
		System.out.println("KData----"+KData);
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
			json.put("LCode", Tran(code));
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
	
	/**
	 * 得到股票对比图
	 * @return
	 */
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
	
	/**
	 * 获得版块基准收益率
	 * @return
	 */
	public String getBenchmark(){
		
		Map<String, ArrayList<String>> data = new HashMap<>();
		
		String type = request.getParameter("type");
		if(type.equals("hs300")){
			data = getStockBLService.getBenchmark("hs300");
		}else if(type.equals("399005")){
			data = getStockBLService.getBenchmark("399005");
		}else{
			data = getStockBLService.getBenchmark("399006");
		}
		
		ArrayList<String> date = new ArrayList<>();
		ArrayList<String> adj = new ArrayList<>();
		date = data.get("date");
		adj = data.get("adjClose");
		
//		System.out.println("date "+date.size());
		int days = date.size();
		String[] dates = new String[days];
		Double[] adjClose = new Double[days];
		
		for(int i=0; i<days; i++){
			dates[i] = date.get(i);
			adjClose[i] = Double.parseDouble(adj.get(i));
		}
//		System.out.println(dates[0]+" "+dates[1]);
		
		JSONObject json = new JSONObject();
		json.put("Date", dates);
		json.put("AdjClose", adjClose);
		result = json.toString();
		return SUCCESS;
	}
	
	/**
	 * 保存用户想要搜索的股票代码
	 * @return
	 */
	public String saveSearchCode(){
		String code = request.getParameter("code");
		String Code = code.replace(" ", "");
		JSONObject json = new JSONObject();
		
		if(Code.equals("")){
			json.put("result", "null");
			result = json.toString();
			return "success";
		}
		
		if(!IsCode(Code)){
			Code = Tran(getKGraphDataService.getCodeByName(Code));
		}
		
		if(Code.equals("000000")){
			json.put("result", "unknow");
		}else if(getKGraphDataService.IsLegalCode(Code)==false){
			json.put("result", "unknow");
		}else{
			WebCookieVO.setSearchCode(Code);
			System.out.println(Code);
			json.put("result", "success");
		}
		result = json.toString();
		return "success";
	}
	
	/**
	 * 根据输入的股票代码/名称的一部分来寻找匹配的股票列表
	 * @return
	 */
	public String getMatchList(){
		String enter = request.getParameter("enter");
		Map<String, String[]> matchlist = getKGraphDataService.getMatchList(enter);
		JSONObject json = new JSONObject();
		json.put("name", matchlist.get("name"));
		json.put("code", matchlist.get("code"));
		result = json.toString();
		return "success";
	}
	
	/**
	 * 得到当天交易股票信息
	 * @return
	 */
	public String getStockMessage(){
		String page = request.getParameter("page");
		System.out.println(page);
		ArrayList<StockPO> poList = getStockBLService.getStockMessage();
		
		int n1 = 0;
		int n2 = 0;
		if(page.equals("-1")){
			n1 = (WebCookieVO.getpage()-1)*10;
		}else if(page.equals("-2")){
			n1 = (WebCookieVO.getpage()+1)*10;
		}else{
			n1 = Integer.valueOf(page)*10;
		}
		WebCookieVO.setpage(n1/10);
		if((n1+10)>poList.size()){
			n2 = poList.size();
		}else{
			n2 = n1+10;
		}
		
		String[] index = new String[poList.size()];
		String[] code = new String[poList.size()];
		String[] name = new String[poList.size()];
		String[] open = new String[poList.size()];
		String[] high = new String[poList.size()];
		String[] low = new String[poList.size()];
		String[] close = new String[poList.size()];
		String[] fluctuation = new String[poList.size()];
		String[] volume = new String[poList.size()];
		int i = 0;
		for(int k=n1;k<n2;k++){
			DecimalFormat df = new DecimalFormat("0.00");
			
			index[i] = Integer.toString(k+1);
			code[i] = poList.get(k).getCode();
			name[i] = getStockBLService.getNameByCode(code[i]);
			open[i] = df.format(poList.get(k).getOpen());
			high[i] = df.format(poList.get(k).getHigh());
			low[i] = df.format(poList.get(k).getLow());
			close[i] = df.format(poList.get(k).getClose());
			volume[i] = poList.get(k).getVolume();
			ArrayList<StockPO> lastday = getStockBLService.getLastStockByCode(code[i]);
			System.out.println(k+" "+lastday);
			double fluct = (poList.get(k).getClose()-lastday.get(0).getClose())/lastday.get(0).getClose();
			fluctuation[i] = df.format(fluct*100);
			i++;
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
		int length = 0;
		if(poList.size()%10==0){
			length = poList.size()/10;
		}else{
			length = poList.size()/10+1;
		}
		json.put("length", length);
		json.put("now", n1/10+1);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 转化股票代码
	 * @param code
	 * @return
	 */
	public String Tran(String code){
		String Code = "";
		switch (code.length()) {
		case 6:
			Code = code;
			break;
		case 5:
			Code = "0"+code;
			break;
		case 4:
			Code = "00"+code;
			break;
		case 3:
			Code = "000"+code;
			break;
		case 2:
			Code = "0000"+code;
			break;
		case 1:
			Code = "00000"+code;
			break;
		default:
			break;
		}
		return Code;
	}
	
	/**
	 * 判断输入的是否为股票代码
	 * @param code
	 * @return
	 */
	private boolean IsCode(String code){
		String C = code.replace(" ", "");
		try{
			int number = Integer.valueOf(code);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
}
