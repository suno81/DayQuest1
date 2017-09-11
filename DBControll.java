package com.example.nklee.myapplication;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DBControll extends StringRequest {

    static private String URL_Login = "http://zoro49.cafe24.com/DB_dayquest/login.php";
    static private String URL_Join = "http://zoro49.cafe24.com/DB_dayquest/join.php";
    private Map<String, String> parameters;

    public DBControll(String user, String password, Response.Listener<String> listener) {
        // Login
        super(Method.POST, URL_Login, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
        parameters.put("password", password);
    }

    public DBControll(String user, String password, String name, Response.Listener<String> listener) {
        //Join
        super(Method.POST, URL_Join, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", user);
        parameters.put("password", password);
        parameters.put("name", name);
        //parameters.put("point", "0");
    }

    @Override
    public Map<String, String> getParams(){return parameters;}
}
