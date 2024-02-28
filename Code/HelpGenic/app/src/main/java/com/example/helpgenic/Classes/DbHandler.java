package com.example.helpgenic.Classes;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.mysql.jdbc.CallableStatement;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DbHandler {

    private Connection connection = null;

    private CallableStatement cs = null;

    String url="jdbc:mysql://sda.mysql.database.azure.com:3306/helpgenic?useSSL=true&loginTimeout=30";


    public boolean  connectToDb(Context ptr) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, "azureuser", "Muhammad167");

            Toast.makeText(ptr,"Success", Toast.LENGTH_SHORT).show();

            return true;

        } catch (Exception e) {
            Toast.makeText(ptr, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
        connection = null;

    }

    public List<Object> matchCredentials(String email , String password , Context context) {
        try {


            if(connection != null){

                cs = (CallableStatement) this.connection.prepareCall("{call matchCredentials(?, ?, ?,?)}");
                cs.setString(1, email);
                cs.setString(2, password);
                cs.registerOutParameter(3, Types.VARCHAR);
                cs.registerOutParameter(4,Types.INTEGER);
                cs.executeQuery();

                String type = cs.getString(3);
                int id = cs.getInt(4);
                System.out.println(type);

                if(Objects.equals(type,"D")){

                    String query1 = "select verified from Doctor where docId = ?";

                    try (PreparedStatement stmt = connection.prepareStatement(query1)) {

                        stmt.setInt(1,id);

                        ResultSet rs = stmt.executeQuery();


                        while (rs.next()) {
                            boolean isVerified = rs.getBoolean("verified");

                            System.out.println(isVerified);

                            if(!isVerified){
                                Toast.makeText(context, "Sorry ! wait for your verification please!", Toast.LENGTH_SHORT).show();
                                return null;
                            }else{
                                return Arrays.asList(cs.getString(3) , cs.getInt(4));
                            }

                        }



                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                return Arrays.asList(cs.getString(3) , cs.getInt(4));

            }



        } catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return null;
    }

    public List<Object> getPatientDetails(int pId , Context context) {

        String query = "select * from `User` u join Patient p on u.uid = p.pid where pid = ? and u.type = 'P'";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,pId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                return Arrays.asList(rs.getString("name") , rs.getBoolean("gender") , rs.getDate("dob"), rs.getString("bloodGroup"), rs.getString("phoneNum"));

            }



        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public List<Object> getDoctorDetails(int docId , Context context) {

        String query = "select * from `User` u join Doctor d on u.uid = d.docid where docId = ? and u.type = 'D'";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                return Arrays.asList(rs.getString("name") , rs.getBoolean("gender") , rs.getDate("dob"), rs.getString("specialization") , rs.getBoolean("surgeon") , rs.getString("accNum"), rs.getFloat("rating"));

            }



        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public boolean isConnectionOpen(){
        if (Objects.equals(connection ,null))
            return false;
        return true;
    }


    public ArrayList<Doctor> getListOfDoctors(Context ptr){

        ArrayList<Doctor> list = new ArrayList<>();
        String query = "select * from `User` u join Doctor d on u.uid = d.docId  where verified=true order by rating desc";

        try (Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                int id = rs.getInt("uid");
                String docName = rs.getString("name");
                String email = rs.getString("email");
                boolean isGender = rs.getBoolean("gender");
                float rating = rs.getFloat("rating");


                char gender;
                if (isGender){
                    gender = 'M';
                }else{
                    gender = 'F';
                }

                String specialization = rs.getString("specialization");
                boolean isSurgeon = rs.getBoolean("surgeon");

                if(isSurgeon){
                    specialization += ", Surgeon";
                }

                String accNum = rs.getString("accNum");

                list.add(new Doctor(id , email,specialization ,isSurgeon , accNum ,docName, gender,null, rating));
            }


        } catch (Exception e) {
            Toast.makeText(ptr, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return list;
    }

    public ArrayList<VirtualAppointmentSchedule> getDoctorVirtualAppDetails(int docId , Context context){

        ArrayList<VirtualAppointmentSchedule> list = new ArrayList<>();
        String query = "select a.aptId, a.fee ,a.day , a.sTime , a.eTime from Doctor d join appSchedule a on d.docId=a.docId join virtualAppSchedule vs on a.aptId=vs.aptId\n" +
                "where d.docId = ?";



        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                int id = rs.getInt("aptId");
                String day = rs.getString("day");
                Time sTime = rs.getTime("sTime");
                Time eTime = rs.getTime("eTime");
                float fee = rs.getFloat("fee");


                list.add(new VirtualAppointmentSchedule(id,day,sTime,eTime,fee));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return list;
    }

    public ArrayList<PhysicalAppointmentSchedule> getDoctorPhysicalAppDetails(int docId , Context context){

        ArrayList<PhysicalAppointmentSchedule> list = new ArrayList<>();

        String query = "select a.aptId ,fee ,day , sTime , eTime ,clinicName ,location,assistantPhoneNum  from Doctor d join appSchedule a on d.docId=a.docId join phyAppSchedule ps on a.aptId=ps.aptId\n" +
                "where d.docId = ?";



        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                String day = rs.getString("day");
                Time sTime = rs.getTime("sTime");
                Time eTime = rs.getTime("eTime");
                float fee = rs.getFloat("fee");

                String clinicName = rs.getString("clinicName");
                String location = rs.getString("location");
                String assistantPh = rs.getString("assistantPhoneNum");

                list.add(new PhysicalAppointmentSchedule(clinicName,assistantPh,day,sTime,eTime,fee));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return list;
    }

    public ArrayList<Slot> getConsumedSlots(int docId , Date date ,Context context ){

        ArrayList<Slot> list = new ArrayList<>();

        String query = "select * from Appointment where docId= ? and `date`= ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);
            stmt.setDate(2,date);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                Time sTime = rs.getTime("sTime");
                Time eTime = rs.getTime("eTime");

                list.add(new Slot(sTime , eTime ,null));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return list;
    }


    public boolean checkDuplicateAppointment(int docId , int patientId , Date date , Context context){

        String query = "select * from Appointment where docId= ? and pId = ? and `date`= ?";

        boolean isPresent = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);
            stmt.setInt(2,patientId);
            stmt.setDate(3,date);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                isPresent = true;
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return isPresent;

    }


    public int loadAppointmentToDb(int docId , int patientId , Date date ,Time sTime , Time eTime, Context context) {

        String query = "insert into Appointment (docId , pId , date , sTime , eTime) values (?,?,?,?,?)";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);
            stmt.setInt(2,patientId);
            stmt.setDate(3,date);
            stmt.setTime(4,sTime);
            stmt.setTime(5,eTime);


            stmt.execute();

            Toast.makeText(context, "Inserted Successfully !", Toast.LENGTH_SHORT).show();


            String query1 = "select max(appId) as appId from Appointment";

            try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {

                ResultSet rs = stmt1.executeQuery();


                while (rs.next()) {
                    return rs.getInt("appId");
                }


            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return -1;

    }

    public ArrayList<Appointment> getUpcommingAppointmentsForPatients(int pId,Context context){

        ArrayList<Appointment> upcomingApp = new ArrayList<>();
        String query = "select * from upcomingAppointments ua where pId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,pId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                 Character gender;
                 if(Objects.equals(rs.getBoolean("gender"),1)) {
                     gender = 'M';
                 }else{
                     gender = 'F';
                 }

                 Doctor d = new Doctor(rs.getInt("docId"), null ,rs.getString("specialization"),rs.getBoolean("surgeon") , null ,rs.getString("name") , gender , null, 0.0f);
                 d.setVSch(rs.getString("day"),rs.getTime("docsTime"), rs.getTime("docetime"));

                upcomingApp.add(new Appointment(rs.getDate("date") , d , null ,rs.getTime("sTime")  ,rs.getTime("eTime"),rs.getInt("appId")));


            }

            return upcomingApp;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        return upcomingApp;
    }


    public void updateAppointmentInDatabase(int appointmentId , Time sTime , Time eTime ,Context context){
        String query = "update Appointment set sTime=? , eTime=? where appId=? ";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {


            stmt.setTime(1,sTime);
            stmt.setTime(2,eTime);
            stmt.setInt(3,appointmentId);



            stmt.execute();

            Toast.makeText(context, "Updated Successfully !", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void InsertComment(int pId, int dId, String comment,Context context) {
        String query = "call insertComment(?,?,?)";

        try {

            if (connection != null) {
                cs = (CallableStatement) this.connection.prepareCall(query);
                cs.setInt(1, pId);
                cs.setInt(2, dId);
                cs.setString(3, comment);
                cs.executeQuery();
            }

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void updateRating( int docId ,  float rating , Context context){
        String query = "call updateRating(?,?)";

        try {

            if (connection != null) {
                cs = (CallableStatement) this.connection.prepareCall(query);
                cs.setFloat(1, rating);
                cs.setInt(2, docId);
                cs.executeQuery();
            }

        } catch (Exception e) {


            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void cancelAppointment(int appId , Context context){
        String query = "Delete from appointment where appid = ?";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,appId);
            stmt.execute();
            Toast.makeText(context, "Cancelled Successfully !", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public boolean matchPatientCredentials( String email , Context context) {


        String query = "select * from `User` where email = ?";

        boolean isPresent = false;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1,email);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                isPresent = true;
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return isPresent;


    }

    public  int insertPatientDetailsInDb(String name , String email , String password , boolean gender , Date dob, String bloodGrup , String phNum, Context context ){

        String query = "call insertUser(?,?,?,?,?,?,?)";

        int id = 0;
        try {

            if (connection != null) {

                cs = (CallableStatement) this.connection.prepareCall(query);

                cs.setString(1, name);
                cs.setString(2, email);
                cs.setString(3, password);
                cs.setBoolean(4, gender);
                cs.setDate(5,dob);
                cs.setString(6,"P");
                cs.registerOutParameter(7,Types.INTEGER);
                cs.executeQuery();

                id = cs.getInt(7);

                String query2 = "insert into Patient values (?,?,?)";

                try (PreparedStatement stmt = connection.prepareStatement(query2)) {


                    stmt.setInt(1,id);
                    stmt.setString(2,bloodGrup);
                    stmt.setString(3,phNum);

                    stmt.execute();

                    Toast.makeText(context, "Inserted Successfully !", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }



            }

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return id;
    }

    public String loadDoctorDegreeImageFromDb(int docId , Context context){

        String query = "select degree from Doctor where docId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();
            String degree = "";

            while (rs.next()) {
                degree = rs.getString("degree");
            }

           return degree;

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public ArrayList<Appointment> getUpcommingAppointmentsForDoctor(int docId,Context context)
    {
        ArrayList<Appointment> upcomingApp = new ArrayList<>();
        String query = "select * from upcomingAppointmentsForDoctor where docId = ?";


        try ( PreparedStatement stmt = connection.prepareStatement(query) ) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {


                Appointment ap = new Appointment(rs.getDate("date")  , null,new Patient(rs.getInt("pid"), rs.getString("name")) , rs.getTime("sTime") , rs.getTime("eTime") , 0);
                upcomingApp.add(ap);


            }


            return upcomingApp;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public Patient getPatientInfo(int patientId, Context context){
        String query = "select * from getPatientInfo where pid=?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,patientId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                return new Patient(rs.getString("name") ,rs.getString("email") , rs.getString("phoneNum") ,rs.getString("bloodGroup") ,rs.getInt("age")  ,rs.getBoolean("gender") );
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }


    public ArrayList<Appointment> getPreviousAppointmentInfo(int docId , int patientId , Context context){
        String query = "select * from getPreviousAppointmentsInfo where pid=? and docId = ?";

        ArrayList<Appointment> prevAppointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,patientId);
            stmt.setInt(2,docId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {

                prevAppointments.add(new Appointment(rs.getDate("date"), null , null,null,null,rs.getInt("appId")));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return prevAppointments;
    }

    public void loadPrescriptionToDb(int aptId ,Prescription p , Context context) {{
        String query = "insert into Prescripton values(?,?)";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,aptId);
            stmt.setInt(2,p.getDays());

            stmt.execute();



            String query1 = "insert into medicineDosage values(?,?,?,?,?,?)";

            try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {
                stmt1.setInt(5,aptId);

                for (MedicineDosage m: p.getMedicines()){
                    stmt1.setString(1,m.getMedicineName());
                    stmt1.setInt(2,m.getMedicineDosage());
                    stmt1.setBoolean(3,m.isMorning());
                    stmt1.setBoolean(4,m.isEvening());
                    stmt1.setBoolean(6,m.isAfternoon());


                    stmt1.execute();
                }

            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }}


    public ArrayList<Prescription> getPrescriptionInfo(int aptId , Context context){
        String query = "select * from Prescripton p join medicineDosage m on p.presId = m.presId where p.presId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,aptId);

            ResultSet rs = stmt.executeQuery();


            Prescription p = new Prescription();

            while (rs.next()) {

                p.addMedicine(new MedicineDosage(rs.getString("name") , rs.getInt("dosage"), rs.getBoolean("morning") , rs.getBoolean("afternoon"), rs.getBoolean("night")));
                p.setDays(rs.getInt("days"));

            }

            if(p.getMedicines().size() != 0){
                ArrayList<Prescription> temp = new ArrayList<>();
                temp.add(p);
                return temp;
            } else{
                return null;
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;

    }


    public void loadDocumentToDb(int aptId , String document, Context context){
        String query1 = "insert into Document (appId , documentBinary) values(?,?)";

        try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {

            stmt1.setInt(1,aptId);
            stmt1.setString(2,document);
            stmt1.execute();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Document> getDocumentsFromDb(int aptId , Context context){

        String query = "select * from Document where appId = ?";

        ArrayList<Document> documents = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,aptId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                documents.add(new Document(rs.getInt("documentId") , null));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return documents;
    }

    public String getPatientDocumentsFromDb(int documentId , Context context){

        String query = "select documentBinary from Document where documentId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,documentId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                return rs.getString("documentBinary");
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public ArrayList<Patient> getPreviousPatientsAttended(int docId , Context context){

        ArrayList<Patient> prevPatients = new ArrayList<>();

        String query = "select DISTINCT * from getPreviousPatients where docId = ?";


        try ( PreparedStatement stmt = connection.prepareStatement(query) ) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {


                Patient ap = new Patient(rs.getInt("pid"), rs.getString("name"));
                prevPatients.add(ap);


            }


            return prevPatients;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public ArrayList<Doctor> getPreviousDoctorsMet(int patientID , Context context){

        ArrayList<Doctor> list = new ArrayList<>();

        String query = "select DISTINCT * from getPreviousDoctors  where pId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query) ) {


            stmt.setInt(1,patientID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("docId");
                String docName = rs.getString("name");
                String email = rs.getString("email");
                boolean isGender = rs.getBoolean("gender");
                float rating = rs.getFloat("rating");


                char gender;
                if (isGender){
                    gender = 'M';
                }else{
                    gender = 'F';
                }

                String specialization = rs.getString("specialization");
                boolean isSurgeon = rs.getBoolean("surgeon");

                if(isSurgeon){
                    specialization += ", Surgeon";
                }

                list.add(new Doctor(id , email,specialization ,isSurgeon , null ,docName, gender,null, rating));

            }

            return list;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        return list;

    }

    public void updateFee(int docId , int fee,Context context){

        String query1 = "update appschedule set fee = ? where docId = ? and type = 'v'";

        try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {

            stmt1.setInt(1,fee);
            stmt1.setInt(2,docId);
            stmt1.execute();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public boolean verifyUser(String email,Context context) {
        boolean already_exists = false;
        String query = "CALL VERIFYEMAIL(?,?)";
        try {
            if (connection != null) {
                 cs = (CallableStatement) connection.prepareCall(query);
                cs.setString(1, email);
                cs.registerOutParameter(2, Types.BOOLEAN);
                cs.executeQuery();
                already_exists = cs.getBoolean(2);

            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (already_exists)
            return false;
        else {
            return true;
        }
    }

    public int insertUser(GuestUser user,String type,Context context){

        String query = "CALL INSERTUSER(?,?,?,?,?,?,?)";
        int id=-1;
        try{
            if(connection!=null){
                //convert gender to boolean
                boolean g=false;
                if(user.gender=='f')
                    g=false;
                else
                    g=true;


                //prepare query
                cs = (CallableStatement) connection.prepareCall(query);
                cs.setString(1,user.name);
                cs.setString(2,user.email);
                cs.setString(3,user.password);
                cs.setBoolean(4, g);
                cs.setDate(5, user.getDob());
                cs.setString(6,type);
                cs.setInt(7,id);
                cs.executeQuery();
                id=cs.getInt(7);
            }
        }
        catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return id;
    }

    public void insertDoctor(Doctor doctor,int id,String encodedImage ,Context context) {

        String query="CALL INSERTDOCTOR(?,?,?,?)";
        try{
            if(connection!=null)
            {
                cs = (CallableStatement) connection.prepareCall(query);
                cs.setInt(1,id);
                cs.setString(2,doctor.getSpecialization());
                cs.setBoolean(3,doctor.isSurgeon());
                cs.setString(4, encodedImage);
                cs.executeQuery();
            }
        }
        catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    public ResultSet get_doctors_and_prev_patients(Context context){
        ResultSet rs =null;
        try{
            Statement st= connection.createStatement();
            rs = st.executeQuery("Select user.name,t.pAttended from user inner join (select distinct Docid,count(*) as pAttended from getpreviouspatients \n" +
                    "group by docid)as t on user.uid=t.docid");
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rs;
    }


    public ResultSet getAllDocRating(Context context){
        ResultSet rs=null;
        try {
            if(connection!=null){
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("select user.name,doctor.rating from doctor inner join user on user.uid=doctor.docid");
            }
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return rs;
    }



    public ResultSet getUnVerifiedDocs(Context context) {

        ResultSet resultSet=null;
        try{
            if(connection!=null){
                Statement statement= connection.createStatement();
                resultSet= statement.executeQuery("select user.uid,user.name,doctor.specialization,doctor.verified\n" +
                        "from user Inner Join doctor on user.uid=doctor.docid\n" +
                        "where doctor.verified=0");

            }
        }
        catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return resultSet;
    }

    public void setVerified(Context context,int docId) {

        try{
            if(connection!=null){
                cs = (CallableStatement) connection.prepareCall("call setVerified(?)");
                cs.setInt(1,docId);
                cs.executeQuery();
            }
        }
        catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void removeDoctor(Context context,int docId){
        if(connection!=null){
            try {
                cs = (CallableStatement) connection.prepareCall("call deleteDoctor(?)");
                cs.setInt(1, docId);
                cs.executeQuery();
                Toast.makeText(context,"Deleted!",Toast.LENGTH_SHORT).show();
            }
            catch(SQLException e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public Doctor getDoctorComments(int docId , Context context){
        String query = "select  u.name , c.pComment from comments c join Patient p on c.pid = p.pid join `User` u on p.pid = u.uid\n" +
                "where c.docId = ?";

        Doctor d = new Doctor(null , null);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,docId);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                d.setComment(rs.getString("name") , rs.getString("pComment"));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return d;
    }

    public void updateVirtualSchedule(int aptId , Time stime ,Time etime , String day, Context context){
        String query = "update appSchedule set sTime =? , eTime = ? , day= ? where aptId = ?";

        try (PreparedStatement stmt1 = connection.prepareStatement(query)) {

            stmt1.setTime(1,stime);
            stmt1.setTime(2,etime);
            stmt1.setString(3,day);
            stmt1.setInt(4,aptId);
            stmt1.execute();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void insertVAppSchedule(Context context,VirtualAppointmentSchedule appointmentSchedule, int docId){
        int aptId=-1;
        try {
            if(connection!=null){
                cs = (CallableStatement) connection.prepareCall("call insertAppSchedule(?,?,?,?,?,?,?)");
                cs.setString(1,appointmentSchedule.getDay());
                cs.setFloat(2,appointmentSchedule.getFee());
                cs.setString(3,"v");
                cs.setInt(4,docId);
                cs.setTime(5,appointmentSchedule.getsTime());
                cs.setTime(6,appointmentSchedule.geteTime());
                cs.registerOutParameter(7,aptId);
                cs.executeQuery();

                aptId= cs.getInt(7);

                cs = (CallableStatement) connection.prepareCall("call insertVirtualAppSchedule(?)");
                cs.setInt(1,aptId);
                cs.executeQuery();

            }
        }
        catch(Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void insertPAppSchedule(Context context,PhysicalAppointmentSchedule physicalAppointmentSchedule,int docId){
        int aptId= -1;
        try{
            if(connection!=null) {

                //System.out.println(physicalAppointmentSchedule);
                cs = (CallableStatement) connection.prepareCall("call insertappschedule(?,?,?,?,?,?,?)");
                cs.setString(1,physicalAppointmentSchedule.getDay());
                cs.setFloat(2,physicalAppointmentSchedule.getFee());
                cs.setString(3,"p");
                cs.setInt(4,docId);
                cs.setTime(5,physicalAppointmentSchedule.getsTime());
                cs.setTime(6,physicalAppointmentSchedule.geteTime());
                cs.registerOutParameter(7,aptId);
                cs.executeQuery();

                aptId =cs.getInt(7);
                Toast.makeText(context,"Inserted in App table",Toast.LENGTH_SHORT).show();

                //insert in phyappschedule
                cs = (CallableStatement) connection.prepareCall("call insertphyappschedule (?,?,?,?,?)");
                cs.setInt(1, aptId);
                cs.setString(2, physicalAppointmentSchedule.getClinicName());
                cs.setDouble(3,physicalAppointmentSchedule.getLatts());
                cs.setDouble(4,physicalAppointmentSchedule.getLongs());
                cs.setString(5, physicalAppointmentSchedule.getAssistantPhNum());

                cs.executeQuery();
            }
        }
        catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ResultSet getAllVirAppointments(Context context,int docId){
        ResultSet resultSet = null;
        try{
            cs = (CallableStatement) connection.prepareCall("call getAllAppointments(?)");
            cs.setInt(1,docId);
            resultSet = cs.executeQuery();
        }
        catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resultSet;
    }

    public ResultSet getAllPhyAppointments(Context context,int docId){
        ResultSet resultSet=null;
        try{
            cs= (CallableStatement) connection.prepareCall("call getAllPhyAppointments(?)");
            cs.setInt(1,docId);
            resultSet= cs.executeQuery();
        }
        catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return resultSet;
    }

    public ResultSet getAllClinicNames(Context context, int docId) {
        ResultSet resultSet=null;
        try{
            cs = (CallableStatement) connection.prepareCall("call getAllClinicNames(?)");
            cs.setInt(1,docId);
            resultSet= cs.executeQuery();
        }
        catch (Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  resultSet;
    }


    public void removeAppSchedule(int aptId, Context context){
        String query= "delete from appSchedule where aptId= ?";
        try (PreparedStatement stmt1 = connection.prepareStatement(query)) {

            stmt1.setInt(1,aptId);
            stmt1.execute();


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<PhysicalAppointmentSchedule> getPhysicalSchDetails(Context context){

        String query = "select u.name , clinicName, latts, longs  from appschedule ap join phyappschedule p on ap.aptId = p.aptId join `User` u on ap.docId = u.uid";

        ArrayList<PhysicalAppointmentSchedule> lst = new ArrayList<>();

        try ( PreparedStatement stmt = connection.prepareStatement(query) ) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lst.add(new PhysicalAppointmentSchedule(rs.getString("clinicName"), rs.getDouble("latts"), rs.getDouble("longs"), rs.getString("name")));

            }


            return lst;

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return lst;
    }

    public ArrayList<Double> getLattsAndLongs(String clinicName , Context context){

        ArrayList<Double> coordinates = new ArrayList<>();
        String query = "select DISTINCT p.latts , p.longs from phyAppSchedule p where clinicName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1,clinicName);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                coordinates.add(rs.getDouble("latts"));
                coordinates.add(rs.getDouble("longs"));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return coordinates;
    }

    public ArrayList<Donor> getDonorDetails(Context context){
        ArrayList<Donor> donorDetails = new ArrayList<>();
        String query = "select * from `User` u join Donor d on u.uid = d.did";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {


            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                char gender = 'M';
                if(!rs.getBoolean("gender")){
                    gender = 'F';
                }

                donorDetails.add(new Donor(rs.getString("name"), null,null,gender, rs.getDate("dob"), rs.getString("bloodGroup"), rs.getString("phoneNum"), rs.getString("address")));
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return donorDetails;
    }

    public void insertDonor(Donor donor,int id,Context context) {
        String query="CALL insertDonor(?,?,?,?)";
        try{
            if(connection!=null)
            {
                cs= (CallableStatement) connection.prepareCall(query);
                cs.setInt(1,id);
                cs.setString(2,donor.getBloodGroup());
                cs.setString(3,donor.getAddress());
                cs.setString(4,donor.getPhNum());
                cs.executeQuery();
            }
        }
        catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}

