package com.example.codeflow

class KniznicaKodov {
    val kody: MutableList<String> = mutableListOf()

    init {
        kody.add("""
        String q = "ahoj"; 
        int a = 5 * 6 - (12 / 4); 
        int b = 5 + 5; 
        a = 5 * b; 
        b = 19;
        for (int i = 0; i < 10; i++) {
            a = a + 8;
            b = a;
            System.out.println(a + " ahojjj");
        }
        """.trimIndent())
    }
}