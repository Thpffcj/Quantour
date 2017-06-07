package dataService;

import java.util.ArrayList;

import po.UserPO;

public interface UsersDao{

	public boolean usersLogin(String username, String password);
	
	public boolean usersRegister(String username, String password);
	
	public boolean ChangeName(String newname, String oldname);
	
	public String getPasswordByUsername(String username);
	
	public boolean ChangePassword(String username,String password);
	
	public ArrayList<String> getAllUsername();
}
