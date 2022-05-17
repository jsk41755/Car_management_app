package com.example.car_management_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Frag1 extends Fragment {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    TextView textView;
    EditText textEdit;
    String TempText, Text;
    public static String kakaoID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View v=inflater.inflate(R.layout.activity_tab1_fragment,container,false);

        textView = v.findViewById(R.id.textView32);
        textEdit = v.findViewById(R.id.editTextTextPersonName2);
        updateKakaoLoginUi(); //실행시간동안 다른 코드 실행 중지 받아오는게 느려서 다른코드 실행 불가

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseReference.child("Car_Management").child(kakaoID).child("1").child("memo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TempText = snapshot.getValue(String.class);
                        textEdit.setText(TempText);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                textEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        Text = textEdit.getText().toString();
                        databaseReference.child("Car_Management").child(kakaoID).child("1").child("memo").setValue(Text);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


            }
        }, 300);
        return v;
    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){
                    kakaoID = user.getKakaoAccount().getProfile().getNickname();
                    Log.d("이거 카카오 닉네임", kakaoID);
                }
                else{

                }
                if (throwable != null) {
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                return null;
            }
        });
    }

}