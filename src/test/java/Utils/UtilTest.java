package Utils;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class UtilTest {
    @Test
    public void testConnect() throws SQLException {
        Connection conn = Util.getConnection();
        Assert.assertNotNull(conn);
        conn.close();
    }

}