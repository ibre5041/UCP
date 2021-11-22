package kerb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Properties;
import java.util.HashMap;

import com.sun.security.auth.module.Krb5LoginModule;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.net.ano.AnoServices;

public class Test {
	public Test() {
	}

	public void doit() throws Exception

	{
		System.setProperty("java.util.logging.config.file", "/home/ivan/devel/UCP/src/main/resources/OracleLog.properties");		
		System.setProperty("oracle.jdbc.Trace", "true");
		
		Properties props = new Properties();
		props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_SERVICES,
				"( " + AnoServices.AUTHENTICATION_KERBEROS5 + " )");

		props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_MUTUAL, "true");

		System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
		
		// kinit -c /tmp/krb5cc_ivan ivan
		// klist -c /tmp/krb5cc_ivan
		props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_AUTHENTICATION_KRB5_CC_NAME,
				"/tmp/krb5cc_ivan");

		String url = "jdbc:oracle:thin:@//rhel7a-19-restart.prod.vmware.haf:1521/TEST19C";

		DriverManager.registerDriver(new OracleDriver());

		Connection conn = DriverManager.getConnection(url, props);

		String sql = "select user from dual";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next())
			System.out.println("results: " + rs.getString(1));
		conn.close();
	}

	public static void main(String[] args) {
		Test test = new Test();
		try {
			test.doit();
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
