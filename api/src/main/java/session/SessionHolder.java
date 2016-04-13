package session;


import model.User;
import org.apache.log4j.Logger;
import request.RequestProcessor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * It contains all current active sessions
 * @author Gladush Ivan
 * @since 30.03.16.
 */
public class SessionHolder {
    private static final Logger log = Logger.getLogger(SessionHolder.class);
    private static final Session defaultSession = new Session(new User("anonym", "anonym"));
    private static List<Session> sessions = new CopyOnWriteArrayList<>();

    /**
     * Returns the session on a specific title
     */
    public static Session getSession(String header) {
        int id = getSessionId(header);
        for (Session s : sessions) {
            if (s.getId() == id) {
                return s;
            }
        }
        return defaultSession;
    }

    /**
     * Adds a new session and returns it's id
     */
    public static int addSession(User user) {
        Session s = new Session(user);
        sessions.add(s);
        log.info(String.format("Add new session %s", sessions.toString()));
        return s.getId();
    }


    private static int getSessionId(String header) {
        try {
            return RequestProcessor.getSessionFromCookies(header);
        } catch (Exception e) {
            return -1;
        }
    }

    public static void removeSession(int session) {
        sessions.remove(new Session(session));
    }

    /**
     * The method checks for a particular session among active sessions
     */
    public static boolean containsSession(int sesId) {
        for (Session s : sessions) {
            if (s.getId() == sesId) {
                return true;
            }
        }
        return false;
    }
}