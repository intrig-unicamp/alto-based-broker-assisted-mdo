package edu.unicamp.intrig.p5gex.altoBase.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UtilsFunctions {

    public static String exceptionToString(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
