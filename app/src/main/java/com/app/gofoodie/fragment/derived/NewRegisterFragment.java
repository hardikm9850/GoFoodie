package com.app.gofoodie.fragment.derived;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.app.gofoodie.R;
import com.app.gofoodie.activity.derived.LocationActivity;
import com.app.gofoodie.fragment.base.BaseFragment;
import com.app.gofoodie.global.constants.Constants;
import com.app.gofoodie.global.constants.Network;
import com.app.gofoodie.handler.dashboardHandler.DashboardInterruptListener;
import com.app.gofoodie.handler.modelHandler.ModelParser;
import com.app.gofoodie.handler.profileDataHandler.CustomerProfileHandler;
import com.app.gofoodie.model.customer.Customer;
import com.app.gofoodie.model.login.Login;
import com.app.gofoodie.network.callback.NetworkCallbackListener;
import com.app.gofoodie.network.handler.NetworkHandler;
import com.app.gofoodie.utility.LocationUtils;
import com.app.gofoodie.utility.SessionUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * @class NewRegisterFragment
 * @desc {@link BaseFragment} Fragment class to handle New Customer Registration UI screen.
 */
public class NewRegisterFragment extends BaseFragment implements View.OnClickListener, NetworkCallbackListener {

    public static final String TAG = "NewRegisterFragment";

    /**
     * class private data member(s).
     */
    private MaterialEditText mEtFirstName, mEtLastName, mEtEmail, mEtAltEmail, mEtMobile, mEtAltMobile, mEtAddress, mEtLocationName, mEtPassword, mEtCfmPassword, mLocationPref;
    private Button mBtnRegister = null;
    private CheckBox mChkAcceptTerms = null;
    private String locationId = "";
    private String locationName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        Toast.makeText(getDashboardActivity(), "Enter profile info", Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.frag_register_new_user, container, false);

        mEtFirstName = (MaterialEditText) view.findViewById(R.id.et_first_name);
        mEtLastName = (MaterialEditText) view.findViewById(R.id.et_last_name);
        mEtEmail = (MaterialEditText) view.findViewById(R.id.et_email);
        mEtAltEmail = (MaterialEditText) view.findViewById(R.id.et_alt_email);
        mEtMobile = (MaterialEditText) view.findViewById(R.id.et_mobile);
        mEtAltMobile = (MaterialEditText) view.findViewById(R.id.et_alt_mobile);
        mEtAddress = (MaterialEditText) view.findViewById(R.id.et_address);
        mLocationPref = (MaterialEditText) view.findViewById(R.id.et_location_pref);
        mEtLocationName = (MaterialEditText) view.findViewById(R.id.et_location_pref);
        mEtPassword = (MaterialEditText) view.findViewById(R.id.et_password);
        mEtCfmPassword = (MaterialEditText) view.findViewById(R.id.et_conform_password);
        mChkAcceptTerms = (CheckBox) view.findViewById(R.id.chk_agree_terms);
        mBtnRegister = (Button) view.findViewById(R.id.btn_register_new);

