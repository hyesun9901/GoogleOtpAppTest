package com.example.otptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    TextView textView;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textViewUserPrivateKey);
        String id = textView.getText().toString();
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cManager = (ClipboardManager)  getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", textView.getText());
                cManager.setPrimaryClip(cData);
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "클립보드에 복사됨!!", Toast.LENGTH_LONG);
                toast.show();

                return true;
            }
        });

    }

    public void doLogin(View v) {
        EditText editUserId = (EditText) findViewById(R.id.editUserId);
        String UserId = editUserId.getText().toString();
        String companyURL = "softcamptest.com"; //안드로이드 빌드 시 세팅되어있는 bundle name같은거 가져오면될듯

        GoogleOTP otp = new GoogleOTP();
        HashMap<String, String> map = otp.generate(UserId, companyURL);

        //사용자에게 알려줄 Url과 encodekey
        String url = map.get("url");
        TextView editURL = (TextView) findViewById(R.id.editURL);
        editURL.setText(url);

        String googleOtpAppkey = map.get("encodedKey");
        TextView editSecretKey = (TextView) findViewById(R.id.textViewUserPrivateKey);
        editSecretKey.setText(googleOtpAppkey);

        //위 Url과 encodeKey는 공유메모리에 넣었다치고~
        //사용자는 Google otp앱에 등록히여야함
    }

    public void checkOTP(View v){
        GoogleOTP otp = new GoogleOTP();

        // 우선 로그인 버튼 클릭 시 생성된 키를 Google OTP앱에 등록 후
        // 표시되는 otp 번호와 생성된 키를 넣어주면 true가 나올 것이다.
        EditText editOtpNum= (EditText) findViewById(R.id.editOtpNum);
        String otpRandomNumber = editOtpNum.getText().toString();

        TextView editUserPrivateKey = (TextView) findViewById(R.id.textViewUserPrivateKey);
        String otpUserPrivateKey = editUserPrivateKey.getText().toString();

        boolean bOtpCheck = otp.checkCode(otpRandomNumber, otpUserPrivateKey);
        if(bOtpCheck){
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "인증되었습니다.", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "인증실패. OTP번호를 다시 확인해주세요.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}