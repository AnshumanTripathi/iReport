package com.ireport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.AddUserHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetReportByIdHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetUserForEmailID;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateReportByIdHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateUserInfoHandler;
import com.ireport.model.AppContext;
import com.ireport.model.ReportData;
import com.ireport.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        ICallbackActivity
{
    private static String TAG = "MAIN_ACTIVITY";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    //Google API Client
    private GoogleApiClient mGoogleApiClient;

    //Facebook Callback Manager
    CallbackManager callbackManager;

    String userEmail;

    AddUserHandler addUserHandler = null;
    GetUserForEmailID getUserInfo = null;
    AppContext ctx = AppContext.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Facebook SDk before setContentView for using Facebook UI elements
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        //test <code></code>
        UpdateReportByIdHandler gid = new UpdateReportByIdHandler(this,
                "reportId","5848a1e0c479e405d06496ca","still_there");
        gid.updateReportForReportId(getApplicationContext());

        //Special Facebook Login Button
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        mAuth = FirebaseAuth.getInstance();
        AppEventsLogger.activateApp(MainActivity.this);

        //Google Login Button
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Google API client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();

        //Firebase auth Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Log.d(TAG, "Email: " + user.getEmail());
                    handleUserSignIn(user.getEmail());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        findViewById(R.id.emailLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = "somyaaggarwal@gmail.com";  // just for testing
                handleUserSignIn(userEmail);
            }
        });

        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListReportsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListReportsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(MainActivity.this, "Sign in Failed", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        signIn();
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final GoogleSignInAccount user = acct;
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String user_email = user.getEmail();
                        handleUserSignIn(user_email);

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        Log.d(TAG,"Email for fb sign in:" + user_email);

                        if (!task.isSuccessful()) {
                            //Signin Failed
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkIfOfficial(String user_email) {
        if(user_email.endsWith("gmail.com")) {
            return true;
        } else
            return false;
    }


    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Sign in Successfull
                        GraphRequest request = GraphRequest.newMeRequest(
                                token,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        try {
                                            userEmail = object.getString("email");
                                            System.out.println("User email: "+userEmail);
                                            handleUserSignIn(userEmail);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email");
                        request.setParameters(parameters);
                        request.executeAsync();
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn() {
        Log.v("in sign in", "");
        //Go to google ap to login
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleUserSignIn(String userEmail) {
        UserInfo newUser = new UserInfo(userEmail);
        newUser.setOfficial(this.checkIfOfficial(userEmail));
        //set the current user in app context
        AppContext.setCurrentLoggedInUser(newUser);
        //create the new user on server
        addUserHandler = new AddUserHandler(
                MainActivity.this,
                "add_new_user",
                userEmail,
                newUser.isOfficial());
        addUserHandler.addNewUser(getApplicationContext());
    }

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        switch (identifier) {
            case "add_new_user": {
                if(responseObj instanceof String) {
                    Log.d("ADD_USER_STATUS_CODE",responseObj.toString());
                    if(responseObj.toString().equals("200") || responseObj.toString().equals("501")) {
                        if (this.ctx.getCurrentLoggedInUser().isOfficial()) {
                            //direct the user to list reports activity
                            Intent intent = new Intent(
                                    MainActivity.this,
                                    ListReportsActivity.class
                            );
                            startActivity(intent);
                        } else{
                            // get user settings now
                            getUserInfo = new GetUserForEmailID(
                                    this,
                                    "get_user_details",
                                    this.ctx.getCurrentLoggedInUser().getEmail()
                            );

                            getUserInfo.getUserDataForEmail(getApplicationContext());
                        }
                        Log.d("ADD_USER_SUCCESS", "New User has been added successfully");
                    } else {
                        Log.d("ADD_USER_FAILURE","Unable to add the deatils of the new user on server");
                    }
                }
                break;
            }
            case "get_user_details": {
                if (responseObj != null && responseObj instanceof UserInfo) {
                    this.ctx.setUserInfo((UserInfo)responseObj);
                }
                //direct the user to list reports activity
                Intent intent = new Intent(
                        MainActivity.this,
                        ListReportsActivity.class
                );
                startActivity(intent);
            }
        }
    }
}
