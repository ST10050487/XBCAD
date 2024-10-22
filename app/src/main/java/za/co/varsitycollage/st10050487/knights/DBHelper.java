package za.co.varsitycollage.st10050487.knights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "knights.db";
    private static final int DATABASE_VERSION = 8;


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
                "PHOTO BLOB," +
                "PASSWORD TEXT NOT NULL," +
                "ROLE_ID INTEGER," +
                "FOREIGN KEY (ROLE_ID) REFERENCES ROLES(ROLE_ID))";
        db.execSQL(CREATE_TABLE_USERS);

        // Create ROLES table
        String CREATE_TABLE_ROLES = "CREATE TABLE ROLES (" +
                "ROLE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ROLE TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_ROLES);

        // Insert dummy data into ROLES table
        String INSERT_ROLES = "INSERT INTO ROLES (ROLE) VALUES " +
                "('Admin')," +
                "('Student')," +
                "('Parent')";
        db.execSQL(INSERT_ROLES);

        // Insert dummy data into USERS table
        String INSERT_USERS = "INSERT INTO USERS (NAME, SURNAME, DATEOFBIRTH, EMAIL, PASSWORD, ROLE_ID) VALUES " +
                "('John', 'Doe', '1990-01-01', 'john.doe@example.com', 'Password@123', 1)," +
                "('Jane', 'Smith', '1992-02-02', 'jane.smith@example.com', 'password456', 2)," +
                "('Alice', 'Johnson', '1994-03-03', 'alice.johnson@example.com', 'password789', 3)";
        db.execSQL(INSERT_USERS);

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
                "PICTURE BLOB," +
                "AGE_GROUP TEXT NOT NULL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_PLAYER_PROFILE);

        // Insert dummy data into PLAYER_PROFILE table
        String INSERT_PLAYER_PROFILE = "INSERT INTO PLAYER_PROFILE (NAME, SURNAME, NICKNAME, AGE, GRADE, HEIGHT, POSITION, DATEOFBIRTH, PICTURE, AGE_GROUP, USER_ID) VALUES " +
                "('Michael', 'Jordan', 'MJ', 15, 'Grade 10', '2m', 'Shooting Guard', '2007-06-18', NULL, 'Under 18', 1)," +
                "('Serena', 'Williams', 'Rena', 17, 'Grade 12', '1.3m', 'Tennis Player', '2005-09-26', NULL, 'Under 18', 2)," +
                "('Lionel', 'Messi', 'Leo', 16, 'Grade 11', '1.8m', 'Forward', '2006-06-24', NULL, 'Under 18', 3)";
        db.execSQL(INSERT_PLAYER_PROFILE);

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
                "DESCRIPTION TEXT NOT NULL," +
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
                "PICTURE BLOB," +
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
                "MATCH_LOCATION TEXT NOT NULL," +
                "MATCH_DATE TEXT NOT NULL," +
                "MATCH_TIME TEXT NOT NULL," +
                "PRICE REAL," +
                "MATCH_DISCRIPTION TEXT," +
                "PIICTURE BLOB," +
                "TIME_ID INTEGER NOT NULL," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (TIME_ID) REFERENCES TIMES(TIME_ID)," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID));";

        db.execSQL(CREATE_TABLE_SPORT_FIXTURES);
        //Creating the AGE_GROUP table
        String CREATE_TABLE_AGE_GROUP = "CREATE TABLE AGE_GROUP (" +
                "AGE_GROUP_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AGE_GROUP TEXT NOT NULL)";

        db.execSQL(CREATE_TABLE_AGE_GROUP);

        //Inserting data into the AGE_GROUP table
        String INSERT_AGE_GROUP = "INSERT INTO AGE_GROUP (AGE_GROUP) VALUES " +
                "('Boys Under 15')," +
                "('Girls Under 15')," +
                "('Boys Under 16')," +
                "('Girls Under 16')," +
                "('Boys Under 17')," +
                "('Girls Under 17')," +
                "('Boys Under 18')," +
                "('Girls Under 18')," +
                "('Open')";
        db.execSQL(INSERT_AGE_GROUP);
        //Creating the sport table
        String CREATE_TABLE_SPORT = "CREATE TABLE SPORT (" +
                "SPORT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SPORT TEXT NOT NULL)";

        db.execSQL(CREATE_TABLE_SPORT);

        //Inserting data into the SPORT table
        String INSERT_SPORT = "INSERT INTO SPORT (SPORT) VALUES " +
                "('Soccer')," +
                "('Netball')," +
                "('Rugby')," +
                "('Hockey')," +
                "('Cricket')," +
                "('Tennis')," +
                "('Basketball')," +
                "('Athletics')," +
                "('Swimming')";
        db.execSQL(INSERT_SPORT);
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
        db.execSQL("DROP TABLE IF EXISTS AGE_GROUP");
        db.execSQL("DROP TABLE IF EXISTS SPORT");
        // Recreate tables
        onCreate(db);
    }
    // HANNAH ADDED, CAUSE NO PASSWORD IN addUsers and to log user in ********************************/  /*********************************/  /*********************************/
    public boolean addUser(String name, String surname, String dateOfBirth, String email, String password, int roleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("SURNAME", surname);
        values.put("DATEOFBIRTH", dateOfBirth);
        values.put("EMAIL", email);
        values.put("PASSWORD", password);
        values.put("ROLE_ID", roleId);
        long result = db.insert("USERS", null, values);
        return result != -1;
    }
    public List<EventModel> getAllEvents() {
        List<EventModel> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EVENTS", null);

        if (cursor.moveToFirst()) {
            do {
                EventModel event = new EventModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("EVENT_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("EVENT_NAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("EVENT_DATE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("EVENT_TIME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("EVENT_LOCATION")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("EVENT_PRICE")),
                        false // Default value for 'selected'
                );
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    // Method to delete selected events
    public void deleteEvents(List<EventModel> selectedEvents) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (EventModel event : selectedEvents) {
                db.delete("EVENTS", "EVENT_ID = ?", new String[]{String.valueOf(event.getEventId())});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    //MUST ADD A PERMISSION TABLE TO ALLOW ADMIN TO ADD PERMISSIONS TO USERS
    public AdminModel getAdminUserDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "ROLE_ID = ?", new String[]{"1"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex("USER_ID");
            int nameIndex = cursor.getColumnIndex("NAME");
            int surnameIndex = cursor.getColumnIndex("SURNAME");
            int emailIndex = cursor.getColumnIndex("EMAIL");
            int photoIndex = cursor.getColumnIndex("PHOTO");
            int passwordIndex = cursor.getColumnIndex("PASSWORD");
            int dateOfBirthIndex = cursor.getColumnIndex("DATEOFBIRTH");

            if (userIdIndex >= 0 && nameIndex >= 0 && surnameIndex >= 0 && emailIndex >= 0 && passwordIndex >= 0 && dateOfBirthIndex >= 0) {
                AdminModel adminUser = new AdminModel(
                        cursor.getInt(userIdIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(surnameIndex),
                        cursor.getString(emailIndex),
                        cursor.getBlob(photoIndex),
                        cursor.getString(passwordIndex),
                        cursor.getString(dateOfBirthIndex),
                        true,
                        true,
                        true,
                        true
                );
                cursor.close();
                return adminUser;
            }
            cursor.close();
        }
        return null;
    }

    public int updateAdminUser(AdminModel adminUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", adminUser.getName());
        values.put("SURNAME", adminUser.getSurname());
        values.put("EMAIL", adminUser.getEmail());
        values.put("PHOTO", adminUser.getProfilePicture());
        values.put("PASSWORD", adminUser.getPassword());
        values.put("DATEOFBIRTH", adminUser.getDateOfBirth());

        // Update the row and return the number of rows affected
        return db.update("USERS", values, "USER_ID = ?", new String[]{String.valueOf(adminUser.getUserId())});
    }
    // Method to get user details
    public UserModel getUserDetails(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "ROLE_ID = ?", new String[]{"1"}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex("USER_ID");
            int nameIndex = cursor.getColumnIndex("NAME");
            int surnameIndex = cursor.getColumnIndex("SURNAME");
            int emailIndex = cursor.getColumnIndex("EMAIL");
            int photoIndex = cursor.getColumnIndex("PHOTO");
            int dateOfBirthIndex = cursor.getColumnIndex("DATEOFBIRTH");

            if  (userIdIndex >= 0 && nameIndex >= 0 && surnameIndex >= 0 && emailIndex >= 0 && dateOfBirthIndex >= 0) {
                UserModel user = new UserModel(
                        cursor.getInt(userIdIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(surnameIndex),
                        cursor.getBlob(photoIndex),
                        cursor.getString(emailIndex),
                        null, // No password
                        cursor.getString(dateOfBirthIndex)
                );
                cursor.close();
                return user;
            }
            cursor.close();
        }
        return null;
    }

    // Method to update user details
    public int updateUser(UserModel user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", user.getName());
        values.put("SURNAME", user.getSurname());
        values.put("EMAIL", user.getEmail());
        values.put("PHOTO", user.getProfilePicture());
        values.put("DATEOFBIRTH", user.getDateOfBirth());

        // Update the row and return the number of rows affected
        return db.update("USERS", values, "USER_ID = ?", new String[]{String.valueOf(user.getUserId())});
    }

    public List<String> getAllSports() {
        List<String> sportsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SPORT FROM SPORT", null);

        if (cursor.moveToFirst()) {
            do {
                sportsList.add(cursor.getString(cursor.getColumnIndexOrThrow("SPORT")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sportsList;
    }
    public List<String> getAllAgeGroups() {
        List<String> ageGroupList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AGE_GROUP FROM AGE_GROUP", null);

        if (cursor.moveToFirst()) {
            do {
                ageGroupList.add(cursor.getString(cursor.getColumnIndexOrThrow("AGE_GROUP")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ageGroupList;
    }
    public long addDummyFixtureWithUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SPORT", "Soccer");
        values.put("HOME_TEAM", "Team A");
        values.put("AWAY_TEAM", "Team B");
        values.put("AGE_GROUP", "Boys Under 18");
        values.put("LEAGUE", "Premier League");
        values.put("SET_DATE", "2023-10-01");
        values.put("SET_TIME", "15:00");
        values.put("SET_LOCATION", "Stadium A");
        values.put("HOME_LOGO", (byte[]) null); // Assuming no logo for dummy data
        values.put("AWAY_LOGO", (byte[]) null); // Assuming no logo for dummy data
        values.put("MATCH_LOCATION", "Stadium A");
        values.put("MATCH_DATE", "2023-10-01");
        values.put("MATCH_TIME", "15:00");
        values.put("PRICE", 10.0);
        values.put("MATCH_DISCRIPTION", "Friendly match");
        values.put("PIICTURE", (byte[]) null); // Assuming no picture for dummy data
        values.put("TIME_ID", 1); // Assuming a valid TIME_ID
        values.put("USER_ID", userId); // Link to the current user

       long fixid =  db.insert("SPORT_FIXTURES", null, values);
       return (fixid);
    }

    public FixtureModel getFixtureDetails(int fixtureId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM SPORT_FIXTURES WHERE FIXTURE_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(fixtureId)});

        if (cursor != null && cursor.moveToFirst()) {
            FixtureModel fixture = new FixtureModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("FIXTURE_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("SPORT")),
                    cursor.getString(cursor.getColumnIndexOrThrow("HOME_TEAM")),
                    cursor.getString(cursor.getColumnIndexOrThrow("AWAY_TEAM")),
                    cursor.getString(cursor.getColumnIndexOrThrow("AGE_GROUP")),
                    cursor.getString(cursor.getColumnIndexOrThrow("LEAGUE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("SET_DATE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("SET_TIME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("SET_LOCATION")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("HOME_LOGO")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("AWAY_LOGO")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_LOCATION")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DATE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_TIME")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DISCRIPTION")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("PIICTURE")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("TIME_ID"))
            );
            cursor.close();
            return fixture;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
    public long insertProduct(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("NAME", "School T-Shirt");
        values.put("DESCRIPTION", "A blue Bosmansdam hoodie with the school crest on the front, Bosmansdam Pride on the back, and white drawstrings. Simple and stylish");
        values.put("PRICE", 20.0); // Assuming a price for the dummy data
        values.put("PHOTO", new byte[0]); // Assuming no photo for dummy data
        values.put("USER_ID", userId);

        long newProductId = db.insert("SCHOOL_MERCH", null, values);
        return newProductId;
    }
    public int updateProduct(ProductModel prod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", prod.getProdName());
        values.put("DESCRIPTION", prod.getProdDescription());
        values.put("PRICE", prod.getProdPrice());
        values.put("PHOTO", prod.getProdPicture());
        values.put("USER_ID",prod.getUserId());

        return db.update("SCHOOL_MERCH", values, "PRODUCT_ID = ?", new String[]{String.valueOf(prod.getProdId())});
    }
    public ProductModel getProduct(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("SCHOOL_MERCH", null, "PRODUCT_ID = ?", new String[]{String.valueOf(productId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ProductModel product = new ProductModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("PRODUCT_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("PHOTO"))
            );
            cursor.close();
            return product;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
    /*********************************/  /*********************************/  /*********************************/  /*********************************/
    // A method to add users to the database
public void addUsers(String name, String surname, String dateOfBirth, String email, String password, int roleId) {
    // Add users to the database
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("NAME", name);
    values.put("SURNAME", surname);
    values.put("DATEOFBIRTH", dateOfBirth);
    values.put("EMAIL", email);
    values.put("PASSWORD", password);
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
    // Method to check if a user exists and retrieve USER_ID
    public Integer validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Integer userId = null; // Initialize userId to null

        try {
            // Query to check if user exists
            cursor = db.rawQuery("SELECT USER_ID FROM USERS WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});

            // Checking if cursor is not null and move to first
            if (cursor != null && cursor.moveToFirst()) {
                int userIdColumnIndex = cursor.getColumnIndex("USER_ID");
                if (userIdColumnIndex != -1) {
                    // Getting the USER_ID
                    userId = cursor.getInt(userIdColumnIndex);
                }
            }
        } finally {
            // Closing cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // Returning the USER_ID or null
        return userId;
    }
    // Method to check if a user exists and retrieve ROLE_ID
    public PlayerProfileModel getPlayerProfile(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM PLAYER_PROFILE WHERE USER_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            PlayerProfileModel playerProfile = new PlayerProfileModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("PLAYER_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("SURNAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NICKNAME")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("AGE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("GRADE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("HEIGHT")),
                    cursor.getString(cursor.getColumnIndexOrThrow("POSITION")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DATEOFBIRTH")),
                    cursor.getString(cursor.getColumnIndexOrThrow("AGE_GROUP")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE")) // Fetch profile picture as byte array
            );
            cursor.close();
            return playerProfile;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    //A method to update the player profile
    public int updatePlayerProfile(int playerId, String name, String surname, String nickname,
                                   int age, String dateOfBirth, String grade, String height,
                                   String position, String ageGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Fill the values to update
        values.put("NAME", name);
        values.put("SURNAME", surname);
        values.put("NICKNAME", nickname);
        values.put("AGE", age);
        values.put("DATEOFBIRTH", dateOfBirth);
        values.put("GRADE", grade);
        values.put("HEIGHT", height);
        values.put("POSITION", position);
        values.put("AGE_GROUP", ageGroup);

        // Update and return the number of rows affected
        return db.update("PLAYER_PROFILE", values, "PLAYER_ID = ?", new String[]{String.valueOf(playerId)});
    }
    // A method to update profile picture
    public int updatePlayerProfilePicture(int playerId, byte[] picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PICTURE", picture);

        return db.update("PLAYER_PROFILE", values, "PLAYER_ID = ?", new String[]{String.valueOf(playerId)});
    }
}
