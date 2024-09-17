package za.co.varsitycollage.st10050487.knights;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "knights.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create USERS table
        String CREATE_TABLE_USERS = "CREATE TABLE USERS (" +
                "USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL," +
                "SURNAME TEXT NOT NULL," +
                "DATEOFBIRTH TEXT NOT NULL," +
                "EMAIL TEXT NOT NULL," +
                "ROLE_ID INTEGER," +
                "FOREIGN KEY (ROLE_ID) REFERENCES ROLES(ROLE_ID))";
        db.execSQL(CREATE_TABLE_USERS);

        // Create ROLES table
        String CREATE_TABLE_ROLES = "CREATE TABLE ROLES (" +
                "ROLE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ROLE TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_ROLES);

        // Create PLAYER_PROFILE table
        String CREATE_TABLE_PLAYER_PROFILE = "CREATE TABLE PLAYER_PROFILE (" +
                "PLAYER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL," +
                "SURNAME TEXT NOT NULL," +
                "NICKNAME TEXT NOT NULL," +
                "AGE INTEGER NOT NULL," +
                "GRADE TEXT NOT NULL," +
                "HEIGHT TEXT NOT NULL," +
                "POSITION TEXT NOT NULL," +
                "DATEOFBIRTH TEXT NOT NULL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_PLAYER_PROFILE);

        // Create TIMES table
        String CREATE_TABLE_TIMES = "CREATE TABLE TIMES (" +
                "TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "MEETING_TIME TEXT," +
                "BUS_DEPATURE_TIME TEXT," +
                "BUS_RETURN_TIME TEXT," +
                "MESSAGE TEXT)";
        db.execSQL(CREATE_TABLE_TIMES);

        // Create SCHOOL_MERCH table
        String CREATE_TABLE_SCHOOL_MERCH = "CREATE TABLE SCHOOL_MERCH (" +
                "PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL," +
                "PRICE REAL NOT NULL," +
                "PHOTO BLOB NOT NULL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_SCHOOL_MERCH);

        // Create BANNED_WORDS table
        String CREATE_TABLE_BANNED_WORDS = "CREATE TABLE BANNED_WORDS (" +
                "WORD_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "WORD TEXT NOT NULL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_BANNED_WORDS);

        // Create PREVIOUS_REPORTS table
        String CREATE_TABLE_PREVIOUS_REPORTS = "CREATE TABLE PREVIOUS_REPORTS (" +
                "REPORT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "REPORT_NAME TEXT NOT NULL," +
                "REPORT_DATE TEXT NOT NULL," +
                "REPORT_TYPE TEXT," +
                "REPORT_INFORMATION TEXT NOT NULL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_PREVIOUS_REPORTS);

        // Create EVENTS table
        String CREATE_TABLE_EVENTS = "CREATE TABLE EVENTS (" +
                "EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "EVENT_NAME TEXT NOT NULL," +
                "EVENT_DATE TEXT NOT NULL," +
                "EVENT_TIME TEXT NOT NULL," +
                "EVENT_LOCATION TEXT," +
                "EVENT_PRICE REAL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_EVENTS);

        // Create MATCHES table
        String CREATE_TABLE_MATCHES = "CREATE TABLE MATCHES (" +
                "MATCH_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "MATCH_LOCATION TEXT NOT NULL," +
                "MATCH_DATE TEXT NOT NULL," +
                "MATCH_TIME TEXT NOT NULL," +
                "PRICE REAL," +
                "MATCH_DISCRIPTION TEXT," +
                "PIICTURE BLOB," +
                "TIME_ID INTEGER NOT NULL," +
                "FOREIGN KEY (TIME_ID) REFERENCES TIMES(TIME_ID))";
        db.execSQL(CREATE_TABLE_MATCHES);

        // Create SPORT_FIXTURES table
        String CREATE_TABLE_SPORT_FIXTURES = "CREATE TABLE SPORT_FIXTURES (" +
                "FIXTURE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SPORT TEXT NOT NULL," +
                "HOME_TEAM TEXT NOT NULL," +
                "AWAY_TEAM TEXT NOT NULL," +
                "AGE_GROUP TEXT NOT NULL," +
                "LEAGUE TEXT NOT NULL," +
                "SET_DATE TEXT NOT NULL," +
                "SET_TIME TEXT NOT NULL," +
                "SET_LOCATION TEXT NOT NULL," +
                "HOME_LOGO BLOB," +
                "AWAY_LOGO BLOB," +
                "MATCH_ID INTEGER NOT NULL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (MATCH_ID) REFERENCES MATCHES(MATCH_ID)," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_SPORT_FIXTURES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS ROLES");
        db.execSQL("DROP TABLE IF EXISTS PLAYER_PROFILE");
        db.execSQL("DROP TABLE IF EXISTS TIMES");
        db.execSQL("DROP TABLE IF EXISTS SCHOOL_MERCH");
        db.execSQL("DROP TABLE IF EXISTS BANNED_WORDS");
        db.execSQL("DROP TABLE IF EXISTS PREVIOUS_REPORTS");
        db.execSQL("DROP TABLE IF EXISTS EVENTS");
        db.execSQL("DROP TABLE IF EXISTS MATCHES");
        db.execSQL("DROP TABLE IF EXISTS SPORT_FIXTURES");

        // Recreate tables
        onCreate(db);
    }
    //A method to add users to the database
    public void addUsers(String name, String surname, String dateOfBirth, String email, int roleId) {
        // Add users to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("SURNAME", surname);
        values.put("DATEOFBIRTH", dateOfBirth);
        values.put("EMAIL", email);
        values.put("ROLE_ID", roleId);
        db.insert("USERS", null, values);
    }
    //A method to add roles to the database
    public void addRoles(String role) {
        // Add roles to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ROLE", role);
        db.insert("ROLES", null, values);
    }
    //A method to add player profiles to the database
    public void addPlayerProfile(String name, String surname, String nickname, int age, String grade, String height, String position, String dateOfBirth, int userId) {
        // Add player profiles to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("SURNAME", surname);
        values.put("NICKNAME", nickname);
        values.put("AGE", age);
        values.put("GRADE", grade);
        values.put("HEIGHT", height);
        values.put("POSITION", position);
        values.put("DATEOFBIRTH", dateOfBirth);
        values.put("USER_ID", userId);
        db.insert("PLAYER_PROFILE", null, values);
    }
    //A method to add times to the database
    public void addTimes(String meetingTime, String busDepatureTime, String busReturnTime, String message) {
        // Add times to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MEETING_TIME", meetingTime);
        values.put("BUS_DEPATURE_TIME", busDepatureTime);
        values.put("BUS_RETURN_TIME", busReturnTime);
        values.put("MESSAGE", message);
        db.insert("TIMES", null, values);
    }
    //A method to add school merch to the database
    public void addSchoolMerch(String name, double price, byte[] photo, int userId) {
        // Add school merch to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("PRICE", price);
        values.put("PHOTO", photo);
        values.put("USER_ID", userId);
        db.insert("SCHOOL_MERCH", null, values);
    }
    //A method to add banned words to the database
    public void addBannedWords(String word, int userId) {
        // Add banned words to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("WORD", word);
        values.put("USER_ID", userId);
        db.insert("BANNED_WORDS", null, values);
    }
    //A method to add previous reports to the database
    public void addPreviousReports(String reportName, String reportDate, String reportType, String reportInformation, int userId) {
        // Add previous reports to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("REPORT_NAME", reportName);
        values.put("REPORT_DATE", reportDate);
        values.put("REPORT_TYPE", reportType);
        values.put("REPORT_INFORMATION", reportInformation);
        values.put("USER_ID", userId);
        db.insert("PREVIOUS_REPORTS", null, values);
    }
    //A method to add events to the database
    public void addEvents(String eventName, String eventDate, String eventTime, String eventLocation, double eventPrice, int userId) {
        // Add events to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EVENT_NAME", eventName);
        values.put("EVENT_DATE", eventDate);
        values.put("EVENT_TIME", eventTime);
        values.put("EVENT_LOCATION", eventLocation);
        values.put("EVENT_PRICE", eventPrice);
        values.put("USER_ID", userId);
        db.insert("EVENTS", null, values);
    }
    //A method to add matches to the database
    public void addMatches(String matchLocation, String matchDate, String matchTime, double price, String matchDiscription, byte[] picture, int timeId) {
        // Add matches to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MATCH_LOCATION", matchLocation);
        values.put("MATCH_DATE", matchDate);
        values.put("MATCH_TIME", matchTime);
        values.put("PRICE", price);
        values.put("MATCH_DISCRIPTION", matchDiscription);
        values.put("PIICTURE", picture);
        values.put("TIME_ID", timeId);
        db.insert("MATCHES", null, values);
    }
    //A method to add sport fixtures to the database
    public void addSportFixtures(String sport, String homeTeam, String awayTeam, String ageGroup, String league, String setDate, String setTime, String setLocation, byte[] homeLogo, byte[] awayLogo, int matchId, int userId) {
        // Add sport fixtures to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SPORT", sport);
        values.put("HOME_TEAM", homeTeam);
        values.put("AWAY_TEAM", awayTeam);
        values.put("AGE_GROUP", ageGroup);
        values.put("LEAGUE", league);
        values.put("SET_DATE", setDate);
        values.put("SET_TIME", setTime);
        values.put("SET_LOCATION", setLocation);
        values.put("HOME_LOGO", homeLogo);
        values.put("AWAY_LOGO", awayLogo);
        values.put("MATCH_ID", matchId);
        values.put("USER_ID", userId);
        db.insert("SPORT_FIXTURES", null, values);
    }
}
