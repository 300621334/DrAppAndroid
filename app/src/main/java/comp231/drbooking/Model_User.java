package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Model class used for data-binding when reading or writing to db
 */
public class Model_User
{
    String loginName;
    String pw;
    int Id_User = 0;
    String nameOfUser;
    String address;
    String email;
    String phone;
    String role = "1";// 1 = patient, 2 = Dr, 3 = admin, 0 = guest
    boolean isLoggedIn = false;
}
