package API_VootKids;

import java.util.Random;

public class passwordgen {
	public static String passwordGenerator()
	{
		String SALTCHARS = "1234567890";
	    StringBuilder salt = new StringBuilder();
	    Random rnd = new Random();
	    while (salt.length() < 4) 
	    { 
	    	int index = (int) (rnd.nextFloat() * SALTCHARS.length());
	        salt.append(SALTCHARS.charAt(index));
	    }
	    String password = salt.toString();
	   
	    return password;
	}

	public static void main(String[] args) {
		
		String i=passwordgen.passwordGenerator();
		System.out.println(i);
	}

}
