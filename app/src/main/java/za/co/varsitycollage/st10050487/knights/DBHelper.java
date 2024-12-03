package za.co.varsitycollage.st10050487.knights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;
import android.util.Log;

import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "knights.db";
    private static final int DATABASE_VERSION = 27;


    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create USERS table
        String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS USERS (" +
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
        db.execSQL(CREATE_TABLE_PLAYER_PROFILE);    // Insert dummy player profiles into PLAYER_PROFILE table
        String INSERT_PLAYER_PROFILE = "INSERT INTO PLAYER_PROFILE (NAME, SURNAME, NICKNAME, AGE, GRADE, HEIGHT, POSITION, DATEOFBIRTH, PICTURE, AGE_GROUP, USER_ID) VALUES " +
                "('Michael', 'Jordan', 'MJ', 15, 'Grade 10', '2m', 'Shooting Guard', '2007-06-18', NULL, 'Under 18', 1)," +
                "('Serena', 'Williams', 'Rena', 17, 'Grade 12', '1.3m', 'Tennis Player', '2005-09-26', NULL, 'Under 18', 2)," +
                "('Lionel', 'Messi', 'Leo', 16, 'Grade 11', '1.8m', 'Forward', '2006-06-24', NULL, 'Under 18', 3)," +
                "('LeBron', 'James', 'King', 16, 'Grade 11', '2.1m', 'Small Forward', '2006-12-30', NULL, 'Under 18', 4)," +
                "('Roger', 'Federer', 'FedEx', 17, 'Grade 12', '1.85m', 'Tennis Player', '2005-08-08', NULL, 'Under 18', 5)," +
                "('Cristiano', 'Ronaldo', 'CR7', 17, 'Grade 12', '1.87m', 'Forward', '2005-02-05', NULL, 'Under 18', 6)," +
                "('Usain', 'Bolt', 'Lightning', 16, 'Grade 11', '1.95m', 'Sprinter', '2006-08-21', NULL, 'Under 18', 7)," +
                "('Tom', 'Brady', 'TB12', 17, 'Grade 12', '1.93m', 'Quarterback', '2005-08-03', NULL, 'Under 18', 8)," +
                "('Tiger', 'Woods', 'Tiger', 17, 'Grade 12', '1.85m', 'Golfer', '2005-12-30', NULL, 'Under 18', 9)," +
                "('Kobe', 'Bryant', 'Black Mamba', 16, 'Grade 11', '1.98m', 'Shooting Guard', '2006-08-23', NULL, 'Under 18', 10)";
        db.execSQL(INSERT_PLAYER_PROFILE);


        String CREATE_TABLE_FIXTURE_PLAYERS = "CREATE TABLE IF NOT EXISTS FIXTURE_PLAYERS (" +
                "FIXTURE_PLAYER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FIXTURE_ID INTEGER NOT NULL," +
                "PLAYER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (FIXTURE_ID) REFERENCES SPORT_FIXTURES(FIXTURE_ID)," +
                "FOREIGN KEY (PLAYER_ID) REFERENCES PLAYER_PROFILE(PLAYER_ID))";
        db.execSQL(CREATE_TABLE_FIXTURE_PLAYERS);

// Step 2: Insert corresponding entries Player Profiles for Fixture (ID)1
        String INSERT_FIXTURE_PLAYERS = "INSERT INTO FIXTURE_PLAYERS (FIXTURE_ID, PLAYER_ID) VALUES " +
                "(1, 1)," +
                "(1, 2)," +
                "(1, 3)," +
                "(1, 4)," +
                "(1, 5)," +
                "(1, 6)," +
                "(1, 7)," +
                "(1, 8)," +
                "(1, 9)," +
                "(1, 10)";
        db.execSQL(INSERT_FIXTURE_PLAYERS);

        String CREATE_TABLE_TIME_HIGHLIGHTS = "CREATE TABLE IF NOT EXISTS TIME_HIGHLIGHTS (" +
                "HIGHLIGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TIMES_ID INTEGER," +
                "PHOTO BLOB," +
                "FOREIGN KEY (TIMES_ID) REFERENCES TIMES (TIMES_ID))";
        db.execSQL(CREATE_TABLE_TIME_HIGHLIGHTS);

        // Create TIMES table
        String CREATE_TABLE_TIMES = "CREATE TABLE TIMES (" +
                "TIME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "MEETING_TIME TEXT," +
                "BUS_DEPATURE_TIME TEXT," +
                "BUS_RETURN_TIME TEXT," +
                "MESSAGE TEXT," +
                "MAN_OF_THE_MATCH TEXT," +
                "HOME_SCORE INTEGER," +
                "AWAY_SCORE INTEGER," +
                "FIXTURE_ID INTEGER NOT NULL," + // Ensure this line exists
                "MATCH_STATUS TEXT NOT NULL," +
                "MATCH_STATUS_ID INTEGER," +
                "FOREIGN KEY (FIXTURE_ID) REFERENCES SPORT_FIXTURES(FIXTURE_ID)," + // Foreign key reference
                "FOREIGN KEY (MATCH_STATUS_ID) REFERENCES MATCH_STATUS(MATCH_STATUS_ID)" +
                ");";
        db.execSQL(CREATE_TABLE_TIMES);

        // Create SCHOOL_MERCH table
        String CREATE_TABLE_SCHOOL_MERCH = "CREATE TABLE SCHOOL_MERCH (" +
                "PRODUCT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL," +
                "PRICE REAL NOT NULL," +
                "DESCRIPTION TEXT NOT NULL," +
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
                "EVENT_DESCRIPTION TEXT," +
                "PICTURE BLOB," +
                "USER_ID INTEGER NOT NULL," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_EVENTS);

