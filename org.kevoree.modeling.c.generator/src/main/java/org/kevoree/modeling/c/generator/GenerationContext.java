package org.kevoree.modeling.c.generator;

import java.io.File;

public class GenerationContext {

    private String root = "";
    private File eCore = null;
    private File generationDirectory = null;
    private File framework = null;

    private boolean debug_model = false;

    private String versionMicroFramework = "";
    private String version = "";

    public void setRoot(String root) throws Exception {
        if (!(new File(this.root + root)).isDirectory())
            throw new Exception("The root directory does not exist.");
        this.root = root;
    }

    public File getECore() {
        return this.eCore;
    }

    public void setECore(String eCore) throws Exception {
        if (!(new File(this.root + eCore)).exists())
            throw new Exception("The eCore file is empty.");
        this.eCore = new File(this.root + eCore);
    }

    public void setFrameworkDirectory(String framework) throws Exception {
        if (!(new File(this.root + framework)).isDirectory())
            throw new Exception("The framework directory does not exist.");
        this.framework = new File(this.root + framework);
    }

    public File getFramework() {
        return this.framework;
    }

    public File getGenerationDirectory() {
        return this.generationDirectory;
    }

    public void setGenerationDirectory(String output) {
        this.generationDirectory = new File(this.root + output);
        if (!this.generationDirectory.isDirectory())
            this.generationDirectory.mkdir();
    }

}
