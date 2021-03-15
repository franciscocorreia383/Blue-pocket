package com.example.bluepocket.view.activities


import android.content.Intent
import android.drm.DrmRights
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.bluepocket.R
import com.example.bluepocket.model.Movimentation
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.DecimalFormat
import android.util.Log
import android.widget.ImageView
import com.google.firebase.database.*


class GraphActivity : AppCompatActivity() {

    private lateinit var mpLineChart: LineChart;
    private val movimentations = mutableListOf<Movimentation>()

    var colorVariant = intArrayOf(Color.GREEN, Color.RED)
    var legendName = arrayOf<String>("Receitas", "Despesas")
    lateinit var firebaseDatabase:FirebaseDatabase
    lateinit var myRef2: DatabaseReference
    var movimentationList = arrayListOf<Movimentation>()
    val dataVals1 = ArrayList<Entry>()
    val dataVals2 = ArrayList<Entry>()
    private lateinit var mButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph);

        mButton =findViewById(R.id.grafico_backspace)

        mButton.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        // myRef = firebaseDatabase.getReference("ChartValues")
        myRef2 = firebaseDatabase.getReference("movimentation")

        myRef2.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.value
                val perguntasChild = dataSnapshot.children

                perguntasChild.forEach {
                    it.getValue(Movimentation::class.java)?.let { movimentation ->
                        movimentations.add(movimentation)


                    }
                }

                var sortedData = movimentations.sortedWith(compareBy({it.date}))

                for ( i in sortedData){
                    if (!i.expense) {
                        dataVals1.add(Entry(
                            i.date.split("/")[0].toFloat(),
                            i.value.toFloat()))
                    }else if(i.expense){
                        dataVals2.add(Entry(
                            i.date.split("/")[0].toFloat(),
                            i.value.toFloat()))
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                //print error.message
            }
        })


        mpLineChart = findViewById(R.id.line_chart)
        val lineDataSet1 = LineDataSet(dataVals1, "Receitas")
        val lineDataSet2 = LineDataSet(dataVals2, "Despesas")
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet1)
        dataSets.add(lineDataSet2)
        val data = LineData(dataSets)


        mpLineChart.setDrawGridBackground(true)
        mpLineChart.setDrawBorders(true)
        mpLineChart.setBorderWidth(2F)


        lineDataSet1.setLineWidth(4F)
        lineDataSet1.setColor(Color.GREEN)
        lineDataSet2.setLineWidth(4F)
        lineDataSet2.setColor(Color.RED)
        lineDataSet1.setDrawCircles(true)
        lineDataSet1.setDrawCircleHole(true)
        lineDataSet2.setDrawCircles(true)
        lineDataSet2.setDrawCircleHole(true)
        lineDataSet1.setCircleColor(Color.GRAY)
        lineDataSet1.setCircleHoleColor(Color.BLUE)
        lineDataSet2.setCircleHoleColor(Color.BLUE)
        lineDataSet1.setValueTextSize(10F)
        lineDataSet2.setValueTextSize(10F)


        var descricao = Description()
        descricao.setText("Gráfico do Mês")
        descricao.setTextColor(Color.BLUE)
        descricao.setTextSize(10F)
        mpLineChart.setDescription(descricao)

        var legend = mpLineChart.getLegend()

        legend.setTextSize(15F)

        val legendEntries = arrayOfNulls<LegendEntry>(2)
        for (i in legendEntries.indices) {
            val entry = LegendEntry()
            entry.formColor = colorVariant[i]
            entry.label = legendName[i]
            legendEntries[i] = entry
        }
        legend.setCustom(legendEntries)

        val xAxis = mpLineChart.getXAxis()
        val yAxisLeft = mpLineChart.getAxisLeft()
        val yAxisRight = mpLineChart.getAxisRight()


        mpLineChart.getAxisLeft().setAxisMaxValue(100F);
        mpLineChart.getAxisLeft().setAxisMinValue(0F);
        mpLineChart.getAxisLeft().setLabelCount(25)

        mpLineChart.getAxisRight().setAxisMaxValue(100F);
        mpLineChart.getAxisRight().setAxisMinValue(0F);
        mpLineChart.getAxisRight().setLabelCount(25)


        mpLineChart.getXAxis().setAxisMaxValue(30f);
        mpLineChart.getXAxis().setAxisMinValue(0F);
        mpLineChart.getXAxis().setLabelCount(10)




        mpLineChart.setData(data)
        mpLineChart.invalidate()



        fun dataValues1(): ArrayList<Entry> {
            val dataVals = ArrayList<Entry>()

            for( i in movimentations ){
                Log.d("Tesao"+i, i.date.split("/")[0])
                //           Log.d("Tesao"+i, i.value.toFloat().toString())


            }
            return dataVals;


        }

        fun dataValues2(): ArrayList<Entry> {

            val dataVals = ArrayList<Entry>()
            dataVals.add(Entry(20f,30f))
            dataVals.add(Entry(5f,8f))
            dataVals.add(Entry(1f,6f))



            return dataVals;


        }

    }



}




















