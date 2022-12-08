package com.example.charmingplaces.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.charmingplaces.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
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

/**
 * Pantalla de inicio de la aplicación.
 * Permite al usuario, loguearse con su cuenta de Google.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    ActivityResultLauncher<Intent> resultLauncher;
    /**
     * Método que se ejecuta al cargar la pantalla, se encarga de pintar el layout y de
     * instanciar las variables de los input y servicios que se utilizarán en este activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Establece el botón de click en Google para abrir la interfaz de autenticación
        findViewById(R.id.signInButton).setOnClickListener(v ->
                resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()))
        );
        //Autentica al usuario
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG, "firebaseAuthWithGoogle" + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            Log.w(TAG, "Google sign in failed", e);
                        }
                    }

                });
    }

    /**
     * Guarda el registro de sesión del usuario en Firebase
     */
    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }


    /**
     * Autentifica por token en Firebase la cuenta de Google
     *
     * @param idToken token
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "sign in with Credential : Success");
                    } else {
                        Log.d(TAG, "sign in with Credential : Failure", task.getException());
                    }
                    updateUI();
                });
    }

    /**
     * Si el usuario está autenticado, lo redirecciona al MenuActivity
     */
    private void updateUI() {
        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
