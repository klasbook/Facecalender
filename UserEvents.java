

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.annotation.ManagedBean;
import javax.inject.Named;

@ManagedBean
@Named

public class UserEvents {
	
	private int userEventId;
	private int user=0;
	private int events;

	
//UserEventsID
	public void setUserEventsId(int id){
		this.userEventId=id;
	}
	
	public int getUserEventsId(){
		return this.userEventId;
	}

	
//User
	public void setUser(int user){
		this.user=user;
	}
	
	public int getUser(){
		return this.user;
	}
	
	
//Events
	public void setEvens(int event){
		this.events=event;
	}
	
	public void setAction(String action) {
		user = Integer.parseInt(action);
	}	
	
	
//Adding event	
	public void addEvent(){
		
	String connection_url = "jdbc:mysql://localhost:3306/facecalender?useSSL=false";	
	PreparedStatement ps = null;
				
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
		
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "INSERT INTO userevent(iduserEvent, userID, eventId) VALUES(?,?,?)";
				ps= conn.prepareStatement(sql); 
				ps.setString(1, null);
				ps.setString(2, ""+this.user);
				ps.setString(3, ""+this.events);
								
				ps.executeUpdate();
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 UE ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 UE ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 UE ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 UE ");
			e.printStackTrace();
		}
	}
}
