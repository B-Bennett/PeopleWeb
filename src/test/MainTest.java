import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by BennettIronYard on 11/5/15.
 */
public class MainTest {
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
            People.insertPerson(conn, "Alex", "Hanger", "alex@theironyard.com", "USA", "1.2.3.4.5");
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
    }
}

