package com.example.trackattendance.SignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.trackattendance.MainActivity;
import com.example.trackattendance.Models.Users;
import com.example.trackattendance.R;
import com.example.trackattendance.SignUp.SignUpActivity;
import com.example.trackattendance.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private ActivitySignInBinding binding;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setTitle("Sign In");
        dialog.setMessage("SignIn to your Account");

        mAuth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();

        /**
         * Sign In With Email and Password
         */
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                dialog.dismiss();
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DatabaseReference databaseReference = database.getReference().child("Users").child(user.getUid());
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Users currUser = new Users();
                                            for(DataSnapshot ds: snapshot.getChildren()){
                                                switch (ds.getKey()){
                                                    case "email":
                                                        currUser.setEmail(ds.getValue().toString());
                                                        break;
                                                    case "userName":
                                                        currUser.setUserName(ds.getValue().toString());
                                                        break;
                                                    case "courseIds":
                                                        List<String> courseIds = new ArrayList<>();
                                                        for (DataSnapshot courseData: ds.getChildren()){
                                                            courseIds.add((String)courseData.getValue());
                                                        }
                                                        currUser.setCourseIds(courseIds);
                                                        break;
                                                }
                                            }
                                            if (currUser.getCourseIds()==null) currUser.setCourseIds(new ArrayList<String>());
                                            Prefs.putString("UserID", new Gson().toJson(currUser.getCourseIds()));
                                            Prefs.putString("UserEmail", currUser.getEmail());
                                            Prefs.putString("UserDisplayName", currUser.getUserName());
                                            Prefs.putString("UserCourseIds", new Gson().toJson(currUser.getCourseIds()));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            //toast
                                        }
                                    });
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //if user is already signIn
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        //if user don't have an account
        binding.registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //R.string.default_web_client_id = 659787252749-9sarvapn4s8erojc9kc8hphdc30fhnnt.apps.googleusercontent.com
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("659787252749-9sarvapn4s8erojc9kc8hphdc30fhnnt.apps.googleusercontent.com")//you can also use R.string.default_web_client_id
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);



        /**
         * Google SignIn
         */
        binding.googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        /**
         * FaceBook SignIn
         */
        binding.facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    //signIn with google method
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //store in firebase
                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfile_pic(user.getPhotoUrl().toString());

                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignInActivity.this, "Sign In with google", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}