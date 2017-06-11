package action;

import sun.misc.BASE64Decoder;

import sun.misc.BASE64Encoder;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import businessLogicService.UsersService;
import po.UserPO;


public class UsersAction extends SuperAction implements ModelDriven<UserPO> {
	
	private static final long serialVersionUID = 1L;
	
	private UserPO user = new UserPO();
	public UserPO getModel() {
		return this.user;
	}
	
	// 注入用户管理的Service
	private UsersService usersService;
	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}
	
	public String result;
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String query(){
		ArrayList<UserPO> list = new ArrayList<>();
		UserPO u = new UserPO();
		u.setUid(0);
		u.setUsername("s");
		u.setPassword("d");
		list.add(u);
		if(list!=null && list.size()>0){
			session.setAttribute("user_list", list);
		}
		return "query_success";
	}

	/**
	 * 登录动作
	 * @return
	 */
	public String login(){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println(username+password);
		
		String re = usersService.Login(username, password);
		if(re.equals("success")){
			session.setAttribute("loginUserName", user.getUsername());
		}
		
		JSONObject json = new JSONObject();
		json.put("result", re);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 注册动作
	 * @return
	 */
	public String register(){
		String username = request.getParameter("username");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		
		String resultMessage = usersService.Register(username, password1, password2);
		JSONObject json = new JSONObject();
		json.put("result", resultMessage);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	public String logout(){
		System.out.println("-------");
		if(session.getAttribute("loginUserName")!=null){
			session.removeAttribute("loginUserName");
			
			System.out.println(session.getAttribute("loginUserName"));
		}
		return "success";
	}
	
	/**
	 * 修改昵称
	 * @return
	 */
	public String ChangeName(){
		String name = request.getParameter("name");
		String oldname = (String) session.getAttribute("loginUserName");
		System.out.println(name+"      "+oldname);
		
		JSONObject json = new JSONObject();
		if(name.equals("")||name==null){
			json.put("result", "null");
		}else if(name.equals(oldname)){
			json.put("result", "same");
		}else{
			boolean IsSuccess = usersService.ChangeName(name, oldname);
			
			if(IsSuccess){
				session.setAttribute("loginUserName", name);
				json.put("result", "success");
			}else{
				json.put("result", "failure");
			}
		}
	
		result = json.toString();
		return "success";
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	public String ChangePassword(){
		String oldpw = request.getParameter("oldpassword");
		String newpw1 = request.getParameter("newpassword1");
		String newpw2 = request.getParameter("newpassword2");
		String username = (String) session.getAttribute("loginUserName");
		
		String re = usersService.ChangePassword(username, oldpw, newpw1, newpw2);
		JSONObject json = new JSONObject();
		json.put("result", re);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 得到头像
	 */
	public String getPhoto(){
		String username = (String) session.getAttribute("loginUserName");
		String image = usersService.GetPhoto(username);
		JSONObject json = new JSONObject();
		json.put("image", image);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 上传头像
	 * @return
	 */
	public String UploadImage(){
		String username = (String) session.getAttribute("loginUserName");
		String image = request.getParameter("image");
		System.out.println(image.length());
		String resultmessage = usersService.UploadPhoto(username, image);
		JSONObject json = new JSONObject();
		json.put("result", resultmessage);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 得到用户自选股
	 * @return
	 */
	public String getSelfStock(){
		String page = request.getParameter("page");
		String username = request.getParameter("username");
		
		Map<String, String[]> stockmessage = usersService.getSelfStockByUsername(page, username);
		
		JSONObject json = new JSONObject();
		json.put("index", stockmessage.get("index"));
		json.put("code", stockmessage.get("code"));
		json.put("name", stockmessage.get("name"));
		json.put("close", stockmessage.get("close"));
		json.put("fluct", stockmessage.get("fluct"));
		int length = 0;
		if(stockmessage.get("index").length%10==0){
			length = stockmessage.get("index").length/10;
		}else {
			length = stockmessage.get("index").length/10+1;
		}
		json.put("length", length);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 添加用户自选股
	 * @return
	 */
	public String addSelfStock(){
		String code = request.getParameter("code");
		String username = request.getParameter("username");
		
		String resultmessage = usersService.addSelfStockByUsername(code, username);
		JSONObject json = new JSONObject();
		json.put("result", resultmessage);
		result = json.toString();
		return "success";
	}
	
	/**
	 * 删除用户自选股
	 * @return
	 */
	public String deleteSelfStock(){
		String code = request.getParameter("code");
		String username = request.getParameter("username");
		
		String resultmessage = usersService.deleteSelfStockByUsername(code, username);
		JSONObject json = new JSONObject();
		json.put("result", resultmessage);
		result = json.toString();
		return "success";
	}
	
	public static void main(String[] args) {
		UsersAction ac = new UsersAction();
		ac.login();
	}
}