// Create SPORT_FIXTURES table
// Create SPORT_FIXTURES table
        String CREATE_TABLE_SPORT_FIXTURES = "CREATE TABLE SPORT_FIXTURES (" +
                "FIXTURE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SPORT TEXT NOT NULL," +
                "HOME_TEAM TEXT NOT NULL," +
                "AWAY_TEAM TEXT NOT NULL," +
                "AGE_GROUP TEXT NOT NULL," +
                "LEAGUE TEXT NOT NULL," +
                "HOME_LOGO BLOB," +
                "AWAY_LOGO BLOB," +
                "MATCH_LOCATION TEXT NOT NULL," +
                "MATCH_DATE TEXT NOT NULL," +
                "MATCH_TIME TEXT NOT NULL," +
                "SET_TIME TEXT NOT NULL," +
                "SET_DATE TEXT NOT NULL," +
                "MATCH_DESCRIPTION TEXT," +
                "PICTURE BLOB," +
                "USER_ID INTEGER NOT NULL," +
                "LEAGUE_ID INTEGER," +
                "MATCH_STATUS_ID INTEGER," +
                "IS_HOME_GAME INTEGER DEFAULT 0," +
                "IS_PAST INTEGER DEFAULT 0, " + // Add this line
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)," +
                "FOREIGN KEY (LEAGUE_ID) REFERENCES HIGH_SCHOOL_LEAGUE(LEAGUE_ID)," +
                "FOREIGN KEY (MATCH_STATUS_ID) REFERENCES MATCH_STATUS(MATCH_STATUS_ID))";
        db.execSQL(CREATE_TABLE_SPORT_FIXTURES);

        // Create AGE_GROUP table
        String CREATE_TABLE_AGE_GROUP = "CREATE TABLE AGE_GROUP (" +
                "AGE_GROUP_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AGE_GROUP TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_AGE_GROUP);

        // Insert data into AGE_GROUP table
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

        // Create SPORT table
        String CREATE_TABLE_SPORT = "CREATE TABLE SPORT (" +
                "SPORT_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "SPORT TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_SPORT);

        // Insert data into SPORT table
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
        // Corrected table creation and insert statements
        String CREATE_TABLE_TIME_STATUS = "CREATE TABLE TIME_STATUS (" +
                "TIME_STATUS_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "STATUS TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_TIME_STATUS);

        String INSERT_TIME_STATUS = "INSERT INTO TIME_STATUS (STATUS) VALUES " +
                "('Not Started')," +
                "('Full-time')," +
                "('Half-time')";
        db.execSQL(INSERT_TIME_STATUS);

        // Create HIGH_SCHOOL_LEAGUE table
        String CREATE_TABLE_HIGH_SCHOOL_LEAGUE = "CREATE TABLE HIGH_SCHOOL_LEAGUE (" +
                "LEAGUE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "LEAGUE TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_HIGH_SCHOOL_LEAGUE);

        // Insert data into HIGH_SCHOOL_LEAGUE table
        String INSERT_HIGH_SCHOOL_LEAGUE = "INSERT INTO HIGH_SCHOOL_LEAGUE (LEAGUE) VALUES " +
                "('WP League')," +
                "('Inter-School')," +
                "('Provincial')," +
                "('National')," +
                "('International')," +
                "('Friendly')," +
                "('Tournament')";
        db.execSQL(INSERT_HIGH_SCHOOL_LEAGUE);

        // Creating the Match Status table
        String CREATE_TABLE_MATCH_STATUS = "CREATE TABLE MATCH_STATUS (" +
                "MATCH_STATUS_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "MATCH_STATUS TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_MATCH_STATUS);

        // Inserting data into the Match Status table
        String INSERT_MATCH_STATUS = "INSERT INTO MATCH_STATUS (MATCH_STATUS) VALUES " +
                "('Upcoming')," +
                "('First Half')," +
                "('Half Time')," +
                "('Second Half')," +
                "('Match Over')," +
                "('Cancelled')";
        db.execSQL(CREATE_TABLE_USERS);

        // Create SUSPICIOUS_ACTIVITY table
        String CREATE_TABLE_SUSPICIOUS_ACTIVITY = "CREATE TABLE IF NOT EXISTS  SUSPICIOUS_ACTIVITY (" +
                "ACTIVITY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USER_ID INTEGER," +
                "ACTIVITY_DESCRIPTION TEXT," +
                "TIMESTAMP INTEGER," +
                "FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))";
        db.execSQL(CREATE_TABLE_SUSPICIOUS_ACTIVITY);
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
        db.execSQL("DROP TABLE IF EXISTS HIGH_SCHOOL_LEAGUE");
        db.execSQL("DROP TABLE IF EXISTS MATCH_STATUS");
        db.execSQL("DROP TABLE IF EXISTS FIXTURE_PLAYERS");
        db.execSQL("DROP TABLE IF EXISTS TIME_HIGHLIGHTS");
        db.execSQL("DROP TABLE IF EXISTS TIME_STATUS");
        db.execSQL("DROP TABLE IF EXISTS SUSPICIOUS_ACTIVITY");

        // Recreate tables
        onCreate(db);
    }

    //    @Override