        mBtnRegister.setOnClickListener(this);
        startActivity(new Intent(getActivity(), LocationActivity.class));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load the user location preference into object and update UI.
        locationId = LocationUtils.getInstance().getLocationId(getActivity(), "");
        locationName = LocationUtils.getInstance().getLocationName(getActivity(), "");
        mLocationPref.setText(locationName);

    }

    @Override
    public void fragQuitCallback() {

    }

    /**
     * {@link android.view.View.OnClickListener} click event callback method.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_register_new:

                registerNewUser();
                break;
        }
    }

    /**
     * @method registerNewUser
     * @desc Method to validate and register new user, calling the Http API.
     */
    private void registerNewUser() {

        // Fetch the data from UI fields.
        String strFirstName = mEtFirstName.getText().toString().trim();
        String strLastName = mEtLastName.getText().toString().trim();
        String strEmail = mEtEmail.getText().toString().trim();
        String strAltEmail = mEtAltEmail.getText().toString().trim();
        String strMobile = mEtMobile.getText().toString().trim();
        String strAltMobile = mEtAltMobile.getText().toString().trim();
        String strAddress = mEtAddress.getText().toString().trim();
        String strCompanyName = mEtLocationName.getText().toString().trim();
        String strPassword = mEtPassword.getText().toString().trim();
        String strConfirmPassword = mEtCfmPassword.getText().toString().trim();

        boolean isValid = false;

        /**
         * Name field validation.
         */
        if (strFirstName.isEmpty()) {

            isValid = false;
            mEtFirstName.setError(getString(R.string.cannot_be_empty));
        } else {

            isValid = true;
            mEtFirstName.setError(null);
        }

        if (strLastName.isEmpty()) {

            isValid = false;
            mEtLastName.setError(getString(R.string.cannot_be_empty));
        } else {

            isValid = isValid & true;
            mEtLastName.setError(null);
        }

        /**
         * Email field validation.
         */
        if (strEmail.isEmpty()) {

            isValid = false;
            mEtEmail.setError(getString(R.string.cannot_be_empty));

        } else if (!Pattern.compile(Constants.REGEX_EMAIL).matcher(strEmail).matches()) {

            isValid = false;
            mEtEmail.setError(getString(R.string.proper_email_id));

        } else {

            isValid = true && isValid;
            mEtEmail.setError(null);
        }

        if (strAltEmail.trim().isEmpty()) {

            isValid = true & isValid;
            mEtAltEmail.setError(null);

        } else if (!Pattern.compile(Constants.REGEX_EMAIL).matcher(strAltEmail).matches()) {

            isValid = false;
            mEtAltEmail.setError(getString(R.string.proper_email_id));

        } else {

            isValid = true & isValid;
            mEtAltEmail.setError(null);
        }

        /**
         * Mobile number validation.
         */
        if (strMobile.isEmpty()) {

            isValid = false;
            mEtMobile.setError(getString(R.string.cannot_be_empty));

        } else if (!Pattern.compile(Constants.REGEX_MOBILE).matcher(strMobile).matches()) {

            isValid = false;
            mEtMobile.setError(getString(R.string.proper_mobile_number));

        } else {

            isValid = true && isValid;
            mEtMobile.setError(null);
        }

        if (strAltMobile.trim().isEmpty()) {

            isValid = true & isValid;
            mEtAltMobile.setError(null);

        } else if (!Pattern.compile(Constants.REGEX_MOBILE).matcher(strAltMobile).matches()) {

            isValid = false;
            mEtAltMobile.setError(getString(R.string.proper_mobile_number));

        } else {

            isValid = true & isValid;
            mEtAltMobile.setError(null);
        }

        /**
         * Address field validation.
         */
        if (strAddress.isEmpty()) {

            isValid = false;
            mEtAddress.setError(getString(R.string.cannot_be_empty));
        } else {

            isValid = true && isValid;
            mEtAddress.setError(null);
        }

        if (strCompanyName.isEmpty()) {

            isValid = false;
            startActivity(new Intent(getActivity(), LocationActivity.class));
        } else {

            isValid = true && isValid;
            mEtLocationName.setError(null);
        }

        /**
         * Password field validations.
         */
        if (strPassword.isEmpty()) {

            isValid = false;
            mEtPassword.setError(getString(R.string.cannot_be_empty));
        } else {

            isValid = true && isValid;
            mEtPassword.setError(null);
        }

        // Check for confirm password match.
        if (!strConfirmPassword.equals(strPassword.trim())) {

            isValid = false;
            mEtCfmPassword.setError(getString(R.string.confirm_password_not_match));
        } else {

            isValid = true && isValid;
            mEtCfmPassword.setError(null);
        }

        /**
         * Check for terms accepted.
         */
        if (!mChkAcceptTerms.isChecked()) {

            isValid = false;
            mChkAcceptTerms.setError(getString(R.string.accept_the_terms));
            return;
        }

        /**
         * Proceed after validation.
         */
        if (!isValid) {

            return;
        }


        mLocationPref.setText(locationName);

        // Check if the customer has selected his location preference.
        if (locationId.isEmpty() || locationName.isEmpty()) {

            Toast.makeText(getActivity(), R.string.select_location_first, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LocationActivity.class));
            return;
        }
        JSONObject jsonNewUserRegisterRequest = new JSONObject();

        try {

            jsonNewUserRegisterRequest.put("name", strFirstName + " " + strLastName);
            jsonNewUserRegisterRequest.put("address", strAddress);
            jsonNewUserRegisterRequest.put("company_name", strCompanyName);
            jsonNewUserRegisterRequest.put("social_login", "no");
            jsonNewUserRegisterRequest.put("mobile", strMobile);
            jsonNewUserRegisterRequest.put("mobile2", strAltMobile);
            jsonNewUserRegisterRequest.put("email", strEmail);
            jsonNewUserRegisterRequest.put("email2", strAltEmail);
            jsonNewUserRegisterRequest.put("location", mLocationPref.getText().toString().trim());
            jsonNewUserRegisterRequest.put("location_id", locationId);
            jsonNewUserRegisterRequest.put("geo_lat", "");
            jsonNewUserRegisterRequest.put("geo_lng", "");
            jsonNewUserRegisterRequest.put("password", strPassword);
            jsonNewUserRegisterRequest.put("username", "");

            NetworkHandler networkHandler = new NetworkHandler();
            networkHandler.httpCreate(1, getDashboardActivity(), this, jsonNewUserRegisterRequest, Network.URL_NEW_REGISTRATION, NetworkHandler.RESPONSE_TYPE.JSON_OBJECT);
            networkHandler.executePost();

        } catch (JSONException excJson) {

            excJson.printStackTrace();
            Toast.makeText(getActivity(), "EXCEPTION: " + excJson.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    /**
     * {@link NetworkCallbackListener} http response callback method.
     */
    @Override
    public void networkSuccessResponse(int requestCode, JSONObject rawObject, JSONArray rawArray) {

        getDashboardActivity().getProgressDialog().hide();
        if (requestCode == 1) {     // New register response.

            userRegisterResponse(rawObject);
        } else if (requestCode == 2) {      // Login response.

            userLoginResponse(rawObject);
        } else if (requestCode == 3) {      // Customer Full Profile.

            userProfileResponse(rawObject);
        }
    }

    @Override
    public void networkFailResponse(int requestCode, String message) {

        Toast.makeText(getActivity(), "HTTP Failed: " + message, Toast.LENGTH_SHORT).show();
        getDashboardActivity().getProgressDialog().hide();
        if (requestCode == 1) {

        }
    }

    /**
     * @param json Http API response.
     * @method userRegisterResponse
     * @desc Method to register response.
     */
    private void userRegisterResponse(JSONObject json) {

        // Check if registered and navigate to login page.
        try {

            int statusCode = json.getInt("statusCode");
            String statusMessage = json.getString("statusMessage");

            if (statusCode == 200) {

                String strPassword = mEtPassword.getText().toString();
                String strEmail = mEtEmail.getText().toString();
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("email", strEmail);
                jsonRequest.put("social_login", "no");
                jsonRequest.put("password", strPassword);

                NetworkHandler networkHandler = new NetworkHandler();
                networkHandler.httpCreate(2, getDashboardActivity(), this, jsonRequest, Network.URL_LOGIN, NetworkHandler.RESPONSE_TYPE.JSON_OBJECT);
                networkHandler.executePost();

            } else {
                Toast.makeText(getActivity(), statusCode + "#" + statusMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException jsonExc) {

            jsonExc.printStackTrace();
            Toast.makeText(getActivity(), "JSONException: " + jsonExc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * @param json Email to login.
     * @method userLoginResponse
     * @desc Method to call for Customer Login API.
     */
    private void userLoginResponse(JSONObject json) {

        ModelParser modelParser = new ModelParser();
        Login login = (Login) modelParser.getModel(json.toString(), Login.class, null);
        SessionUtils.getInstance().saveSession(getActivity(), json);
        JSONObject jsonRequest = new JSONObject();
        try {

            jsonRequest.put("login_id", login.getData().getLoginId());
            jsonRequest.put("token", login.getData().getToken());
            jsonRequest.put("customer_id", login.getData().getCustomerId());

            NetworkHandler networkHandler = new NetworkHandler();
            networkHandler.httpCreate(3, getDashboardActivity(), this, jsonRequest, Network.URL_GET_CUST_PROFILE, NetworkHandler.RESPONSE_TYPE.JSON_OBJECT);
            networkHandler.executePost();

        } catch (Exception exc) {

            Toast.makeText(getActivity(), "Exception: " + exc.getMessage(), Toast.LENGTH_SHORT).show();
            exc.printStackTrace();
            Log.e(TAG, exc.getMessage());
        }
    }

    /**
     * @param json
     * @method userProfileResponse
     * @desc Method to handle the user/customer full profile details.
     */
    private void userProfileResponse(JSONObject json) {

        ModelParser modelParser = new ModelParser();
        CustomerProfileHandler.CUSTOMER = (Customer) modelParser.getModel(json.toString(), Customer.class, null);

        getDashboardActivity().signalLoadFragment(DashboardInterruptListener.FRAGMENT_TYPE.PROFILE);

    }

}
