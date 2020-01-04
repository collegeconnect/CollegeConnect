package com.bvcoe.bvpconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText username, password, name, email;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username= findViewById(R.id.editText6);
        password= findViewById(R.id.editText5);
        name= findViewById(R.id.editText3);
        email= findViewById(R.id.editText4);
        button = findViewById(R.id.button3);


//        if (emails.isEmpty()||passwords.isEmpty()||usernames.isEmpty()||names.isEmpty()) {
//            Toast.makeText(SignUp.this, "Fields Empty", Toast.LENGTH_SHORT).show();
//        }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String usernames =username.getText().toString();
                    String passwords =password.getText().toString();
                    String names =name.getText().toString();
                    String emails =email.getText().toString();
                    User.addUser(usernames, emails, names, passwords);
//                       mAuth= FirebaseAuth.getInstance();
////                        mAuth.createUserWithEmailAndPassword(usernames,passwords).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
////                            @Override
////                            public void onComplete(@NonNull Task<AuthResult> task) {
////                                if (task.isSuccessful()) {
////                                    // Sign in success, update UI with the signed-in user's information
////                                    Log.d(TAG, "createUserWithEmail:success");
////                                    FirebaseUser user = mAuth.getCurrentUser();
////                                    Toast.makeText(SignUp.this, user.getDisplayName()+" registered!", Toast.LENGTH_SHORT).show();
////                                } else {
////                                    // If sign in fails, display a message to the user.
////                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
////                                    Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
////                                }
////                            }
////                        });
////
                }
            });
    }
}
