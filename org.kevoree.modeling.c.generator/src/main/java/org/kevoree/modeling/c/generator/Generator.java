package org.kevoree.modeling.c.generator;

import org.apache.velocity.VelocityContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.kevoree.modeling.c.generator.model.ClassSerializer;
import org.kevoree.modeling.c.generator.model.Classifier;
import org.kevoree.modeling.c.generator.model.TestSerializer;
import org.kevoree.modeling.c.generator.model.Variable;
import org.kevoree.modeling.c.generator.utils.CheckerConstraint;
import org.kevoree.modeling.c.generator.utils.FileManager;
import org.kevoree.modeling.c.generator.utils.HelperGenerator;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.*;

public class Generator {

    /**
     * List of all created Classifier, key is the name of the Classifier.
     */
    public static Map<String, Classifier> classifiers;
    private GenerationContext context;
    private File eCoreFile;

    public Generator(GenerationContext ctx) {
        this.context = ctx;
        this.eCoreFile = ctx.getECore();
        classifiers = new HashMap<String, Classifier>();

        this.clean();
    }

    /**
     * Delete all files under the output directory.
     *
     * @see GenerationContext#generationDirectory
     */
    private void clean() {
        try {
            FileManager.delete(this.context.getGenerationDirectory());
        } catch (IOException e) {
            System.err.println("Error while cleaning output directory: " + e.getMessage());
        }
    }

    public void generateModel() throws Exception {
        URI fileUri = URI.createFileURI(eCoreFile.getAbsolutePath());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
        ResourceSetImpl rs = new ResourceSetImpl();
        XMIResource resource = (XMIResource) rs.createResource(fileUri);

        CheckerConstraint checkerConstraint = new CheckerConstraint(context);
        checkerConstraint.verify(resource);

        resource.load(null);
        EcoreUtil.resolveAll(resource);

        for (Iterator i = resource.getAllContents(); i.hasNext(); ) {
            EObject eo = (EObject) i.next();

            if (eo instanceof EClass) {
                Classifier c = Classifier.createFromEClass((EClass) eo);
                classifiers.put(((EClass) eo).getName(), c);
            }
        }

        for (Classifier c : classifiers.values()) {
            ClassSerializer.writeHeader(c, context);
            ClassSerializer.writeSource(c, context);
        }
    }

    private void copyFramework() throws IOException {
        File dest;
        for (File f : this.context.getFramework().listFiles()) {
            if (f.isDirectory()) {
                FileManager.copyDirectory(f, new File(this.context.getGenerationDirectory() +
                        File.separator + f.getName()));
            } else {
                dest = new File(this.context.getGenerationDirectory() + File.separator + f.getName());
                Files.copy(f.toPath(), dest.toPath());
            }
        }
    }

    /**
     * Generate a CMakeLists.txt file based on the meta-model and the hardcoded framework.
     * <p>
     * Source file are stored in sourceList along with framework files, except main files.
     * For every main files in the framework an executable is created.
     * <p>
     * To be considered as a main file it has to be named main*.c
     *
     * @throws IOException
     * @see Generator#classifiers
     * @see GenerationContext#framework
     * @see TemplateManager#gen_cmakelists
     */
    private void generateCMakeLists() throws IOException {
        String sourceList = "";
        String testList = "";
        List<String> mainFile = new ArrayList<String>();

        //TODO isn't the key the same?
        for (Classifier c : Generator.classifiers.values()) {
            sourceList += c.getName() + ".c ";
            if (!c.isAbstract())
                testList += c.getName() + "Test.c ";
        }
        for (File f : this.context.getFramework().listFiles()) {
            if (f.isFile() && f.getName().endsWith(".c") && !f.getName().startsWith("main"))
                sourceList += f.getName() + " ";
            else if (f.isFile() && f.getName().endsWith(".c") && f.getName().startsWith("main"))
                mainFile.add(f.getName());
        }

        VelocityContext context = new VelocityContext();
        context.put("source_list", sourceList);
        context.put("test_list", testList);
        context.put("main_list", mainFile);
        StringWriter result = new StringWriter();
        TemplateManager.getInstance().getGen_cmakelists().merge(context, result);
        FileManager.writeFile(this.context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "CMakeLists.txt", result.toString(), false);
    }

