package com.example.duke.Colorify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class DisplayUserInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_info);

        String fullName = getIntent().getStringExtra("EXTRA_FULL_NAME");
        ((TextView)findViewById(R.id.textViewFullName)).setText(fullName);

        String emailAddress = getIntent().getStringExtra("EXTRA_EMAIL_ADDRESS");
        ((TextView)findViewById(R.id.textViewEmailAddress)).setText(emailAddress);

        String phoneNumber = getIntent().getStringExtra("EXTRA_PHONE_NUMBER");
        ((TextView)findViewById(R.id.textViewPhoneNumber)).setText(phoneNumber);

        String password = getIntent().getStringExtra("EXTRA_PASSWORD");
        ((TextView)findViewById(R.id.textViewPassword)).setText(password);

        String gender = getIntent().getStringExtra("EXTRA_GENDER");
        ((TextView)findViewById(R.id.textViewGender)).setText(gender);

        String dateOfBirth = getIntent().getStringExtra("EXTRA_DATE_OF_BIRTH");
        ((TextView)findViewById(R.id.textViewDateOfBirth)).setText(dateOfBirth);

        String avatarUriString = getIntent().getStringExtra("EXTRA_AVATAR");
        if (avatarUriString!=null) {
            Uri avatarUri = Uri.parse(avatarUriString);
            Bitmap avatarBitmap = null;
            try {
                avatarBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), avatarUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageView) findViewById(R.id.imageViewAvatar)).setImageBitmap(avatarBitmap);
        }

        Button dialButton = (Button)findViewById(R.id.buttonDial);
        dialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String phoneNumber = ((TextView)findViewById(R.id.textViewPhoneNumber)).getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);
            }
        });
    }
}
