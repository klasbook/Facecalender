

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.annotation.ManagedBean;

@Named
@SessionScoped
@ManagedBean(value="user")

public class LogComp  implements Serializable{
	private static final String connection_url = "jdbc:mysql://localhost:3306/facecalender?useSSL=false";
	private String userName;
	private String password;
	private String email;
	private String country;
	private int id =  0;
	
	
//UserName
	public void setUserName(String name){
		this.userName = name;
	}
	public String getUserName(){
		return userName;
	}
	
	
//Password
	public void setPassword(String password){
		this.password = password;
	}
	
	public String getPassword(){
		return password;
	}
	
	
//email
	public void setEmail(String email){
		this.email = email;
	}
	public String getEmail(){
		return email;
	}
	
	
//Country
	private String[]countres = new String[4];
	public String[] getCountres(){
		countres[0] = new String("Sverige");
		countres[1] = new String("England");
		countres[2] = new String("Deutschland");
		countres[3] = new String("Elsewhere");
		
		return countres;
	}
	
	private String selectedCountry;
	
	public String getSelectedCountry(){
		return selectedCountry;	
	}
	
	public void setSelectedCountry(String selectedCountry){
		this.selectedCountry=selectedCountry;
	}
	
	public void setCountry(String name){
		this.country = name;
	}
	
	public String getCountry(){
		return country;
	}
	
//Id
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public String getId2str(){
		return ""+id;
	}
	
	
//Validate
	public void validateName(FacesContext fc, UIComponent c, Object value){
		String str =((String)value).trim();
		if(str.length()<4){
			throw new ValidatorException(new FacesMessage("To short user name!"));
		}
		this.setUserName(str);
		if(getUserIdByName()!=0 ){
			id=0;
			this.setUserName(null);
			throw new ValidatorException(new FacesMessage("username exist"));
		}
	}
	
	
	
//Adds user
	public String add(){
	
		int i = 0;
		country=this.getSelectedCountry();
		PreparedStatement ps = null;
		Connection con = null; 
		try	{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/facecalender", "klas", "klas");
			String sql = "INSERT INTO user(password, userName, country, email, id) VALUES(?,?,?,?,?)";
				
			ps= con.prepareStatement(sql); 
			ps.setString(1, password);
			ps.setString(2, userName);
			ps.setString(3, country);
			ps.setString(4, email);
			ps.setString(5, null);
			password = getPassword();
				
			i = ps.executeUpdate();
		}
		catch(Exception e){
			System.out.println(e); 
		}
		finally{
			try{
				con.close();
				ps.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(i >0){
			getUserId();
			return "main";
		}
		else{
			return "invalid";
		}
	} 
		

//Gets the user id when new user and when user login 
	public int getUserId(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
			id=0;
			
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "SELECT id FROM user WHERE username='"+userName+"' AND password='"+password+"'";
				PreparedStatement stat = conn.prepareStatement(sql);
				ResultSet rs = null;
				stat.execute();
				rs = stat.getResultSet();
				
				while(rs.next()) {
					
					LogComp log = new LogComp();
					log.setId(Integer.parseInt(rs.getString(1)));
					id=log.getId();
				}
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 LC ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 LC ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 LC ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 LC ");
			e.printStackTrace();
		}
		clear();
		return id;
	}
	

//When i only have user name this returns id
	public int getUserIdByName(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
			id=0;
			
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "SELECT id FROM user WHERE username='"+userName+"' ";
				PreparedStatement stat = conn.prepareStatement(sql);
				ResultSet rs = null;
				stat.execute();
				rs = stat.getResultSet();
		
				while(rs.next()) {
					LogComp log = new LogComp();
					log.setId(Integer.parseInt(rs.getString(1)));
					id=log.getId();
				}
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 LC1 ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 LC1 ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 LC1 ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 LC1 ");
			e.printStackTrace();
		}

		return id;
	}
	
//Clear
	private void clear(){
		this.email= null;
	}
	
//Leave
	public String leave(){
		this.email = null;
		//this.id = 0;
		//this.userName = null;
		//this.password = null;
		return "out";
	}
}
