package com.example.podgotovka_modul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.OutputStreamWriter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.net.Socket
import android.os.AsyncTask

class RegActivity : AppCompatActivity() {
    private var number_reader_ticket_editText: EditText? = null
    private var password_editTxt: EditText? = null
    private var socket: Socket? = null
    private var out: PrintWriter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)
        number_reader_ticket_editText = findViewById(R.id.number_reader_ticket_editText)
        password_editTxt = findViewById(R.id.password_editText)
        val button_reg: Button = findViewById(R.id.reg_button)
        button_reg.setOnClickListener {
            reg_user(it)
        }
    }

    fun reg_user(view: View) {
        val number = number_reader_ticket_editText?.text.toString()
        val password = password_editTxt?.text.toString()
        val file = File(filesDir, "Users.json")
        val stringJson = if (file.exists()) {
            file.bufferedReader().use {
                it.readText()
            }
        } else {
            "{\"users\":[]}"
        }
        val jsonObject = JSONObject(stringJson)
        val jsonArray = jsonObject.getJSONArray("users")
        val newUsers = JSONObject().apply {
            put("Id", jsonArray.length())
            put("Login", number)
            put("Password", password)
            put("Admin", false)
        }
        sendUserDataToServer(newUsers.toString())
        jsonArray.put(newUsers)
        jsonObject.put("users", jsonArray)

        try {
            val write = BufferedWriter(FileWriter(file))
            write.write(jsonObject.toString())
            write.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendUserDataToServer(userData: String) {
        Thread {
            try {
                if (socket == null || socket?.isClosed == true) {
                    socket = Socket("192.168.1.103", 8080)
                    out = PrintWriter(OutputStreamWriter(socket?.getOutputStream()))
                }
                out?.println(userData)
                out?.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        out?.close()
        socket?.close()
    }
}
