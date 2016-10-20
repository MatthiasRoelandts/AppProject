package com.example.matth.finalapp;

import android.os.AsyncTask;

import com.example.matth.finalapp.objects.Itemcategory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by matth on 17/10/2016.
 */

public class ExpandableMenuPump extends BaseActivity{
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        cricket.add("Pakistan");
        cricket.add("Australia");
        cricket.add("England");
        cricket.add("South Africa");

        List<String> football = new ArrayList<String>();
        football.add("Brazil");
        football.add("Spain");
        football.add("Germany");
        football.add("Netherlands");
        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia");

        expandableListDetail.put("CRICKET TEAMS", cricket);
        expandableListDetail.put("FOOTBALL TEAMS", football);
        expandableListDetail.put("BASKETBALL TEAMS", basketball);
        return expandableListDetail;
    }

    private void getItemCategories() {
        new AsyncTask<Void, Void, ArrayList<Object>>() {
            ArrayList<Object> responsArray = new ArrayList<Object>();
            @Override
            protected ArrayList<Object> doInBackground(Void... params) {
                final String url = "http://10.0.2.2:8080/ItemCategory/all";
                HttpStatus status = null;
                RestTemplate restTemplate = new RestTemplate();
                // Add the Jackson and String message converters
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<Itemcategory[]> response = null;
                try {
                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", getAuthToken());
                    HttpEntity request = new HttpEntity(headers);
                    response = restTemplate.exchange(url, HttpMethod.GET, request, Itemcategory[].class);
                    status = response.getStatusCode();
                    List<Itemcategory> list = new ArrayList<Itemcategory>();
                    for(Itemcategory category: response.getBody()) {
                        //Log.e("Category:", category.getName());
                        list.add(category);
                    }
                    responsArray.add(list);
                } catch (HttpClientErrorException e){
                    if(e.getStatusCode() == HttpStatus.CONFLICT){
                        status = HttpStatus.CONFLICT;
                    }
                }catch(ResourceAccessException e){
                    status = HttpStatus.BAD_REQUEST;
                }
                responsArray.add(status);
                return responsArray;
            }

            @Override
            protected void onPostExecute(ArrayList<Object> objects) {
                if((HttpStatus) objects.get(1) == HttpStatus.OK){
                    //fillCategorySpinner((List<Itemcategory>) objects.get(0));
                }
                super.onPostExecute(objects);
            }
        }.execute();
    }
}
