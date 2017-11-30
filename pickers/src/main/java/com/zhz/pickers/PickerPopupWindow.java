package com.zhz.pickers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhz.pickers.util.CommonRecylerAdapter;
import com.zhz.pickers.util.ItemTouchListener;
import com.zhz.pickers.util.RecyclerViewHolder;
import com.zhz.pickers.util.Tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Zack_zhuo on 2017/9/3.
 */

public class PickerPopupWindow extends PopupWindow {

    public static final int HALF_YEAR_LENGTH = 25;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int MONTH_AND_DAY = 3;

    private Context context;
    private String year = "";
    private String month = "";
    private String day = "";
    private List<String> yearAndMonth;
    private List<String> monthAndDay;
    private int position = HALF_YEAR_LENGTH;
    private int type = MONTH;
    List<Map<String,String>> recycler_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommonRecylerAdapter adapter = null;
    private TextView dateText;
    private TextView changeText;
    private TextView startText;
    private TextView endText;

    private String start_year = "";
    private String start_month = "";
    private String start_day = "";
    private String end_year = "";
    private String end_month = "";
    private String end_day = "";
    private int last_type = MONTH;
    private boolean is_start_choose = true;
    private boolean hasSetStartDate = false;
    private boolean hasSetEndDate = false;

    public PickerPopupWindow(Context context,int type, int last_type){
//        this(context, ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_layout, null),
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true, type, "", "", "", last_type);

