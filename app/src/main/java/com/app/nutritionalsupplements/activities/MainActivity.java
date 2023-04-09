package com.app.nutritionalsupplements.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.app.nutritionalsupplements.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseAuth auth;
    private FirebaseUser user;

    MenuItem loginItem;
    MenuItem logoutItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(LOG_TAG, "OnCreate has run...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG_TAG, "onStart has run...");
        initializeData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu(); // force the system to call onPrepareOptionsMenu() again
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.signOut();
        Log.e(LOG_TAG, "Logged out successfully!");
    }

    private void initializeData() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public void openLoginActivity() {
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void logout() {
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkLoginStatus() {
        if (user == null) {
            Log.e(LOG_TAG, "Null user detected!");
            return false;
        }
        Log.e(LOG_TAG, "Authenticated user detected!" + user.getEmail());
        return true;
    }

//    Setting up Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop_list_menu, menu);
        loginItem = menu.findItem(R.id.login);
        logoutItem = menu.findItem(R.id.logout);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                openLoginActivity();
                Log.e(LOG_TAG, "Login clicked!");
                return true;
            case R.id.logout:
                logout();
                Log.e(LOG_TAG, "Logout clicked!");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.e(LOG_TAG, "onPrepareOptionsMenu has run...");
        loginItem = menu.findItem(R.id.login);
        logoutItem = menu.findItem(R.id.logout);

        setLoginItemsVisibility();

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     *  Check if the user is logged in or out
     */
    private void setLoginItemsVisibility() {
        boolean isLoggedIn = checkLoginStatus();

        // Set the visibility of the login and logout menu items
        loginItem.setVisible(!isLoggedIn);
        logoutItem.setVisible(isLoggedIn);
    }
}