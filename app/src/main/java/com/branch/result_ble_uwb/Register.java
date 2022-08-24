package com.branch.result_ble_uwb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    Boolean isExistBlank = false;
    Boolean isPWSame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn_cancel = findViewById(R.id.btn_cancel);
        Button btn_register2 = findViewById(R.id.btn_register);
        EditText edit_id = findViewById(R.id.edit_id);
        EditText edit_pw = findViewById(R.id.edit_pw);
        EditText edit_pw_re = findViewById(R.id.edit_pw_re);

        btn_cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        btn_register2.setOnClickListener(v -> {
            String id = edit_id.getText().toString();
            String pw = edit_pw.getText().toString();
            String pw_re = edit_pw_re.getText().toString();

            // 유저가 항목을 다 채우지 않았을 경우
            if(id.isEmpty() || pw.isEmpty() || pw_re.isEmpty()){
                isExistBlank = true;
            }
            else{
                if(pw.equals(pw_re)){
                    isPWSame = true;
                }
            }

            if(!isExistBlank && isPWSame){
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                /*val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE);
                val editor = sharedPreference.edit();
                editor.putString("id", id);
                editor.putString("pw", pw);
                editor.apply();*/

                // 로그인 화면으로 이동
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
            else{

                // 상태에 따라 다른 다이얼로그 띄워주기
                if(isExistBlank){   // 작성 안한 항목이 있을 경우
                    dialog("blank");
                }
                else if(!isPWSame){ // 입력한 비밀번호가 다를 경우
                    dialog("not same");
                }
            }
        });
    }

    private void dialog(String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        if(message.equals("blank")){
            dialog.setTitle("회원가입 실패");
            dialog.setMessage("입력란을 모두 작성해주세요");
        }
        // 입력한 비밀번호가 다를 경우
        else if(message.equals("not same")){
            dialog.setTitle("회원가입 실패");
            dialog.setMessage("비밀번호가 다릅니다");
        }

        dialog.setPositiveButton("확인", (dialog1, id) ->
                Log.d("Register", "다이얼로그"));
        dialog.show();
    }
}
