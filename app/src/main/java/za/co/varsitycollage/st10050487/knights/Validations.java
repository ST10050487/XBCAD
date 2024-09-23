package za.co.varsitycollage.st10050487.knights;

public class Validations {

    //A method to check user input
    public boolean CheckUserInput(String input){
        if(input == null || input.isEmpty()){
            return false;
        }
        return true;
    }
    //*******************************************CODE-X**************************************************//
    //A method to check password requirements
    public boolean CheckPassword(String password) {
        // Checking if password is empty or blank
        if (password.isEmpty() || password.isBlank()) {
            return false;
        }

        // Checking if password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Regular expressions for password checks
        // Checking if the password at least one digit
        String digitPattern = ".*[0-9].*";
        // Checking if the password at least one special character (@, #, $, %)
        String specialCharacterPattern = ".*[@,#,$,%].*";
        //Checking if the password at least one lowercase letter
        String lowerCasePattern = ".*[a-z].*";
        // Checking if the password at least one uppercase letter
        String upperCasePattern = ".*[A-Z].*";

        // Checking for at least one digit
        if (!password.matches(digitPattern)) {
            return false;
        }

        // Checking for at least one special character
        if (!password.matches(specialCharacterPattern)) {
            return false;
        }

        // Checking for at least one lowercase letter
        if (!password.matches(lowerCasePattern)) {
            return false;
        }

        // Checking for at least one uppercase letter
        if (!password.matches(upperCasePattern)) {
            return false;
        }

        // If all conditions are met, return true
        return true;
    }
    //*******************************************CODE-X**************************************************//
    // A method to check if the email is valid
    public boolean CheckEmail(String email) {
        // Check if email is empty
        if (email.isEmpty()) {
            return false;
        }

        // Regular expression for email validation
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        // Checking if email matches the pattern
        if (!email.matches(emailPattern)) {
            return false;
        }

        // If all conditions are met, return true
        return true;
    }
}
