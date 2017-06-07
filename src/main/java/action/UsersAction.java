package action;

import java.util.ArrayList;

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
	
	public String logout(){
		System.out.println("-------");
		if(session.getAttribute("loginUserName")!=null){
			session.removeAttribute("loginUserName");
			
			System.out.println(session.getAttribute("loginUserName"));
		}
		return "success";
	}
	
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
	
	public static void main(String[] args) {
		UsersAction ac = new UsersAction();
		ac.login();
	}
}