# KMFC Generator

Since 0.4 4117ce860422f2c3e7b4201959f86ede412433ba we use an internal representation.

The two entry point of the program are `org.kevoree.modeling.c.generator.Test` for in IDE launch and
`org.kevoree.modeling.c.generator.App` for CLI usage.

They both have to create a context, GenerationContext, to specify the location of the meta-model to parse, where
to look for the micro framework and where to generate the code.

The generator will then loop over the meta-model and create a `Classifier` instance for every class.

For each and every class several `Function`, some with `Parameter`s, and `Variable`s.

At the end of the first loop the internal representation is complete, we now have to produce code.

The `Serializer` will loop over the IR and produce both header file and implementation file.