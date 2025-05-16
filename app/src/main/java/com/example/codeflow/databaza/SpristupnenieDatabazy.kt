package com.example.codeflow.databaza

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase

object DatabaseProvider {
    private var instance: Databaza? = null

    fun getDatabase(context: Context): Databaza {
        return instance ?: Room.databaseBuilder(
            context.applicationContext,
            Databaza::class.java,
            "kod_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    instance?.let { database ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val rozhranie = database.kodDatabazoveRozhranie()
                            val prikladoveKody = listOf(
                                Kod(nazov = "PiApproximation", obsah = """
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
                                System.out.println("Approximate value of π: " + pi);
                            """.trimIndent()),
                                Kod(nazov = "HelloWorld", obsah = """                              
                                System.out.println("Hello, World!");
                            """.trimIndent()),
                                Kod(nazov = "Fibonacci", obsah = """
                                int n = 10;
                                int a = 0;
                                int b = 1;
                                for (int i = 0; i < n; i++) {
                                    System.out.print(a + " ");
                                    int sum = a + b;
                                    a = b;
                                    b = sum;
                                }
                            """.trimIndent()),
                                Kod(nazov = "Factorial", obsah = """
                                int fact = 1;
                                int n = 5;
                                for (int i = 1; i <= n; i++) {
                                    fact *= i;
                                }
                                System.out.println("Factorial of " + n + " is " + fact);
                            """.trimIndent()),
                                Kod(nazov = "SimpleLoop", obsah = """
                                for (int i = 0; i < 5; i++) {
                                    System.out.println("Iteration: " + i);
                                }
                            """.trimIndent()),
                                Kod(nazov = "FizzBuzz ", obsah = """
                                for (int i = 1; i <= 100; i++) {
                                    if (i % 15 == 0) {
                                        System.out.println("FizzBuzz");
                                        continue;
                                    }
                                    if (i % 3 == 0) {
                                        System.out.println("Fizz");
                                        continue;
                                    }
                                    if (i % 5 == 0) {
                                        System.out.println("Buzz");
                                        continue;
                                    }
                                    if (i > 50) {
                                        break;
                                    }
                                    System.out.println(i);
                                }
                            """.trimIndent()),
                                Kod(nazov = "Súčet párnych čísel (While, break)", obsah = """
                               int i = 0;
                               int sum = 0;
                               while (true) {
                                   i++;
                                   if (i % 2 != 0) {
                                       continue;
                                   }
                                   sum += i;
                                   System.out.println("Pridavam: " + i);
                                   if (i >= 20) {
                                       break;
                                   }
                               }
                               System.out.println("Suma parnych cisel: " + sum);
                            """.trimIndent()),
                                Kod(nazov = " Do-while cyklus so vstavaným if", obsah = """
                                int x = 5;
                                do {
                                    System.out.println("Aktualna hodnota x: " + x);
                                    if (x % 2 == 0) {
                                        System.out.println("Parne cislo");
                                    } 
                                    else {
                                        System.out.println("Neparne cislo");
                                    }
                                    x--;
                                } while (x > 0);
                            """.trimIndent()),
                                Kod(nazov = "Vnorený for cyklus + break vnútri", obsah = """
                                for (int i = 1; i <= 3; i++) {
                                    System.out.println("Vonkajsi cyklus i = " + i);
                                    for (int j = 1; j <= 5; j++) {
                                        if (j == 4) {
                                            System.out.println("Prerusujem vnutorny cyklus pri j = " + j);
                                            break;
                                        }
                                        System.out.println("  Vnutorny cyklus j = " + j);
                                    }
                                }
                            """.trimIndent()),
                                Kod(nazov = "Kombinácia všetkých – faktorál", obsah = """
                               int n = 6;
                               int result = 1;
                               for (int i = 1; i <= n; i++) {
                                   if (i == 4) {
                                       System.out.println("Preskakujem cislo 4");
                                       continue;
                                   }
                                   System.out.println("Nasobim s " + i);
                                   result *= i;
                               }
                               System.out.println("Vysledok (bez 4): " + result);
                            """.trimIndent()),
                                Kod(nazov = "ParOdd Loop - dlhý kód", obsah = """
                               int count = 0;
                               int max = 30;
                               int sum = 0;
                               for (int i = 1; i <= max; i++) {
                                   if (i % 2 == 0) {
                                       System.out.println(i + " je parne cislo.");
                                       sum += i;
                                       continue;
                                   }
                                   System.out.println(i + " je neparne cislo.");
                                   count++;
                                   if (count >= 10) {
                                       System.out.println("Dosiahli sme 10 neparnych cisel, koncim for cyklus.");
                                       break;
                                   }
                               }
                               int j = 5;
                               while (j > 0) {
                                   System.out.println("Hodnota j je: " + j);
                                   if (j == 3) {
                                       System.out.println("j je 3, preskakujem zmenu hodnoty.");
                                       j--;
                                       continue;
                                   }
                                   j -= 2;
                               }
                               int k = 0;
                               do {
                                   System.out.println("Hodnota k: " + k);
                                   if (k == 4) {
                                       System.out.println("k dosiahol 4, prerusujem do-while.");
                                       break;
                                   }
                                   k++;
                               } while (k < 10);
                               System.out.println("Suma parnych cisel: " + sum);
                               System.out.println("Pocet neparnych cisel: " + count);
                               System.out.println("Program ukonceny.");
                            """.trimIndent()),
                            )
                            prikladoveKody.forEach { kod ->
                                rozhranie.vlozKod(kod)
                            }
                        }
                    }
                }
            })
            .build()
            .also { instance = it }
    }
}