    private void generateKMFContainer() throws IOException {
        FileManager.writeFile(this.context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "KMFContainer.h", TemplateManager.getInstance().getKMFContainer(), false);
    }

    private void generateKevoreeBigHeader() throws IOException {
        String ret = "";
        ret += HelperGenerator.genIFDEF("kevoree");
        ret += "\n";
        for (String s : Generator.classifiers.keySet())
            ret += HelperGenerator.genIncludeLocal(s);
        ret += "\n";
        ret += HelperGenerator.genENDIF();

        FileManager.writeFile(this.context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "kevoree.h", ret, false);
    }

    private void generateDeserializer() throws IOException {
        String ret = "";
        int nbClass = Generator.classifiers.size();
        ret += "#define NB_CLASSES " + nbClass + "\n\n";

        for (Classifier c : Generator.classifiers.values()) {
            int nbVar = c.getVariables().size();
            for (String s : c.getAllSuperClass())
                if (!s.equals("KMFContainer"))
                    nbVar += Generator.classifiers.get(s).getVariables().size();
            ret += "#define " + c.getName() + "_NB_ATTR " + nbVar + "\n";
        }

        ret += "\n";
        ret += "typedef enum TYPE {\n";
        for (String s : Generator.classifiers.keySet())
            ret += "\t" + s.toUpperCase() + "_TYPE,\n";
        ret += "};\n";

        for (Classifier c : Generator.classifiers.values()) {
            List<Variable> allVars = new LinkedList<Variable>(c.getVariables());
            ret += "const struct at " + c.getName() + "_Attr[" + c.getName() + "_NB_ATTR] = {\n";
            for (String parent : c.getAllSuperClass())
                if (!parent.equals("KMFContainer"))
                    allVars.addAll(Generator.classifiers.get(parent).getVariables());

            for (Variable v : allVars) {
                String parser = "";
                String type = "";
                if (v.getLinkType() == Variable.LinkType.MULTIPLE_LINK) {
                    parser = "parseArray";
                    type = v.getType() + "_TYPE";
                } else if (v.getLinkType() == Variable.LinkType.UNARY_LINK) {
                    parser = "parseObject";
                    type = v.getType() + "_TYPE";
                } else if (v.getLinkType() == Variable.LinkType.PRIMITIVE) {
                    if (v.getType().equals("char*"))
                        parser = "parseStr";
                    else
                        parser = "parseBool";
                    type = "PRIMITIVE_TYPE";
                }

                ret += "{\"" + v.getName() + "\", " + parser + ", " + c.getName().toUpperCase() + "_TYPE, " + type + "},\n";
            }
            ret += "};\n\n";
        }

        ret += "const struct ClassType Classes[NB_CLASSES] = {\n";
        for (Classifier c : Generator.classifiers.values()) {
            ret += "\t{\n";
            ret += "\t\t.type = " + c.getName() + "_TYPE,\n";
            ret += "\t\t.attributes = &" + c.getName() + "_Attr,\n";
            ret += "\t\t.nb_attributes = " + c.getName() + "_NB_ATTR,\n";
            ret += "\t},\n";
        }
        ret += "};\n";

        FileManager.writeFile(this.context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "jsondeserializer.temp", ret, false);
    }

    private void generateTests() throws IOException {
        Map<String, Classifier> concreteClass = new HashMap<String, Classifier>();
        //generate test suites
        for (Classifier c : Generator.classifiers.values()) {
            if (!c.isAbstract()) {
                concreteClass.put(c.getName(), c);
                TestSerializer.writeHeader(c, context);
                TestSerializer.writeSource(c, context);
            }
        }

        //generate test runner
        VelocityContext context = new VelocityContext();
        context.put("classifiers", concreteClass);
        StringWriter result = new StringWriter();
        TemplateManager.getInstance().getGen_test_runner().merge(context, result);
        FileManager.writeFile(this.context.getGenerationDirectory().getAbsolutePath() + File.separator +
                "test_runner.c", result.toString(), false);
    }

    public void generateEnvironment() {
        try {
            this.copyFramework();
            this.generateCMakeLists();
            this.generateKMFContainer();
            this.generateKevoreeBigHeader();
            this.generateDeserializer();
            this.generateTests();
        } catch (IOException e) {
            System.err.println("Error while generating environment: " + e.getMessage());
        }
    }
}
