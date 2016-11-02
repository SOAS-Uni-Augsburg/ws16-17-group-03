#!/usr/bin/python
import matplotlib.pyplot as plt
import numpy as np
def entropy(h):
	return - (h*np.log2(h) + (1-h)*np.log2(1-h))

h = np.arange(0.0,1.0, 0.01)
e = entropy(h)
	
plt.plot(h, e)


plt.xlabel('h')
plt.ylabel('entropy')
plt.title('Aufgabe 2c')
plt.grid(True)
plt.savefig("Aufgabe2c.png")
plt.show()

