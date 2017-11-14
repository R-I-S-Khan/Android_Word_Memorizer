package com.risk.dolist;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //ArrayList<String> items;
    ArrayList<TaskCauseItem> items;
    ListView lvItems;
    ImageButton imgbAdd, imgbRefresh;
    EditText etTask,etCause;
    TextView tvSelection;
    DBhelper db;

    NewAdapter adapter;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);
        imgbAdd = (ImageButton) findViewById(R.id.imgbAdd);
        imgbRefresh = (ImageButton)findViewById(R.id.imgbRefresh);
        etTask = (EditText) findViewById(R.id.etTask);
        etCause = (EditText)findViewById(R.id.etCause);
        tvSelection = (TextView)findViewById(R.id.tvSelection);

        //items = new ArrayList<String>();
        items = new ArrayList<TaskCauseItem>();
        //items.add(new TaskCauseItem("Task 1","Cause 1"));
        //items.add(new TaskCauseItem("Task 2","Cause 2"));
        //items.add(new TaskCauseItem("Task 3","Cause 3"));

        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        //lvItems.setAdapter(adapter);

        adapter = new NewAdapter();
        lvItems.setAdapter(adapter);
        db = new DBhelper(this);
        //Refresh(adapter);
        requestQueue = Volley.newRequestQueue(this);


        /*StringRequest jor = new StringRequest(Request.Method.POST, "http://192.168.0.103/amaderApp/test.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    tvSelection.setText("Sum is: " + j.getString("sum"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvSelection.setText(error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put("A","10");
                p.put("B", "15");
                return p;
            }
        };


        requestQueue.add(jor);*/

        imgbRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh(adapter);
            }
        });

        imgbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String s1 = etTask.getText().toString();
                final String s2 = etCause.getText().toString();
                //StringRequest jor = new StringRequest(Request.Method.POST, "http://192.168.1.13/amaderApp/insert.php",
                //StringRequest jor = new StringRequest(Request.Method.POST, "http://192.168.0.103/amaderApp/insert.php",
                StringRequest jor = new StringRequest(Request.Method.POST, "http://192.168.1.13/WordMemorizer/insert.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Refresh(adapter);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            tvSelection.setText(error.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> p = new HashMap<>();
                        p.put("task", s1);
                        p.put("cause", s2);
                        return p;
                    }
                };
                requestQueue.add(jor);
                //items.add(task);
                //adapter.notifyDataSetChanged();
                //items.add(new TaskCauseItem(task,cause));
                //adapter.notifyDataSetChanged();


                //db.insert(new TaskCauseItem(task,cause));
                //Refresh(adapter);

            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //tvSelection.setText(items.get(position));
                //tvSelection.setText(items.get(position).toString());
                Intent in = new Intent(MainActivity.this, ShowTaskCause.class);
                in.putExtra("word",items.get(position).getTask());
                in.putExtra("meaning",items.get(position).getCause());
                startActivity(in);
            }
        });

    }

    public class NewAdapter extends ArrayAdapter<TaskCauseItem>{

        public NewAdapter(){

            super(getApplicationContext(),R.layout.task_cause_item,items);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                v = getLayoutInflater().inflate(R.layout.task_cause_item, parent, false);
            }
            TextView tvTask = (TextView)v.findViewById(R.id.tvTask);
            TextView tvCause = (TextView)v.findViewById(R.id.tvCause);
            ImageButton imgbDelete = (ImageButton)v.findViewById(R.id.imgbDelete);

            tvTask.setText(items.get(position).task);
            tvCause.setText(items.get(position).cause);

            imgbDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //db.delete(items.get(position).id);
                    int id = items.get(position).id;
                    delete(id);
                    items.remove(position);
                    notifyDataSetChanged();
                }
            });
            imgbDelete.setFocusable(false);


            return v;
        }
    }

    void Refresh(final NewAdapter adapter){

        items.clear();
        //JsonArrayRequest jor  = new JsonArrayRequest(Request.Method.GET, "http://192.168.1.13/amaderApp/view.php", null,
        //JsonArrayRequest jor  = new JsonArrayRequest(Request.Method.GET, "http://192.168.0.103/amaderApp/view.php", null,
        JsonArrayRequest jor  = new JsonArrayRequest(Request.Method.GET, "http://192.168.1.13/WordMemorizer/view.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                            items.clear();
                            int len =  response.length();
                            for( int i =0; i<len; i++){
                                try {
                                    JSONObject j = response.getJSONObject(i);

                                    int id = j.getInt("id");
                                    String task = j.getString("task");
                                    String cause = j.getString("cause");
                                    items.add(new TaskCauseItem(id,task,cause));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvSelection.setText(error.toString());
            }
        });
        requestQueue.add(jor);
        //items.addAll(db.retrieve());
    }

    void delete(int id){
        final int _id= id;
        //StringRequest jor = new StringRequest(Request.Method.POST, "http://192.168.1.13/amaderApp/delete.php",
        //StringRequest jor = new StringRequest(Request.Method.POST, "http://192.168.0.103/amaderApp/delete.php",
        StringRequest jor = new StringRequest(Request.Method.POST, "http://192.168.1.13/WordMemorizer/delete.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvSelection.setText(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> p = new HashMap<>();
                p.put("id", String.valueOf(_id));
                return p;
            }
        };
        requestQueue.add(jor);
    }
}
