package action;

import java.util.ArrayList;

import org.apache.struts2.interceptor.validation.SkipValidation;

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
//		System.out.println(user.getUsername()+" "+user.getPassword());
		boolean flag = usersService.Login(user);
		if(flag){
			session.setAttribute("loginUserName", user.getUsername());
			return "login_success";
		}else{
			return "login_failure";
		}
	}
	
	/**
	 * 注册动作
	 * @return
	 */
	public String register(){
		if(usersService.Register(user)){
			return "register_success";
		}else{
			return "register_failure";
		}
	}
	
	@SkipValidation
	public String logout(){
		if(session.getAttribute("loginUserName")!=null){
			session.removeAttribute("loginUserName");
		}
		return "logout_success";
	}
	
	public static void main(String[] args) {
		UsersAction ac = new UsersAction();
		ac.login();
	}
}