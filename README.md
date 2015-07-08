# KMFC

Rewrite of a cpp code generator from kevoree metal-model.
Original write is by [Jean-Emile](https://github.com/Jean-Emile) and is located [here](https://github.com/kevoree/kmfcpp).

## Class generator

See `org.kevoree.modeling.c.generator` for class generation and its [Readme](/org.kevoree.modeling.c.generator/README.md)
for further reading.

### Progress

We still have to decide whether lots of code is necessary for a C implementation or not. This can lead to a massive refactoring.

Initial project didn't have a proper coding style, lots of clean up to do.

## Manipulation library

See `org.kevoree.modeling.cpp.microframework` for tools to manipulate the classes (still in cpp, so unusable).