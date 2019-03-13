package com.dm.client.informationcentre

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dm.client.CompassDirection
import com.dm.client.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.atan2

class InformationCentreActivity : AppCompatActivity(), InformationCentrePresenter.Contract {


    private lateinit var newsList: RecyclerView
    private lateinit var presenter: InformationCentrePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informationcentre)

        newsList = findViewById(R.id.News_Recyclerview)
        newsList.layoutManager = LinearLayoutManager(this)

        presenter = InformationCentrePresenter(this, this)

        //val compass = CompassDirection()
        //val angle = compass.calc(11.694640f, 75.820944f, 12.268006f, 75.266447f)
        //Toast.makeText(this, angle.toString(), Toast.LENGTH_LONG).show()

        val newsview = InformationCentrePresenter(this, this)
        newsview.getNews()
    }

    override fun onListReady(list: ArrayList<NewsItem>) {
        newsList.adapter = NewsListAdapter(list)

    }


}





