package businessLogicService;

import java.util.Map;

import po.UserPO;

public interface UsersService {
	
	public String Login(String username, String password);
	
	public String Register(String username, String password1, String password2);
	
	public boolean ChangeName(String newname, String oldname);
	
	public String ChangePassword(String username,String oldpw,String newpw1,String newpw2);
	
	public String GetPhoto(String username);
	
	public String UploadPhoto(String username,String image);
//	
	public boolean Logout(String username);
	
	public Map<String, String[]> getSelfStockByUsername(String page, String username);
	
	public String addSelfStockByUsername(String enter, String username);
	
	public String deleteSelfStockByUsername(String code, String username);

}
