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

        Button btn_login = findViewById(R.id.btn_login);
        Button btn_register = findViewById(R.id.btn_register);
        EditText edit_id = findViewById(R.id.edit_id);
        EditText edit_pw = findViewById(R.id.edit_pw);

        btn_login.setOnClickListener(v -> {
            String id = edit_id.getText().toString();
            String pw = edit_pw.getText().toString();
            Log.d("VEX", id);
            Log.d("VEX", pw);

            if(id.equals("") && pw.equals("")){
                dialog("success");
                Intent intent = new Intent(this, ChooseMode.class);
                startActivity(intent);
            }
            else{
                dialog("fail");
            }
        });

        btn_register.setOnClickListener(v -> {
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