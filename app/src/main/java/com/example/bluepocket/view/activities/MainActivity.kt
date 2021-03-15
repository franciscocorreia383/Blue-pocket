package com.example.bluepocket.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bluepocket.R
import com.example.bluepocket.view.fragments.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // supportActionBar?.hide()


        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.main_toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = " "

         drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener (this)



        loadAllMovimentations()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_itens_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_movimentation_add) {
            startActivity(Intent(this, AddMovimentationActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when(id){

            R.id.drawer_menu_despesas -> {
                loadDespesasMovimentation()
            }
            R.id.drawer_menu_all -> {
                loadAllMovimentations()
            }
            R.id.drawer_menu_receitas -> {
                loadReceitasMovimentation()
            }
            R.id.drawer_menu_demonstrativo_receita -> {
                loadDemonstrativoReceita()
            }
            R.id.drawer_menu_demonstrativo_despesa -> {
                loadDemonstrativoDespesa()
            }
            R.id.grafico -> {
                startActivity(Intent(this,GraphActivity::class.java))
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadDemonstrativoDespesa() {
        val fragmentDemonstrativo = DemonstrativoDespesaFragment()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout_main_conteiner, fragmentDemonstrativo
            ).addToBackStack(null)
            .commit()
    }

    private fun loadDemonstrativoReceita() {
        val fragmentDemonstrativo = DemonstrativoReceitaFragment()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout_main_conteiner, fragmentDemonstrativo
            ).addToBackStack(null)
            .commit()
    }


    private fun loadReceitasMovimentation() {
        val fragmentReceitasListFragment = ReceitaListFragment()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout_main_conteiner, fragmentReceitasListFragment
            ).addToBackStack(null)
            .commit()    }

    private fun loadDespesasMovimentation() {
        val fragmentDespesasListFragment = DespesasListFragment()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout_main_conteiner, fragmentDespesasListFragment
            ).addToBackStack(null)
            .commit()
    }

    private fun loadAllMovimentations() {
        val fragmentAllMovimentationsList = ListAllMovimentationFragment()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout_main_conteiner,
                fragmentAllMovimentationsList
            ).addToBackStack(null)
            .commit()

    }
}


