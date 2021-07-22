/*
 * Student: Kshitij Agarwal
 * Final Project Phase 3
 * Instructor: Dr. Jessica Lin
 * Email: kagarwal@gmu.edu
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;

public class MovieDatabase
{
    private static Scanner scan;
    private ArrayList<Integer> numAccount;

    // DB connection properties
    private String driver = "oracle.jdbc.driver.OracleDriver";
    private String jdbc_url = "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
    private String username;
    private String password;

    public MovieDatabase(){}

    private Connection getConnection(){
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection (jdbc_url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void close(Connection connection) throws SQLException{
        try
        {
            connection.close();
        } catch (SQLException e)
        {
            throw e;
        }
    }

    private static void printMainMenu(){
        System.out.println("");
        System.out.println("1. View a Table");
        System.out.println("2. Insert New Record Into a Table");
        System.out.println("3. Update or Delete Record From a Table");
        System.out.println("4. Search Movies");
        System.out.println("5. Find a Member");
        System.out.println("0. Exit");
        System.out.print("Please enter your selection: ");
    }

    private static void printTables(){
        System.out.println("");
        System.out.println("1. Members");
        System.out.println("2. Profiles");
        System.out.println("3. Movies");
        System.out.println("4. Actors/Directors/Producers");
        System.out.println("5. History");
        System.out.println("0. Go Back");
        System.out.print("Please enter your selection: ");
    }

    public void printTable(Connection connection){
        printTables();
        int choice = scan.nextInt();

        Statement statement = null;
        StringBuffer sb = new StringBuffer();
        ResultSet rs = null;
        ArrayList collection = new ArrayList();

        try{
            while (choice != 0) {
                switch (choice) {
                    case 1:
                        sb.append("SELECT * FROM members");
                        statement = connection.createStatement();
                        rs = statement.executeQuery(sb.toString());
                        while (rs != null && rs.next())
                        {
//                            ArrayList answer = new ArrayList();
//                            answer.add(rs.getString("MEMBER_ID"));
//                            answer.add(rs.getString("FIRST_NAME"));
//                            answer.add(rs.getString("LAST_NAME"));
//                            answer.add(Integer.toString(rs.getInt("CREDIT_CARD")));
//                            collection.add(answer);

                            System.out.print(rs.getString("member_id"));
                            System.out.print(rs.getString("FIRST_NAME"));
                            System.out.print(rs.getString("LAST_NAME"));
                            System.out.print(rs.getInt("CREDIT_CARD"));
                            System.out.println();

                        }
                        choice = 0;
                        break;
                    case 2:
                        sb.append("SELECT * FROM profiles");
                        choice = 0;
                        break;
                    case 3:
                        sb.append("SELECT * FROM movies");
                        choice = 0;
                        break;
                    case 4:
                        sb.append("SELECT * FROM people");
                        choice = 0;
                        break;
                    case 5:
                        sb.append("SELECT * FROM history");
                        choice = 0;
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("That is an Invalid Selection!");
                        printTables();
                        choice = scan.nextInt();
                }
            }

            statement = connection.createStatement();
            System.out.println(statement.executeQuery(sb.toString()));
        } catch (Exception e){
        } finally{
            try{
                rs.close();
                statement.close();
            }catch(Exception e){}
        }
    }

    public void insert(Connection connection){
        printTables();
        int choice = scan.nextInt();

        Statement statement = null;
        StringBuffer sb = new StringBuffer();

        String s1, s2, s3, s4;

        try{
            while (choice != 0) {
                switch (choice) {
                    case 1:
                        System.out.print("Please Enter a 5 Character Account ID: ");
                        s1 = scan.next().concat("00000").substring(0,5);
                        System.out.print("Please Enter a First Name for the Account: ");
                        s2 = scan.next();
                        System.out.print("Please Enter a Last Name for the Account: ");
                        s3 = scan.next();
                        System.out.print("Please Enter the last 4 digits of the credit card on the Account: ");
                        s4 = scan.next();


                        sb.append("INSERT INTO member VLAUES('" + s1 + "', '"  + s2 + "', '"+ s3 + "', " + s4 + ")");
                        statement = connection.createStatement();
                        statement.executeUpdate (sb.toString());

                        choice = 0;
                        break;
                    case 2:
                        System.out.print("Please Enter the 5 Character Account ID: ");
                        s1 = scan.next().concat("00000").substring(0,5);
                        System.out.print("Please Enter a First Name for the Profile: ");
                        s2 = scan.next();
                        System.out.print("What Is your Favorite Genre: ");
                        s3 = scan.next();
                        System.out.print("'CHILD' or 'ADULT': ");
                        s4 = scan.next();


                        sb.append("INSERT INTO profile VLAUES('" + s1 + "', '"  + s2 + "', '"+ s3 + "', '" + s4 + "')");
                        statement = connection.createStatement();
                        statement.executeUpdate (sb.toString());

                        choice = 0;
                        break;
                    case 3:
                        System.out.print("Please Enter a 5 Character Movie ID: ");
                        s1 = scan.next().concat("00000").substring(0,5);
                        System.out.print("Please Enter the Name of the Movie: ");
                        s2 = scan.next();
                        System.out.print("What Year was it Released in: ");
                        s3 = scan.next();

                        sb.append("INSERT INTO movie VLAUES('" + s1 + "', '"  + s2 + "', "+ s3 + ", 0, 0)");
                        statement = connection.createStatement();
                        statement.executeUpdate (sb.toString());

                        choice = 0;
                        break;
                    case 4:
                        System.out.print("Please Enter A Person's 5 Character ID: ");
                        s1 = scan.next().concat("00000").substring(0,5);
                        System.out.print("Please Enter a 5 Character Movie ID: ");
                        s2 = scan.next().concat("00000").substring(0,5);
                        System.out.print("ACTR or DIRC or PROD: ");
                        s3 = scan.next();


                        sb.append("INSERT INTO role VLAUES('" + s1 + "', '"  + s2 + "', '"+ s3 + "')");
                        statement = connection.createStatement();
                        statement.executeUpdate (sb.toString());

                        choice = 0;
                        break;
                    case 5:
                        System.out.print("Please Enter the 5 Character Account ID: ");
                        s1 = scan.next().concat("00000").substring(0,5);
                        System.out.print("Please Enter the First Name for the Profile: ");
                        s2 = scan.next();
                        System.out.print("Please Enter a 5 Character Movie ID: ");
                        s3 = scan.next().concat("00000").substring(0,5);
                        System.out.print("Please Enter a Rating from 1 - 5 or NULL: ");
                        s4 = scan.next();


                        sb.append("INSERT INTO history VLAUES('" + s1 + "', '"  + s2 + "', '"+ s3 + "', " + s4 + ")");
                        statement = connection.createStatement();
                        statement.executeUpdate (sb.toString());

                        choice = 0;
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("That is an Invalid Selection!");
                        printTables();
                        choice = scan.nextInt();
                }
            }

            statement = connection.createStatement();
            System.out.println(statement.executeQuery(sb.toString()));
        } catch (Exception e){
        } finally{
            try{
                statement.close();
            }catch(Exception e){}
        }
    }

    public void update(Connection connection){

    }

    public static void main (String arg[]){
        scan = new Scanner(System.in);
        MovieDatabase movieDB = new MovieDatabase();

        System.out.print("Please Enter Your Oracle Database Username: ");
        movieDB.username = "kagarwal"; // = scan.next();
        System.out.print("Please Enter Your Oracle Database Password: ");
        movieDB.password = "azoackoa"; // = scan.next();
        Connection connection = movieDB.getConnection();

        try{
            movieDB.initialize(connection);
            System.out.println("Initial Values Have Been Set!");
        }catch (SQLException e){
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }

        System.out.println("\nWelcome to Your Movie Database!");
        printMainMenu();
        int choice = scan.nextInt();

        while (choice != 0){
            switch(choice){
                case 1:
                    movieDB.printTable(connection);
                    break;
                case 2:
                    movieDB.insert(connection);
                    break;
                case 3:
                    movieDB.update(connection);
                    break;
                case 4:
//                    movieDB.findMovie(connection);
                    break;
                case 5:
//                    movieDB.findMember(connection);
                    break;
                default:
                    System.out.println("That is an Invalid Selection!");
            }
            printMainMenu();
            choice = scan.nextInt();
        }


        try{
            movieDB.close(connection);
        }catch (SQLException e){
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }


    }

    private void initialize(Connection connection) throws SQLException{
        StringBuffer sbInsert;
        Statement statement = connection.createStatement();
        // PreparedStatement stmt = connection.prepareStatement();

        try
        {
            try{
                sbInsert = new StringBuffer();
                sbInsert.append("DROP TABLE member cascade constraint");
                statement = connection.createStatement();
                statement.executeUpdate (sbInsert.toString());
            } catch (SQLException e){
            }

            try{
                sbInsert = new StringBuffer();
                sbInsert.append("DROP TABLE profile cascade constraint");
                statement = connection.createStatement();
                statement.executeUpdate (sbInsert.toString());
            } catch (SQLException e){
            }

            try{
                sbInsert = new StringBuffer();
                sbInsert.append("DROP TABLE movie cascade constraint");
                statement = connection.createStatement();
                statement.executeUpdate (sbInsert.toString());
            } catch (SQLException e){
            }

            try{
                sbInsert = new StringBuffer();
                sbInsert.append("DROP TABLE genre cascade constraint");
                statement = connection.createStatement();
                statement.executeUpdate (sbInsert.toString());
            } catch (SQLException e){
            }

            try{
                sbInsert = new StringBuffer();
                sbInsert.append("DROP TABLE person cascade constraint");
                statement = connection.createStatement();
                statement.executeUpdate (sbInsert.toString());
            } catch (SQLException e){
            }

            try{
                sbInsert = new StringBuffer();
                sbInsert.append("DROP TABLE role cascade constraint");
                statement = connection.createStatement();
                statement.executeUpdate (sbInsert.toString());
            } catch (SQLException e){
            }

            try{
                sbInsert = new StringBuffer();
                sbInsert.append("DROP TABLE history cascade constraint");
                statement = connection.createStatement();
                statement.executeUpdate (sbInsert.toString());
            } catch (SQLException e){
            }

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE TABLE member(");
            sbInsert.append("member_id CHAR(5) NOT NULL,");
            sbInsert.append("first_name VARCHAR(20) NOT NULL,");
            sbInsert.append("last_name VARCHAR(20) NOT NULL,");
            sbInsert.append("credit_card INT NOT NULL,");
            sbInsert.append("PRIMARY KEY(member_id)");
            sbInsert.append(")");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE TABLE profile(");
            sbInsert.append("member_id CHAR(5) NOT NULL,");
            sbInsert.append("name VARCHAR(20) NOT NULL,");
            sbInsert.append("fav_genre VARCHAR(10) NOT NULL,");
            sbInsert.append("account_type CHAR(5) NOT NULL,");
            sbInsert.append("PRIMARY KEY (member_id, name)");
            sbInsert.append(")");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE TABLE movie(");
            sbInsert.append("movie_id CHAR(5) NOT NULL,");
            sbInsert.append("title VARCHAR(50) NOT NULL,");
            sbInsert.append("year INT NOT NULL,");
            sbInsert.append("avg_rating FLOAT DEFAULT 0.0,");
            sbInsert.append("rate_count INT DEFAULT 0,");
            sbInsert.append("PRIMARY KEY (movie_id)");
            sbInsert.append(")");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE TABLE genre(");
            sbInsert.append("movie_id CHAR(5) NOT NULL,");
            sbInsert.append("genre VARCHAR(10) NOT NULL");
            sbInsert.append(")");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE TABLE person(");
            sbInsert.append("pid CHAR(5) NOT NULL,");
            sbInsert.append("name VARCHAR(40) NOT NULL,");
            sbInsert.append("PRIMARY KEY (pid)");
            sbInsert.append(")");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE TABLE role(");
            sbInsert.append("pid CHAR(5) NOT NULL,");
            sbInsert.append("movie_id CHAR(5) NOT NULL,");
            sbInsert.append("role CHAR(4) NOT NULL");
            sbInsert.append(")");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE TABLE history(");
            sbInsert.append("member_id CHAR(5) NOT NULL,");
            sbInsert.append("pname VARCHAR(20) NOT NULL,");
            sbInsert.append("movie_id CHAR(5) NOT NULL,");
            sbInsert.append("rating INT");
            sbInsert.append(")");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("CREATE OR REPLACE TRIGGER avg_rat_update AFTER INSERT OR DELETE OR UPDATE ON history FOR EACH ROW WHEN (OLD.rating >= 1 OR NEW.rating >= 1) DECLARE rat FLOAT; cnt INT;");
            sbInsert.append("BEGIN IF UPDATING OR DELETING THEN SELECT rate_count into cnt FROM movie WHERE movie.movie_id = :OLD.movie_id; SELECT avg_rating into rat FROM movie WHERE movie.movie_id = :OLD.movie_id;");
            sbInsert.append("IF cnt = 1 THEN UPDATE movie SET movie.rate_count = 0 WHERE movie.movie_id = :OLD.movie_id; UPDATE movie SET movie.avg_rating = 0 WHERE movie.movie_id = :OLD.movie_id;");
            sbInsert.append("ELSE rat := (rat*cnt - :OLD.rating)/(cnt-1); UPDATE movie SET movie.rate_count = cnt - 1 WHERE movie.movie_id = :OLD.movie_id; UPDATE movie SET movie.avg_rating = rat WHERE movie.movie_id = :OLD.movie_id;");
            sbInsert.append("END IF; END IF;");
            sbInsert.append("IF UPDATING OR INSERTING THEN SELECT rate_count into cnt FROM movie WHERE movie.movie_id = :NEW.movie_id; SELECT avg_rating into rat FROM movie WHERE movie.movie_id = :NEW.movie_id;");
            sbInsert.append("rat := (rat*cnt + :NEW.rating)/(cnt+1); UPDATE movie SET movie.rate_count = cnt + 1 WHERE movie.movie_id = :NEW.movie_id; UPDATE movie SET movie.avg_rating = rat WHERE movie.movie_id = :NEW.movie_id;");
            sbInsert.append("END IF; END;");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());


            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO member VALUES ('MEM00', 'Micheal', 'Scott', 0916)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO member VALUES ('MEM01', 'Dwight', 'Schrute', 1234)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO member VALUES ('MEM02', 'Pam', 'Beesly', 4321)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO member VALUES ('MEM03', 'Jan', 'Levinson', 1872)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO member VALUES ('MEM04', 'Kelly', 'Kapoor', 3651)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM00', 'Micheal', 'Comedy', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM00', 'Holly', 'Horror', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM01', 'Dwight', 'Action', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM01', 'Angela', 'Drama', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM02', 'Pam', 'Drama', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM02', 'Jim', 'Comedy', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM02', 'Cece', 'Fantasy', 'CHILD')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM03', 'Jan', 'Thriller', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM03', 'Astrid', 'Fairy Tale', 'CHILD')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM04', 'Kelly', 'Drama', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO profile VALUES ('MEM04', 'Ryan', 'Horror', 'ADULT')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV00', 'Threat Level Midnight', 2019, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV01', 'Jumanji: The Next Level', 2019, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV02', 'Crazy Rich Asians', 2018, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV03', 'Cinderella', 2015, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV04', 'Avengers: Endgame', 2019, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV05', 'Bad Boys For Life', 2020, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV06', 'The Pursuit of Happyness', 2006, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV07', 'Contagion', 2011, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV08', 'Guardians of the Galaxy', 2014, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV09', 'Toy Story 3', 2010, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV10', 'Big Short', 2015, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV11', 'Dark Knight', 2008, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV12', 'Prestige', 2006, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV13', 'The Upside', 2017, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO movie VALUES ('MOV14', 'Fast and Furious', 2009, 0, 0)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());


            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID00', 'Steve Carell')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID01', 'Karen Gillan')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID02', 'Dwayne Johnson')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID03', 'Kevin Hart')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID04', 'Ken Jeong')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID05', 'Lily James')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID06', 'Chris Evans')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID07', 'Robert Downey Jr.')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID08', 'Chris Hemsworth')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID09', 'Christian Bale')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID10', 'Hugh Jackman')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID11', 'Scarlett Johansson')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID12', 'Brad Pitt')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID13', 'Heath Ledger')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID14', 'Will Smith')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID15', 'Jaydon Smith')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID16', 'Chadwick Boseman')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID17', 'Chris Pratt')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID18', 'Vin Diesel')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID19', 'Kate Winslet')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID20', 'Bryan Cranston')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID21', 'Keanu Reeves')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID22', 'Tucker Gates')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID23', 'Jake Kasdan')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID24', 'Russo Brothers')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID25', 'Jon Favreau')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID26', 'Bilall Fallah')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID27', 'Gwyneth Paltrow')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID28', 'Christopher Nolan')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID29', 'Neil Burger')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID30', 'Paul Walker')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO person VALUES ('PID31', 'Gal Gadot')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());


            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID00', 'MOV00', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID22', 'MOV00', 'DIRC')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID22', 'MOV00', 'PROD')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID02', 'MOV01', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID03', 'MOV01', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID02', 'MOV01', 'PROD')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID01', 'MOV01', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID23', 'MOV01', 'DIRC')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID04', 'MOV02', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID05', 'MOV03', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID07', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID24', 'MOV04', 'DIRC')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID06', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID08', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID04', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID01', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID16', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID27', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID17', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID25', 'MOV04', 'PROD')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID25', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID18', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID11', 'MOV04', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID14', 'MOV05', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID26', 'MOV05', 'DIRC')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID14', 'MOV06', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID15', 'MOV06', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID27', 'MOV07', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID20', 'MOV07', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID19', 'MOV07', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID17', 'MOV08', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID01', 'MOV08', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID18', 'MOV08', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID12', 'MOV10', 'PROD')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID12', 'MOV10', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID09', 'MOV10', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID01', 'MOV10', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID00', 'MOV10', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID01', 'MOV11', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID13', 'MOV11', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID28', 'MOV11', 'DIRC')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID28', 'MOV12', 'DIRC')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID10', 'MOV12', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID09', 'MOV12', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID11', 'MOV12', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID20', 'MOV13', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID03', 'MOV13', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID29', 'MOV13', 'DIRC')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID02', 'MOV14', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID31', 'MOV14', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID30', 'MOV14', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO role VALUES ('PID18', 'MOV14', 'ACTR')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());


            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV00', 'Comedy')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV00', 'Thriller')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV01', 'Adventure')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV01', 'Comedy')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV02', 'Comedy')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV02', 'Romance')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV03', 'Romance')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV03', 'Fantasy')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV04', 'Sci-fi')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV04', 'Action')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV05', 'Comedy')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV05', 'Action')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV06', 'Drama')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV07', 'Drama')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV07', 'Thriller')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV08', 'Action')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV08', 'Sci-fi')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV09', 'Family')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV09', 'Animation')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV10', 'Comedy')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV10', 'Drama')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV11', 'Adventure')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV11', 'Action')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV12', 'Thriller')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV12', 'Mystery')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV12', 'Drama')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV13', 'Comedy')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV13', 'Drama')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV14', 'Thriller')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO genre VALUES ('MOV14', 'Action')");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Micheal', 'MOV00', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Micheal', 'MOV05', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Micheal', 'MOV07', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Micheal', 'MOV04', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Micheal', 'MOV10', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Micheal', 'MOV11', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Micheal', 'MOV09', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Holly', 'MOV01', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Holly', 'MOV03', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Holly', 'MOV06', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Holly', 'MOV08', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM00', 'Holly', 'MOV09', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Dwight', 'MOV00', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Dwight', 'MOV03', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Dwight', 'MOV04', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Dwight', 'MOV06', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Angela', 'MOV09', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Angela', 'MOV10', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Angela', 'MOV11', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Angela', 'MOV12', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM01', 'Angela', 'MOV05', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV00', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV01', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV02', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV06', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV07', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV08', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV09', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV10', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV11', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Pam', 'MOV13', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Jim', 'MOV12', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Jim', 'MOV03', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Jim', 'MOV14', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Cece', 'MOV00', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM02', 'Cece', 'MOV03', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM03', 'Jan', 'MOV06', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM03', 'Jan', 'MOV07', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM03', 'Jan', 'MOV08', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM03', 'Jan', 'MOV09', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM03', 'Astrid', 'MOV02', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM03', 'Astrid', 'MOV03', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM03', 'Astrid', 'MOV06', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV00', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV01', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV02', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV03', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV04', 4)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV05', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV06', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV07', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV08', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV09', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV10', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV11', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV12', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV13', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Kelly', 'MOV14', 1)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Ryan', 'MOV10', 3)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Ryan', 'MOV11', 5)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());
            sbInsert = new StringBuffer();
            sbInsert.append("INSERT INTO history VALUES ('MEM04', 'Ryan', 'MOV14', 2)");
            statement = connection.createStatement();
            statement.executeUpdate (sbInsert.toString());

        } catch (SQLException e)
        {
            throw e;
        } finally
        {
            statement.close();
        }
    }








































    // // create and insert first record
    // System.out.println("Insert Album 1");
    // MovieDatabase musicDB = new MovieDatabase();
    // musicDB.setArtist("Bon Jovi");
    // musicDB.setAlbumTitle("These Days");
    // musicDB.setCategory("Rock n Roll");
    // musicDB.setMediaType("CD");
    // musicDB.setDescription("A pretty good rock album.");
    // musicDB.setReleaseDate(new Date());
    // try
    // {
    //     musicDB.insertData();
    // } catch (SQLException sqlException)
    // {
    //     while (sqlException != null)
    //     {
    //         sqlException.printStackTrace();
    //         sqlException = sqlException.getNextException();
    //     }
    // } catch (Exception e)
    // {
    //     e.printStackTrace();
    // }

    // // create and insert second record
    // System.out.println("Insert Album 2");
    // MovieDatabase musicDB2 = new MovieDatabase();
    // musicDB2.setArtist("Rush");
    // musicDB2.setAlbumTitle("Fly By Night");
    // musicDB2.setCategory("Hard Rock");
    // musicDB2.setMediaType("Record");
    // musicDB2.setDescription("A reall good rock album.");
    // musicDB2.setReleaseDate(new Date());
    // try
    // {
    //     musicDB2.insertData();
    // } catch (SQLException sqlException)
    // {
    //     while (sqlException != null)
    //     {
    //         sqlException.printStackTrace();
    //         sqlException = sqlException.getNextException();
    //     }
    // } catch (Exception e)
    // {
    //     e.printStackTrace();
    // }

    // // List the records
    // MovieDatabase[] db;
    // try {
    //     db = musicDB2.loadAll();
    //     System.out.println("\nAlbum \t Artist \t Description");
    //     System.out.println("----- \t ------ \t -----------");
    //     System.out.println(db.length);
    //     for (int i = 0; i <db.length; i++) {
    //         MovieDatabase mdb = db[i];
    //         System.out.println(mdb.getAlbumTitle() + "\t" + mdb.getArtist() + "\t" + mdb.getDescription());
    //     }
    // } catch (SQLException e) {
    //     e.printStackTrace();
    // }


    private String artist;
    private String albumTitle;
    private String category;
    private String mediaType;
    private String description;
    private Date releaseDate;

    //
    // Getter and setter methods
    //

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }



    /** Method to check if a table exists
     * @param connection java.sql.Connection object
     * @return true is the table exists, false otherwise
     * @throws SQLException
     */
    private boolean doesTableExist(Connection connection) throws SQLException {
        boolean bTableExists = false;

        DatabaseMetaData dmd = connection.getMetaData();
        ResultSet rs = dmd.getTables (null, null, "MUSIC_COLLECTION", null);
        if (rs.next()){
            bTableExists =  true;
        }
        rs.close(); // close the result set
        return bTableExists;
    }

    /** Method to create the MUSIC_COLLECTION table
     * @param connection java.sql.Connection object
     * @throws SQLException
     */
    private void createTable(Connection connection) throws SQLException
    {
        // create the SQL for the table
        StringBuffer sbCreate = new StringBuffer();
        sbCreate.append(" CREATE TABLE MUSIC_COLLECTION ");
        sbCreate.append(" ( ");
        sbCreate.append("     ARTIST VARCHAR(256), ");
        sbCreate.append("     ALBUM_TITLE VARCHAR(256), ");
        sbCreate.append("     CATEGORY VARCHAR(20),  ");
        sbCreate.append("     MEDIA_TYPE VARCHAR(20),  ");
        sbCreate.append("     DESCRIPTION VARCHAR(500),  ");
        sbCreate.append("     RELEASE_DATE DATE ");
        sbCreate.append(" ) ");

        // create the table
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate (sbCreate.toString());
        } catch (SQLException e) {
            throw e;
        } finally {
            statement.close();
        }
    }

    public MovieDatabase[] loadAll(Connection connection) throws SQLException{
        // get the connection
        // Connection connection = getConnection();

        // create the SELECT SQL
        StringBuffer sbSelect = new StringBuffer();
        sbSelect.append(" SELECT ARTIST, ALBUM_TITLE, CATEGORY, MEDIA_TYPE, DESCRIPTION, RELEASE_DATE FROM MUSIC_COLLECTION ");

        Statement statement = null;
        ResultSet rs = null;
        ArrayList collection = new ArrayList();
        try
        {
            // create the statement
            statement = connection.createStatement();
            // Insert the data
            rs = statement.executeQuery(sbSelect.toString());
            if (rs != null) {
                // when the resultset is not null, there are records returned
                while (rs.next())
                {
                    // loop through each result and store the data
                    // as an MovieDatabase object
                    MovieDatabase music = new MovieDatabase();
                    music.setArtist(rs.getString("ARTIST"));
                    music.setAlbumTitle(rs.getString("ALBUM_TITLE"));
                    music.setCategory(rs.getString("CATEGORY"));
                    music.setMediaType(rs.getString("MEDIA_TYPE"));
                    music.setDescription(rs.getString("DESCRIPTION"));
                    music.setReleaseDate(rs.getDate("RELEASE_DATE"));

                    // store it in the list
                    collection.add(music);
                }
            }
        } catch (SQLException e)
        {
            throw e;
        } finally
        {
            rs.close();
            statement.close();
            // close(connection);
        }

        //   return the array
        return (MovieDatabase[])collection.toArray(new MovieDatabase[0]);
    }

    public void insertData () throws SQLException{
        // get the connection
        Connection connection = getConnection();

        // check if table exists
        // if (!this.doesTableExist(connection))
        {
            // create the table
            System.out.println("MUSIC_COLLECTION Table doesn't exist. Creating it.....");
            createTable(connection);
        }

        // format the date
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getInstance();
        df.applyPattern("MM/dd/yyyy");
        String strReleaseDate = df.format(releaseDate);

        // create the INSERT SQL
        StringBuffer sbInsert = new StringBuffer();
        sbInsert.append(" INSERT INTO MUSIC_COLLECTION (ARTIST, ALBUM_TITLE, CATEGORY, MEDIA_TYPE, DESCRIPTION, RELEASE_DATE) ");
        sbInsert.append(" VALUES ");
        sbInsert.append(" ('" + artist + "', '" + albumTitle + "','" + category + "','" + mediaType + "', '" + description + "', TO_DATE('" + strReleaseDate + "', 'MM-DD-YYYY'))");

        // create the statement
        Statement statement = connection.createStatement();
        try
        {
            // Insert the data
            statement.executeUpdate (sbInsert.toString());
        } catch (SQLException e)
        {
            throw e;
        } finally
        {
            statement.close();
            // close(connection);
        }
    }
} 