package com.example.pratush.docfinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pratush.docfinder.R;
import com.example.pratush.docfinder.helpers.TaskCallBack;
import com.example.pratush.docfinder.models.CategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//to set the user id with the cat id
//what kind of doctor
public class DocTypeActivity extends Activity {

    ArrayList<String> cat_list= new ArrayList<String>();
    ArrayList<Integer> catID_list= new ArrayList<Integer>();
    Integer selectedcatID;
    TextView c,q,addr;
    ListView cat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_type);

        c = (TextView) findViewById(R.id.consult);
        q = (TextView) findViewById(R.id.qual);
        cat = (ListView) findViewById(R.id.setdoclist);
        addr= (TextView) findViewById(R.id.address);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final EditText search = (EditText) findViewById(R.id.setdoctext);
        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                cat_list.clear();
                catID_list.clear();

                if(s.length()==0)
                    cat.setVisibility(View.GONE);
                else if (s.length() > 0) {


                    CategoryModel obj = new CategoryModel();
                    obj.cat = s.toString();

                    obj.searchAsync(new TaskCallBack() {
                        @Override
                        public void onSuccess(Object response) throws JSONException {
                            JSONArray res = new JSONArray(response.toString());

                            for (int i = 0; i < res.length(); i++) {
                                JSONObject resobj = (JSONObject) res.get(i);
                                cat_list.add(resobj.getString("cat"));

                                catID_list.add(resobj.getInt("catID"));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(search.hasFocus())   //if doctor is on another edittext
                                    cat.setVisibility(View.VISIBLE);
                                    else
                                        cat.setVisibility(View.GONE);
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.cat_list_item, cat_list);
                                    cat.setAdapter(arrayAdapter);
                                    cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        public void onItemClick(AdapterView parent, View v, int position, long id) {

                                            selectedcatID=catID_list.get(position);
                                            search.setText(cat_list.get(position));
                                            c.requestFocus();
                                            cat.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });

                        }

                        public void onError(Object response)
                        {

                        }
                    });
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });



    }

    public void next(View view)
    {

        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        i.putExtra("DoctypeID",selectedcatID);
        i.putExtra("consult",Integer.parseInt(c.getText().toString()));
        i.putExtra("qual",q.getText().toString());
        i.putExtra("addr",addr.getText().toString());
        i.putExtra("User","doc");
        startActivity(i);
    }
}
