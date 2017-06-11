package dataService;

import java.util.ArrayList;

import po.UserPO;

public interface UsersDao{

	public boolean usersLogin(String username, String password);
	
	public boolean usersRegister(String username, String password);
	
	public boolean ChangeName(String newname, String oldname);
	
	public String getPasswordByUsername(String username);
	
	public boolean ChangePassword(String username,String password);
	
	public String getPhoto(String username);
	
	public boolean UploadPhoto(String username, String image);
	
	public ArrayList<String> getAllUsername();
	
	public ArrayList<String> getSelfStockByUsername(String username);
	
	public boolean addSelfStockByUsername(String code, String username);
	
	public boolean deleteSelfStockByUsername(String code, String username);
}
