package com.branch.result_ble_uwb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Boolean isExistBlank = false;
    Boolean isPWSame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_login = findViewById(R.id.loginButton);
        Button btn_register = findViewById(R.id.joinButton);
        EditText edit_id = findViewById(R.id.id);
        EditText edit_pw = findViewById(R.id.pw);

        btn_login.setOnClickListener(v -> {
            String id = edit_id.getText().toString();
            String pw = edit_pw.getText().toString();

            if(id.equals("") && pw.equals("")){ // 아이디 일치 시 Toast 알람 후 페이지 이동
                dialog("success");
                Intent intent = new Intent(this, ChooseMode.class);
                startActivity(intent);
            }
            else{
                dialog("fail");
            }
        });

        btn_register.setOnClickListener(v -> { // 회원가입 페이지로 이동
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });
    }

    private void dialog(String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        if(message.equals("success")){
            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
        }
        else if(message.equals("fail")){
            dialog.setTitle("로그인 실패");
            dialog.setMessage("아이디와 비밀번호를 확인해주세요");
            dialog.setPositiveButton("확인", (dialog1, id) ->
                    Log.d("Main", "fail"));
            dialog.show();
        }
    }
}