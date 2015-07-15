package org.kevoree.modeling.c.generator.utils;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 09/10/13
 * Time: 13:29
 * To change this templates use File | Settings | File Templates.
 */
public class HelperGenerator {

    public static final String internal_id_name  = "generated_KMF_ID";

    public static String genInclude(String name){
        return   "#include <" + name + ">\n";
    }


    public static String genIncludeLocal(String name){
        return   "#include \"" + name + ".h\"\n";
    }

    public static String genToLowerCaseFirstChar(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    public static String genToUpperCaseFirstChar(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static String genDefaultValue(String type) {
        String defVal = "NULL";
        if (type.equals("EString")) {
            defVal = "NULL";
        }
        else if (type.equals("EBoolean")) {
            defVal = "false";
        }
        else if (type.equals("EIntegerObject")) {
            defVal = "-1";
        }
        return defVal;
    }

    public static String genIFDEF(String name){
        return "#ifndef __"+name+"_H\n" +
                "#define __"+name+"_H\n";
    }

    public static String genENDIF() {
        return "#endif\n";
    }
}
