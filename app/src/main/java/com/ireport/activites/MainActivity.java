package com.ireport.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.util.StringUtils;
import com.ireport.R;

import java.util.HashMap;
import java.util.Map;

import static android.R.id.content;

public class MainActivity extends AppCompatActivity {

    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
        // Set your request method, path, query string parameters, and request body
        final String method = "POST";
        final String path = "/items";
        final String body = "{\"someParameter\":\"someValue\"}";
        final Map<String, String> queryStringParameters = new HashMap<String, String>();
        final Map<String, String> headers = new HashMap<String, String>();

        //Construct the request
        final ApiRequest request =
                new ApiRequest(this.getClass().getSimpleName())
                        .withPath(path)
                        .withHttpMethod(HttpMethodName.valueOf(method))
                        .withHeaders(headers)
                        .addHeader("Content-Type"s "application/json")
                        .addHeader("Content-Length", String.valueOf(content.length))
                        .withParameters(queryStringParameters)
                        .withBody(content);

        // Create an instance of your custom SDK client
        final AWSMobileClient mobileClient = AWSMobileClient.defaultMobileClient();
        final CloudLogicAPI client = mobileClient.createAPIClient(MyAPImobilehubClient.class);
        final byte[] content = body.getBytes(StringUtils.UTF8);


        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void initElements(){
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
    }

    public void signUp(){

    }
}
