import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by BennettIronYard on 11/5/15.
 */
public class PeopleTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./test");
        People.createTables(conn);
        return conn;
    }

    public void endConnection(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE people");
        conn.close();
    }

    @Test
    public void testPerson() throws SQLException {
        Connection conn = startConnection();
        People.insertPerson(conn, "Kim", "House", "kim@.com", "USA", "03.838");
        Person person = People.selectPerson(conn, 1);
        endConnection(conn);

        assertTrue(person != null);
    }

    @Test
    public void testSelectPeople() throws SQLException {
        Connection conn = startConnection();
        People.insertPerson(conn, "Alice", "Pace", "Al@.com", "China", "00.109");
        People.insertPerson(conn, "Bob", "Billings", "b12@.com", "India", "99.00");
        ArrayList<Person> people = People.selectPeople(conn, 0);
        endConnection(conn);

        assertTrue(people.size() == 3);
    }
}