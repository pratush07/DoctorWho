package com.example.pratush.docfinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.ConfigHelper;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.DoclistModel;
import com.example.pratush.docfinder.models.DoctorModel;
import com.example.pratush.docfinder.models.UserModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ResultCallback<People.LoadPeopleResult> {
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    CallbackManager fbCallbackManager;
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private LoginButton fbLoginButton;
    UserModel use;
    DoctorModel doc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        fbCallbackManager = CallbackManager.Factory.create();

        use = new UserModel();
        doc=new DoctorModel();

        fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        fbLoginButton.setText("Login");

        fbLoginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

        fbLoginButton.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult fbLoginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(fbLoginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response)
                            {
                                try {
                                    if(getIntent().getStringExtra("User").equalsIgnoreCase("usr"))   //if user is a patient
                                    {
                                        use.FirstName = object.getString("first_name");
                                        use.LastName = object.getString("last_name");
                                        use.EmailAddress = object.getString("email");
                                        String gender = object.getString("gender");
                                        if (gender.equalsIgnoreCase("male"))
                                            use.Gender = "M";
                                        else
                                            use.Gender = "F";
                                        JSONObject temp = (JSONObject) object.get("picture");
                                        temp = (JSONObject) temp.get("data");
                                        use.ProfilePic = temp.getString("url");
                                        use.loadasync_email(new TaskCallBack() {    //checking if email exists in the database or not
                                            @Override
                                            public void onSuccess(Object response) throws JSONException {   //if email already exists
                                                super.onSuccess(response);
                                                UserModel obj = (UserModel) response;
                                                ConfigHelper.setUserID(obj.ID, getApplicationContext());
                                                ConfigHelper.setType("usr",getApplicationContext());
                                                // obj.UserType=use.UserType;
                                                obj.FirstName=use.FirstName;
                                                obj.LastName=use.LastName;
                                                obj.EmailAddress=use.EmailAddress;
                                                obj.Gender=use.Gender;
                                                obj.ProfilePic=use.ProfilePic;

                                                obj.update(new TaskCallBack() {
                                                    @Override
                                                    public void onSuccess(Object response) throws JSONException {//update the database with new data
                                                        super.onSuccess(response);

                                                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                                        startActivity(i);
                                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                                    }
                                                });

                                            }

                                            public void onError(Object error)   //if  email doesn't exist
                                            {
                                                super.onError(error);
                                                use.saveAsync(new TaskCallBack() {  //save the new data in the database
                                                    @Override
                                                    public void onSuccess(Object response) throws JSONException {
                                                        super.onSuccess(response);
                                                        UserModel obj = (UserModel) response;
                                                        ConfigHelper.setUserID(obj.ID, getApplicationContext());  //setting user ID for the first time
                                                        ConfigHelper.setType("usr", getApplicationContext());
                                                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                                        startActivity(i);
                                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    }
                                                });

                                            }


                                        });
                                    }

                                    else if(getIntent().getStringExtra("User").equalsIgnoreCase("doc"))
                                    {
                                        doc.FirstName = object.getString("first_name");
                                        doc.LastName = object.getString("last_name");
                                        doc.EmailAddress = object.getString("email");
                                        String gender = object.getString("gender");
                                        if (gender.equalsIgnoreCase("male"))
                                            doc.Gender = "M";
                                        else
                                            doc.Gender = "F";
                                        JSONObject temp = (JSONObject) object.get("picture");
                                        temp = (JSONObject) temp.get("data");
                                        doc.ProfilePic = temp.getString("url");
                                        doc.Rate=0.0;
                                        doc.Qualification=getIntent().getStringExtra("qual");
                                        doc.Consultation=getIntent().getIntExtra("consult", 0);
                                        doc.Address=getIntent().getStringExtra("addr");

                                        doc.loadasync_email(new TaskCallBack() {    //checking if email exists in the database or not
                                            @Override
                                            public void onSuccess(Object response) throws JSONException {   //if email already exists
                                                super.onSuccess(response);
                                                DoctorModel obj = (DoctorModel) response;
                                                ConfigHelper.setUserID(obj.ID, getApplicationContext());
                                                ConfigHelper.setType("doc", getApplicationContext());

                                                obj.FirstName = doc.FirstName;
                                                obj.LastName = doc.LastName;
                                                obj.EmailAddress = doc.EmailAddress;
                                                obj.Gender = doc.Gender;
                                                obj.ProfilePic = doc.ProfilePic;
                                                obj.Qualification=doc.Qualification;
                                                obj.Consultation=doc.Consultation;
                                                obj.Address=doc.Address;

                                                obj.update(new TaskCallBack() {
                                                    @Override
                                                    public void onSuccess(Object response) throws JSONException {//update the database with new data
                                                        super.onSuccess(response);

                                                        final DoctorModel obj = (DoctorModel) response;
                                                        DoclistModel docObj = new DoclistModel();   //for updating the docid and catid mapping in the database
                                                        docObj.usrID=obj.ID;
                                                        docObj.catID=getIntent().getIntExtra("DoctypeID",0);
                                                        docObj.updateAsync(new TaskCallBack() {
                                                            @Override
                                                            public void onSuccess(Object response) throws JSONException {//update the database with new data
                                                                super.onSuccess(response);
                                                                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                                                startActivity(i);
                                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                            }

                                                            public void onError(Object error)   //if mapping doesn't exist
                                                            {
                                                                super.onError(error);

                                                                DoclistModel docObj = new DoclistModel();   //for saving the docid and catid mapping in the database
                                                                docObj.usrID=obj.ID;
                                                                docObj.catID=getIntent().getIntExtra("DoctypeID",0);
                                                                docObj.saveAsync(new TaskCallBack() {
                                                                    @Override
                                                                    public void onSuccess(Object response) throws JSONException {//save the data in the database
                                                                        super.onSuccess(response);
                                                                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                                                        startActivity(i);
                                                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                                    }
                                                                });

                                                            }

                                                        });

                                                    }
                                                });

                                            }

                                            public void onError(Object error)   //if  email doesn't exist
                                            {
                                                super.onError(error);
                                                doc.saveAsync(new TaskCallBack() {  //save the new data in the database
                                                    @Override
                                                    public void onSuccess(Object response) throws JSONException {
                                                        super.onSuccess(response);
                                                        DoctorModel obj = (DoctorModel) response;
                                                        ConfigHelper.setUserID(obj.ID, getApplicationContext());  //setting user ID for the first time
                                                        ConfigHelper.setType("doc", getApplicationContext());
                                                        DoclistModel docObj = new DoclistModel();   //for saving the docid and catid mapping in the database
                                                        docObj.usrID=obj.ID;
                                                        docObj.catID=getIntent().getIntExtra("DoctypeID",0);
                                                        docObj.saveAsync(new TaskCallBack() {
                                                            @Override
                                                            public void onSuccess(Object response) throws JSONException {//save the data in the database
                                                                super.onSuccess(response);
                                                                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                                                startActivity(i);
                                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                            }
                                                        });
                                                    }
                                                });

                                            }


                                        });

                                    }
                                }
                                catch (JSONException ex)
                                {

                                }


                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });


        // Initialize Google Login
       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.googleLoginButton);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        setGooglePlusButtonText(signInButton, "Log in with Google");
        signInButton.setOnClickListener(this);*/
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView mTextView = (TextView) v;
                mTextView.setText(buttonText);
                mTextView.setTypeface(null, Typeface.BOLD);
                mTextView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                mTextView.setPadding(0, 0, 20, 0);
                mTextView.setTextSize(15);

                return;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("VS", "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);
    ;
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }
            mIsResolving = false;
            mGoogleApiClient.connect();
        }
        else {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
// onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d("VS", "onConnected:" + bundle);
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
        {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if(getIntent().getStringExtra("User").equalsIgnoreCase("usr"))   //if user is a patient
            {
                String nameTemp[] = currentPerson.getDisplayName().split(" ");
                use.FirstName = nameTemp[0];
                use.LastName = nameTemp[1];
                use.EmailAddress = Plus.AccountApi.getAccountName(mGoogleApiClient);
                int gender = currentPerson.getGender();
                if (gender == 0)
                    use.Gender = "M";
                else
                    use.Gender = "F";

                use.ProfilePic=currentPerson.getImage().getUrl();

                use.loadasync_email(new TaskCallBack() {    //checking if email exists in the database or not
                    @Override
                    public void onSuccess(Object response) throws JSONException {   //if email already exists
                        super.onSuccess(response);
                        UserModel obj = (UserModel) response;
                        ConfigHelper.setUserID(obj.ID, getApplicationContext());
                        ConfigHelper.setType("usr", getApplicationContext());

                        obj.FirstName=use.FirstName;
                        obj.LastName=use.LastName;
                        obj.EmailAddress=use.EmailAddress;
                        obj.Gender=use.Gender;
                        obj.ProfilePic=use.ProfilePic;

                        obj.update(new TaskCallBack() {
                            @Override
                            public void onSuccess(Object response) throws JSONException {//update the database with new data
                                super.onSuccess(response);

                                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                            }
                        });

                    }

                    public void onError(Object error)   //if  email doesn't exist
                    {
                        super.onError(error);
                        use.saveAsync(new TaskCallBack() {  //save the new data in the database
                            @Override
                            public void onSuccess(Object response) throws JSONException {
                                super.onSuccess(response);
                                UserModel obj = (UserModel) response;
                                ConfigHelper.setUserID(obj.ID, getApplicationContext());  //setting user ID for the first time
                                ConfigHelper.setType("usr", getApplicationContext());
                                Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });

                    }


                });
            }
            else if(getIntent().getStringExtra("User").equalsIgnoreCase("doc"))
            {
                String nameTemp[] = currentPerson.getDisplayName().split(" ");
                doc.FirstName = nameTemp[0];
                doc.LastName = nameTemp[1];
                doc.EmailAddress = Plus.AccountApi.getAccountName(mGoogleApiClient);
                int gender = currentPerson.getGender();
                if (gender == 0)
                    doc.Gender = "M";
                else
                    doc.Gender = "F";
                 doc.Rate=0.0;
                doc.ProfilePic=currentPerson.getImage().getUrl();
                doc.Qualification=getIntent().getStringExtra("qual");
                doc.Consultation=getIntent().getIntExtra("consult",0);
                doc.Address=getIntent().getStringExtra("addr");

                doc.loadasync_email(new TaskCallBack() {    //checking if email exists in the database or not
                    @Override
                    public void onSuccess(Object response) throws JSONException {   //if email already exists
                        super.onSuccess(response);
                        DoctorModel obj = (DoctorModel) response;
                        ConfigHelper.setUserID(obj.ID, getApplicationContext());
                        ConfigHelper.setType("doc", getApplicationContext());
                        obj.FirstName = doc.FirstName;
                        obj.LastName = doc.LastName;
                        obj.EmailAddress = doc.EmailAddress;
                        obj.Gender = doc.Gender;
                        obj.ProfilePic = doc.ProfilePic;
                        obj.Qualification=doc.Qualification;
                        obj.Consultation=doc.Consultation;
                        obj.Address=doc.Address;

                        obj.update(new TaskCallBack() {
                            @Override
                            public void onSuccess(Object response) throws JSONException {//update the database with new data
                                super.onSuccess(response);

                                final DoctorModel obj = (DoctorModel) response;
                                DoclistModel docObj = new DoclistModel();   //for updating the docid and catid mapping in the database
                                docObj.usrID=obj.ID;
                                docObj.catID=getIntent().getIntExtra("DoctypeID",0);
                                docObj.updateAsync(new TaskCallBack() {
                                    @Override
                                    public void onSuccess(Object response) throws JSONException {//update the database with new data
                                        super.onSuccess(response);
                                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                    public void onError(Object error)   //if  email doesn't exist
                                    {
                                        super.onError(error);
                                        doc.saveAsync(new TaskCallBack() {  //save the new data in the database
                                            @Override
                                            public void onSuccess(Object response) throws JSONException {
                                                super.onSuccess(response);
                                                DoclistModel docObj = new DoclistModel();   //for saving the docid and catid mapping in the database
                                                docObj.usrID = obj.ID;
                                                docObj.catID = getIntent().getIntExtra("DoctypeID", 0);
                                                docObj.saveAsync(new TaskCallBack() {
                                                    @Override
                                                    public void onSuccess(Object response) throws JSONException {//save the data in the database
                                                        super.onSuccess(response);
                                                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                                        startActivity(i);
                                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    }
                                                });
                                            }
                                        });

                                    }

                                });

                            }
                        });

                    }

                    public void onError(Object error)   //if  email doesn't exist
                    {
                        super.onError(error);
                        doc.saveAsync(new TaskCallBack() {  //save the new data in the database
                            @Override
                            public void onSuccess(Object response) throws JSONException {
                                super.onSuccess(response);
                                DoctorModel obj = (DoctorModel) response;
                                ConfigHelper.setUserID(obj.ID, getApplicationContext());  //setting user ID for the first time
                                ConfigHelper.setType("doc", getApplicationContext());
                                DoclistModel docObj = new DoclistModel();   //for saving the docid and catid mapping in the database
                                docObj.usrID=obj.ID;
                                docObj.catID=getIntent().getIntExtra("DoctypeID",0);
                                docObj.saveAsync(new TaskCallBack() {
                                    @Override
                                    public void onSuccess(Object response) throws JSONException {//save the data in the database
                                        super.onSuccess(response);
                                        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                });
                            }
                        });

                    }


                });

            }

        }
        else
        {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
        mShouldResolve = false;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("VS", "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e("VS", "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {

            }
        } else {

        }
    }

    @Override
    public void onClick(View v)
    {
        LoginButton fbButton = (LoginButton) findViewById(R.id.fbLoginButton);
        SignInButton googleButton = (SignInButton) findViewById(R.id.googleLoginButton);

        fbButton.setVisibility(View.INVISIBLE);
        googleButton.setVisibility(View.INVISIBLE);

        ProgressBar load = (ProgressBar) findViewById(R.id.loadBarLogin);
        load.setVisibility(View.VISIBLE);
        mShouldResolve = true;
        if(ConfigHelper.internetAvailable(getApplicationContext())==1) {
            mGoogleApiClient.connect();
        }
        else
        {
            Toast.makeText(this,"Internet unavailable!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult)
    {

    }
    public void fbauthentication(View view) {

        if(ConfigHelper.internetAvailable(this)==1){

            LoginButton fbButton = (LoginButton) findViewById(R.id.fbLoginButton);
            //SignInButton googleButton = (SignInButton) findViewById(R.id.googleLoginButton);

            fbButton.setVisibility(View.INVISIBLE);
            //googleButton.setVisibility(View.INVISIBLE);

            ProgressBar load = (ProgressBar) findViewById(R.id.loadBarLogin);
            load.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(getApplicationContext(), "Internet Unavailable!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(i);
        }

    }


}
