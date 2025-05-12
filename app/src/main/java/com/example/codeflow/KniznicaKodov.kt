package com.example.codeflow

class KniznicaKodov {
    val kody: MutableList<String> = mutableListOf()

    init {
        kody.add("""
int terms = 100;
double pi = 0;
boolean addTerm = true;
for (int i = 1; i <= terms * 2; i += 2) {
    if (addTerm) {
        pi += 4.0 / i;
    } 
    else {
        pi -= 4.0 / i;
    }
    addTerm = !addTerm; 
}
System.out.println("Približná hodnota π je: " + pi);
        """.trimIndent())
    }
}
