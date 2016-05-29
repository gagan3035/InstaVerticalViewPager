package com.example.gagan.lbbtask.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.gagan.lbbtask.VerticalViewPager;
import com.example.gagan.lbbtask.adapter.InstaVerticalPagerAdapter;
import com.example.gagan.lbbtask.listeners.AuthenticationListener;
import com.example.gagan.lbbtask.R;
import com.example.gagan.lbbtask.constants.Constants;
import com.example.gagan.lbbtask.model.Picture;
import com.example.gagan.lbbtask.model.TokenResponse;
import com.example.gagan.lbbtask.services.ServiceGenerator;
import com.example.gagan.lbbtask.services.ServiceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gagan on 5/28/2016.
 */
public class LoginActivity extends AppCompatActivity implements AuthenticationListener {

    AuthenticationDialog dialog;
    EditText mText;
    String mAuthToken;
    VerticalViewPager verticalViewPager;
    InstaVerticalPagerAdapter instaVerticalPagerAdapter;
    private  int height, width;

    List<Picture> mPictures= new ArrayList<>();
    private String mMaxId, mMinId;


    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);
        //screenSize();
        showAuthenticationDialog();
    }

    private void screenSize() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
        Display display = wm.getDefaultDisplay(); // getting the screen size of device
        Point size = new Point();
        display.getSize(size);
         width = size.x;
         height = size.y;
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            showAuthenticationDialog();
        }
    }

    private void showAuthenticationDialog() {
        screenSize();
        dialog = new AuthenticationDialog(LoginActivity.this, this, height,width);
        dialog.show();
    }

    @Override
    public void onCodeReceived(String code) {
        if(code!= null){
            dialog.dismiss();

        } else {
            finish();
        }
        showProgressDialog();
        //mSpinnerll.setVisibility(View.VISIBLE);

        final Call<TokenResponse> accessToken =  ServiceGenerator.createTokenService().getAccessToken(Constants.CLIENT_ID,Constants.CLIENT_SECRET,Constants.REDIRECT_URI, Constants.AUTORISATION_CODE,code);
        accessToken.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {

                if(response.isSuccessful()){
                    mAuthToken = response.body().getAccess_token();
                    Log.d("access_tocken", mAuthToken);
                    getTagResults(Constants.HASHTAG, "", "");

                }else{
                    try {
                        mText.setText(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                mText.setText("failure");
                finish();
            }
        });


    }

    private void initViewpager(ArrayList<Picture> pictures) {
        mPictures.addAll(pictures);
        verticalViewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        instaVerticalPagerAdapter = new InstaVerticalPagerAdapter(getApplicationContext(), mPictures);
        if (verticalViewPager != null && !mPictures.isEmpty()) {
            verticalViewPager.setAdapter(instaVerticalPagerAdapter);
            verticalViewPager.setOffscreenPageLimit(Constants.PAGES_COUNT);
            verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == mPictures.size()*(0.7) ) {
                        getTagResults(Constants.HASHTAG, mMaxId, mMinId);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }

    //"631477962.1fb234f.f7c5cda97c7f4df983b1c764f066ed37"
//"3249369591.dd45fa2.8dc881c8d2c24ef8a5b1951b0cd272b0"
    private void getTagResults(String query, String minId, String maxId) {
        showProgressDialog();

        final ArrayList<Picture> Pictures = new ArrayList<Picture>();

        Call<ResponseBody> response = ServiceManager.createService().getResponse(query, "631477962.1fb234f.f7c5cda97c7f4df983b1c764f066ed37", minId, maxId);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    hideProgressDialog();


                    StringBuilder sb = new StringBuilder();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }

                        JSONObject tagResponse = new JSONObject(sb.toString());
                        Log.e("response",sb.toString());

                        for (int i = 0; i < tagResponse.length() - 2; i++) {
                            JSONObject pagination = tagResponse.getJSONObject("pagination");
                            // in case of sandbox image or not that much available maxId not present
                            mMaxId = pagination.getString("next_max_id");
                            mMinId = pagination.getString("next_min_id");

                            JSONObject meta = tagResponse.getJSONObject("meta");
                            JSONArray data = tagResponse.getJSONArray("data");

                            for (int j = 0; j < data.length(); j++) {

                                JSONArray tags = data.getJSONObject(j).getJSONArray("tags");


                                JSONObject images = data.getJSONObject(j).getJSONObject("images").getJSONObject("low_resolution");


                                Picture picture = new Picture();
                                picture.setURL(images.getString("url"));
                                Pictures.add(picture);

                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                if (verticalViewPager == null) {
                    initViewpager(Pictures);
                } else {
                    instaVerticalPagerAdapter.addData(Pictures);
                    instaVerticalPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
            }

        });


    }

    private void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading images...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

    }
}
