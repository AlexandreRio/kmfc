package org.kevoree.modeling.c.generator;

import java.io.File;

public class GenerationContext {

    private String root = "";
    private File eCore = null;
    private String generationDirectory = "";
    private String framework = "";

    private boolean debug_model = false;

    private String versionMicroFramework = "";
    private String version = "";

    public void setRoot(String root) {
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

    public String getFramework() {
        return this.framework;
    }

    public void setFramework(String framework) {
        this.framework = framework;
    }

    public String getGenerationDirectory() {
        return this.root + this.generationDirectory;
    }

    public void setGenerationDirectory(String output) {
        this.generationDirectory = output;
    }
}
