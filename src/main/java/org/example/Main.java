package org.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
    String API = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    Scanner sc = new Scanner(System.in);
    System.out.println("Введите название организации режим работы которой вы хотите узнать:");
    String nameOfOrg=sc.nextLine().replace(" ","+");

    HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://search-maps.yandex.ru/v1/?apikey="+API+"&text="+nameOfOrg+"&lang=ru_RU&type=biz")).GET().build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    JSONObject jo = new JSONObject(response.body());
    JSONArray res = jo.getJSONArray("features");

    if (res.length() == 0) {
      System.out.println("404 not found");
    } else {

    System.out.println("Всего нашлось "+res.length()+" организаций"); System.out.println("");
    for (Object org : res){
      String timeOfWork = null;

      JSONObject out = ((JSONObject)org).getJSONObject("properties").getJSONObject("CompanyMetaData");
      String nameOfOrgOut = out.getString("name");
      String address = out.getString("address");
      try {
        timeOfWork = out.getJSONObject("Hours").getString("text");
      } catch (JSONException e) {
        timeOfWork = "не известен";
      }
      System.out.println("Название организации: " + nameOfOrgOut);
      System.out.println("Адрес организации: " + address);
      System.out.println("Режим работы: " + timeOfWork);
      System.out.println("");
    }
    }
  }
}
