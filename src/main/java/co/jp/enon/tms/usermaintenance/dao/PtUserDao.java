package co.jp.enon.tms.usermaintenance.dao;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import co.jp.enon.tms.usermaintenance.entity.PtUser;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PtUserDao {
	private final JdbcTemplate jdbcTemplate;

    public PtUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("this.jdbcTemplate " + this.jdbcTemplate);
    }
    
    // RowMapper to map ResultSet to PtUser
    private RowMapper<PtUser> userRowMapper = new RowMapper<PtUser>() {
        @Override
        public PtUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            PtUser user = new PtUser();
            user.setUserId(rs.getInt("user_id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setResetPasswordToken(rs.getString("reset_password_token"));
            user.setRole(rs.getByte("role"));
            user.setActive(rs.getByte("active"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return user;
        }
    };
    public List<PtUser> findAll() {
        String sql = "SELECT * FROM pt_user";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    //Find user by email
    public PtUser findByEmail(String email) {
        String sql = "SELECT * FROM pt_user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, email);
    }
    
    //Find user by reset_password_token
    public PtUser findByResetPasswordToken(String token) {
        String sql = "SELECT * FROM pt_user WHERE reset_password_token = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, token);
    }

    // Insert new user
    public int save(PtUser user) {
        String sql = "INSERT INTO pt_user (email, first_name, last_name, password, role, active, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        return jdbcTemplate.update(sql, user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getRole(), user.getActive());
    }
    
    public int updateToken(PtUser user) {
        String sql = "UPDATE pt_user SET reset_password_token = ? WHERE email = ?";
        return jdbcTemplate.update(sql, user.getResetPasswordToken(), user.getEmail());
    }
    
    public int updatePassword (PtUser user) {
    	String sql = "UPDATE pt_user SET password = ?, reset_password_token = ? WHERE email = ?";
        return jdbcTemplate.update(sql, user.getPassword(), user.getResetPasswordToken(), user.getEmail());
    }
    
    public int delete(String email) {
        String sql = "UPDATE pt_user SET active = ? WHERE email = ?";
        return jdbcTemplate.update(sql, 1, email);
    }
}
