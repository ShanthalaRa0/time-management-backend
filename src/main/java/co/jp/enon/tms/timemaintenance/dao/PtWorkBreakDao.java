package co.jp.enon.tms.timemaintenance.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;

import co.jp.enon.tms.timemaintenance.entity.PtWorkBreak;

@Repository
public class PtWorkBreakDao {
	
	private final JdbcTemplate jdbcTemplate;

    public PtWorkBreakDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("this.jdbcTemplate " + this.jdbcTemplate);
    }
    
    public int save(PtWorkBreak ptWorkBreak) {
        String sql = "INSERT INTO pt_work_break (work_session_id, break_start, break_end, break_time, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, NOW(), NOW())";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, ptWorkBreak.getWorkSessionId());
            ps.setTime(2, Time.valueOf(ptWorkBreak.getBreakStart()));

            // handle nullable break_end
            if (ptWorkBreak.getBreakEnd() != null) {
                ps.setTime(3, Time.valueOf(ptWorkBreak.getBreakEnd()));
            } else {
                ps.setNull(3, java.sql.Types.TIME);
            }

            ps.setInt(4, ptWorkBreak.getBreakTime());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            return key.intValue(); // generated work_break_id
        } else {
            throw new RuntimeException("Failed to retrieve generated work_break_id.");
        }
    }
    
    public LocalTime getBreakStartTime(int workBreakId, int workSessionId) {
        String sql = "SELECT break_start FROM pt_work_break WHERE work_break_id = ? AND work_session_id = ?";
        
        return jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> {
                Time time = rs.getTime("break_start");
                return time != null ? time.toLocalTime() : null;
            },
            workBreakId, workSessionId // parameters go here
        );
    }
    
    public PtWorkBreak getActiveBreakTimeUsingSessionId(int workSessionId) {
        String sql = "SELECT * FROM pt_work_break WHERE work_session_id = ? AND break_end = '00:00:00'";

        try {
            return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> {
                    PtWorkBreak breakRecord = new PtWorkBreak();
                    breakRecord.setWorkBreakId(rs.getInt("work_break_id"));
                    breakRecord.setWorkSessionId(rs.getInt("work_session_id"));
                    breakRecord.setBreakStart(rs.getTime("break_start").toLocalTime());
                    Time endTime = rs.getTime("break_end");
                    breakRecord.setBreakEnd(endTime != null ? endTime.toLocalTime() : null);
                    breakRecord.setBreakTime(rs.getInt("break_time"));
                    breakRecord.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    breakRecord.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return breakRecord;
                },
                workSessionId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    public int update(PtWorkBreak ptWorkBreak) {
        String sql = "UPDATE pt_work_break " +
                     "SET break_end = ?, break_time = ?, updated_at = NOW() " +
                     "WHERE work_break_id = ? AND work_session_id = ?";

        return jdbcTemplate.update(sql,
                ptWorkBreak.getBreakEnd(),      // LocalTime or Time
                ptWorkBreak.getBreakTime(),     // int minutes
                ptWorkBreak.getWorkBreakId(),   // int
                ptWorkBreak.getWorkSessionId()  // int
        );
    }
    
    public Integer getTotalBreakTime(int workSessionId) {
        String sql = "SELECT SUM(break_time) AS total_break_time FROM pt_work_break WHERE work_session_id = ?";

        Integer totalBreakTime = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            workSessionId
        );

        // Handle null (no rows found)
        return totalBreakTime != null ? totalBreakTime : 0;
    }

}
