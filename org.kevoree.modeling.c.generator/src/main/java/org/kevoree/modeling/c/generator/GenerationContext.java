package org.kevoree.modeling.c.generator;

import java.io.File;

public class GenerationContext {

    private File eCore = null;
    private File generationDirectory = null;
    private File framework = null;

    private boolean debug_model = false;

    private String versionMicroFramework = "";
    private String version = "";

    public File getECore() {
        return this.eCore;
    }

    public void setECore(String eCore) throws Exception {
        if (!(new File(eCore)).exists())
            throw new Exception("The eCore file does not exist.");
        this.eCore = new File(eCore);
    }

    public void setFrameworkDirectory(String framework) throws Exception {
        if (!(new File(framework)).isDirectory())
            throw new Exception("The framework directory does not exist.");
        this.framework = new File(framework);
    }

    public File getFramework() {
        return this.framework;
    }

    public File getGenerationDirectory() {
        return this.generationDirectory;
    }

    public void setGenerationDirectory(String output) {
        this.generationDirectory = new File(output);
        if (!this.generationDirectory.isDirectory())
            this.generationDirectory.mkdir();
    }

}
