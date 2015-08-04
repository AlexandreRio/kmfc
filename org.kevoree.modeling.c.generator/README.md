# KMFC Generator

Since version 0.4 (commit 4117ce860422f2c3e7b4201959f86ede412433ba) we use an Intermediate Representation.

The two entry point of the program are `org.kevoree.modeling.c.generator.Test` for an "in IDE launch" and
`org.kevoree.modeling.c.generator.App` for CLI usage.

They both have to create a context, `GenerationContext`, to specify: the location of the meta-model to parse, where
to look for the micro framework and where to generate the code.

The `Generator` will then loop over the meta-model and create a `Classifier` instance for every class.

For each and every classes several `Function`, some with `Parameter`s, and `Variable`s are created and linked to
the `Classifier`.

At the end of the first loop the intermediate representation is complete, we now have to produce code.

The `ClassSerializer` will loop over the IR and produce both header file and implementation file.

It's also the `Generator` role to produce the unit tests managed by the `TestSerializer`, the tests mostly check
for segfault and function null pointer rather than correct behavior.

Finally the `Generator` produces what we call the environment which consist in copying the hardcoded micro framework
and generating the cmake build file.