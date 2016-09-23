

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.annotation.ManagedBean;


@ManagedBean
@Named
@SessionScoped
public class DataAdd implements Serializable{
	private static final String connection_url = "jdbc:mysql://localhost:3306/facecalender?useSSL=false";
	private int id;
	private HtmlInputHidden text;

	
//Convert date format
	private Date StringToDate(String dateString)
	{
	    Date date = null;
	    DateFormat df = new SimpleDateFormat("yyyy MM dd");
	    try{
	        date = df.parse(dateString);
	    }
	    catch ( Exception ex ){
	        System.out.println(ex);
	    }
	    return date;
	}

	
//Setting user id
	public void setId(int id){
		this.id=id;
	}
	
	public int getId(){
		return id;
	}
	
	
//Reads hidden field for user id
    public HtmlInputHidden getText() {
        return text;
    }

    public String getTextValue() {
        return (String) text.getAttributes().get("userid");
    }
        
    public void setTextValue(String text) {
    //    this.text = HtmlInputHidden.text;
    }
    
    public void setText(HtmlInputHidden text) {
        this.text = text;
    }

		
//List 2 addEvent
	public List<Event> getEvents() {
		List<Event> result = new ArrayList<>();
		id=Integer.parseInt(getTextValue());
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
			
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "SELECT event.* FROM event WHERE eventid NOT IN(SELECT eventId FROM userevent Where userevent.userid = " + id + ") order by date;";
				PreparedStatement stat = conn.prepareStatement(sql);
				ResultSet rs = null;
				stat.execute();
				rs = stat.getResultSet();
				
				while(rs.next()) {
					Event eve = new Event();
					eve.SetEventId(Integer.parseInt(rs.getString(1)));
					eve.setStartDate(StringToDate(rs.getString(2)));
					eve.setInterest(rs.getString(3));
					eve.setDescription(rs.getString(4));
					eve.setUserId(Integer.parseInt(rs.getString(5)));
					result.add(eve);
				}
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 DA ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 DA ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 DA ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 DA ");
			e.printStackTrace();
		}
		return result;
	}
	

//User id by name
	 private int getUserId() {
		 FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> parameters = context.getExternalContext().getRequestParameterMap();
		LogComp us = new LogComp();
		if(parameters.get("form:name")!= null){
			us.setUserName(parameters.get("form:name"));
			return us.getUserIdByName();
		}
		return Integer.parseInt(parameters.get("id"));
	 }

	 
//Sets user  events2com    
	public List<Event> getEvents2com(Event eve) {
		List<Event> result = new ArrayList<>();

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
			
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "select comment from comment where eventId = " + eve.getEventId();
				PreparedStatement stat = conn.prepareStatement(sql);
				ResultSet rs = null;
				stat.execute();
				rs = stat.getResultSet();
				
				while(rs.next()) {
					Event com = new Event();
					com.setCom(rs.getString(1));
					result.add(com);
				}
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 DA3 ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 DA3 ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 DA3 ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 DA3 ");
			e.printStackTrace();
		}
		return result;
	}
	 
    
//Sets user events 2 main   OK 
	public List<Event> getEvents2main() {
		List<Event> result = new ArrayList<>();
		int IdUser = getUserId();
		int[] eventId = eventList(IdUser);
		int antal = eventId.length;
		for(int i=0;i<antal;i++){
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Properties user = new Properties();
				user.put("user", "klas");
				user.put("password", "klas");
				
				Connection conn = DriverManager.getConnection(connection_url, user);
				try {
					String sql = "select * from event where eventId = " + eventId[i];
					PreparedStatement stat = conn.prepareStatement(sql);
					ResultSet rs = null;
					stat.execute();
					rs = stat.getResultSet();
					while(rs.next()) {
						Event eve = new Event();
						eve.SetEventId(Integer.parseInt(rs.getString(1)));
						eve.setStartDate(StringToDate(rs.getString(2)));
						eve.setInterest(rs.getString(3));
						eve.setDescription(rs.getString(4));
						eve.setUserId(Integer.parseInt(rs.getString(5)));
						result.add(eve);
					}
				}
				finally {
					conn.close();
				}
			} 
			catch (SQLException e) {
				System.out.println("    Här Gick Det Fel 1 DA1 ");
				e.printStackTrace();
			} 
			catch (InstantiationException e) {
				System.out.println("    Här Gick Det Fel 2 DA1 ");
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) {
				System.out.println("    Här Gick Det Fel 3 DA1 ");
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) {
				System.out.println("    Här Gick Det Fel 4 DA1 ");
				e.printStackTrace();
			}
		}
		return result;
	}
	
		
//Event list That user have chosen
	public int[] eventList(int id){
		int[] result = null;
	
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Properties user = new Properties();
			user.put("user", "klas");
			user.put("password", "klas");
			
			Connection conn = DriverManager.getConnection(connection_url, user);
			try {
				String sql = "SELECT userevent.eventId FROM event JOIN userevent ON event.eventID = userevent.eventId where userevent.userid = " + id + " order by event.date";
				PreparedStatement stat = conn.prepareStatement(sql);
				ResultSet rs = null;
				stat.execute();
				rs = stat.getResultSet();
				
				rs.last();
				int antal=rs.getRow();
				rs.beforeFirst();
				result = new int[antal];
				
				int i = 0;
				while(rs.next()) {

					int idNrEv =Integer.parseInt(rs.getString(1));
					
					result[i++]=idNrEv;
				}
			}
			finally {
				conn.close();
			}
		} 
		catch (SQLException e) {
			System.out.println("    Här Gick Det Fel 1 DA2 ");
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			System.out.println("    Här Gick Det Fel 2 DA2 ");
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			System.out.println("    Här Gick Det Fel 3 DA2 ");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			System.out.println("    Här Gick Det Fel 4 DA2 ");
			e.printStackTrace();
		}
		return result;
	}
		
}