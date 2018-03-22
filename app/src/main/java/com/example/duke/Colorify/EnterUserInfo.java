package com.example.duke.Colorify;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.InputStream;


public class EnterUserInfo extends Activity {

    public static final int PICK_IMAGE = 1;
    public Uri avatarUri;

    private static void checkIsStringEmpty (String s) throws InvalidUserInfo{
        if (0 == s.length()) throw new InvalidUserInfo("All fields are mandatory");
    }

    private String getSelectedGender(RadioGroup rg) throws InvalidUserInfo{
        int selectedRadioButtonID = rg.getCheckedRadioButtonId();
        if (-1 == selectedRadioButtonID){
            throw new InvalidUserInfo("No Gender Selected");
        }
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);

        String gender = selectedRadioButton.getText().toString();
        EnterUserInfo.checkIsStringEmpty(gender);
        return gender;
    }

    private String getFullName(EditText et) throws InvalidUserInfo{
        String fullName= et.getText().toString();
        EnterUserInfo.checkIsStringEmpty(fullName);

        if (!fullName.contains(" ")){
            throw new InvalidUserInfo("Full Name Must Contain Both First and Last Names");
        }

        return fullName;
    }

    private String getEmailAddress(EditText et) throws InvalidUserInfo{
        String emailAddress= et.getText().toString();
        EnterUserInfo.checkIsStringEmpty(emailAddress);

        if (!emailAddress.contains("@")){
            throw new InvalidUserInfo("Email Address Must Contain '@' sign");
        }
        if (emailAddress.indexOf('@') == 0){
            throw new InvalidUserInfo("Email Address Cannot Start with '@' sign");
        }
        if (emailAddress.indexOf('@') == emailAddress.length() - 1){
            throw new InvalidUserInfo("Email Address Cannot End With '@' sign");
        }

        return emailAddress;
    }

    private String getPhoneNumber(EditText et) throws InvalidUserInfo{
        String phoneNumber= et.getText().toString();
        EnterUserInfo.checkIsStringEmpty(phoneNumber);

        if (phoneNumber.length() != 10){
            throw new InvalidUserInfo("Phone Number Must Have Exactly 10 digits");
        }

        return phoneNumber;
    }

    private String getPassword(EditText et) throws InvalidUserInfo{
        String password= et.getText().toString();
        EnterUserInfo.checkIsStringEmpty(password);

        if (password.length() < 4){
            throw new InvalidUserInfo("Password Minimal Length is 4 Characters");
        }

        return password;
    }

    private String getDateOfBirth(EditText et) throws InvalidUserInfo{
        String dateOfBirth= et.getText().toString();
        EnterUserInfo.checkIsStringEmpty(dateOfBirth);

        if (!dateOfBirth.contains("/")){
            throw new InvalidUserInfo("Date of Birth Must be YYYY/MM/DD");
        }
        if (dateOfBirth.length() != 10){
            throw new InvalidUserInfo("Date of Birth Must be YYYY/MM/DD");
        }

        return dateOfBirth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user_info);

        Button submitButton = (Button)findViewById(R.id.buttonSubmit);

        final EditText editTextFullName = (EditText)findViewById(R.id.editTextFullName);
        final EditText editTextEmailAddress = (EditText)findViewById(R.id.editTextEmailAddress);
        final EditText editTextPhoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        final EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        final RadioGroup radioGroupGender = (RadioGroup)findViewById(R.id.radioGroupGender);
        final EditText editTextDateOfBirth = (EditText)findViewById(R.id.editTextDateOfBirth);

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getBaseContext(), DisplayUserInfo.class);
                try {
                    intent.putExtra(getString(R.string.EXTRA_USER_FULL_NAME), getFullName(editTextFullName));
                    intent.putExtra(getString(R.string.EXTRA_USER_EMAIL_ADDRESS), getEmailAddress(editTextEmailAddress));
                    intent.putExtra(getString(R.string.EXTRA_USER_PHONE_NUMBER), getPhoneNumber(editTextPhoneNumber));
                    intent.putExtra(getString(R.string.EXTRA_USER_PASSWORD), getPassword(editTextPassword));
                    intent.putExtra(getString(R.string.EXTRA_USER_GENDER), getSelectedGender(radioGroupGender));
                    intent.putExtra(getString(R.string.EXTRA_USER_DATE_OF_BIRTH), getDateOfBirth(editTextDateOfBirth));
                    if (avatarUri != null) intent.putExtra(getString(R.string.EXTRA_USER_AVATAR), avatarUri.toString());
                } catch (InvalidUserInfo invalidUserInfo) {
//                    invalidUserInfo.printStackTrace();
                    ((TextView) findViewById(R.id.textViewErrorMessage)).setText(invalidUserInfo.getMessage());
                    return;
                }
                startActivity(intent);
            }
        });

        ////////

        Button selectAvatarButton = (Button)findViewById(R.id.buttonSelectAvatar);

        selectAvatarButton .setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.SELECT_PICTURE_TITLE)), PICK_IMAGE);

                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (imageReturnedIntent == null) {
                ((TextView) findViewById(R.id.textViewErrorMessage)).setText(R.string.NoPictureChosenError);
                return;
            }
            try{
                Uri selectedImage = imageReturnedIntent.getData();
                this.avatarUri = selectedImage;
                ((ImageView)findViewById(R.id.imageViewAvatarThumb)).setImageURI(selectedImage);

            } catch (Exception e){
                ((TextView) findViewById(R.id.textViewErrorMessage)).setText(R.string.ErrorGettingPicture);
            }
        }
    }
}
