package businessLogic;

import java.io.FileOutputStream;
import java.sql.PseudoColumnUsage;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.components.Password;
import org.dom4j.util.UserDataAttribute;

import businessLogicService.UsersService;
import data.GetUserData;
import dataService.GetUserDataService;
import dataService.StockDataService;
import dataService.UsersDao;
import po.StockPO;
import po.UserPO;
import sun.misc.BASE64Decoder;

public class Users implements UsersService{
	//注入用户管理的Dao
	private UsersDao usersDao;
	public void setUsersDao(UsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	private static ArrayList<String> codelist = null;
	
	private StockDataService stockDataService;
	
	public void setStockDataService(StockDataService stockDataService) {
		this.stockDataService = stockDataService;
	}

	/**
	 * 登录
	 */
	public String Login(String username, String password){
//		System.out.println(666);
		if(username.equals("")||password.equals("")){
			return "null";
		}
		if(usersDao.usersLogin(username, password)){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 用户注册
	 */
	public String Register(String username, String password1, String password2) {
		if(username.equals("")||password1.equals("")||password2.equals("")){
			return "null";
		}
		if(!password1.equals(password2)){
			return "notsame";
		}
		
		ArrayList<String> namelist = usersDao.getAllUsername();
		boolean NotSame = true;
		for(int i=0;i<namelist.size();i++){
			if(username.equals(namelist.get(i))){
				NotSame = false;
			}
		}
		if(NotSame==false){
			return "falsename";
		}
		
		if(usersDao.usersRegister(username, password1)){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 修改昵称
	 * @param newname
	 * @param oldname
	 * @return
	 */
	public boolean ChangeName(String newname, String oldname){
		return usersDao.ChangeName(newname, oldname);
	}
	
	/**
	 * 修改密码
	 * @param username
	 * @param oldpw
	 * @param newpw1
	 * @param newpw2
	 * @return
	 */
	public String ChangePassword(String username,String oldpw,String newpw1,String newpw2){
		if(oldpw.equals("")||newpw1.equals("")||newpw2.equals("")){
			return "null";
		}
		if(!newpw1.equals(newpw2)){
			return "notsame";
		}
		String realpw = usersDao.getPasswordByUsername(username);
		if(!realpw.equals(oldpw)){
			return "falseold";
		}
		boolean IsSuccess = usersDao.ChangePassword(username, newpw1);
		if(IsSuccess){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 得到头像路径
	 * @param username
	 * @return
	 */
	public String GetPhoto(String username){
		return usersDao.getPhoto(username);
	}
	
	/**
	 * 上传头像
	 */
	public String UploadPhoto(String username,String image){
		if(image.equals("")||image==null){
			return "null";
		}
		String header ="data:image/jpeg;base64,";
		if(image.indexOf(header) != 0){ 
			return "failure";
		}  
		boolean IsSuccess = usersDao.UploadPhoto(username, image);
		if(IsSuccess){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 登出
	 */
	public boolean Logout(String username){
		return true;
	}
	
	/**
	 * 得到用户自选股以及相关信息
	 * @param username
	 * @return
	 */
	public Map<String, String[]> getSelfStockByUsername(String page, String username){
		ArrayList<String> codelist = usersDao.getSelfStockByUsername(username);
		int n1 = Integer.valueOf(page)*10;
		int n2 = 0;
		if((n1+10)>codelist.size()){
			n2 = codelist.size();
		}else{
			n2 = n1+10;
		}
		
		String[] index = new String[codelist.size()];
		String[] code = new String[10];
		String[] name = new String[10];
		String[] close = new String[10];
		String[] fluct = new String[10];
		int k = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		
		for(int i=n1;i<n2;i++){
			index[k] = Integer.toString(i+1);
			code[k] = codelist.get(i);
			name[k] = stockDataService.getNameByCode(Integer.valueOf(codelist.get(i)));
			ArrayList<StockPO> todaymessage = stockDataService.getStockByCodeAndDate(Integer.valueOf(codelist.get(i)), "2017-05-25", "2017-05-25");
			if(todaymessage.size()>0){
				close[k] = df.format(todaymessage.get(0).getClose());
				String lastday = getDayBefore("2017-05-25", Integer.valueOf(codelist.get(i)));
				ArrayList<StockPO> lastdaymessage = stockDataService.getStockByCodeAndDate(Integer.valueOf(codelist.get(i)), lastday, lastday);
				fluct[k] = df.format((todaymessage.get(0).getClose()-lastdaymessage.get(0).getClose())/lastdaymessage.get(0).getClose()*100)+"%";
			}else{
				close[k] = "--";
				fluct[k] = "--";
			}
			k++;
		}
		Map<String, String[]> result = new HashMap<>();
		result.put("index", index);
		result.put("code", code);
		result.put("name", name);
		result.put("close", close);
		result.put("fluct", fluct);
		return result;
	}
	
	/**
	 * 添加自选股
	 * @param enter
	 * @param username
	 * @return
	 */
	public String addSelfStockByUsername(String enter, String username){
		String code = enter.replace(" ", "");
		if(code.equals("")){
			return "null";
		}
		if(!IsCode(code)){
			code = Tran(Integer.toString(stockDataService.getCodeByName(code)));
		}
		if(code.equals("000000")||!IsLegalCode(code)){
			return "unknow";
		}
		
		ArrayList<String> codelist = usersDao.getSelfStockByUsername(username);
		for(int i=0;i<codelist.size();i++){
			if(code.equals(codelist.get(i))){
				return "same";
			}
		}
		
		boolean IsSuccess = usersDao.addSelfStockByUsername(code, username);
		if(IsSuccess){
			return "success";
		}
		return "failure";
	}
	
	/**
	 * 删除用户自选股
	 * @param code
	 * @param username
	 * @return
	 */
	public String deleteSelfStockByUsername(String code, String username){
		boolean IsSuccess = usersDao.deleteSelfStockByUsername(code, username);
		if(IsSuccess){
			return "success";
		}else{
			return "failure";
		}
	}
	
	/**
	 * 获得当前股票的前一交易日
	 * 
	 * @param today
	 * @return
	 */
	private String getDayBefore(String today, int code) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		if (code == 603580||code==2876) {
			return today;
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
		return time.format(dayBefore);
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
	
	/**
	 * 将股票代码转化为六位
	 * @param code
	 * @return
	 */
	private String Tran(String code){
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
	 * 判断是否存在该股票代码
	 * @param code
	 * @return
	 */
	private boolean IsLegalCode(String code){
		if(codelist==null){
			codelist = stockDataService.GetAllCode();
		}
		for(int i=0;i<codelist.size();i++){
			if(code.equals(codelist.get(i))){
				return true;
			}
		}
		return false;
	}
}
