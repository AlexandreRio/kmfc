# KMFC

Rewrite of a cpp code generator from kevoree meta-model.
Originally written by [Jean-Emile](https://github.com/Jean-Emile) and located [here](https://github.com/kevoree/kmfcpp).

# Interest

The goal of the kmfc is to provide a set of tools to dynamically deploy or reconfigure systems. Many other implementations exist
in high-level language but this version aims the IoT world.

## Class generator

See `org.kevoree.modeling.c.generator` to know how the classes generation is done and its [Readme](/org.kevoree.modeling.c.generator/README.md)
for further reading.

# Progress

We currently produce a full structure of classes to store instance of models.
A set a unit tests is generated to check the behavior of every methods.

You can also see the project [Roadmap](ROADMAP.md).

## Manipulation library

See `org.kevoree.modeling.c.generator/src/main/resources/hardcoded/` to see some "in action" usage.

A real micro framework is still on its way.

# About

kmfc is being developped in the context of my (Alexandre Rio) internship at [Irisa](http://irisa.fr).
You can find my report [here](https://github.com/AlexandreRio/rapport-de-stage-2015/)(in french).
