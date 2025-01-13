package facebook_depi;

	import java.security.MessageDigest;
	import java.security.NoSuchAlgorithmException;

	public class PasswordUtil {
	    
	    // Method to hash the password
	    public static String hashPassword(String password) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            byte[] hashedBytes = md.digest(password.getBytes());
	            StringBuilder sb = new StringBuilder();
	            for (byte b : hashedBytes) {
	                sb.append(String.format("%02x", b));
	            }
	            return sb.toString(); // Return the hashed password in hexadecimal format
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	}
