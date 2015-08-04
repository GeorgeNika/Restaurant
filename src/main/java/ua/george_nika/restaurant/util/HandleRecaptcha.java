package ua.george_nika.restaurant.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by George on 28.07.2015.
 */
public class HandleRecaptcha {

//    6LducwoTAAAAAKyj9sOskLORPY9ajB736-lylByf      for 52.26.11.200
//    6LducwoTAAAAAHGdEfGvL3BFHQErRmra6cZDD7Lq

//    6LcUcwoTAAAAAFP87r0vher-5grWR5OM2GILuX-m    for localhost
//    6LcUcwoTAAAAAJc3ARmls7eevdtGsvT901r9WdwX

//    6LdMAgMTAAAAAGYY5PEQeW7b3L3tqACmUcU6alQf     from inet
//    6LdMAgMTAAAAAJOAqKgjWe9DUujd2iyTmzjXilM7

    static String secretParameter = "6LducwoTAAAAAHGdEfGvL3BFHQErRmra6cZDD7Lq";

    public static boolean isGoodCapcha(HttpServletRequest request, String reCaptcha) {

        URL url = null;
        try {
            url = new URL("https://www.google.com/recaptcha/api/siteverify?secret="
                    + secretParameter + "&response=" + reCaptcha + "&remoteip=" + request.getRemoteAddr());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String line, outputString = "";
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outputString += line;
            }

            CaptchaResponse capRes = new Gson().fromJson(outputString, CaptchaResponse.class);
            if (capRes.isSuccess()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
