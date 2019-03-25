package edu.brown.cs.drawbridge.database;

public class DatabaseQueryTest {

<<<<<<< HEAD
  // @Test
  // public void testDatabaseConnection() {
  // try {
  // DatabaseQuery test = new DatabaseQuery("//localhost/carpools");
  // } catch (ClassNotFoundException | SQLException e) {
  // assert false;
  // }
  // }
=======
  //@Test
  public void testDatabaseConnection() {
    try {
      DatabaseQuery test = new DatabaseQuery("//localhost/carpools", "", "");
    } catch (ClassNotFoundException | SQLException e) {
      assert false;
    }
  }
>>>>>>> 67f20ca7fec920066e65919d24400dddd6f4ada0

}
