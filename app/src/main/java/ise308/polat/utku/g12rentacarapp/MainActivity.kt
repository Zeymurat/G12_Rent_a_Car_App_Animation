package ise308.polat.utku.g12rentacarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import ise308.polat.utku.g12rentacarapp.ui.AllCars
import ise308.polat.utku.g12rentacarapp.ui.InsertFragment
import ise308.polat.utku.g12rentacarapp.ui.RentCarFragment
import ise308.polat.utku.g12rentacarapp.ui.SearchFragment
import java.lang.Exception

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, Animation.AnimationListener {
    private var jsonSerializer: JSONSerializer? = null
    private lateinit var carList: ArrayList<Cars>
    private var recyclerViewCars: RecyclerView? = null
    private var carsAdapter: CarsAdapter? = null

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var fadeInAnimation : Animation
    private lateinit var fadeOutAnimation : Animation
    private lateinit var fadeInOutAnimation : Animation
    private lateinit var zoomInAnimation : Animation
    private lateinit var zoomOutAnimation : Animation
    private lateinit var leftRightAnimation : Animation
    private lateinit var rightLeftAnimation : Animation
    private lateinit var bottomTopAnimation : Animation
    private lateinit var bounceAnimation : Animation
    private lateinit var imageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsonSerializer = JSONSerializer("RentACar", applicationContext)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggleBar = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )

        drawerLayout.addDrawerListener(toggleBar)
        toggleBar.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AllCars())
            .commit()
        navigationView.setCheckedItem(R.id.nav_all_cars)


        try {
            carList = jsonSerializer!!.load()
        } catch (e: Exception) {
            carList = ArrayList()
        }

        val fabNewCar = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabNewCar.setOnClickListener {
            val newCarDialog = NewCarDialog()
            newCarDialog.show(supportFragmentManager, "123")
        }

        recyclerViewCars = findViewById<RecyclerView>(R.id.recyclerViewCars) as RecyclerView
        carsAdapter = CarsAdapter(carList, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewCars!!.layoutManager = layoutManager
        recyclerViewCars!!.itemAnimator = DefaultItemAnimator()
        recyclerViewCars!!.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerViewCars!!.adapter = carsAdapter

        val inflater = layoutInflater
        val removeCarDialog = inflater?.inflate(R.layout.note_frame, null)
        val buttonRemove = removeCarDialog.findViewById<Button>(R.id.button_remove)

        buttonRemove.setOnClickListener {
            carList.removeAt(1)
        }

        loadAnimations()
        fadeInAnimation.duration=1000
        fadeOutAnimation.duration=1000
        fadeInOutAnimation.duration=1000
        zoomInAnimation.duration = 1000
        leftRightAnimation.duration=1000
        rightLeftAnimation.duration=1000
        bottomTopAnimation.duration = 1000
        bounceAnimation.duration = 1000
        findViewById<Button>(R.id.animation_zoom_in_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_zoom_out_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_left_right_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_right_left_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_bounce_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_bottom_top_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_fade_in_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_fade_out_button)?.setOnClickListener(this)
        findViewById<Button>(R.id.animation_fade_both_button)?.setOnClickListener(this)


    }
    fun loadAnimations() {
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeInOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_out)
        zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        zoomOutAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
        leftRightAnimation = AnimationUtils.loadAnimation(this, R.anim.left_right)
        rightLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.right_left)
        bottomTopAnimation = AnimationUtils.loadAnimation(this, R.anim.top_bottom)
        bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun createNewCar(newCar: Cars) {
        carList.add(newCar)
    }

    fun deleteCar() {
        carList.removeAt(1)
        Toast.makeText(applicationContext, "We could not found", Toast.LENGTH_LONG).show()
    }

    fun searchCar(searchedKey: String) {
        var i = 0
        var flag = 0
        while (i < carList.size) {
            if (carList[i].carModel == searchedKey) {
                Toast.makeText(
                    applicationContext,
                    "We found, we have: " + carList[i].carModel + " and price is: " + carList[i].rentPrice,
                    Toast.LENGTH_LONG
                ).show()
                flag++
            }
            i++
        }
        if (flag == 0) {
            Toast.makeText(applicationContext, "We could not found", Toast.LENGTH_LONG).show()
        }


    }

    private fun saveCars() {
        try {
            jsonSerializer!!.save(this.carList!!)
        } catch (e: Exception) {
            //Log.e(TAG, "error loading notes :((")
        }
    }

    override fun onPause() {
        super.onPause()
        saveCars()
    }

    fun showNote(adapterPosition: Int) {
        val showCar = DialogShowCars()
        showCar.setCars(carList.get(adapterPosition))
        showCar.show(supportFragmentManager, "124")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add_new_car -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InsertFragment()).commit()
            R.id.nav_car_rent -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RentCarFragment()).commit()
            R.id.nav_car_search -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment()).commit()
            R.id.nav_all_cars -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AllCars()).commit()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View?) {
        imageView = findViewById(R.id.car_animated)
        when(v!!.id) {
            R.id.animation_fade_in_button -> {
                fadeInAnimation.setAnimationListener(this)
                imageView.startAnimation(fadeInAnimation)
            }
            R.id.animation_fade_out_button -> {
                fadeOutAnimation?.setAnimationListener(this)
                imageView.startAnimation(fadeOutAnimation)
            }
            R.id.animation_fade_both_button -> {
                fadeInOutAnimation.setAnimationListener(this)
                imageView.startAnimation(fadeInOutAnimation)
            }
            R.id.animation_zoom_in_button -> {
                zoomInAnimation.setAnimationListener(this)
                imageView.startAnimation(zoomInAnimation)
            }
            R.id.animation_zoom_out_button -> {
                zoomOutAnimation.setAnimationListener(this)
                imageView.startAnimation(zoomOutAnimation)
            }
            R.id.animation_left_right_button -> {
                leftRightAnimation.setAnimationListener(this)
                imageView.startAnimation(leftRightAnimation)
            }
            R.id.animation_right_left_button -> {
                rightLeftAnimation.setAnimationListener(this)
                imageView.startAnimation(rightLeftAnimation)
            }
            R.id.animation_bottom_top_button -> {
                bottomTopAnimation.setAnimationListener(this)
                imageView.startAnimation(bottomTopAnimation)
            }
            R.id.animation_bounce_button -> {
                bounceAnimation.setAnimationListener(this)
                imageView.startAnimation(bounceAnimation)
            }
        }
    }

    override fun onAnimationStart(p0: Animation?) {}

    override fun onAnimationEnd(p0: Animation?) {}

    override fun onAnimationRepeat(p0: Animation?) {}

}