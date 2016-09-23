

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;


@ManagedBean
@Named
@SessionScoped
public class Event implements Serializable{
		
	private int userId = 0;
	private int eventId;
	private String interest;
	private Date startDate;
	private String description;
	private String com;

	
//User Id
	public void setUserId(int userId){
		this.userId=userId;
	}

	public int getId(){
		return this.userId;
	}

	
//Event Id
	public void SetEventId(int eventId){
		this.eventId= eventId;
	}

	public int getEventId(){
		 return this.eventId;
	}

	
//Interest
	private String[]interests = new String[4];
	private String selectedInterest;

	public String[] getInterests(){
		interests[0] = new String("Party");
		interests[1] = new String("Movie");
		interests[2] = new String("Kulture");
		interests[3] = new String("Else");
		
		return interests;
	}
				
	public String getSelectedInterest(){
		return selectedInterest;	
	}

	public void setSelectedInterest(String selectedInterest){
		this.selectedInterest=selectedInterest;
	}

	public void setInterest(String name){
		this.interest = name;
	}
	
	public String getInterest(){
		return interest;
	}
	
	
//Date
	public void setStartDate(Date date){
		this.startDate=date;
	}

	public Date getStartDate(){
		return this.startDate;
	}

	
//Description
	public void setDescription(String description){
		this.description=description;
	}

	public String getDescription(){
		return this.description;
	}

		
//Getting the user id
	public void setAction(String action) {
		userId = Integer.parseInt(action);
	}
			
			
//Create event	
	public int createEvent(){
		String connection_url = "jdbc:mysql://localhost:3306/facecalender?useSSL=false";
							
		PreparedStatement ps = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
	
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "INSERT INTO event(userid, interest, date, description, eventID) VALUES(?,?,?,?,?)";
				String output = new SimpleDateFormat("yyyy MM dd",Locale.getDefault()).format(startDate);
				ps= conn.prepareStatement(sql); 
				ps.setString(1, ""+userId);
				ps.setString(2, this.selectedInterest);
				ps.setString(3, output);
				ps.setString(4, this.description);
				ps.setString(5, null);
				
				ps.executeUpdate();
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 E ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 E ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 E ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 E ");
			e.printStackTrace();
		}
		clear();
		return 1;
	}

	
//Clear form
	private void clear(){
		this.description = null;
		this.startDate= null;
	}
	
//Getting id from action parameter	
	private String idUE;
	public void setIdUE(String action) {
		idUE = action;
	}

		
//ToDo 
	public void deleteEventFromMain(Event eve){
		ComData com = new ComData();
		com.setEventId(eve.eventId);		
//System.out.println(eve.eventId +" , " +eve.userId);
		return;
	}
	
	
//Preparing add comment	
	public int addComment(Event eve){
		ComData com = new ComData();
		com.setEventId(getevent2main());
		com.setUserId(eve.userId);
		com.setCom(eve.com);
		sendSqlComAdd(com);
		return 1;
		
	}

	
//Gets the event id for the comment to be added	 
	 public int getevent2main(){
    	FacesContext context = FacesContext.getCurrentInstance();
    	Map<String, String> parameters = context.getExternalContext().getRequestParameterMap();
 		return Integer.parseInt(parameters.get("event"));
     }
		
	 
//Setting comment to database
	public int sendSqlComAdd(ComData com){
		String connection_url = "jdbc:mysql://localhost:3306/facecalender?useSSL=false";
		PreparedStatement ps = null;
				
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
	
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "INSERT INTO comment(eventId, userId, comment) VALUES(?,?,?)";						
				ps= conn.prepareStatement(sql); 
				ps.setString(1, ""+com.eventId);
				ps.setString(2, ""+com.userId);
				ps.setString(3, com.com);
										
				ps.executeUpdate();
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 E1 ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 E1 ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 E1 ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 E1 ");
			e.printStackTrace();
		}
		this.com=null;
		return 1;
	 }
	
	//Adding to userEvent
		public void add2userEvents(){
			UserEvents ueve= new UserEvents();
			ueve.setEvens(eventId);
			ueve.setUser(Integer.parseInt(idUE));
			ueve.addEvent();
		}

	
//Comment	
	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public class ComData{
		private String com;
		private int eventId;
		private int comId;
		private int userId;
	
		
	//Comments id
		public int getComId() {
			return comId;
		}
	
		public void setComId(int comId) {
			this.comId = comId;
		}
	
		
	//Event id
		public int getEventId() {
			return eventId;
		}
		
		public void setEventId(int eventId) {
			this.eventId = eventId;
		}
		
		
	//Comment
		public String getCom() {
			return com;
		}
		
		public void setCom(String com) {
			this.com = com;
		}
		
		
	//User Id
		public void setUserId(int userId) {
			this.userId= userId;
		}
		
		
	}
}

