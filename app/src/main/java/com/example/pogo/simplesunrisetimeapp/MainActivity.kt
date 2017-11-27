package com.example.pogo.simplesunrisetimeapp

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.Buffer
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    protected fun getSunset(view: View) {
        var city = etCityName.text.toString()
        if (city == ""){
            return
        }
        val url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D\"$city\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg p0: String?): String {

            var content = StringBuilder()
            val url = URL(p0[0])
            val urlConnect = url.openConnection() as HttpURLConnection

//                urlConnect.connectTimeout = 7000
            val inputStreamReader = InputStreamReader(urlConnect.inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var line = ""

            try {
                do{
                    line = bufferedReader.readLine()
                    if(line != null){
                        content.append(line)
                    }
                }while(line != null)
                bufferedReader.close()
            }catch (e:Exception){
                Log.d("AAA",e.toString())
            }
            println(content)
            return content.toString()

        }


        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            var json = JSONObject(result)
            val query = json.getJSONObject("query")
            val results = query.getJSONObject("results")
            val channel = results.getJSONObject("channel")
            val astronomy = channel.getJSONObject("astronomy")
            var sunrise =  astronomy.getString("sunrise")

            Toast.makeText(applicationContext,sunrise,Toast.LENGTH_LONG).show()

            tvSunsetTime.text = "Sunrise time is $sunrise"

        }

    }
//    fun ConvertStreamToString(inputStream: InputStream) : String{
//
//        val bufferReader = BufferedReader(InputStreamReader(inputStream))
//        var line:String
//        var allString = ""
//
//        try {
//            do{
//                line = bufferReader.readLine()
//                if(line != null){
//                    allString += line
//                }
//            }while (line != null)
//
//            bufferReader.close()
//
//
//
//        }catch (ex: Exception){}
//
//        return allString
//
//    }


}
