# KMFC

Rewrite of a cpp code generator from kevoree meta-model.
Originally written by [Jean-Emile](https://github.com/Jean-Emile) and located [here](https://github.com/kevoree/kmfcpp) and
based on the hand-crafted work of [kYc0o](https://github.com/kYc0o/kevoree-c-reloaded).

# Interest: model@runtime

The goal of kmfc is to provide a set of tools to dynamically deploy or reconfigure systems. Many other implementations exist
in high-level language but this version aims the IoT world.

What we call a system is a network, eventually of a mesh topology, and a node is a machine/computer/SystemOnChip connected to this network.

From a metamodel kmfc will generate what we call Kevoree, here we might even say Kevoree-c, it manipulates a model of the running system, it
details the nodes running, what component they ship, what version, communicating on what ports and such…

## Model based approach

Metamodels can be created in eCore format with “classic” tools such as Eclipse Modeling Framework.

See [KMF](http://kevoree.org/kmf/) for full documentation.

Models to deploy or update systems are usually in JSON format. Here is one [model example](https://github.com/AlexandreRio/kevoree-c-reloaded/blob/master/models/5nodes1component.json).

## Code generator

kmfc generates a data structure to hold information about the running system, and a set of tools to

* read a model in JSON format and recreate the corresponding instance in the data structure,
* export the state of the current system into JSON format,
* compare the current model with a new one,
* perform modifications based on this comparison.

All of these features are built with a spirit of optimization, but see section Prospect.

See `org.kevoree.modeling.c.generator` to see how its done and its [Readme](/org.kevoree.modeling.c.generator/README.md)
for further details.

# Workflow

![Workflow-schema](https://github.com/AlexandreRio/kmfc/blob/master/workflow.png)

# Progress

Kmfc currently produces a full structure of classes to store instances of models.
A set a unit tests is generated to check the behavior of every methods.

JSON export is fully working and “import“ from JSON is partially working (only 1-1 contained links aren't support).

You can also see the project [Roadmap](ROADMAP.md).

# Prospect

Since kmfc is a research tool IoT focused it aims optimization but to quickly get a working version some trade-offs were made.

If some are specified directly in the source code a full list of design choices will soon be made. One might want to modify or
adapt those choices to produce an even better Kevoree version and reduce even more the general overhead of this implementation.

The static version of Kevoree-c, which is what kmfc tries to reproduce dynamically, runs on m3 nodes in [FIT IoT-lab testbed](https://www.iot-lab.info/).


# About

kmfc is being developed in the context of my (Alexandre Rio) internship at [Irisa](http://irisa.fr).
You can find my report [here](https://github.com/AlexandreRio/rapport-de-stage-2015/)(in french).
