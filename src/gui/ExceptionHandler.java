package gui;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import org.xml.sax.SAXException;

import exceptions.AuthenticationError;
import exceptions.CorruptedCache;
import exceptions.CorruptedDatabase;
import exceptions.InvalidPassword;
import exceptions.InvalidToken;
import exceptions.NoAccountNumber;
import exceptions.NoDigestAlgorithm;
import exceptions.PasswordsDontMatch;
import exceptions.RemoteDesyncException;
import exceptions.UsernameExists;

public class ExceptionHandler {

	private ExceptionHandler() {
	}

	public static void handle(Exception exception) {
		if (exception instanceof RemoteException) {
			String msg = "There's been a problem connecting to the server.\n";
			msg += "If this problem persists make sure that the server is setup correctly.";
			JOptionPane.showMessageDialog(null, msg, "Server error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} else if (exception instanceof InvalidToken) {
			String msg = "The session you are trying to connect has expired or does not exist.\n";
			msg += "Try after login again.";
			JOptionPane.showMessageDialog(null, msg, "Session expired",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof RemoteDesyncException) {
			String msg = "There's a syncronization problem between the server and client.\n";
			msg += "Try making the request again.";
			JOptionPane.showMessageDialog(null, msg, "Syncronizatin error",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof NoDigestAlgorithm) {
			String msg = "A hashing algorithm is missing:\n";
			msg += exception.toString();
			JOptionPane.showMessageDialog(null, msg, "Algorithm missing",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof MalformedURLException) {
			String msg = "Server URL isn't setup correctly.\n";
			msg += exception.toString();
			JOptionPane.showMessageDialog(null, msg, "Incorrect server URL",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof UnsupportedLookAndFeelException) {
			String msg = "The chosen UI look and feel isn't supported.\n";
			msg += exception.toString();
			JOptionPane.showMessageDialog(null, msg, "Invalid UI look & feel",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof IllegalAccessException) {
			String msg = exception.toString();
			;
			JOptionPane.showMessageDialog(null, msg, "Illegal access",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof InstantiationException) {
			String msg = exception.toString();
			JOptionPane.showMessageDialog(null, msg, "Instantiation problem",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof ClassNotFoundException) {
			String msg = exception.toString();
			JOptionPane.showMessageDialog(null, msg, "Class not found",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof com.db4o.ext.DatabaseFileLockedException) {
			String msg = "The database file is locked, make sure that there's "
					+ "not another process using it.";
			JOptionPane.showMessageDialog(null, msg, "DB locked",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof java.rmi.NotBoundException) {
			String msg = "Couldn't connect with the business logic "
					+ "server, make sure that the server is setup correctly.";
			JOptionPane.showMessageDialog(null, msg, "Server error",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof java.rmi.NotBoundException) {
			String msg = "The business logic server wasn't found, "
					+ "make sure that the server is running and ";
			msg += "that the URL references are setup correctly.";
			JOptionPane.showMessageDialog(null, msg, "DB locked",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof AuthenticationError) {
			String msg = "Incorrect username or password.";
			JOptionPane.showMessageDialog(null, msg, "Authentication error",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof PasswordsDontMatch) {
			String msg = "The values of the two password fields must be equal.";
			JOptionPane.showMessageDialog(null, msg, "Passwords do not match",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof UsernameExists) {
			String msg = "The username you are trying to use already "
					+ "exists in the database. Choose another one.";
			JOptionPane.showMessageDialog(null, msg, "Edition error",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof InvalidPassword) {
			String msg = "The given password it is not correct.";
			JOptionPane.showMessageDialog(null, msg, "Invalid password",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof CorruptedDatabase) {
			String msg = "The data stored in the database is corrupted.\n"
					+ "Restore to a previous version or create a new database.";
			JOptionPane.showMessageDialog(null, msg, "Corrupted database",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof CorruptedCache) {
			String msg = "The data stored in the cache is corrupted.\n"
					+ "Restart the application to restore the cache.";
			JOptionPane.showMessageDialog(null, msg, "Corrupted cache",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof NoAccountNumber) {
			String msg = "The owner of the selected offer has not "
					+ "set an account number, and as a consequence it is "
					+ "not possible to make the booking.";
			JOptionPane.showMessageDialog(null, msg, "Parse error",
					JOptionPane.ERROR_MESSAGE);
		} else if (exception instanceof SAXException) {
			String msg = "The config file couldn't be read properly. "
					+ "Make sure that the file has correct XML syntax.";
			JOptionPane.showMessageDialog(null, msg, "XML error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			exception.printStackTrace();
		}
	}
}
