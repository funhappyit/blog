package com.example.blog.util.func

import jakarta.servlet.http.HttpServletResponse
import java.io.PrintWriter

fun responseData(resp:HttpServletResponse, jsonData:String?){
    val out: PrintWriter
    println("응답 데이터: $jsonData")
    resp.setHeader("Content-Type", "application/json; charset=utf-8")

    try{
        out = resp.writer
        out.println(jsonData)
        out.flush() // 버퍼 비우기
    }catch(e:Exception){
        e.printStackTrace()
    }
}