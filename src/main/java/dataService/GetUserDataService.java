package dataService;

public interface GetUserDataService {
	
	public String GetUserpassword(String username);
	
	public String GetUserphoto(String username);
	
	public void ChangePassword(String username,String password);
	
	public void ChangePhoto(String username,String imagename);

}
