package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public User findById(Long id) {
//        int intID = Integer.parseInt(id.toString());
        try {
            return this.jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?", new Object[]{id}, new UserRowMapper());
        }catch (Exception exception) {
            throw new UserNotFoundException(id);
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAllLimit(int page, int limit) {
//        int intID = Integer.parseInt(id.toString());
//        List<User> cus = new ArrayList<User>();
//        List<Map> rows = this.jdbcTemplate.queryForList("SELECT * FROM users WHERE id>0");
//        List<User> rows = this.jdbcTemplate.queryForList("SELECT * FROM users WHERE LIMIT ?",new Object[]{limit}, (Class<User>) new User().getClass());

        int countRow;
//        countRow = this.jdbcTemplate.query("SELECT * FROM users", ResultSet::);
//        System.out.println(countRow+"xxxxxxxxx");

        List<User> rows;



        rows = this.jdbcTemplate.query("SELECT * FROM users LIMIT ?, ?", new Object[]{(page-1)*limit, limit},  new UserRowMapper());
//        List<User> rows = this.jdbcTemplate.queryForList("SELECT * FROM users LIMIT ?", new BeanPropertyRowMapper(User.class));

        countRow = rows.size();

        try {
            return rows;
//            return this.jdbcTemplate.queryForList("SELECT * FROM users WHERE id>?", new Object[]{limit}, new UserRowMapper());
        }catch (Exception exception) {
            throw new UserNotFoundException((long)limit);
        }
    }

    @Transactional(readOnly = true)
    public int getPageAll(int limit){
        List<User> rows;

        rows = this.jdbcTemplate.query("SELECT * FROM users", new Object[]{},  new UserRowMapper());

        return (int) Math.ceil((double) rows.size()/limit);
//        return rows.size()/limit;
    }

    @Transactional
    public void save(User user) {
        String sql = "INSERT INTO users(id, firstname, lastname) VALUES (?,?,?)";
        this.jdbcTemplate.update(sql, user.getId(), user.getFirstname(), user.getLastname());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id=?";
        this.jdbcTemplate.update(sql, id);
    }
}