package com.zhz.pickers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack_zhuo on 2017/11/20.
 */

public class KeyboardPopupWindow extends PopupWindow implements View.OnClickListener{

    public static int CALCULATER = 0;
    public static int NUM = 1;

    private Context context;
    private OnTextChange mOnTextChange;
    private View view;
    private int type;
    String num = "";
    boolean isCalculating = false;
    private int maxSize = 100;

    public KeyboardPopupWindow(Context context,String num,int type){
        if(CALCULATER == type){
            this.view = LayoutInflater.from(context).inflate(R.layout.popup_keyboard_calculater, null);
        }else if(NUM == type){
            this.view = LayoutInflater.from(context).inflate(R.layout.popup_keyboard_num, null);
        }
        this.type = type;
        this.context = context;
        this.num = num;
        this.setOutsideTouchable(true);
        this.setContentView(this.view);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setFocusable(true);
        this.setAnimationStyle(R.style.pop_anim);

        initView();
    }

    public interface OnTextChange{

        void textChange(String text);
    }
    public void setmOnTextChange(OnTextChange onTextChange){
        this.mOnTextChange = onTextChange;
    }

    public void setMaxSize(int size){
        this.maxSize = size;
    }
    @Override
    public void onClick(View v) {
        String str = "";
        int vId = v.getId();
        if(vId == R.id.key_0){
            str = "0";
        }else if(vId == R.id.key_1) {
            str = "1";
        }else if(vId == R.id.key_2) {
            str = "2";
        }else if(vId == R.id.key_3) {
            str = "3";
        }else if(vId == R.id.key_4) {
            str = "4";
        }else if(vId == R.id.key_5) {
            str = "5";
        }else if(vId == R.id.key_6) {
            str = "6";
        }else if(vId == R.id.key_7) {
            str = "7";
        }else if(vId == R.id.key_8) {
            str = "8";
        }else if(vId == R.id.key_9) {
            str = "9";
        }else if(vId == R.id.key_point) {
            str = ".";
        }else if(vId == R.id.key_add) {
            str = "+";
        }else if(vId == R.id.key_minus) {
            str = "-";
        }else if(vId == R.id.key_del) {
            str = "del";
        }else if(vId == R.id.key_ok){
            str = "ok";
        }
        doCount(str);
        if(mOnTextChange != null){
            mOnTextChange.textChange(num);;
        }
        if("ok".equals(str)) this.dismiss();
    }

    public void initView(){
        List<Integer> id_list = new ArrayList<>();
        id_list.add(R.id.key_0);
        id_list.add(R.id.key_1);
        id_list.add(R.id.key_2);
        id_list.add(R.id.key_3);
        id_list.add(R.id.key_4);
        id_list.add(R.id.key_5);
        id_list.add(R.id.key_6);
        id_list.add(R.id.key_7);
        id_list.add(R.id.key_8);
        id_list.add(R.id.key_9);
        id_list.add(R.id.key_del);
        if(CALCULATER == type) {
            id_list.add(R.id.key_add);
            id_list.add(R.id.key_minus);
            id_list.add(R.id.key_point);
            id_list.add(R.id.key_ok);
        }
        setListener(id_list);
    }

    public void setListener(List<Integer> views){
        for(int id : views){
            View v = view.findViewById(id);
            v.setOnClickListener(this);
        }
    }

    public boolean isNum(String str){
        if("+".equals(str) || "-".equals(str) || "point".equals(str) || "del".equals(str) || "ok".equals(str)) return false;
        return true;
    }

    public boolean isEndWithCalculate(){
        if(num.endsWith("+") || num.endsWith("-")) return true;
        return false;
    }

    public boolean canAddPoint(){
        if(!num.contains(".")) return true;
        if(num.contains("+") && num.indexOf("+") < num.lastIndexOf(".")){
            return false;
        }
        if(num.contains("-") && num.indexOf("-") < num.lastIndexOf(".")){
            return false;
        }
        return true;
    }

    public void calculate(){
        if(num.contains("+")){
            String[] nums = num.split("\\+");
            BigDecimal num1 = new BigDecimal(nums[0]);
            BigDecimal num2 = new BigDecimal(nums[1]);
            num = num1.add(num2).toString();
        }else{
            String[] nums = num.split("-");
            BigDecimal num1 = new BigDecimal(nums[0]);
            BigDecimal num2 = new BigDecimal(nums[1]);
            num = num1.subtract(num2).toString();
        }
    }

    public void doCount(String str){
//        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
        if(num.length() >= maxSize && !str.equals("del")){
            return;
        }
        if(num.equals("")){
            if(isNum(str)){
                num = str;
                return;
            }else{
                return;
            }
        }else{
            if(isNum(str)){
                if(num.contains(".") && num.lastIndexOf(".") + 3 == num.length()){
                    return;
                }else{
                    num += str;
                    return;
                }
            }else{
                if(isEndWithCalculate()){
                    if(str.equals("+") || str.equals("-")){
                        num = num.substring(0,num.length()-1);
                        num += str;
                        return;
                    }
                    if(str.equals("del") || str.equals("ok")){
                        num = num.substring(0,num.length()-1);
                        isCalculating = false;
                        return;
                    }
                    if(str.equals("point")) return;
                }else{

                    if(str.equals("+") || str.equals("-")){
                        if(isCalculating) {
                            calculate();
                        }
                        num += str;
                        isCalculating = true;
                        return;
                    }else if(str.equals("point")){
                        if(canAddPoint()) num += ".";
                        return;
                    }else if(str.equals("del")){
                        num = num.substring(0,num.length()-1);
                        if(num.length()==0)num = "";
                        return;
                    }else{
                        if(isCalculating) {
                            calculate();
                        }
                        isCalculating = false;
                    }

                }
            }
        }


    }
}
