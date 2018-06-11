package comp231.drbooking;
/*
 * By: SHAFIQ-UR-REHMAN
 * Purpose: Model class used for data-binding when reading or writing to db
 */
public class Model_Booking
{
    int Id_Appointment;//PK
    int Id_User;//Foreign Key
    int Id_Doc = 0;
    String Clinic;
    String Doctor;
    String AppointmentTime;
    String CreationTime;
    String User = "";//Name of Patient
    String DRAVAILABLE;

    //If NOT using custom Adapter, then just overriding toString insode complex type will avoid displaying class-tpe in list & show actual info :https://stackoverflow.com/questions/2265661/how-to-use-arrayadaptermyclass?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    @Override
    public String toString() {
        return  Clinic +
                "\n" + Doctor +
                "\n" + AppointmentTime;


                /*"Model_Booking{" +
                "Id_Appointment=" + Id_Appointment +
                ", Id_User=" + Id_User +
                ", Clinic='" + Clinic + '\'' +
                ", Doctor='" + Doctor + '\'' +
                ", AppointmentTime='" + AppointmentTime + '\'' +
                ", CreationTime='" + CreationTime + '\'' +
                '}';*/
    }
}
