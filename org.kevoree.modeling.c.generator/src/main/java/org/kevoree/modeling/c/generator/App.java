package org.kevoree.modeling.c.generator;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 28/10/13
 * Time: 11:04
 * To change this templates use File | Settings | File Templates.
 */
public class App {


    public static void main(String args[]) throws IOException {


        Options options = new Options();
        options.addOption("h", "help", false, "prints the help content");
        options.addOption(OptionBuilder
                .withArgName("file")
                .hasArg()
                .isRequired()
                .withDescription("eCore file")
                .withLongOpt("input")
                .create("i"));
        options.addOption(OptionBuilder
                .withArgName("framework")
                .hasArg()
                .isRequired()
                .withDescription("Framework directory")
                .withLongOpt("framework")
                .create("f"));
        options.addOption(OptionBuilder
                .withArgName("directory")
                .hasArg()
                .isRequired()
                .withDescription("Root Generation Directory")
                .withLongOpt("root")
                .create("t"));

        options.addOption(OptionBuilder
                .withArgName("debug")
                .hasArg()
                .withDescription("{true,false}")
                .withLongOpt("enable")
                .create("d"));


        try {
            CommandLineParser parser = new GnuParser();
            CommandLine cmd = parser.parse(options, args);

            //     InputStream ecorefile = new FileInputStream(cmd.getOptionValue("i"));

            OutputStream out = System.out;


            String eCore = cmd.getOptionValue("i");
            String path = cmd.getOptionValue("t");
            String framework = cmd.getOptionValue("f");
            Boolean debugmode = Boolean.parseBoolean(cmd.getOptionValue("d"));


            GenerationContext context = new GenerationContext();
            context.setGenerationDirectory(path);
            context.setECore(eCore);
            context.setFrameworkDirectory(framework);
//            context.setDebug_model(false);
//            context.setVersion("1.3");
//            context.setVersionmicroframework("1.3");

            Generator gen = new Generator(context);
            gen.generateModel();
            gen.generateEnvironment();
        } catch (MissingOptionException e) {

            boolean help = false;
            try {
                Options helpOptions = new Options();
                helpOptions.addOption("h", "help", false, "prints the help content");
                CommandLineParser parser = new PosixParser();
                CommandLine line = parser.parse(helpOptions, args);
                if (line.hasOption("h")) help = true;
            } catch (Exception ex) {
            }
            if (!help) System.err.println(e.getMessage());
            //Et oui commons-cli permet aussi d'affiche l'aide
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("App", options);
            System.exit(1);
        } catch (MissingArgumentException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("App", options);
            System.exit(1);
        } catch (ParseException e) {
            System.err.println("Error while parsing the command line: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
