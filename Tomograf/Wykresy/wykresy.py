import matplotlib.pyplot as plt
import numpy as np
import sys
def plot1():
    #Liczba detektorów zmienna
    x = np.array([90,180,270,360,450,540,630,720])
    y = np.array([
        0.17363184311092342,
        0.17363184311092342,
        0.30806746738874735,
        0.331152007525297,
        0.3353641270973506,
        0.3580343356316612,
        0.3580343356316612,
        0.35149359807045394
    ])
    plt.plot(x, y, 'ro')
    plt.grid(True)
    # plt.xlim(90,720,90)
    plt.xlabel("Liczba detektorów")
    plt.ylabel("Błąd średniokwadratowy")
    plt.title("Liczba detektorów, a błąd średniokwadratowy")
    plt.savefig("LiczbaDetektorow.jpg", dpi = 72)
    plt.show()

def plot2():
    #Liczba skanów zmienna
    x = np.array([90,180,270,360,450,540,630,720])
    y = np.array([
        0.29830203025063967,
        0.25418988590136243,
        0.3389849417389246,
        0.37721812979121166,
        0.38004032228529216,
        0.36798068461800526,
        0.38635025960817165,
        0.383257486789202
    ])
    plt.plot(x, y, 'ro')
    plt.grid(True)
    plt.xlabel("Liczba skanów")
    plt.ylabel("Błąd średniokwadratowy")
    plt.title("Liczba skanów, a błąd średniokwadratowy")
    plt.savefig("LiczbaSkanow.jpg", dpi = 72)
    plt.show()

def plot3():
    #Rozpiętość wachlarza zmienna
    x = np.array([45,90,135,180,225,270])
    y = np.array([
        0.14474119259663606,
        0.21268040702738916,
        0.3371477552002176,
        0.3216264667999515,
        0.36338530612782177,
        0.34496575567097454
    ])
    plt.plot(x, y, 'ro')
    plt.grid(True)
    plt.xlabel("Rozpiętość wachlarza")
    plt.ylabel("Błąd średniokwadratowy")
    plt.title("Rozpiętość wachlarza, a błąd średniokwadratowy")
    plt.savefig("RozpietoscWachlarza.jpg", dpi = 72)
    plt.show()

plot1()
plot2()
plot3()