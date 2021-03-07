package cc.robart.iot.demoproject.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import cc.robart.iot.demoproject.persistent.Firmware;
import cc.robart.iot.demoproject.persistent.Mapping;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import cc.robart.iot.demoproject.persistent.Robot;

/**
 * The main and single repository for the demo project.
 * It handles all the SQL queries as it interacts with the database.
 *
 */
@Repository
public class DemoprojectRepository {

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private JdbcTemplate jdbcTemplate;
	
	// Nevermind the constructor not being called eplicitely somewhere in the code
	// Spring does this implicitly via its dependency injection mechanism
	// It also makes sure that the DataSource parameter gets constructor and that 
	// it is injected properly here.
	public DemoprojectRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * Get a list of all the robots that are currently stored in the database.
	 * @return A list of {@link Robot} objects.
	 */
	public List<Robot> findAllRobots() {
		return jdbcTemplate.query("SELECT * FROM Robots", new RowMapper<Robot>() {
			
			@Override
			public Robot mapRow(ResultSet rs, int rowNum) throws SQLException {
				Robot robot = new Robot();
				robot.setName(rs.getString("name"));
				robot.setHardwareVersion(rs.getString("hardware_version"));
				return robot; 
			}
			
		});
		
	}

	public List<Firmware> findAllFirmware(){
		return jdbcTemplate.query("SELECT * FROM Firmwares", new RowMapper<Firmware>() {
			@Override
			public Firmware mapRow(ResultSet rs, int i) throws SQLException {
				Firmware firmware =  new Firmware();
				firmware.setName(rs.getString("name"));
				firmware.setData(rs.getString("data"));
				return firmware;
			}
		});
	}

	public int addFirmware(Firmware firmware){
           return jdbcTemplate.update("insert into Firmwares values (?, ?)",firmware.getName(), firmware.getData() );
	}

	public int deleteFirmware(String firmwareName){
		return jdbcTemplate.update("delete from Firmwares where name = ?", firmwareName);
	}

	public int updateFirmware(Firmware firmware, boolean type){
		if(type) {
			return jdbcTemplate.update("update Firmwares set name = '" + firmware.getName() + "'  where data = '" + firmware.getData() + "'");
		}else{
			return jdbcTemplate.update("update Firmwares set data = '" + firmware.getData() + "'  where name = '" + firmware.getName() + "'");
		}

	}

	public List<String> validateRobot(List<String> robots){

		String inSql = "'"+robots.stream().collect(Collectors.joining("','"))+"'";

		List<String> validList = jdbcTemplate.query(
				String.format("SELECT name FROM Robots WHERE name IN (%s)", inSql),
				(rs, rowNum) -> rs.getString("name"));

		if(robots.containsAll(validList)){
			return Collections.emptyList();
		}else {
               robots.removeAll(validList);
               return robots;
		}


	}

	public boolean validateFirmware(String firmwareName){

		String sql = "SELECT name FROM Firmwares WHERE name = '"+firmwareName+"'";

		String name =  jdbcTemplate.queryForObject(sql, String.class);

		return firmwareName.equals(name);

	}

   public int updateOldMapping(List<String> robots){

	   int[] i = Arrays.stream(this.jdbcTemplate.batchUpdate(
			   String.format("update robot_firmware_mapping set update_ts = ?, isActive = ? where robot_name in %s",
					   "('"+robots.stream().collect(Collectors.joining("','"))+"')"),
			   new BatchPreparedStatementSetter() {
                   @Override
				   public void setValues(PreparedStatement ps, int i)
						   throws SQLException {
					   ps.setString(1, formatter.format(new Date()));
					   ps.setInt(2, 0);
				   }

				   public int getBatchSize() {
					   return robots.size();
				   }
			   })).filter(j->j==1).map(j->j).toArray();

	   return i.length;

   }

	public int addNewMapping(String firmwareName,List<String> robots){

		int[] i = Arrays.stream(this.jdbcTemplate.batchUpdate(
				"insert into robot_firmware_mapping (robot_name,firmware_name,create_ts,update_ts,isActive) VALUES (?, ?, ?, ?, ?)",
				new BatchPreparedStatementSetter() {
                    @Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setString(1, robots.get(i));
						ps.setString(2, firmwareName);
						ps.setString(3, formatter.format(new Date()));
						ps.setString(4, formatter.format(new Date()));
						ps.setInt(5, 1);
					}

					public int getBatchSize() {
						return robots.size();
					}
				})).filter(j->j==1).map(j->j).toArray();

		return i.length;

	}

	public String getLatestFirmware(@NonNull String robotName){
		String sql = "SELECT firmware_name FROM robot_firmware_mapping WHERE isActive = '1' AND robot_name = '"+robotName+"'";

		List<String> strLst  = jdbcTemplate.query(sql,new RowMapper<String>() {

			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}

		});

		if ( strLst.isEmpty() ){
			return String.format("No Firmware Available for robot %s",robotName);
		}else if ( strLst.size() == 1 ) { // list contains exactly 1 element
			return strLst.get(0);
		}else{
			return "Incorrect mapping in robot and firmwares ";
		}
	}


}