        this.context = context;
        this.type = type;
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR) + "";
        this.month = (calendar.get(Calendar.MONTH)+1) + "";
        this.day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        this.start_year = year;
        this.start_month = month;
        this.end_year = year;
        this.end_month = month;
        this.last_type = last_type;
        View view = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);
        this.setOutsideTouchable(true);
        this.setContentView(view);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setFocusable(true);
        this.setAnimationStyle(R.style.pop_anim);
        initList();
        initView(view);
    }

    public PickerPopupWindow(Context context,int type){
        this.context = context;
        this.type = type;
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR) + "";
        this.month = (calendar.get(Calendar.MONTH)+1) + "";
        this.day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        View view = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);
        this.setOutsideTouchable(true);
        this.setContentView(view);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        this.setFocusable(true);
        this.setAnimationStyle(R.style.pop_anim);
        initList();
        initView(view);
    }

    public PickerPopupWindow(Context context, View contentView, int width, int height,
                             boolean focusable, int type, String year, String month, String day){

        super(contentView, width, height, focusable);
        this.context = context;
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        initList();
        initView(contentView);
    }

    public PickerPopupWindow(Context context, View contentView, int width, int height,
                             boolean focusable, int type, String year, String month, String day,
                             int last_type){
        super(contentView, width, height, focusable);
        this.context = context;
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.start_year = year;
        this.start_month = month;
//        this.start_day = start_day;
        this.end_year = year;
        this.end_month = month;
//        this.end_day = start_day;
        this.last_type = last_type;
        initList();
        initView(contentView);
    }

    public interface OnDateSelected{
        void dateselected(String year, String month, String day);
    }

    public interface OnDateRangeSelected{
        void monthSelected(String year, String month);
        void startDaySelected(String start_year, String start_month, String start_day);
        void endDaySelected(String end_year, String end_month, String end_day);
    }

    public OnDateSelected onDateSelected;
    public OnDateRangeSelected onDateRangeSelected;

    public void setOnDateSelected(OnDateSelected onDateSelected){
        this.onDateSelected = onDateSelected;
    }

    public void setOnDateRangeSelected(OnDateRangeSelected onDateRangeSelected){
        this.onDateRangeSelected = onDateRangeSelected;
    }

    public void setStartDate(String year,String month,String day){
        start_year = year;
        start_month = month;
        start_day = day;
        hasSetStartDate = true;
        is_start_choose = false;
        adapter.notifyDataSetChanged();
    }

    public void setEndDate(String year,String month,String day){
        end_year = year;
        end_month = month;
        end_day = day;
        hasSetEndDate = true;
        is_start_choose = true;
        adapter.notifyDataSetChanged();
    }

    public void initList(){
        yearAndMonth = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
        int now_year = Tools.string2int(year);
        int now_month = Tools.string2int(month);
        int now_day = Tools.string2int(day);
        switch (type){
            case MONTH:


                for(int i = 0;i<50;i++){
                    yearAndMonth.add((i+ now_year -HALF_YEAR_LENGTH)+"");
                }
                break;
            case DAY:
                for(int i = 0;i<50;i++){
                    int temp_year = now_year -HALF_YEAR_LENGTH + i;
                    for(int j = 1;j<=12;j++){
                        yearAndMonth.add(temp_year + "年" + j + "月");
                        if(now_year == temp_year && j == now_month) position = yearAndMonth.size()-1;
                    }
                }
                break;
            case MONTH_AND_DAY:
//                    if(last_type == 0) {
//                        for (int i = 0; i < 50; i++) {
//                            yearAndMonth.add((i + now_year - Params.HALF_YEAR_LENGTH) + "");
//                        }
//                    }else{
//                        for(int i = 0;i<50;i++){
//                            int temp_year = now_year -Params.HALF_YEAR_LENGTH + i;
//                            for(int j = 1;j<=12;j++){
//                                yearAndMonth.add(temp_year + "年" + j + "月");
//                                if(now_year == temp_year && j == now_month) position = yearAndMonth.size()-1;
//                            }
//                        }
//                    }
                initMonthAndDayList();
                break;
        }
    }

    public void initView(View contentView){

        if(type == MONTH_AND_DAY) {
            startText = (TextView)contentView.findViewById(R.id.picker_start);
            startText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startText.setBackgroundColor(ContextCompat.getColor(context,R.color.blue));
                    startText.setTextColor(Color.WHITE);
                    endText.setBackgroundColor(Color.WHITE);
                    endText.setTextColor(ContextCompat.getColor(context,R.color.blue));
                    is_start_choose = true;
                    initMonthAndDayList();
                    dateText.setText(yearAndMonth.get(position));
                    recycler_list.clear();
                    recycler_list.addAll(getDayList());
                    adapter.notifyDataSetChanged();
                }
            });
            endText = (TextView)contentView.findViewById(R.id.picker_end);
            endText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endText.setBackgroundColor(ContextCompat.getColor(context,R.color.blue));
                    endText.setTextColor(Color.WHITE);
                    startText.setBackgroundColor(Color.WHITE);
                    startText.setTextColor(ContextCompat.getColor(context,R.color.blue));
                    is_start_choose = false;
                    initMonthAndDayList();
                    dateText.setText(yearAndMonth.get(position));
                    recycler_list.clear();
                    recycler_list.addAll(getDayList());
                    adapter.notifyDataSetChanged();
                }
            });
            changeText = (TextView) contentView.findViewById(R.id.picker_change_day_month);
            changeText.setVisibility(View.VISIBLE);
            changeText.setText(context.getResources().getString(R.string.picker_change_to_day));
            changeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(last_type == MONTH){
                        last_type = DAY;
                        changeText.setText(context.getResources().getString(R.string.picker_change_to_month));
                        startText.setVisibility(View.VISIBLE);
                        endText.setVisibility(View.VISIBLE);
                    }else{
                        last_type = MONTH;
                        changeText.setText(context.getResources().getString(R.string.picker_change_to_day));
                        startText.setVisibility(View.GONE);
                        endText.setVisibility(View.GONE);
                    }
                    position = HALF_YEAR_LENGTH;
                    initMonthAndDayList();
                    resetPicker();
                }
            });
        }


        dateText = (TextView) contentView.findViewById(R.id.picker_text);
        dateText.setText(yearAndMonth.get(position));

        final TextView pre = (TextView)contentView.findViewById(R.id.picker_pre);
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position<0){
                    Toast.makeText(context,"别捣乱！哪有记那么久远的帐。",Toast.LENGTH_SHORT).show();
                    return;
                }
                String temp = yearAndMonth.get(position);
                int temp_tepe = type == MONTH_AND_DAY? last_type:type;
                switch (temp_tepe){
                    case MONTH:
                        year = temp;
//                        start_year = temp;
                        break;
                    case DAY:

                        year = temp.substring(0,temp.indexOf("年"));
                        month = temp.substring(temp.indexOf("年")+1,temp.indexOf("月"));
                        if(type == MONTH_AND_DAY && is_start_choose){
                            start_year = temp.substring(0,temp.indexOf("年"));
                            start_month = temp.substring(temp.indexOf("年")+1,temp.indexOf("月"));
                        }else if(type == MONTH_AND_DAY && !is_start_choose){
                            end_year = temp.substring(0,temp.indexOf("年"));
                            end_month = temp.substring(temp.indexOf("年")+1,temp.indexOf("月"));
                        }
                        recycler_list.clear();
                        recycler_list.addAll(getDayList());
                        adapter.notifyDataSetChanged();
                }
                if(null != onDateSelected)
                onDateSelected.dateselected(year,month,day);
                if(null != onDateRangeSelected){
                    if(last_type == MONTH)
                        onDateRangeSelected.monthSelected(year,month);
                    if(last_type == DAY) {
                        if(is_start_choose) {
                            onDateRangeSelected.startDaySelected(start_year, start_month,start_day);
                        }else{
                            onDateRangeSelected.endDaySelected(end_year,end_month,end_day);
                        }
                    }
                }
                dateText.setText(temp);
            }
        });
        TextView next = (TextView)contentView.findViewById(R.id.picker_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position>=yearAndMonth.size()){
                    Toast.makeText(context,"别捣乱！哪有记那么久远的帐。",Toast.LENGTH_SHORT).show();
                    return;
                }
                String temp = yearAndMonth.get(position);
                int temp_tepe = type == MONTH_AND_DAY? last_type:type;
                switch (temp_tepe){
                    case MONTH:
                        year = temp;
//                        start_year = temp;
                        break;
                    case DAY:

                        year = temp.substring(0,temp.indexOf("年"));
                        month = temp.substring(temp.indexOf("年")+1,temp.indexOf("月"));
                        if(type == MONTH_AND_DAY && is_start_choose){
                            start_year = temp.substring(0,temp.indexOf("年"));
                            start_month = temp.substring(temp.indexOf("年")+1,temp.indexOf("月"));
                        }else if(type == MONTH_AND_DAY && !is_start_choose){
                            end_year = temp.substring(0,temp.indexOf("年"));
                            end_month = temp.substring(temp.indexOf("年")+1,temp.indexOf("月"));
                        }
                        recycler_list.clear();
                        recycler_list.addAll(getDayList());
                        adapter.notifyDataSetChanged();

                }
                if(null != onDateSelected)
                onDateSelected.dateselected(year,month,day);
                if(null != onDateRangeSelected){
                    if(last_type == MONTH)
                        onDateRangeSelected.monthSelected(year,month);
                    if(last_type == DAY) {
                        if(is_start_choose) {
                            onDateRangeSelected.startDaySelected(start_year, start_month,start_day);
                        }else{
                            onDateRangeSelected.endDaySelected(end_year,end_month,end_day);
                        }
                    }
                }
                dateText.setText(temp);
            }
        });
        recyclerView = (RecyclerView)contentView.findViewById(R.id.picker_m_d);




        recyclerView.setLayoutManager(setRecyclerAdapter());
        recyclerView.setAdapter(adapter);
    }

    public void initMonthAndDayList(){
        int now_year = Tools.string2int(start_year);
        int now_month = Tools.string2int(start_month);
        int now_day = Tools.string2int(start_day);
        if(last_type == MONTH) {
            for (int i = 0; i < 50; i++) {
                String temp_year = i + now_year - HALF_YEAR_LENGTH + "";
                if(temp_year.equals(year)) position = yearAndMonth.size();
                yearAndMonth.add(temp_year);
            }
        }else{
            if(is_start_choose){
                now_year = Tools.string2int(start_year);
                now_month = Tools.string2int(start_month);
            }else{
                now_year = Tools.string2int(end_year);
                now_month = Tools.string2int(end_month);
            }
            for(int i = 0;i<50;i++){
                int temp_year = now_year -HALF_YEAR_LENGTH + i;
                for(int j = 1;j<=12;j++){
                    yearAndMonth.add(temp_year + "年" + j + "月");
                    if(now_year == temp_year && j == now_month) position = yearAndMonth.size()-1;
                }
            }
        }
    }

    public void resetPicker(){
        dateText.setText(yearAndMonth.get(position));
        recyclerView.setLayoutManager(setRecyclerAdapter());
        recyclerView.setAdapter(adapter);
    }

    public GridLayoutManager setRecyclerAdapter(){
        GridLayoutManager gridLayoutManager = null;
        recycler_list.clear();
        int temp_tepe = type == MONTH_AND_DAY? last_type:type;
        String temp_month = type == MONTH_AND_DAY?start_month:month;
        switch (temp_tepe){
            case MONTH:
                for (int i = 1;i<=12;i++){
                    Map<String,String> map = new HashMap<>();
                    map.put("month",i+"");
                    if(i== Integer.parseInt(temp_month)){
                        map.put("isselected","1");
                    }else {
                        map.put("isselected", "0");
                    }
                    recycler_list.add(map);
                }
                gridLayoutManager = new GridLayoutManager(context, 4, OrientationHelper.VERTICAL, false);

                adapter = new CommonRecylerAdapter(context, R.layout.item_for_picker_recycler, recycler_list, new ItemTouchListener() {
                    @Override
                    public void onItemClick(Map map, int position) {
                        month = map.get("month").toString();
                        for(int i = 0;i<recycler_list.size();i++){
                            Map<String,String> item = recycler_list.get(i);
                            String isselected = item.get("isselected");
                            if(isselected.equals("1")){
                                recycler_list.get(i).put("isselected","0");
                                adapter.notifyItemChanged(i);
                                break;
                            }
                        }
                        recycler_list.get(position).put("isselected","1");
                        adapter.notifyItemChanged(position);
                        if(null != onDateSelected)
                        onDateSelected.dateselected(year,month,day);
                        if(null != onDateRangeSelected){
                            onDateRangeSelected.monthSelected(year,month);
                        }
                    }

                    @Override
                    public void onItemClick(Object date, int position) {

                    }
                }) {
                    @Override
                    public void setDate(RecyclerViewHolder.ViewHolder holder, Map item, int position) {
                        holder.setText(R.id.picker_day_text,item.get("month").toString());
                        if(item.get("isselected").toString().equals("1")){
                            holder.setTextColor(R.id.picker_day_text, Color.BLUE);
                        }else{
                            holder.setTextColor(R.id.picker_day_text, Color.BLACK);
                        }
                    }
                };
                break;
            case DAY:
                recycler_list.addAll(getDayList());


                gridLayoutManager = new GridLayoutManager(context, 7, OrientationHelper.VERTICAL, false);

                adapter = new CommonRecylerAdapter(context, R.layout.item_for_picker_recycler, recycler_list, new ItemTouchListener() {
                    @Override
                    public void onItemClick(Map map,int position) {
                        String item_type = map.get("type").toString();
                        String canchoose = map.get("canchoose").toString();
                        if(item_type.equals("day") && canchoose.equals("1")){
                            day = map.get("day").toString();
                            if(type == MONTH_AND_DAY && last_type == DAY){
                                if(is_start_choose){
                                    start_day = day;
                                }else{
                                    end_day = day;
                                }
                            }
                            for(int i = 0;i<recycler_list.size();i++){
                                Map<String,String> item = recycler_list.get(i);
                                String isselected = item.get("isselected");
                                if(isselected.equals("1")){
                                    recycler_list.get(i).put("isselected","0");
                                    adapter.notifyItemChanged(i);
                                    break;
                                }
                            }
                            recycler_list.get(position).put("isselected","1");
                            adapter.notifyItemChanged(position);
                            if(null != onDateSelected)
                            onDateSelected.dateselected(year,month,day);
                            if(null != onDateRangeSelected){
                                if(is_start_choose){
                                    onDateRangeSelected.startDaySelected(start_year,start_month,day);
                                }else{
                                    onDateRangeSelected.endDaySelected(end_year,end_month,day);
                                }
                            }
                        }
                    }

                    @Override
                    public void onItemClick(Object date,int position) {

                    }
                }) {
                    @Override
                    public void setDate(RecyclerViewHolder.ViewHolder holder, Map item, int position) {
//                        System.out.println("setData ==========" + position);
                        String type = item.get("type").toString();
                        if(type.equals("title")){
                            holder.setVisibility(R.id.picker_day_title_text,View.VISIBLE);
                            holder.setVisibility(R.id.picker_day_text,View.GONE);
                            holder.setText(R.id.picker_day_title_text,item.get("day").toString());
                        }else {
//                            System.out.println("item.get(\"day\").toString():"+item.get("day").toString());
                            holder.setVisibility(R.id.picker_day_title_text,View.GONE);
                            holder.setVisibility(R.id.picker_day_text,View.VISIBLE);
                            holder.setText(R.id.picker_day_text, item.get("day").toString());
                            if(item.get("isselected").toString().equals("1")){
                                holder.setTextColor(R.id.picker_day_text, Color.BLUE);
                            }else{
                                holder.setTextColor(R.id.picker_day_text, Color.BLACK);
                            }
                            if(item.get("canchoose").toString().equals("0")){
                                holder.setTextColor(R.id.picker_day_text, Color.LTGRAY);
                            }
                        }
                    }
                };
                break;
//            case Params.MONTH_AND_DAY:
//                for (int i = 1;i<=12;i++){
//                    Map<String,String> map = new HashMap<>();
//                    map.put("month",i+"");
//                    if(i== Integer.parseInt(month)){
//                        map.put("isselected","1");
//                    }else {
//                        map.put("isselected", "0");
//                    }
//                    recycler_list.add(map);
//                }
//                gridLayoutManager = new GridLayoutManager(context, 4, OrientationHelper.VERTICAL, false);
//
//                adapter = new CommonRecylerAdapter(context, R.layout.item_for_picker_recycler, recycler_list, new ItemTouchListener() {
//                    @Override
//                    public void onItemClick(Map map, int position) {
//                        month = map.get("month").toString();
//                        for(int i = 0;i<recycler_list.size();i++){
//                            Map<String,String> item = recycler_list.get(i);
//                            String isselected = item.get("isselected");
//                            if(isselected.equals("1")){
//                                recycler_list.get(i).put("isselected","0");
//                                adapter.notifyItemChanged(i);
//                                break;
//                            }
//                        }
//                        recycler_list.get(position).put("isselected","1");
//                        adapter.notifyItemChanged(position);
//                        onDateSelected.dateselected(year,month,day);
//                    }
//
//                    @Override
//                    public void onItemClick(Object date, int position) {
//
//                    }
//                }) {
//                    @Override
//                    public void setDate(RecyclerViewHolder.ViewHolder holder, Map item,  int position) {
//                        holder.setText(R.id.picker_day_text,item.get("month").toString());
//                        if(item.get("isselected").toString().equals("1")){
//                            holder.setTextColor(R.id.picker_day_text, Color.BLUE);
//                        }
//                    }
//                };
//                break;
        }
        return gridLayoutManager;
    }


    public String getWeek(int index){
        switch (index){
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
        }
        return "";
    }

    public List<Map<String,String>> getDayList(){
        String temp_year = year;
        String temp_month = month;
        String temp_day = day;

        if (type == MONTH_AND_DAY && last_type == DAY ){
            if(is_start_choose){
                temp_year = start_year.equals("")?year:start_year;
                temp_month = start_month.equals("")?month:start_month;
                temp_day = start_day.equals("")?day:start_day;
            }else{
                temp_year = end_year.equals("")?year:start_year;
                temp_month = end_month.equals("")?month:end_month;
                temp_day = end_day.equals("")?day:end_day;
            }
        }

        List<Map<String,String>> list = new ArrayList<>();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Tools.string2int(temp_year), Tools.string2int(temp_month)-1,1);
        int startOfMonth = tempCalendar.get(Calendar.DAY_OF_WEEK);//1 星期天 7 星期六
        int countOfDay = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 0;i<7;i++){
            Map<String,String> map = new HashMap<>();
            map.put("day",getWeek(i));
            map.put("type","title");
            map.put("isselected", "0");
            map.put("canchoose","0");
            list.add(map);
        }
        if(startOfMonth>0){
            for (int i = 1;i< startOfMonth;i++){
                Map<String ,String> map = new HashMap<>();
                map.put("day","");
                map.put("type","null");
                map.put("isselected", "0");
                map.put("canchoose","0");
                list.add(map);
            }

        }
        for (int i=1;i<=countOfDay;i++){
            Map<String ,String> map = new HashMap<>();
            map.put("day",i+"");
            map.put("type","day");
            if(i== Integer.parseInt(temp_day)){
                map.put("isselected","1");
            }else {
                map.put("isselected", "0");
            }
            map.put("canchoose",canChoose(temp_year,temp_month,i));
            list.add(map);
        }
        if(list.size()%7>0){
            for (int i = 0;i< (7-(list.size()%7));i++){
                Map<String ,String> map = new HashMap<>();
                map.put("day","");
                map.put("type","null");
                map.put("isselected", "0");
                map.put("canchoose","0");
                list.add(map);
            }
        }

        return list;
    }

    public String canChoose(String year, String month, int day){
        if (needCanChoose()){
            Calendar c1 = Calendar.getInstance();
            c1.set(Integer.parseInt(year),Integer.parseInt(month),day);
            if(is_start_choose){
                if(end_day.equals("")) return "1";

                Calendar c2 = Calendar.getInstance();
                c2.set(Integer.parseInt(end_year),Integer.parseInt(end_month),Integer.parseInt(end_day));

                if(c1.compareTo(c2) <= 0){
                    return "1";
                }else{
                    return "0";
                }
            }else{
                System.out.println("start day :" + start_day);
                if(start_day.equals("")) return "1";
                Calendar c2 = Calendar.getInstance();
                c2.set(Integer.parseInt(start_year),Integer.parseInt(start_month),Integer.parseInt(start_day));
                System.out.println("c1:"  + c1.getTime());
                System.out.println("c2:"  + c2.getTime());
                System.out.println("c1:c2:"  + c1.compareTo(c2));
                if(c1.compareTo(c2) >= 0){
                    return "1";
                }else{
                    return "0";
                }
            }
        }else{
            return "1";
        }
    }

    public boolean needCanChoose(){
        if (type == MONTH_AND_DAY && last_type == DAY ){
            return true;
        }
        if(hasSetEndDate || hasSetStartDate){
            return true;
        }
        return false;
    }

}
