package za.co.varsitycollage.st10050487.knights;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class StudentParentReg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_parent_reg);

        // Adding a color for the text view
        MakingTxtColor();

        // Adding a color for the register button
        changeRegisterButtonColor();

        // Set checkbox color programmatically
        setCheckboxColor();
    }

    private void MakingTxtColor() {
        TextView textView = findViewById(R.id.textView7);
        String text = "I agree to Term of Service and Privacy Policy";
        SpannableString spannableString = new SpannableString(text);

        // Set color for "Term of Service"
        int termOfServiceStart = text.indexOf("Term of Service");
        int termOfServiceEnd = termOfServiceStart + "Term of Service".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), termOfServiceStart, termOfServiceEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set color for "Privacy Policy"
        int privacyPolicyStart = text.indexOf("Privacy Policy");
        int privacyPolicyEnd = privacyPolicyStart + "Privacy Policy".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), privacyPolicyStart, privacyPolicyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the spannable string to the TextView
        textView.setText(spannableString);
    }

    private void changeRegisterButtonColor() {
        Button registerButton = findViewById(R.id.Regbtn);
        registerButton.setBackgroundColor(Color.parseColor("#FFA500")); // Set the desired color here
    }

    private void setCheckboxColor() {
        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setButtonDrawable(R.drawable.checkbox_custom);
    }

    // This method is used for the date picker dialog
    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        EditText dateField = findViewById(R.id.DateField);
                        dateField.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
}

