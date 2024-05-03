package com.example.proj_00;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private TextView out = null ;
    private Boolean
            flag_ptr=true,
            flag_opt=false;

    private LinkedList<String> vls = new LinkedList<>();
    private String cnt="";

    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        out=findViewById(R.id.out);
        vls.add("");
    }

    // Operations:
    String values(String end){
        cnt=" ";
        vls.forEach(vl -> cnt+=vl);
        return cnt+end;
    }

    void operate(String sb){
        if(!flag_opt) return;
        out.setText(values(sb));
        vls.add(sb);
        vls.add("");
        i+=2;
        flag_opt=false;
    }

    void clr(String value){
        vls.clear();
        vls.add("");
        flag_ptr=true;
        flag_opt=false;
        i=0;
        out.setHint(value);
        out.setText("");
    }

    float parse(String value){ return (value.equals("")? 0 : Float.parseFloat(value)); }

    double div(double a ,double b) throws Exception{
        if(b==0) throw new Exception();
        BigDecimal bg = new BigDecimal(a/b).setScale(5, RoundingMode.HALF_EVEN);
        return bg.doubleValue();
    }

    void calc(int size){
        if(vls.getLast().equals("")) return;
        double value = Double.parseDouble(vls.getFirst());
        for(int i=1;i<size;i++){
            if(i%2==0){
                switch (vls.get(i-1)){
                    case "+": { value+=parse(vls.get(i)); } break;
                    case "-": { value-=parse(vls.get(i)); } break;
                    case "×": { value*=parse(vls.get(i)); } break;
                    case "÷": {
                        try {
                            value = div(value, parse(vls.get(i)));
                        }catch (Exception e){
                            if      (value>0) clr(values("= ∞"));
                            else if (value<0) clr(values("= -∞"));
                            else              clr(values("= NaN"));
                            return;
                        }
                    } break;
                }
            }
        }
        clr(values("=")+value);
    }

    // Numbers:
    void changer_number(String n){
        vls.set(i,vls.get(i)+n);
        out.setText(values(""));
    }

    void pnt(){
        if(!flag_ptr) return;
        changer_number(".");
        flag_ptr=false;
    }

    void number(String n){
        changer_number(n);
        flag_opt=true;

    }

    // Manager:
    public void manager(View v){
        Button btn = findViewById(v.getId());

        if      (v.getId()==R.id.btn_add) operate("+");
        else if (v.getId()==R.id.btn_sub) if(flag_opt) operate("-"); else number("-");
        else if (v.getId()==R.id.btn_mtl) operate("×");
        else if (v.getId()==R.id.btn_div) operate("÷");
        else if (v.getId()==R.id.btn_clr) clr("0");
        else if (v.getId()==R.id.btn_rlt){
          try { calc(vls.size());}
          catch (Exception e){ clr(" : ("); }

        } else if (v.getId()==R.id.btn_ptr) pnt();
        else    number(btn.getText().toString());
    }
}