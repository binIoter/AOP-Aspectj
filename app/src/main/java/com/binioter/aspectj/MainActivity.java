package com.binioter.aspectj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.binioter.aspect.annotation.DebugTrace;
import com.binioter.aspect.util.MLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  }

}
