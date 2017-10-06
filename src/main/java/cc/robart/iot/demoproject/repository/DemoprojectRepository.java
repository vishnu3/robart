package cc.robart.iot.demoproject.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cc.robart.iot.demoproject.persistent.Robot;

/**
 * The main and single repository for the demo project.
 * It handles all the SQL queries as it interacts with the database.
 *
 */
@Repository
public class DemoprojectRepository {
	
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

}
