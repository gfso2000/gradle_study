import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class Test
{

  public static void main(String[] args)
  {
    int i = 0;
    while (i < 10) {
      String password = "1";
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      String hashedPassword = passwordEncoder.encode(password);
      System.out.println(hashedPassword);
      i++;
    }
    
//    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    boolean result =passwordEncoder.matches("123456", "$2a$10$zmAXPbe2XJ9aME/J6XTLhOwW8VJf7vdR/ITueOUaGTY2ZNz5vNiQC");
//    System.err.println(result);
//    result =passwordEncoder.matches("123456", "$2a$10$Nu0KeynvURVqU7iDJS4YVuWUMhLFA2r7J/XdRCUmydXfZNiz/8D82");
//    System.err.println(result);
//    result =passwordEncoder.matches("123456", "$2a$10$vWm.r1xQ7iQMei7VfPBG4upUCJAJ0U1INGH9gbRcAp6bUck8NvZOm");
//    System.err.println(result);
  }

}
