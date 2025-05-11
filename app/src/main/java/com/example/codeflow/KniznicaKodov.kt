package com.example.codeflow

class KniznicaKodov {
    val kody: MutableList<String> = mutableListOf()

    init {
        kody.add("""
        String q = "ahoj"; 
        int a = 5 * 6 - (12 / 4); 
        int b = 5 + 5; 
        if (5 < 4) {
            System.out.println("ahoj");
        } 
        else if (5 > 6) {
            System.out.println("ahoj");
        } 
        else if (5 == 6) {
            System.out.println("ahoj");
        } 
        a = 5 * b; 
        b = 19;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println(i + j);
                break;
            }
            int j = 0;
            continue;
        }
        int k = 0;
        while (k < 10) {
            k = k + 1;
            continue;
            System.out.println(k);
        }
        """.trimIndent())
    }
}