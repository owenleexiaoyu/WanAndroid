package cc.lixiaoyu.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cc.lixiaoyu.amz_uikit.titlebar.AMZTitleBar
import cc.lixiaoyu.amz_uikit.titlebar.OnAMZTitleBarListener

class MainActivity : AppCompatActivity() {

    private var titleBar: AMZTitleBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleBar = findViewById(R.id.title_bar)
        titleBar?.setOnTitleBarListener(object : OnAMZTitleBarListener {
            override fun onEndClick(v: View?) {
                Toast.makeText(this@MainActivity, "end click", Toast.LENGTH_SHORT).show()
            }

            override fun onStartClick(v: View?) {
                Toast.makeText(this@MainActivity, "start click", Toast.LENGTH_SHORT).show()
            }

            override fun onTitleClick(v: View?) {
                Toast.makeText(this@MainActivity, "title click", Toast.LENGTH_SHORT).show()
            }

        })
    }
}