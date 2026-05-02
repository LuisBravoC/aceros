package services;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for SessionManager — no DB required.
 */
public class SessionManagerTest {

    @After
    public void clearSession() {
        SessionManager.getInstance().clear();
    }

    @Test
    public void getInstance_returnsSameInstance() {
        assertSame("SessionManager debe ser singleton",
            SessionManager.getInstance(), SessionManager.getInstance());
    }

    @Test
    public void initialState_userIdIsNull() {
        SessionManager.getInstance().clear();
        assertNull("userId debe ser null tras clear()", SessionManager.getInstance().getUserId());
    }

    @Test
    public void setUserId_thenGetUserId_returnsValue() {
        SessionManager.getInstance().setUserId("test_user");
        assertEquals("test_user", SessionManager.getInstance().getUserId());
    }

    @Test
    public void clear_resetsUserId() {
        SessionManager.getInstance().setUserId("test_user");
        SessionManager.getInstance().clear();
        assertNull("userId debe ser null tras clear()", SessionManager.getInstance().getUserId());
    }

    @Test
    public void setUserId_overwrite_returnsNewValue() {
        SessionManager.getInstance().setUserId("user_a");
        SessionManager.getInstance().setUserId("user_b");
        assertEquals("user_b", SessionManager.getInstance().getUserId());
    }
}
