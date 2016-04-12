package session;

/**
 * @author Gladush Ivan
 * @since 12.04.16.
 */

import model.User;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class session simulates a single person,
 * the session is valid as long as the user log out.
 * Sessions are considered equal if they are equal to id
 * @author Gladush Ivan
 * @since 30.03.16.
 */
public class Session {
    private static final Logger log =Logger.getLogger(Session.class);
    private static AtomicInteger countSession = new AtomicInteger(-2);
    private int id;
    private User user;

    public Session(User user) {
        this.user = user;
        this.id = countSession.addAndGet(1);
        log.trace(String.format("\nCreate new session with user %s and the id session is %d\n",user,id));
    }

    Session(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String userName() {
        return user.getName();
    }

    public String userSurname() {
        return user.getSurname();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Session && ((Session) o).getId() == id;

    }

    @Override
    public String toString() {
        return String.format("Name: %s Surname: %s", userName(), userSurname());
    }
}