//    public void onOpen(SQLiteDatabase db) {
//        super.onOpen(db);
//        db.execSQL("PRAGMA foreign_keys=ON;");
//    }
//
//    public synchronized SQLiteDatabase getWritableDatabase() {
//        return super.getWritableDatabase(DATABASE_PASSWORD);
//    }
//
//    public synchronized SQLiteDatabase getReadableDatabase() {
//        SQLiteDatabase db = null;
//        try {
//            db = super.getReadableDatabase(DATABASE_PASSWORD);
//        } catch (Exception e) {
//            Log.e("DBHelper", "Error opening readable database", e);
//        }
//        return db;
//    }
//    public boolean isDatabaseValid() {
//        File dbFile = context.getDatabasePath(DATABASE_NAME);
//        if (!dbFile.exists()) {
//            return false;
//        }
//
//        SQLiteDatabase db = null;
//        try {
//            db = getReadableDatabase();
//            Cursor cursor = db.rawQuery("PRAGMA integrity_check;", null);
//            if (cursor != null) {
//                if (cursor.moveToFirst()) {
//                    String result = cursor.getString(0);
//                    cursor.close();
//                    return "ok".equalsIgnoreCase(result);
//                }
//                cursor.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//        return false;
//    }
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

    //__Suspicious Activity Table CRUD_________________________________________________________________________________\\
    public void addSuspiciousActivity(int userId, String activityDescription, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USER_ID", userId);
        values.put("ACTIVITY_DESCRIPTION", activityDescription);
        values.put("TIMESTAMP", timestamp);
        db.insert("SUSPICIOUS_ACTIVITY", null, values);
    }

    public void addSuspiciousActivity(String userId, String activityDescription, long timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UNKNOWN", userId);
        values.put("ACTIVITY_DESCRIPTION", activityDescription);
        values.put("TIMESTAMP", timestamp);
        db.insert("SUSPICIOUS_ACTIVITY", null, values);
    }

    public Integer getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Integer userId = null;
        String query = "SELECT USER_ID FROM USERS WHERE EMAIL = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID"));
        }
        cursor.close();
        db.close();
        return userId;
    }

    public List<EventModel> getAllEvents() {
        List<EventModel> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EVENTS", null);

        if (cursor.moveToFirst()) {
            do {
                int eventId = cursor.getInt(cursor.getColumnIndexOrThrow("EVENT_ID"));
                String eventName = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_NAME"));
                String eventDate = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_DATE"));
                String eventTime = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_TIME"));
                String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_LOCATION"));
                double eventPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("EVENT_PRICE"));
                byte[] pictures = cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"));
                String eventDescription = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_DESCRIPTION"));

                if (pictures == null) {
                    pictures = new byte[0]; // or any default value
                }

                EventModel event = new EventModel(
                        eventId,
                        eventName,
                        eventDate,
                        eventTime,
                        eventLocation,
                        eventPrice,
                        pictures,
                        eventDescription,
                        false
                );
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    public List<String> getAllStatus() {
        List<String> status = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT TIME_STATUS FROM TIME_STATUS", null);

        if (cursor.moveToFirst()) {
            do {
                status.add(cursor.getString(cursor.getColumnIndexOrThrow("LEAGUE")));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHelper", "No status found in the database.");
        }
        cursor.close();
        Log.d("DBHelper", "status: " + status);
        return status;
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

    //Admin User
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

    // User
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

            if (userIdIndex >= 0 && nameIndex >= 0 && surnameIndex >= 0 && emailIndex >= 0 && dateOfBirthIndex >= 0) {
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

    // TIMES
    public boolean addTimes(String meetingTime, String busDepartureTime, String busReturnTime, String message, int matchStatus, long fixtureID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MEETING_TIME", meetingTime);
        values.put("BUS_DEPATURE_TIME", busDepartureTime);
        values.put("BUS_RETURN_TIME", busReturnTime);
        values.put("MESSAGE", message);
        values.put("MATCH_STATUS", matchStatus);
        values.put("FIXTURE_ID", fixtureID);

        // Insert the new row, returning the primary key value of the new row
        long result = db.insert("TIMES", null, values);

        // Check if the insert was successful
        return result != -1; // If result is -1, the insert failed
    }

    public void addDummyTimesEntry(int fixtureId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FIXTURE_ID", fixtureId);
        values.put("LEAGUE_ID", 1);
        values.put("TIMES_STATUS_ID", 1);
        values.put("FIXTURE_ID", fixtureId);
        values.put("MEETING_TIME", "2023-10-01 14:00");
        values.put("BUS_DEPATURE_TIME", "2023-10-01 13:00");
        values.put("BUS_RETURN_TIME", "2023-10-01 18:00");
        values.put("MESSAGE", "This is a dummy message for the times entry.");
        values.put("HOME_SCORE", 2); // Assuming a dummy home score
        values.put("AWAY_SCORE", 1); // Assuming a dummy away score
        db.insert("TIMES", null, values);
    }

    public TimesheetModel getTimesDetails(int fixtureId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM TIMES WHERE FIXTURE_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(fixtureId)});

        if (cursor != null && cursor.moveToFirst()) {
            TimesheetModel timesheet = new TimesheetModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("TIME_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("FIXTURE_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("MATCH_STATUS")), // Use MATCH_STATUS as an integer
                    cursor.getString(cursor.getColumnIndexOrThrow("MEETING_TIME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("BUS_DEPATURE_TIME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("BUS_RETURN_TIME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MAN_OF_THE_MATCH")), // Adjusted to match your data class
                    cursor.isNull(cursor.getColumnIndexOrThrow("HOME_SCORE")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("HOME_SCORE")),
                    cursor.isNull(cursor.getColumnIndexOrThrow("AWAY_SCORE")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("AWAY_SCORE"))
            );
            cursor.close();
            return timesheet;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public int updateTimesheet(TimesheetModel timesheet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MEETING_TIME", timesheet.getMeetTime());
        values.put("BUS_DEPATURE_TIME", timesheet.getBusDepartureTime());
        values.put("BUS_RETURN_TIME", timesheet.getBusReturnTime());
        values.put("MESSAGE", timesheet.getMessage());
        values.put("HOME_SCORE", timesheet.getHomeScore());
        values.put("AWAY_SCORE", timesheet.getAwayScore());

        return db.update("TIMES", values, "TIME_ID = ?", new String[]{String.valueOf(timesheet.getTimeId())});
    }

    public long addHighlight(int timesId, byte[] photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TIMES_ID", timesId);
        values.put("PHOTO", photo);
        return db.insert("TIME_HIGHLIGHTS", null, values);
    }

    public List<byte[]> getHighlights(int timesId) {
        List<byte[]> highlights = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("TIME_HIGHLIGHTS", new String[]{"PHOTO"}, "TIMES_ID = ?", new String[]{String.valueOf(timesId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                highlights.add(cursor.getBlob(cursor.getColumnIndexOrThrow("PHOTO")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return highlights;
    }

    public void updateHighlights(int timesId, List<byte[]> photos) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Delete existing highlights for the given timesId
            db.delete("TIME_HIGHLIGHTS", "TIMES_ID = ?", new String[]{String.valueOf(timesId)});

            // Insert new photos
            ContentValues values = new ContentValues();
            for (byte[] photo : photos) {
                values.clear();
                values.put("TIMES_ID", timesId);
                values.put("PHOTO", photo);
                db.insert("TIME_HIGHLIGHTS", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // Method to delete a highlight from the TIME_HIGHLIGHTS table
    public boolean deleteHighlights(int timesId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("TIME_HIGHLIGHTS", "TIMES_ID = ?", new String[]{String.valueOf(timesId)}) > 0;
    }

    // Method to delete a times entry from the TIMES table
    public boolean deleteTimes(int timeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("TIMES", "TIME_ID = ?", new String[]{String.valueOf(timeId)}) > 0;
    }

    // Method to create a new time status
    public long createTimeStatus(String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STATUS", status);
        return db.insert("TIME_STATUS", null, values);
    }

    // Method to update an existing time status
    public int updateTimeStatus(int timeStatusId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STATUS", newStatus);
        return db.update("TIME_STATUS", values, "TIME_STATUS_ID = ?", new String[]{String.valueOf(timeStatusId)});
    }


    //Fixture
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

    public int updateFixture(FixtureModel fixture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SPORT", fixture.getSport());
        values.put("HOME_TEAM", fixture.getHomeTeam());
        values.put("AWAY_TEAM", fixture.getAwayTeam());
        values.put("AGE_GROUP", fixture.getAgeGroup());
        values.put("LEAGUE", fixture.getLeague());
        values.put("MATCH_LOCATION", fixture.getMatchLocation());
        values.put("MATCH_DATE", fixture.getMatchDate());
        values.put("MATCH_TIME", fixture.getMatchTime());
        values.put("MATCH_DESCRIPTION", fixture.getMatchDescription());
        values.put("HOME_LOGO", fixture.getHomeLogo());
        values.put("AWAY_LOGO", fixture.getAwayLogo());
        values.put("PICTURE", fixture.getPicture());
        values.put("USER_ID", fixture.getUserId());
        values.put("LEAGUE_ID", fixture.getLeagueId());
        values.put("IS_HOME_GAME", fixture.isHomeGame() ? 1 : 0); // Add this line

        // Update the row and return the number of rows affected
        return db.update("SPORT_FIXTURES", values, "FIXTURE_ID = ?", new String[]{String.valueOf(fixture.getFixtureId())});
    }

    public long addDummyFixtureWithUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SPORT", "Soccer");
        values.put("HOME_TEAM", "Team A");
        values.put("AWAY_TEAM", "Team B");
        values.put("AGE_GROUP", "Boys Under 18");
        values.put("LEAGUE", "Premier League");
        values.put("HOME_LOGO", (byte[]) null); // Assuming no logo for dummy data
        values.put("AWAY_LOGO", (byte[]) null); // Assuming no logo for dummy data
        values.put("MATCH_LOCATION", "Stadium A");
        values.put("MATCH_DATE", "2023-10-01");
        values.put("MATCH_TIME", "15:00");
        values.put("MATCH_DESCRIPTION", "Friendly match");
        values.put("PICTURE", (byte[]) null); // Assuming no picture for dummy data
        values.put("USER_ID", userId); // Link to the current user
        values.put("LEAGUE_ID", 1); // Assuming a valid LEAGUE_ID
        values.put("IS_HOME_GAME", 0); // Add this line

        long fixid = db.insert("SPORT_FIXTURES", null, values);
        return fixid;
    }

    // A method to get the match staus
    public String getMatchStatus(int matchStatusId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String matchStatus = null;
        String query = "SELECT MATCH_STATUS FROM MATCH_STATUS WHERE MATCH_STATUS_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(matchStatusId)});

        if (cursor.moveToFirst()) {
            matchStatus = cursor.getString(cursor.getColumnIndexOrThrow("MATCH_STATUS"));
        }
        cursor.close();
        return matchStatus;
    }

    public ScoresModel getScores(int fixtureId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT HOME_SCORE, AWAY_SCORE FROM TIMES WHERE FIXTURE_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(fixtureId)});

        ScoresModel scores = null;
        if (cursor.moveToFirst()) {
            int homeScore = cursor.getInt(cursor.getColumnIndexOrThrow("HOME_SCORE"));
            int awayScore = cursor.getInt(cursor.getColumnIndexOrThrow("AWAY_SCORE"));
            scores = new ScoresModel(homeScore, awayScore);
        }
        cursor.close();
        return scores;
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
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_LOCATION")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DATE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_TIME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DESCRIPTION")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("HOME_LOGO")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("AWAY_LOGO")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("LEAGUE_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("IS_HOME_GAME")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("MATCH_STATUS_ID"))

            );
            cursor.close();
            return fixture;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    //Get All Fixtures details by using the Fixture ID and this created by Vicky
    public List<FixtureModel> getAllFixtures() {
        List<FixtureModel> fixtures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get all fixtures sorted by FIXTURE_ID in descending order
        Cursor cursor = db.rawQuery("SELECT * FROM SPORT_FIXTURES ORDER BY FIXTURE_ID DESC", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                FixtureModel fixture = new FixtureModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("FIXTURE_ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SPORT")),
                        cursor.getString(cursor.getColumnIndexOrThrow("HOME_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("AWAY_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("AGE_GROUP")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LEAGUE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_LOCATION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DATE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_TIME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DESCRIPTION")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("HOME_LOGO")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("AWAY_LOGO")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("LEAGUE_ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("IS_HOME_GAME")) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow("MATCH_STATUS_ID"))
                );
                fixtures.add(fixture);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return fixtures;
    }

    //Vicky Implemented
    public void markFixtureAsPast(int fixtureId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IS_PAST", 1); // Mark as past
        db.update("SPORT_FIXTURES", values, "FIXTURE_ID = ?", new String[]{String.valueOf(fixtureId)});
    }

    public void addPlayerToFixture(int fixtureId, int playerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FIXTURE_ID", fixtureId);
        values.put("PLAYER_ID", playerId);
        db.insert("FIXTURE_PLAYERS", null, values);
    }

// PLAYERS

    public List<PlayerProfileModel> getAllPlayers() {
        List<PlayerProfileModel> players = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PLAYER_PROFILE", null);

        if (cursor.moveToFirst()) {
            do {
                PlayerProfileModel player = new PlayerProfileModel(
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
                        false,
                        cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"))


                );
                players.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }

    public List<PlayerProfileModel> getAllFixturePlayers(int fixtureId) {
        List<PlayerProfileModel> players = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM PLAYER_PROFILE pp " +
                "INNER JOIN FIXTURE_PLAYERS fp ON pp.PLAYER_ID = fp.PLAYER_ID " +
                "WHERE fp.FIXTURE_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(fixtureId)});

        if (cursor.moveToFirst()) {
            do {
                PlayerProfileModel player = new PlayerProfileModel(
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
                        true,
                        cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"))
                );
                players.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return players;
    }

    public void updateFixturePlayers(int fixtureId, List<Integer> selectedPlayers) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Delete existing players for the fixture
            db.delete("FIXTURE_PLAYERS", "FIXTURE_ID = ?", new String[]{String.valueOf(fixtureId)});

            // Insert new players for the fixture
            for (int playerId : selectedPlayers) {
                ContentValues values = new ContentValues();
                values.put("FIXTURE_ID", fixtureId);
                values.put("PLAYER_ID", playerId);
                db.insert("FIXTURE_PLAYERS", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
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

    // MERCH / PRODUCT
    public long dummyProduct(int userId) {
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

    public int updateProduct(ProductModel prod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", prod.getProdName());
        values.put("DESCRIPTION", prod.getProdDescription());
        values.put("PRICE", prod.getProdPrice());
        values.put("PHOTO", prod.getProdPicture());
        values.put("USER_ID", prod.getUserId());

        return db.update("SCHOOL_MERCH", values, "PRODUCT_ID = ?", new String[]{String.valueOf(prod.getProdId())});
    }

    // Method to delete a product using the passed product ID
    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("SCHOOL_MERCH", "PRODUCT_ID = ?", new String[]{String.valueOf(productId)}) > 0;
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


/*********************************/  /*********************************/  /*********************************/
    /*********************************/
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


    public UserModel getUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERS", null, "USER_ID = ?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            UserModel user = new UserModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("SURNAME")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("PHOTO")),
                    cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DATEOFBIRTH"))
            );
            cursor.close();
            return user;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    //A method to add roles to the database
    public void addRoles(String role) {
        // Add roles to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ROLE", role);
        db.insert("ROLES", null, values);
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
        Integer userId = null;

        try {
            // Query to check if user exists
            cursor = db.rawQuery("SELECT USER_ID, PASSWORD FROM USERS WHERE EMAIL=?", new String[]{email});
            // Checking if cursor is not null and move to first
            if (cursor != null && cursor.moveToFirst()) {
                int userIdColumnIndex = cursor.getColumnIndex("USER_ID");
                int passwordColumnIndex = cursor.getColumnIndex("PASSWORD");
                if (userIdColumnIndex != -1 && passwordColumnIndex != -1) {
                    // Getting the USER_ID
                    userId = cursor.getInt(userIdColumnIndex);
                    String storedHashedPassword = cursor.getString(passwordColumnIndex);
                    // Verify the password using bcrypt
                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        return userId;
                    } else {
                        return null;
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return null; // User not found
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
                    false,
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

    public boolean deletePlayerProfile(int playerId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("PLAYER_PROFILE", "PLAYER_ID = ?", new String[]{String.valueOf(playerId)});
            return true;
        } catch (Exception e) {
            Log.e("DBHelper", "Error deleting player profile: " + e.getMessage());
            return false;
        }
    }
////////////// DBHelper.java

    public List<String> getAllLeagues() {
        List<String> leagues = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT LEAGUE FROM HIGH_SCHOOL_LEAGUE", null);

        if (cursor.moveToFirst()) {
            do {
                leagues.add(cursor.getString(cursor.getColumnIndexOrThrow("LEAGUE")));
            } while (cursor.moveToNext());
        } else {
            Log.d("DBHelper", "No leagues found in the database.");
        }
        cursor.close();
        Log.d("DBHelper", "Leagues: " + leagues);
        return leagues;
    }

    public boolean deleteFixture(int fixtureId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            //Delete linked timsheet
            int deletedTimes = db.delete("TIMES", "FIXTURE_ID = ?", new String[]{String.valueOf(fixtureId)});
            Log.d("DBHelper", "Deleted TIMES entries: " + deletedTimes);

            // Delete Sport Fixture
            int deletedFixtures = db.delete("SPORT_FIXTURES", "FIXTURE_ID = ?", new String[]{String.valueOf(fixtureId)});
            Log.d("DBHelper", "Deleted SPORT_FIXTURES entries: " + deletedFixtures);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e("DBHelper", "Error deleting fixture: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public int countFixtures() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM SPORT_FIXTURES";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int checkFixtureId(int fixtureId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM SPORT_FIXTURES WHERE FIXTURE_ID = ?", new String[]{String.valueOf(fixtureId)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        Log.d("DBHelper", "Count of SPORT_FIXTURES with ID " + fixtureId + ": " + count);
        cursor.close();
        return count;
    }

    public boolean checkIsAdmin(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ROLE_ID FROM USERS WHERE USER_ID = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            int roleId = cursor.getInt(cursor.getColumnIndexOrThrow("ROLE_ID"));
            cursor.close();
            return roleId == 1;
        }
        return false;
    }

    //A method to get the ROLE_ID
    public int getRoleId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ROLE_ID FROM USERS WHERE USER_ID = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            int roleId = cursor.getInt(cursor.getColumnIndexOrThrow("ROLE_ID"));
            cursor.close();
            return roleId;
        }
        return -1;
    }

    // Method to move fixture to past
    public void moveFixtureToPast(MatchDis fixture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STATUS", "FINISHED");
        db.update("SPORT_FIXTURES", values, "FIXTURE_ID = ?", new String[]{fixture.getFixtureId()});
    }

    // A method to get event details
    public EventModel getEventDetails(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("EVENTS", null, "EVENT_ID = ?", new String[]{String.valueOf(eventId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            EventModel event = new EventModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow("EVENT_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("EVENT_NAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("EVENT_DATE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("EVENT_TIME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("EVENT_LOCATION")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("EVENT_PRICE")),
                    cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE")), // Fetch the image
                    cursor.getString(cursor.getColumnIndexOrThrow("EVENT_DESCRIPTION")), // Add this line
                    false // Default value for 'selected'
            );
            cursor.close();
            return event;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public EventModel getEventById(int eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM EVENTS WHERE EVENT_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(eventId)});

        EventModel event = null;
        if (cursor.moveToFirst()) {
            String eventName = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_NAME"));
            String eventDate = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_DATE"));
            String eventTime = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_TIME"));
            String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_LOCATION"));
            double eventPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("EVENT_PRICE"));
            byte[] pictures = cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"));
            String eventDescription = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_DESCRIPTION"));

            if (pictures == null) {
                pictures = new byte[0];
            }

//            if (eventDescription == null) {
//                eventDescription = "";
//            }
            event = new EventModel(
                    eventId,
                    eventName,
                    eventDate,
                    eventTime,
                    eventLocation,
                    eventPrice,
                    pictures,
                    eventDescription,
                    false // Default value for 'selected'
            );
        }
        cursor.close();
        return event;
    }

    // A method to update event details
    public int updateEventDetails(EventModel event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EVENT_NAME", event.getEventName());
        values.put("EVENT_DATE", event.getEventDate());
        values.put("EVENT_TIME", event.getEventTime());
        values.put("EVENT_LOCATION", event.getEventLocation());
        values.put("EVENT_PRICE", event.getEventPrice());
        values.put("PICTURE", event.getEventPicture());

        return db.update("EVENTS", values, "EVENT_ID = ?", new String[]{String.valueOf(event.getEventId())});
    }

    // A method to create a new Product
    public long addProduct(String name, String description, double price, byte[] photo, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("PRICE", price);
        values.put("PHOTO", photo);
        values.put("USER_ID", userId);
        return db.insert("SCHOOL_MERCH", null, values);
    }

//Umar Implementation

    // Method to get all fixtures
    public List<MatchDis> getUpcomingFixtures() {
        List<MatchDis> fixtures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT FIXTURE_ID, HOME_TEAM, AWAY_TEAM, SET_TIME, SET_DATE, HOME_LOGO, AWAY_LOGO FROM SPORT_FIXTURES WHERE SET_DATE >= date('now')", null);

        if (cursor.moveToFirst()) {
            do {
                MatchDis fixture = new MatchDis(
                        cursor.getString(cursor.getColumnIndexOrThrow("FIXTURE_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("HOME_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("AWAY_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SET_TIME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SET_DATE")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("HOME_LOGO")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("AWAY_LOGO")),
                        MatchStatus.UPCOMING
                );
                fixtures.add(fixture);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fixtures;
    }

    // Method to update match status
    public int updateMatchStatus(int fixtureId, int matchStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MATCH_STATUS_ID", matchStatus); // Update the match status

        // If the match status is "Match Over", mark the fixture as past
        if (matchStatus == 4) { // Assuming 4 corresponds to "Match Over"
            markFixtureAsPast(fixtureId);
        }

        return db.update("SPORT_FIXTURES", contentValues, "FIXTURE_ID = ?", new String[]{String.valueOf(fixtureId)});
    }

    // Method to get all past fixtures
    public List<MatchDis> getPastFixtures() {
        List<MatchDis> fixtures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT FIXTURE_ID, HOME_TEAM, AWAY_TEAM, SET_TIME, SET_DATE, HOME_LOGO, AWAY_LOGO FROM SPORT_FIXTURES WHERE SET_DATE < date('now')", null);

        if (cursor.moveToFirst()) {
            do {
                MatchDis fixture = new MatchDis(
                        cursor.getString(cursor.getColumnIndexOrThrow("FIXTURE_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("HOME_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("AWAY_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SET_TIME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SET_DATE")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("HOME_LOGO")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("AWAY_LOGO")),
                        MatchStatus.FINISHED
                );
                fixtures.add(fixture);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fixtures;
    }

    public List<FixtureModel> fetchPastFixtures() {
        List<FixtureModel> fixtures = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SPORT_FIXTURES WHERE IS_PAST = 1", null);

        if (cursor.moveToFirst()) {
            do {
                FixtureModel fixture = new FixtureModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("FIXTURE_ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SPORT")),
                        cursor.getString(cursor.getColumnIndexOrThrow("HOME_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("AWAY_TEAM")),
                        cursor.getString(cursor.getColumnIndexOrThrow("AGE_GROUP")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LEAGUE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_LOCATION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DATE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_TIME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MATCH_DESCRIPTION")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("HOME_LOGO")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("AWAY_LOGO")),
                        cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("LEAGUE_ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("IS_HOME_GAME")) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow("MATCH_STATUS_ID"))
                );
                fixtures.add(fixture);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return fixtures;
    }


    public List<PlayerProfileView> getAllPlayerProfiles() {
        List<PlayerProfileView> playerProfiles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT pp.NAME, pp.SURNAME, pp.AGE, pp.GRADE, pp.AGE_GROUP, u.EMAIL " +
                "FROM PLAYER_PROFILE pp " +
                "JOIN USERS u ON pp.USER_ID = u.USER_ID " +
                "WHERE u.ROLE_ID = (SELECT ROLE_ID FROM ROLES WHERE ROLE = 'User')";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                PlayerProfileView profile = new PlayerProfileView(
                        cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("SURNAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")), // Corrected to getString
                        cursor.getInt(cursor.getColumnIndexOrThrow("AGE")),      // Corrected to getInt
                        cursor.getString(cursor.getColumnIndexOrThrow("GRADE"))
                );
                playerProfiles.add(profile);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return playerProfiles;
    }

    public String getPlayerEmail(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String email = null;
        String query = "SELECT EMAIL FROM USERS WHERE USER_ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
        }
        cursor.close();
        db.close();
        return email;
    }

    public List<PlayerProfileModel> filterPlayersByAgeGroupAndGrade(String ageGroup, String grade) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PlayerProfileModel> playerList = new ArrayList<>();
        String selection = "AGE_GROUP = ? AND GRADE = ?";
        String[] selectionArgs = new String[]{ageGroup, grade};
        Cursor cursor = db.query("PLAYER_PROFILE", null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("PLAYER_ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow("SURNAME"));
                String nickname = cursor.getString(cursor.getColumnIndexOrThrow("NICKNAME"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("AGE"));
                String height = cursor.getString(cursor.getColumnIndexOrThrow("HEIGHT"));
                String position = cursor.getString(cursor.getColumnIndexOrThrow("POSITION"));
                String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("DATEOFBIRTH"));
                byte[] profilePicture = cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("USER_ID"));

                PlayerProfileModel player = new PlayerProfileModel(id, name, surname, nickname, age, grade, height, position, dateOfBirth, ageGroup, userId, false, profilePicture);
                playerList.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return playerList;
    }

    public List<PlayerProfileModel> searchPlayers(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PlayerProfileModel> playerList = new ArrayList<>();
        String selection = "p.NAME LIKE ? OR p.SURNAME LIKE ? OR u.EMAIL LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"};
        String queryStr = "SELECT p.*, u.EMAIL FROM PLAYER_PROFILE p " +
                "JOIN USERS u ON p.USER_ID = u.USER_ID " +
                "WHERE " + selection;

        Cursor cursor = db.rawQuery(queryStr, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("PLAYER_ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow("SURNAME"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                String nickname = cursor.getString(cursor.getColumnIndexOrThrow("NICKNAME"));
                int age = cursor.getInt(cursor.getColumnIndexOrThrow("AGE"));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow("GRADE"));
                String height = cursor.getString(cursor.getColumnIndexOrThrow("HEIGHT"));
                String position = cursor.getString(cursor.getColumnIndexOrThrow("POSITION"));
                String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("DATEOFBIRTH"));
                String ageGroup = cursor.getString(cursor.getColumnIndexOrThrow("AGE_GROUP"));
                byte[] profilePicture = cursor.getBlob(cursor.getColumnIndexOrThrow("PICTURE"));

                PlayerProfileModel player = new PlayerProfileModel(id, name, surname, nickname, age, grade, height, position, dateOfBirth, ageGroup, id, false, profilePicture);
                playerList.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return playerList;
    }

    public long addEvent(String name, String date, String time, String location, double price, String description, byte[] picture, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EVENT_NAME", name);
        values.put("EVENT_DATE", date);
        values.put("EVENT_TIME", time);
        values.put("EVENT_LOCATION", location);
        values.put("EVENT_PRICE", price);
        values.put("EVENT_DESCRIPTION", description);
        values.put("PICTURE", picture);
        values.put("USER_ID", userId);
        return db.insert("EVENTS", null, values);
    }
}

